package de.larssh.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

import de.larssh.utils.text.Characters;
import de.larssh.utils.text.Strings;
import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link Optional}.
 */
@UtilityClass
@SuppressWarnings("PMD.GodClass")
public class Optionals {
	/**
	 * Creates a {@link Comparator} to compare {@link Optional}s based on the inner
	 * types {@link Comparable} implementation.
	 *
	 * @param <T> optional value type
	 * @return comparator for optional
	 */
	public static <T extends Comparable<T>> Comparator<? super Optional<? extends T>> comparator() {
		return comparator((first, second) -> {
			if (first == null) {
				if (second == null) {
					return 0;
				}

				// The integer value MIN_VALUE cannot be negated in the integer range as
				// [-MIN_VALUE] is mathematically equal to [MAX_VALUE + 1].
				final int compare = second.compareTo(null);
				return compare == Integer.MIN_VALUE ? Integer.MAX_VALUE : -compare;
			}
			return first.compareTo(second);
		});
	}

	/**
	 * Wraps a {@link Comparator} to allow compare {@link Optional}s.
	 *
	 * <p>
	 * When comparing {@code comparator} is called with the optionals object if
	 * present or {@code null}. Therefore it needs to handle null values
	 * appropriately. Use {@link Comparator#nullsFirst(Comparator)} and
	 * {@link Comparator#nullsLast(Comparator)} if your comparator does not.
	 *
	 * @param <T>        optional value type
	 * @param comparator optional value comparator
	 * @return comparator for optional
	 */
	public static <T> Comparator<? super Optional<? extends T>> comparator(final Comparator<? super T> comparator) {
		return (first, second) -> comparator.compare(Nullables.map(second, optional -> optional.orElse(null)),
				Nullables.map(second, optional -> optional.orElse(null)));
	}

	/**
	 * Wraps a {@link Comparator} to allow compare {@link Optional}s.
	 *
	 * @param <T>          the type of element to be compared
	 * @param <U>          the type of the sort key
	 * @param keyExtractor method to extract the optional sort key
	 * @return comparator for optional
	 */
	public static <T, U extends Comparable<U>> Comparator<T> comparator(final Function<T, Optional<U>> keyExtractor) {
		return (first, second) -> {
			final Optional<U> firstOptional = keyExtractor.apply(first);
			final Optional<U> secondOptional = keyExtractor.apply(second);

			if (firstOptional.isPresent()) {
				return secondOptional.isPresent() ? firstOptional.get().compareTo(secondOptional.get()) : 1;
			}
			return secondOptional.isPresent() ? -1 : 0;
		};
	}

	/**
	 * If a value is present, apply the provided {@link OptionalDouble}-bearing
	 * mapping function to it, return that result, otherwise return an empty
	 * {@link OptionalDouble}. This method is similar to
	 * {@link #mapToDouble(Optional, ToDoubleFunction)}, but the provided mapper is
	 * one whose result is already an {@code OptionalDouble}, and if invoked,
	 * {@code flatMapToDouble} does not wrap it with an additional
	 * {@link OptionalDouble}.
	 *
	 * @param <T>      element type
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present the
	 *                 mapping function
	 * @return the result of applying an {@link OptionalDouble}-bearing mapping
	 *         function to the value of this {@link OptionalDouble}, if a value is
	 *         present, otherwise an empty {@link OptionalDouble}
	 */
	public static <T> OptionalDouble flatMapToDouble(final Optional<T> optional,
			final Function<? super T, OptionalDouble> mapper) {
		return optional.isPresent() ? mapper.apply(optional.get()) : OptionalDouble.empty();
	}

