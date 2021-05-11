package de.larssh.utils.text;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import lombok.NoArgsConstructor;

/**
 * Tests for {@link Characters}
 */
@NoArgsConstructor
public class CharactersTest {
	/**
	 * Test {@link Characters#compareIgnoreCase(char, char)} with ASCII {@code NUL}
	 * character (lower technical boundary)
	 */
	@Test
	public void testCompareIgnoreCase_asciiZero() {
		assertThat(Characters.compareIgnoreCase('\0', '\0')).isZero();
		assertThat(Characters.compareIgnoreCase('\0', '\u0001')).isNegative();
		assertThat(Characters.compareIgnoreCase('\u0001', '\0')).isPositive();
	}

	/**
	 * Test {@link Characters#compareIgnoreCase(char, char)} with ASCII lower and
	 * upper case letters
	 */
	@Test
	public void testCompareIgnoreCase_asciiLetters() {
		assertThat(Characters.compareIgnoreCase('A', 'A')).isZero();
		assertThat(Characters.compareIgnoreCase('A', 'B')).isNegative();
		assertThat(Characters.compareIgnoreCase('B', 'A')).isPositive();

		assertThat(Characters.compareIgnoreCase('a', 'a')).isZero();
		assertThat(Characters.compareIgnoreCase('a', 'B')).isNegative();
		assertThat(Characters.compareIgnoreCase('B', 'a')).isPositive();
	}

	/**
	 * Test {@link Characters#compareIgnoreCase(char, char)} with one character
	 * lower and higher than ASCII letters
	 */
	@Test
	public void testCompareIgnoreCase_asciiLetterBoundaries() {
		assertThat(Characters.compareIgnoreCase('A', (char) ('a' - 1))).isPositive();
		assertThat(Characters.compareIgnoreCase('a', (char) ('a' - 1))).isPositive();
		assertThat(Characters.compareIgnoreCase('Z', (char) ('z' + 1))).isNegative();
		assertThat(Characters.compareIgnoreCase('z', (char) ('z' + 1))).isNegative();
	}

	/**
	 * Test {@link Characters#compareIgnoreCase(char, char)} with Unicode letters
	 */
	@Test
	public void testCompareIgnoreCase_unicodeLetters() {
		assertThat(Characters.compareIgnoreCase('Ä', 'Ä')).isZero();
		assertThat(Characters.compareIgnoreCase('Ä', 'A')).isPositive();
		assertThat(Characters.compareIgnoreCase('Ä', 'ä')).isZero();

		assertThat(Characters.compareIgnoreCase('ä', 'ä')).isZero();
		assertThat(Characters.compareIgnoreCase('ä', 'A')).isPositive();
		assertThat(Characters.compareIgnoreCase('ä', 'Ä')).isZero();
	}

	/**
	 * Test {@link Characters#compareIgnoreCaseAscii(char, char)} with ASCII
	 * {@code NUL} character (lower technical boundary)
	 */
	@Test
	public void testCompareIgnoreCaseAscii_asciiZero() {
		assertThat(Characters.compareIgnoreCaseAscii('\0', '\0')).isZero();
		assertThat(Characters.compareIgnoreCaseAscii('\0', '\u0001')).isNegative();
		assertThat(Characters.compareIgnoreCaseAscii('\u0001', '\0')).isPositive();
	}

	/**
	 * Test {@link Characters#compareIgnoreCaseAscii(char, char)} with ASCII lower
	 * and upper case letters
	 */
	@Test
	public void testCompareIgnoreCaseAscii_asciiLetters() {
		assertThat(Characters.compareIgnoreCaseAscii('A', 'A')).isZero();
		assertThat(Characters.compareIgnoreCaseAscii('A', 'B')).isNegative();
		assertThat(Characters.compareIgnoreCaseAscii('B', 'A')).isPositive();

		assertThat(Characters.compareIgnoreCaseAscii('a', 'a')).isZero();
		assertThat(Characters.compareIgnoreCaseAscii('a', 'B')).isNegative();
		assertThat(Characters.compareIgnoreCaseAscii('B', 'a')).isPositive();
	}

	/**
	 * Test {@link Characters#compareIgnoreCaseAscii(char, char)} with one character
	 * lower and higher than ASCII letters
	 */
	@Test
	public void testCompareIgnoreCaseAscii_asciiLetterBoundaries() {
		assertThat(Characters.compareIgnoreCaseAscii('A', (char) ('a' - 1))).isPositive();
		assertThat(Characters.compareIgnoreCaseAscii('a', (char) ('a' - 1))).isPositive();
		assertThat(Characters.compareIgnoreCaseAscii('Z', (char) ('z' + 1))).isNegative();
		assertThat(Characters.compareIgnoreCaseAscii('z', (char) ('z' + 1))).isNegative();
	}

