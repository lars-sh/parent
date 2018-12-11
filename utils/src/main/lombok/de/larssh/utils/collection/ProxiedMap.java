package de.larssh.utils.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
	 *
	 * @return wrapped map
	 */
	@SuppressWarnings("null")
	@Getter(AccessLevel.PROTECTED)
	Map<K, V> map;

	/** {@inheritDoc} */
	@Override
	public void clear() {
		getMap().clear();
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsKey(@Nullable final Object key) {
		return getMap().containsKey(key);
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsValue(@Nullable final Object value) {
		return getMap().containsValue(value);
	}

	/** {@inheritDoc} */
	@Override
	public Set<Entry<K, V>> entrySet() {
		return getMap().entrySet();
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(@CheckForNull final Object object) {
		return getMap().equals(object);
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public V get(@Nullable final Object key) {
		return getMap().get(key);
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return getMap().hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return getMap().isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public Set<K> keySet() {
		return getMap().keySet();
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public V put(@Nullable final K key, @Nullable final V value) {
		return getMap().put(key, value);
	}

	/** {@inheritDoc} */
	@Override
	public void putAll(@Nullable final Map<? extends K, ? extends V> map) {
		getMap().putAll(map);
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public V remove(@Nullable final Object key) {
		return getMap().remove(key);
	}

	/** {@inheritDoc} */
	@Override
	public int size() {
		return getMap().size();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getMap().toString();
	}

	/** {@inheritDoc} */
	@Override
	public Collection<V> values() {
		return getMap().values();
	}
}
