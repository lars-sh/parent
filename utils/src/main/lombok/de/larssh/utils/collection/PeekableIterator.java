package de.larssh.utils.collection;

import java.util.Iterator;

import de.larssh.utils.Nullables;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * An iterator that allows peeking up to one element without removing it,
 * supporting a one-element lookahead while iterating.
 *
 * @param <E> the type of elements returned by this iterator
 */
public interface PeekableIterator<E> extends Iterator<E> {
	/**
	 * Returns the next element in the iteration without removing it from the
	 * elements returned by {@link #next()}.
	 *
	 * <p>
	 * Use {@link #peekNonNull()} if the iterator is proven not to contain
	 * {@code null}.
	 *
	 * @return the next element in the iteration
	 * @throws java.util.NoSuchElementException if the iteration has no more
	 *                                          elements
	 */
	@Nullable
	E peek();

	/**
	 * Returns the next element in the iteration without removing it from the
	 * elements returned by {@link #next()}.
	 *
	 * @return the next element in the iteration
	 * @throws java.util.NoSuchElementException if the iteration has no more
	 *                                          elements
	 * @throws NullPointerException             if the peeked element is
	 *                                          {@code null}
	 */
	default E peekNonNull() {
		return Nullables.orElseThrow(peek());
	}
}