	/**
	 * Test {@link Characters#compareIgnoreCaseAscii(char, char)} with Unicode
	 * letters
	 */
	@Test
	public void testCompareIgnoreCaseAscii_unicodeLetters() {
		assertThat(Characters.compareIgnoreCaseAscii('Ä', 'Ä')).isZero();
		assertThat(Characters.compareIgnoreCaseAscii('Ä', 'A')).isPositive();
		assertThat(Characters.compareIgnoreCaseAscii('Ä', 'ä')).isNegative();

		assertThat(Characters.compareIgnoreCaseAscii('ä', 'ä')).isZero();
		assertThat(Characters.compareIgnoreCaseAscii('ä', 'A')).isPositive();
		assertThat(Characters.compareIgnoreCaseAscii('ä', 'Ä')).isPositive();
	}

	/**
	 * Test {@link Characters#equalsIgnoreCase(char, char)} with ASCII {@code NUL}
	 * character (lower technical boundary)
	 */
	@Test
	public void testEqualsIgnoreCase_asciiZero() {
		assertThat(Characters.equalsIgnoreCase('\0', '\0')).isTrue();
		assertThat(Characters.equalsIgnoreCase('\0', '\u0001')).isFalse();
		assertThat(Characters.equalsIgnoreCase('\u0001', '\0')).isFalse();
	}

	/**
	 * Test {@link Characters#equalsIgnoreCase(char, char)} with ASCII lower and
	 * upper case letters
	 */
	@Test
	public void testEqualsIgnoreCase_asciiLetters() {
		assertThat(Characters.equalsIgnoreCase('A', 'A')).isTrue();
		assertThat(Characters.equalsIgnoreCase('A', 'B')).isFalse();
		assertThat(Characters.equalsIgnoreCase('B', 'A')).isFalse();

		assertThat(Characters.equalsIgnoreCase('a', 'a')).isTrue();
		assertThat(Characters.equalsIgnoreCase('a', 'B')).isFalse();
		assertThat(Characters.equalsIgnoreCase('B', 'a')).isFalse();
	}

	/**
	 * Test {@link Characters#equalsIgnoreCase(char, char)} with one character lower
	 * and higher than ASCII letters
	 */
	@Test
	public void testEqualsIgnoreCase_asciiLetterBoundaries() {
		assertThat(Characters.equalsIgnoreCase('A', (char) ('a' - 1))).isFalse();
		assertThat(Characters.equalsIgnoreCase('a', (char) ('a' - 1))).isFalse();
		assertThat(Characters.equalsIgnoreCase('Z', (char) ('z' + 1))).isFalse();
		assertThat(Characters.equalsIgnoreCase('z', (char) ('z' + 1))).isFalse();
	}

	/**
	 * Test {@link Characters#equalsIgnoreCase(char, char)} with Unicode letters
	 */
	@Test
	public void testEqualsIgnoreCase_unicodeLetters() {
		assertThat(Characters.equalsIgnoreCase('Ä', 'Ä')).isTrue();
		assertThat(Characters.equalsIgnoreCase('Ä', 'A')).isFalse();
		assertThat(Characters.equalsIgnoreCase('Ä', 'ä')).isTrue();

		assertThat(Characters.equalsIgnoreCase('ä', 'ä')).isTrue();
		assertThat(Characters.equalsIgnoreCase('ä', 'A')).isFalse();
		assertThat(Characters.equalsIgnoreCase('ä', 'Ä')).isTrue();
	}

	/**
	 * Test {@link Characters#equalsIgnoreCaseAscii(char, char)} with ASCII
	 * {@code NUL} character (lower technical boundary)
	 */
	@Test
	public void testEqualsIgnoreCaseAscii_asciiZero() {
		assertThat(Characters.equalsIgnoreCaseAscii('\0', '\0')).isTrue();
		assertThat(Characters.equalsIgnoreCaseAscii('\0', '\u0001')).isFalse();
		assertThat(Characters.equalsIgnoreCaseAscii('\u0001', '\0')).isFalse();
	}

