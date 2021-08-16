package de.larssh.utils.text;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;

import org.junit.jupiter.api.Test;

import de.larssh.utils.io.PeekableReader;
import lombok.NoArgsConstructor;

/**
 * Tests for {@link CsvParser}
 */
@NoArgsConstructor
public class CsvParserTest {
	private static final CsvParser PARSER = new CsvParser(Csv.DEFAULT_SEPARATOR, Csv.DEFAULT_ESCAPER);

	/**
	 * Creates a {@link PeekableReader} for {@code value} to be used within
	 * {@code consumer}.
	 *
	 * @param value    the value to read
	 * @param consumer the test to perform
	 */
	private static void testWithPeekableReader(final String value, final IOConsumer<PeekableReader> consumer) {
		try (PeekableReader reader = new PeekableReader(new StringReader(value))) {
			consumer.accept(reader);
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Test {@link CsvParser#assertCsvInput(char, char)}: Giving the default
	 * separator and escape characters, expecting no exception
	 */
	@Test
	public void testAssertCsvInput_defaults() {
		assertThatCode(() -> CsvParser.assertCsvInput(Csv.DEFAULT_SEPARATOR, Csv.DEFAULT_ESCAPER))
				.doesNotThrowAnyException();
	}

	/**
	 * Test {@link CsvParser#assertCsvInput(char, char)}: Giving the escaper is
	 * equal to the separator, expecting an exception
	 */
	@Test
	public void testAssertCsvInput_escaperEqualsSeparator() {
		assertThatIllegalArgumentException()
				.isThrownBy(() -> CsvParser.assertCsvInput(Csv.DEFAULT_SEPARATOR, Csv.DEFAULT_SEPARATOR));
	}

	/**
	 * Test {@link CsvParser#assertCsvInput(char, char)}: Giving the escaper is
	 * equal to the ASCII carriage return character, expecting an exception
	 */
	@Test
	public void testAssertCsvInput_escaperEqualsCarriageReturn() {
		assertThatIllegalArgumentException().isThrownBy(() -> CsvParser.assertCsvInput(Csv.DEFAULT_SEPARATOR, '\r'));
	}

	/**
	 * Test {@link CsvParser#assertCsvInput(char, char)}: Giving the escaper is
	 * equal to the ASCII line feed character, expecting an exception
	 */
	@Test
	public void testAssertCsvInput_escaperEqualsLineFeed() {
		assertThatIllegalArgumentException().isThrownBy(() -> CsvParser.assertCsvInput(Csv.DEFAULT_SEPARATOR, '\n'));
	}

	/**
	 * Test {@link CsvParser#assertCsvInput(char, char)}: Giving the separator is
	 * equal to the ASCII carriage return character, expecting an exception
	 */
	@Test
	public void testAssertCsvInput_separatorEqualsCarriageReturn() {
		assertThatIllegalArgumentException().isThrownBy(() -> CsvParser.assertCsvInput('\r', Csv.DEFAULT_ESCAPER));
	}

	/**
	 * Test {@link CsvParser#assertCsvInput(char, char)}: Giving the separator is
	 * equal to the ASCII line feed character, expecting an exception
	 */
	@Test
	public void testAssertCsvInput_separatorEqualsLineFeed() {
		assertThatIllegalArgumentException().isThrownBy(() -> CsvParser.assertCsvInput('\n', Csv.DEFAULT_ESCAPER));
	}

	/**
	 * Test {@link CsvParser#isSeparatorOrNewLine(char)}: Giving <i>no</i> separator
	 * or new line character
	 */
	@Test
	public void testIsSeparatorOrNewLine_false() {
		assertThat(PARSER.isSeparatorOrNewLine('\0')).isFalse();
		assertThat(PARSER.isSeparatorOrNewLine(Csv.DEFAULT_ESCAPER)).isFalse();
		assertThat(PARSER.isSeparatorOrNewLine((char) (Csv.DEFAULT_ESCAPER - 1))).isFalse();
		assertThat(PARSER.isSeparatorOrNewLine((char) (Csv.DEFAULT_ESCAPER + 1))).isFalse();

		assertThat(PARSER.isSeparatorOrNewLine((char) (Csv.DEFAULT_SEPARATOR - 1))).isFalse();
		assertThat(PARSER.isSeparatorOrNewLine((char) (Csv.DEFAULT_SEPARATOR + 1))).isFalse();
		assertThat(PARSER.isSeparatorOrNewLine((char) ('\n' - 1))).isFalse();
		assertThat(PARSER.isSeparatorOrNewLine((char) ('\n' + 1))).isFalse();
		assertThat(PARSER.isSeparatorOrNewLine((char) ('\r' - 1))).isFalse();
		assertThat(PARSER.isSeparatorOrNewLine((char) ('\r' + 1))).isFalse();
	}

	/**
	 * Test {@link CsvParser#isSeparatorOrNewLine(char)}: Giving separator or new
	 * line character
	 */
	@Test
	public void testIsSeparatorOrNewLine_true() {
		assertThat(PARSER.isSeparatorOrNewLine(Csv.DEFAULT_SEPARATOR)).isTrue();
		assertThat(PARSER.isSeparatorOrNewLine('\n')).isTrue();
		assertThat(PARSER.isSeparatorOrNewLine('\r')).isTrue();
	}

	/**
	 * Test {@link CsvParser#parse(java.io.Reader)}: To follow
	 */
	@Test
	public void testParse_TODO() {
		// To follow
	}

	/**
	 * Test {@link CsvParser#parseValue(de.larssh.utils.io.PeekableReader)}: Reading
	 * unescaped simple values
	 */
	@Test
	public void testParseValue_readUnescapedSimpleValues() {
		testWithPeekableReader("", reader -> {
			assertThat(PARSER.parseValue(reader)).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader("A", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo("A");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader("AB", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo("AB");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
	}

	/**
	 * Test {@link CsvParser#parseValue(de.larssh.utils.io.PeekableReader)}: Reading
	 * unescaped values followed by a control character
	 */
	@Test
	public void testParseValue_readUnescapedFollowedByControlCharacters() {
		testWithPeekableReader(",", reader -> {
			assertThat(PARSER.parseValue(reader)).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader("\n", reader -> {
			assertThat(PARSER.parseValue(reader)).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader("A,", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo("A");
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader("A\n", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo("A");
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
	}

	/**
	 * Test {@link CsvParser#parseValue(de.larssh.utils.io.PeekableReader)}: Reading
	 * unescaped values with whitespace prefix or suffix
	 */
	@Test
	public void testParseValue_readUnescapedWithWhitespaces() {
		testWithPeekableReader(" ", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo(" ");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader(" \t A \t ", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo(" \t A \t ");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader(" \t ,", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo(" \t ");
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader(" \t \n", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo(" \t ");
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
	}

	/**
	 * Test {@link CsvParser#parseValue(de.larssh.utils.io.PeekableReader)}: Reading
	 * escaped empty values
	 */
	@Test
	public void testParseValue_readEscapedEmptyValues() {
		testWithPeekableReader("\"", reader -> {
			assertThat(PARSER.parseValue(reader)).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader("\"\"", reader -> {
			assertThat(PARSER.parseValue(reader)).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
	}

	/**
	 * Test {@link CsvParser#parseValue(de.larssh.utils.io.PeekableReader)}: Reading
	 * escaped simple values
	 */
	@Test
	public void testParseValue_readEscapedSimpleValues() {
		testWithPeekableReader("\"A", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo("A");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader("\"A\"", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo("A");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
	}

	/**
	 * Test {@link CsvParser#parseValue(de.larssh.utils.io.PeekableReader)}: Reading
	 * escaped value followed by control character
	 */
	@Test
	public void testParseValue_readEscapedFollowedByControlCharacter() {
		testWithPeekableReader("\"A\",", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo("A");
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader("\"A\"\n", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo("A");
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
	}

	/**
	 * Test {@link CsvParser#parseValue(de.larssh.utils.io.PeekableReader)}: Reading
	 * escaped values with whitespace prefix
	 */
	@Test
	public void testParseValue_readEscapedWithWhitespacePrefix() {
		testWithPeekableReader(" \t \"A\"", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo("A");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader(" \t \"A\"", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo("A");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
	}

	/**
	 * Test {@link CsvParser#parseValue(de.larssh.utils.io.PeekableReader)}: Reading
	 * escaped values with whitespace suffix
	 */
	@Test
	public void testParseValue_readEscapedWithWhitespaceSuffix() {
		testWithPeekableReader("\"A\" \t ", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo("A");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader("\"A\" \t ,", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo("A");
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader("\"A\" \t \n", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo("A");
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
	}

	/**
	 * Test {@link CsvParser#parseValue(de.larssh.utils.io.PeekableReader)}: Reading
	 * escaped value with escaped character
	 */
	@Test
	public void testParseValue_readEscapedWithEscapedCharacters() {
		testWithPeekableReader("\"AB\"", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo("AB");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader("\"A\"B\"", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo("A\"B");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader("\"A\"\"B\"", reader -> {
			assertThat(PARSER.parseValue(reader)).isEqualTo("A\"B");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
	}

	/**
	 * Test
	 * {@link CsvParser#readEscaperAndIsControlCharHandling(de.larssh.utils.io.PeekableReader, boolean)}:
	 * Reading nothing when <i>not</i> in escaped mode
	 */
	@Test
	public void testReadEscaperAndIsControlCharHandling_readNothingWithoutEscaping() {
		testWithPeekableReader("", reader -> {
			assertThat(PARSER.readEscaperAndIsControlCharHandling(reader, false)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader("A", reader -> {
			assertThat(PARSER.readEscaperAndIsControlCharHandling(reader, false)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader("\"", reader -> {
			assertThat(PARSER.readEscaperAndIsControlCharHandling(reader, false)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
	}

	/**
	 * Test
	 * {@link CsvParser#readEscaperAndIsControlCharHandling(de.larssh.utils.io.PeekableReader, boolean)}:
	 * Reading nothing when in escaped mode, but missing escape character
	 */
	@Test
	public void testReadEscaperAndIsControlCharHandling_readNothingWithEscaping() {
		testWithPeekableReader("", reader -> {
			assertThat(PARSER.readEscaperAndIsControlCharHandling(reader, true)).isFalse();
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader("A", reader -> {
			assertThat(PARSER.readEscaperAndIsControlCharHandling(reader, true)).isFalse();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
	}

	/**
	 * Test
	 * {@link CsvParser#readEscaperAndIsControlCharHandling(de.larssh.utils.io.PeekableReader, boolean)}:
	 * Reading end of escape mode
	 */
	@Test
	public void testReadEscaperAndIsControlCharHandling_readEndOfEscaping() {
		testWithPeekableReader("\"", reader -> {
			assertThat(PARSER.readEscaperAndIsControlCharHandling(reader, true)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader("\"A", reader -> {
			assertThat(PARSER.readEscaperAndIsControlCharHandling(reader, true)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
	}

	/**
	 * Test
	 * {@link CsvParser#readEscaperAndIsControlCharHandling(de.larssh.utils.io.PeekableReader, boolean)}:
	 * Reading escaped character
	 */
	@Test
	public void testReadEscaperAndIsControlCharHandling_readEscapedCharacter() {
		testWithPeekableReader("\"\"", reader -> {
			assertThat(PARSER.readEscaperAndIsControlCharHandling(reader, true)).isFalse();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader("\"\"A", reader -> {
			assertThat(PARSER.readEscaperAndIsControlCharHandling(reader, true)).isFalse();
			assertThat(reader.skip(Long.MAX_VALUE)).isEqualTo(2);
		});
	}

	/**
	 * Test
	 * {@link CsvParser#readLeadingWhitespacesAndIsEscaped(de.larssh.utils.io.PeekableReader, StringBuilder)}:
	 * Reading nothing
	 */
	@Test
	public void testReadLeadingWhitespacesAndIsEscaped_readNothing() {
		testWithPeekableReader("", reader -> {
			final StringBuilder builder = new StringBuilder();
			assertThat(PARSER.readLeadingWhitespacesAndIsEscaped(reader, builder)).isFalse();
			assertThat(builder).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader("A", reader -> {
			final StringBuilder builder = new StringBuilder();
			assertThat(PARSER.readLeadingWhitespacesAndIsEscaped(reader, builder)).isFalse();
			assertThat(builder).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
	}

	/**
	 * Test
	 * {@link CsvParser#readLeadingWhitespacesAndIsEscaped(de.larssh.utils.io.PeekableReader, StringBuilder)}:
	 * Reading whitespaces and <i>not</i> finding escape character
	 */
	@Test
	public void testReadLeadingWhitespacesAndIsEscaped_readWhitespacesAndNoEscapeCharacter() {
		testWithPeekableReader(" ", reader -> {
			final StringBuilder builder = new StringBuilder();
			assertThat(PARSER.readLeadingWhitespacesAndIsEscaped(reader, builder)).isFalse();
			assertThat(builder).hasToString(" ");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader(" \t ", reader -> {
			final StringBuilder builder = new StringBuilder();
			assertThat(PARSER.readLeadingWhitespacesAndIsEscaped(reader, builder)).isFalse();
			assertThat(builder).hasToString(" \t ");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader(" \t A", reader -> {
			final StringBuilder builder = new StringBuilder();
			assertThat(PARSER.readLeadingWhitespacesAndIsEscaped(reader, builder)).isFalse();
			assertThat(builder).hasToString(" \t ");
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
	}

	/**
	 * Test
	 * {@link CsvParser#readLeadingWhitespacesAndIsEscaped(de.larssh.utils.io.PeekableReader, StringBuilder)}:
	 * Reading whitespaces and finding escape character followed by the end of data
	 */
	@Test
	public void testReadLeadingWhitespacesAndIsEscaped_readWhitespacesAndEscapeCharacterFollowedByEndOfData() {
		testWithPeekableReader(" \"", reader -> {
			final StringBuilder builder = new StringBuilder();
			assertThat(PARSER.readLeadingWhitespacesAndIsEscaped(reader, builder)).isTrue();
			assertThat(builder).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader(" \"", reader -> {
			final StringBuilder builder = new StringBuilder();
			assertThat(PARSER.readLeadingWhitespacesAndIsEscaped(reader, builder)).isTrue();
			assertThat(builder).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader(" \t \"", reader -> {
			final StringBuilder builder = new StringBuilder();
			assertThat(PARSER.readLeadingWhitespacesAndIsEscaped(reader, builder)).isTrue();
			assertThat(builder).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
	}

	/**
	 * Test
	 * {@link CsvParser#readLeadingWhitespacesAndIsEscaped(de.larssh.utils.io.PeekableReader, StringBuilder)}:
	 * Reading whitespaces and finding escape character followed by characters
	 */
	@Test
	public void testReadLeadingWhitespacesAndIsEscaped_readWhitespacesAndEscapeCharacterFollowedByCharacters() {
		testWithPeekableReader(" \"A", reader -> {
			final StringBuilder builder = new StringBuilder();
			assertThat(PARSER.readLeadingWhitespacesAndIsEscaped(reader, builder)).isTrue();
			assertThat(builder).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader(" \"A", reader -> {
			final StringBuilder builder = new StringBuilder();
			assertThat(PARSER.readLeadingWhitespacesAndIsEscaped(reader, builder)).isTrue();
			assertThat(builder).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader(" \t \"A", reader -> {
			final StringBuilder builder = new StringBuilder();
			assertThat(PARSER.readLeadingWhitespacesAndIsEscaped(reader, builder)).isTrue();
			assertThat(builder).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
	}

	/**
	 * Test {@link CsvParser#readNewLine(de.larssh.utils.io.PeekableReader)}:
	 * Nothing to read
	 */
	@Test
	public void testReadNewLine_readNothing() {
		testWithPeekableReader("", reader -> {
			assertThat(CsvParser.readNewLine(reader)).isFalse();
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader("A", reader -> {
			assertThat(CsvParser.readNewLine(reader)).isFalse();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
	}

	/**
	 * Test {@link CsvParser#readNewLine(de.larssh.utils.io.PeekableReader)}:
	 * Reading single new line character followed by the end of data
	 */
	@Test
	public void testReadNewLine_readNewLineCharacterFollowedByEndOfData() {
		testWithPeekableReader("\n", reader -> {
			assertThat(CsvParser.readNewLine(reader)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader("\r", reader -> {
			assertThat(CsvParser.readNewLine(reader)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
	}

	/**
	 * Test {@link CsvParser#readNewLine(de.larssh.utils.io.PeekableReader)}:
	 * Reading {@code \r\n} sequence followed by the end of data
	 */
	@Test
	public void testReadNewLine_readNewLineSequenceFollowedByEndOfData() {
		testWithPeekableReader("\r\n", reader -> {
			assertThat(CsvParser.readNewLine(reader)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
	}

	/**
	 * Test {@link CsvParser#readNewLine(de.larssh.utils.io.PeekableReader)}:
	 * Reading new line sequences followed by no new line sequence
	 */
	@Test
	public void testReadNewLine_readNewLineFollowedByCharacters() {
		testWithPeekableReader("\nA", reader -> {
			assertThat(CsvParser.readNewLine(reader)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader("\rA", reader -> {
			assertThat(CsvParser.readNewLine(reader)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader("\r\nA", reader -> {
			assertThat(CsvParser.readNewLine(reader)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
	}

	/**
	 * Test {@link CsvParser#readNewLine(de.larssh.utils.io.PeekableReader)}:
	 * Reading new line sequences followed by one more new line sequence
	 */
	@Test
	public void testReadNewLine_readNewLineFollowedByNewLine() {
		testWithPeekableReader("\n\n", reader -> {
			assertThat(CsvParser.readNewLine(reader)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader("\n\r", reader -> {
			assertThat(CsvParser.readNewLine(reader)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader("\n\r\n", reader -> {
			assertThat(CsvParser.readNewLine(reader)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isEqualTo(2);
		});

		testWithPeekableReader("\r\r", reader -> {
			assertThat(CsvParser.readNewLine(reader)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader("\r\r\n", reader -> {
			assertThat(CsvParser.readNewLine(reader)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isEqualTo(2);
		});

		testWithPeekableReader("\r\n\n", reader -> {
			assertThat(CsvParser.readNewLine(reader)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader("\r\n\n", reader -> {
			assertThat(CsvParser.readNewLine(reader)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader("\r\n\r\n", reader -> {
			assertThat(CsvParser.readNewLine(reader)).isTrue();
			assertThat(reader.skip(Long.MAX_VALUE)).isEqualTo(2);
		});
	}

	/**
	 * Test {@link CsvParser#readWhitespaces(de.larssh.utils.io.PeekableReader)}:
	 * Reading nothing
	 */
	@Test
	public void testReadWhitespaces_readNothing() {
		testWithPeekableReader("", reader -> {
			assertThat(PARSER.readWhitespaces(reader)).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader("A", reader -> {
			assertThat(PARSER.readWhitespaces(reader)).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
	}

	/**
	 * Test {@link CsvParser#readWhitespaces(de.larssh.utils.io.PeekableReader)}:
	 * Reading a single whitespace followed by the end of data
	 */
	@Test
	public void testReadWhitespaces_readSingleWhitespace() {
		testWithPeekableReader(" ", reader -> {
			assertThat(PARSER.readWhitespaces(reader)).isEqualTo(" ");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
		testWithPeekableReader("\t", reader -> {
			assertThat(PARSER.readWhitespaces(reader)).isEqualTo("\t");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
	}

	/**
	 * Test {@link CsvParser#readWhitespaces(de.larssh.utils.io.PeekableReader)}:
	 * Reading multiple whitespaces followed by the end of data
	 */
	@Test
	public void testReadWhitespaces_readMultipleWhitespacesFollowedByEndOfData() {
		testWithPeekableReader(" \t ", reader -> {
			assertThat(PARSER.readWhitespaces(reader)).isEqualTo(" \t ");
			assertThat(reader.skip(Long.MAX_VALUE)).isZero();
		});
	}

	/**
	 * Test {@link CsvParser#readWhitespaces(de.larssh.utils.io.PeekableReader)}:
	 * Reading whitespaces followed by characters
	 */
	@Test
	public void testReadWhitespaces_readWhitespacesFollowedByCharacters() {
		testWithPeekableReader(" A", reader -> {
			assertThat(PARSER.readWhitespaces(reader)).isEqualTo(" ");
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader(" \t A", reader -> {
			assertThat(PARSER.readWhitespaces(reader)).isEqualTo(" \t ");
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
	}

	/**
	 * Test {@link CsvParser#readWhitespaces(de.larssh.utils.io.PeekableReader)}:
	 * Reading no new line characters
	 */
	@Test
	public void testReadWhitespaces_readNoNewLine() {
		testWithPeekableReader("\n", reader -> {
			assertThat(PARSER.readWhitespaces(reader)).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader("\r", reader -> {
			assertThat(PARSER.readWhitespaces(reader)).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
		testWithPeekableReader("\r\n", reader -> {
			assertThat(PARSER.readWhitespaces(reader)).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isEqualTo(2);
		});
	}

	/**
	 * Test {@link CsvParser#readWhitespaces(de.larssh.utils.io.PeekableReader)}:
	 * Reading no separator character
	 */
	@Test
	public void testReadWhitespaces_readNoSeparator() {
		testWithPeekableReader("\t", reader -> {
			assertThat(new CsvParser('\t', Csv.DEFAULT_ESCAPER).readWhitespaces(reader)).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
	}

	/**
	 * Test {@link CsvParser#readWhitespaces(de.larssh.utils.io.PeekableReader)}:
	 * Reading no escape character
	 */
	@Test
	public void testReadWhitespaces_readNoEscapeCharacter() {
		testWithPeekableReader("\0", reader -> {
			assertThat(new CsvParser(Csv.DEFAULT_SEPARATOR, '\0').readWhitespaces(reader)).isEmpty();
			assertThat(reader.skip(Long.MAX_VALUE)).isOne();
		});
	}

	/**
	 * Represents an operation that accepts a single input argument and returns no
	 * result. It allows throwing an {@link IOException}.
	 *
	 * <p>
	 * This is a functional interface whose functional method is
	 * {@link #accept(Object)}.
	 *
	 * @param <T> the type of the input to the operation
	 */
	@FunctionalInterface
	private interface IOConsumer<T> {
		/**
		 * Performs this operation on the given argument.
		 *
		 * @param value the input argument
		 * @throws IOException on I/O error
		 */
		void accept(T value) throws IOException;
	}
}
