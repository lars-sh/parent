package de.larssh.utils.text;

import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link Character}.
 */
@UtilityClass
public class Characters {
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
			char a = Character.toUpperCase(first);
			char b = Character.toUpperCase(second);
			if (a != b) {
				a = Character.toLowerCase(a);
				b = Character.toLowerCase(b);
				return a - b;
			}
		}
		return 0;
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
}
