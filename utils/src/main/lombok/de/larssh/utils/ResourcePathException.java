package de.larssh.utils;

import de.larssh.utils.text.Strings;

/**
 * Thrown to indicate that a resource path is invalid.
 */
public class ResourcePathException extends RuntimeException {
	/**
	 * Constructs a new {@link ResourcePathException} with the given message,
	 * formatting as described at {@link Strings#format(String, Object...)}.
	 *
	 * @param message   the detail message
	 * @param arguments arguments referenced by format specifiers in {@code message}
	 */
	public ResourcePathException(final String message, final Object... arguments) {
		super(Strings.format(message, arguments), null);
	}
}