	/**
	 * Test {@link Characters#equalsIgnoreCaseAscii(char, char)} with ASCII lower
	 * and upper case letters
	 */
	@Test
	public void testEqualsIgnoreCaseAscii_asciiLetters() {
		assertThat(Characters.equalsIgnoreCaseAscii('A', 'A')).isTrue();
		assertThat(Characters.equalsIgnoreCaseAscii('A', 'B')).isFalse();
		assertThat(Characters.equalsIgnoreCaseAscii('B', 'A')).isFalse();

		assertThat(Characters.equalsIgnoreCaseAscii('a', 'a')).isTrue();
		assertThat(Characters.equalsIgnoreCaseAscii('a', 'B')).isFalse();
		assertThat(Characters.equalsIgnoreCaseAscii('B', 'a')).isFalse();
	}

	/**
	 * Test {@link Characters#equalsIgnoreCaseAscii(char, char)} with one character
	 * lower and higher than ASCII letters
	 */
	@Test
	public void testEqualsIgnoreCaseAscii_asciiLetterBoundaries() {
		assertThat(Characters.equalsIgnoreCaseAscii('A', (char) ('a' - 1))).isFalse();
		assertThat(Characters.equalsIgnoreCaseAscii('a', (char) ('a' - 1))).isFalse();
		assertThat(Characters.equalsIgnoreCaseAscii('Z', (char) ('z' + 1))).isFalse();
		assertThat(Characters.equalsIgnoreCaseAscii('z', (char) ('z' + 1))).isFalse();
	}

	/**
	 * Test {@link Characters#equalsIgnoreCaseAscii(char, char)} with Unicode
	 * letters
	 */
	@Test
	public void testEqualsIgnoreCaseAscii_unicodeLetters() {
		assertThat(Characters.equalsIgnoreCaseAscii('Ä', 'Ä')).isTrue();
		assertThat(Characters.equalsIgnoreCaseAscii('Ä', 'A')).isFalse();
		assertThat(Characters.equalsIgnoreCaseAscii('Ä', 'ä')).isFalse();

		assertThat(Characters.equalsIgnoreCaseAscii('ä', 'ä')).isTrue();
		assertThat(Characters.equalsIgnoreCaseAscii('ä', 'A')).isFalse();
		assertThat(Characters.equalsIgnoreCaseAscii('ä', 'Ä')).isFalse();
	}

	/**
	 * Test {@link Characters#isAsciiDigit(char)}: Expecting {@code false} for ASCII
	 * {@code NUL} value
	 */
	@Test
	public void testIsAsciiDigit_asciiZero() {
		assertThat(Characters.isAsciiDigit('\0')).isFalse();
	}

	/**
	 * Test {@link Characters#isAsciiDigit(char)}: Expecting {@code true} for ASCII
	 * digits
	 */
	@Test
	public void testIsAsciiWhitespace_asciiDigits() {
		assertThat(Characters.isAsciiDigit('0')).isTrue();
		assertThat(Characters.isAsciiDigit('9')).isTrue();
	}

	/**
	 * Test {@link Characters#isAsciiDigit(char)}: Expecting {@code false} for one
	 * character lower and higher than ASCII digits
	 */
	@Test
	public void testIsAsciiWhitespace_asciiDigitsBoundaries() {
		assertThat(Characters.isAsciiDigit((char) ('0' - 1))).isFalse();
		assertThat(Characters.isAsciiDigit((char) ('9' + 1))).isFalse();
	}

	/**
	 * Test {@link Characters#isAsciiDigit(char)}: Expecting {@code false} for
	 * letters
	 */
	@Test
	public void testIsAsciiWhitespace_letters() {
		assertThat(Characters.isAsciiDigit('A')).isFalse();
		assertThat(Characters.isAsciiDigit('Z')).isFalse();
		assertThat(Characters.isAsciiDigit('a')).isFalse();
		assertThat(Characters.isAsciiDigit('z')).isFalse();
		assertThat(Characters.isAsciiDigit('Ä')).isFalse();
		assertThat(Characters.isAsciiDigit('ä')).isFalse();
	}

	/**
	 * Test {@link Characters#isAsciiLetter(char)}: Expecting {@code false} for
	 * ASCII {@code NUL} value
	 */
	@Test
	public void testIsAsciiLetter_asciiZero() {
		assertThat(Characters.isAsciiLetter('\0')).isFalse();
	}

	/**
	 * Test {@link Characters#isAsciiLetter(char)}: Expecting {@code false} for
	 * ASCII digits
	 */
	@Test
	public void testIsAsciiLetter_asciiDigits() {
		assertThat(Characters.isAsciiLetter('0')).isFalse();
		assertThat(Characters.isAsciiLetter('9')).isFalse();
	}

