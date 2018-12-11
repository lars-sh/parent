package de.larssh.utils;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

import de.larssh.utils.text.Strings;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link Optional}.
 */
@UtilityClass
public class Optionals {
	/**
	 * If a value is present, apply the provided {@link OptionalDouble}-bearing
	 * mapping function to it, return that result, otherwise return an empty
	 * {@link OptionalDouble}. This method is similar to
	 * {@link #mapToDouble(Optional, ToDoubleFunction)}, but the provided mapper is
	 * one whose result is already an {@code OptionalDouble}, and if invoked,
	 * {@code flatMapToDouble} does not wrap it with an additional
	 * {@link OptionalDouble}.
	 *
	 * @param          <T> element type
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present the
	 *                 mapping function
	 * @return the result of applying an {@link OptionalDouble}-bearing mapping
	 *         function to the value of this {@link OptionalDouble}, if a value is
	 *         present, otherwise an empty {@link OptionalDouble}
	 */
	public static <T> OptionalDouble flatMapToDouble(final Optional<T> optional,
			final Function<T, OptionalDouble> mapper) {
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
	 * @param          <T> element type
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present the
	 *                 mapping function
	 * @return the result of applying an {@link OptionalInt}-bearing mapping
	 *         function to the value of this {@link OptionalInt}, if a value is
	 *         present, otherwise an empty {@link OptionalInt}
	 */
	public static <T> OptionalInt flatMapToInt(final Optional<T> optional, final Function<T, OptionalInt> mapper) {
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
	 * @param          <T> element type
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present the
	 *                 mapping function
	 * @return the result of applying an {@link OptionalLong}-bearing mapping
	 *         function to the value of this {@link OptionalLong}, if a value is
	 *         present, otherwise an empty {@link OptionalLong}
	 */
	public static <T> OptionalLong flatMapToLong(final Optional<T> optional, final Function<T, OptionalLong> mapper) {
		return optional.isPresent() ? mapper.apply(optional.get()) : OptionalLong.empty();
	}

	/**
	 * If a value is present, applies {@code mapper} to {@code optional} and returns
	 * an {@link OptionalDouble} describing the result. Otherwise returns an empty
	 * {@link OptionalDouble}.
	 *
	 * @param          <T> element type
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present
	 * @return an {@link OptionalDouble} describing the result of applying
	 *         {@code mapper} function to the value of {@code optional}, if a value
	 *         is present, otherwise an empty {@link OptionalDouble}
	 */
	public static <T> OptionalDouble mapToDouble(final Optional<T> optional, final ToDoubleFunction<T> mapper) {
		return optional.isPresent() ? OptionalDouble.of(mapper.applyAsDouble(optional.get())) : OptionalDouble.empty();
	}

	/**
	 * If a value is present, applies {@code mapper} to {@code optional} and returns
	 * an {@link OptionalInt} describing the result. Otherwise returns an empty
	 * {@link OptionalInt}.
	 *
	 * @param          <T> element type
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present
	 * @return an {@link OptionalInt} describing the result of applying
	 *         {@code mapper} function to the value of {@code optional}, if a value
	 *         is present, otherwise an empty {@link OptionalInt}
	 */
	public static <T> OptionalInt mapToInt(final Optional<T> optional, final ToIntFunction<T> mapper) {
		return optional.isPresent() ? OptionalInt.of(mapper.applyAsInt(optional.get())) : OptionalInt.empty();
	}

	/**
	 * If a value is present, applies {@code mapper} to {@code optional} and returns
	 * an {@link OptionalLong} describing the result. Otherwise returns an empty
	 * {@link OptionalLong}.
	 *
	 * @param          <T> element type
	 * @param optional optional value
	 * @param mapper   a mapping function to apply to the value, if present
	 * @return an {@link OptionalLong} describing the result of applying
	 *         {@code mapper} function to the value of {@code optional}, if a value
	 *         is present, otherwise an empty {@link OptionalLong}
	 */
	public static <T> OptionalLong mapToLong(final Optional<T> optional, final ToLongFunction<T> mapper) {
		return optional.isPresent() ? OptionalLong.of(mapper.applyAsLong(optional.get())) : OptionalLong.empty();
	}

	/**
	 * Returns an {@link Optional} describing the specified value, if non-null and
	 * non-empty, otherwise returns an empty {@link Optional}. The term <i>empty</i>
	 * is described by {@code isEmpty}.
	 *
	 * @param         <T> value type
	 * @param isEmpty predicate describing the term <i>empty</i>
	 * @param value   the possibly-null-or-empty value to describe
	 * @return an {@link Optional} with a present value if the specified value is
	 *         non-null and non-empty, otherwise an empty {@link Optional}
	 */
	public static <T> Optional<T> ofNon(final Predicate<T> isEmpty, @Nullable final T value) {
		return value == null || isEmpty.test(value) ? Optional.empty() : Optional.of(value);
	}

	/**
	 * Returns an {@link Optional} describing the specified value, if non-null and
	 * non-empty after being trimmed, otherwise returns an empty {@link Optional}.
	 *
	 * @param value the possibly-null-or-empty value to describe
	 * @return an {@link Optional} with a present value if the specified value is
	 *         non-null and non-empty after being trimmed, otherwise an empty
	 *         {@link Optional}
	 */
	public static Optional<String> ofNonBlank(@Nullable final String value) {
		return ofNon(Strings::isBlank, value);
	}

	/**
	 * Returns an {@link Optional} describing the specified value, if non-null and
	 * non-empty, otherwise returns an empty {@link Optional}.
	 *
	 * @param value the possibly-null-or-empty value to describe
	 * @return an {@link Optional} with a present value if the specified value is
	 *         non-null and non-empty, otherwise an empty {@link Optional}
	 */
	public static Optional<String> ofNonEmpty(@Nullable final String value) {
		return ofNon(String::isEmpty, value);
	}

	/**
	 * Returns a stream consisting of present elements.
	 *
	 * @param           <T> element type
	 * @param optionals array of optional elements
	 * @return the new stream
	 */
	@SafeVarargs
	public static <T> Stream<T> stream(final Optional<T>... optionals) {
		return Arrays.stream(optionals).filter(Optional::isPresent).map(Optional::get);
	}
}