	/**
	 * If a value is present, apply the provided {@link OptionalInt}-bearing mapping
	 * function to it, return that result, otherwise return an empty
	 * {@link OptionalInt}. This method is similar to
	 * {@link #mapToInt(Optional, ToIntFunction)}, but the provided mapper is one
	 * whose result is already an {@code OptionalInt}, and if invoked,
	 * {@code flatMap} does not wrap it with an additional {@link OptionalInt}.
	 *
	 * @param <T>      element type
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present the
	 *                 mapping function
	 * @return the result of applying an {@link OptionalInt}-bearing mapping
	 *         function to the value of this {@link OptionalInt}, if a value is
	 *         present, otherwise an empty {@link OptionalInt}
	 */
	public static <T> OptionalInt flatMapToInt(final Optional<T> optional,
			final Function<? super T, OptionalInt> mapper) {
		return optional.isPresent() ? mapper.apply(optional.get()) : OptionalInt.empty();
	}

	/**
	 * If a value is present, apply the provided {@link OptionalLong}-bearing
	 * mapping function to it, return that result, otherwise return an empty
	 * {@link OptionalLong}. This method is similar to
	 * {@link #mapToLong(Optional, ToLongFunction)}, but the provided mapper is one
	 * whose result is already an {@code OptionalLong}, and if invoked,
	 * {@code flatMapToDouble} does not wrap it with an additional
	 * {@link OptionalLong}.
	 *
	 * @param <T>      element type
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present the
	 *                 mapping function
	 * @return the result of applying an {@link OptionalLong}-bearing mapping
	 *         function to the value of this {@link OptionalLong}, if a value is
	 *         present, otherwise an empty {@link OptionalLong}
	 */
	public static <T> OptionalLong flatMapToLong(final Optional<T> optional,
			final Function<? super T, OptionalLong> mapper) {
		return optional.isPresent() ? mapper.apply(optional.get()) : OptionalLong.empty();
	}

	/**
	 * Returns the first present value. Returns empty if no value is present.
	 *
	 * @param <T>    value type
	 * @param values any number of optional values to test
	 * @return the first present value, empty if no value is present
	 */
	@SafeVarargs
	public static <T> Optional<T> getFirst(final Optional<T>... values) {
		for (final Optional<T> value : values) {
			if (value.isPresent()) {
				return value;
			}
		}
		return Optional.empty();
	}

	/**
	 * Returns the first present value. Values are created on-demand by calling
	 * {@code suppliers} one after the other. Returns empty if no value is present.
	 *
	 * @param <T>       value type
	 * @param suppliers any number of value suppliers, which values to test,
	 *                  evaluated in a lazy manner
	 * @return the first present value, empty if no value is present
	 */
	@SafeVarargs
	public static <T> Optional<T> getFirst(final Supplier<? extends Optional<T>>... suppliers) {
		for (final Supplier<? extends Optional<T>> supplier : suppliers) {
			final Optional<T> value = supplier.get();
			if (value.isPresent()) {
				return value;
			}
		}
		return Optional.empty();
	}

	/**
	 * Returns an {@link Optional} describing the first value matching the predicate
	 * {@code isPresent}. Values are created on-demand by calling {@code suppliers}
	 * one after the other. Returns an empty {@link Optional} if no element matches.
	 *
	 * @param <T>       type of the return value
	 * @param isPresent predicate to match
	 * @param suppliers any number of value suppliers, which values to test,
	 *                  evaluated in a lazy manner
	 * @return an {@link Optional} describing the first value matching the predicate
	 *         {@code isPresent}, an empty {@link Optional} if no element matches
	 */
	@SafeVarargs
	public static <T> Optional<T> getFirst(final Predicate<? super T> isPresent,
			final Supplier<? extends T>... suppliers) {
		for (final Supplier<? extends T> supplier : suppliers) {
			final T value = supplier.get();
			if (isPresent.test(value)) {
				return Optional.ofNullable(value);
			}
		}
		return Optional.empty();
	}

	/**
	 * Returns an {@link Optional} describing the first value matching the predicate
	 * {@code isPresent}. Returns an empty {@link Optional} if no element matches.
	 *
	 * @param <T>       type of the return value
	 * @param isPresent predicate to match
	 * @param values    any number of values to test
	 * @return an {@link Optional} describing the first value matching the predicate
	 *         {@code isPresent}, an empty {@link Optional} if no element matches
	 */
	@SafeVarargs
	public static <T> Optional<T> getFirstValue(final Predicate<? super T> isPresent, final T... values) {
		for (final T value : values) {
			if (isPresent.test(value)) {
				return Optional.ofNullable(value);
			}
		}
		return Optional.empty();
	}

