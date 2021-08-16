package de.larssh.utils.collection;

import java.util.Iterator;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.NonFinal;

/**
 * An abstract {@link Iterator} implementation pointing to a given iterator.
 *
 * @param <E> the type of elements in this iterator
 */
@RequiredArgsConstructor
public abstract class ProxiedIterator<E> implements Iterator<E> {
	/**
	 * Wrapped iterator
	 */
	Iterator<E> iterator;

	/**
	 * Flag specifying if the underlying collection can be modified
	 *
	 * @param modifiable flag
	 * @return {@code true} if the underlying collection is modifiable, else
	 *         {@code false}
	 */
	@NonFinal
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PROTECTED)
	boolean modifiable = true;

	/** {@inheritDoc} */
	@Override
	public boolean equals(@CheckForNull final Object object) {
		return getProxiedForRead().equals(object);
	}

	/**
	 * Verifies if the underlying collection is modifiable and either returns the
	 * wrapped iterator or throws an appropriate exception.
	 *
	 * @return the wrapped iterator if the underlying collection is modifiable
	 * @throws UnsupportedOperationException if the underlying collection is
	 *                                       unmodifiable
	 */
	protected Iterator<E> getProxiedIfModifiable() {
		if (isModifiable()) {
			return iterator;
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the wrapped iterator without verifying if modifying the underlying
	 * collection is prohibited.
	 *
	 * @return the wrapped iterator
	 */
	protected Iterator<E> getProxiedForRead() {
		return iterator;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return getProxiedForRead().hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasNext() {
		return getProxiedForRead().hasNext();
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public E next() {
		return getProxiedForRead().next();
	}

	/** {@inheritDoc} */
	@Override
	public void remove() {
		getProxiedIfModifiable().remove();
	}
}
