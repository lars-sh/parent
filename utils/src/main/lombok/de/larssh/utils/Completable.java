package de.larssh.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;

/**
 * Implementation of a boolean {@code completed} status, allowing objects to
 * differentiate between an initialization phase and their final value. This
 * implementation is synchronized.
 *
 * <p>
 * Each getter is recommended to call {@code #complete()}, while
 * {@code #throwIfCompleted()} must be called prior to object modification.
 *
 * <p>
 * Before using {@code Completable} think about using either a non-mobifiable
 * constructor or a builder class.
 */
@Getter
@RequiredArgsConstructor
public abstract class Completable {
	/**
	 * Completed status
	 *
	 * @return completed status
	 */
	@NonFinal
	volatile boolean completed = false;

	/**
	 * Object used for locking
	 */
	@Getter(AccessLevel.NONE)
	Object lock = new Object();

	/**
	 * Finishes the objects initialization phase.
	 *
	 * <p>
	 * This method is recommended to be called by each getter.
	 */
	protected void complete() {
		completed = true;
	}

	/**
	 * In case the object has already been completed a {@link CompletedException} is
	 * thrown. Else nothing happens.
	 *
	 * <p>
	 * This method must be called by prior to object modification.
	 *
	 * @throws CompletedException if object is completed
	 */
	protected void throwIfCompleted() {
		if (completed) {
			throw new CompletedException();
		}
	}
}
