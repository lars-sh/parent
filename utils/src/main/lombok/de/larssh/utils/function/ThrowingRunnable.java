package de.larssh.utils.function;

import de.larssh.utils.SneakyException;

/**
 * Represents a classes, that are intended to be executed by a thread.
 *
 * <p>
 * {@code ThrowingRunnable}s can throw any kind of {@link Exception}. When used
 * as {@link Runnable} exceptions are hidden from compiler.
 *
 * <p>
 * This is a <a href="package-summary.html">functional interface</a> whose
 * functional method is {@link #runThrowing()}.
 */
public interface ThrowingRunnable extends Runnable {
	/**
	 * Short-hand method to cast any {@link ThrowingRunnable} as {@link Runnable}.
	 *
	 * @param runnable throwing runnable
	 * @return hidden throwing runnable
	 */
	static Runnable throwing(final ThrowingRunnable runnable) {
		return runnable;
	}

	/**
	 * Runs this runnable, allow throwing any kind of {@link Exception}.
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
	 * @throws SneakyException hidden exceptions
	 */
	@Override
	@SuppressWarnings("PMD.AvoidCatchingGenericException")
	default void run() {
		try {
			runThrowing();
		} catch (final Exception e) {
			throw new SneakyException(e);
		}
	}

	/**
	 * Runs this runnable, allow throwing any kind of {@link Exception}.
	 *
	 * @throws Exception any kind of exception
	 */
	@SuppressWarnings("PMD.SignatureDeclareThrowsException")
	void runThrowing() throws Exception;
}
