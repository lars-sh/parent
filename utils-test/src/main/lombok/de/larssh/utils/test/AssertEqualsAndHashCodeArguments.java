package de.larssh.utils.test;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.NoArgsConstructor;

/**
 * Value object containing arguments in an original and a changed form, to be
 * used for asserting the methods {@link Object#equals(Object)} and
 * {@link Object#hashCode()}.
 *
 * <p>
 * The following example is made for a class called {@code Person}. That class
 * contains three fields (see below) and a constructor accordingly.
 * <ol>
 * <li>Personal ID (used for equality)
 * <li>First name (<b>not</b> used for equality)
 * <li>Last name (<b>not</b> used for equality)
 * </ol>
 *
 * <pre>
 * assertEqualsAndHashCode(a -&gt; new Person((int) a[0], (String) a[0], (String) a[0]),
 *     new AssertEqualsAndHashCodeArguments()
 *         .add(123, 456, true)
 *         .add('John', 'Jane', false)
 *         .add('Doe', 'Smith', false));
 * </pre>
 */
@NoArgsConstructor
public class AssertEqualsAndHashCodeArguments {
	/**
	 * List of original arguments
	 */
	List<Object> original = new ArrayList<>();

	/**
	 * List of changed arguments
	 */
	List<Object> changed = new ArrayList<>();

	/**
	 * List of boolean values per argument, meaning the at that index is meant to be
	 * used for equality checks
	 */
	List<Boolean> expectEquality = new ArrayList<>();

	/**
	 * Adds information of one more argument to the current list of arguments.
	 *
	 * @param original       original value
	 * @param changed        changed value
	 * @param expectEquality true, if argument is used for equality
	 * @return this
	 */
	public AssertEqualsAndHashCodeArguments add(@Nullable final Object original,
			@Nullable final Object changed,
			final boolean expectEquality) {
		this.original.add(original);
		this.changed.add(changed);
		this.expectEquality.add(expectEquality);
		return this;
	}

	/**
	 * List of original arguments
	 *
	 * @return list of original arguments
	 */
	public List<Object> getOriginal() {
		return unmodifiableList(original);
	}

	/**
	 * List of changed arguments
	 *
	 * @return list of changed arguments
	 */
	protected List<Object> getChanged() {
		return unmodifiableList(changed);
	}

	/**
	 * Returns a list of original arguments, which is changed at the given indexes.
	 *
	 * @param indexToBeChanged list of indexes to be changed
	 * @return list of arguments, changed at {@code indexToBeChanged}
	 */
	public List<Object> getChangedArguments(final int... indexToBeChanged) {
		final List<Object> changedAtIndex = new ArrayList<>(getOriginal());
		for (final int index : indexToBeChanged) {
			changedAtIndex.set(index, getChanged().get(index));
		}
		return changedAtIndex;
	}

	/**
	 * Returns true, if the argument at {@code index} is meant to be used for
	 * equality checks.
	 *
	 * @param index argument index
	 * @return true, if {@code index} is used for equality
	 */
	public boolean isExpectEquality(final int index) {
		return expectEquality.get(index);
	}
}
