package de.larssh.utils.collection;

import java.util.Collection;
import java.util.Iterator;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.RequiredArgsConstructor;

/**
 * An abstract {@link Collection} implementation pointing to a given collection
 * object.
 *
 * <p>
 * Therefore it allows implementing collections based on invisible collection
 * types, such as
 * {@link java.util.Collections#unmodifiableCollection(Collection)} or
 * {@link java.util.Collections#synchronizedCollection(Collection)}.
 *
 * @param <E> the type of elements in this collection
 */
@RequiredArgsConstructor
public abstract class ProxiedCollection<E> implements Collection<E> {
	/**
	 * Wrapped collection
	 */
	Collection<E> collection;

	/** {@inheritDoc} */
	@Override
	public boolean add(@Nullable final E element) {
		return getWrappedIfModifiable().add(element);
	}

	/** {@inheritDoc} */
	@Override
	public boolean addAll(@Nullable final Collection<? extends E> collection) {
		return getWrappedIfModifiable().addAll(collection);
	}

	/** {@inheritDoc} */
	@Override
	public void clear() {
		getWrappedIfModifiable().clear();
	}

	/** {@inheritDoc} */
	@Override
	public boolean contains(@Nullable final Object object) {
		return getWrappedForRead().contains(object);
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsAll(@Nullable final Collection<?> collection) {
		return getWrappedForRead().containsAll(collection);
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(@CheckForNull final Object object) {
		return getWrappedForRead().equals(object);
	}

	/**
	 * Verifies if this object is modifiable and either returns the wrapped
	 * collection or throws an appropriate exception.
	 *
	 * @return the wrapped collection if this object is modifiable
	 * @throws UnsupportedOperationException if this object is unmodifiable
	 */
	protected Collection<E> getWrappedIfModifiable() {
		if (isModifiable()) {
			return collection;
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the wrapped collection without verifying if modifying it is
	 * prohibited.
	 *
	 * @return the wrapped collection
	 */
	protected Collection<E> getWrappedForRead() {
		return collection;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return getWrappedForRead().hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return getWrappedForRead().isEmpty();
	}

	/**
	 * Flag specifying if this instance can be modified
	 *
	 * @return {@code true} if this instance is modifiable, else {@code false}
	 */
	public abstract boolean isModifiable();

	/** {@inheritDoc} */
	@Override
	public Iterator<E> iterator() {
		return new ProxiedIterator<E>(getWrappedForRead().iterator()) {
			@Override
			public boolean isModifiable() {
				return ProxiedCollection.this.isModifiable();
			}
		};
	}

	/** {@inheritDoc} */
	@Override
	public boolean remove(@Nullable final Object object) {
		return getWrappedIfModifiable().remove(object);
	}

	/** {@inheritDoc} */
	@Override
	public boolean removeAll(@Nullable final Collection<?> collection) {
		return getWrappedIfModifiable().removeAll(collection);
	}

	/** {@inheritDoc} */
	@Override
	public boolean retainAll(@Nullable final Collection<?> collection) {
		return getWrappedIfModifiable().retainAll(collection);
	}

	/** {@inheritDoc} */
	@Override
	public int size() {
		return getWrappedForRead().size();
	}

	/** {@inheritDoc} */
	@Override
	public Object[] toArray() {
		return getWrappedForRead().toArray();
	}

	/** {@inheritDoc} */
	@Override
	public <T> T[] toArray(@Nullable final T[] array) {
		return getWrappedForRead().toArray(array);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getWrappedForRead().toString();
	}
}
