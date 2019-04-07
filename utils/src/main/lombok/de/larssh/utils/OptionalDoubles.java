package de.larssh.utils;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.stream.DoubleStream;

import de.larssh.utils.function.DoubleToDoubleFunction;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link OptionalDouble}.
 */
@UtilityClass
public class OptionalDoubles {
	/**
	 * Predicate returning {@code true} for all values less than zero.
	 */
	public static final DoublePredicate IS_NEGATIVE = i -> i < 0;

	/**
	 * Predicate returning {@code true} for all values less than or equal to zero.
	 */
	public static final DoublePredicate IS_NEGATIVE_OR_ZERO = i -> i <= 0;

	/**
	 * Predicate returning {@code true} for all values greater than zero.
	 */
	public static final DoublePredicate IS_POSITIVE = i -> i > 0;

	/**
	 * Predicate returning {@code true} for all values greater than or equal to
	 * zero.
	 */
	public static final DoublePredicate IS_POSITIVE_OR_ZERO = i -> i >= 0;

	/**
	 * Returns a {@link Optional} consisting of the elements of {@code optional},
	 * boxed to a {@link Double}.
	 *
	 * @param optional optional value
	 * @return a {@link Optional} consistent of the elements of {@code optional},
	 *         boxed to a {@link Double}
	 */
	public static Optional<Double> boxed(final OptionalDouble optional) {
		return optional.isPresent() ? Optional.ofNullable(optional.getAsDouble()) : Optional.empty();
	}

	/**
	 * If a value is present, and the value matches the given predicate, return an
	 * {@link OptionalDouble} describing the value, otherwise return an empty
	 * {@link OptionalDouble}.
	 *
	 * @param optional  optional value
	 * @param predicate a predicate to apply to the value, if present
	 * @return an {@link OptionalDouble} describing the value of {@code optional} if
	 *         a value is present and the value matches the given predicate,
	 *         otherwise an empty {@link OptionalDouble}
	 */
	public static OptionalDouble filter(final OptionalDouble optional, final DoublePredicate predicate) {
		return optional.isPresent() && predicate.test(optional.getAsDouble()) ? optional : OptionalDouble.empty();
	}

	/**
	 * If a value is present, apply the provided {@link OptionalDouble}-bearing
	 * mapping function to it, return that result, otherwise return an empty
	 * {@link OptionalDouble}. This method is similar to
	 * {@link #map(OptionalDouble, DoubleToDoubleFunction)}, but the provided mapper
	 * is one whose result is already an {@code OptionalDouble}, and if invoked,
	 * {@code flatMapToDouble} does not wrap it with an additional
	 * {@link OptionalDouble}.
	 *
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present the
	 *                 mapping function
	 * @return the result of applying an {@link OptionalDouble}-bearing mapping
	 *         function to the value of this {@link OptionalDouble}, if a value is
	 *         present, otherwise an empty {@link OptionalDouble}
	 */
	public static OptionalDouble flatMap(final OptionalDouble optional, final DoubleFunction<OptionalDouble> mapper) {
		return optional.isPresent() ? mapper.apply(optional.getAsDouble()) : OptionalDouble.empty();
	}

	/**
	 * If a value is present, apply the provided {@link OptionalInt}-bearing mapping
	 * function to it, return that result, otherwise return an empty
	 * {@link OptionalInt}. This method is similar to
	 * {@link #mapToInt(OptionalDouble, DoubleToIntFunction)}, but the provided
	 * mapper is one whose result is already an {@code OptionalInt}, and if invoked,
	 * {@code flatMap} does not wrap it with an additional {@link OptionalInt}.
	 *
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present the
	 *                 mapping function
	 * @return the result of applying an {@link OptionalInt}-bearing mapping
	 *         function to the value of this {@link OptionalInt}, if a value is
	 *         present, otherwise an empty {@link OptionalInt}
	 */
	public static OptionalInt flatMapToInt(final OptionalDouble optional, final DoubleFunction<OptionalInt> mapper) {
		return optional.isPresent() ? mapper.apply(optional.getAsDouble()) : OptionalInt.empty();
	}

