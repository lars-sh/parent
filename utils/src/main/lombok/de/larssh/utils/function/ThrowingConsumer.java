package de.larssh.utils.function;

import java.util.function.Consumer;

import de.larssh.utils.SneakyException;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Represents an operation that accepts a single input argument and returns no
 * result. Unlike most other functional interfaces, {@code ThrowingConsumer} is
 * expected to operate via side-effects.
 *
 * <p>
 * {@code ThrowingConsumer}s can throw any kind of {@link Exception}. When used
 * as {@link Consumer} exceptions are hidden from compiler.
 *
 * <p>
 * This is a <a href="package-summary.html">functional interface</a> whose
 * functional method is {@link #acceptThrowing(Object)}.
 *
 * @param <T> the type of the input to the operation
 */
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {
	/**
	 * Short-hand method to cast any {@link ThrowingConsumer} as {@link Consumer}.
	 *
	 * @param consumer throwing consumer
	 * @param          <T> the type of the input to the operation
	 * @return hidden throwing consumer
	 */
	static <T> Consumer<T> throwing(final ThrowingConsumer<T> consumer) {
		return consumer;
	}

	/**
	 * Performs this operation on the given argument, allow throwing any kind of
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
	 * @param t the input argument
	 * @throws SneakyException hidden exceptions
	 */
	@Override
	@SuppressWarnings({ "checkstyle:IllegalCatch", "PMD.AvoidCatchingGenericException" })
	default void accept(@Nullable final T t) {
		try {
			acceptThrowing(t);
		} catch (final Exception e) {
			throw new SneakyException(e);
		}
	}

	/**
	 * Performs this operation on the given argument, allow throwing any kind of
	 * {@link Exception}.
	 *
	 * @param t the input argument
	 * @throws Exception any kind of exception
	 */
	@SuppressWarnings("PMD.SignatureDeclareThrowsException")
	void acceptThrowing(@Nullable T t) throws Exception;
}
