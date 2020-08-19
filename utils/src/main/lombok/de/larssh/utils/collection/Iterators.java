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
	 * Note: This iterator is <b>not</b> thread safe.
	 *
	 * <p>
	 * <b>Usage example:</b> The following shows how to create an iterator returning
	 * every second element only.
	 *
	 * <pre>
	 * Iterator&lt;E&gt; oldIterator = ...;
	 * Iterator&lt;E&gt; newIterator = Iterators.iterator(state -&gt; {
	 *     if (!oldIterator.hasNext() &amp;&amp; !oldIterator.hasNext()) {
	 *         return state.endOfData();
	 *     }
	 *     return oldIterator.next();
	 * });
	 * </pre>
	 *
	 * @param <E>              the type of the iterator elements
	 * @param elementsSupplier the elements supplier with the state as parameter
	 * @return a {@link PeekableIterator} wrapping {@code elementsSupplier}
	 */
	public static <E> PeekableIterator<E> iterator(
			final Function<ElementsSupplierStateHandler<E>, E> elementsSupplier) {
		return new ElementsSupplierIterator<>(elementsSupplier);
	}

	/**
	 * Wraps {@code iterator} into a {@link PeekableIterator}.
	 *
	 * <p>
	 * Note: The resulting iterator is <b>not</b> thread safe.
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
	public static <E> Stream<E> stream(final Function<ElementsSupplierStateHandler<E>, E> elementsSupplier) {
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
		Function<ElementsSupplierStateHandler<E>, E> elementsSupplier;

		/**
		 * The next element supplied by {@link #elementsSupplier} if {@link #state} is
		 * {@link ElementsSupplierState#PEEKED}, else undefined.
		 */
		@NonFinal
		@Nullable
		E peekedElement = null;

		/**
		 * The iterator's current inner state
		 */
		@NonFinal
		ElementsSupplierState state = ElementsSupplierState.CALL_FOR_NEXT;

		/**
		 * Implementation of {@link ElementsSupplierStateHandler} to update this
		 * iterator's current state
		 */
		ElementsSupplierStateHandler<E> stateHandler = () -> {
			state = ElementsSupplierState.END_OF_DATA;
			return null;
		};

		/** {@inheritDoc} */
		@Override
		@SuppressWarnings("checkstyle:XIllegalCatchDefault")
		public final boolean hasNext() {
			if (state == ElementsSupplierState.CALL_FOR_NEXT) {
				peekedElement = elementsSupplier.apply(stateHandler);
				if (state == ElementsSupplierState.CALL_FOR_NEXT) {
					state = ElementsSupplierState.PEEKED;
				}
			}
			return state == ElementsSupplierState.PEEKED;
		}

		/** {@inheritDoc} */
		@Nullable
		@Override
		@SuppressWarnings("PMD.NullAssignment")
		public final E next() {
			// Method "hasNext" peeks the next element (if required)
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			final E next = peekedElement;
			if (state == ElementsSupplierState.PEEKED) {
				state = ElementsSupplierState.CALL_FOR_NEXT;
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
	 * This enumeration contains the possible inner states of
	 * {@link ElementsSupplierIterator}.
	 */
	@SuppressWarnings("PMD.UnnecessaryModifier")
	private enum ElementsSupplierState {
		/**
		 * Status of iterators, that need to call the elements supplier once information
		 * about the next element is required.
		 */
		CALL_FOR_NEXT,

		/**
		 * Status representing the end of data. The elements supplier must not be called
		 * any longer. This is an end state and must not change once reached.
		 */
		END_OF_DATA,

		/**
		 * Status of iterators, that peeked the next element already.
		 */
		PEEKED;
	}

	/**
	 * An instance of this is passed to the elements supplier at the time of
	 * calculating the next element. It is used to mark the iteration's end. Check
	 * out {@link #endOfData()} for more information.
	 *
	 * @param <E> the type of the iterator elements
	 */
	@FunctionalInterface
	@SuppressWarnings("PMD.UnnecessaryModifier")
	public interface ElementsSupplierStateHandler<E> {
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
		 * @return any valid value, probably {@code null}
		 */
		@Nullable
		E endOfData();
	}
}
