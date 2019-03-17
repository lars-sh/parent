package de.larssh.utils;

import static java.util.stream.Collectors.toCollection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import de.larssh.utils.text.Strings;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link Collector}.
 */
@UtilityClass
public class Collectors {
	/**
	 * Returns a merge function, suitable for use in
	 * {@link java.util.Map#merge(Object, Object, java.util.function.BiFunction)
	 * Map.merge()} or
	 * {@link java.util.stream.Collectors#toMap(Function, Function, BinaryOperator)},
	 * which always throws {@link IllegalStateException}. This can be used to
	 * enforce the assumption that the elements being collected are distinct.
	 *
	 * @param <T> the type of input arguments to the merge function
	 * @return a merge function which always throw {@link IllegalStateException}
	 */
	public static <T> BinaryOperator<T> throwingMerger() {
		return (u, v) -> {
			throw new IllegalStateException(Strings.format("Duplicate key %s", u));
		};
	}

	/**
	 * Returns a {@link Collector} that accumulates the input elements into a new
	 * {@link LinkedHashSet}, in encounter order.
	 *
	 * @param <T> the type of the input elements
	 * @return a {@link Collector} which collects all the input elements into a
	 *         {@link LinkedHashSet}
	 */
	public static <T> Collector<T, ?, LinkedHashSet<T>> toLinkedHashSet() {
		return toCollection(LinkedHashSet::new);
	}

	/**
	 * Returns a {@code Collector} that accumulates elements into a
	 * {@code LinkedHashMap} whose keys and values are the result of applying the
	 * provided mapping functions to the input elements.
	 *
	 * @param             <T> the type of the input elements
	 * @param             <K> the output type of the key mapping function
	 * @param             <U> the output type of the value mapping function
	 * @param keyMapper   a mapping function to produce keys
	 * @param valueMapper a mapping function to produce values
	 * @return a {@code Collector} which collects elements into a
	 *         {@code LinkedHashMap} whose keys and values are the result of
	 *         applying mapping functions to the input elements
	 */
	public static <T, K, U> Collector<T, ?, LinkedHashMap<K, U>> toLinkedHashMap(
			final Function<? super T, ? extends K> keyMapper,
			final Function<? super T, ? extends U> valueMapper) {
		return toLinkedHashMap(keyMapper, valueMapper, throwingMerger());
	}

	/**
	 * Returns a {@code Collector} that accumulates elements into a
	 * {@code LinkedHashMap} whose keys and values are the result of applying the
	 * provided mapping functions to the input elements.
	 *
	 * @param               <T> the type of the input elements
	 * @param               <K> the output type of the key mapping function
	 * @param               <U> the output type of the value mapping function
	 * @param keyMapper     a mapping function to produce keys
	 * @param valueMapper   a mapping function to produce values
	 * @param mergeFunction a merge function, used to resolve collisions between
	 *                      values associated with the same key, as supplied to
	 *                      {@link java.util.Map#merge(Object, Object, java.util.function.BiFunction)}
	 * @return a {@code Collector} which collects elements into a
	 *         {@code LinkedHashMap} whose keys are the result of applying a key
	 *         mapping function to the input elements, and whose values are the
	 *         result of applying a value mapping function to all input elements
	 *         equal to the key and combining them using the merge function
	 */
	public static <T, K, U> Collector<T, ?, LinkedHashMap<K, U>> toLinkedHashMap(
			final Function<? super T, ? extends K> keyMapper,
			final Function<? super T, ? extends U> valueMapper,
			final BinaryOperator<U> mergeFunction) {
		return toMap(keyMapper, valueMapper, mergeFunction, LinkedHashMap::new);
	}

