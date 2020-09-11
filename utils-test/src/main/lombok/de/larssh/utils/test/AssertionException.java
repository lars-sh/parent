package de.larssh.utils.test;

/**
 * Thrown to indicate that an assertion failed in an unexpected situation.
 */
public class AssertionException extends RuntimeException {
	/**
	 * Constructs a new {@link AssertionException} with the given message.
	 *
	 * @param message the detail message
	 */
	public AssertionException(final String message) {
		super(message, null);
	}

	/**
	 * Constructs a new {@link AssertionException} with the given message.
	 *
	 * @param cause   the cause
	 * @param message the detail message
	 */
	public AssertionException(final Throwable cause, final String message) {
		super(message, cause);
	}
}
