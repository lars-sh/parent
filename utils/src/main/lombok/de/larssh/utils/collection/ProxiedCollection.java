package de.larssh.utils.collection;

import java.util.Collection;
import java.util.Iterator;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
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
	 *
	 * @return wrapped collection
	 */
	@Getter(AccessLevel.PROTECTED)
	Collection<E> collection;

	/** {@inheritDoc} */
	@Override
	public boolean add(@Nullable final E element) {
		return getCollection().add(element);
	}

	/** {@inheritDoc} */
	@Override
	public boolean addAll(@Nullable final Collection<? extends E> collection) {
		return getCollection().addAll(collection);
	}

	/** {@inheritDoc} */
	@Override
	public void clear() {
		getCollection().clear();
	}

	/** {@inheritDoc} */
	@Override
	public boolean contains(@Nullable final Object object) {
		return getCollection().contains(object);
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsAll(@Nullable final Collection<?> collection) {
		return getCollection().containsAll(collection);
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(@CheckForNull final Object object) {
		return getCollection().equals(object);
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return getCollection().hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return getCollection().isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public Iterator<E> iterator() {
		return getCollection().iterator();
	}

	/** {@inheritDoc} */
	@Override
	public boolean remove(@Nullable final Object object) {
		return getCollection().remove(object);
	}

	/** {@inheritDoc} */
	@Override
	public boolean removeAll(@Nullable final Collection<?> collection) {
		return getCollection().removeAll(collection);
	}

	/** {@inheritDoc} */
	@Override
	public boolean retainAll(@Nullable final Collection<?> collection) {
		return getCollection().retainAll(collection);
	}

	/** {@inheritDoc} */
	@Override
	public int size() {
		return getCollection().size();
	}

	/** {@inheritDoc} */
	@Override
	public Object[] toArray() {
		return getCollection().toArray();
	}

	/** {@inheritDoc} */
	@Override
	public <T> T[] toArray(@Nullable final T[] array) {
		return getCollection().toArray(array);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getCollection().toString();
	}
}
