package de.larssh.utils.collection;

import static de.larssh.utils.Collectors.toLinkedHashMap;
import static java.util.Collections.unmodifiableMap;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;

import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link Map}.
 */
@UtilityClass
@SuppressWarnings("PMD.ShortClassName")
public class Maps {
	/**
	 * Wraps a builder around an {@link HashMap} to allow fluent creation and
	 * modification of a {@link Map}.
	 *
	 * <p>
	 * To use strict typing Java compilers might need some hint about your maps key
	 * and value types in front of this method call. Check out the below example.
	 *
	 * <p>
	 * <b>Usage example:</b> The following shows how to create an unmodifiable map
	 * containing three entries.
	 *
	 * <pre>
	 * Map&lt;String, Integer&gt; map = Maps.&lt;String, Integer&gt;builder()
	 *         .put("France", 33)
	 *         .put("Germany", 49)
	 *         .put("Italy", 39)
	 *         .unmodifiable();
	 * </pre>
	 *
	 * @param <K> type of the maps key
	 * @param <V> type of the maps value
	 * @return a map builder
	 */
	public <K, V> Builder<K, V> builder() {
		return builder(new HashMap<>());
	}

	/**
	 * Wraps a builder around {@code map} to allow its fluent creation and
	 * modification.
	 *
	 * <p>
	 * <b>Usage example 1:</b> The following shows how to create an unmodifiable
	 * {@link java.util.LinkedHashMap} containing three entries.
	 *
	 * <pre>
	 * Map&lt;String, Integer&gt; map = Maps.builder(new LinkedHashMap&lt;String, Integer&gt;())
	 *         .put("France", 33)
	 *         .put("Germany", 49)
	 *         .put("Italy", 39)
	 *         .unmodifiable();
	 * </pre>
	 *
	 * <p>
	 * <b>Usage example 2:</b> The following shows how to create a
	 * {@link java.util.NavigableMap} based on a {@link java.util.TreeMap}.
	 *
	 * <pre>
	 * NavigableMap&lt;String, Integer&gt; map = Maps.builder(new TreeMap&lt;String, Integer&gt;())
	 *         .put("France", 33)
	 *         .put("Germany", 49)
	 *         .put("Italy", 39)
	 *         .get();
	 * </pre>
	 *
	 * @param <K> type of the maps key
	 * @param <V> type of the maps value
	 * @param map the map to be wrapped
	 * @return a map builder wrapping {@code map}
	 */
	public <K, V> Builder<K, V> builder(final Map<K, V> map) {
		return new Builder<>(map);
	}

	/**
	 * Creates an {@link Entry} representing a mapping from the specified key to the
	 * specified value.
	 *
	 * <p>
	 * This entry is immutable. It does not support the method
	 * {@link Entry#setValue(Object)}.
	 *
	 * @param <K>   the type of the key
	 * @param <V>   the type of the value
	 * @param key   the key represented by this entry
	 * @param value the value represented by this entry
	 * @return the immutable entry
	 */
	public static <K, V> Entry<K, V> entry(@Nullable final K key, @Nullable final V value) {
		return new SimpleImmutableEntry<>(key, value);
	}

	/**
	 * Sorts a {@link Map} based on a given {@link Entry} {@link Comparator}.
	 *
	 * <p>
	 * Currently returned map is implemented as {@link java.util.LinkedHashMap}
	 * though that might change.
	 *
	 * @param <K>        the type of keys maintained by this map
	 * @param <V>        the type of mapped values
	 * @param unsorted   probably unsorted map
	 * @param comparator entry based comparator
	 * @return sorted map based on the given comparator
	 */
	public static <K, V> Map<K, V> sort(final Map<K, V> unsorted, final Comparator<Entry<K, V>> comparator) {
		return unsorted.entrySet().stream().sorted(comparator).collect(toLinkedHashMap(Entry::getKey, Entry::getValue));
	}

	/**
	 * Wraps around {@code map} to allow its fluent creation and modification.
	 *
	 * <p>
	 * Check out {@link Maps#builder()} and {@link Maps#builder(Map)} for more
	 * information.
	 *
	 * @param <K> type of the maps key
	 * @param <V> type of the maps value
	 */
	@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
	public static class Builder<K, V> {
		/**
		 * The wrapped map
		 */
		Map<K, V> map;

		/**
		 * Check out {@link Map#clear()}.
		 *
		 * @return this builder
		 */
		public Builder<K, V> clear() {
			get().clear();
			return this;
		}

