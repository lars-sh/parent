package de.larssh.utils.text;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Thrown to indicate that a string could not be parsed.
 */
@SuppressFBWarnings(value = "FCCD_FIND_CLASS_CIRCULAR_DEPENDENCY",
		justification = "Circular dependency to de.larssh.utils.text.Strings accepted")
public class StringParseException extends Exception {
	/**
	 * Constructs a new {@link StringParseException} with the given message,
	 * formatting as described at {@link Strings#format(String, Object...)}.
	 *
	 * @param message   the detail message
	 * @param arguments arguments referenced by format specifiers in {@code message}
	 */
	public StringParseException(final String message, final Object... arguments) {
		super(Strings.format(message, arguments), null);
	}

	/**
	 * Constructs a new {@link StringParseException} with the given message,
	 * formatting as described at {@link Strings#format(String, Object...)}.
	 *
	 * @param cause     the cause
	 * @param message   the detail message
	 * @param arguments arguments referenced by format specifiers in {@code message}
	 */
	public StringParseException(final Throwable cause, final String message, final Object... arguments) {
		super(Strings.format(message, arguments), cause);
	}
}
