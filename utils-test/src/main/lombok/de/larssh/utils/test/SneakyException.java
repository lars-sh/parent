package de.larssh.utils.test;

import de.larssh.utils.annotations.PackagePrivate;

/**
 * SneakyException allow rethrowing any kind of {@link Exception}, hiding them
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
 *
 * <p>
 * <b>This class has been copied from "utils" module and must not be used
 * publicly!</b>
 */
@PackagePrivate
final class SneakyException extends RuntimeException {
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
	 * @param throwable throwable to be hidden
	 */
	@PackagePrivate
	SneakyException(final Throwable throwable) {
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
	private <T extends Throwable> void rethrow(final Throwable throwable) throws T {
		throw (T) throwable;
	}
}
