package de.larssh.utils.text;

import java.util.Comparator;

import de.larssh.utils.annotations.PackagePrivate;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;

/**
 * A {@link Comparator} that orders alpha numeric strings in <i>natural</i>
 * order, either case sensitive or case insensitive. It is appreciated to use
 * this kind of ordering for user output.
 *
 * <p>
 * Because numeric values are not deserialized into numeric data types, their
 * length is not limited. Fractions are not supported and will be handled as two
 * separate numeric values.
 *
 * <p>
 * Numeric values can have a leading plus or minus sign when following a
 * whitespace character or at a strings start.
 *
 * <p>
 * The following lists some example values to demonstrate the ordering.
 * <ul>
 * <li>Banana -12 Circus
 * <li>Banana -5 Circus
 * <li>Banana +5 Circus
 * <li>Banana 5 Circus
 * <li>Banana +5 Dolphin
 * <li>Banana 8 Circus
 * <li>Banana 12 Circus
 * <li>Banana-5 Circus
 * <li>Banana-12 Circus
 * <li>Banana--5 Circus
 * <li>Elephant 5 Circus
 * </ul>
 *
 * <p>
 * This comparator permits null values.
 *
 * <p>
 * Note that this Comparator does <em>not</em> take locale into account, and
 * will result in an unsatisfactory ordering for certain locales.
 */
