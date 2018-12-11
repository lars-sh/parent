package de.larssh.utils.text;

import lombok.ToString;

/**
 * Thrown to indicate that a string could not be parsed.
 */
@ToString
// @EqualsAndHashCode(callSuper = true, onParam_ = { @Nullable })
public class ParseException extends Exception {
	private static final long serialVersionUID = 4898591627790201970L;

	/**
	 * Constructs a new {@link ParseException} with the given message, formatting as
	 * described at {@link Strings#format(String, Object...)}.
	 *
	 * @param message   the detail message
	 * @param arguments arguments referenced by format specifiers in {@code message}
	 * @throws java.util.IllegalFormatException {@code arguments} is not empty and
	 *         {@code format} contains unexpected syntax
	 */
	public ParseException(final String message, final Object... arguments) {
		super(Strings.format(message, arguments));
	}

	/**
	 * Constructs a new {@link ParseException} with the given message, formatting as
	 * described at {@link Strings#format(String, Object...)}.
	 *
	 * @param cause     the cause
	 * @param message   the detail message
	 * @param arguments arguments referenced by format specifiers in {@code message}
	 * @throws java.util.IllegalFormatException {@code arguments} is not empty and
	 *         {@code format} contains unexpected syntax
	 */
	public ParseException(final Throwable cause, final String message, final Object... arguments) {
		super(Strings.format(message, arguments), cause);
	}
}
