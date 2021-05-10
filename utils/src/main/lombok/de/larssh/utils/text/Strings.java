package de.larssh.utils.text;

import static de.larssh.utils.Collectors.toLinkedHashMap;
import static de.larssh.utils.Finals.constant;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.joining;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Formatter;
import java.util.IllegalFormatException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import de.larssh.utils.Optionals;
import de.larssh.utils.collection.Maps;
import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link String}.
 */
@UtilityClass
@SuppressWarnings({ "PMD.CyclomaticComplexity", "PMD.ExcessiveImports", "PMD.GodClass" })
public class Strings {
	/**
	 * Character to separate strings inside regular expressions
	 */
	private static final String PATTERN_STRING_SEPARATOR = "|";

	/**
	 * Map of binary units to their factor
	 *
	 * <ul>
	 * <li>{@code K}: {@code 1024}
	 * <li>{@code M}: {@code 1024 * 1024}
	 * <li>{@code G}: {@code 1024 * 1024 * 1024}
	 * <li>...
	 * </ul>
	 */
	public static final Map<String, BigDecimal> BINARY_UNITS = unmodifiableMap(getBinaryUnits());

	/**
	 * Pattern for parsing binary unit strings
	 */
	private static final Pattern BINARY_UNIT_PATTERN
			= Pattern.compile("(?i)^\\s*(?<value>[+-]?\\s*(\\d([\\d_]*\\d)?)?\\.?\\d([\\d_]*\\d)?)\\s*((?<unit>"
					+ BINARY_UNITS.keySet().stream().map(Pattern::quote).collect(joining(PATTERN_STRING_SEPARATOR))
					+ ")i?)?\\s*$");

	/**
	 * Map of decimal units to their power of ten
	 *
	 * <ul>
	 * <li>...
	 * <li>{@code m}: {@code -3}
	 * <li>{@code k}: {@code 3}
	 * <li>{@code M}: {@code 6}
	 * <li>...
	 * </ul>
	 */
	@SuppressWarnings({ "checkstyle:MagicNumber", "checkstyle:MultipleStringLiterals" })
	@SuppressFBWarnings(value = "PSC_PRESIZE_COLLECTIONS",
			justification = "this method is called just once (in static initializer); keep code simple")
	public static final Map<String, Integer> DECIMAL_UNITS = Maps.builder(new LinkedHashMap<String, Integer>())
			.put("y", -24)
			.put("z", -21)
			.put("a", -18)
			.put("f", -15)
			.put("p", -12)
			.put("n", -9)
			.put("u", -6)
			.put("μ", -6)
			.put("m", -3)
			.put("c", -2)
			.put("d", -1)
			.put("da", 1)
			.put("h", 2)
			.put("k", 3)
			.put("M", 6)
			.put("G", 9)
			.put("T", 12)
			.put("P", 15)
			.put("E", 18)
			.put("Z", 21)
			.put("Y", 24)
			.unmodifiable();

	/**
	 * Pattern for parsing decimal unit strings
	 */
	private static final Pattern DECIMAL_UNIT_PATTERN
			= Pattern.compile("(?i)^\\s*(?<value>[+-]?\\s*(\\d([\\d_]*\\d)?)?\\.?\\d([\\d_]*\\d)?)\\s*(?<unit>"
					+ DECIMAL_UNITS.keySet().stream().map(Pattern::quote).collect(joining(PATTERN_STRING_SEPARATOR))
					+ ")?\\s*$");