	/**
	 * Returns a {@code Collector} that accumulates elements into a {@code Map}
	 * whose keys and values are the result of applying the provided mapping
	 * functions to the input elements.
	 *
	 * <p>
	 * If the mapped keys contains duplicates (according to
	 * {@link Object#equals(Object)}), an {@code IllegalStateException} is thrown
	 * when the collection operation is performed. If the mapped keys may have
	 * duplicates, use {@link #toMap(Function, Function, BinaryOperator)} instead.
	 *
	 * @apiNote It is common for either the key or the value to be the input
	 *          elements. In this case, the utility method
	 *          {@link java.util.function.Function#identity()} may be helpful. For
	 *          example, the following produces a {@code Map} mapping students to
	 *          their grade point average: <pre>{@code
	 *     Map<Student, Double> studentToGPA
	 *         students.stream().collect(toMap(Functions.identity(),
	 *                                         student -> computeGPA(student)));
	 * }</pre> And the following produces a {@code Map} mapping a unique identifier
	 *          to students: <pre>{@code
	 *     Map<String, Student> studentIdToStudent
	 *         students.stream().collect(toMap(Student::getId,
	 *                                         Functions.identity());
	 * }</pre>
	 *
	 * @implNote The returned {@code Collector} is not concurrent. For parallel
	 *           stream pipelines, the {@code combiner} function operates by merging
	 *           the keys from one map into another, which can be an expensive
	 *           operation. If it is not required that results are inserted into the
	 *           {@code Map} in encounter order, using
	 *           {@link #toConcurrentMap(Function, Function)} may offer better
	 *           parallel performance.
	 *
	 * @param             <T> the type of the input elements
	 * @param             <K> the output type of the key mapping function
	 * @param             <U> the output type of the value mapping function
	 * @param keyMapper   a mapping function to produce keys
	 * @param valueMapper a mapping function to produce values
	 * @return a {@code Collector} which collects elements into a {@code Map} whose
	 *         keys and values are the result of applying mapping functions to the
	 *         input elements
	 */
	public static <T, K, U> Collector<T, ?, Map<K, U>> toMap(final Function<? super T, ? extends K> keyMapper,
			final Function<? super T, ? extends U> valueMapper) {
		return toMap(keyMapper, valueMapper, throwingMerger());
	}

	/**
	 * Returns a {@code Collector} that accumulates elements into a {@code Map}
	 * whose keys and values are the result of applying the provided mapping
	 * functions to the input elements.
	 *
	 * <p>
	 * If the mapped keys contains duplicates (according to
	 * {@link Object#equals(Object)}), the value mapping function is applied to each
	 * equal element, and the results are merged using the provided merging
	 * function.
	 *
	 * @apiNote There are multiple ways to deal with collisions between multiple
	 *          elements mapping to the same key. The other forms of {@code toMap}
	 *          simply use a merge function that throws unconditionally, but you can
	 *          easily write more flexible merge policies. For example, if you have
	 *          a stream of {@code Person}, and you want to produce a "phone book"
	 *          mapping name to address, but it is possible that two persons have
	 *          the same name, you can do as follows to gracefully deals with these
	 *          collisions, and produce a {@code Map} mapping names to a
	 *          concatenated list of addresses: <pre>{@code
	 *     Map<String, String> phoneBook
	 *         people.stream().collect(toMap(Person::getName,
	 *                                       Person::getAddress,
	 *                                       (s, a) -> s + ", " + a));
	 * }</pre>
	 *
	 * @implNote The returned {@code Collector} is not concurrent. For parallel
	 *           stream pipelines, the {@code combiner} function operates by merging
	 *           the keys from one map into another, which can be an expensive
	 *           operation. If it is not required that results are merged into the
	 *           {@code Map} in encounter order, using
	 *           {@link #toConcurrentMap(Function, Function, BinaryOperator)} may
	 *           offer better parallel performance.
	 *
	 * @param               <T> the type of the input elements
	 * @param               <K> the output type of the key mapping function
	 * @param               <U> the output type of the value mapping function
	 * @param keyMapper     a mapping function to produce keys
	 * @param valueMapper   a mapping function to produce values
	 * @param mergeFunction a merge function, used to resolve collisions between
	 *                      values associated with the same key, as supplied to
	 *                      {@link java.util.Map#merge(Object, Object, java.util.function.BiFunction)}
	 * @return a {@code Collector} which collects elements into a {@code Map} whose
	 *         keys are the result of applying a key mapping function to the input
	 *         elements, and whose values are the result of applying a value mapping
	 *         function to all input elements equal to the key and combining them
	 *         using the merge function
	 */
	public static <T, K, U> Collector<T, ?, Map<K, U>> toMap(final Function<? super T, ? extends K> keyMapper,
			final Function<? super T, ? extends U> valueMapper,
			final BinaryOperator<U> mergeFunction) {
		return toMap(keyMapper, valueMapper, mergeFunction, HashMap::new);
	}

