package de.larssh.utils.function;

import static de.larssh.utils.function.ThrowingConsumer.throwing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import lombok.NoArgsConstructor;

/**
 * Tests for {@link ThrowingConsumer}
 */
@NoArgsConstructor
public class ThrowingConsumerTest {
	private static final ThrowingConsumer<String> CONSUME_VALUE = value -> {
		// no need for an implementation for these tests
	};

	private static final Consumer<String> THROW_EXCEPTION = throwing(value -> {
		throw new TestException();
	});

	/**
	 * Test {@link ThrowingConsumer#throwing(ThrowingConsumer)}: Creating a
	 * {@code ThrowingConsumer} based on an existing one
	 */
	@Test
	public void testThrowing_createByThrowingConsumer() {
		assertThat(throwing(CONSUME_VALUE)).isEqualTo(CONSUME_VALUE);
	}

	/**
	 * Test {@link ThrowingConsumer#throwing(ThrowingConsumer)}: Creating a
	 * {@code ThrowingConsumer} using the functional interface
	 */
	@Test
	public void testThrowing_createByFunctionalInterface() {
		assertThat(throwing((ThrowingConsumer<?>) THROW_EXCEPTION)).isEqualTo(THROW_EXCEPTION);
	}

	/**
	 * Test {@link ThrowingConsumer#accept(Object)}: Supplying null value and
	 * expecting no exception to be thrown
	 */
	@Test
	public void testAccept_consumeNull() {
		assertThatCode(() -> CONSUME_VALUE.accept(null)).doesNotThrowAnyException();
	}

	/**
	 * Test {@link ThrowingConsumer#accept(Object)}: Supplying a value and expecting
	 * no exception to be thrown
	 */
	@Test
	public void testAccept_consumeValue() {
		assertThatCode(() -> CONSUME_VALUE.accept("A")).doesNotThrowAnyException();
	}

	/**
	 * Test {@link ThrowingConsumer#accept(Object)}: Throw the expected exception
	 */
	@Test
	public void testAccept_throwException() {
		assertThatExceptionOfType(TestException.class).isThrownBy(() -> THROW_EXCEPTION.accept("A"));
	}

	/**
	 * Test {@link ThrowingConsumer#acceptThrowing(Object)}: Supplying any value,
	 * and expecting the correct exception to be thrown
	 */
	@Test
	public void testAcceptThrowing_consumeNull() {
		assertThatCode(() -> CONSUME_VALUE.acceptThrowing(null)).doesNotThrowAnyException();
	}

	/**
	 * Test {@link ThrowingConsumer#acceptThrowing(Object)}: Supplying a value and
	 * expecting no exception to be thrown
	 */
	@Test
	public void testAcceptThrowing_consumeValue() {
		assertThatCode(() -> CONSUME_VALUE.acceptThrowing("A")).doesNotThrowAnyException();
	}

	/**
	 * Test {@link ThrowingConsumer#acceptThrowing(Object)}: Supplying any value,
	 * and expecting the correct exception to be thrown
	 */
	@Test
	public void testAcceptThrowing_throwException() {
		final ThrowingConsumer<String> throwingConsumer = (ThrowingConsumer<String>) THROW_EXCEPTION;
		assertThatExceptionOfType(TestException.class).isThrownBy(() -> throwingConsumer.acceptThrowing("A"));
	}

	/**
	 * Test exception
	 */
	@NoArgsConstructor
	private static class TestException extends Exception {
		/* empty exception */
	}
}
