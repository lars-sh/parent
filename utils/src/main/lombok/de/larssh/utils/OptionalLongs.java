package de.larssh.utils;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.stream.LongStream;

import de.larssh.utils.function.LongToLongFunction;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link OptionalLong}.
 */
@UtilityClass
public class OptionalLongs {
	/**
	 * Predicate returning {@code true} for all values less than zero.
	 */
	public static final LongPredicate IS_NEGATIVE = value -> value < 0;

	/**
	 * Predicate returning {@code true} for all values less than or equal to zero.
	 */
	public static final LongPredicate IS_NEGATIVE_OR_ZERO = value -> value <= 0;

	/**
	 * Predicate returning {@code true} for all values greater than zero.
	 */
	public static final LongPredicate IS_POSITIVE = value -> value > 0;

	/**
	 * Predicate returning {@code true} for all values greater than or equal to
	 * zero.
	 */
	public static final LongPredicate IS_POSITIVE_OR_ZERO = value -> value >= 0;

	/**
	 * Returns a {@link Optional} consisting of the elements of {@code optional},
	 * boxed to a {@link Long}.
	 *
	 * @param optional optional value
	 * @return a {@link Optional} consistent of the elements of {@code optional},
	 *         boxed to a {@link Long}
	 */
	public static Optional<Long> boxed(final OptionalLong optional) {
		return optional.isPresent() ? Optional.ofNullable(optional.getAsLong()) : Optional.empty();
	}

	/**
	 * If a value is present, and the value matches the given predicate, return an
	 * {@link OptionalLong} describing the value, otherwise return an empty
	 * {@link OptionalLong}.
	 *
	 * @param optional  optional value
	 * @param predicate a predicate to apply to the value, if present
	 * @return an {@link OptionalLong} describing the value of {@code optional} if a
	 *         value is present and the value matches the given predicate, otherwise
	 *         an empty {@link OptionalLong}
	 */
	public static OptionalLong filter(final OptionalLong optional, final LongPredicate predicate) {
		return optional.isPresent() && predicate.test(optional.getAsLong()) ? optional : OptionalLong.empty();
	}

	/**
	 * If a value is present, apply the provided {@link OptionalLong}-bearing
	 * mapping function to it, return that result, otherwise return an empty
	 * {@link OptionalLong}. This method is similar to
	 * {@link #map(OptionalLong, LongToLongFunction)}, but the provided mapper is
	 * one whose result is already an {@code OptionalLong}, and if invoked,
	 * {@code flatMapToDouble} does not wrap it with an additional
	 * {@link OptionalLong}.
	 *
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present the
	 *                 mapping function
	 * @return the result of applying an {@link OptionalLong}-bearing mapping
	 *         function to the value of this {@link OptionalLong}, if a value is
	 *         present, otherwise an empty {@link OptionalLong}
	 */
	public static OptionalLong flatMap(final OptionalLong optional, final LongFunction<OptionalLong> mapper) {
		return optional.isPresent() ? mapper.apply(optional.getAsLong()) : OptionalLong.empty();
	}

	/**
	 * If a value is present, apply the provided {@link OptionalDouble}-bearing
	 * mapping function to it, return that result, otherwise return an empty
	 * {@link OptionalDouble}. This method is similar to
	 * {@link #mapToDouble(OptionalLong, LongToDoubleFunction)}, but the provided
	 * mapper is one whose result is already an {@code OptionalDouble}, and if
	 * invoked, {@code flatMapToDouble} does not wrap it with an additional
	 * {@link OptionalDouble}.
	 *
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present the
	 *                 mapping function
	 * @return the result of applying an {@link OptionalDouble}-bearing mapping
	 *         function to the value of this {@link OptionalDouble}, if a value is
	 *         present, otherwise an empty {@link OptionalDouble}
	 */
	public static OptionalDouble flatMapToDouble(final OptionalLong optional,
			final LongFunction<OptionalDouble> mapper) {
		return optional.isPresent() ? mapper.apply(optional.getAsLong()) : OptionalDouble.empty();
	}

	/**
	 * If a value is present, apply the provided {@link OptionalInt}-bearing mapping
	 * function to it, return that result, otherwise return an empty
	 * {@link OptionalInt}. This method is similar to
	 * {@link #mapToInt(OptionalLong, LongToIntFunction)}, but the provided mapper
	 * is one whose result is already an {@code OptionalInt}, and if invoked,
	 * {@code flatMap} does not wrap it with an additional {@link OptionalInt}.
	 *
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present the
	 *                 mapping function
	 * @return the result of applying an {@link OptionalInt}-bearing mapping
	 *         function to the value of this {@link OptionalInt}, if a value is
	 *         present, otherwise an empty {@link OptionalInt}
	 */
	public static OptionalInt flatMapToInt(final OptionalLong optional, final LongFunction<OptionalInt> mapper) {
		return optional.isPresent() ? mapper.apply(optional.getAsLong()) : OptionalInt.empty();
	}

