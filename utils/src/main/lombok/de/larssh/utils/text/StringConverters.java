package de.larssh.utils.text;

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for decoding and encoding strings.
 */
@UtilityClass
public class StringConverters {
	/**
	 * Decodes {@code value} as Base64 string.
	 *
	 * <p>
	 * This method is equivalent to using {@link Base64#getDecoder()} with the char
	 * set {@link Strings#DEFAULT_CHARSET}.
	 *
	 * @param value the value to be decoded
	 * @return the decoded value
	 */
	public static String decodeBase64(final String value) {
		return new String(Base64.getDecoder().decode(value), Strings.DEFAULT_CHARSET);
	}

	/**
	 * Decodes {@code value} using the Base64 MIME type decoding scheme.
	 *
	 * <p>
	 * This method is equivalent to using {@link Base64#getMimeDecoder()} with the
	 * char set {@link Strings#DEFAULT_CHARSET}.
	 *
	 * @param value the value to be decoded
	 * @return the decoded value
	 */
	public static String decodeBase64Mime(final String value) {
		return new String(Base64.getMimeDecoder().decode(value), Strings.DEFAULT_CHARSET);
	}

	/**
	 * Decodes {@code value} as Base64 URL and filename-safe string.
	 *
	 * <p>
	 * This method is equivalent to using {@link Base64#getUrlDecoder()} with the
	 * char set {@link Strings#DEFAULT_CHARSET}.
	 *
	 * @param value the value to be decoded
	 * @return the decoded value
	 */
	public static String decodeBase64Url(final String value) {
		return new String(Base64.getUrlDecoder().decode(value), Strings.DEFAULT_CHARSET);
	}

