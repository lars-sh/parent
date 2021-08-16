package de.larssh.utils.function;

import static de.larssh.utils.function.ThrowingFunction.throwing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import lombok.NoArgsConstructor;

/**
 * Tests for {@link ThrowingFunction}
 */
@NoArgsConstructor
public class ThrowingFunctionTest {
	private static final ThrowingFunction<String, String> RETURN_IDENTITY = value -> value;

	private static final Function<String, ?> THROW_EXCEPTION = throwing(value -> {
		throw new TestException();
	});

	/**
	 * Test {@link ThrowingFunction#throwing(ThrowingFunction)}: Creating a
	 * {@code ThrowingFunction} based on an existing one
	 */
	@Test
	public void testThrowing_createByThrowingFunction() {
		assertThat(throwing(RETURN_IDENTITY)).isEqualTo(RETURN_IDENTITY);
	}

	/**
	 * Test {@link ThrowingFunction#throwing(ThrowingFunction)}: Creating a
	 * {@code ThrowingFunction} using the functional interface
	 */
	@Test
	public void testThrowing_createByFunctionalInterface() {
		assertThat(throwing((ThrowingFunction<?, ?>) THROW_EXCEPTION)).isEqualTo(THROW_EXCEPTION);
	}

	/**
	 * Test {@link ThrowingFunction#apply(Object)}: Supplying null value and
	 * expecting it to be returned
	 */
	@Test
	public void testApply_returnNull() {
		assertThat(RETURN_IDENTITY.apply(null)).isNull();
	}

	/**
	 * Test {@link ThrowingFunction#apply(Object)}: Supplying a value and expecting
	 * it to be returned
	 */
	@Test
	public void testApply_returnValue() {
		assertThat(RETURN_IDENTITY.apply("A")).isEqualTo("A");
	}

	/**
	 * Test {@link ThrowingFunction#apply(Object)}: Supplying any value, and
	 * expecting the correct exception to be thrown
	 */
	@Test
	public void testApply_throwException() {
		assertThatExceptionOfType(TestException.class).isThrownBy(() -> THROW_EXCEPTION.apply("A"));
	}

	/**
	 * Test {@link ThrowingFunction#applyThrowing(Object)}: Supplying null value and
	 * expecting it to be returned
	 *
	 * @throws Exception any exception
	 */
	@Test
	public void testApplyThrowing_returnNull() throws Exception {
		assertThat(RETURN_IDENTITY.applyThrowing(null)).isNull();
	}

	/**
	 * Test {@link ThrowingFunction#applyThrowing(Object)}: Supplying a value and
	 * expecting it to be returned
	 *
	 * @throws Exception any exception
	 */
	@Test
	public void testApplyThrowing_returnValue() throws Exception {
		assertThat(RETURN_IDENTITY.applyThrowing("A")).isEqualTo("A");
	}

	/**
	 * Test {@link ThrowingFunction#applyThrowing(Object)}: Supplying any value, and
	 * expecting the correct exception to be thrown
	 */
	@Test
	public void testApplyThrowing_throwException() {
		final ThrowingFunction<String, ?> throwingFunction = (ThrowingFunction<String, ?>) THROW_EXCEPTION;
		assertThatExceptionOfType(TestException.class).isThrownBy(() -> throwingFunction.applyThrowing("A"));
	}

	/**
	 * Test exception
	 */
	@NoArgsConstructor
	private static class TestException extends Exception {
		/* empty exception */
	}
}
