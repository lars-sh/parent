package de.larssh.utils.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link Iterator}.
 */
@UtilityClass
public class Iterators {
	/**
	 * Wraps {@code elementsSupplier} into a {@link PeekableIterator}. This is
	 * probably the simplest way of writing an iterator.
	 *
	 * <p>
	 * <b>Usage example:</b> The following shows how to create an iterator returning
	 * every second element only.
	 *
	 * <pre>
	 * Iterator&lt;T&gt; oldIterator = ...;
	 * Iterator&lt;T&gt; newIterator = Iterators.iterator(state -> {
	 *     if (!oldIterator.hasNext() && !oldIterator.hasNext()) {
	 *         return state.endOfData();
	 *     }
	 *     return oldIterator.next();;
	 * });
	 * </pre>
	 *
	 * @param <E>              the type of the iterator elements
	 * @param elementsSupplier the elements supplier with the state as parameter
	 * @return a {@link PeekableIterator} wrapping {@code elementsSupplier}
	 */
	public static <E> PeekableIterator<E> iterator(final Function<ElementsSupplierState<E>, E> elementsSupplier) {
		return new ElementsSupplierIterator<>(elementsSupplier);
	}

	/**
	 * Wraps {@code iterator} into a {@link PeekableIterator}.
	 *
	 * @param <E>      the type of the iterator elements
	 * @param iterator the iterator
	 * @return a {@link PeekableIterator} wrapping {@code iterator}
	 */
	public static <E> PeekableIterator<E> peekableIterator(final Iterator<E> iterator) {
		if (iterator instanceof PeekableIterator) {
			return (PeekableIterator<E>) iterator;
		}
		return iterator(state -> iterator.hasNext() ? iterator.next() : state.endOfData());
	}

	/**
	 * Returns a sequential {@link Stream} with the {@code elementsSupplier} as its
	 * source.
	 *
	 * <p>
	 * Check out {@link #iterator(Function)} for more information about
	 * {@code elementsSupplier}.
	 *
	 * @param <E>              the type of the iterator elements
	 * @param elementsSupplier the elements supplier with the state as parameter
	 * @return a {@code Stream} with the elements of {@code elementsSupplier}
	 */
	public static <E> Stream<E> stream(final Function<ElementsSupplierState<E>, E> elementsSupplier) {
		return stream(iterator(elementsSupplier));
	}

	/**
	 * Returns a sequential {@link Stream} with the {@code iterator} as its source.
	 *
	 * @param <E>      the type of the iterator elements
	 * @param iterator the iterator
	 * @return a {@code Stream} with the elements of {@code iterator}
	 */
	public static <E> Stream<E> stream(final Iterator<E> iterator) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
	}

	/**
	 * Implementation of a {@link PeekableIterator} based upon an elements supplier.
	 *
	 * <p>
	 * Check out {@link #iterator(Function)} for more information about the elements
	 * supplier.
	 *
	 * @param <E> the type of the iterator elements
	 */
	@RequiredArgsConstructor
	private static class ElementsSupplierIterator<E> implements PeekableIterator<E> {
		/**
		 * An elements supplier with the state as parameter.
		 *
		 * <p>
		 * Check out {@link #iterator(Function)} for more information.
		 */
		Function<ElementsSupplierState<E>, E> elementsSupplier;

		/**
		 * The next element supplied by {@link #elementsSupplier} if {@link #state} is
		 * {@link State#PEEKED}, else undefined.
		 */
		@NonFinal
		@Nullable
		E peekedElement = null;

		/**
		 * TODO
		 */
		@NonFinal
		State state = State.NOT_PEEKED;

		/**
		 * TODO: The end of data supplier to be passed to the elements supplier.
		 */
		ElementsSupplierState<E> stateHandler = () -> {
			state = State.END_OF_DATA;
			return null;
		};

		/** {@inheritDoc} */
		@Override
		@SuppressWarnings("checkstyle:XIllegalCatchDefault")
		public final boolean hasNext() {
			if (state == State.NOT_PEEKED) {
				peekedElement = elementsSupplier.apply(stateHandler);
				if (state == State.NOT_PEEKED) {
					state = State.PEEKED;
				}
			}
			return state == State.PEEKED;
		}

		/** {@inheritDoc} */
		@Nullable
		@Override
		public final E next() {
			final E next = peek();
			if (state == State.PEEKED) {
				state = State.NOT_PEEKED;
			}
			peekedElement = null;
			return next;
		}

		/** {@inheritDoc} */
		@Nullable
		@Override
		public E peek() {
			// Method "hasNext" peeks the next element (if required)
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return peekedElement;
		}
	}

	/**
	 * TODO: Implementation of the elements supplier state. An instance of this
	 * class is passed to the elements supplier at the time of calculating the next
	 * element.
	 *
	 * <p>
	 * It is used to mark the iteration's end. Check out {@link #iterator(Function)}
	 * for more information.
	 *
	 * @param <E> the type of the iterator elements
	 */
	public interface ElementsSupplierState<E> {
		/**
		 * Marks the iteration's end, the so called <i>end of data</i>.
		 *
		 * <p>
		 * If this has been called the return value of the elements supplier will not be
		 * taken into account.
		 *
		 * <p>
		 * Check out {@link #iterator(Function)} for an usage example.
		 *
		 * @param <T> the type of the iterator elements
		 * @return any valid value, probably {@code null}
		 */
		@Nullable
		E endOfData();
	}

	/**
	 * TODO
	 */
	private enum State {
		/**
		 * TODO
		 */
		END_OF_DATA,

		/**
		 * TODO
		 */
		NOT_PEEKED,

		/**
		 * TODO
		 */
		PEEKED;
	}
}