	/**
	 * Test {@link Characters#isAsciiLetter(char)}: Expecting {@code true} for ASCII
	 * letters
	 */
	@Test
	public void testIsAsciiLetter_asciiLetters() {
		assertThat(Characters.isAsciiLetter('A')).isTrue();
		assertThat(Characters.isAsciiLetter('Z')).isTrue();
		assertThat(Characters.isAsciiLetter('a')).isTrue();
		assertThat(Characters.isAsciiLetter('z')).isTrue();
	}

	/**
	 * Test {@link Characters#isAsciiLetter(char)}: Expecting {@code false} for one
	 * character lower and higher than ASCII letters
	 */
	@Test
	public void testIsAsciiLetter_asciiLetterBoundaries() {
		assertThat(Characters.isAsciiLetter((char) ('A' - 1))).isFalse();
		assertThat(Characters.isAsciiLetter((char) ('Z' + 1))).isFalse();
		assertThat(Characters.isAsciiLetter((char) ('a' - 1))).isFalse();
		assertThat(Characters.isAsciiLetter((char) ('z' + 1))).isFalse();
	}

	/**
	 * Test {@link Characters#isAsciiLetter(char)}: Expecting {@code false} for
	 * Unicode letters
	 */
	@Test
	public void testIsAsciiLetter_unicodeLetters() {
		assertThat(Characters.isAsciiLetter('Ä')).isFalse();
		assertThat(Characters.isAsciiLetter('ä')).isFalse();
	}

	/**
	 * Test {@link Characters#isAsciiWhitespace(char)}: Expecting {@code true} for
	 * ASCII {@code NUL} value
	 */
	@Test
	public void testIsAsciiWhitespace_asciiZero() {
		assertThat(Characters.isAsciiWhitespace('\0')).isTrue();
	}

	/**
	 * Test {@link Characters#isAsciiWhitespace(char)}: Expecting {@code true} for
	 * ASCII whitespaces
	 */
	@Test
	public void testIsAsciiWhitespace_asciiWhitespaces() {
		assertThat(Characters.isAsciiWhitespace('\t')).isTrue();
		assertThat(Characters.isAsciiWhitespace('\n')).isTrue();
		assertThat(Characters.isAsciiWhitespace('\r')).isTrue();
		assertThat(Characters.isAsciiWhitespace((char) (' ' - 1))).isTrue();
		assertThat(Characters.isAsciiWhitespace(' ')).isTrue();
	}

	/**
	 * Test {@link Characters#isAsciiWhitespace(char)}: Expecting {@code false} for
	 * one character higher than ASCII whitespaces
	 */
	@Test
	public void testIsAsciiWhitespace_asciiWhitespaceBoundary() {
		assertThat(Characters.isAsciiWhitespace((char) (' ' + 1))).isFalse();
	}

	/**
	 * Test {@link Characters#isAsciiWhitespace(char)}: Expecting {@code false} for
	 * digits and letters
	 */
	@Test
	public void testIsAsciiWhitespace_digitsAndLetters() {
		assertThat(Characters.isAsciiWhitespace('0')).isFalse();
		assertThat(Characters.isAsciiWhitespace('9')).isFalse();
		assertThat(Characters.isAsciiWhitespace('A')).isFalse();
		assertThat(Characters.isAsciiWhitespace('Z')).isFalse();
		assertThat(Characters.isAsciiWhitespace('a')).isFalse();
		assertThat(Characters.isAsciiWhitespace('z')).isFalse();
		assertThat(Characters.isAsciiWhitespace('Ä')).isFalse();
		assertThat(Characters.isAsciiWhitespace('ä')).isFalse();
	}

	/**
	 * Test {@link Characters#toLowerCaseAscii(char)}: Expecting no change for ASCII
	 * {@code NUL} character
	 */
	@Test
	public void testToLowerCaseAscii_asciiZero() {
		assertThat(Characters.toLowerCaseAscii('\0')).isEqualTo('\0');
	}

	/**
	 * Test {@link Characters#toLowerCaseAscii(char)}: Expecting no change for ASCII
	 * digits
	 */
	@Test
	public void testToLowerCaseAscii_asciiDigits() {
		assertThat(Characters.toLowerCaseAscii('0')).isEqualTo('0');
		assertThat(Characters.toLowerCaseAscii('9')).isEqualTo('9');
	}