	/**
	 * Constant UTF-8 for usage as default char set.
	 *
	 * <p>
	 * Using {@link java.nio.charset.Charset#defaultCharset()} leads to unexpected
	 * compatibility problems. While the new {@link java.nio.file.Files} API has
	 * been changed to use UTF-8 by default, old and third-party implementations
	 * still depend on the default char set or require a custom char set.
	 *
	 * <p>
	 * <b>Why shouldn't I use {@link StandardCharsets#UTF_8} directly?</b><br>
	 * Using {@link StandardCharsets#UTF_8} is bad practice, as it holds
	 * implementation specific information.
	 */
	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	/**
	 * Constant locale as default locale where no language and country is required.
	 *
	 * <p>
	 * Using {@link Locale#getDefault()} leads to unexpected behavior for technical
	 * formatting operations. Therefore this locale specifies a default value.
	 *
	 * <p>
	 * The returned value is {@link Locale#ROOT}.
	 */
	public static final Locale DEFAULT_LOCALE = Locale.ROOT;

	/**
	 * Constant {@code "\n"} <b>for output</b>. Remember to accept {@code "\r\n"}
	 * and {@code "\r"} on the input side, too!
	 *
	 * <p>
	 * Using {@link System#lineSeparator()} leads to files, which cannot (should
	 * not) be transferred between UNIX and Microsoft Windows systems. To avoid
	 * problems, some developers decided to depend on the UNIX line separator only.
	 *
	 * <p>
	 * <b>Why shouldn't I use {@code "\n"} directly?</b><br>
	 * Using {@code "\n"} is bad practice, as it holds implementation specific
	 * information.
	 */
	public static final String NEW_LINE = constant("\n");

	/**
	 * Pattern to retrieve white space characters in binary and decimal unit strings
	 */
	private static final Pattern UNIT_WHITE_SPACE_PATTERN = Pattern.compile("[\\s_]+");

	/**
	 * Compares two strings, ignoring case differences in the ASCII range.
	 *
	 * @param first  the first string to compare
	 * @param second the second string to compare
	 * @return a positive integer, zero, or a negative integer as {@code first} is
	 *         greater than, equal to, or less than {@code second}, ignoring case
	 *         considerations in the ASCII range.
	 */
	public static int compareIgnoreCaseAscii(final CharSequence first, final CharSequence second) {
		final int firstLength = first.length();
		final int secondLength = second.length();
		final int minLength = Math.min(firstLength, secondLength);

		for (int index = 0; index < minLength; index += 1) {
			final int compare = Characters.compareIgnoreCaseAscii(first.charAt(index), second.charAt(index));
			if (compare != 0) {
				return compare;
			}
		}

		if (firstLength == secondLength) {
			return 0;
		}
		return firstLength < secondLength ? -1 : 1;
	}

	/**
	 * Tests if {@code value} contains {@code substring}, ignoring case
	 * considerations.
	 *
	 * @param string    the string to search in
	 * @param substring the sequence to search for
	 * @return {@code true} if {@code value} contains {@code substring}, ignoring
	 *         case considerations, else {@code false}
	 */
	public static boolean containsIgnoreCase(final CharSequence string, final CharSequence substring) {
		return indexOfIgnoreCase(string, substring) != -1;
	}

	/**
	 * Tests if {@code value} contains {@code substring}, ignoring case
	 * considerations in the ACII range.
	 *
	 * @param string    the string to search in
	 * @param substring the sequence to search for
	 * @return {@code true} if {@code value} contains {@code substring}, ignoring
	 *         case considerations in the ACII range, else {@code false}
	 */
	public static boolean containsIgnoreCaseAscii(final CharSequence string, final CharSequence substring) {
		return indexOfIgnoreCaseAscii(string, substring) != -1;
	}

	/**
	 * Compares {@code first} to {@code second}, ignoring case considerations in the
	 * ASCII range. Two strings are considered equal ignoring case if they are of
	 * the same length and corresponding characters in the two strings are equal
	 * ignoring case in the ASCII range.
	 *
	 * @param first  the first of both strings to compare
	 * @param second the second of both strings to compare
	 * @return {@code true} if {@code first} and {@code second} are considered
	 *         equal, ignoring case in the ACII range, else {@code false}
	 */
	@SuppressWarnings("PMD.CompareObjectsWithEquals")
	public static boolean equalsIgnoreCaseAscii(@Nullable final CharSequence first,
			@Nullable final CharSequence second) {
		if (first == null || second == null) {
			return first == second;
		}
		return compareIgnoreCaseAscii(first, second) == 0;
	}

