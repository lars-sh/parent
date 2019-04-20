package de.larssh.utils;

import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class implements a boolean {@code completed} status, allowing objects to
 * differentiate between an initialization phase and their final value.
 *
 * <p>
 * Each getter is recommended to call {@code #complete()}, while
 * {@code #throwIfCompleted()} must be called prior to object modification.
 *
 * <p>
 * Before using {@code Completable} think about using either a streamlined
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
	AtomicBoolean completed = new AtomicBoolean(false);

	/**
	 * Finishes the objects initialization phase.
	 *
	 * <p>
	 * This method is recommended to be called by each getter.
	 */
	protected void complete() {
		completed.set(true);
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
		if (completed.get()) {
			throw new CompletedException();
		}
	}
}
