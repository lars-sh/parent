package de.larssh.utils;

/**
 * Thrown to indicate that an object has been completed prior modifying.
 */
public class CompletedException extends RuntimeException {
	// @EqualsAndHashCode(callSuper = true, onParam_ = { @Nullable })

	private static final long serialVersionUID = -469617111369439014L;

	/**
	 * Constructs a new {@link CompletedException} with the default detail message.
	 */
	public CompletedException() {
		super("Object has been completed prior modifying.", null);
	}
}
