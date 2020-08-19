package de.larssh.utils.text;

import static de.larssh.utils.collection.Iterators.peekableIterator;
import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;

import de.larssh.utils.collection.Iterators;
import de.larssh.utils.collection.Maps;
import de.larssh.utils.collection.PeekableIterator;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for line based processing.
 *
 * <p>
 * <b>Usage example 1:</b> The following shows how to merge lines to logical log
 * lines.
 *
 * <pre>
 * // Some lines of example log files
 * final List&lt;String&gt; fileLines = lines("[20:57:30] Thread 0: Start request\n"
 * 		+ "[20:57:31] Thread 0: Request ID: Example 1\n"
 * 		+ "[20:57:31] Thread 0: Request failed: SomeException\n"
 * 		+ "  on line 12\n"
 * 		+ "  on line 34\n"
 * 		+ "[20:57:32] Thread 0: Stop request");
 *
 * // Calculating logical log lines based on consecutive lines
 * final Stream&lt;List&lt;String&gt;&gt; logicalLogLines = consecutive(
 * 		fileLines,
 * 		(lines, line) -&gt; !line.startsWith("["));
 *
 * // Output logical log lines with three dashes in between each of them
 * System.out.println(logicalLogLines //
 * 		.map(lines -&gt; lines.stream().collect(joining("\n")))
 * 		.collect(joining("\n---\n")));
 * </pre>
 *
 * <p>
 * Console output for usage example 1:
 *
 * <pre>
 * [20:57:30] Thread 1: Start request
 * ---
 * [20:57:31] Thread 1: Request ID: Example 1
 * ---
 * [20:57:31] Thread 1: Request failed: SomeException
 *   on line 12
 *   on line 34
 * ---
 * [20:57:32] Thread 1: Stop request
 * </pre>
 *
 * <p>
 * <b>Usage example 2:</b> The following shows how to group log lines based on
 * their thread name.
 *
 * <pre>
 * // Some lines of example log files
 * final List&lt;String&gt; fileLines = lines(
 * 		  "[20:57:30] Thread 1: Start request\n"
 * 		+ "[20:57:30] Thread 2: Stop request\n"
 * 		+ "[20:57:30] Thread 1: Request ID: Example 2.1\n"
 * 		+ "[20:57:31] Thread 2: Start request\n"
 * 		+ "[20:57:31] Thread 1: Request processed\n"
 * 		+ "[20:57:31] Thread 2: Request ID: Example 2.2\n"
 * 		+ "[20:57:32] Thread 1: Stop request\n"
 * 		+ "[20:57:32] Thread 2: Request processed");
 *
 * // Grouping log lines based on thread names
 * final Stream&lt;Entry&lt;String, List&lt;String&gt;&gt;&gt; groupedLogLines = grouped(
 * 		fileLines,
 * 		line -&gt; line.substring(11, 19),
 * 		(lines, line) -&gt; {
 * 			if (line.endsWith("Start request")) {
 * 				return GroupedLineType.START;
 * 			}
 * 			if (line.endsWith("Stop request")) {
 * 				return GroupedLineType.END;
 * 			}
 * 			return GroupedLineType.MIDDLE;
 * 		});
 *
 * // Output grouped lines with three dashes between each group
 * System.out.println(groupedLogLines
 * 		.map(entry -&gt; entry.getKey() + ":\n" + entry.getValue().stream().collect(joining("\n")))
 * 		.collect(joining("\n---\n")));
 * </pre>
 *
 * <p>
 * Console output for usage example 2:
 *
 * <pre>
 * Thread 2:
 * [20:57:30] Thread 2: Stop request
 * ---
 * Thread 1:
 * [20:57:30] Thread 1: Start request
 * [20:57:30] Thread 1: Request ID: Example 2.1
 * [20:57:31] Thread 1: Request processed
 * [20:57:32] Thread 1: Stop request
 * ---
 * Thread 2:
 * [20:57:31] Thread 2: Start request
 * [20:57:31] Thread 2: Request ID: Example 2.2
 * [20:57:32] Thread 2: Request processed
 * </pre>
 *
 * <p>
 * Based on your your needs you might need to combine the methods
 * {@code lines(...)}, {@code consecutive(...)} and {@code grouped(...)}.
 */
