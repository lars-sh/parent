package de.larssh.utils.text;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link Pattern}.
 */
@UtilityClass
public class Patterns {
	/**
	 * Attempts to find the first subsequence of the input sequence that matches the
	 * given pattern.
	 *
	 * <p>
	 * Use {@link Strings#find(CharSequence, Pattern)} if you do <b>not</b> need a
	 * matcher.
	 *
	 * @param pattern the pattern
	 * @param input   the input sequence to find the pattern in
	 * @return if the match succeeds then more information can be obtained via the
	 *         returned {@link Matcher} object or else {@link Optional#empty()} is
	 *         returned
	 */
	public static Optional<Matcher> find(final Pattern pattern, final CharSequence input) {
		final Matcher matcher = pattern.matcher(input);
		return matcher.find() ? Optional.of(matcher) : Optional.empty();
	}

	/**
	 * Attempts to match the entire region against the pattern.
	 *
	 * <p>
	 * Use {@link Strings#matches(CharSequence, Pattern)} if you do <b>not</b> need
	 * a matcher.
	 *
	 * @param pattern the pattern
	 * @param input   the input sequence to match against the pattern
	 * @return if the match succeeds then more information can be obtained via the
	 *         returned {@link Matcher} object or else {@link Optional#empty()} is
	 *         returned
	 */
	public static Optional<Matcher> matches(final Pattern pattern, final CharSequence input) {
		final Matcher matcher = pattern.matcher(input);
		return matcher.matches() ? Optional.of(matcher) : Optional.empty();
	}
}
