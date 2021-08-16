package de.larssh.utils.text;

import static de.larssh.utils.Finals.constant;

import lombok.experimental.UtilityClass;

/**
 * {@link String#split(String)} and
 * {@link java.util.regex.Pattern#split(CharSequence)} should not be used as
 * trailing empty strings are discarded. That might be confusing.
 *
 * <p>
 * Use {@link java.util.regex.Pattern#split(CharSequence, int)} instead and
 * think about the {@code limit} parameter. Use {@link #NO_LIMIT} to not strip
 * empty trailing strings.
 */
@UtilityClass
@SuppressWarnings("PMD.ClassNamingConventions")
public class SplitLimit {
	/**
	 * No limit. Empty trailing strings are kept.
	 *
	 * <p>
	 * This value is meant to be used by either
	 * {@link java.util.regex.Pattern#split(CharSequence, int)} or
	 * {@link String#split(String, int)}.
	 */
	public static final int NO_LIMIT = constant(-1);

	/**
	 * No limit and strip empty trailing strings.
	 *
	 * <p>
	 * This value is meant to be used by either
	 * {@link java.util.regex.Pattern#split(CharSequence, int)} or
	 * {@link String#split(String, int)}.
	 */
	public static final int NO_LIMIT_AND_STRIP_EMPTY_TRAILING = constant(0);
}
