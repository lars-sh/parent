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
		getModifiableList().add(index, element);
	}

	/** {@inheritDoc} */
	@Override
	public boolean addAll(final int index, @Nullable final Collection<? extends E> collection) {
		return getModifiableList().addAll(index, collection);
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public E get(final int index) {
		return getUnmodifiableList().get(index);
	}

	/**
	 * Verifies if this object is modifiable and either returns the wrapped list or
	 * throws an appropriate exception.
	 *
	 * @return the wrapped list if this object is modifiable
	 * @throws UnsupportedOperationException if this object is unmodifiable
	 */
	protected List<E> getModifiableList() {
		if (isModifiable()) {
			return list;
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the wrapped list without verifying if modifying it is prohibited.
	 *
	 * @return the wrapped list
	 */
	protected List<E> getUnmodifiableList() {
		return list;
	}

	/** {@inheritDoc} */
	@Override
	public int indexOf(@Nullable final Object object) {
		return getUnmodifiableList().indexOf(object);
	}

	/** {@inheritDoc} */
	@Override
	public int lastIndexOf(@Nullable final Object object) {
		return getUnmodifiableList().lastIndexOf(object);
	}

	/** {@inheritDoc} */
	@Override
	public ListIterator<E> listIterator() {
		// Remark: This implementation might allow callers to modify an unmodifiable
		// list as of now.
		return getUnmodifiableList().listIterator();
	}

	/** {@inheritDoc} */
	@Override
	public ListIterator<E> listIterator(final int index) {
		// Remark: This implementation might allow callers to modify an unmodifiable
		// list as of now.
		return getUnmodifiableList().listIterator(index);
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public E remove(final int index) {
		return getModifiableList().remove(index);
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public E set(final int index, @Nullable final E element) {
		return getModifiableList().set(index, element);
	}

	/** {@inheritDoc} */
	@Override
	public List<E> subList(final int fromIndex, final int toIndex) {
		// Remark: This implementation might allow callers to modify an unmodifiable
		// list as of now.
		return getUnmodifiableList().subList(fromIndex, toIndex);
	}
}
