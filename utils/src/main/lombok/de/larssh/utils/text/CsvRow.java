package de.larssh.utils.text;

import java.util.List;
import java.util.Optional;

import de.larssh.utils.annotations.PackagePrivate;
import de.larssh.utils.collection.ProxiedList;
import lombok.Getter;

/**
 * Class representing the values of one CSV row, implementing
 * {@code List<String>} for convenience reasons.
 *
 * <p>
 * Objects of this type include a back-reference to the parent {@link Csv}
 * object and the current row's index. If the parent object is marked
 * unmodifiable, its rows are unmodifiable, too.
 *
 * <p>
 * For convenience the method {@link #get(String)} allows to retrieve values by
 * the column's CSV header value.
 */
@Getter
public class CsvRow extends ProxiedList<String> {
	/**
	 * Reference to the parent CSV object
	 *
	 * @return the reference to the parent CSV object
	 */
	Csv csv;

	/**
	 * The current row's index
	 *
	 * @return the current row's index
	 */
	int rowIndex;

	/**
	 * Class representing the values of one CSV row, implementing {@link List} for
	 * convenience reasons.
	 *
	 * <p>
	 * Objects of this type include a back-reference to the parent CSV object and
	 * the current row's index.
	 *
	 * @param csv      the back-reference to the parent CSV object
	 * @param rowIndex the current row's index
	 * @param data     the current row's values
	 * @throws IllegalArgumentException if {@code rowIndex} is less than zero
	 */
	@PackagePrivate
	CsvRow(final Csv csv, final int rowIndex, final List<String> data) {
		super(data);

		if (rowIndex < 0) {
			throw new IllegalArgumentException(
					String.format("CSV row index must not be less tan zero, but was %d.", rowIndex));
		}

		this.csv = csv;
		this.rowIndex = rowIndex;
	}

	/**
	 * Returns the current row's value of the column specified by {@code header}.
	 * Valid headers are specified by the first row of the CSV. In case
	 * {@code header} is not a valid header or there is no value for the column of
	 * the given header, {@code Optional#empty()} is returned.
	 *
	 * <p>
	 * In case multiple headers with the name {@code header} exist, the first column
	 * is selected.
	 *
	 * @param header the header name to search for
	 * @return the current row's value of the column specified by {@code header},
	 *         else empty
	 */
	public Optional<String> get(final String header) {
		final int index = getCsv().getHeaders().indexOf(header);
		return index == -1 || index >= size() ? Optional.empty() : Optional.of(get(index));
	}

	/** {@inheritDoc} */
	@Override
	public boolean isModifiable() {
		return getCsv().isModifiable();
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
		return toString(Csv.DEFAULT_SEPARATOR, Csv.DEFAULT_ESCAPER);
	}

	/**
	 * Returns a valid CSV string representation of this object using
	 * {@code separator} and {@code escaper}.
	 *
	 * <p>
	 * This is a short-hand method for
	 * {@link StringConverters#encodeCsvRow(java.util.Collection, char, char)}.
	 *
	 * @param separator the CSV separator
	 * @param escaper   the CSV value escaper
	 * @return a valid CSV representation of this object using {@code separator} and
	 *         {@code escaper}
	 */
	public String toString(final char separator, final char escaper) {
		return StringConverters.encodeCsvRow(this, separator, escaper);
	}
}