	/**
	 * If a value is present, apply the provided {@link Optional}-bearing mapping
	 * function to it, return that result, otherwise return an empty
	 * {@link Optional}. This method is similar to
	 * {@link #mapToObj(OptionalLong, LongFunction)}, but the provided mapper is one
	 * whose result is already an {@code Optional}, and if invoked,
	 * {@code flatMapToDouble} does not wrap it with an additional {@link Optional}.
	 *
	 * @param <T>      the type parameter to the {@code Optional} returned
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present the
	 *                 mapping function
	 * @return the result of applying an {@link Optional}-bearing mapping function
	 *         to the value of this {@link Optional}, if a value is present,
	 *         otherwise an empty {@link Optional}
	 */
	public static <T> Optional<T> flatMapToObj(final OptionalLong optional, final LongFunction<Optional<T>> mapper) {
		return optional.isPresent() ? mapper.apply(optional.getAsLong()) : Optional.empty();
	}

	/**
	 * If a value is present, applies {@code mapper} to {@code optional} and returns
	 * an {@link OptionalLong} describing the result. Otherwise returns an empty
	 * {@link OptionalLong}.
	 *
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present
	 * @return an {@link OptionalLong} describing the result of applying
	 *         {@code mapper} function to the value of {@code optional}, if a value
	 *         is present, otherwise an empty {@link OptionalLong}
	 */
	public static OptionalLong map(final OptionalLong optional, final LongToLongFunction mapper) {
		return optional.isPresent() ? OptionalLong.of(mapper.applyAsLong(optional.getAsLong())) : OptionalLong.empty();
	}

	/**
	 * If a value is present, applies {@code mapper} to {@code optional} and returns
	 * an {@link OptionalDouble} describing the result. Otherwise returns an empty
	 * {@link OptionalDouble}.
	 *
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present
	 * @return an {@link OptionalDouble} describing the result of applying
	 *         {@code mapper} function to the value of {@code optional}, if a value
	 *         is present, otherwise an empty {@link OptionalDouble}
	 */
	public static OptionalDouble mapToDouble(final OptionalLong optional, final LongToDoubleFunction mapper) {
		return optional.isPresent()
				? OptionalDouble.of(mapper.applyAsDouble(optional.getAsLong()))
				: OptionalDouble.empty();
	}

	/**
	 * If a value is present, applies {@code mapper} to {@code optional} and returns
	 * an {@link OptionalInt} describing the result. Otherwise returns an empty
	 * {@link OptionalInt}.
	 *
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present
	 * @return an {@link OptionalInt} describing the result of applying
	 *         {@code mapper} function to the value of {@code optional}, if a value
	 *         is present, otherwise an empty {@link OptionalInt}
	 */
	public static OptionalInt mapToInt(final OptionalLong optional, final LongToIntFunction mapper) {
		return optional.isPresent() ? OptionalInt.of(mapper.applyAsInt(optional.getAsLong())) : OptionalInt.empty();
	}

	/**
	 * If a value is present, applies {@code mapper} to {@code optional} and returns
	 * an {@link Optional} describing the result. Otherwise returns an empty
	 * {@link Optional}.
	 *
	 * @param <T>      the type of the result of {@code mapper}
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present
	 * @return an {@link Optional} describing the result of applying {@code mapper}
	 *         function to the value of {@code optional}, if a value is present,
	 *         otherwise an empty {@link Optional}
	 */
	public static <T> Optional<T> mapToObj(final OptionalLong optional, final LongFunction<T> mapper) {
		return optional.isPresent() ? Optional.ofNullable(mapper.apply(optional.getAsLong())) : Optional.empty();
	}

	/**
	 * Returns an {@link OptionalLong} describing the specified value, if non-empty,
	 * otherwise returns an empty {@link OptionalLong}. The term <i>empty</i> is
	 * described by {@code isEmpty}.
	 *
	 * @param isEmpty predicate describing the term <i>empty</i>
	 * @param value   the possibly-empty value to describe
	 * @return an {@link OptionalLong} with a present value if the specified value
	 *         is non-empty, otherwise an empty {@link OptionalLong}
	 */
	public static OptionalLong ofNon(final LongPredicate isEmpty, final long value) {
		return isEmpty.test(value) ? OptionalLong.empty() : OptionalLong.of(value);
	}

	/**
	 * Returns an {@link OptionalLong} describing the specified value, if non-null,
	 * otherwise returns an empty {@link OptionalLong}.
	 *
	 * @param value the possibly-null value to describe
	 * @return an {@link OptionalLong} with a present value if the specified value
	 *         is non-null, otherwise an empty {@link OptionalLong}
	 */
	public static OptionalLong ofNullable(@Nullable final Long value) {
		return value == null ? OptionalLong.empty() : OptionalLong.of(value);
	}

	/**
	 * Returns a stream consisting of present elements.
	 *
	 * @param optionals array of optional elements
	 * @return the new stream
	 */
	@SafeVarargs
	public static LongStream stream(final OptionalLong... optionals) {
		return Arrays.stream(optionals).filter(OptionalLong::isPresent).mapToLong(OptionalLong::getAsLong);
	}
}