@UtilityClass
public class Lines {
	/**
	 * Creates lists of consecutive lines based on {@code lines}. An input line is
	 * added to an output list whenever {@code isNextConsecutive} returns
	 * {@code true}. On {@code false} a new list gets created.
	 *
	 * <p>
	 * Check out usage example 1 at {@link Lines}.
	 *
	 * <p>
	 * <b>Tip:</b> {@code lines} must not necessarily consist of elements of type
	 * {@link String}.
	 *
	 * @param <T>               type of line (most probably {@link String})
	 * @param lines             the lines
	 * @param isNextConsecutive when returning {@code true} the input line is added
	 *                          to an output list, else a new list gets created. The
	 *                          first argument is the previous list of lines,
	 *                          whereas the second argument is the current line.
	 * @return stream of lists containing consecutive lines
	 */
	public static <T> Stream<List<T>> consecutive(final Iterable<T> lines,
			final BiPredicate<List<T>, T> isNextConsecutive) {
		return consecutive(lines.iterator(), isNextConsecutive);
	}

	/**
	 * Creates lists of consecutive lines based on {@code lines}. An input line is
	 * added to an output list whenever {@code isNextConsecutive} returns
	 * {@code true}. On {@code false} a new list gets created.
	 *
	 * <p>
	 * Check out usage example 1 at {@link Lines}.
	 *
	 * <p>
	 * <b>Tip:</b> {@code lines} must not necessarily consist of elements of type
	 * {@link String}.
	 *
	 * @param <T>               type of line (most probably {@link String})
	 * @param linesIterator     the lines
	 * @param isNextConsecutive when returning {@code true} the input line is added
	 *                          to an output list, else a new list gets created. The
	 *                          first argument is the previous list of lines,
	 *                          whereas the second argument is the current line.
	 * @return stream of lists containing consecutive lines
	 */
	@SuppressFBWarnings(value = "PSC_PRESIZE_COLLECTIONS",
			justification = "No reasonable way to calculate a size estimation for \"lines\"")
	public static <T> Stream<List<T>> consecutive(final Iterator<T> linesIterator,
			final BiPredicate<List<T>, T> isNextConsecutive) {
		final PeekableIterator<T> peekableLinesIterator = peekableIterator(linesIterator);

		return Iterators.stream(state -> {
			final List<T> lines = new ArrayList<>();
			while (peekableLinesIterator.hasNext()) {
				lines.add(peekableLinesIterator.next());

				// Fail fast if the peeked line is not consecutive
				if (!isNextConsecutive.test(lines, peekableLinesIterator.peek())) {
					return lines;
				}
			}
			return lines.isEmpty() ? state.endOfData() : lines;
		});
	}

	/**
	 * Creates lists of consecutive lines based on {@code lines}. An input line is
	 * added to an output list whenever {@code isNextConsecutive} returns
	 * {@code true}. On {@code false} a new list gets created.
	 *
	 * <p>
	 * Check out usage example 1 at {@link Lines}.
	 *
	 * <p>
	 * <b>Tip:</b> {@code lines} must not necessarily consist of elements of type
	 * {@link String}.
	 *
	 * @param <T>               type of line (most probably {@link String})
	 * @param lines             the lines
	 * @param isNextConsecutive when returning {@code true} the input line is added
	 *                          to an output list, else a new list gets created. The
	 *                          first argument is the previous list of lines,
	 *                          whereas the second argument is the current line.
	 * @return stream of lists containing consecutive lines
	 */
	public static <T> Stream<List<T>> consecutive(final Stream<T> lines,
			final BiPredicate<List<T>, T> isNextConsecutive) {
		return consecutive(lines.iterator(), isNextConsecutive);
	}

	/**
	 * Groups lines based on a key and line types. While {@code getGroupKey}
	 * calculates the key that is used for grouping {@code getLineType} calculates
	 * the line type, which specifies the way groups of lines are created and
	 * closed.
	 *
	 * <p>
	 * Check out usage example 2 at {@link Lines}.
	 *
	 * <p>
	 * <b>Tip:</b> {@code lines} must not necessarily consist of elements of type
	 * {@link String}.
	 *
	 * @param <K>         type of the group key
	 * @param <V>         type of line
	 * @param lines       the lines
	 * @param getGroupKey calculates the lines group key
	 * @param getLineType calculates the lines type to specify the way groups of
	 *                    lines are created and closed. The first argument is the
	 *                    previous list of lines, whereas the second argument is the
	 *                    current line.
	 * @return stream of entries with the group key as key and the grouped lines as
	 *         value
	 */
	public static <K, V> Stream<Entry<K, List<V>>> grouped(final Iterable<V> lines,
			final Function<V, K> getGroupKey,
			final BiFunction<List<V>, V, GroupedLineType> getLineType) {
		return grouped(lines.iterator(), getGroupKey, getLineType);
	}

