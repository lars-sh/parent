package de.larssh.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import de.larssh.utils.function.ThrowingFunction;
import lombok.NoArgsConstructor;

/**
 * {@link ThrowingFunction}
 */
@NoArgsConstructor
public class ThrowingFunctionTest {
	private static final ThrowingFunction<String, String> A = a -> a;

	private static final Function<String, ?> B = ThrowingFunction.throwing(b -> {
		throw new TestException();
	});

	/**
	 * {@link ThrowingFunction#throwing(ThrowingFunction)}
	 */
	@Test
	public void testThrowing() {
		assertEquals(A, ThrowingFunction.throwing(A));
		assertEquals(B, ThrowingFunction.throwing((ThrowingFunction<?, ?>) B));
	}

	/**
	 * {@link ThrowingFunction#apply(Object)}
	 */
	@Test
	public void testApply() {
		assertEquals(null, A.apply(null));
		assertEquals("A", A.apply("A"));
		assertEquals("B", A.apply("B"));

		assertThrows(TestException.class, () -> B.apply(null));
		assertThrows(TestException.class, () -> B.apply("B"));
	}

	/**
	 * {@link ThrowingFunction#applyThrowing(Object)}
	 */
	@Test
	public void testApplyThrowing() {
		assertDoesNotThrow(() -> A.applyThrowing(null));
		assertDoesNotThrow(() -> assertEquals("A", A.applyThrowing("A")));
		assertDoesNotThrow(() -> assertEquals("B", A.applyThrowing("B")));

		final ThrowingFunction<String, ?> b = (ThrowingFunction<String, ?>) B;
		assertThrows(TestException.class, () -> b.applyThrowing(null));
		assertThrows(TestException.class, () -> b.applyThrowing("B"));
	}

	/**
	 * Test exception
	 */
	@NoArgsConstructor
	private static class TestException extends Exception {
		private static final long serialVersionUID = 1;
	}
}
