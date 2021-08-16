package de.larssh.utils.text;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Class to format and parse a list of words.
 *
 * <p>
 * Constants define instances of common formatters. The following is an example
 * using the words {@code abc}, {@code def} and {@code ghi}.
 * <table>
 * <caption>Examples</caption>
 * <tr>
 * <th>Formatter</th>
 * <th>Example</th>
 * </tr>
 * <tr>
 * <td>{@link #LOWER_CAMEL_CASE}</td>
 * <td>abcDefGhi</td>
 * </tr>
 * <tr>
 * <td>{@link #LOWER_KEBAB_CASE}</td>
 * <td>abc-def-ghi</td>
 * </tr>
 * <tr>
 * <td>{@link #LOWER_SNAKE_CASE}</td>
 * <td>abc_def_ghi</td>
 * </tr>
 * <tr>
 * <td>{@link #LOWER_WHITE_SPACE}</td>
 * <td>Abc def ghi</td>
 * </tr>
 * <tr>
 * <td>{@link #UPPER_CAMEL_CASE}</td>
 * <td>AbcDefGhi</td>
 * </tr>
 * <tr>
 * <td>{@link #UPPER_KEBAB_CASE}</td>
 * <td>ABC-DEF-GHI</td>
 * </tr>
 * <tr>
 * <td>{@link #UPPER_SNAKE_CASE}</td>
 * <td>ABC_DEF_GHI</td>
 * </tr>
 * <tr>
 * <td>{@link #UPPER_WHITE_SPACE}</td>
 * <td>Abc Def Ghi</td>
 * </tr>
 * </table>
 */
@Getter
@RequiredArgsConstructor
public class SentenceFormatter {
	/**
	 * Word separator for kebab space
	 */
	private static final String SEPARATOR_KEBAB_CASE = "-";

	/**
	 * Word separator for snake space
	 */
	private static final String SEPARATOR_SNAKE_CASE = "_";

	/**
	 * Word separator for white space
	 */
	private static final String SEPARATOR_WHITE_SPACE = " ";

	/**
	 * Lower Camel Case formatter
	 *
	 * <p>
	 * Example: abcDefGhi
	 *
	 * <p>
	 * Words are translated to title case, except the first words, which is
	 * translated to lower case.
	 */
	public static final SentenceFormatter LOWER_CAMEL_CASE
			= new SentenceFormatter(Strings::toLowerCaseNeutral, "", Strings::toTitleCaseNeutral);

	/**
	 * Lower Kebab Case formatter
	 *
	 * <p>
	 * Example: abc-def-ghi
	 *
	 * <p>
	 * Words are translated to lower case and are separated by minus character.
	 */
	public static final SentenceFormatter LOWER_KEBAB_CASE
			= new SentenceFormatter(Strings::toLowerCaseNeutral, SEPARATOR_KEBAB_CASE, Strings::toLowerCaseNeutral);

	/**
	 * Lower Snake Case formatter
	 *
	 * <p>
	 * Example: abc_def_ghi
	 *
	 * <p>
	 * Words are translated to lower case and are separated by underscore character.
	 */
	public static final SentenceFormatter LOWER_SNAKE_CASE
			= new SentenceFormatter(Strings::toLowerCaseNeutral, SEPARATOR_SNAKE_CASE, Strings::toLowerCaseNeutral);

	/**
	 * Lower White Space formatter
	 *
	 * <p>
	 * Example: Abc def ghi
	 *
	 * <p>
	 * Words are translated to lower case, except the first word, which is
	 * translated to title case, and are separated by white space character.
	 */
	public static final SentenceFormatter LOWER_WHITE_SPACE
			= new SentenceFormatter(Strings::toTitleCaseNeutral, SEPARATOR_WHITE_SPACE, Strings::toLowerCaseNeutral);

	/**
	 * Upper Camel Case formatter
	 *
	 * <p>
	 * Example: AbcDefGhi
	 *
	 * <p>
	 * Words are translated to title case.
	 */
	public static final SentenceFormatter UPPER_CAMEL_CASE
			= new SentenceFormatter(Strings::toTitleCaseNeutral, "", Strings::toTitleCaseNeutral);

	/**
	 * Upper Kebab Case formatter
	 *
	 * <p>
	 * Example: ABC-DEF-GHI
	 *
	 * <p>
	 * Words are translated to upper case and are separated by minus character.
	 */
	public static final SentenceFormatter UPPER_KEBAB_CASE
			= new SentenceFormatter(Strings::toUpperCaseNeutral, SEPARATOR_KEBAB_CASE, Strings::toUpperCaseNeutral);

	/**
	 * Upper Snake Case formatter
	 *
	 * <p>
	 * Example: ABC_DEF_GHI
	 *
	 * <p>
	 * Words are translated to upper case and are separated by underscore character.
	 */
	public static final SentenceFormatter UPPER_SNAKE_CASE
			= new SentenceFormatter(Strings::toUpperCaseNeutral, SEPARATOR_SNAKE_CASE, Strings::toUpperCaseNeutral);

	/**
	 * Upper White Space formatter
	 *
	 * <p>
	 * Example: Abc Def Ghi
	 *
	 * <p>
	 * Words are translated to title case and are separated by white space
	 * character.
	 */
	public static final SentenceFormatter UPPER_WHITE_SPACE
			= new SentenceFormatter(Strings::toTitleCaseNeutral, SEPARATOR_WHITE_SPACE, Strings::toTitleCaseNeutral);

	/**
	 * Function to convert the first word
	 *
	 * @return convert function for the first word of a sentence
	 */
	Function<String, String> convertFirstWord;

	/**
	 * Separator character between words
	 *
	 * @return separator
	 */
	String separator;

	/**
	 * Function to convert all words except the first word
	 *
	 * @return convert function for subsequent words
	 */
	Function<String, String> convertSubsequentWords;

	/**
	 * Formats the list of given words using the specified converter functions and
	 * separator.
	 *
	 * @param words list of words
	 * @return formatted sentence
	 */
	public String format(final String... words) {
		return format(asList(words));
	}

	/**
	 * Formats the list of given words using the specified converter functions and
	 * separator.
	 *
	 * @param words list of words
	 * @return formatted sentence
	 */
	public String format(final List<String> words) {
		if (words.isEmpty()) {
			return "";
		}

		final StringBuilder builder = new StringBuilder();
		builder.append(getConvertFirstWord().apply(words.get(0)));

		final int size = words.size();
		for (int index = 1; index < size; index += 1) {
			builder.append(getSeparator());
			builder.append(getConvertSubsequentWords().apply(words.get(index)));
		}
		return builder.toString();
	}

	/**
	 * Splits the given sentence into words using the separator. If the separator
	 * string is empty, the sentence is splitted by title characters. A leading
	 * title character does not generate an empty leading word.
	 *
	 * @param sentence the sentence to be splitted
	 * @return list of splitted words
	 */
	public List<String> parse(final String sentence) {
		if (sentence.isEmpty()) {
			return emptyList();
		}
		if (getSeparator().isEmpty()) {
			return splitByTitleCharacters(sentence);
		}
		return splitBySeparator(sentence);
	}

	/**
	 * Splits the given sentence into words using the separator string.
	 *
	 * @param sentence the sentence to be splitted
	 * @return list of splitted words
	 */
	private List<String> splitBySeparator(final String sentence) {
		final int sentenceLength = sentence.length();
		final int separatorLength = getSeparator().length();
		final int length = sentenceLength - separatorLength;

		int beginIndex = 0;
		final List<String> words = new ArrayList<>();
		for (int index = 0; index < length; index += 1) {
			if (sentence.regionMatches(index, getSeparator(), 0, separatorLength)) {
				words.add(sentence.substring(beginIndex, index));
				beginIndex = index + separatorLength;
			}
		}
		words.add(sentence.substring(beginIndex, sentenceLength));
		return words;
	}

	/**
	 * Splits the given sentence into words by title characters. A leading title
	 * character does not generate an empty leading word.
	 *
	 * @param sentence the sentence to be splitted
	 * @return list of splitted words
	 */
	private List<String> splitByTitleCharacters(final String sentence) {
		final int length = sentence.length();

		int beginIndex = 0;
		final List<String> words = new ArrayList<>();
		for (int index = 1; index < length; index += 1) {
			if (Character.isUpperCase(sentence.charAt(index))) {
				words.add(sentence.substring(beginIndex, index));
				beginIndex = index;
			}
		}
		words.add(sentence.substring(beginIndex, length));
		return words;
	}
}
