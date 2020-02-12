package de.larssh.utils.collection;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link Enumeration}.
 */
@UtilityClass
public class Enumerations {
	/**
	 * Returns a sequential {@link Iterator} with the {@code enumeration} as its
	 * source.
	 *
	 * @param <T>         The type of the enumeration elements
	 * @param enumeration the enumeration
	 * @return an {@code Iterator} for the enumeration
	 */
	public static <T> Iterator<T> iterator(final Enumeration<T> enumeration) {
		return new Iterator<T>() {
			@Override
			public T next() {
				return enumeration.nextElement();
			}

			@Override
			public boolean hasNext() {
				return enumeration.hasMoreElements();
			}
		};
	}

	/**
	 * Returns a sequential {@link Stream} with the {@code enumeration} as its
	 * source.
	 *
	 * @param <T>         The type of the enumeration elements
	 * @param enumeration the enumeration, assumed to be unmodified during use
	 * @return a {@code Stream} for the enumeration
	 */
	@SuppressWarnings("checkstyle:IllegalToken")
	public static <T> Stream<T> stream(final Enumeration<T> enumeration) {
		return StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(iterator(enumeration), Spliterator.IMMUTABLE | Spliterator.ORDERED),
				false);
	}
}