	/**
	 * If a value is present, apply the provided {@link OptionalLong}-bearing
	 * mapping function to it, return that result, otherwise return an empty
	 * {@link OptionalLong}. This method is similar to
	 * {@link #mapToLong(OptionalDouble, DoubleToLongFunction)}, but the provided
	 * mapper is one whose result is already an {@code OptionalLong}, and if
	 * invoked, {@code flatMapToDouble} does not wrap it with an additional
	 * {@link OptionalLong}.
	 *
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present the
	 *                 mapping function
	 * @return the result of applying an {@link OptionalLong}-bearing mapping
	 *         function to the value of this {@link OptionalLong}, if a value is
	 *         present, otherwise an empty {@link OptionalLong}
	 */
	public static OptionalLong flatMapToLong(final OptionalDouble optional, final DoubleFunction<OptionalLong> mapper) {
		return optional.isPresent() ? mapper.apply(optional.getAsDouble()) : OptionalLong.empty();
	}

	/**
	 * If a value is present, apply the provided {@link Optional}-bearing mapping
	 * function to it, return that result, otherwise return an empty
	 * {@link Optional}. This method is similar to
	 * {@link #mapToObj(OptionalDouble, DoubleFunction)}, but the provided mapper is
	 * one whose result is already an {@code Optional}, and if invoked,
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
	public static <T> Optional<T> flatMapToObj(final OptionalDouble optional,
			final DoubleFunction<Optional<T>> mapper) {
		return optional.isPresent() ? mapper.apply(optional.getAsDouble()) : Optional.empty();
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
	public static OptionalDouble map(final OptionalDouble optional, final DoubleToDoubleFunction mapper) {
		return optional.isPresent()
				? OptionalDouble.of(mapper.applyAsDouble(optional.getAsDouble()))
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
	public static OptionalInt mapToInt(final OptionalDouble optional, final DoubleToIntFunction mapper) {
		return optional.isPresent() ? OptionalInt.of(mapper.applyAsInt(optional.getAsDouble())) : OptionalInt.empty();
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
	public static OptionalLong mapToLong(final OptionalDouble optional, final DoubleToLongFunction mapper) {
		return optional.isPresent()
				? OptionalLong.of(mapper.applyAsLong(optional.getAsDouble()))
				: OptionalLong.empty();
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
	public static <T> Optional<T> mapToObj(final OptionalDouble optional, final DoubleFunction<T> mapper) {
		return optional.isPresent() ? Optional.ofNullable(mapper.apply(optional.getAsDouble())) : Optional.empty();
	}

	/**
	 * Returns an {@link OptionalDouble} describing the specified value, if
	 * non-empty, otherwise returns an empty {@link OptionalDouble}. The term
	 * <i>empty</i> is described by {@code isEmpty}.
	 *
	 * @param isEmpty predicate describing the term <i>empty</i>
	 * @param value   the possibly-empty value to describe
	 * @return an {@link OptionalDouble} with a present value if the specified value
	 *         is non-empty, otherwise an empty {@link OptionalDouble}
	 */
	public static OptionalDouble ofNon(final DoublePredicate isEmpty, final double value) {
		return isEmpty.test(value) ? OptionalDouble.empty() : OptionalDouble.of(value);
	}

	/**
	 * Returns an {@link OptionalDouble} describing the specified value, if
	 * non-null, otherwise returns an empty {@link OptionalDouble}.
	 *
	 * @param value the possibly-null value to describe
	 * @return an {@link OptionalDouble} with a present value if the specified value
	 *         is non-null, otherwise an empty {@link OptionalDouble}
	 */
	public static OptionalDouble ofNullable(@Nullable final Double value) {
		return value == null ? OptionalDouble.empty() : OptionalDouble.of(value);
	}

	/**
	 * Returns a stream consisting of present elements.
	 *
	 * @param optionals array of optional elements
	 * @return the new stream
	 */
	@SafeVarargs
	public static DoubleStream stream(final OptionalDouble... optionals) {
		return Arrays.stream(optionals).filter(OptionalDouble::isPresent).mapToDouble(OptionalDouble::getAsDouble);
	}
}
