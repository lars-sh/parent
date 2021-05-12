package de.larssh.utils.collection;

import java.util.Set;

/**
 * An abstract {@link Set} implementation pointing to a given set object.
 *
 * <p>
 * Therefore it allows implementing sets based on invisible set types, such as
 * {@link java.util.Collections#unmodifiableSet(Set)} or
 * {@link java.util.Collections#synchronizedSet(Set)}.
 *
 * @param <E> the type of elements in this set
 */
public abstract class ProxiedSet<E> extends ProxiedCollection<E> implements Set<E> {
	/**
	 * Wrapped set
	 */
	Set<E> set;

	/**
	 * An abstract {@link Set} implementation pointing to {@code set}.
	 *
	 * <p>
	 * Therefore it allows implementing sets based on invisible set types, such as
	 * {@link java.util.Collections#unmodifiableSet(Set)} or
	 * {@link java.util.Collections#synchronizedSet(Set)}.
	 *
	 * @param set the set to proxy
	 */
	protected ProxiedSet(final Set<E> set) {
		super(set);

		this.set = set;
	}

	/**
	 * Verifies if this object is modifiable and either returns the wrapped set or
	 * throws an appropriate exception.
	 *
	 * @return the wrapped set if this object is modifiable
	 * @throws UnsupportedOperationException if this object is unmodifiable
	 */
	protected Set<E> getModifiableSet() {
		if (isModifiable()) {
			return set;
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the wrapped set without verifying if modifying it is prohibited.
	 *
	 * @return the wrapped set
	 */
	protected Set<E> getUnmodifiableSet() {
		return set;
	}
}