		/**
		 * Check out {@link Map#compute(Object, BiFunction)}.
		 *
		 * @param key               key with which the specified value is to be
		 *                          associated
		 * @param remappingFunction the function to compute a value
		 * @return this builder
		 */
		public Builder<K, V> compute(@Nullable final K key,
				final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
			get().compute(key, remappingFunction);
			return this;
		}

		/**
		 * Check out {@link Map#computeIfAbsent(Object, Function)}.
		 *
		 * @param key             key with which the specified value is to be associated
		 * @param mappingFunction the function to compute a value
		 * @return this builder
		 */
		public Builder<K, V> computeIfAbsent(@Nullable final K key,
				final Function<? super K, ? extends V> mappingFunction) {
			get().computeIfAbsent(key, mappingFunction);
			return this;
		}

		/**
		 * Check out {@link Map#computeIfPresent(Object, BiFunction)}.
		 *
		 * @param key               key with which the specified value is to be
		 *                          associated
		 * @param remappingFunction the function to compute a value
		 * @return this builder
		 */
		public Builder<K, V> computeIfPresent(@Nullable final K key,
				final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
			get().computeIfPresent(key, remappingFunction);
			return this;
		}

		/**
		 * Returns the wrapped map
		 *
		 * @return the map
		 */
		public Map<K, V> get() {
			return map;
		}

		/**
		 * Check out {@link Map#merge(Object, Object, BiFunction)}.
		 *
		 * @param key               key with which the resulting value is to be
		 *                          associated
		 * @param value             the non-null value to be merged with the existing
		 *                          value associated with the key or, if no existing
		 *                          value or a null value is associated with the key, to
		 *                          be associated with the key
		 * @param remappingFunction the function to recompute a value if present
		 * @return this builder
		 */
		public Builder<K, V> merge(@Nullable final K key,
				@Nullable final V value,
				final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
			get().merge(key, value, remappingFunction);
			return this;
		}

		/**
		 * Check out {@link Map#put(Object, Object)}.
		 *
		 * @param key   key with which the specified value is to be associated
		 * @param value value to be associated with the specified key
		 * @return this builder
		 */
		public Builder<K, V> put(@Nullable final K key, @Nullable final V value) {
			get().put(key, value);
			return this;
		}

		/**
		 * Check out {@link Map#putAll(Map)}.
		 *
		 * @param map mappings to be stored in this map
		 * @return this builder
		 */
		public Builder<K, V> putAll(final Map<? extends K, ? extends V> map) {
			get().putAll(map);
			return this;
		}

		/**
		 * Check out {@link Map#putIfAbsent(Object, Object)}.
		 *
		 * @param key   key with which the specified value is to be associated
		 * @param value value to be associated with the specified key
		 * @return this builder
		 */
		public Builder<K, V> putIfAbsent(@Nullable final K key, @Nullable final V value) {
			get().putIfAbsent(key, value);
			return this;
		}

		/**
		 * Check out {@link Map#remove(Object)}.
		 *
		 * @param key key whose mapping is to be removed from the map
		 * @return this builder
		 */
		public Builder<K, V> remove(@Nullable final K key) {
			get().remove(key);
			return this;
		}

		/**
		 * Check out {@link Map#remove(Object, Object)}.
		 *
		 * @param key   key with which the specified value is associated
		 * @param value value expected to be associated with the specified key
		 * @return this builder
		 */
		public Builder<K, V> remove(@Nullable final K key, @Nullable final V value) {
			get().remove(key, value);
			return this;
		}

		/**
		 * Check out {@link Map#replace(Object, Object)}.
		 *
		 * @param key   key with which the specified value is associated
		 * @param value value to be associated with the specified key
		 * @return this builder
		 */
		public Builder<K, V> replace(@Nullable final K key, @Nullable final V value) {
			get().replace(key, value);
			return this;
		}

		/**
		 * Check out {@link Map#replace(Object, Object, Object)}.
		 *
		 * @param key      key with which the specified value is associated
		 * @param oldValue value expected to be associated with the specified key
		 * @param newValue value to be associated with the specified key
		 * @return this builder
		 */
		public Builder<K, V> replace(@Nullable final K key, @Nullable final V oldValue, @Nullable final V newValue) {
			get().replace(key, oldValue, newValue);
			return this;
		}

		/**
		 * Check out {@link Map#replaceAll(BiFunction)}.
		 *
		 * @param function the function to apply to each entry
		 * @return this builder
		 */
		public Builder<K, V> replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
			get().replaceAll(function);
			return this;
		}

		/**
		 * Returns the wrapped map as unmodifiable map
		 *
		 * @return unmodifiable map
		 */
		public Map<K, V> unmodifiable() {
			return unmodifiableMap(get());
		}
	}
}
