package de.larssh.utils.test;

/**
 * Thrown to indicate that an assertion failed in an unexpected situation.
 */
public class AssertionException extends RuntimeException {
	// @EqualsAndHashCode(callSuper = true, onParam_ = { @Nullable })

	private static final long serialVersionUID = -7622697337052594786L;

	/**
	 * Constructs a new {@link AssertionException} with the given message,
	 * formatting as described at {@link String#format(String, Object...)}.
	 *
	 * @param message   the detail message
	 * @param arguments arguments referenced by format specifiers in {@code message}
	 * @throws java.util.IllegalFormatException {@code arguments} is not empty and
	 *         {@code message} contains unexpected syntax
	 */
	public AssertionException(final String message, final Object... arguments) {
		super(String.format(message, arguments), null);
	}

	/**
	 * Constructs a new {@link AssertionException} with the given message,
	 * formatting as described at {@link String#format(String, Object...)}.
	 *
	 * @param cause     the cause
	 * @param message   the detail message
	 * @param arguments arguments referenced by format specifiers in {@code message}
	 * @throws java.util.IllegalFormatException {@code arguments} is not empty and
	 *         {@code message} contains unexpected syntax
	 */
	public AssertionException(final Throwable cause, final String message, final Object... arguments) {
		super(String.format(message, arguments), cause);
	}
}