	/**
	 * If a value is present, returns an {@link OptionalDouble} describing the
	 * result. Otherwise returns an empty {@link OptionalDouble}.
	 *
	 * @param optional optional value
	 * @return an {@link OptionalDouble} describing the value of {@code optional},
	 *         if a value is present, otherwise an empty {@link OptionalDouble}
	 */
	@SuppressFBWarnings(value = "FII_USE_METHOD_REFERENCE", justification = "Missing ToLongFunction.identity()")
	public static OptionalDouble mapToDouble(final Optional<Double> optional) {
		return mapToDouble(optional, value -> value);
	}

	/**
	 * If a value is present, applies {@code mapper} to {@code optional} and returns
	 * an {@link OptionalDouble} describing the result. Otherwise returns an empty
	 * {@link OptionalDouble}.
	 *
	 * @param <T>      element type
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present
	 * @return an {@link OptionalDouble} describing the result of applying
	 *         {@code mapper} function to the value of {@code optional}, if a value
	 *         is present, otherwise an empty {@link OptionalDouble}
	 */
	public static <T> OptionalDouble mapToDouble(final Optional<T> optional, final ToDoubleFunction<? super T> mapper) {
		return optional.isPresent() ? OptionalDouble.of(mapper.applyAsDouble(optional.get())) : OptionalDouble.empty();
	}

	/**
	 * If a value is present, returns an {@link OptionalInt} describing the result.
	 * Otherwise returns an empty {@link OptionalInt}.
	 *
	 * @param optional optional value
	 * @return an {@link OptionalInt} describing the value of {@code optional}, if a
	 *         value is present, otherwise an empty {@link OptionalInt}
	 */
	@SuppressFBWarnings(value = "FII_USE_METHOD_REFERENCE", justification = "Missing ToLongFunction.identity()")
	public static OptionalInt mapToInt(final Optional<Integer> optional) {
		return mapToInt(optional, value -> value);
	}

	/**
	 * If a value is present, applies {@code mapper} to {@code optional} and returns
	 * an {@link OptionalInt} describing the result. Otherwise returns an empty
	 * {@link OptionalInt}.
	 *
	 * @param <T>      element type
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present
	 * @return an {@link OptionalInt} describing the result of applying
	 *         {@code mapper} function to the value of {@code optional}, if a value
	 *         is present, otherwise an empty {@link OptionalInt}
	 */
	public static <T> OptionalInt mapToInt(final Optional<T> optional, final ToIntFunction<? super T> mapper) {
		return optional.isPresent() ? OptionalInt.of(mapper.applyAsInt(optional.get())) : OptionalInt.empty();
	}

	/**
	 * If a value is present, returns an {@link OptionalLong} describing the result.
	 * Otherwise returns an empty {@link OptionalLong}.
	 *
	 * @param optional optional value
	 * @return an {@link OptionalLong} describing the value of {@code optional}, if
	 *         a value is present, otherwise an empty {@link OptionalLong}
	 */
	@SuppressFBWarnings(value = "FII_USE_METHOD_REFERENCE", justification = "Missing ToLongFunction.identity()")
	public static OptionalLong mapToLong(final Optional<Long> optional) {
		return mapToLong(optional, value -> value);
	}

	/**
	 * If a value is present, applies {@code mapper} to {@code optional} and returns
	 * an {@link OptionalLong} describing the result. Otherwise returns an empty
	 * {@link OptionalLong}.
	 *
	 * @param <T>      element type
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present
	 * @return an {@link OptionalLong} describing the result of applying
	 *         {@code mapper} function to the value of {@code optional}, if a value
	 *         is present, otherwise an empty {@link OptionalLong}
	 */
	public static <T> OptionalLong mapToLong(final Optional<T> optional, final ToLongFunction<? super T> mapper) {
		return optional.isPresent() ? OptionalLong.of(mapper.applyAsLong(optional.get())) : OptionalLong.empty();
	}

