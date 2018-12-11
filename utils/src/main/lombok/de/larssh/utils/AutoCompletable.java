package de.larssh.utils;

import lombok.NoArgsConstructor;

/**
 * This class implements a boolean {@code completed} status, allowing objects to
 * differentiate between an initialization phase and their final value.
 *
 * <p>
 * In addition to {@link Completable} it implements {@link AutoCloseable} to
 * allow simplified factory methods using try-with-resource statement.
 *
 * <p>
 * Each getter is recommended to call {@code #complete()}, while setters must
 * call {@code #throwIfCompleted()} prior to modification.
 *
 * <p>
 * Before using {@code AutoCompletable} think about using either a streamlined
 * constructor or a builder class.
 *
 * <p>
 * <b>Example:</b> {@code SomeFancyThing} is of type {@code AutoCloseable}. The
 * method {@code getSomeFancyThing()} is meant to initialize
 * {@code SomeFancyThing} while it should no longer be modifiable outside that
 * method.
 *
 * <pre>
 * SomeFancyThing getSomeFanyThing() {
 * 	try (SomeFancyThing someFanyThing = new SomeFancyThing()) {
 * 		...
 * 		return someFanyThing;
 * 	}
 * }
 * </pre>
 */
@NoArgsConstructor
public abstract class AutoCompletable extends Completable implements AutoCloseable {

	/**
	 * Calls {@link Completable#complete()} to finish the objects initialization
	 * phase.
	 */
	@Override
	public void close() {
		complete();
	}
}
