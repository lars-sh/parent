package de.larssh.utils.text;

import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link Character}.
 */
@UtilityClass
public class Characters {
	/**
	 * Difference between the ASCII (and Unicode) positions of the upper and lower
	 * case ASCII letters.
	 */
	private static final char CASE_CHARACTER_DIFFERENCE = 'a' - 'A';

	/**
	 * Compares two {@code char} values lexicographically, ignoring case
	 * differences. This method eliminates case differences by calling
	 * {@code Character.toLowerCase(Character.toUpperCase(character))}.
	 *
	 * <p>
	 * Note that this method does <i>not</i> take locale into account, and will
	 * result in an unsatisfactory ordering for certain locales.
	 *
	 * @param first  the first {@code char} to compare
	 * @param second the second {@code char} to compare
	 * @return a positive integer, zero, or a negative integer as {@code first}
	 *         {@code char} is greater than, equal to, or less than {@code second}
	 *         {@code char}, ignoring case considerations.
	 */
	public static int compareIgnoreCase(final char first, final char second) {
		if (first != second) {
			final char firstUpperCase = Character.toUpperCase(first);
			final char secondUpperCase = Character.toUpperCase(second);
			if (firstUpperCase != secondUpperCase) {
				return Character.toLowerCase(firstUpperCase) - Character.toLowerCase(secondUpperCase);
			}
		}
		return 0;
	}

	/**
	 * Compares two {@code char} values, ignoring case differences in the ASCII
	 * range. This method eliminates case differences by calling
	 * {@code Characters.toUpperCaseAscii(character)}.
	 *
	 * <p>
	 * Note that this method does <i>not</i> ignore the case for characters outside
	 * of the ASCII range.
	 *
	 * @param first  the first {@code char} to compare
	 * @param second the second {@code char} to compare
	 * @return a positive integer, zero, or a negative integer as {@code first}
	 *         {@code char} is greater than, equal to, or less than {@code second}
	 *         {@code char}, ignoring case considerations in the ASCII range.
	 */
	public static int compareIgnoreCaseAscii(final char first, final char second) {
		return first == second ? 0 : toUpperCaseAscii(first) - toUpperCaseAscii(second);
	}

	/**
	 * Compares the characters, ignoring case considerations.
	 *
	 * @param first  the first {@code char} to compare
	 * @param second the second {@code char} to compare
	 * @return {@code true} if the objects are the same, ignoring case
	 *         considerations; {@code false} otherwise.
	 */
	public static boolean equalsIgnoreCase(final char first, final char second) {
		return compareIgnoreCase(first, second) == 0;
	}

	/**
	 * Compares the characters, ignoring case considerations in the ASCII range.
	 *
	 * @param first  the first {@code char} to compare
	 * @param second the second {@code char} to compare
	 * @return {@code true} if the objects are the same, ignoring case
	 *         considerations; {@code false} otherwise.
	 */
	public static boolean equalsIgnoreCaseAscii(final char first, final char second) {
		return compareIgnoreCaseAscii(first, second) == 0;
	}

	/**
	 * Determines if the specified character is an ASCII digit.
	 *
	 * <p>
	 * This method provides higher performance than {@link Character#isDigit(char)}
	 * while not checking Unicode character ranges.
	 *
	 * @param character the character to be tested
	 * @return {@code true} if the character is an ASCII digit; {@code false}
	 *         otherwise.
	 */
	public static boolean isAsciiDigit(final char character) {
		return character >= '0' && character <= '9';
	}

	/**
	 * Determines if the specified character is an ASCII letter.
	 *
	 * <p>
	 * This method provides higher performance than {@link Character#isLetter(char)}
	 * while not checking Unicode character ranges. It returns {@code true} for
	 * characters {@code A} to {@code Z} and {@code a} to {@code z}.
	 *
	 * @param character the character to be tested
	 * @return {@code true} if the character is an ASCII letter; {@code false}
	 *         otherwise.
	 */
	public static boolean isAsciiLetter(final char character) {
		return character >= 'A' && character <= 'Z' || character >= 'a' && character <= 'z';
	}

	/**
	 * Determines if the specified character is an ASCII white space.
	 *
	 * <p>
	 * This method provides higher performance than
	 * {@link Character#isWhitespace(char)} while not checking Unicode character
	 * ranges. It is equivalent to white space checking of {@link String#trim()}.
	 *
	 * @param character the character to be tested
	 * @return {@code true} if the character is an ASCII white space; {@code false}
	 *         otherwise.
	 */
	public static boolean isAsciiWhitespace(final char character) {
		return character <= ' ';
	}

	/**
	 * Converts the character argument to lowercase when inside the ASCII range.
	 *
	 * @param character the character to be converted.
	 * @return the lowercase equivalent of the character, if any; otherwise, the
	 *         character itself.
	 * @see Character#isLowerCase(char)
	 * @see String#toLowerCase()
	 */
	public static char toLowerCaseAscii(final char character) {
		return character < 'A' || character > 'Z' ? character : (char) (character + CASE_CHARACTER_DIFFERENCE);
	}

	/**
	 * Converts the character argument to uppercase when inside the ASCII range.
	 *
	 * @param character the character to be converted.
	 * @return the uppercase equivalent of the character, if any; otherwise, the
	 *         character itself.
	 * @see Character#isUpperCase(char)
	 * @see String#toUpperCase()
	 */
	public static char toUpperCaseAscii(final char character) {
		return character < 'a' || character > 'z' ? character : (char) (character - CASE_CHARACTER_DIFFERENCE);
	}
}