	/**
	 * Test {@link Characters#toLowerCaseAscii(char)}: Expecting lower case for
	 * ASCII upper case letters
	 */
	@Test
	public void testToLowerCaseAscii_asciiUpperLetters() {
		assertThat(Characters.toLowerCaseAscii('A')).isEqualTo('a');
		assertThat(Characters.toLowerCaseAscii('Z')).isEqualTo('z');
	}

	/**
	 * Test {@link Characters#toLowerCaseAscii(char)}: Expecting no change for ASCII
	 * lower case letters
	 */
	@Test
	public void testToLowerCaseAscii_asciiLowerLetters() {
		assertThat(Characters.toLowerCaseAscii('a')).isEqualTo('a');
		assertThat(Characters.toLowerCaseAscii('z')).isEqualTo('z');
	}

	/**
	 * Test {@link Characters#toLowerCaseAscii(char)}: Expecting no change for one
	 * character lower and higher than ASCII letters
	 */
	@Test
	public void testToLowerCaseAscii_asciiLetterBoundaries() {
		assertThat(Characters.toLowerCaseAscii((char) ('A' - 1))).isEqualTo((char) ('A' - 1));
		assertThat(Characters.toLowerCaseAscii((char) ('Z' + 1))).isEqualTo((char) ('Z' + 1));
		assertThat(Characters.toLowerCaseAscii((char) ('a' - 1))).isEqualTo((char) ('a' - 1));
		assertThat(Characters.toLowerCaseAscii((char) ('z' + 1))).isEqualTo((char) ('z' + 1));
	}

	/**
	 * Test {@link Characters#toLowerCaseAscii(char)}: Expecting no change for
	 * Unicode letters
	 */
	@Test
	public void testToLowerCaseAscii_unicodeLetters() {
		assertThat(Characters.toLowerCaseAscii('Ä')).isEqualTo('Ä');
		assertThat(Characters.toLowerCaseAscii('ä')).isEqualTo('ä');
	}

	/**
	 * Test {@link Characters#toUpperCaseAscii(char)}: Expecting no change for ASCII
	 * {@code NUL} character
	 */
	@Test
	public void testToUpperCaseAscii_asciiZero() {
		assertThat(Characters.toUpperCaseAscii('\0')).isEqualTo('\0');
	}

	/**
	 * Test {@link Characters#toUpperCaseAscii(char)}: Expecting no change for ASCII
	 * digits
	 */
	@Test
	public void testToUpperCaseAscii_asciiDigits() {
		assertThat(Characters.toUpperCaseAscii('0')).isEqualTo('0');
		assertThat(Characters.toUpperCaseAscii('9')).isEqualTo('9');
	}

	/**
	 * Test {@link Characters#toUpperCaseAscii(char)}: Expecting no change for ASCII
	 * upper case letters
	 */
	@Test
	public void testToUpperCaseAscii_asciiUpperLetters() {
		assertThat(Characters.toUpperCaseAscii('A')).isEqualTo('A');
		assertThat(Characters.toUpperCaseAscii('Z')).isEqualTo('Z');
	}

	/**
	 * Test {@link Characters#toUpperCaseAscii(char)}: Expecting upper case for
	 * ASCII lower case letters
	 */
	@Test
	public void testToUpperCaseAscii_asciiLowerLetters() {
		assertThat(Characters.toUpperCaseAscii('a')).isEqualTo('A');
		assertThat(Characters.toUpperCaseAscii('z')).isEqualTo('Z');
	}

	/**
	 * Test {@link Characters#toUpperCaseAscii(char)}: Expecting no change for one
	 * character lower and higher than ASCII letters
	 */
	@Test
	public void testToUpperCaseAscii_asciiLetterBoundaries() {
		assertThat(Characters.toUpperCaseAscii((char) ('A' - 1))).isEqualTo((char) ('A' - 1));
		assertThat(Characters.toUpperCaseAscii((char) ('Z' + 1))).isEqualTo((char) ('Z' + 1));
		assertThat(Characters.toUpperCaseAscii((char) ('a' - 1))).isEqualTo((char) ('a' - 1));
		assertThat(Characters.toUpperCaseAscii((char) ('z' + 1))).isEqualTo((char) ('z' + 1));
	}

	/**
	 * Test {@link Characters#toUpperCaseAscii(char)}: Expecting no change for
	 * Unicode letters
	 */
	@Test
	public void testToUpperCaseAscii_unicodeLetters() {
		assertThat(Characters.toUpperCaseAscii('Ä')).isEqualTo('Ä');
		assertThat(Characters.toUpperCaseAscii('ä')).isEqualTo('ä');
	}
}
