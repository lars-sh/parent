package de.larssh.utils.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Optional;

import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;

/**
 * A {@code MultiInputStream} concatenates multiple {@link InputStream}s into
 * one. That might be useful for e.g. reading log files of multiple days.
 *
 * <p>
 * The {@code inputStreamIterator} needs to supply the {@link InputStream}s to
 * concatenate one after the other. {@link InputStream}s, which have been read,
 * are closed prior requesting the next.
 */
@RequiredArgsConstructor
public class MultiInputStream extends InputStream {
	/**
	 * Value, that is returned by {@link InputStream#read()} on end of an
	 * {@link InputStream}.
	 */
	private static final int END_OF_INPUT_STREAM = -1;

	/**
	 * If {@code true} this {@link MultiInputStream} reached its end, meaning
	 * {@link #inputStreamIterator} has no next element.
	 */
	@NonFinal
	boolean closed = false;

	/**
	 * The current {@link InputStream} or empty if the next needs to be supplied
	 * first or if this {@link MultiInputStream} is closed.
	 */
	@NonFinal
	Optional<InputStream> currentInputStream = Optional.empty();

	/**
	 * {@link Iterator}, called whenever the next {@link InputStream} needs to be
	 * supplied. If it has no next element, the {@link MultiInputStream} is closed.
	 */
	Iterator<InputStream> inputStreamIterator;

	/** {@inheritDoc} */
	@Override
	public void close() throws IOException {
		closed = true;
		if (currentInputStream.isPresent()) {
			currentInputStream.get().close();
			currentInputStream = Optional.empty();
		}
	}

	/**
	 * Determines the current {@link InputStream} by updating the internal state of
	 * this {@link MultiInputStream} if necessary.
	 *
	 * @return the current {@link InputStream} or empty if closed
	 */
	@SuppressWarnings({ "checkstyle:SuppressWarnings", "resource" })
	private Optional<InputStream> getCurrentInputStream() {
		if (closed) {
			return Optional.empty();
		}
		if (currentInputStream.isPresent()) {
			return currentInputStream;
		}
		currentInputStream = inputStreamIterator.hasNext() ? Optional.of(inputStreamIterator.next()) : Optional.empty();
		if (!currentInputStream.isPresent()) {
			closed = true;
		}
		return currentInputStream;
	}

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("checkstyle:SuppressWarnings")
	public int read() throws IOException {
		final Optional<InputStream> inputStream = getCurrentInputStream();
		if (!inputStream.isPresent()) {
			return END_OF_INPUT_STREAM;
		}

		// Read from current input stream
		@SuppressWarnings("resource")
		final int value = inputStream.get().read();
		if (value != END_OF_INPUT_STREAM) {
			return value;
		}

		// At the end of the current input stream, close it and continue with the next
		currentInputStream = Optional.empty();
		inputStream.get().close();
		return read();
	}

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("checkstyle:SuppressWarnings")
	public int read(@Nullable final byte[] outputBuffer, final int offset, final int length) throws IOException {
		final Optional<InputStream> inputStream = getCurrentInputStream();
		if (!inputStream.isPresent()) {
			return END_OF_INPUT_STREAM;
		}

		// Read from current input stream
		@SuppressWarnings("resource")
		final int chars = inputStream.get().read(outputBuffer, offset, length);
		if (chars == length) {
			return chars;
		}

		// If less bytes than expected have been read, closing the current input stream
		// and continuing with the next
		currentInputStream = Optional.empty();
		inputStream.get().close();

		// Read additional chunk of bytes
		final int nonNegativeChars = chars == END_OF_INPUT_STREAM ? 0 : chars;
		final int nextChars = read(outputBuffer, offset + nonNegativeChars, length - nonNegativeChars);
		return nonNegativeChars + (nextChars == END_OF_INPUT_STREAM ? 0 : nextChars);
	}
}