	/**
	 * Returns an {@link Optional} describing the specified value, if non-null and
	 * non-empty, otherwise returns an empty {@link Optional}. The term <i>empty</i>
	 * is described by {@code isEmpty}.
	 *
	 * @param <T>     value type
	 * @param isEmpty predicate describing the term <i>empty</i>
	 * @param value   the possibly-null-or-empty value to describe
	 * @return an {@link Optional} with a present value if the specified value is
	 *         non-null and non-empty, otherwise an empty {@link Optional}
	 */
	public static <T> Optional<T> ofNon(final Predicate<? super T> isEmpty, @Nullable final T value) {
		return value == null || isEmpty.test(value) ? Optional.empty() : Optional.of(value);
	}

	/**
	 * Returns an {@link Optional} describing the specified string, if non-null and
	 * non-blank, otherwise returns an empty {@link Optional}.
	 *
	 * @param string the possibly-null-or-blank string to describe
	 * @return an {@link Optional} with a present value if the specified string is
	 *         non-null and non-empty after being trimmed, otherwise an empty
	 *         {@link Optional}
	 */
	public static Optional<String> ofNonBlank(@Nullable final String string) {
		return ofNon(Strings::isBlank, string);
	}

	/**
	 * Returns an {@link Optional} describing the specified array, if non-null and
	 * non-empty, otherwise returns an empty {@link Optional}.
	 *
	 * @param <T>   array element type
	 * @param array the possibly-null-or-empty array to describe
	 * @return an {@link Optional} with a present value if the specified array is
	 *         non-null and non-empty, otherwise an empty {@link Optional}
	 */
	@SuppressWarnings("PMD.UseVarargs")
	@SuppressFBWarnings(value = "UVA_USE_VAR_ARGS",
			justification = "var args make no sense as their length is constant")
	public static <T> Optional<T[]> ofNonEmpty(@Nullable final T[] array) {
		return ofNon(a -> a.length == 0, array);
	}

	/**
	 * Returns an {@link Optional} describing the specified collection, if non-null
	 * and non-empty, otherwise returns an empty {@link Optional}.
	 *
	 * @param <T>        collection and collection element type
	 * @param collection the possibly-null-or-empty collection to describe
	 * @return an {@link Optional} with a present value if the specified collection
	 *         is non-null and non-empty, otherwise an empty {@link Optional}
	 */
	public static <T extends Collection<?>> Optional<T> ofNonEmpty(@Nullable final T collection) {
		return ofNon(Collection::isEmpty, collection);
	}

	/**
	 * Returns an {@link Optional} describing the specified string, if non-null and
	 * non-empty, otherwise returns an empty {@link Optional}.
	 *
	 * @param string the possibly-null-or-empty string to describe
	 * @return an {@link Optional} with a present value if the specified string is
	 *         non-null and non-empty, otherwise an empty {@link Optional}
	 */
	public static Optional<String> ofNonEmpty(@Nullable final String string) {
		return ofNon(String::isEmpty, string);
	}

	/**
	 * Returns an {@link Optional} describing the specified arrays first element, if
	 * present, otherwise returns an empty {@link Optional}. Throws a
	 * {@link TooManyElementsException} if {@code array} contains more than one
	 * element.
	 *
	 * @param <T>   array element type
	 * @param array the array describing the element
	 * @return an {@link Optional} describing the specified arrays first element, if
	 *         present, otherwise returns an empty {@link Optional}
	 * @throws TooManyElementsException if {@code array} contains more than one
	 *                                  element
	 */
	@SuppressWarnings("PMD.UseVarargs")
	@SuppressFBWarnings(value = "UVA_USE_VAR_ARGS",
			justification = "var args make no sense as their length is constant")
	public static <T> Optional<T> ofSingle(final T[] array) {
		if (array.length == 0) {
			return Optional.empty();
		}
		if (array.length == 1) {
			return Optional.ofNullable(array[0]);
		}
		throw new TooManyElementsException();
	}

