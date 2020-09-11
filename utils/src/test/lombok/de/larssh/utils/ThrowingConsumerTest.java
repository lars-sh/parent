package de.larssh.utils;

import static de.larssh.utils.function.ThrowingConsumer.throwing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import de.larssh.utils.function.ThrowingConsumer;
import lombok.NoArgsConstructor;

/**
 * {@link ThrowingConsumer}
 */
@NoArgsConstructor
public class ThrowingConsumerTest {
	private static final ThrowingConsumer<String> A = a -> assertThat(a).isEqualTo("A");

	private static final Consumer<String> B = throwing(b -> {
		throw new TestException();
	});

	/**
	 * {@link ThrowingConsumer#throwing(ThrowingConsumer)}
	 */
	@Test
	public void testThrowing() {
		assertThat(throwing(A)).isEqualTo(A);
		assertThat(throwing((ThrowingConsumer<?>) B)).isEqualTo(B);
	}

	/**
	 * {@link ThrowingConsumer#accept(Object)}
	 */
	@Test
	public void testAccept() {
		A.accept("A");
		assertThatExceptionOfType(TestException.class).isThrownBy(() -> B.accept(null));
		assertThatExceptionOfType(TestException.class).isThrownBy(() -> B.accept("B"));
	}

	/**
	 * {@link ThrowingConsumer#acceptThrowing(Object)}
	 */
	@Test
	@SuppressWarnings("checkstyle:XIllegalCatchDefault")
	public void testAcceptThrowing() {
		try {
			A.acceptThrowing("A");
		} catch (final Exception e) {
			throw new SneakyException(e);
		}

		final ThrowingConsumer<String> b = (ThrowingConsumer<String>) B;
		assertThatExceptionOfType(TestException.class).isThrownBy(() -> b.acceptThrowing(null));
		assertThatExceptionOfType(TestException.class).isThrownBy(() -> b.acceptThrowing("B"));
	}

	/**
	 * Test exception
	 */
	@NoArgsConstructor
	private static class TestException extends Exception {
		/* empty exception */
	}
}