	/**
	 * Groups lines based on a key and line types. While {@code getGroupKey}
	 * calculates the key that is used for grouping {@code getLineType} calculates
	 * the line type, which specifies the way groups of lines are created and
	 * closed.
	 *
	 * <p>
	 * Check out usage example 2 at {@link Lines}.
	 *
	 * <p>
	 * <b>Tip:</b> {@code lines} must not necessarily consist of elements of type
	 * {@link String}.
	 *
	 * @param <K>         type of the group key
	 * @param <V>         type of line
	 * @param lines       the lines
	 * @param getGroupKey calculates the lines group key
	 * @param getLineType calculates the lines type to specify the way groups of
	 *                    lines are created and closed. The first argument is the
	 *                    previous list of lines, whereas the second argument is the
	 *                    current line.
	 * @return stream of entries with the group key as key and the grouped lines as
	 *         value
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public static <K, V> Stream<Entry<K, List<V>>> grouped(final Iterator<V> lines,
			final Function<V, K> getGroupKey,
			final BiFunction<List<V>, V, GroupedLineType> getLineType) {
		// Buffer of so-far not-closed groups of lines
		final Map<K, List<V>> groups = new LinkedHashMap<>();

		return Iterators.stream(state -> {
			while (lines.hasNext()) {
				final V line = lines.next();
				final K groupKey = getGroupKey.apply(line);

				List<V> groupOfCurrentLine = groups.computeIfAbsent(groupKey, key -> new ArrayList<>());
				final GroupedLineType lineType = getLineType.apply(groupOfCurrentLine, line);

				if (lineType == GroupedLineType.END) {
					groupOfCurrentLine.add(line);
					return Maps.entry(groupKey, groups.remove(groupKey));
				}
				if (lineType == GroupedLineType.START && !groupOfCurrentLine.isEmpty()) {
					groupOfCurrentLine = new ArrayList<>();
					groupOfCurrentLine.add(line);
					return Maps.entry(groupKey, groups.put(groupKey, groupOfCurrentLine));
				}

				groupOfCurrentLine.add(line);
			}

			if (groups.isEmpty()) {
				return state.endOfData();
			}
			final K groupKey = groups.keySet().iterator().next();
			return Maps.entry(groupKey, groups.remove(groupKey));
		});
	}

	/**
	 * Groups lines based on a key and line types. While {@code getGroupKey}
	 * calculates the key that is used for grouping {@code getLineType} calculates
	 * the line type, which specifies the way groups of lines are created and
	 * closed.
	 *
	 * <p>
	 * Check out usage example 2 at {@link Lines}.
	 *
	 * <p>
	 * <b>Tip:</b> {@code lines} must not necessarily consist of elements of type
	 * {@link String}.
	 *
	 * @param <K>         type of the group key
	 * @param <V>         type of line
	 * @param lines       the lines
	 * @param getGroupKey calculates the lines group key
	 * @param getLineType calculates the lines type to specify the way groups of
	 *                    lines are created and closed. The first argument is the
	 *                    previous list of lines, whereas the second argument is the
	 *                    current line.
	 * @return stream of entries with the group key as key and the grouped lines as
	 *         value
	 */
	public static <K, V> Stream<Entry<K, List<V>>> grouped(final Stream<V> lines,
			final Function<V, K> getGroupKey,
			final BiFunction<List<V>, V, GroupedLineType> getLineType) {
		return grouped(lines.iterator(), getGroupKey, getLineType);
	}

	/**
	 * Reads all characters of {@code reader} and splits them into lines using
	 * {@link BufferedReader#lines()}.
	 *
	 * @param reader character stream to split into lines
	 * @return list of lines
	 */
	public static Stream<String> lines(final Reader reader) {
		final BufferedReader bufferedReader
				= reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
		return bufferedReader.lines();
	}

	/**
	 * Splits {@code value} into lines using {@link BufferedReader#lines()}.
	 *
	 * @param value value to split
	 * @return list of lines
	 */
	public static List<String> lines(final String value) {
		return lines(new StringReader(value)).collect(toList());
	}

	/**
	 * Specifies the way groups of lines are created and closed.
	 */
	@SuppressWarnings("PMD.UnnecessaryModifier")
	public enum GroupedLineType {
		/**
		 * Start of a group of lines
		 *
		 * <p>
		 * Creates a new group of lines. Closes a previously not-closed group, if any.
		 */
		START,

		/**
		 * A grouped line, placed in a groups middle
		 *
		 * <p>
		 * Adds the current line to an existing group of lines. If no group was open, a
		 * new group is created.
		 */
		MIDDLE,

		/**
		 * End of a group of lines
		 *
		 * <p>
		 * Closes an open group of lines, if any.
		 */
		END;
	}
}
