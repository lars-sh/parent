package de.larssh.utils.function;

import java.util.function.Supplier;

import de.larssh.utils.SneakyException;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Represents a supplier of results. There is no requirement that a new or
 * distinct result be returned each time the supplier is invoked.
 *
 * <p>
 * {@code ThrowingSupplier}s can throw any kind of {@link Exception}. When used
 * as {@link Supplier} exceptions are hidden from compiler.
 *
 * <p>
 * This is a <a href="package-summary.html">functional interface</a> whose
 * functional method is {@link #get()}.
 *
 * @param <T> the type of results supplied by this supplier
 */
@FunctionalInterface
public interface ThrowingSupplier<T> extends Supplier<T> {
	/**
	 * Short-hand method to cast any {@link ThrowingSupplier} as {@link Supplier}.
	 *
	 * @param supplier throwing supplier
	 * @param          <T> the type of results supplied by this supplier
	 * @return hidden throwing supplier
	 */
	static <T> Supplier<T> throwing(final ThrowingSupplier<T> supplier) {
		return supplier;
	}

	/**
	 * Gets a result, allow throwing any kind of {@link Exception}.
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
	 * @return a result
	 * @throws SneakyException hidden exceptions
	 */
	@Nullable
	@Override
	@SuppressWarnings("PMD.AvoidCatchingGenericException")
	default T get() {
		try {
			return getThrowing();
		} catch (final Exception e) {
			throw new SneakyException(e);
		}
	}

	/**
	 * Gets a result, allow throwing any kind of {@link Exception}.
	 *
	 * @return a result
	 * @throws Exception any kind of exception
	 */
	@Nullable
	@SuppressWarnings("PMD.SignatureDeclareThrowsException")
	T getThrowing() throws Exception;
}
