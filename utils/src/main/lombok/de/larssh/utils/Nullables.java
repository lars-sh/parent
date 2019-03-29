package de.larssh.utils;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for nullable values. It extends the
 * functionality offered by {@link Objects}.
 */
@UtilityClass
public class Nullables {
	/**
	 * Returns the first non-null value. Returns null if no value is non-null.
	 *
	 * @param <T>    value type
	 * @param values any number of nullable values to test
	 * @return the first non-null value, null if no value is non-null
	 */
	@Nullable
	@SafeVarargs
	public static <T> T getFirst(final T... values) {
		for (final T value : values) {
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	/**
	 * If {@code value} is non-null, invoke the specified consumer with
	 * {@code value}, otherwise do nothing.
	 *
	 * @param <T>      value type
	 * @param value    nullable value
	 * @param consumer executed if {@code value} is null
	 */
	public static <T> void ifNonNull(@Nullable final T value, final Consumer<? super T> consumer) {
		if (value != null) {
			consumer.accept(value);
		}
	}

	/**
	 * If {@code value} is non-null, performs the given action with {@code value},
	 * otherwise performs the given empty-based action.
	 *
	 * @param <T>         value type
	 * @param value       nullable value
	 * @param action      the action to be performed, if {@code value} is non-null
	 * @param emptyAction the empty-based action to be performed, if {@code value}
	 *                    is null
	 */
	public static <T> void ifNonNullOrElse(@Nullable final T value,
			final Consumer<? super T> action,
			final Runnable emptyAction) {
		if (value == null) {
			emptyAction.run();
		} else {
			action.accept(value);
		}
	}

	/**
	 * If {@code value} is non-null, apply the provided mapping function to it, and
	 * return the nullable result. Otherwise return {@code null}.
	 *
	 * @param <T>    value type
	 * @param <U>    the mapping functions result type
	 * @param value  nullable value
	 * @param mapper a mapping function to apply to {@code value}, if non-null
	 * @return a nullable describing the result of applying a mapping function to
	 *         {@code value}, if {@code value} is non-null, otherwise {@code null}
	 */
	@Nullable
	public static <T, U> U map(@Nullable final T value, final Function<? super T, ? extends U> mapper) {
		return value == null ? null : mapper.apply(value);
	}

	/**
	 * If {@code value} is non-null, returns {@code value}, otherwise returns a
	 * nullable value produced by the supplying function.
	 *
	 * @param <T>      value type
	 * @param value    nullable value
	 * @param supplier the supplying function that produces a nullable value to be
	 *                 returned
	 * @return returns {@code value}, if {@code value} is non-null, otherwise a
	 *         nullable value produced by the supplying function.
	 */
	@Nullable
	public static <T> T or(@Nullable final T value, final Supplier<? extends T> supplier) {
		return value == null ? supplier.get() : value;
	}

	/**
	 * Return {@code value} if non-null, otherwise return {@code other}.
	 *
	 * @param <T>   value type
	 * @param value nullable value
	 * @param other the value to be returned if {@code value} is null
	 * @return {@code value}, if non-null, otherwise {@code other}
	 */
	public static <T> T orElse(@Nullable final T value, final T other) {
		return value == null ? other : value;
	}

	/**
	 * Return {@code value} if non-null, otherwise invoke {@code other} and return
	 * the result of that invocation.
	 *
	 * @param <T>   value type
	 * @param value nullable value
	 * @param other a {@code Supplier} whose result is returned if {@code value} is
	 *              null
	 * @return {@code value} if non-null otherwise the result of {@code other.get()}
	 */
	public static <T> T orElseGet(@Nullable final T value, final Supplier<? extends T> other) {
		return value != null ? value : other.get();
	}

	/**
	 * If {@code value} is non-null, returns {@code value}, otherwise throws
	 * {@code java.lang.NullPointerException}.
	 *
	 * @param <T>   value type
	 * @param value nullable value
	 * @return the non-null value held by {@code value}
	 * @throws java.lang.NullPointerException if {@code value} is null
	 */
	@SuppressFBWarnings(value = "NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE",
			justification = "nullable vs. non-null by design")
	public static <T> T orElseThrow(@Nullable final T value) {
		return Objects.requireNonNull(value);
	}

	/**
	 * Return {@code value}, if non-null, otherwise throw an exception to be created
	 * by the provided supplier.
	 *
	 * @param <T>               value type
	 * @param <X>               Type of the exception to be thrown
	 * @param value             nullable value
	 * @param exceptionSupplier The supplier which will return the exception to be
	 *                          thrown
	 * @return the non-null value
	 * @throws X if {@code value} is null
	 */
	public static <T, X extends Throwable> T orElseThrow(@Nullable final T value,
			final Supplier<? extends X> exceptionSupplier) throws X {
		if (value == null) {
			throw exceptionSupplier.get();
		}
		return value;
	}
}
