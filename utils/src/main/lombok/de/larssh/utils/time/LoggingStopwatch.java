package de.larssh.utils.time;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import de.larssh.utils.text.Strings;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Implementation of {@link CloseableStopwatch} meant for logging stopwatch
 * data.
 *
 * <p>
 * See the static factory methods for simplified object creation.
 *
 * <p>
 * <b>Usage example:</b> The following shows the LoggingStopwatch used inside a
 * try-with-resource statement. The factory methods description describes
 * further details.
 *
 * <pre>
 * // Create new stopwatch using simplifying factory method and log
 * try (LoggingStopwatch stopwatch = LoggingStopwatch.early("Process steps", LOGGER::info)) {
 *
 *     // Do step 1
 *     ...
 *
 *     // Create new checkpoint and log
 *     stopwatch.checkpoint("Step 1 done");
 *
 *     // Do step 2
 *     ...
 *
 * // Stop stopwatch using try-with-resource (close method) and log
 * }
 * </pre>
 */
@Getter
@ToString(callSuper = true)
public class LoggingStopwatch extends CloseableStopwatch {
	/**
	 * Creates a {@link LoggingStopwatch} that logs on every logging event.
	 *
	 * <p>
	 * This logs an already constructed message. Use
	 * {@link #early(String, Consumer)} for common logging frameworks instead.
	 *
	 * @param name   the stopwatches name
	 * @param logger the string logging operation
	 * @return a {@link LoggingStopwatch} that logs on every logging event
	 */
	public static LoggingStopwatch earlyWithString(final String name, final Consumer<String> logger) {
		return early(name, supplier -> logger.accept(supplier.get()));
	}

	/**
	 * Creates a {@link LoggingStopwatch} that does <b>not</b> log on every logging
	 * event. Instead stopwatch data is logged after stopping the stopwatch to
	 * reduce the impact of logging from measured times.
	 *
	 * <p>
	 * This logs an already constructed message. Use {@link #late(String, Consumer)}
	 * for common logging frameworks instead.
	 *
	 * @param name   the stopwatches name
	 * @param logger the string logging operation
	 * @return a {@link LoggingStopwatch} that logs on every logging event
	 */
	public static LoggingStopwatch lateWithString(final String name, final Consumer<String> logger) {
		return late(name, supplier -> logger.accept(supplier.get()));
	}

	/**
	 * Creates a {@link LoggingStopwatch} that logs on every logging event.
	 *
	 * <p>
	 * Instead of {@link #earlyWithString(String, Consumer)} this logs a still to be
	 * constructed message.
	 *
	 * @param name   the stopwatches name
	 * @param logger the string supplier logging operation
	 * @return a {@link LoggingStopwatch} that logs on every logging event
	 */
	public static LoggingStopwatch early(final String name, final Consumer<Supplier<String>> logger) {
		return new LoggingStopwatch(name,
				stopwatch -> StringLoggingStopwatchMode.EARLY.getLogger().accept(stopwatch, logger));
	}

	/**
	 * Creates a {@link LoggingStopwatch} that does <b>not</b> log on every logging
	 * event. Instead stopwatch data is logged after stopping the stopwatch to
	 * reduce the impact of logging from measured times.
	 *
	 * <p>
	 * Instead of {@link #lateWithString(String, Consumer)} this logs a still to be
	 * constructed message.
	 *
	 * @param name   the stopwatches name
	 * @param logger the string supplier logging operation
	 * @return a {@link LoggingStopwatch} that logs on every logging event
	 */
	public static LoggingStopwatch late(final String name, final Consumer<Supplier<String>> logger) {
		return new LoggingStopwatch(name,
				stopwatch -> StringLoggingStopwatchMode.LATE.getLogger().accept(stopwatch, logger));
	}

	/**
	 * Logging operation that consumes a {@link LoggingStopwatch}
	 *
	 * <p>
	 * This operation is called after starting and stopping the stopwatch and after
	 * each checkpoint creation.
	 *
	 * @return the logging operation
	 */
	Consumer<? super LoggingStopwatch> logger;

	/**
	 * Stopwatches name
	 *
	 * @return the stopwatches name
	 */
	String name;

	/**
	 * Object used for locking
	 */
	@ToString.Exclude
	@Getter(AccessLevel.NONE)
	Object lock = new Object();

