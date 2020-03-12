package de.larssh.utils;

import static de.larssh.utils.function.ThrowingFunction.throwing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

	private static final Function<String, ?> B = throwing(b -> {
		throw new TestException();
	});

	/**
	 * {@link ThrowingFunction#throwing(ThrowingFunction)}
	 */
	@Test
	public void testThrowing() {
		assertThat(throwing(A)).isEqualTo(A);
		assertThat(throwing((ThrowingFunction<?, ?>) B)).isEqualTo(B);
	}

	/**
	 * {@link ThrowingFunction#apply(Object)}
	 */
	@Test
	public void testApply() {
		assertThat(A.apply(null)).isEqualTo(null);
		assertThat(A.apply("A")).isEqualTo("A");
		assertThat(A.apply("B")).isEqualTo("B");

		assertThatExceptionOfType(TestException.class).isThrownBy(() -> B.apply(null));
		assertThatExceptionOfType(TestException.class).isThrownBy(() -> B.apply("B"));
	}

	/**
	 * {@link ThrowingFunction#applyThrowing(Object)}
	 */
	@Test
	public void testApplyThrowing() {
		try {
			A.applyThrowing(null);
			assertThat(A.applyThrowing("A")).isEqualTo("A");
			assertThat(A.applyThrowing("B")).isEqualTo("B");
		} catch (final Exception e) {
			throw new SneakyException(e);
		}

		final ThrowingFunction<String, ?> b = (ThrowingFunction<String, ?>) B;
		assertThatExceptionOfType(TestException.class).isThrownBy(() -> b.applyThrowing(null));
		assertThatExceptionOfType(TestException.class).isThrownBy(() -> b.applyThrowing("B"));
	}

	/**
	 * Test exception
	 */
	@NoArgsConstructor
	private static class TestException extends Exception {
		private static final long serialVersionUID = 1;
	}
}
