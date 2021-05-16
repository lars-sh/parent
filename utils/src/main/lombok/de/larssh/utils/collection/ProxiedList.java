package de.larssh.utils.collection;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * An abstract {@link List} implementation pointing to a given list object.
 *
 * <p>
 * Therefore it allows implementing lists based on invisible list types, such as
 * {@link java.util.Collections#unmodifiableList(List)} or
 * {@link java.util.Collections#synchronizedList(List)}.
 *
 * @param <E> the type of elements in this list
 */
public abstract class ProxiedList<E> extends ProxiedCollection<E> implements List<E> {
	/**
	 * Wrapped list
	 */
	List<E> list;

	/**
	 * An abstract {@link List} implementation pointing to {@code list}.
	 *
	 * <p>
	 * Therefore it allows implementing lists based on invisible list types, such as
	 * {@link java.util.Collections#unmodifiableList(List)} or
	 * {@link java.util.Collections#synchronizedList(List)}.
	 *
	 * @param list the list to proxy
	 */
	protected ProxiedList(final List<E> list) {
		super(list);

		this.list = list;
	}

	/** {@inheritDoc} */
	@Override
	public void add(final int index, @Nullable final E element) {
		getWrappedIfModifiable().add(index, element);
	}

	/** {@inheritDoc} */
	@Override
	public boolean addAll(final int index, @Nullable final Collection<? extends E> collection) {
		return getWrappedIfModifiable().addAll(index, collection);
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public E get(final int index) {
		return getWrappedForRead().get(index);
	}

	/** {@inheritDoc} */
	@Override
	protected List<E> getWrappedIfModifiable() {
		if (isModifiable()) {
			return list;
		}
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	@Override
	protected List<E> getWrappedForRead() {
		return list;
	}

	/** {@inheritDoc} */
	@Override
	public int indexOf(@Nullable final Object object) {
		return getWrappedForRead().indexOf(object);
	}

	/** {@inheritDoc} */
	@Override
	public int lastIndexOf(@Nullable final Object object) {
		return getWrappedForRead().lastIndexOf(object);
	}

	/** {@inheritDoc} */
	@Override
	public ListIterator<E> listIterator() {
		return new ProxiedListIterator<E>(getWrappedForRead().listIterator()) {
			@Override
			public boolean isModifiable() {
				return ProxiedList.this.isModifiable();
			}
		};
	}

	/** {@inheritDoc} */
	@Override
	public ListIterator<E> listIterator(final int index) {
		return new ProxiedListIterator<E>(getWrappedForRead().listIterator(index)) {
			@Override
			public boolean isModifiable() {
				return ProxiedList.this.isModifiable();
			}
		};
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public E remove(final int index) {
		return getWrappedIfModifiable().remove(index);
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public E set(final int index, @Nullable final E element) {
		return getWrappedIfModifiable().set(index, element);
	}

	/** {@inheritDoc} */
	@Override
	public List<E> subList(final int fromIndex, final int toIndex) {
		return new ProxiedList<E>(getWrappedForRead().subList(fromIndex, toIndex)) {
			@Override
			public boolean isModifiable() {
				return ProxiedList.this.isModifiable();
			}
		};
	}
}
