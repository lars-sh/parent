package de.larssh.utils.collection;

import static de.larssh.utils.Collectors.toLinkedHashMap;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link Map}.
 */
@UtilityClass
@SuppressWarnings("PMD.ShortClassName")
public class Maps {
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
}