	/**
	 * Parses the CSV data given by {@code data}.
	 *
	 * <p>
	 * To process {@link Reader} input, refer to
	 * {@link Csv#parse(Reader, char, char)}.
	 *
	 * @param data      the CSV input data
	 * @param separator the CSV separator character
	 * @param escaper   the CSV escaping character
	 * @return an object representing the parsed CSV data
	 * @throws IllegalArgumentException on illegal {@code separator} or
	 *                                  {@code escaper} value
	 */
	@SuppressFBWarnings(value = "EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS",
			justification = "converting checked to unchecked exception, that should never be thrown at all")
	public static Csv decodeCsv(final String data, final char separator, final char escaper) {
		try (Reader reader = new StringReader(data)) {
			return Csv.parse(reader, separator, escaper);
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Decodes an {@code application/x-www-form-urlencoded} string.
	 *
	 * <p>
	 * This method is equivalent to using {@link URLDecoder#decode(String)} with the
	 * char set {@link StandardCharsets#UTF_8} as the
	 * <a href= "https://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
	 * World Wide Web Consortium Recommendation</a> states that UTF-8 should be
	 * used.
	 *
	 * @param value the value to be decoded
	 * @return the decoded value
	 * @throws StringParseException on illegal or incomplete hex characters
	 */
	@SuppressFBWarnings(value = "EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS",
			justification = "converting checked to unchecked exception, that should never be thrown at all")
	public static String decodeUrl(final String value) throws StringParseException {
		try {
			return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
		} catch (final IllegalArgumentException e) {
			throw new StringParseException(e, "Failed decoding URL.");
		} catch (final UnsupportedEncodingException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Translates {@code value} into Base64 format.
	 *
	 * <p>
	 * This method is equivalent to using {@link Base64#getEncoder()} with the char
	 * set {@link Strings#DEFAULT_CHARSET}.
	 *
	 * @param value the value to be encoded
	 * @return the encoded value
	 */
	public static String encodeBase64(final String value) {
		return new String(Base64.getEncoder().encode(value.getBytes(Strings.DEFAULT_CHARSET)), Strings.DEFAULT_CHARSET);
	}

	/**
	 * Translates {@code value} into Base64 MIME type encoding scheme.
	 *
	 * <p>
	 * This method is equivalent to using {@link Base64#getMimeEncoder()} with the
	 * char set {@link Strings#DEFAULT_CHARSET}.
	 *
	 * @param value the value to be encoded
	 * @return the encoded value
	 */
	public static String encodeBase64Mime(final String value) {
		return new String(Base64.getMimeEncoder().encode(value.getBytes(Strings.DEFAULT_CHARSET)),
				Strings.DEFAULT_CHARSET);
	}

	/**
	 * Translates {@code value} into Base64 URL and filename-safe format.
	 *
	 * <p>
	 * This method is equivalent to using {@link Base64#getUrlEncoder()} with the
	 * charset {@link Strings#DEFAULT_CHARSET}.
	 *
	 * @param value the value to be encoded
	 * @return the encoded value
	 */
	public static String encodeBase64Url(final String value) {
		return new String(Base64.getUrlEncoder().encode(value.getBytes(Strings.DEFAULT_CHARSET)),
				Strings.DEFAULT_CHARSET);
	}

	/**
	 * Encodes {@code data} as CSV document. Occurrences of special characters are
	 * encoded using {@code escaper} and values are separated using
	 * {@code separator}.
	 *
	 * <p>
	 * For more information on single value escaping see
	 * {@link #encodeCsvValue(String, char, char)}.
	 *
	 * @param data      the data to be encoded
	 * @param separator the CSV separator character
	 * @param escaper   the CSV escaping character
	 * @return the encoded CSV document
	 */
	public static String encodeCsv(final Collection<? extends Collection<String>> data,
			final char separator,
			final char escaper) {
		return data.stream() //
				.map(row -> encodeCsvRow(row, separator, escaper))
				.collect(joining(Strings.NEW_LINE));
	}

	/**
	 * Encodes {@code values} as one CSV row. Occurrences of special characters are
	 * encoded using {@code escaper} and values are separated using
	 * {@code separator}.
	 *
	 * <p>
	 * For more information on single value escaping see
	 * {@link #encodeCsvValue(String, char, char)}.
	 *
	 * @param values    the values to be encoded
	 * @param separator the CSV separator character
	 * @param escaper   the CSV escaping character
	 * @return the encoded CSV row
	 */
	public static String encodeCsvRow(final Collection<? extends String> values,
			final char separator,
			final char escaper) {
		return values.stream()
				.map(value -> encodeCsvValue(value, separator, escaper))
				.collect(joining(Character.toString(separator)));
	}

	/**
	 * Encodes {@code value} into a single CSV value. All occurrences of
	 * {@code escaper} are escaped (doubled). If {@code value} contains
	 * inappropriate characters, it is surrounded by {@code escaper}.
	 *
	 * <p>
	 * Inappropriate characters are {@code '\r'}, {@code '\n'}, {@code escaper} and
	 * {@code separator}. The latter is used for the list of inappropriate
	 * characters only.
	 *
	 * <p>
	 * <b>Examples:</b>
	 * <table>
	 * <caption>Examples</caption>
	 * <tr>
	 * <th>Parameter</th>
	 * <th>Return Value</th>
	 * </tr>
	 * <tr>
	 * <td>EMPTY</td>
	 * <td>EMPTY</td>
	 * </tr>
	 * <tr>
	 * <td>abc</td>
	 * <td>abc</td>
	 * </tr>
	 * <tr>
	 * <td>abc"xyz</td>
	 * <td>"abc""xyz"</td>
	 * </tr>
	 * <tr>
	 * <td>"</td>
	 * <td>""""</td>
	 * </tr>
	 * </table>
	 *
	 * @param value     the value to be encoded
	 * @param separator the CSV separator character
	 * @param escaper   the CSV escaping character
	 * @return the encoded value
	 */
	@SuppressWarnings("PMD.CyclomaticComplexity")
	public static String encodeCsvValue(final String value, final char separator, final char escaper) {
		CsvParser.assertCsvInput(separator, escaper);

		final int length = value.length();
		int index = 0;

		boolean needsEscaping = false;
		for (; index < length && !needsEscaping; index += 1) {
			final char character = value.charAt(index);
			if (character == escaper) {
				needsEscaping = true;
				index -= 1;
			} else if (character == separator || character == '\r' || character == '\n') {
				needsEscaping = true;
			}
		}
		if (!needsEscaping) {
			return value;
		}

		final StringBuilder builder = new StringBuilder(length + 2);
		builder.append(escaper);
		if (index > 0) {
			builder.append(value, 0, index);
		}
		for (; index < length; index += 1) {
			final char character = value.charAt(index);
			if (character == escaper) {
				builder.append(escaper);
			}
			builder.append(character);
		}
		return builder.append(escaper).toString();
	}

	/**
	 * Translates a string into {@code application/x-www-form-urlencoded} format
	 * with the char set {@link StandardCharsets#UTF_8} as the
	 * <a href= "https://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
	 * World Wide Web Consortium Recommendation</a> states that UTF-8 should be
	 * used.
	 *
	 * @param value the value to be encoded
	 * @return the encoded value
	 */
	@SuppressFBWarnings(value = "EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS",
			justification = "converting checked to unchecked exception, that should never be thrown at all")
	public static String encodeUrl(final String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
		} catch (final UnsupportedEncodingException e) {
			throw new UncheckedIOException(e);
		}
	}
}
