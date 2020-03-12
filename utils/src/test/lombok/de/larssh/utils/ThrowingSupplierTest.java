package de.larssh.utils;

import static de.larssh.utils.function.ThrowingSupplier.throwing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import de.larssh.utils.function.ThrowingSupplier;
import lombok.NoArgsConstructor;

/**
 * {@link ThrowingSupplier}
 */
@NoArgsConstructor
public class ThrowingSupplierTest {
	private static final ThrowingSupplier<String> A = () -> "A";

	private static final Supplier<?> B = throwing(() -> {
		throw new TestException();
	});

	/**
	 * {@link ThrowingSupplier#throwing(ThrowingSupplier)}
	 */
	@Test
	public void testThrowing() {
		assertThat(throwing(A)).isEqualTo(A);
		assertThat(throwing((ThrowingSupplier<?>) B)).isEqualTo(B);
	}

	/**
	 * {@link ThrowingSupplier#get()}
	 */
	@Test
	public void testGet() {
		assertThat(A.get()).isEqualTo("A");
		assertThatExceptionOfType(TestException.class).isThrownBy(B::get);
	}

	/**
	 * {@link ThrowingSupplier#getThrowing()}
	 */
	@Test
	public void testGetThrowing() {
		try {
			assertThat(A.getThrowing()).isEqualTo("A");
		} catch (final Exception e) {
			throw new SneakyException(e);
		}
		assertThatExceptionOfType(TestException.class).isThrownBy(((ThrowingSupplier<?>) B)::getThrowing);
	}

	/**
	 * Test exception
	 */
	@NoArgsConstructor
	private static class TestException extends Exception {
		private static final long serialVersionUID = 1;
	}
}
