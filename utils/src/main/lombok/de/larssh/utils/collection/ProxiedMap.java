package de.larssh.utils.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.NonFinal;

/**
 * An abstract {@link Map} implementation pointing to a given map object.
 *
 * <p>
 * Therefore it allows implementing maps based on invisible map types, such as
 * {@link java.util.Collections#unmodifiableMap(Map)} or
 * {@link java.util.Collections#synchronizedMap(Map)}.
 *
 * @param <K> the type of keys in this map
 * @param <V> the type of values in this map
 */
@RequiredArgsConstructor
public abstract class ProxiedMap<K, V> implements Map<K, V> {
	/**
	 * Wrapped map
	 */
	Map<K, V> map;

	/**
	 * Flag specifying if this instance can be modified
	 *
	 * @param modifiable flag
	 * @return {@code true} if this instance is modifiable, else {@code false}
	 */
	@NonFinal
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PROTECTED)
	boolean modifiable = true;

	/** {@inheritDoc} */
	@Override
	public void clear() {
		getModifiableMap().clear();
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsKey(@Nullable final Object key) {
		return getUnmodifiableMap().containsKey(key);
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsValue(@Nullable final Object value) {
		return getUnmodifiableMap().containsValue(value);
	}

	/** {@inheritDoc} */
	@Override
	public Set<Entry<K, V>> entrySet() {
		// Remark: This implementation might allow callers to modify an unmodifiable map
		// as of now.
		return getUnmodifiableMap().entrySet();
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(@CheckForNull final Object object) {
		return getUnmodifiableMap().equals(object);
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public V get(@Nullable final Object key) {
		return getUnmodifiableMap().get(key);
	}

	/**
	 * Verifies if this object is modifiable and either returns the wrapped map or
	 * throws an appropriate exception.
	 *
	 * @return the wrapped map if this object is modifiable
	 * @throws UnsupportedOperationException if this object is unmodifiable
	 */
	protected Map<K, V> getModifiableMap() {
		if (isModifiable()) {
			return map;
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the wrapped map without verifying if modifying it is prohibited.
	 *
	 * @return the wrapped map
	 */
	protected Map<K, V> getUnmodifiableMap() {
		return map;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return getUnmodifiableMap().hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return getUnmodifiableMap().isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public Set<K> keySet() {
		// Remark: This implementation might allow callers to modify an unmodifiable map
		// as of now.
		return getUnmodifiableMap().keySet();
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public V put(@Nullable final K key, @Nullable final V value) {
		return getModifiableMap().put(key, value);
	}

	/** {@inheritDoc} */
	@Override
	public void putAll(@Nullable final Map<? extends K, ? extends V> map) {
		getModifiableMap().putAll(map);
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public V remove(@Nullable final Object key) {
		return getModifiableMap().remove(key);
	}

	/** {@inheritDoc} */
	@Override
	public int size() {
		return getUnmodifiableMap().size();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getUnmodifiableMap().toString();
	}

	/** {@inheritDoc} */
	@Override
	public Collection<V> values() {
		// Remark: This implementation might allow callers to modify an unmodifiable map
		// as of now.
		return getUnmodifiableMap().values();
	}
}