	/**
	 * Tests if {@code value} ends with the specified suffix, ignoring case
	 * considerations.
	 *
	 * @param value  string to compare against
	 * @param suffix the suffix
	 * @return {@code true} if {@code prefix} is a suffix of the character sequence
	 *         represented by {@code value}, ignoring case considerations, else
	 *         {@code false}
	 */
	public static boolean endsWithIgnoreCase(final CharSequence value, final CharSequence suffix) {
		return startsWithIgnoreCase(value, suffix, value.length() - suffix.length());
	}

	/**
	 * Tests if {@code value} ends with the specified suffix, ignoring case
	 * considerations in the ASCII range.
	 *
	 * @param value  string to compare against
	 * @param suffix the suffix
	 * @return {@code true} if {@code prefix} is a suffix of the character sequence
	 *         represented by {@code value}, ignoring case considerations in the
	 *         ASCII range, else {@code false}
	 */
	public static boolean endsWithIgnoreCaseAscii(final CharSequence value, final CharSequence suffix) {
		return startsWithIgnoreCaseAscii(value, suffix, value.length() - suffix.length());
	}

	/**
	 * Tells whether or not a subsequence {@code input} matches {@code pattern}.
	 *
	 * <p>
	 * Use {@link Patterns#find(Pattern, CharSequence)} if you need a matcher.
	 *
	 * @param input   the input sequence to find the pattern in
	 * @param pattern the matching pattern
	 * @return {@code true} if, and only if, {@code input} matches {@code pattern}
	 */
	public static boolean find(final CharSequence input, final Pattern pattern) {
		return pattern.matcher(input).find();
	}

	/**
	 * Returns a formatted string using the specified format string and
	 * {@code arguments}. In that way this method works <i>similar</i> to
	 * {@link String#format(String, Object...)}.
	 *
	 * <p>
	 * Here are the differences:
	 * <ul>
	 * <li>In case {@code arguments} is empty, no checks and formatting is
	 * performed. {@code format} is returned immediately and without modification.
	 * <li>{@link #DEFAULT_LOCALE} is used instead of the systems default
	 * {@link Locale}.
	 * <li>On formatting failure no exception is thrown. When used for error
	 * messages this often hides the original error message. Instead a failure
	 * message is returned, containing {@code format}.
	 * </ul>
	 *
	 * @param format    <a href="../util/Formatter.html#syntax">format string</a>
	 * @param arguments arguments referenced by format specifiers in {@code format}
	 * @return formatted string
	 */
	@SuppressFBWarnings(value = "FORMAT_STRING_MANIPULATION",
			justification = "formatting exceptions are catched and handled accordingly")
	public static String format(final String format, final Object... arguments) {
		if (arguments.length < 1) {
			return format;
		}
		try (Formatter formatter = new Formatter(DEFAULT_LOCALE)) {
			return formatter.format(format, arguments).toString();
		} catch (final IllegalFormatException e) {
			return "Failed formatting string [" + format + "]: " + e.getMessage();
		}
	}

	/**
	 * Returns a map of binary units to their factor for static initialization.
	 *
	 * @return map map of binary units to their factor
	 */
	@SuppressWarnings("checkstyle:MultipleStringLiterals")
	private static Map<String, BigDecimal> getBinaryUnits() {
		final List<String> binaryUnits = asList("K", "M", "G", "T", "P", "E", "Z", "Y");
		final BigDecimal oneThousandTwentyFour = new BigDecimal(1024);

		return IntStream.range(0, binaryUnits.size())
				.boxed()
				.collect(toLinkedHashMap(binaryUnits::get, index -> oneThousandTwentyFour.pow(index + 1)));
	}