	/**
	 * Returns a {@code Collector} that accumulates elements into a {@code Map}
	 * whose keys and values are the result of applying the provided mapping
	 * functions to the input elements.
	 *
	 * <p>
	 * If the mapped keys contains duplicates (according to
	 * {@link Object#equals(Object)}), an {@code IllegalStateException} is thrown
	 * when the collection operation is performed. If the mapped keys may have
	 * duplicates, use {@link #toMap(Function, Function, BinaryOperator)} instead.
	 *
	 * @param             <T> the type of the input elements
	 * @param             <K> the output type of the key mapping function
	 * @param             <U> the output type of the value mapping function
	 * @param             <M> the type of the resulting {@code Map}
	 * @param keyMapper   a mapping function to produce keys
	 * @param valueMapper a mapping function to produce values
	 * @param mapSupplier a function which returns a new, empty {@code Map} into
	 *                    which the results will be inserted
	 * @return a {@code Collector} which collects elements into a {@code Map} whose
	 *         keys are the result of applying a key mapping function to the input
	 *         elements, and whose values are the result of applying a value mapping
	 *         function to all input elements equal to the key and combining them
	 *         using the merge function
	 */
	public static <T, K, U, M extends Map<K, U>> Collector<T, ?, M> toMap(
			final Function<? super T, ? extends K> keyMapper,
			final Function<? super T, ? extends U> valueMapper,
			final Supplier<M> mapSupplier) {
		return toMap(keyMapper, valueMapper, throwingMerger(), mapSupplier);
	}

	/**
	 * Returns a {@code Collector} that accumulates elements into a {@code Map}
	 * whose keys and values are the result of applying the provided mapping
	 * functions to the input elements.
	 *
	 * <p>
	 * If the mapped keys contains duplicates (according to
	 * {@link Object#equals(Object)}), the value mapping function is applied to each
	 * equal element, and the results are merged using the provided merging
	 * function. The {@code Map} is created by a provided supplier function.
	 *
	 * @implNote The returned {@code Collector} is not concurrent. For parallel
	 *           stream pipelines, the {@code combiner} function operates by merging
	 *           the keys from one map into another, which can be an expensive
	 *           operation. If it is not required that results are merged into the
	 *           {@code Map} in encounter order, using
	 *           {@link #toConcurrentMap(Function, Function, BinaryOperator, Supplier)}
	 *           may offer better parallel performance.
	 *
	 * @param               <T> the type of the input elements
	 * @param               <K> the output type of the key mapping function
	 * @param               <U> the output type of the value mapping function
	 * @param               <M> the type of the resulting {@code Map}
	 * @param keyMapper     a mapping function to produce keys
	 * @param valueMapper   a mapping function to produce values
	 * @param mergeFunction a merge function, used to resolve collisions between
	 *                      values associated with the same key, as supplied to
	 *                      {@link java.util.Map#merge(Object, Object, java.util.function.BiFunction)}
	 * @param mapSupplier   a function which returns a new, empty {@code Map} into
	 *                      which the results will be inserted
	 * @return a {@code Collector} which collects elements into a {@code Map} whose
	 *         keys are the result of applying a key mapping function to the input
	 *         elements, and whose values are the result of applying a value mapping
	 *         function to all input elements equal to the key and combining them
	 *         using the merge function
	 */
	public static <T, K, U, M extends Map<K, U>> Collector<T, ?, M> toMap(
			final Function<? super T, ? extends K> keyMapper,
			final Function<? super T, ? extends U> valueMapper,
			final BinaryOperator<U> mergeFunction,
			final Supplier<M> mapSupplier) {
		return java.util.stream.Collectors.toMap(keyMapper, valueMapper, mergeFunction, mapSupplier);
	}
}
