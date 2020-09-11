package de.larssh.utils;

/**
 * Thrown to indicate that an object has been completed prior modifying.
 */
public class CompletedException extends RuntimeException {
	/**
	 * Constructs a new {@link CompletedException} with the default detail message.
	 */
	public CompletedException() {
		super("Object has been completed prior modifying.", null);
	}
}
