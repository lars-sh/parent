package de.larssh.utils.text;

import static java.util.Collections.emptyList;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.larssh.utils.Finals;
import de.larssh.utils.Nullables;
import de.larssh.utils.collection.ProxiedList;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.NonFinal;

/**
 * This class represents CSV data, consisting of rows of values, implementing
 * {@code List<String>} for convenience reasons.
 *
 * <p>
 * The first row is referenced as <i>header</i> row and {@link CsvRow} instances
 * allow to retrieve their values by the column's CSV header value.
 *
 * <p>
 * Instances of this class can be marked unmodifiable using
 * {@link #unmodifiable()}. Afterwards applying modifying actions on that
 * instance results in an {@link UnsupportedOperationException}.
 */
@SuppressWarnings("PMD.ShortClassName")
public class Csv extends ProxiedList<CsvRow> {
	/**
	 * The default CSV escape character
	 */
	public static final char DEFAULT_ESCAPER = Finals.constant('"');

	/**
	 * The default CSV separator character
	 */
	public static final char DEFAULT_SEPARATOR = Finals.constant(',');

	/**
	 * Parses the CSV data given by {@code reader}, starting at the current
	 * position.
	 *
	 * @param reader    a {@link Reader} as CSV data input
	 * @param separator the CSV separator character
	 * @param escaper   the CSV escaping character
	 * @return an object representing the parsed CSV data
	 * @throws IllegalArgumentException on illegal {@code separator} or
	 *                                  {@code escaper} value
	 * @throws IOException              if an I/O error occurs
	 */
	public static Csv parse(final Reader reader, final char separator, final char escaper) throws IOException {
		return new CsvParser(separator, escaper).parse(reader);
	}

	/**
	 * Flag specifying if this instance can be modified
	 *
	 * @param modifiable flag
	 * @return {@code true} if this instance is modifiable, else {@code false}
	 */
	@NonFinal
	@Getter(AccessLevel.PUBLIC)
	boolean modifiable = true;

	/**
	 * This class represents CSV data, consisting of rows of values, implementing
	 * {@code List<String>} for convenience reasons.
	 *
	 * <p>
	 * The first row is referenced as <i>header</i> row and {@link CsvRow} instances
	 * allow to retrieve their values by the column's CSV header value.
	 */
	public Csv() {
		super(new ArrayList<>());
	}

	/**
	 * This class represents CSV data, consisting of rows of values, implementing
	 * {@code List<String>} for convenience reasons.
	 *
	 * <p>
	 * The first row is referenced as <i>header</i> row and {@link CsvRow} instances
	 * allow to retrieve their values by the column's CSV header value.
	 *
	 * @param initialCapacity an initial capacity of the internal list
	 * @throws IllegalArgumentException if the specified initial capacity is
	 *                                  negative
	 */
	public Csv(final int initialCapacity) {
		super(new ArrayList<>(initialCapacity));
	}

	/**
	 * This class represents CSV data, consisting of rows of values, implementing
	 * {@code List<String>} for convenience reasons.
	 *
	 * <p>
	 * The first row is referenced as <i>header</i> row and {@link CsvRow} instances
	 * allow to retrieve their values by the column's CSV header value.
	 *
	 * @param data a collection containing CSV rows to be added
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	@SuppressFBWarnings(value = "PCOA_PARTIALLY_CONSTRUCTED_OBJECT_ACCESS",
			justification = "passing this object to CsvRow constructor shouldn't be a problem")
	public Csv(final List<? extends List<String>> data) {
		this(data.size());

		for (final List<String> row : Nullables.orElseThrow(data)) {
			super.add(new CsvRow(this, super.size(), Nullables.orElseThrow(row)));
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean add(@Nullable final CsvRow element) {
		return add((List<String>) Nullables.orElseThrow(element));
	}

	/**
	 * Adds the specified CSV row to the this CSV object.
	 *
	 * @param element the CSV row to be added
	 * @return {@code true}
	 * @throws UnsupportedOperationException if this CSV object is unmodifiable
	 */
	public boolean add(final List<String> element) {
		return super.add(new CsvRow(this, size(), element));
	}

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unused")
	public void add(final int index, @Nullable final CsvRow element) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	@Override
	public boolean addAll(@Nullable final Collection<? extends CsvRow> collection) {
		return addAllList(Nullables.orElseThrow(collection));
	}

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unused")
	public boolean addAll(final int index, @Nullable final Collection<? extends CsvRow> collection) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Adds all of the CSV rows in {@code collection} to this CSV object.
	 *
	 * <p>
	 * The behavior of this operation is undefined if the specified collection is
	 * modified while the operation is in progress. (This implies, that the behavior
	 * of this call is undefined if the specified collection is this CSV object, and
	 * this CSV object is non-empty.)
	 *
	 * @param collection the collection containing CSV rows to be added
	 * @return {@code true} if this CSV object changed as a result of the call, else
	 *         {@code false}
	 * @throws UnsupportedOperationException if this CSV object is unmodifiable
	 */
	public boolean addAllList(final Collection<? extends List<String>> collection) {
		boolean modified = false;
		for (final List<String> row : collection) {
			modified |= add(row);
		}
		return modified;
	}

	/** {@inheritDoc} */
	@NonNull
	@Override
	public CsvRow get(final int index) {
		return getWrappedForRead().get(index);
	}

	/**
	 * Returns the headers of this CSV object. Headers are specified by the first
	 * row. If this CSV object is empty, an empty list is returned.
	 *
	 * @return the headers of this CSV object
	 */
	public List<String> getHeaders() {
		return isEmpty() ? emptyList() : get(0);
	}

	/** {@inheritDoc} */
	@NonNull
	@Override
	public CsvRow remove(final int index) {
		if (index == size() - 1) {
			return Nullables.orElseThrow(super.remove(index));
		}
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	@NonNull
	@Override
	public CsvRow set(final int index, @Nullable final CsvRow element) {
		return getWrappedIfModifiable().set(index, new CsvRow(this, index, Nullables.orElseThrow(element)));
	}

	/**
	 * Returns a valid CSV string representation of this object using the default
	 * separator and escaper.
	 *
	 * @return a valid CSV representation of this object using default separator and
	 *         escaper
	 */
	@Override
	public String toString() {
		return toString(DEFAULT_SEPARATOR, Csv.DEFAULT_ESCAPER);
	}

	/**
	 * Returns a valid CSV string representation of this object using
	 * {@code separator} and {@code escaper}.
	 *
	 * <p>
	 * This is a short-hand method for
	 * {@link StringConverters#encodeCsv(Collection, char, char)}.
	 *
	 * @param separator the CSV separator
	 * @param escaper   the CSV value escaper
	 * @return a valid CSV representation of this object using {@code separator} and
	 *         {@code escaper}
	 */
	public String toString(final char separator, final char escaper) {
		return StringConverters.encodeCsv(this, separator, escaper);
	}

	/**
	 * Marks the CSV object as unmodifiable
	 *
	 * @return this objects
	 */
	public Csv unmodifiable() {
		modifiable = false;
		return this;
	}
}
