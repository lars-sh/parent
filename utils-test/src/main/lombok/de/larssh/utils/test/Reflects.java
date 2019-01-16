package de.larssh.utils.test;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import org.joor.Reflect;
import org.joor.ReflectException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link Reflect}.
 */
@UtilityClass
public class Reflects {
	/**
	 * Calls {@link Reflect#create(Object...)} to call a constructor.
	 *
	 * <p>
	 * This is roughly equivalent to {@link Reflect#create(Object...)} except that
	 * any exception thrown within the called constructor is not encapsulated into
	 * {@link ReflectException} and {@link InvocationTargetException}.
	 *
	 * @param reflect   the reflection object
	 * @param arguments the constructor arguments
	 * @return the wrapped new object to be used for further reflection
	 * @throws ReflectException any reflection exception
	 */
	public static Reflect create(final Reflect reflect, final Object... arguments) {
		return extractInvocationTargetExceptions(() -> reflect.create(arguments));
	}

	/**
	 * Calls {@link Reflect#call(String, Object...)} to call a method by its name.
	 *
	 * <p>
	 * This is roughly equivalent to {@link Reflect#call(String, Object...)} except
	 * that any exception thrown within the called method is not encapsulated into
	 * {@link ReflectException} and {@link InvocationTargetException}.
	 *
	 * @param reflect   the reflection object
	 * @param name      the method name
	 * @param arguments the method arguments
	 * @return the wrapped method result or the same wrapped object if the method
	 *         returns <code>void</code> to be used for further reflection
	 * @throws ReflectException any reflection exception
	 */
	public static Reflect call(final Reflect reflect, final String name, final Object... arguments) {
		return extractInvocationTargetExceptions(() -> reflect.call(name, arguments));
	}

	/**
	 * Calls {@code supplier} and forwards its return value. Makes sure any thrown
	 * within the supplier is not encapsulated exception inside a
	 * {@link InvocationTargetException} or {@link ReflectException}.
	 *
	 * @param          <T> type of the return value
	 * @param supplier functionality to execute
	 * @return any value returned by {@code supplier}
	 * @throws ReflectException any reflection exception
	 * @throws SneakyException  invocation target exceptions
	 */
	@SuppressWarnings({
			"PMD.AvoidCatchingGenericException",
			"PMD.AvoidInstanceofChecksInCatchClause",
			"PMD.PreserveStackTrace" })
	@SuppressFBWarnings(value = { "BC_IMPOSSIBLE_INSTANCEOF", "EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS" },
			justification = "supplier might sneaky throw InvocationTargetException")
	private static <T> T extractInvocationTargetExceptions(final Supplier<T> supplier) {
		try {
			return supplier.get();
		} catch (final ReflectException e) {
			if (e.getCause() instanceof InvocationTargetException) {
				throw new SneakyException(e.getCause().getCause());
			}
			throw e;
		} catch (final Exception e) {
			if (e instanceof InvocationTargetException) {
				throw new SneakyException(e.getCause());
			}
			throw e;
		}
	}
}
