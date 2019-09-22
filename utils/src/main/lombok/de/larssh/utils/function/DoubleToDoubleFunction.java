package de.larssh.utils.function;

/**
 * Represents a function that accepts an double-valued argument and produces an
 * double-valued result. This is the {@code double}-consuming and
 * {@code double}-producing primitive specialization for
 * {@link java.util.function.Function}.
 *
 * <p>
 * This is a <a href="package-summary.html">functional interface</a> whose
 * functional method is {@link #applyAsDouble(double)}.
 */
@FunctionalInterface
public interface DoubleToDoubleFunction {
	/**
	 * Applies this function to the given argument.
	 *
	 * @param value the function argument
	 * @return the function result
	 */
	double applyAsDouble(double value);
}
