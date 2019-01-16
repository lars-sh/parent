package de.larssh.utils;

import static de.larssh.utils.function.ThrowingSupplier.throwing;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
		assertEquals(A, throwing(A));
		assertEquals(B, throwing((ThrowingSupplier<?>) B));
	}

	/**
	 * {@link ThrowingSupplier#get()}
	 */
	@Test
	public void testGet() {
		assertEquals("A", A.get());
		assertThrows(TestException.class, B::get);
	}

	/**
	 * {@link ThrowingSupplier#getThrowing()}
	 */
	@Test
	public void testGetThrowing() {
		assertDoesNotThrow(() -> assertEquals("A", A.getThrowing()));
		assertThrows(TestException.class, ((ThrowingSupplier<?>) B)::getThrowing);
	}

	/**
	 * Test exception
	 */
	@NoArgsConstructor
	private static class TestException extends Exception {
		private static final long serialVersionUID = 1;
	}
}
