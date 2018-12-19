package de.larssh.utils;

import lombok.ToString;

/**
 * Thrown to indicate that an object contains more than one element while either
 * no or exactly one element are expected.
 */
@ToString
// @EqualsAndHashCode(callSuper = true, onParam_ = { @Nullable })
public class TooManyElementsException extends RuntimeException {
	private static final long serialVersionUID = -1912601313948443590L;

	/**
	 * Constructs a new {@link TooManyElementsException} with the default detail
	 * message.
	 */
	public TooManyElementsException() {
		super("Object contains more than one element. Expected either no or exactly one element.", null);
	}
}
