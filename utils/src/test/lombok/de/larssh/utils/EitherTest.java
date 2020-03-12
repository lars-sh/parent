package de.larssh.utils;

import static de.larssh.utils.test.Assertions.assertEqualsAndHashCode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import de.larssh.utils.test.AssertEqualsAndHashCodeArguments;
import lombok.NoArgsConstructor;

/**
 * {@link Either}
 */
@NoArgsConstructor
public class EitherTest {
	private static final Either<Character, Integer> A = Either.ofFirst('A');

	private static final Either<Character, Integer> B = Either.ofSecond(2);

	/**
	 * {@link Either#of(Object, Object)}
	 */
	@Test
	@SuppressWarnings("deprecation")
	public void testOf() {
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> Either.of('A', 2));
		assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> Either.of(null, null));
	}

	/**
	 * {@link Either#equals(Object)} and {@link Either#hashCode()}
	 */
	@Test
	public void testEqualsAndHashCode() {
		assertEqualsAndHashCode(a -> Either.ofFirst(a[0]), new AssertEqualsAndHashCodeArguments().add('A', 'B', false));
		assertEqualsAndHashCode(a -> Either.ofSecond(a[0]),
				new AssertEqualsAndHashCodeArguments().add('B', 'C', false));
	}

	/**
	 * {@link Either#toString()}
	 */
	@Test
	public void testToString() {
		assertThat(A.toString()).isEqualTo("Either(first=Optional[A], second=Optional.empty)");
		assertThat(B.toString()).isEqualTo("Either(first=Optional.empty, second=Optional[2])");
	}

	/**
	 * {@link Either#getFirst()}
	 */
	@Test
	public void testGetFirst() {
		assertThat(A.getFirst()).isEqualTo(Optional.of('A'));
		assertThat(B.getFirst()).isEqualTo(Optional.empty());
	}

	/**
	 * {@link Either#getSecond()}
	 */
	@Test
	public void testGetSecond() {
		assertThat(A.getSecond()).isEqualTo(Optional.empty());
		assertThat(B.getSecond()).isEqualTo(Optional.of(2));
	}

	/**
	 * {@link Either#ifPresent(java.util.function.Consumer, java.util.function.Consumer)}
	 */
	@Test
	public void testIfPresent() {
		final AtomicReference<Character> firstValue = new AtomicReference<>(null);
		A.ifPresent(c -> firstValue.set(c), i -> assertTrue(false));
		assertThat(firstValue.get()).isEqualTo('A');

		final AtomicReference<Integer> secondValue = new AtomicReference<>(null);
		B.ifPresent(c -> assertTrue(false), i -> secondValue.set(i));
		assertThat(secondValue.get()).isEqualTo(2);
	}

	/**
	 * {@link Either#ifFirstIsPresent(java.util.function.Consumer)}
	 */
	@Test
	public void testIfFirstIsPresent() {
		final AtomicReference<Character> value = new AtomicReference<>(null);
		A.ifFirstIsPresent(c -> value.set(c));
		assertThat(value.get()).isEqualTo('A');

		B.ifFirstIsPresent(c -> assertTrue(false));
	}

	/**
	 * {@link Either#ifSecondIsPresent(java.util.function.Consumer)}
	 */
	@Test
	public void testIfSecondIsPresent() {
		A.ifSecondIsPresent(i -> assertTrue(false));

		final AtomicReference<Integer> value = new AtomicReference<>(null);
		B.ifSecondIsPresent(i -> value.set(i));
		assertThat(value.get()).isEqualTo(2);
	}

	/**
	 * {@link Either#map(Function, Function)}
	 */
	@Test
	public void testMap() {
		final Function<Character, String> firstFunction = c -> Character.toString(c);
		final Function<Integer, String> secondFunction = i -> Integer.toString(i);

		assertThat(A.map(firstFunction, secondFunction)).isEqualTo("A");
		assertThat(B.map(firstFunction, secondFunction)).isEqualTo("2");
	}

	/**
	 * {@link Either#mapFirst(Function)}
	 */
	@Test
	public void testMapFirst() {
		final Function<Character, Integer> function = Character::getNumericValue;

		assertThat(A.mapFirst(function)).isEqualTo((Integer) Character.getNumericValue('A'));
		assertThat(B.mapFirst(function)).isEqualTo((Integer) 2);
	}

	/**
	 * {@link Either#mapSecond(Function)}
	 */
	@Test
	public void testMapSecond() {
		final Function<Integer, Character> function = i -> Character.toChars(i)[0];

		assertThat(A.mapSecond(function)).isEqualTo((Character) 'A');
		assertThat(B.mapSecond(function)).isEqualTo((Character) '\u0002');
	}
}
