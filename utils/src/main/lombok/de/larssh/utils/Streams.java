package de.larssh.utils;

import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import de.larssh.utils.collection.Maps;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link Stream}.
 */
@UtilityClass
public class Streams {
	/**
	 * Maps elements of {@code stream} to entries with index and the original value.
	 *
	 * <p>
	 * The first elements index is zero. In case of an integer overflow an
	 * {@link ArithmeticException} is thrown.
	 *
	 * @param <T>    the type of the input stream elements
	 * @param stream the input stream
	 * @return the output stream of entries with index and input stream value
	 * @throws ArithmeticException Stream index overflow
	 */
	public static final <T> Stream<Entry<Integer, T>> indexed(final Stream<T> stream) {
		final AtomicInteger nextIndex = new AtomicInteger(0);
		return stream.map(value -> {
			final int index = nextIndex.getAndUpdate(i -> {
				if (i < 0) {
					throw new ArithmeticException("Stream index overflow");
				}
				return i + 1;
			});
			return Maps.entry(index, value);
		});
	}

	/**
	 * Maps elements of {@code stream} to entries with index of type long and the
	 * original value.
	 *
	 * <p>
	 * The first elements index is zero. In case of an overflow an
	 * {@link ArithmeticException} is thrown.
	 *
	 * @param <T>    the type of the input stream elements
	 * @param stream the input stream
	 * @return the output stream of entries with index of type long and input stream
	 *         value
	 * @throws ArithmeticException Stream index overflow
	 */
	public static final <T> Stream<Entry<Long, T>> indexedLong(final Stream<T> stream) {
		final AtomicLong nextIndex = new AtomicLong(0);
		return stream.map(value -> {
			final long index = nextIndex.getAndUpdate(i -> {
				if (i < 0) {
					throw new ArithmeticException("Stream index of type long overflow");
				}
				return i + 1;
			});
			return Maps.entry(index, value);
		});
	}
}
