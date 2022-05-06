package de.larssh.utils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * SneakyException allows rethrowing any kind of {@link Exception}, hiding them
 * from compiler checks.
 *
 * <p>
 * The calling method should either:
 * <ul>
 * <li>have a {@code throws} statement for the hidden exceptions
 * <li>handle hidden exceptions using {@code catch}
 * <li>or at least document the hidden exceptions in JavaDoc using
 * {@code @throws}.
 * </ul>
 */
public final class SneakyException extends RuntimeException {
	/**
	 * Rethrows {@code throwable}, hiding it from compiler checks.
	 *
	 * <p>
	 * The calling method should either:
	 * <ul>
	 * <li>have a {@code throws} statement for the hidden exceptions
	 * <li>handle hidden exceptions using {@code catch}
	 * <li>or at least document the hidden exceptions in JavaDoc using
	 * {@code @throws}.
	 * </ul>
	 *
	 * <p>
	 * To hide {@link java.io.IOException}s from compiler checks the usage of
	 * {@link java.io.UncheckedIOException} is highly recommended instead.
	 *
	 * @param throwable throwable to be hidden
	 */
	public SneakyException(final Throwable throwable) {
		super(throwable);

		rethrow(throwable);
	}

	/**
	 * Rethrows {@code throwable}, hiding it from compiler checks.
	 *
	 * <p>
	 * This is not part of the constructor to hide the public API from generic type
	 * {@code T}.
	 *
	 * @param <T>       generic throwable
	 * @param throwable throwable to be hidden
	 * @throws T hidden throwable
	 */
	@SuppressWarnings("unchecked")
	@SuppressFBWarnings(value = "THROWS_METHOD_THROWS_CLAUSE_THROWABLE",
			justification = "this is the way sneaky exceptions work")
	private <T extends Throwable> void rethrow(final Throwable throwable) throws T {
		throw (T) throwable;
	}
}
