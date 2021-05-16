package de.larssh.utils.collection;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import de.larssh.utils.Finals;
import de.larssh.utils.Nullables;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.Nullable;
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
	 */
	Map<K, V> map;

	/**
	 * The set of entries as defined by {@link Map#entrySet()}.
	 */
	@SuppressWarnings({ "checkstyle:AnonInnerLength", "PMD.AvoidFieldNameMatchingMethodName" })
	Supplier<Set<Entry<K, V>>> entrySet
			= Finals.lazy(() -> new ProxiedSet<Entry<K, V>>(getProxiedForRead().entrySet()) {
				/** {@inheritDoc} */
				@Override
				public boolean isModifiable() {
					return ProxiedMap.this.isModifiable();
				}

				/** {@inheritDoc} */
				@Override
				public Iterator<Entry<K, V>> iterator() {
					return new ProxiedIterator<Entry<K, V>>(getWrappedForRead().iterator()) {
						/** {@inheritDoc} */
						@Override
						public boolean isModifiable() {
							return ProxiedMap.this.isModifiable();
						}

						/** {@inheritDoc} */
						@Nullable
						@Override
						public Entry<K, V> next() {
							return new ProxiedEntry<>(this::isModifiable, Nullables.orElseThrow(super.next()));
						}
					};
				}

				/** {@inheritDoc} */
				@Override
				public Object[] toArray() {
					return stream() //
							.map(entry -> new ProxiedEntry<>(this::isModifiable, entry))
							.toArray();
				}

				/** {@inheritDoc} */
				@Override
				public <T> T[] toArray(@Nullable final T[] array) {
					return stream() //
							.map(entry -> new ProxiedEntry<>(this::isModifiable, entry))
							.collect(toList())
							.toArray(array);
				}
			});

	/**
	 * The set of keys as defined by {@link Map#keySet()}.
	 */
	@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
	Supplier<Set<K>> keySet = Finals.lazy(() -> new ProxiedSet<K>(getProxiedForRead().keySet()) {
		/** {@inheritDoc} */
		@Override
		public boolean isModifiable() {
			return ProxiedMap.this.isModifiable();
		}
	});

	/**
	 * The collection of values as defined by {@link Map#values()}.
	 */
	@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
	Supplier<Collection<V>> values = Finals.lazy(() -> new ProxiedCollection<V>(getProxiedForRead().values()) {
		/** {@inheritDoc} */
		@Override
		public boolean isModifiable() {
			return ProxiedMap.this.isModifiable();
		}
	});

	/** {@inheritDoc} */
	@Override
	public void clear() {
		getProxiedIfModifiable().clear();
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsKey(@Nullable final Object key) {
		return getProxiedForRead().containsKey(key);
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsValue(@Nullable final Object value) {
		return getProxiedForRead().containsValue(value);
	}

	/** {@inheritDoc} */
	@Override
	public Set<Entry<K, V>> entrySet() {
		return entrySet.get();
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(@CheckForNull final Object object) {
		return getProxiedForRead().equals(object);
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public V get(@Nullable final Object key) {
		return getProxiedForRead().get(key);
	}

	/**
	 * Verifies if this object is modifiable and either returns the wrapped map or
	 * throws an appropriate exception.
	 *
	 * @return the wrapped map if this object is modifiable
	 * @throws UnsupportedOperationException if this object is unmodifiable
	 */
	protected Map<K, V> getProxiedIfModifiable() {
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
	protected Map<K, V> getProxiedForRead() {
		return map;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return getProxiedForRead().hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return getProxiedForRead().isEmpty();
	}

	/**
	 * Flag specifying if this instance can be modified
	 *
	 * @return {@code true} if this instance is modifiable, else {@code false}
	 */
	public abstract boolean isModifiable();

	/** {@inheritDoc} */
	@Override
	public Set<K> keySet() {
		return keySet.get();
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public V put(@Nullable final K key, @Nullable final V value) {
		return getProxiedIfModifiable().put(key, value);
	}

	/** {@inheritDoc} */
	@Override
	public void putAll(@Nullable final Map<? extends K, ? extends V> map) {
		getProxiedIfModifiable().putAll(map);
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public V remove(@Nullable final Object key) {
		return getProxiedIfModifiable().remove(key);
	}

	/** {@inheritDoc} */
	@Override
	public int size() {
		return getProxiedForRead().size();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getProxiedForRead().toString();
	}

	/** {@inheritDoc} */
	@Override
	public Collection<V> values() {
		return values.get();
	}

	/**
	 * An abstract {@link Entry} implementation pointing to a given entry object.
	 *
	 * @param <K> the type of keys in this map
	 * @param <V> the type of values in this map
	 */
	@RequiredArgsConstructor
	private static class ProxiedEntry<K, V> implements Entry<K, V> {
		/**
		 * Supplies whether this instance is modifiable or not
		 */
		@SuppressWarnings("PMD.LinguisticNaming")
		final BooleanSupplier isModifiable;

		/**
		 * Wrapped entry
		 *
		 * @return the wrapped entry
		 */
		@Getter
		final Entry<K, V> entry;

		/** {@inheritDoc} */
		@Override
		public boolean equals(@CheckForNull final Object object) {
			return getEntry().equals(object);
		}

		/** {@inheritDoc} */
		@Nullable
		@Override
		public K getKey() {
			return getEntry().getKey();
		}

		/** {@inheritDoc} */
		@Nullable
		@Override
		public V getValue() {
			return getEntry().getValue();
		}

		/** {@inheritDoc} */
		@Override
		public int hashCode() {
			return getEntry().hashCode();
		}

		/** {@inheritDoc} */
		@Nullable
		@Override
		public V setValue(@Nullable final V value) {
			if (isModifiable.getAsBoolean()) {
				return getEntry().setValue(value);
			}
			throw new UnsupportedOperationException();
		}
	}
}
