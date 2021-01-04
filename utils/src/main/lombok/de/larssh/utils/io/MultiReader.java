package de.larssh.utils.io;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Optional;

import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;

/**
 * A {@code MultiReader} concatenates multiple {@link Reader}s into one. That
 * might be useful for e.g. reading log files of multiple days.
 *
 * <p>
 * The {@code readerIterator} needs to supply the {@link Reader}s to concatenate
 * one after the other. {@link Reader}s, which have been read, are closed prior
 * requesting the next.
 */
@RequiredArgsConstructor
public class MultiReader extends Reader {
	/**
	 * Value, that is returned by {@link Reader#read(char[], int, int)} on end of a
	 * {@link Reader}.
	 */
	private static final int END_OF_READER = -1;

	/**
	 * If {@code true} this {@link MultiReader} reached its end, meaning
	 * {@link #readerIterator} has no next element.
	 */
	@NonFinal
	boolean closed = false;

	/**
	 * The current {@link Reader} or empty if the next needs to be supplied first or
	 * if this {@link MultiReader} is closed.
	 */
	@NonFinal
	Optional<Reader> currentReader = Optional.empty();

	/**
	 * {@link Iterator}, called whenever the next {@link Reader} needs to be
	 * supplied. If it has no next element, the {@link MultiReader} is closed.
	 */
	Iterator<Reader> readerIterator;

	/** {@inheritDoc} */
	@Override
	public void close() throws IOException {
		closed = true;
		if (currentReader.isPresent()) {
			currentReader.get().close();
			currentReader = Optional.empty();
		}
	}

	/**
	 * Determines the current {@link Reader} by updating the internal state of this
	 * {@link MultiReader} if necessary.
	 *
	 * @return the current {@link Reader} or empty if closed
	 */
	@SuppressWarnings({ "checkstyle:SuppressWarnings", "resource" })
	private Optional<Reader> getCurrentReader() {
		if (closed) {
			return Optional.empty();
		}
		if (currentReader.isPresent()) {
			return currentReader;
		}
		currentReader = readerIterator.hasNext() ? Optional.of(readerIterator.next()) : Optional.empty();
		if (!currentReader.isPresent()) {
			closed = true;
		}
		return currentReader;
	}

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("checkstyle:SuppressWarnings")
	public int read(@Nullable final char[] outputBuffer, final int offset, final int length) throws IOException {
		final Optional<Reader> reader = getCurrentReader();
		if (!reader.isPresent()) {
			return END_OF_READER;
		}

		// Read from current reader
		@SuppressWarnings("resource")
		final int chars = reader.get().read(outputBuffer, offset, length);
		if (chars == length) {
			return chars;
		}

		// If less characters than expected have been read from the current reader,
		// close it and continue with the next
		currentReader = Optional.empty();
		reader.get().close();

		// Read additional chunk of characters
		final int nonNegativeChars = chars == END_OF_READER ? 0 : chars;
		final int nextChars = read(outputBuffer, offset + nonNegativeChars, length - nonNegativeChars);
		return nonNegativeChars + (nextChars == END_OF_READER ? 0 : nextChars);
	}

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings({ "checkstyle:SuppressWarnings", "resource" })
	public boolean ready() throws IOException {
		final Optional<Reader> reader = getCurrentReader();
		return reader.isPresent() && reader.get().ready();
	}
}
