package de.larssh.utils.collection;

import java.util.Set;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import lombok.AccessLevel;
import lombok.Getter;

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
public class ProxiedSet<E> extends ProxiedCollection<E> implements Set<E> {

	/**
	 * Wrapped set
	 *
	 * @return wrapped set
	 */
	@SuppressWarnings("null")
	@Getter(AccessLevel.PROTECTED)
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
	public ProxiedSet(final Set<E> set) {
		super(set);

		this.set = set;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(@CheckForNull final Object object) {
		return getSet().equals(object);
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return getSet().hashCode();
	}
}
