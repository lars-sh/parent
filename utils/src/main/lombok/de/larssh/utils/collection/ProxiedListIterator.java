package de.larssh.utils.collection;

import java.util.ListIterator;

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * An abstract {@link ListIterator} implementation pointing to a given iterator.
 *
 * @param <E> the type of elements in this iterator
 */
public abstract class ProxiedListIterator<E> extends ProxiedIterator<E> implements ListIterator<E> {
	/**
	 * Wrapped iterator
	 */
	ListIterator<E> listIterator;

	/**
	 * An abstract {@link ListIterator} implementation pointing to a given iterator.
	 *
	 * @param listIterator the iterator to proxy
	 */
	protected ProxiedListIterator(final ListIterator<E> listIterator) {
		super(listIterator);

		this.listIterator = listIterator;
	}

	/** {@inheritDoc} */
	@Override
	public void add(@Nullable final E element) {
		getProxiedIfModifiable().add(element);
	}

	/** {@inheritDoc} */
	@Override
	protected ListIterator<E> getProxiedIfModifiable() {
		if (isModifiable()) {
			return listIterator;
		}
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	@Override
	protected ListIterator<E> getProxiedForRead() {
		return listIterator;
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasPrevious() {
		return getProxiedForRead().hasPrevious();
	}

	/** {@inheritDoc} */
	@Override
	public int nextIndex() {
		return getProxiedForRead().nextIndex();
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public E previous() {
		return getProxiedForRead().previous();
	}

	/** {@inheritDoc} */
	@Override
	public int previousIndex() {
		return getProxiedForRead().previousIndex();
	}

	/** {@inheritDoc} */
	@Override
	public void set(@Nullable final E element) {
		getProxiedIfModifiable().set(element);
	}
}
