package de.larssh.utils.text;

/**
 * Thrown to indicate that a string could not be parsed.
 */
public class ParseException extends Exception {
	private static final long serialVersionUID = 4898591627790201970L;

	/**
	 * Constructs a new {@link ParseException} with the given message, formatting as
	 * described at {@link Strings#format(String, Object...)}.
	 *
	 * @param message   the detail message
	 * @param arguments arguments referenced by format specifiers in {@code message}
	 */
	public ParseException(final String message, final Object... arguments) {
		super(Strings.format(message, arguments), null);
	}

	/**
	 * Constructs a new {@link ParseException} with the given message, formatting as
	 * described at {@link Strings#format(String, Object...)}.
	 *
	 * @param cause     the cause
	 * @param message   the detail message
	 * @param arguments arguments referenced by format specifiers in {@code message}
	 */
	public ParseException(final Throwable cause, final String message, final Object... arguments) {
		super(Strings.format(message, arguments), cause);
	}
}
