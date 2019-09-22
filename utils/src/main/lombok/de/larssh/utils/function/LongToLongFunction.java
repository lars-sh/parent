package de.larssh.utils.function;

/**
 * Represents a function that accepts an long-valued argument and produces an
 * long-valued result. This is the {@code long}-consuming and
 * {@code long}-producing primitive specialization for
 * {@link java.util.function.Function}.
 *
 * <p>
 * This is a <a href="package-summary.html">functional interface</a> whose
 * functional method is {@link #applyAsLong(long)}.
 */
@FunctionalInterface
public interface LongToLongFunction {
	/**
	 * Applies this function to the given argument.
	 *
	 * @param value the function argument
	 * @return the function result
	 */
	long applyAsLong(long value);
}
