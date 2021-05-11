package de.larssh.utils.function;

import static de.larssh.utils.function.ThrowingSupplier.throwing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import lombok.NoArgsConstructor;

/**
 * Tests for {@link ThrowingSupplier}
 */
@NoArgsConstructor
public class ThrowingSupplierTest {
	private static final ThrowingSupplier<String> SUPPLY_VALUE = () -> "A";

	private static final Supplier<?> SUPPLY_EXCEPTION = throwing(() -> {
		throw new TestException();
	});

	/**
	 * Test {@link ThrowingSupplier#throwing(ThrowingSupplier)}: Creating a
	 * {@code ThrowingSupplier} based on an existing one
	 */
	@Test
	public void testThrowing_createByThrowingSupplier() {
		assertThat(throwing(SUPPLY_VALUE)).isEqualTo(SUPPLY_VALUE);
	}

	/**
	 * Test {@link ThrowingSupplier#throwing(ThrowingSupplier)}: Creating a
	 * {@code ThrowingSupplier} using the functional interface
	 */
	@Test
	public void testThrowing_createByFunctionalInterface() {
		assertThat(throwing((ThrowingSupplier<?>) SUPPLY_EXCEPTION)).isEqualTo(SUPPLY_EXCEPTION);
	}

	/**
	 * Test {@link ThrowingSupplier#get()}: Calling and expecting to return a value
	 */
	@Test
	public void testGet_returnValue() {
		assertThat(SUPPLY_VALUE.get()).isEqualTo("A");
	}

	/**
	 * Test {@link ThrowingSupplier#get()}: Calling and expecting the correct
	 * exception to be thrown
	 */
	@Test
	public void testGet_throwException() {
		assertThatExceptionOfType(TestException.class).isThrownBy(SUPPLY_EXCEPTION::get);
	}

	/**
	 * Test {@link ThrowingSupplier#getThrowing()}: Calling and expecting to return
	 * a value
	 *
	 * @throws Exception any exception
	 */
	@Test
	public void testGetThrowing_returnValue() throws Exception {
		assertThat(SUPPLY_VALUE.getThrowing()).isEqualTo("A");
	}

	/**
	 * Test {@link ThrowingSupplier#getThrowing()}: Calling and expecting the
	 * correct exception to be thrown
	 */
	@Test
	public void testGetThrowing_throwException() {
		assertThatExceptionOfType(TestException.class)
				.isThrownBy(((ThrowingSupplier<?>) SUPPLY_EXCEPTION)::getThrowing);
	}

	/**
	 * Test exception
	 */
	@NoArgsConstructor
	private static class TestException extends Exception {
		/* empty exception */
	}
}