@Getter
@PackagePrivate
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class NumericTextComparator implements Comparator<String> {
	/**
	 * Singleton instance of {@link NumericTextComparator} to compare strings case
	 * insensitive.
	 */
	@PackagePrivate
	static final Comparator<String> COMPARATOR_CASE_INSENSITIVE = new NumericTextComparator(true);

	/**
	 * Singleton instance of {@link NumericTextComparator} to compare strings case
	 * sensitive.
	 */
	@PackagePrivate
	static final Comparator<String> COMPARATOR_CASE_SENSITIVE = new NumericTextComparator(false);

	/**
	 * Specifies case sensitivity for comparison
	 *
	 * @return {@code true} if comparison should take place case insensitive
	 */
	boolean caseInsensitive;

	/**
	 * Compares its two arguments for numeric <i>and</i> text order. Returns a
	 * negative integer, zero, or a positive integer as the first argument is less
	 * than, equal to, or greater than the second.
	 *
	 * <p>
	 * This comparator permits null values.
	 *
	 * @param first  the first char sequence to be compared
	 * @param second the second char sequence to be compared
	 * @return a negative integer, zero, or a positive integer as the first argument
	 *         is less than, equal to, or greater than the second
	 */
	@Override
	public int compare(@Nullable final String first, @Nullable final String second) {
		if (first == null) {
			return second == null ? 0 : -1;
		}
		return second == null ? 1 : new NumericTextComparatorContext(first, second).compare();
	}

	/**
	 * Internal context per each comparison, holding the strings to compare and
	 * their current indexes
	 */
	private final class NumericTextComparatorContext {
		/**
		 * First string to be compared
		 */
		String first;

		/**
		 * State of the comparison: Current index of the first string
		 */
		@NonFinal
		int firstIndex;

		/**
		 * Length of the first string (cached)
		 */
		int firstLength;

		/**
		 * Second string to be compared
		 */
		String second;

		/**
		 * State of the comparison: Current index of the second string
		 */
		@NonFinal
		int secondIndex;

		/**
		 * Length of the second string (cached)
		 */
		int secondLength;

		/**
		 * Constructor for the internal context per each comparison, holding the strings
		 * to compare and their current indexes
		 *
		 * @param first  the first char sequence to be compared
		 * @param second the second char sequence to be compared
		 */
		/* package */ NumericTextComparatorContext(final String first, final String second) {
			this.first = first;
			firstLength = first.length();

			this.second = second;
			secondLength = second.length();
		}

		/**
		 * Compares both strings
		 *
		 * @return comparison result
		 */
		public int compare() {
			while (!isRest()) {
				if (isNumeric()) {
					final int compared = compareSignedNumeric();
					if (compared != 0) {
						return compared;
					}
				} else {
					final int compared = compareText();
					if (compared != 0) {
						return compared;
					}
				}
			}

			final int compared = compareRest();
			if (compared != 0) {
				return compared;
			}
			return isCaseInsensitive() ? first.compareToIgnoreCase(second) : first.compareTo(second);
		}

		/**
		 * Returns {@code true} if at least one of the strings end has been reached.
		 *
		 * @return {@code true} if a strings end has been reached
		 */
		private boolean isRest() {
			return firstIndex >= firstLength || secondIndex >= secondLength;
		}

		/**
		 * Returns {@code true} if at least of the strings continues with an optionally
		 * signed numeric value.
		 *
		 * <p>
		 * This method is used to initiate the numeric comparison. It loads one
		 * character of both strings <i>without</i> validation.
		 *
		 * @return {@code true} if any sequence continues with an optionally signed
		 *         numeric value
		 */
		private boolean isNumeric() {
			return Characters.isAsciiDigit(first.charAt(firstIndex))
					|| Characters.isAsciiDigit(second.charAt(secondIndex))
					|| isSignedNumeric(first, firstLength, firstIndex)
					|| isSignedNumeric(second, secondLength, secondIndex);
		}

		/**
		 * Returns {@code true} if {@code value} has a signed numeric value at
		 * {@code index}.
		 *
		 * <p>
		 * Signed values need to be at index {@code 0} or require a whitespace character
		 * at {@code index - 1}.
		 *
		 * @param value  the string to check
		 * @param length the length of {@code value} (cached)
		 * @param index  the index to check
		 * @return {@code true} if {@code value} has a signed numeric value at
		 *         {@code index}
		 */
		private boolean isSignedNumeric(final String value, final int length, final int index) {
			final char sign = value.charAt(index);
			if (sign != '-' && sign != '+'
					|| index > 0 && !Characters.isAsciiWhitespace(value.charAt(index - 1))
					|| index + 1 >= length) {
				return false;
			}
			return Characters.isAsciiDigit(value.charAt(index + 1));
		}

		/**
		 * Compares an optionally signed numeric value.
		 *
		 * <p>
		 * This method updates the internal contexts state. It loads one character of
		 * both strings <i>without</i> validation.
		 *
		 * @return comparison result
		 */
		private int compareSignedNumeric() {
			// Calculate length of the numeric chunks
			final int firstSignedNumericLength = getNumericLength(first, firstLength, firstIndex);
			final int secondSignedNumericLength = getNumericLength(second, secondLength, secondIndex);

			// Return if one is not numeric
			if (firstSignedNumericLength == 0 || secondSignedNumericLength == 0) {
				return firstSignedNumericLength == 0 ? 1 : -1;
			}

			// Handle negative sign
			final boolean firstIsNegative = first.charAt(firstIndex) == '-';
			final boolean secondIsNegative = second.charAt(secondIndex) == '-';
			if (firstIsNegative != secondIsNegative) {
				return firstIsNegative ? -1 : 1;
			}

			// Calculate numeric length without signs and leading zeros
			final int firstNumericLength = getDigitsLength(first, firstIndex, firstSignedNumericLength);
			final int secondNumericLength = getDigitsLength(second, secondIndex, secondSignedNumericLength);

			// Update indexes
			firstIndex += firstSignedNumericLength;
			secondIndex += secondSignedNumericLength;

			// Compare
			return (firstIsNegative ? -1 : 1) * compareNumeric(firstNumericLength, secondNumericLength);
		}

		/**
		 * Returns the length of an optionally signed numeric in {@code value} at
		 * {@code index} or zero.
		 *
		 * @param value  the string to check
		 * @param length the length of {@code value} (cached)
		 * @param index  the index to check
		 * @return length of an optionally signed numeric in {@code value} at
		 *         {@code index} or zero
		 */
		private int getNumericLength(final String value, final int length, final int index) {
			int numericIndex = index;
			if (isSignedNumeric(value, length, numericIndex)) {
				numericIndex += 2;
			}
			while (numericIndex < length && Characters.isAsciiDigit(value.charAt(numericIndex))) {
				numericIndex += 1;
			}
			return numericIndex - index;
		}

		/**
		 * Returns the length of the absolute value of an optionally signed numeric
		 * without leading zeros in {@code value} at {@code index} for signed length
		 * {@code signedNumericLength}.
		 *
		 * <p>
		 * In case the numeric value is zero a length of one is returned.
		 *
		 * <p>
		 * This method does not validate if characters in {@code value} from
		 * {@code index} to length {@code signedNumericLength} are valid numeric values!
		 *
		 * @param value               the string to check
		 * @param index               the index to check
		 * @param signedNumericLength the length of the optionally signed numeric to
		 *                            check (cached)
		 * @return length of the absolute value of an optionally signed numeric without
		 *         leading zeros
		 */
		private int getDigitsLength(final String value, final int index, final int signedNumericLength) {
			for (int numericIndex = 0; numericIndex < signedNumericLength; numericIndex += 1) {
				final char character = value.charAt(index + numericIndex);
				if (character != '0' && character != '-' && character != '+') {
					return signedNumericLength - numericIndex;
				}
			}
			return 1;
		}

		/**
		 * Compares numeric values at the current index and of the given lengths.
		 *
		 * <p>
		 * This method requires its arguments to match the rules of
		 * {@link #getNumericLength(String, int, int)}.
		 *
		 * @param firstNumericLength  length of the first numeric
		 * @param secondNumericLength length of the second numeric
		 * @return comparison result
		 */
		private int compareNumeric(final int firstNumericLength, final int secondNumericLength) {
			// Return on different lengths
			if (firstNumericLength != secondNumericLength) {
				return firstNumericLength - secondNumericLength;
			}

			// Compare each numeric character from highest to lowest
			for (int numericIndex = firstNumericLength; numericIndex > 0; numericIndex -= 1) {
				final char firstNumericCharacter = first.charAt(firstIndex - numericIndex);
				final char secondNumericCharacter = second.charAt(secondIndex - numericIndex);

				if (firstNumericCharacter != secondNumericCharacter) {
					return firstNumericCharacter - secondNumericCharacter;
				}
			}
			return 0;
		}

		/**
		 * Compares a single textual character.
		 *
		 * <p>
		 * This method updates the internal contexts state. It loads one character of
		 * both strings <i>without</i> validation.
		 *
		 * @return comparison result
		 */
		private int compareText() {
			final char firstCharacter = first.charAt(firstIndex);
			final char secondCharacter = second.charAt(secondIndex);

			firstIndex += 1;
			secondIndex += 1;

			return isCaseInsensitive()
					? Characters.compareIgnoreCase(firstCharacter, secondCharacter)
					: firstCharacter - secondCharacter;
		}

		/**
		 * Compares the strings if at least one has reached its end.
		 *
		 * @return comparison result
		 */
		private int compareRest() {
			return firstLength - firstIndex - (secondLength - secondIndex);
		}
	}
}
