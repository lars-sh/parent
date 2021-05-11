package de.larssh.utils.text;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import de.larssh.utils.annotations.PackagePrivate;
import de.larssh.utils.io.PeekableReader;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * This class holds all information required to parse a CSV data stream and
 * encapsulates the parsing algorithm using {@link #parse()}.
 */
@ToString
@PackagePrivate
@RequiredArgsConstructor
class CsvParser {
	/**
	 * Asserts the validity of {@code separator} and {@escaper} as CSV control
	 * characters.
	 *
	 * @param separator the separator character
	 * @param escaper   the escaping character
	 * @throws IllegalArgumentException on illegal {@code separator} or
	 *                                  {@escaper} value
	 */
	@PackagePrivate
	@SuppressWarnings({ "PMD.AvoidLiteralsInIfCondition", "PMD.CyclomaticComplexity" })
	static void assertCsvInput(final char separator, final char escaper) {
		if (escaper == separator) {
			throw new IllegalArgumentException("The escape and separator characters must not be equal.");
		}
		if (escaper == '\r') {
			throw new IllegalArgumentException("The escape character must not be '\r'.");
		}
		if (escaper == '\n') {
			throw new IllegalArgumentException("The escape character must not be '\n'.");
		}
		if (separator == '\r') {
			throw new IllegalArgumentException("The separator character must not be '\r'.");
		}
		if (separator == '\n') {
			throw new IllegalArgumentException("The separator character must not be '\n'.");
		}
	}

	/**
	 * Tries to read a new line from {@code reader}. This method accepts either of
	 * {@code \n}, {@code \r\n} or {@code \r} as new line sequence.
	 *
	 * @param reader a {@link PeekableReader} as data input
	 * @return {@code true} if a new line was read., else {@code false}
	 * @throws IOException if an I/O error occurs
	 */
	@PackagePrivate
	static boolean readNewLine(final PeekableReader reader) throws IOException {
		if (!reader.hasNext()) {
			return false;
		}
		final char character = reader.peek();
		if (character != '\r' && character != '\n') {
			return false;
		}
		reader.next();
		if (character == '\r' && reader.hasNext() && reader.peek() == '\n') {
			reader.next();
		}
		return true;
	}

	/**
	 * The CSV separator character
	 */
	char separator;

	/**
	 * The CSV escaping character
	 */
	char escaper;

	/**
	 * Checks if {@character} is either the CSV separator character or a new line
	 * character.
	 *
	 * @param character the character to check
	 * @return {@code true} if {@character} is either the separator character or a
	 *         new line character, else {@code false}
	 */
	@PackagePrivate
	boolean isSeparatorOrNewLine(final char character) {
		return character == separator || character == '\r' || character == '\n';
	}

	/**
	 * Parses the CSV data given by {@code reader}, starting at the current
	 * position.
	 *
	 * @param reader a {@link Reader} as CSV data input
	 * @return an object representing the parsed CSV data
	 * @throws IllegalArgumentException on illegal {@code separator} or
	 *                                  {@escaper} value
	 * @throws IOException              if an I/O error occurs
	 */
	public Csv parse(final Reader reader) throws IOException {
		try (PeekableReader peekableReader = new PeekableReader(reader)) {
			return parse(peekableReader);
		}
	}

	/**
	 * Parses the CSV data given by {@code reader}, starting at the current
	 * position.
	 *
	 * @param reader a {@link PeekableReader} as CSV data input
	 * @return an object representing the parsed CSV data
	 * @throws IllegalArgumentException on illegal {@code separator} or
	 *                                  {@escaper} value
	 * @throws IOException              if an I/O error occurs
	 */
	private Csv parse(final PeekableReader reader) throws IOException {
		assertCsvInput(separator, escaper);

		final Csv csv = new Csv();

		// Make sure, an empty input results in an empty result with no row
		if (!reader.hasNext()) {
			return csv.unmodifiable();
		}

		List<String> currentRow = new CsvRow(csv, csv.size(), new ArrayList<>());
		csv.add(currentRow);
		while (reader.hasNext()) {
			if (readNewLine(reader)) {
				// Ignore a trailing new line
				if (!reader.hasNext()) {
					return csv.unmodifiable();
				}

				// Add new row
				currentRow = new CsvRow(csv, csv.size(), new ArrayList<>());
				csv.add(currentRow);
			} else {
				// Parse the next value and add it to the current row
				currentRow.add(parseValue(reader));

				// In case of a separator, continue with the next value of the current row
				while (reader.hasNext() && reader.peek() == separator) {
					reader.next();
					currentRow.add(parseValue(reader));
				}
			}
		}
		return csv.unmodifiable();
	}

	/**
	 * Parses a single CSV value, starting at the current position and stopping
	 * right behind its last character.
	 *
	 * <p>
	 * In case an escaped CSV value comes with leading or trailing spaces, they are
	 * stripped off of the actual value.
	 *
	 * @param reader a {@link PeekableReader} as data input
	 * @return the parsed CSV value
	 * @throws IOException if an I/O error occurs
	 */
	@PackagePrivate
	@SuppressWarnings("PMD.PrematureDeclaration")
	String parseValue(final PeekableReader reader) throws IOException {
		final StringBuilder builder = new StringBuilder();

		// Check if the given value is escaped. Leading whitespaces need to be read to
		// determine that.
		final boolean isEscaped = readLeadingWhitespacesAndIsEscaped(reader, builder);

		while (reader.hasNext()) {
			// Check if control characters, such as the separator or new line characters,
			// need to be handled in their special way. If the given value is escaped, the
			// escape character needs to precede such characters to take effect.
			if (readEscaperAndIsControlCharHandling(reader, isEscaped)) {
				// If the given value is escaped and the escaping character is followed by
				// whitespaces only, we trim the trailing whitespaces.
				final String trailingWhitespaces = isEscaped ? readWhitespaces(reader) : "";

				// In case of a separator or new line character the current value was read
				// successfully
				if (!reader.hasNext() || isSeparatorOrNewLine(reader.peek())) {
					return builder.toString();
				}

				if (isEscaped) {
					// Else we simply append the escaping character and further unexpected
					// characters.
					builder.append(escaper).append(trailingWhitespaces);
				}
			}

			// Append the current character
			builder.append(reader.next());
		}
		return builder.toString();
	}

	/**
	 * Checks if control characters need to be handled.
	 *
	 * <p>
	 * For non-escaped values control characters always need to be handled. For
	 * escaped values the value needs to be "closed" using the escaping character
	 * prior handling following control characters.
	 *
	 * @param reader    a {@link PeekableReader} as data input
	 * @param isEscaped {@code true} if the current value is escaped, else
	 *                  {@code false}
	 * @return {@code true} if control characters need to be handled, else
	 *         {@code false}
	 * @throws IOException if an I/O error occurs
	 */
	@PackagePrivate
	boolean readEscaperAndIsControlCharHandling(final PeekableReader reader, final boolean isEscaped)
			throws IOException {
		if (!isEscaped) {
			return true;
		}
		if (!reader.hasNext() || reader.peek() != escaper) {
			return false;
		}
		reader.next();
		return !reader.hasNext() || reader.peek() != escaper;
	}

	/**
	 * Checks if the value, that starts at the reader's position, is escaped.
	 * Leading whitespaces need to be read to determine that.
	 *
	 * <p>
	 * The reader is forwarded to the first character of the value. In case of an
	 * escaped value, the escaping character is read.
	 *
	 * <p>
	 * Leading whitespaces are trimmed in case of an escaped value, else they are
	 * appended to {@code builder}.
	 *
	 * @param reader  a {@link PeekableReader} as data input
	 * @param builder a builder to possibly append whitespaces to.
	 * @return {@code true} if the value, that starts at the reader's position, is
	 *         escaped, else {@code false}
	 * @throws IOException if an I/O error occurs
	 */
	@PackagePrivate
	@SuppressWarnings("PMD.PrematureDeclaration")
	boolean readLeadingWhitespacesAndIsEscaped(final PeekableReader reader, final StringBuilder builder)
			throws IOException {
		final String whitespaces = readWhitespaces(reader);

		if (reader.hasNext() && reader.peek() == escaper) {
			reader.next();
			return true;
		}

		builder.append(whitespaces);
		return false;
	}

	/**
	 * Reads whitespace characters without a special meaning starting at the current
	 * position of the reader.
	 *
	 * <p>
	 * Whitespace characters are defined using
	 * {@link Characters#isAsciiWhitespace(char)}. Separator, escaping and new line
	 * characters are not handled as whitespace character, even if they could be an
	 * whitespace character.
	 *
	 * @param reader a {@link PeekableReader} as data input
	 * @return the read whitespace characters
	 * @throws IOException if an I/O error occurs
	 */
	@PackagePrivate
	String readWhitespaces(final PeekableReader reader) throws IOException {
		final StringBuilder builder = new StringBuilder();
		while (reader.hasNext()) {
			final char character = reader.peek();

			// Stop reading when finding the first non-whitespace character
			if (character == escaper || !Characters.isAsciiWhitespace(character) || isSeparatorOrNewLine(character)) {
				return builder.toString();
			}

			builder.append(reader.next());
		}
		return builder.toString();
	}
}
