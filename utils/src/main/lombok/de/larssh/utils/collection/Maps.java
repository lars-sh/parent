package de.larssh.utils.collection;

import static de.larssh.utils.Collectors.toLinkedHashMap;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link Map}.
 */
@UtilityClass
public class Maps {
	/**
	 * Sorts a {@link Map} based on a given {@link Entry} {@link Comparator}.
	 *
	 * <p>
	 * Currently returned map is implemented as {@link java.util.LinkedHashMap}
	 * though that might change.
	 *
	 * @param            <K> the type of keys maintained by this map
	 * @param            <V> the type of mapped values
	 * @param unsorted   probably unsorted map
	 * @param comparator entry based comparator
	 * @return sorted map based on the given comparator
	 */
	public static <K, V> Map<K, V> sort(final Map<K, V> unsorted, final Comparator<Entry<K, V>> comparator) {
		return unsorted.entrySet().stream().sorted(comparator).collect(toLinkedHashMap(Entry::getKey, Entry::getValue));
	}
}
