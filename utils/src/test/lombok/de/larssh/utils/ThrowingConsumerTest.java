package de.larssh.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import de.larssh.utils.function.ThrowingConsumer;
import lombok.NoArgsConstructor;

/**
 * {@link ThrowingConsumer}
 */
@NoArgsConstructor
public class ThrowingConsumerTest {
	private static final ThrowingConsumer<String> A = a -> assertEquals("A", a);

	private static final Consumer<String> B = ThrowingConsumer.throwing(b -> {
		throw new TestException();
	});

	/**
	 * {@link ThrowingConsumer#throwing(ThrowingConsumer)}
	 */
	@Test
	public void testThrowing() {
		assertEquals(A, ThrowingConsumer.throwing(A));
		assertEquals(B, ThrowingConsumer.throwing((ThrowingConsumer<?>) B));
	}

	/**
	 * {@link ThrowingConsumer#accept(Object)}
	 */
	@Test
	public void testAccept() {
		assertDoesNotThrow(() -> A.accept("A"));
		assertThrows(TestException.class, () -> B.accept(null));
		assertThrows(TestException.class, () -> B.accept("B"));
	}

	/**
	 * {@link ThrowingConsumer#acceptThrowing(Object)}
	 */
	@Test
	public void testAcceptThrowing() {
		assertDoesNotThrow(() -> A.acceptThrowing("A"));

		final ThrowingConsumer<String> b = (ThrowingConsumer<String>) B;
		assertThrows(TestException.class, () -> b.acceptThrowing(null));
		assertThrows(TestException.class, () -> b.acceptThrowing("B"));
	}

	/**
	 * Test exception
	 */
	@NoArgsConstructor
	private static class TestException extends Exception {
		private static final long serialVersionUID = 1;
	}
}
