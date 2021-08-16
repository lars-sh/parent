package de.larssh.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import lombok.NoArgsConstructor;

/**
 * Tests for {@link Either}
 */
@NoArgsConstructor
public class EitherTest {
	private static final Either<Character, Integer> FIRST_VALUE = Either.ofFirst('A');

	private static final Either<Character, Integer> SECOND_VALUE = Either.ofSecond(2);

	/**
	 * Test {@link Either#of(Object, Object)}: Creating with first value
	 */
	@Test
	@SuppressWarnings("deprecation")
	public void testOf_createWithFirstValue() {
		assertDoesNotThrow(() -> Either.of('A', null));
	}

	/**
	 * Test {@link Either#of(Object, Object)}: Creating with second value
	 */
	@Test
	@SuppressWarnings("deprecation")
	public void testOf_createWithSecondValue() {
		assertDoesNotThrow(() -> Either.of(null, 2));
	}

	/**
	 * Test {@link Either#of(Object, Object)}: Creating with two values, expecting
	 * exception to be thrown
	 */
	@Test
	@SuppressWarnings("deprecation")
	public void testOf_throwIfBoth() {
		assertThatIllegalArgumentException().isThrownBy(() -> Either.of('A', 2));
	}

	/**
	 * Test {@link Either#of(Object, Object)}: Creating with no value, expecting
	 * exception to be thrown
	 */
	@Test
	@SuppressWarnings("deprecation")
	public void testOf_throwIfNone() {
		assertThatNullPointerException().isThrownBy(() -> Either.of(null, null));
	}

	/**
	 * Test {@link Either#getFirst()}: Expecting empty
	 */
	@Test
	public void testGetFirst_returnEmpty() {
		assertThat(SECOND_VALUE.getFirst()).isEmpty();
	}

	/**
	 * Test {@link Either#getFirst()}: Expecting value
	 */
	@Test
	public void testGetFirst_returnValue() {
		assertThat(FIRST_VALUE.getFirst()).isEqualTo(Optional.of('A'));
	}

	/**
	 * Test {@link Either#getSecond()}: Expecting empty
	 */
	@Test
	public void testGetSecond_returnEmpty() {
		assertThat(FIRST_VALUE.getSecond()).isEmpty();
	}

	/**
	 * Test {@link Either#getSecond()}: Expecting value
	 */
	@Test
	public void testGetSecond_returnValue() {
		assertThat(SECOND_VALUE.getSecond()).isEqualTo(Optional.of(2));
	}

	/**
	 * Test
	 * {@link Either#ifPresent(java.util.function.Consumer, java.util.function.Consumer)}:
	 * Expecting first supplier to be called
	 */
	@Test
	public void testIfPresent_ifFirstIsPresent() {
		final AtomicReference<Character> firstValue = new AtomicReference<>(null);
		FIRST_VALUE.ifPresent(firstValue::set, value -> fail());
		assertThat(firstValue.get()).isEqualTo('A');
	}

	/**
	 * Test
	 * {@link Either#ifPresent(java.util.function.Consumer, java.util.function.Consumer)}:
	 * Expecting second supplier to be called
	 */
	@Test
	public void testIfPresent_ifSecondIsPresent() {
		final AtomicReference<Integer> secondValue = new AtomicReference<>(null);
		SECOND_VALUE.ifPresent(value -> fail(), secondValue::set);
		assertThat(secondValue.get()).isEqualTo(2);
	}

	/**
	 * Test {@link Either#ifFirstIsPresent(java.util.function.Consumer)}: Expecting
	 * not to be called
	 */
	@Test
	public void testIfFirstIsPresent_fsNotPresent() {
		SECOND_VALUE.ifFirstIsPresent(value -> fail());
	}

	/**
	 * Test {@link Either#ifFirstIsPresent(java.util.function.Consumer)}: Expecting
	 * to be called
	 */
	@Test
	public void testIfFirstIsPresent_fsPresent() {
		final AtomicReference<Character> value = new AtomicReference<>(null);
		FIRST_VALUE.ifFirstIsPresent(value::set);
		assertThat(value.get()).isEqualTo('A');
	}

	/**
	 * Test {@link Either#ifSecondIsPresent(java.util.function.Consumer)}: Expecting
	 * not to be called
	 */
	@Test
	public void testIfSecondIsPresent_ifNotPresent() {
		FIRST_VALUE.ifSecondIsPresent(value -> fail());
	}

	/**
	 * Test {@link Either#ifSecondIsPresent(java.util.function.Consumer)}: Expecting
	 * to be called
	 */
	@Test
	public void testIfSecondIsPresent_ifPresent() {
		final AtomicReference<Integer> value = new AtomicReference<>(null);
		SECOND_VALUE.ifSecondIsPresent(value::set);
		assertThat(value.get()).isEqualTo(2);
	}

	/**
	 * Test {@link Either#map(Function, Function)}: Expecting first to be mapped
	 */
	@Test
	public void testMap_ifFirstIsPresent() {
		final Function<Character, String> firstFunction = value -> Character.toString(value);
		final Function<Integer, String> secondFunction = value -> Integer.toString(value);
		assertThat(FIRST_VALUE.map(firstFunction, secondFunction)).isEqualTo("A");
	}

	/**
	 * Test {@link Either#map(Function, Function)}: Expecting second to be mapped
	 */
	@Test
	public void testMap_ifSecondIsPresent() {
		final Function<Character, String> firstFunction = value -> Character.toString(value);
		final Function<Integer, String> secondFunction = value -> Integer.toString(value);
		assertThat(SECOND_VALUE.map(firstFunction, secondFunction)).isEqualTo("2");
	}

	/**
	 * Test {@link Either#mapFirst(Function)}: Expecting not to be mapped
	 */
	@Test
	public void testMapFirst_ifNotPresent() {
		assertThat(SECOND_VALUE.mapFirst(Character::getNumericValue)).isEqualTo(2);
	}

	/**
	 * Test {@link Either#mapFirst(Function)}: Expecting to be mapped
	 */
	@Test
	public void testMapFirst_ifPresent() {
		assertThat(FIRST_VALUE.mapFirst(Character::getNumericValue)).isEqualTo(Character.getNumericValue('A'));
	}

	/**
	 * Test {@link Either#mapSecond(Function)}: Expecting not to be mapped
	 */
	@Test
	public void testMapSecond_ifNotPresent() {
		final Function<Integer, Character> function = value -> Character.toChars(value)[0];
		assertThat(SECOND_VALUE.mapSecond(function)).isEqualTo('\u0002');
	}

	/**
	 * Test {@link Either#mapSecond(Function)}: Expecting to be mapped
	 */
	@Test
	public void testMapSecond_ifPresent() {
		final Function<Integer, Character> function = value -> Character.toChars(value)[0];
		assertThat(FIRST_VALUE.mapSecond(function)).isEqualTo('A');
	}
}
