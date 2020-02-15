package de.larssh.utils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A container object which contains a value matching either one or another type
 * represented by {@code <A>} and {@code <B>}.
 *
 * <p>
 * The value can be accessed in a type-safe way using {@link #getFirst()} and
 * {@link #getSecond()}.
 *
 * <p>
 * Additional methods to process the value based on its type and resulting in a
 * fixed typed instance are provided, such as {@link #map(Function, Function)}.
 *
 * <p>
 * This is a <a href="../lang/doc-files/ValueBased.html">value-based</a> class;
 * use of identity-sensitive operations (including reference equality
 * ({@code ==}), identity hash code, or synchronization) on instances of
 * {@code Optional} may have unpredictable results and should be avoided.
 *
 * @param <A> first type of value
 * @param <B> second type of value
 */
@Getter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
public class Either<A, B> {
	/**
	 * Returns an {@link Either} object describing the given value. One of both
	 * parameters has to be non null (means being set), the other one has to be null
	 * (means being not set).
	 *
	 * @deprecated This is a convenience method for special cases only. Use
	 *             {@link #ofFirst} and {@link #ofSecond} where possible.
	 *
	 * @param <A>    type of first value
	 * @param <B>    type of second value
	 * @param first  value of first type or {@code null}
	 * @param second value of second type or {@code null}
	 * @return an {@link Either} object with the given value
	 * @throws NullPointerException     if both parameters are {@code null}
	 * @throws IllegalArgumentException if both parameters are non-{@code null}
	 */
	@Deprecated
	@SuppressWarnings({ "PMD.AvoidThrowingNullPointerException", "PMD.ShortMethodName" })
	public static <A, B> Either<A, B> of(@Nullable final A first, @Nullable final B second) {
		if (first == null && second == null) {
			throw new NullPointerException();
		}
		if (first != null && second != null) {
			throw new IllegalArgumentException("Two values given while expecting exactly one value.");
		}
		return new Either<>(Optional.ofNullable(first), Optional.ofNullable(second));
	}

	/**
	 * Returns an {@link Either} object describing the given value of the
	 * <b>first</b> type.
	 *
	 * @param <A>   first type of value
	 * @param <B>   second type of value
	 * @param value value to describe
	 * @return an {@link Either} object describing the given value
	 */
	public static <A, B> Either<A, B> ofFirst(final A value) {
		return new Either<>(Optional.of(value), Optional.empty());
	}

	/**
	 * Returns an {@link Either} object describing the given value of the
	 * <b>second</b> type.
	 *
	 * @param <A>   first type of value
	 * @param <B>   second type of value
	 * @param value value to describe
	 * @return an {@link Either} object describing the given value
	 */
	public static <A, B> Either<A, B> ofSecond(final B value) {
		return new Either<>(Optional.empty(), Optional.of(value));
	}

	/**
	 * Value of first type
	 *
	 * @return value of first type
	 */
	Optional<A> first;

	/**
	 * Value of second type
	 *
	 * @return value of second type
	 */
	Optional<B> second;

	/**
	 * Invoke either consumer depending on which value is present..
	 *
	 * @param firstConsumer  block to be executed if the first value is present
	 * @param secondConsumer block to be executed if the second value is present
	 */
	public void ifPresent(final Consumer<? super A> firstConsumer, final Consumer<? super B> secondConsumer) {
		ifFirstIsPresent(firstConsumer);
		ifSecondIsPresent(secondConsumer);
	}

	/**
	 * If the first value is present, invoke the specified consumer with the value,
	 * otherwise do nothing.
	 *
	 * @param consumer block to be executed if the first value is present
	 */
	public void ifFirstIsPresent(final Consumer<? super A> consumer) {
		first.ifPresent(consumer);
	}

	/**
	 * If the second value is present, invoke the specified consumer with the value,
	 * otherwise do nothing.
	 *
	 * @param consumer block to be executed if the second value is present
	 */
	public void ifSecondIsPresent(final Consumer<? super B> consumer) {
		second.ifPresent(consumer);
	}

	/**
	 * If value is of first type {@code functionA} is applied, else value is of
	 * second type and {@code functionB} is applied. The resulting value is
	 * returned.
	 *
	 * <p>
	 * <b>Example:</b> The first type is somehow used for user input, while the
	 * second one is used for system default values.
	 *
	 * <pre>
	 * Either&lt;String, Number&gt; either = Either.ofFirst("123");
	 *
	 * String text = either.map(s -&gt; "Changed", n -&gt; "System default");
	 * long value = either.map(Long::parseLong, Number::longValue);
	 * </pre>
	 *
	 * @param <T>            resulting type
	 * @param firstFunction  function to map value, if it is of first type
	 * @param secondFunction function to map value, if it is of second type
	 * @return result after mapping the value using either {@code functionA} or
	 *         {@code functionB}
	 */
	@Nullable
	public <T> T map(final Function<? super A, ? extends T> firstFunction,
			final Function<? super B, ? extends T> secondFunction) {
		if (first.isPresent()) {
			return first.map(firstFunction).orElseThrow(IllegalStateException::new);
		}
		return second.map(secondFunction).orElseThrow(IllegalStateException::new);
	}

	/**
	 * If the value is of second type {@code function} is applied and the resulting
	 * value is returned else the value is of first type and returned as-is.
	 *
	 * <p>
	 * <b>Example:</b> The first type is somehow used for user input, while the
	 * second one is used for system default values.
	 *
	 * <pre>
	 * Either&lt;String, Number&gt; either = Either.ofFirst("123");
	 *
	 * String text = either.map(s -&gt; "Changed", n -&gt; "System default");
	 * Number value = either.mapFirst(Long::parseLong);
	 * </pre>
	 *
	 * @param function function to map value, if it is of first type
	 * @return value if it is of second type, else result after mapping it using
	 *         {@code function}
	 */
	@Nullable
	public B mapFirst(final Function<? super A, ? extends B> function) {
		return second.orElseGet(() -> first.map(function).orElseThrow(IllegalStateException::new));
	}

	/**
	 * If the value is of first type {@code function} is applied and the resulting
	 * value is returned else the value is of second type and returned as-is.
	 *
	 * <p>
	 * <b>Example:</b> The first type is somehow used for user input, while the
	 * second one is used for system default values.
	 *
	 * <pre>
	 * Either&lt;String, Number&gt; either = Either.ofFirst(123);
	 *
	 * String text = either.map(s -&gt; "Changed", n -&gt; "System default");
	 * String displayValue = either.mapSecond(Number::toString);
	 * </pre>
	 *
	 * @param function function to map value, if it is of second type
	 * @return value if it is of first type, else result after mapping it using
	 *         {@code function}
	 */
	@Nullable
	public A mapSecond(final Function<? super B, ? extends A> function) {
		return first.orElseGet(() -> second.map(function).orElseThrow(IllegalStateException::new));
	}
}
