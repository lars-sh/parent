package de.larssh.utils.time;

import static de.larssh.utils.function.ThrowingConsumer.throwing;
import static java.util.Collections.synchronizedList;
import static java.util.Collections.unmodifiableList;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import de.larssh.utils.Nullables;
import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Implementation of a synchronized stopwatch starting at {@link Instant#now()}.
 * {@link #sinceStart()} can be used to retrieve the duration since the
 * stopwatch started.
 *
 * <p>
 * {@link #checkpoint(String)} adds a new checkpoint. It might be used for split
 * or lap times. {@link #sinceLast()} can be used to retrieve the duration since
 * the last checkpoint was created.
 */
@Getter
@ToString
@NoArgsConstructor
public class Stopwatch {
	/**
	 * List of checkpoints
	 */
	List<Checkpoint> checkpoints = synchronizedList(new LinkedList<>());

	/**
	 * Instant at the stopwatches start
	 *
	 * @return the instant at the stopwatches start time
	 */
	Instant startInstant = Instant.now();

	/**
	 * Object used for locking
	 */
	@ToString.Exclude
	@Getter(AccessLevel.NONE)
	Object lock = new Object();

	/**
	 * Adds a new checkpoint referenced by {@code name}.
	 *
	 * <p>
	 * {@code name} does not need to be unique. Multiple checkpoints with the same
	 * name might exist.
	 *
	 * @param name name to reference the checkpoint
	 * @return the new checkpoint
	 */
	public Checkpoint checkpoint(final String name) {
		synchronized (lock) {
			final Checkpoint checkpoint = new Checkpoint(this, name, Instant.now(), getLastCheckpoint());
			checkpoints.add(checkpoint);
			return checkpoint;
		}
	}

	/**
	 * List of checkpoints
	 *
	 * @return the list of checkpoints
	 */
	public List<Checkpoint> getCheckpoints() {
		return unmodifiableList(checkpoints);
	}

	/**
	 * The last created {@link Checkpoint} or empty if no checkpoint was created
	 *
	 * @return the last created checkpoint
	 */
	public Optional<Checkpoint> getLastCheckpoint() {
		return checkpoints.isEmpty() ? Optional.empty() : Optional.of(checkpoints.get(checkpoints.size() - 1));
	}

	/**
	 * Instant of the last created {@link Checkpoint} or the stopwatches starting
	 * time if no checkpoint was created
	 *
	 * @return the last created instant or the stopwatches starting time
	 */
	public Instant getLastInstant() {
		return checkpoints.isEmpty() ? getStartInstant() : checkpoints.get(checkpoints.size() - 1).getInstant();
	}

	/**
	 * Duration since the last created {@link Checkpoint} or the stopwatches
	 * starting time if no checkpoint was created
	 *
	 * @return the duration since last checkpoints instant or the stopwatches
	 *         starting time
	 */
	public Duration sinceLast() {
		return Duration.between(getLastInstant(), Instant.now());
	}

	/**
	 * Duration since the stopwatches start
	 *
	 * @return the duration since the stopwatches start
	 */
	public Duration sinceStart() {
		return Duration.between(getStartInstant(), Instant.now());
	}

	/**
	 * Waits for {@code duration} using {@code Thread#sleep(long)}. If reached, it
	 * times out at {@code timeoutSinceStart} after the {@link Stopwatch} start.
	 *
	 * @param duration          duration to wait
	 * @param timeoutSinceStart timeout duration since the {@link Stopwatch} start
	 * @return {@code true} if the timeout was not reached and {@code false} if the
	 *         timeout has been reached
	 * @throws InterruptedException if any thread has interrupted the current thread
	 */
	@SuppressWarnings("unused")
	@SuppressFBWarnings(value = "MDM_THREAD_YIELD",
			justification = "This is really intended to sleep for a specified duration.")
	public boolean waitFor(final Duration duration, final Duration timeoutSinceStart) throws InterruptedException {
		return waitFor(duration,
				timeoutSinceStart,
				throwing(waiting -> Thread.sleep(Nullables.orElseThrow(waiting).toMillis())));
	}

	/**
	 * Waits for {@code duration} using {@code wait}. If reached, it times out at
	 * {@code timeoutSinceStart} after the {@link Stopwatch} start.
	 *
	 * <p>
	 * Note: The {@link Duration} given to {@code wait} might be less than
	 * {@code duration} to handle {@code timeoutSinceStart} more precisely.
	 *
	 * @param duration          duration to wait
	 * @param timeoutSinceStart timeout duration since the {@link Stopwatch} start
	 * @param wait              method to use for waiting
	 * @return {@code true} if the timeout was not reached and {@code false} if the
	 *         timeout has been reached
	 */
	@SuppressFBWarnings(value = "PRMC_POSSIBLY_REDUNDANT_METHOD_CALLS",
			justification = "no redundant calls, because Instant.now() might return different values")
	public boolean waitFor(final Duration duration, final Duration timeoutSinceStart, final Consumer<Duration> wait) {
		if (duration.isNegative()) {
			throw new IllegalArgumentException(
					String.format("Parameter \"duration\" must not be negative, but is %s.", duration));
		}
		if (timeoutSinceStart.isNegative()) {
			throw new IllegalArgumentException(String
					.format("Parameter \"timeoutSinceStart\" must not be negative, but is %s.", timeoutSinceStart));
		}

		final Instant timeout = getStartInstant().plus(timeoutSinceStart);
		final Duration maxWaiting = Duration.between(Instant.now(), timeout);
		final Duration actualWaiting = maxWaiting.compareTo(duration) > 0 ? duration : maxWaiting;

		if (!actualWaiting.isNegative() && !actualWaiting.isZero()) {
			wait.accept(actualWaiting);
		}

		return timeout.isAfter(Instant.now());
	}

	/**
	 * Sequential {@code Stream} with the checkpoints as its source
	 *
	 * @return a sequential {@code Stream} over the checkpoints
	 */
	public Stream<Checkpoint> stream() {
		return checkpoints.stream();
	}

	/**
	 * Implementation of a stopwatches checkpoint. It might be used for split or lap
	 * times.
	 */
	@Getter
	@ToString
	@EqualsAndHashCode(onParam_ = { @Nullable })
	@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Checkpoint implements Comparable<Checkpoint>, TemporalAccessor {
		/**
		 * Comparator of {@link Checkpoint}
		 */
		private static final Comparator<Checkpoint> COMPARATOR = Comparator.comparing(Checkpoint::getInstant)
				.thenComparing(Comparator.comparing(checkpoint -> checkpoint.getPreviousCheckpoint()
						.map(Checkpoint::getInstant)
						.orElse(Instant.MIN)));

		/**
		 * Stopwatch
		 *
		 * @return the stopwatch
		 */
		@EqualsAndHashCode.Exclude
		Stopwatch stopwatch;

		/**
		 * Checkpoints name as reference
		 *
		 * @return the checkpoints name
		 */
		@ToString.Include(rank = 2)
		String name;

		/**
		 * Instant
		 *
		 * @return the instant
		 */
		@ToString.Include(rank = 3)
		Instant instant;

		/**
		 * Previous checkpoint or empty if this is the stopwatches first checkpoint
		 *
		 * @return the previous checkpoint
		 */
		@ToString.Include(rank = 1)
		Optional<Checkpoint> previousCheckpoint;

		/** {@inheritDoc} */
		@Override
		public int compareTo(@Nullable final Checkpoint object) {
			return Objects.compare(this, object, COMPARATOR);
		}

		/** {@inheritDoc} */
		@Override
		public long getLong(@Nullable final TemporalField field) {
			return getInstant().getLong(field);
		}

		/**
		 * Instant of the previous {@link Checkpoint} or the stopwatches starting time
		 * if this is the first checkpoint
		 *
		 * @return the previous checkpoints instant or the stopwatches starting time
		 */
		public Instant getPreviousInstant() {
			return getPreviousCheckpoint().map(Checkpoint::getInstant).orElseGet(getStopwatch()::getStartInstant);
		}

		/** {@inheritDoc} */
		@Override
		public boolean isSupported(@Nullable final TemporalField field) {
			return getInstant().isSupported(field);
		}

		/**
		 * Duration between this checkpoint and the previous {@link Checkpoint} or the
		 * stopwatches starting time if this is the first checkpoint
		 *
		 * @return the duration between this checkpoint and previous checkpoints instant
		 *         or the stopwatches starting time
		 */
		public Duration sincePrevious() {
			return Duration.between(getPreviousInstant(), getInstant());
		}

		/**
		 * Duration between this checkpoint and the stopwatches start
		 *
		 * @return the duration between this checkpoint and the stopwatches start
		 */
		public Duration sinceStart() {
			return Duration.between(getStopwatch().getStartInstant(), getInstant());
		}
	}
}
