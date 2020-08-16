package de.larssh.utils.collection;

import java.util.Enumeration;
import java.util.stream.Stream;

import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link Enumeration}.
 */
@UtilityClass
public class Enumerations {
	/**
	 * Returns a sequential {@link java.util.Iterator} with the {@code enumeration}
	 * as its source.
	 *
	 * @param <E>         The type of the enumeration elements
	 * @param enumeration the enumeration
	 * @return an {@code Iterator} for the enumeration
	 */
	public static <E> PeekableIterator<E> iterator(final Enumeration<E> enumeration) {
		return Iterators
				.iterator(state -> enumeration.hasMoreElements() ? enumeration.nextElement() : state.endOfData());
	}

	/**
	 * Returns a sequential {@link Stream} with the {@code enumeration} as its
	 * source.
	 *
	 * @param <E>         The type of the enumeration elements
	 * @param enumeration the enumeration, assumed to be unmodified during use
	 * @return a {@code Stream} for the enumeration
	 */
	public static <E> Stream<E> stream(final Enumeration<E> enumeration) {
		return Iterators.stream(iterator(enumeration));
	}
}
