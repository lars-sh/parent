package de.larssh.utils.collection;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.AccessLevel;
import lombok.Getter;

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
	 *
	 * @return wrapped list
	 */
	@Getter(AccessLevel.PROTECTED)
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
	public ProxiedList(final List<E> list) {
		super(list);

		this.list = list;
	}

	/** {@inheritDoc} */
	@Override
	public void add(final int index, @Nullable final E element) {
		getList().add(index, element);
	}

	/** {@inheritDoc} */
	@Override
	public boolean addAll(final int index, @Nullable final Collection<? extends E> collection) {
		return getList().addAll(index, collection);
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(@CheckForNull final Object object) {
		return getList().equals(object);
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public E get(final int index) {
		return getList().get(index);
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return getList().hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public int indexOf(@Nullable final Object object) {
		return getList().indexOf(object);
	}

	/** {@inheritDoc} */
	@Override
	public int lastIndexOf(@Nullable final Object object) {
		return getList().lastIndexOf(object);
	}

	/** {@inheritDoc} */
	@Override
	public ListIterator<E> listIterator() {
		return getList().listIterator();
	}

	/** {@inheritDoc} */
	@Override
	public ListIterator<E> listIterator(final int index) {
		return getList().listIterator(index);
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public E remove(final int index) {
		return getList().remove(index);
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public E set(final int index, @Nullable final E element) {
		return getList().set(index, element);
	}

	/** {@inheritDoc} */
	@Override
	public List<E> subList(final int fromIndex, final int toIndex) {
		return getList().subList(fromIndex, toIndex);
	}
}