	/**
	 * Returns an {@link Optional} describing the specified iterables first element,
	 * if present, otherwise returns an empty {@link Optional}. Throws a
	 * {@link TooManyElementsException} if {@code iterable} contains more than one
	 * element.
	 *
	 * @param <T>      iterable element type
	 * @param iterable the iterable describing the element
	 * @return an {@link Optional} describing the specified iterables first element,
	 *         if present, otherwise returns an empty {@link Optional}
	 * @throws TooManyElementsException if {@code iterable} contains more than one
	 *                                  element
	 */
	public static <T> Optional<T> ofSingle(final Iterable<T> iterable) {
		return ofSingle(iterable.iterator());
	}

	/**
	 * Returns an {@link Optional} describing the specified iterators first element,
	 * if present, otherwise returns an empty {@link Optional}. Throws a
	 * {@link TooManyElementsException} if {@code iterator} contains more than one
	 * element.
	 *
	 * @param <T>      iterator element type
	 * @param iterator the iterator describing the element
	 * @return an {@link Optional} describing the specified iterators first element,
	 *         if present, otherwise returns an empty {@link Optional}
	 * @throws TooManyElementsException if {@code iterator} contains more than one
	 *                                  element
	 */
	@SuppressWarnings("PMD.PrematureDeclaration")
	public static <T> Optional<T> ofSingle(final Iterator<T> iterator) {
		if (!iterator.hasNext()) {
			return Optional.empty();
		}

		final Optional<T> optional = Optional.ofNullable(iterator.next());
		if (iterator.hasNext()) {
			throw new TooManyElementsException();
		}

		return optional;
	}

	/**
	 * Returns an {@link Optional} describing the specified streams first element,
	 * if present, otherwise returns an empty {@link Optional}. Throws a
	 * {@link TooManyElementsException} if {@code stream} contains more than one
	 * element.
	 *
	 * <p>
	 * This is a terminal operation.
	 *
	 * @param <T>    stream element type
	 * @param stream the stream describing the element
	 * @return an {@link Optional} describing the specified streams first element,
	 *         if present, otherwise returns an empty {@link Optional}
	 * @throws TooManyElementsException if {@code stream} contains more than one
	 *                                  element
	 */
	public static <T> Optional<T> ofSingle(final Stream<T> stream) {
		return ofSingle(stream.iterator());
	}

	/**
	 * Returns an {@link OptionalInt} describing the specified strings first
	 * character, if present, otherwise returns an empty {@link OptionalInt}. Throws
	 * a {@link TooManyElementsException} if {@code string} contains more than one
	 * character, ignoring trailing whitespaces.
	 *
	 * <p>
	 * Whitespace characters are recognized using
	 * {@link Characters#isAsciiWhitespace(char)}.
	 *
	 * @param string the string describing the character
	 * @return an {@link OptionalInt} describing the specified strings first
	 *         character, if present, otherwise returns an empty {@link OptionalInt}
	 * @throws TooManyElementsException if {@code string} contains more than one
	 *                                  character, ignoring trailing whitespaces
	 */
	public static OptionalInt ofSingle(final CharSequence string) {
		final int length = string.length();
		if (length == 0) {
			return OptionalInt.empty();
		}

		for (int index = 1; index < length; index += 1) {
			if (!Characters.isAsciiWhitespace(string.charAt(index))) {
				throw new TooManyElementsException();
			}
		}
		return OptionalInt.of(string.charAt(0));
	}

	/**
	 * Returns a stream consisting of present elements.
	 *
	 * @param <T>       element type
	 * @param optionals array of optional elements
	 * @return the new stream
	 */
	@SafeVarargs
	public static <T> Stream<T> stream(final Optional<T>... optionals) {
		return Arrays.stream(optionals).filter(Optional::isPresent).map(Optional::get);
	}
}
