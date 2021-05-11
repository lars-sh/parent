package de.larssh.utils.io;

import java.io.IOException;
import java.io.Reader;
import java.util.NoSuchElementException;

import de.larssh.utils.Nullables;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;

/**
 * A {@link Reader} that allows peeking up to one character without removing it,
 * from the logical I/O stream, supporting a one-element lookahead.
 *
 * <p>
 * The methods {@link #hasNext()} and {@link #next()} are implemented similar to
 * the methods of {@link java.util.Iterator} to simplify working with character
 * based readers.
 */
@RequiredArgsConstructor
public class PeekableReader extends Reader {
	/**
	 * The wrapped reader
	 */
	Reader reader;

	/**
	 * The next character read if {@link #state} is {@link ReaderState#PEEKED}, else
	 * undefined.
	 */
	@NonFinal
	int peekedCharacter = -1;

	/**
	 * The reader's current inner state
	 */
	@NonFinal
	ReaderState state = ReaderState.CALL_FOR_NEXT;

	/**
	 * The peeked character at the time of calling {@link #mark(int)} the last time.
	 */
	@NonFinal
	int markedPeekedCharacter = -1;

	/**
	 * The reader's current inner state at the time of calling {@link #mark(int)}
	 * the last time.
	 */
	@NonFinal
	ReaderState markedState = ReaderState.CALL_FOR_NEXT;

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("PMD.CloseResource")
	public void close() throws IOException {
		reader.close();
	}

	/**
	 * Returns {@code true} if the reader has more characters. (In other words,
	 * returns {@code true} if {@link #next()} would return a character rather than
	 * throwing an exception.)
	 *
	 * @return {@code true} if the reader has more characters, else {@code false}
	 * @throws IOException if an I/O error occurs
	 */
	public final boolean hasNext() throws IOException {
		if (state == ReaderState.CALL_FOR_NEXT) {
			peekedCharacter = reader.read();
			state = peekedCharacter == -1 ? ReaderState.END_OF_DATA : ReaderState.PEEKED;
		}
		return state == ReaderState.PEEKED;
	}

	/** {@inheritDoc} */
	@Override
	public void mark(final int readAheadLimit) throws IOException {
		reader.mark(readAheadLimit);
		markedPeekedCharacter = peekedCharacter;
		markedState = state;
	}

	/** {@inheritDoc} */
	@Override
	public boolean markSupported() {
		return reader.markSupported();
	}

	/**
	 * Returns the next character of the reader.
	 *
	 * @return the next character of the reader
	 * @throws IOException            if an I/O error occurs
	 * @throws NoSuchElementException if the reader has no more characters
	 */
	public final char next() throws IOException {
		// Method "hasNext" peeks the next character (if required)
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		if (state == ReaderState.PEEKED) {
			state = ReaderState.CALL_FOR_NEXT;
		}
		final char next = (char) peekedCharacter;
		peekedCharacter = -1;
		return next;
	}

	/**
	 * Returns the next character in the reader, returned by {@link #read()},
	 * without removing it from the I/O stream.
	 *
	 * @return the next element in the iteration
	 * @throws IOException            if an I/O error occurs
	 * @throws NoSuchElementException if the iteration has no more elements
	 */
	public char peek() throws IOException {
		// Method "hasNext" peeks the next character (if required)
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		return (char) peekedCharacter;
	}

	/** {@inheritDoc} */
	@Override
	public int read() throws IOException {
		if (state == ReaderState.PEEKED) {
			state = ReaderState.CALL_FOR_NEXT;
			return (char) peekedCharacter;
		}
		return super.read();
	}

	/** {@inheritDoc} */
	@Override
	public int read(@Nullable final char[] buffer, final int offset, final int length) throws IOException {
		if (state == ReaderState.CALL_FOR_NEXT) {
			return reader.read(buffer, offset, length);
		}
		if (state == ReaderState.END_OF_DATA) {
			return -1;
		}

		// Error handling as of JavaDoc
		final char[] nonNullableBuffer = Nullables.orElseThrow(buffer);
		if (offset < 0 || length < 0 || offset + length > nonNullableBuffer.length) {
			throw new IndexOutOfBoundsException();
		}

		// Early exit: Avoid further processing in case no character were requested
		if (length == 0) {
			return 0;
		}

		// Insert peeked character as first character
		nonNullableBuffer[offset] = (char) peekedCharacter;
		state = ReaderState.CALL_FOR_NEXT;

		// No need to read further characters if just one character were requested
		if (length == 1) {
			return 1;
		}

		// Read further characters and handle possible end of data
		final int noOfCharacters = reader.read(buffer, offset + 1, length - 1);
		if (noOfCharacters < 0) {
			state = ReaderState.END_OF_DATA;
			return 1;
		}
		return noOfCharacters + 1;
	}

	/** {@inheritDoc} */
	@Override
	public void reset() throws IOException {
		reader.reset();
		this.peekedCharacter = markedPeekedCharacter;
		state = markedState;
	}

	/**
	 * This enumeration contains the possible inner states of
	 * {@link PeekableReader}.
	 */
	@SuppressWarnings("PMD.UnnecessaryModifier")
	private enum ReaderState {
		/**
		 * Status of a reader, that needs to read once information about the next
		 * character is required.
		 */
		CALL_FOR_NEXT,

		/**
		 * Status representing the end of data. The reader must not be called any
		 * longer. This is an end state and must not change once reached.
		 */
		END_OF_DATA,

		/**
		 * Status of a reader, that peeked the next character already.
		 */
		PEEKED;
	}
}
