package de.larssh.utils;

import static de.larssh.utils.test.Assertions.assertEqualsAndHashCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
		assertThrows(IllegalArgumentException.class, () -> Either.of('A', 2));
		assertThrows(NullPointerException.class, () -> Either.of(null, null));
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
		assertEquals("Either(first=Optional[A], second=Optional.empty)", A.toString());
		assertEquals("Either(first=Optional.empty, second=Optional[2])", B.toString());
	}

	/**
	 * {@link Either#getFirst()}
	 */
	@Test
	public void testGetFirst() {
		assertEquals(Optional.of('A'), A.getFirst());
		assertEquals(Optional.empty(), B.getFirst());
	}

	/**
	 * {@link Either#getSecond()}
	 */
	@Test
	public void testGetSecond() {
		assertEquals(Optional.empty(), A.getSecond());
		assertEquals(Optional.of(2), B.getSecond());
	}

	/**
	 * {@link Either#ifPresent(java.util.function.Consumer, java.util.function.Consumer)}
	 */
	@Test
	public void testIfPresent() {
		final AtomicReference<Character> firstValue = new AtomicReference<>(null);
		A.ifPresent(c -> firstValue.set(c), i -> assertFalse(true));
		assertEquals('A', firstValue.get());

		final AtomicReference<Integer> secondValue = new AtomicReference<>(null);
		B.ifPresent(c -> assertFalse(true), i -> secondValue.set(i));
		assertEquals(2, secondValue.get());
	}

	/**
	 * {@link Either#ifFirstIsPresent(java.util.function.Consumer)}
	 */
	@Test
	public void testIfFirstIsPresent() {
		final AtomicReference<Character> value = new AtomicReference<>(null);
		A.ifFirstIsPresent(c -> value.set(c));
		assertEquals('A', value.get());

		B.ifFirstIsPresent(c -> assertFalse(true));
	}

	/**
	 * {@link Either#ifSecondIsPresent(java.util.function.Consumer)}
	 */
	@Test
	public void testIfSecondIsPresent() {
		A.ifSecondIsPresent(i -> assertFalse(true));

		final AtomicReference<Integer> value = new AtomicReference<>(null);
		B.ifSecondIsPresent(i -> value.set(i));
		assertEquals(2, value.get());
	}

	/**
	 * {@link Either#map(Function, Function)}
	 */
	@Test
	public void testMap() {
		final Function<Character, String> firstFunction = c -> Character.toString(c);
		final Function<Integer, String> secondFunction = i -> Integer.toString(i);

		assertEquals("A", A.map(firstFunction, secondFunction));
		assertEquals("2", B.map(firstFunction, secondFunction));
	}

	/**
	 * {@link Either#mapFirst(Function)}
	 */
	@Test
	public void testMapFirst() {
		final Function<Character, Integer> function = Character::getNumericValue;

		assertEquals((Integer) Character.getNumericValue('A'), A.mapFirst(function));
		assertEquals((Integer) 2, B.mapFirst(function));
	}

	/**
	 * {@link Either#mapSecond(Function)}
	 */
	@Test
	public void testMapSecond() {
		final Function<Integer, Character> function = i -> Character.toChars(i)[0];

		assertEquals((Character) 'A', A.mapSecond(function));
		assertEquals((Character) '\u0002', B.mapSecond(function));
	}
}
