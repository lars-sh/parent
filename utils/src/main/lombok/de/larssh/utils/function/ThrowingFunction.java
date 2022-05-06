package de.larssh.utils.function;

import java.util.function.Function;

import de.larssh.utils.SneakyException;
import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Represents a function that accepts one argument and produces a result.
 *
 * <p>
 * {@code ThrowingFunction}s can throw any kind of {@link Exception}. When used
 * as {@link Function} exceptions are hidden from compiler.
 *
 * <p>
 * This is a <a href="package-summary.html">functional interface</a> whose
 * functional method is {@link #applyThrowing(Object)}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> extends Function<T, R> {
	/**
	 * Short-hand method to cast any {@link ThrowingFunction} as {@link Function}.
	 *
	 * @param function throwing function
	 * @param <T>      the type of the input to the function
	 * @param <R>      the type of the result of the function
	 * @return hidden throwing function
	 */
	static <T, R> Function<T, R> throwing(final ThrowingFunction<T, R> function) {
		return function;
	}

	/**
	 * Applies this function to the given argument, allow throwing any kind of
	 * {@link Exception}.
	 *
	 * <p>
	 * As this hides exceptions from the compiler, the calling method should either:
	 * <ul>
	 * <li>have a {@code throws} statement for the hidden exceptions
	 * <li>handle hidden exceptions using {@code catch}
	 * <li>or at least document the hidden exceptions in JavaDoc using
	 * {@code @throws}.
	 * </ul>
	 *
	 * @param value the function argument
	 * @return the function result
	 * @throws SneakyException hidden exceptions
	 */
	@Nullable
	@Override
	@SuppressWarnings({ "checkstyle:IllegalCatch", "PMD.AvoidCatchingGenericException" })
	default R apply(@Nullable final T value) {
		try {
			return applyThrowing(value);
		} catch (final Exception e) {
			throw new SneakyException(e);
		}
	}

	/**
	 * Applies this function to the given argument, allow throwing any kind of
	 * {@link Exception}.
	 *
	 * @param value the function argument
	 * @return the function result
	 * @throws Exception any kind of exception
	 */
	@Nullable
	@SuppressWarnings("PMD.SignatureDeclareThrowsException")
	@SuppressFBWarnings(value = "THROWS_METHOD_THROWS_CLAUSE_BASIC_EXCEPTION",
			justification = "this is the way throwing functions work")
	R applyThrowing(@Nullable T value) throws Exception;
}
