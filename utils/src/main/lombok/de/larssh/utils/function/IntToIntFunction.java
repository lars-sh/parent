package de.larssh.utils.function;

/**
 * Represents a function that accepts an int-valued argument and produces an
 * int-valued result. This is the {@code int}-consuming and
 * {@code int}-producing primitive specialization for
 * {@link java.util.function.Function}.
 *
 * <p>
 * This is a <a href="package-summary.html">functional interface</a> whose
 * functional method is {@link #applyAsInt(int)}.
 */
@FunctionalInterface
public interface IntToIntFunction {
	/**
	 * Applies this function to the given argument.
	 *
	 * @param value the function argument
	 * @return the function result
	 */
	int applyAsInt(int value);
}
