package de.larssh.utils.collection;

import java.util.Collection;
import java.util.Iterator;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.NonFinal;

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

	/**
	 * Flag specifying if this instance can be modified
	 *
	 * @return {@code true} if this instance is modifiable, else {@code false}
	 */
	@NonFinal
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PROTECTED)
	boolean modifiable = true;

	/** {@inheritDoc} */
	@Override
	public boolean add(@Nullable final E element) {
		return getModifiableCollection().add(element);
	}

	/** {@inheritDoc} */
	@Override
	public boolean addAll(@Nullable final Collection<? extends E> collection) {
		return getModifiableCollection().addAll(collection);
	}

	/** {@inheritDoc} */
	@Override
	public void clear() {
		getModifiableCollection().clear();
	}

	/** {@inheritDoc} */
	@Override
	public boolean contains(@Nullable final Object object) {
		return getUnmodifiableCollection().contains(object);
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsAll(@Nullable final Collection<?> collection) {
		return getUnmodifiableCollection().containsAll(collection);
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(@CheckForNull final Object object) {
		return getUnmodifiableCollection().equals(object);
	}

	/**
	 * Verifies if this object is modifiable and either returns the wrapped
	 * collection or throws an appropriate exception.
	 *
	 * @return the wrapped collection if this object is modifiable
	 * @throws UnsupportedOperationException if this object is unmodifiable
	 */
	protected Collection<E> getModifiableCollection() {
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
	protected Collection<E> getUnmodifiableCollection() {
		return collection;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return getUnmodifiableCollection().hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return getUnmodifiableCollection().isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public Iterator<E> iterator() {
		// Remark: This implementation might allow callers to modify an unmodifiable
		// collection as of now.
		return getUnmodifiableCollection().iterator();
	}

	/** {@inheritDoc} */
	@Override
	public boolean remove(@Nullable final Object object) {
		return getModifiableCollection().remove(object);
	}

	/** {@inheritDoc} */
	@Override
	public boolean removeAll(@Nullable final Collection<?> collection) {
		return getModifiableCollection().removeAll(collection);
	}

	/** {@inheritDoc} */
	@Override
	public boolean retainAll(@Nullable final Collection<?> collection) {
		return getModifiableCollection().retainAll(collection);
	}

	/** {@inheritDoc} */
	@Override
	public int size() {
		return getUnmodifiableCollection().size();
	}

	/** {@inheritDoc} */
	@Override
	public Object[] toArray() {
		return getUnmodifiableCollection().toArray();
	}

	/** {@inheritDoc} */
	@Override
	public <T> T[] toArray(@Nullable final T[] array) {
		return getUnmodifiableCollection().toArray(array);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getUnmodifiableCollection().toString();
	}
}
