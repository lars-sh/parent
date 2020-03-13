package de.larssh.utils.text;

import static de.larssh.utils.Collectors.toLinkedHashMap;
import static de.larssh.utils.Finals.constant;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
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
@SuppressWarnings({ "PMD.ExcessiveImports", "PMD.GodClass" })
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
	 * Tests if this string ends with the specified suffix, ignoring case
	 * considerations.
	 *
	 * @param value  string to compare against
	 * @param suffix the suffix.
	 * @return {@code true} if the character sequence represented by the argument is
	 *         a suffix of the character sequence represented by this object,
	 *         ignoring case considerations; {@code false} otherwise.
	 */
	public static boolean endsWithIgnoreCase(final String value, final String suffix) {
		return startsWithIgnoreCase(value, suffix, value.length() - suffix.length());
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
	 * Splits {@code value} into lines using {@link BufferedReader#lines()}.
	 *
	 * @param value value to split
	 * @return list of lines
	 */
	@SuppressFBWarnings(value = "EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS",
			justification = "converting checked to unchecked exception, that should never be thrown at all")
	public static List<String> getLines(final String value) {
		try (BufferedReader reader = new BufferedReader(new StringReader(value))) {
			return reader.lines().collect(toList());
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
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
	public static BigInteger parseBinaryUnit(final String binaryValue) throws ParseException {
		final Optional<Matcher> matcher = Patterns.matches(BINARY_UNIT_PATTERN, binaryValue);
		if (!matcher.isPresent()) {
			throw new ParseException("Value [%s] does not match binary unit pattern.", binaryValue);
		}

		final String value = matcher.get().group("value");
		if (value == null) {
			throw new ParseException("No binary unit value given in string [%s].", binaryValue);
		}

		final String unit = matcher.get().group("unit");
		final BigDecimal multiplicator
				= unit == null ? BigDecimal.ONE : BINARY_UNITS.get(Strings.toNeutralUpperCase(unit));

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
	public static BigDecimal parseDecimalUnit(final String decimalValue) throws ParseException {
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
						() -> DECIMAL_UNITS.get(Strings.toNeutralUpperCase(unit)),
						() -> DECIMAL_UNITS.get(Strings.toNeutralLowerCase(unit)))
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
	 * Tests if this string starts with the specified prefix, ignoring case
	 * considerations.
	 *
	 * @param value  string to compare against
	 * @param prefix the prefix.
	 * @return {@code true} if the character sequence represented by the argument is
	 *         a prefix of the character sequence represented by this string,
	 *         ignoring case considerations; {@code false} otherwise.
	 */
	public static boolean startsWithIgnoreCase(final String value, final String prefix) {
		return startsWithIgnoreCase(value, prefix, 0);
	}

	/**
	 * Tests if the substring of this string beginning at the specified index starts
	 * with the specified prefix, ignoring case considerations.
	 *
	 * @param value  string to compare against
	 * @param prefix the prefix.
	 * @param offset where to begin looking in this string.
	 * @return {@code true} if the character sequence represented by the argument is
	 *         a prefix of the substring of this object starting at index
	 *         {@code offset}, ignoring case considerations; {@code false}
	 *         otherwise. The result is {@code false} if {@code offset} is negative
	 *         or greater than the length of this {@code String} object.
	 */
	public static boolean startsWithIgnoreCase(final String value, final String prefix, final int offset) {
		final int prefixLength = prefix.length();
		if (offset < 0 || offset + prefixLength > value.length()) {
			return false;
		}

		for (int index = 0; index < prefixLength; index += 1) {
			if (!Characters.equalsIgnoreCase(value.charAt(offset + index), prefix.charAt(index))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Converts all of the characters in {@code value} to lower case using
	 * {@link #DEFAULT_LOCALE}. This method is equivalent to
	 * {@code toLowerCase(Locale.ROOT)}.
	 *
	 * @param value string to convert
	 * @return converted string
	 */
	public static String toNeutralLowerCase(final String value) {
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
	public static String toNeutralTitleCase(final String value) {
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
	 * Converts all of the characters in {@code value} to upper case using
	 * {@link #DEFAULT_LOCALE}. This method is equivalent to
	 * {@code toUpperCase(Locale.ROOT)}.
	 *
	 * @param value string to convert
	 * @return converted string
	 */
	public static String toNeutralUpperCase(final String value) {
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
	public static String trimStart(final String value) {
		int start = 0;
		final int length = value.length();
		while (start < length && Characters.isAsciiWhitespace(value.charAt(start))) {
			start += 1;
		}
		return value.substring(start);
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
	public static String trimEnd(final String value) {
		int end = value.length();
		while (end > 0 && Characters.isAsciiWhitespace(value.charAt(end - 1))) {
			end -= 1;
		}
		return value.substring(0, end);
	}
}
