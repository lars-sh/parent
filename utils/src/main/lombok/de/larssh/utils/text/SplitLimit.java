package de.larssh.utils.text;

import static de.larssh.utils.Finals.constant;

import lombok.experimental.UtilityClass;

/**
 * {@link String#split(String)} and
 * {@link java.util.regex.Pattern#split(CharSequence)} should not be used as
 * trailing empty strings will be discarded. That is something which might be
 * confusing.
 *
 * <p>
 * Use {@link java.util.regex.Pattern#split(CharSequence, int)} instead and
 * think about the {@code limit} parameter. Use {@link #NONE} to not discard
 * trailing empty strings.
 */
@UtilityClass
public class SplitLimit {
	/**
	 * No limit and do not discard trailing empty strings.
	 *
	 * <p>
	 * This value is meant to be used by either
	 * {@link java.util.regex.Pattern#split(CharSequence, int)} or
	 * {@link String#split(String, int)}.
	 */
	public static final int NONE = constant(-1);

	/**
	 * No limit and discard trailing empty strings.
	 *
	 * <p>
	 * This value is meant to be used by either
	 * {@link java.util.regex.Pattern#split(CharSequence, int)} or
	 * {@link String#split(String, int)}.
	 */
	public static final int NONE_AND_NO_EMPTY_TRAILING = constant(0);
}
