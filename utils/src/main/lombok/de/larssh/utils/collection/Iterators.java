package de.larssh.utils.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import de.larssh.utils.SneakyException;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
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
	@Getter(AccessLevel.PROTECTED)
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
		 * The end of data supplier to be passed to the elements supplier.
		 */
		ElementsSupplierState<E> state = new ElementsSupplierState<>();

		/** {@inheritDoc} */
		@Override
		@SuppressWarnings("checkstyle:XIllegalCatchDefault")
		public final boolean hasNext() {
			if (getState().isEnd()) {
				return false;
			}
			if (!getState().isPeeked()) {
				getState().peek(() -> getElementsSupplier().apply(getState()));
			}
			return getState().isPeeked();
		}

		/** {@inheritDoc} */
		@Nullable
		@Override
		public final E next() {
			final E next = peek();
			getState().unsetPeeked();
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
			return getState().getPeekedElement();
		}
	}

	/**
	 * Implementation of the elements supplier state. An instance of this class is
	 * passed to the elements supplier at the time of calculating the next element.
	 *
	 * <p>
	 * It is used to mark the iteration's end. Check out {@link #iterator(Function)}
	 * for more information.
	 *
	 * @param <E> the type of the iterator elements
	 */
	@Getter(AccessLevel.PRIVATE)
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ElementsSupplierState<E> {
		/**
		 * Specifies if the iterators end has been reached and no more elements can be
		 * read.
		 *
		 * <p>
		 * It has no effect if {@link #illegalState} is {@code true}.
		 */
		@NonFinal
		boolean end = false;

		/**
		 * Specifies if in an illegal state
		 */
		@NonFinal
		boolean illegalState = false;

		/**
		 * Optional cause of an illegal state.
		 *
		 * <p>
		 * This field <b>might</b> be non-null in case {@link #illegalState} is
		 * {@code true}, else undefined.
		 */
		@NonFinal
		@Nullable
		Throwable illegalStateCause = null;

		/**
		 * Specifies if the next element has been peeked by calling
		 * {@link #elementsSupplier}.
		 *
		 * <p>
		 * It has no effect if {@link #illegalState} is {@code true} and <b>must</b> be
		 * {@code false} if {@link #end} is {@code true}.
		 */
		@NonFinal
		boolean peeked = false;

		/**
		 * The next element supplied by {@link #elementsSupplier} if {@link #peeked} is
		 * {@code true}, else undefined.
		 */
		@NonFinal
		@Nullable
		E peekedElement = null;

		/**
		 * Marks the iteration's end, the so called <i>end of data</i>.
		 *
		 * <p>
		 * Once this has been called the return value of the elements supplier will not
		 * be taken into account.
		 *
		 * <p>
		 * Check out {@link #iterator(Function)} for an usage example.
		 *
		 * @param <T> the type of the iterator elements
		 * @return any valid value, probably {@code null}
		 */
		@Nullable
		public E endOfData() {
			if (!isIllegalState()) {
				throw new IllegalStateException(
						"Must not call \"endOfData\" outside of the elements supplier iteration process.");
			}
			end = true;
			return null;
		}

		/**
		 * Try peeking and remember the cause in case of an exception
		 *
		 * @param <E>
		 * @param elementsSupplier
		 * @return
		 */
		private void peek(final Supplier<E> elementsSupplier) {
			if (isIllegalState()) {
				throw new IllegalStateException(
						"The iterator cannot be accessed. Either it is currently reading the next element or the previous read operation failed.",
						getIllegalStateCause());
			}

			try {
				illegalState = true;
				peekedElement = elementsSupplier.get();
				illegalState = false;

				// Reset peeked flag and make sure it is "false" upon the end flag is "true".
				peeked = !isEnd();
			} catch (final Exception e) {
				illegalStateCause = e;
				throw new SneakyException(e);
			}
		}

		private void unsetPeeked() {
			peeked = false;
			peekedElement = null;
		}
	}
}