	/**
	 * Implementation of {@link CloseableStopwatch} meant for logging stopwatch
	 * data.
	 *
	 * <p>
	 * Logging operation that consumes a {@link LoggingStopwatch} {@code logger} is
	 * a logging operation that consumes a {@link LoggingStopwatch}. It is called
	 * after starting and stopping the stopwatch and after each checkpoint creation.
	 *
	 * <p>
	 * See the static factory methods for simplified object creation.
	 *
	 * @param name   the stopwatches name
	 * @param logger the logging operation
	 */
	@SuppressFBWarnings(value = "MC_OVERRIDABLE_METHOD_CALL_IN_CONSTRUCTOR",
			justification = "made sure to call accept after initializing all fields")
	public LoggingStopwatch(final String name, final Consumer<? super LoggingStopwatch> logger) {
		this.logger = logger;
		this.name = name;
		logger.accept(this);
	}

	/** {@inheritDoc} */
	@Override
	public Checkpoint checkpoint(final String name) {
		synchronized (lock) {
			final Checkpoint checkpoint = super.checkpoint(name);
			logger.accept(this);
			return checkpoint;
		}
	}

	/** {@inheritDoc} */
	@Override
	public void close() {
		synchronized (lock) {
			if (!isStopped()) {
				super.close();
				logger.accept(this);
			}
		}
	}

	/**
	 * Default stopwatch logger implementations
	 */
	@Getter
	@RequiredArgsConstructor
	private enum StringLoggingStopwatchMode {
		/**
		 * Early logger implementation that logs on every logging event.
		 */
		EARLY((stopwatch, logger) -> {
			final Optional<Checkpoint> lastCheckpoint = stopwatch.getLastCheckpoint();
			if (stopwatch.isStopped()) {
				if (lastCheckpoint.isPresent()) {
					logStopwatchLastPeriod(stopwatch, logger);
				}
				logStopwatchStopped(stopwatch, logger);
			} else if (lastCheckpoint.isPresent()) {
				logCheckpoint(lastCheckpoint.get(), logger);
			} else {
				logStopwatchStarted(stopwatch, logger);
			}
		}),

		/**
		 * Early logger implementation that does <b>not</b> log on every logging event.
		 * Instead stopwatch data is logged after stopping the stopwatch to reduce the
		 * impact of logging from measured times.
		 */
		LATE((stopwatch, logger) -> {
			if (stopwatch.isStopped()) {
				// Stopwatch: Start
				logStopwatchStarted(stopwatch, logger);

				// Checkpoints
				stopwatch.stream().forEach(checkpoint -> logCheckpoint(checkpoint, logger));

				// Stopwatch: Stop
				if (stopwatch.getLastCheckpoint().isPresent()) {
					logStopwatchLastPeriod(stopwatch, logger);
				}
				logStopwatchStopped(stopwatch, logger);
			}
		});

		/**
		 * Logs {@code checkpoint}.
		 *
		 * @param checkpoint the checkpoint
		 * @param logger     the string supplier logging operation
		 */
		private static void logCheckpoint(final Checkpoint checkpoint, final Consumer<Supplier<String>> logger) {
			logger.accept(() -> Strings.format("Period took %s. Checkpoint \"%s\" reached at %s.",
					checkpoint.sincePrevious().toString().substring(2),
					checkpoint.getName(),
					checkpoint.getInstant()));
		}

		/**
		 * Logs the last periods length.
		 *
		 * <p>
		 * The last period is either from stopwatch start or from the last checkpoint
		 * until the stopwatches stop.
		 *
		 * @param stopwatch the stopwatch
		 * @param logger    the string supplier logging operation
		 */
		private static void logStopwatchLastPeriod(final LoggingStopwatch stopwatch,
				final Consumer<Supplier<String>> logger) {
			logger.accept(() -> Strings.format("Period took %s.", stopwatch.sinceLast().toString().substring(2)));
		}

		/**
		 * Logs the stopwatches start.
		 *
		 * @param stopwatch the stopwatch
		 * @param logger    the string supplier logging operation
		 */
		private static void logStopwatchStarted(final LoggingStopwatch stopwatch,
				final Consumer<Supplier<String>> logger) {
			logger.accept(() -> Strings
					.format("Stopwatch \"%s\" started at %s.", stopwatch.getName(), stopwatch.getStartInstant()));
		}

		/**
		 * Logs the stopwatches stop.
		 *
		 * @param stopwatch the stopwatch
		 * @param logger    the string supplier logging operation
		 */
		private static void logStopwatchStopped(final LoggingStopwatch stopwatch,
				final Consumer<Supplier<String>> logger) {
			logger.accept(() -> Strings.format("Stopwatch \"%s\" stopped after %s.",
					stopwatch.getName(),
					stopwatch.sinceStart().toString().substring(2)));
		}

		/**
		 * Operation that is called on every logging event.
		 */
		private BiConsumer<LoggingStopwatch, Consumer<Supplier<String>>> logger;
	}
}