	/**
	 * Returns the index within {@code string} of the first occurrence of
	 * {@code substring}, ignoring e.g. case considerations using a custom character
	 * comparison method, starting at the specified index.
	 *
	 * @param string    the string to search in
	 * @param substring the substring to search for
	 * @param fromIndex the index from which to start the search
	 * @param equals    the character comparison method
	 * @return the index of the first occurrence of {@code substring}, starting at
	 *         the specified index, or {@code -1} if there is no such occurrence
	 */
	private static int indexOf(final CharSequence string,
			final CharSequence substring,
			final int fromIndex,
			final BiCharPredicate equals) {
		final int stringLength = string.length();

		// Fix index
		int index;
		if (fromIndex < 0) {
			index = 0;
		} else if (fromIndex >= stringLength) {
			index = stringLength;
		} else {
			index = fromIndex;
		}

		// Get first substring character
		if (substring.length() == 0) {
			return index;
		}
		final char substringFirst = substring.charAt(0);

		// Perform the search
		final int maxIndex = stringLength - substring.length();
		for (; index <= maxIndex; index += 1) {
			if (equals.test(string.charAt(index), substringFirst) && startsWith(string, substring, index, equals)) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * Returns the index within {@code string} of the first occurrence of
	 * {@code substring}, ignoring case considerations.
	 *
	 * @param string    the string to search in
	 * @param substring the substring to search for
	 * @return the index of the first occurrence of {@code substring}, ignoring case
	 *         considerations, or {@code -1} if there is no such occurrence
	 */
	public static int indexOfIgnoreCase(final CharSequence string, final CharSequence substring) {
		return indexOfIgnoreCase(string, substring, 0);
	}

	/**
	 * Returns the index within {@code string} of the first occurrence of
	 * {@code substring}, ignoring case considerations, starting at the specified
	 * index.
	 *
	 * @param string    the string to search in
	 * @param substring the substring to search for
	 * @param fromIndex the index from which to start the search
	 * @return the index of the first occurrence of {@code substring}, ignoring case
	 *         considerations, starting at the specified index, or {@code -1} if
	 *         there is no such occurrence
	 */
	public static int indexOfIgnoreCase(final CharSequence string, final CharSequence substring, final int fromIndex) {
		return indexOf(string, substring, fromIndex, Characters::equalsIgnoreCase);
	}

	/**
	 * Returns the index within {@code string} of the first occurrence of
	 * {@code substring}, ignoring case considerations in the ASCII range.
	 *
	 * @param string    the string to search in
	 * @param substring the substring to search for
	 * @return the index of the first occurrence of {@code substring}, ignoring case
	 *         considerations in the ASCII range, or {@code -1} if there is no such
	 *         occurrence
	 */
	public static int indexOfIgnoreCaseAscii(final CharSequence string, final CharSequence substring) {
		return indexOfIgnoreCaseAscii(string, substring, 0);
	}

	/**
	 * Returns the index within {@code string} of the first occurrence of
	 * {@code substring}, ignoring case considerations in the ASCII range, starting
	 * at the specified index.
	 *
	 * @param string    the string to search in
	 * @param substring the substring to search for
	 * @param fromIndex the index from which to start the search
	 * @return the index of the first occurrence of {@code substring}, ignoring case
	 *         considerations in the ASCII range, starting at the specified index,
	 *         or {@code -1} if there is no such occurrence
	 */
	public static int indexOfIgnoreCaseAscii(final CharSequence string,
			final CharSequence substring,
			final int fromIndex) {
		return indexOf(string, substring, fromIndex, Characters::equalsIgnoreCaseAscii);
	}

	/**
	 * Returns {@code true} if {@code value} consists of whitespace only or equals
	 * {@code null}.
	 *
	 * <p>
	 * This is an optimized way of {@code value.trim().isEmpty()}.
	 *
	 * @param value string
	 * @return {@code true} if {@code value} consists of whitespace only or equals
	 *         {@code null}
	 */
	public static boolean isBlank(@Nullable final CharSequence value) {
		if (value == null) {
			return true;
		}

		final int length = value.length();
		for (int index = 0; index < length; index += 1) {
			if (!Characters.isAsciiWhitespace(value.charAt(index))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Tells whether or not {@code input} matches {@code pattern}.
	 *
	 * <p>
	 * Use {@link Patterns#matches(Pattern, CharSequence)} if you need a matcher.
	 *
	 * @param input   the value to match
	 * @param pattern the matching pattern
	 * @return {@code true} if, and only if, {@code input} matches {@code pattern}
	 */
	public static boolean matches(final CharSequence input, final Pattern pattern) {
		return pattern.matcher(input).matches();
	}

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
	 *
	 * @param caseInsensitive {@code true} if comparison should take place case
	 *                        insensitive
	 * @return a {@link Comparator} that orders alpha numeric strings in
	 *         <i>natural</i> order, either case sensitive or case insensitive. It
	 *         is appreciated to use this kind of ordering for user output.
	 */
	public static Comparator<String> numericTextComparator(final boolean caseInsensitive) {
		return caseInsensitive
				? NumericTextComparator.COMPARATOR_CASE_INSENSITIVE
				: NumericTextComparator.COMPARATOR_CASE_SENSITIVE;
	}

	/**
	 * Parses {@code binaryValue} as binary value with
	 * <ul>
	 * <li>optional sign,
	 * <li>optional fraction
	 * <li>and optional binary unit (case insensitive matching).
	 * </ul>
	 *
	 * <p>
	 * Binary units multiply by 1024. Though strings with fractions can be parsed,
	 * the resulting value must not contain a fraction part. Numeric values can be
	 * formatted using underscore, just as numeric Java literals can be formatted.
	 *
	 * <p>
	 * <b>Examples:</b>
	 * <table>
	 * <caption>Examples</caption>
	 * <tr>
	 * <th>Parameter</th>
	 * <th>Calculation</th>
	 * <th>Return Value</th>
	 * </tr>
	 * <tr>
	 * <td>-2m</td>
	 * <td>-2 * 1024 * 1024</td>
	 * <td>-2 097 152</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td></td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5.4k</td>
	 * <td>5.4 * 1024</td>
	 * <td>5529.6 -&gt; ArithmeticException</td>
	 * </tr>
	 * <tr>
	 * <td>+5_432</td>
	 * <td></td>
	 * <td>5 432</td>
	 * </tr>
	 * </table>
	 *
	 * @param binaryValue binary string
	 * @return binary value
	 * @throws ParseException on parse failure
	 */
	@SuppressWarnings("checkstyle:MultipleStringLiterals")
	public static BigInteger parseBinaryUnit(final CharSequence binaryValue) throws ParseException {
		final Optional<Matcher> matcher = Patterns.matches(BINARY_UNIT_PATTERN, binaryValue);
		if (!matcher.isPresent()) {
			throw new ParseException("Value [%s] does not match binary unit pattern.", binaryValue);
		}

		final String value = matcher.get().group("value");
		if (value == null) {
			throw new ParseException("No binary unit value given in string [%s].", binaryValue);
		}

		final String unit = matcher.get().group("unit");
		final BigDecimal multiplicator = unit == null ? BigDecimal.ONE : BINARY_UNITS.get(toUpperCaseAscii(unit));

		return new BigDecimal(replaceAll(value, UNIT_WHITE_SPACE_PATTERN, "")).multiply(multiplicator)
				.toBigIntegerExact();
	}

	/**
	 * Parses {@code decimalValue} as decimal value with
	 * <ul>
	 * <li>optional sign,
	 * <li>optional fraction
	 * <li>and optional decimal unit (case insensitive matching - where possible).
	 * </ul>
	 *
	 * <p>
	 * Numeric values can be formatted using underscore, just as numeric Java
	 * literals can be formatted.
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
	 * <td>-2m</td>
	 * <td>-0.002</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5.4k</td>
	 * <td>5 400</td>
	 * </tr>
	 * <tr>
	 * <td>+5_432</td>
	 * <td>5 432</td>
	 * </tr>
	 * </table>
	 *
	 * @param decimalValue decimal string
	 * @return decimal value
	 * @throws ParseException on parse failure
	 */
	@SuppressWarnings("checkstyle:MultipleStringLiterals")
	public static BigDecimal parseDecimalUnit(final CharSequence decimalValue) throws ParseException {
		final Optional<Matcher> matcher = Patterns.matches(DECIMAL_UNIT_PATTERN, decimalValue);
		if (!matcher.isPresent()) {
			throw new ParseException("Value [%s] does not match decimal unit pattern.", decimalValue);
		}

		final String value = matcher.get().group("value");
		if (value == null) {
			throw new ParseException("No decimal unit value given in string [%s].", decimalValue);
		}

		final String unit = matcher.get().group("unit");

		final int powerOfTen = Optionals
				.getFirst(Objects::nonNull,
						() -> unit == null ? 0 : null,
						() -> DECIMAL_UNITS.get(unit),
						() -> DECIMAL_UNITS.get(toUpperCaseAscii(unit)),
						() -> DECIMAL_UNITS.get(toLowerCaseAscii(unit)))
				.orElseThrow(() -> new ParseException("Found unexpected decimal unit [%s].", unit));

		return new BigDecimal(replaceAll(value, UNIT_WHITE_SPACE_PATTERN, "")).scaleByPowerOfTen(powerOfTen);
	}

	/**
	 * Replaces the first subsequence of {@code input} that matches {@code pattern}
	 * with {@code replacement}.
	 *
	 * <p>
	 * Note that {@code replacement} is not a literal replacement string.
	 * Backslashes ({@code \}) and dollar signs ({@code $}) may be treated
	 * differently. Quote unknown literal replacement strings using
	 * {@link Matcher#quoteReplacement(String)}.
	 *
	 * @param input       the value to match
	 * @param pattern     the matching pattern
	 * @param replacement the replacement string
	 * @return the string constructed by replacing each matching subsequence by the
	 *         replacement string, substituting captured subsequences as needed
	 */
	public static String replaceFirst(final CharSequence input, final Pattern pattern, final String replacement) {
		return pattern.matcher(input).replaceFirst(replacement);
	}

	/**
	 * Replaces every subsequence of {@code input} that matches {@code pattern} with
	 * {@code replacement}.
	 *
	 * <p>
	 * Note that {@code replacement} is not a literal replacement string.
	 * Backslashes ({@code \}) and dollar signs ({@code $}) may be treated
	 * differently. Quote unknown literal replacement strings using
	 * {@link Matcher#quoteReplacement(String)}.
	 *
	 * @param input       the value to match
	 * @param pattern     the matching pattern
	 * @param replacement the replacement string
	 * @return the string constructed by replacing each matching subsequence by the
	 *         replacement string, substituting captured subsequences as needed
	 */
	public static String replaceAll(final CharSequence input, final Pattern pattern, final String replacement) {
		return pattern.matcher(input).replaceAll(replacement);
	}

	/**
	 * Tests if the substring of {@code value} beginning at {@code offset} starts
	 * with {@code prefix}, ignoring e.g. case considerations using a custom
	 * character comparison method.
	 *
	 * @param value  string to compare against
	 * @param prefix the prefix
	 * @param offset where to begin looking in {@code value}
	 * @param equals the character comparison method
	 * @return {@code true} if {@code prefix} is a prefix of the substring of
	 *         {@code value} starting at {@code offset}, else {@code false}. The
	 *         result is {@code false} if {@code offset} is negative or greater than
	 *         the length of {@code value}.
	 */
	private static boolean startsWith(final CharSequence value,
			final CharSequence prefix,
			final int offset,
			final BiCharPredicate equals) {
		final int prefixLength = prefix.length();
		if (offset < 0 || offset + prefixLength > value.length()) {
			return false;
		}

		for (int index = 0; index < prefixLength; index += 1) {
			if (!equals.test(value.charAt(offset + index), prefix.charAt(index))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Tests if {@code value} starts with {@code prefix} ignoring case
	 * considerations.
	 *
	 * @param value  string to compare against
	 * @param prefix the prefix
	 * @return {@code true} if {@code prefix} is a prefix of {@code value}, ignoring
	 *         case considerations, else {@code false}
	 */
	public static boolean startsWithIgnoreCase(final CharSequence value, final CharSequence prefix) {
		return startsWithIgnoreCase(value, prefix, 0);
	}

	/**
	 * Tests if the substring of {@code value} beginning at {@code offset} starts
	 * with {@code prefix}, ignoring case considerations.
	 *
	 * @param value  string to compare against
	 * @param prefix the prefix
	 * @param offset where to begin looking in {@code value}
	 * @return {@code true} if {@code prefix} is a prefix of the substring of
	 *         {@code value} starting at {@code offset}, ignoring case
	 *         considerations, else {@code false}. The result is {@code false} if
	 *         {@code offset} is negative or greater than the length of
	 *         {@code value}.
	 */
	public static boolean startsWithIgnoreCase(final CharSequence value, final CharSequence prefix, final int offset) {
		return startsWith(value, prefix, offset, Characters::equalsIgnoreCase);
	}

	/**
	 * Tests if {@code value} starts with the specified prefix, ignoring case
	 * considerations in the ASCII range.
	 *
	 * @param value  string to compare against
	 * @param prefix the prefix
	 * @return {@code true} if {@code prefix} is a prefix of the character sequence
	 *         represented by {@code value}, ignoring case considerations in the
	 *         ASCII range, else {@code false}
	 */
	public static boolean startsWithIgnoreCaseAscii(final CharSequence value, final CharSequence prefix) {
		return startsWithIgnoreCaseAscii(value, prefix, 0);
	}

	/**
	 * Tests if the substring of {@code value} beginning at the specified index
	 * starts with the specified prefix, ignoring case considerations in the ASCII
	 * range.
	 *
	 * @param value  string to compare against
	 * @param prefix the prefix
	 * @param offset where to begin looking in {@code value}.
	 * @return {@code true} if {@code prefix} is a prefix of the substring of
	 *         {@code value} starting at index {@code offset}, ignoring case
	 *         considerations in the ASCII range, else {@code false}. The result is
	 *         {@code false} if {@code offset} is negative or greater than the
	 *         length of this {@code String} object.
	 */
	public static boolean startsWithIgnoreCaseAscii(final CharSequence value,
			final CharSequence prefix,
			final int offset) {
		return startsWith(value, prefix, offset, Characters::equalsIgnoreCaseAscii);
	}

	/**
	 * Converts all of the ASCII upper case characters in {@code value} to lower
	 * case.
	 *
	 * @param value string to convert
	 * @return converted string
	 */
	public static String toLowerCaseAscii(final CharSequence value) {
		final int length = value.length();
		int index = 0;

		char lower = ' ';
		boolean found = false;
		for (; index < length && !found; index += 1) {
			final char character = value.charAt(index);
			lower = Characters.toLowerCaseAscii(character);
			found = character != lower;
		}
		if (index >= length) {
			return value.toString();
		}

		final StringBuilder builder = new StringBuilder(length);
		builder.append(value, 0, index - 1);
		builder.append(lower);
		for (; index < length; index += 1) {
			builder.append(Characters.toLowerCaseAscii(value.charAt(index)));
		}
		return builder.toString();
	}

	/**
	 * Converts all of the characters in {@code value} to lower case using
	 * {@link #DEFAULT_LOCALE}. This method is equivalent to
	 * {@code toLowerCase(Locale.ROOT)}.
	 *
	 * @param value string to convert
	 * @return converted string
	 */
	public static String toLowerCaseNeutral(final String value) {
		return value.toLowerCase(DEFAULT_LOCALE);
	}

	/**
	 * Converts {@code value} to title case by converting its first character using
	 * {@link Character#toTitleCase(char)} and following to lower case using
	 * {@link #DEFAULT_LOCALE}.
	 *
	 * @param value string to convert
	 * @return converted string
	 */
	public static String toTitleCaseNeutral(final String value) {
		if (value.isEmpty()) {
			return value;
		}

		final int length = value.length();
		if (length == 1) {
			return Character.toString(Character.toTitleCase(value.charAt(0)));
		}

		final String lowerCase = value.toLowerCase(DEFAULT_LOCALE);
		return new StringBuilder(length).append(Character.toTitleCase(value.charAt(0)))
				.append(lowerCase, 1, lowerCase.length())
				.toString();
	}

	/**
	 * Converts all of the ASCII lower case characters in {@code value} to upper
	 * case.
	 *
	 * @param value string to convert
	 * @return converted string
	 */
	public static String toUpperCaseAscii(final CharSequence value) {
		final int length = value.length();
		int index = 0;

		char upper = ' ';
		boolean found = false;
		for (; index < length && !found; index += 1) {
			final char character = value.charAt(index);
			upper = Characters.toUpperCaseAscii(character);
			found = character != upper;
		}
		if (index >= length) {
			return value.toString();
		}

		final StringBuilder builder = new StringBuilder(length);
		builder.append(value, 0, index - 1);
		builder.append(upper);
		for (; index < length; index += 1) {
			builder.append(Characters.toUpperCaseAscii(value.charAt(index)));
		}
		return builder.toString();
	}

	/**
	 * Converts all of the characters in {@code value} to upper case using
	 * {@link #DEFAULT_LOCALE}. This method is equivalent to
	 * {@code toUpperCase(Locale.ROOT)}.
	 *
	 * @param value string to convert
	 * @return converted string
	 */
	public static String toUpperCaseNeutral(final String value) {
		return value.toUpperCase(DEFAULT_LOCALE);
	}

	/**
	 * Returns a string similar to {@code value}, but with any leading whitespace
	 * removed.
	 *
	 * <p>
	 * This method works similar to {@link String#trim()}, though it handles only
	 * the strings start.
	 *
	 * <p>
	 * Whitespace characters are recognized using
	 * {@link Characters#isAsciiWhitespace(char)}.
	 *
	 * @param value value
	 * @return left trimmed value
	 */
	public static String trimStart(final CharSequence value) {
		final int length = value.length();

		int start = 0;
		while (start < length && Characters.isAsciiWhitespace(value.charAt(start))) {
			start += 1;
		}
		return value.subSequence(start, length).toString();
	}

	/**
	 * Returns a string similar to {@code value}, but with any trailing whitespace
	 * removed.
	 *
	 * <p>
	 * This method works similar to {@link String#trim()}, though it handles only
	 * the strings end.
	 *
	 * <p>
	 * Whitespace characters are recognized using
	 * {@link Characters#isAsciiWhitespace(char)}.
	 *
	 * @param value value
	 * @return left trimmed value
	 */
	public static String trimEnd(final CharSequence value) {
		int end = value.length();
		while (end > 0 && Characters.isAsciiWhitespace(value.charAt(end - 1))) {
			end -= 1;
		}
		return value.subSequence(0, end).toString();
	}

	/**
	 * Represents a predicate (boolean-valued function) of two primitive
	 * {@code char} arguments.
	 *
	 * <p>
	 * This is a functional interface whose functional method is
	 * {@link #test(char, char)}.
	 */
	@FunctionalInterface
	@SuppressWarnings("PMD.UnnecessaryModifier")
	private interface BiCharPredicate {
		/**
		 * Evaluates this predicate on the given arguments.
		 *
		 * @param first  the first input argument
		 * @param second the second input argument
		 * @return {@code true} if the input arguments match the predicate, otherwise
		 *         {@code false}
		 */
		boolean test(char first, char second);
	}
}
