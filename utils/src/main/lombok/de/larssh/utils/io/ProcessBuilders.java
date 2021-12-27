package de.larssh.utils.io;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.lang.ProcessBuilder.Redirect.Type;
import java.nio.file.Paths;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.larssh.utils.Nullables;
import de.larssh.utils.SystemUtils;
import de.larssh.utils.annotations.PackagePrivate;
import de.larssh.utils.text.Strings;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link ProcessBuilder}.
 */
@UtilityClass
public class ProcessBuilders {
	/**
	 * Pattern describing all single characters retaining the special meaning of a
	 * backslash when the backslash is followed by this characters inside a Unix
	 * command line arguments.
	 */
	private static final Pattern UNIX_BACKSLASH_SPECIAL_MEANING_CHARACTER_PATTERN = Pattern.compile("[\r\n!\"$\\\\`]");

	/**
	 * Pattern describing Unix command line arguments, which can be safely used
	 * without quoting and escaping.
	 */
	private static final Pattern UNIX_SAFE_ARGUMENT_PATTERN = Pattern.compile("[-.0-9A-Z_a-z]+");

	/**
	 * Pattern describing a single double quote
	 */
	private static final Pattern WINDOWS_DOUBLE_QUOTE_PATTERN = Pattern.compile("\"");

	/**
	 * Pattern describing all single space characters inside Windows command line
	 * arguments, requiring an argument to be escaped.
	 */
	private static final Pattern WINDOWS_SPACE_CHARACTER_PATTERN = Pattern.compile("[ \t\n\u000b]");

	/**
	 * Appends the redirect {@code operation} to {@code file} to {@code builder}.
	 * {@code file} is converted into an absolute path and normalized.
	 *
	 * @param builder   the builder to append to
	 * @param operation the redirect operation
	 * @param file      the path of the redirection
	 */
	private static void appendRedirect(final StringBuilder builder, final String operation, final File file) {
		final String normalizedPath = file.toPath().toAbsolutePath().normalize().toString();
		builder.append(' ')
				.append(operation)
				.append(' ')
				.append(SystemUtils.isWindows()
						? escapeArgumentOnWindows(normalizedPath)
						: escapeArgumentOnUnix(normalizedPath));
	}

	/**
	 * Appends redirect operations of {@code processBuilder} to {@code builder}.
	 *
	 * @param builder        the builder to append to
	 * @param processBuilder the process builder, which information to handle
	 */
	@SuppressWarnings("PMD.CyclomaticComplexity")
	private static void appendRedirects(final StringBuilder builder, final ProcessBuilder processBuilder) {
		// Standard Input
		final Redirect input = processBuilder.redirectInput();
		if (input.type() == Type.READ && input.file() != null) {
			appendRedirect(builder, "<", input.file());
		}

		// Standard Output
		final Redirect output = processBuilder.redirectOutput();
		if (output.type() == Type.APPEND && output.file() != null) {
			appendRedirect(builder, ">>", output.file());
		} else if (output.type() == Type.WRITE && output.file() != null) {
			appendRedirect(builder, ">", output.file());
		}

		// Standard Error
		if (processBuilder.redirectErrorStream()) {
			builder.append(" 2>&1");
		} else {
			final Redirect error = processBuilder.redirectError();
			if (error.type() == Type.APPEND && error.file() != null) {
				appendRedirect(builder, "2>>", error.file());
			} else if (error.type() == Type.WRITE && error.file() != null) {
				appendRedirect(builder, "2>", error.file());
			}
		}
	}

	/**
	 * Escapes {@code argument} as command line argument for Unix.
	 *
	 * <p>
	 * based on <a href=
	 * "https://www.gnu.org/software/bash/manual/bash.html#Double-Quotes">"3.1.2.3
	 * Double Quotes" of the Bash Reference Manual</a>
	 *
	 * @param argument the argument to escape
	 * @return the escaped argument
	 */
	@PackagePrivate
	static String escapeArgumentOnUnix(final String argument) {
		// No need to quote and escape in case of clearly safe characters
		if (Strings.matches(argument, UNIX_SAFE_ARGUMENT_PATTERN)) {
			return argument;
		}

		// Wrap in double quotes and escape characters with special meanings
		return '"' + Strings.replaceAll(argument, UNIX_BACKSLASH_SPECIAL_MEANING_CHARACTER_PATTERN, "\\\\$0") + '"';
	}

	/**
	 * Escapes {@code argument} as command line argument for Windows.
	 *
	 * @param argument the argument to escape
	 * @return the escaped argument
	 */
	@PackagePrivate
	@SuppressWarnings({ "checkstyle:IllegalInstantiation", "checkstyle:XIllegalTypeCustom" })
	static String escapeArgumentOnWindows(final String argument) {
		final StringBuffer buffer = new StringBuffer();

		// The argument needs to be wrapped in double quotes in case of space characters
		final boolean quote = argument.isEmpty() || Strings.find(argument, WINDOWS_SPACE_CHARACTER_PATTERN);
		if (quote) {
			buffer.append('"');
		}

		final Matcher matcher = WINDOWS_DOUBLE_QUOTE_PATTERN.matcher(argument);
		while (matcher.find()) {
			matcher.appendReplacement(buffer, "");

			// In case of a double quote trailing backslashes need to be duplicated and the
			// found double quote needs to be escaped and appended.
			for (int index = buffer.length() - 1; index > -1 && buffer.charAt(index) == '\\'; index -= 1) {
				buffer.append('\\');
			}
			buffer.append("\\\"");
		}
		matcher.appendTail(buffer);

		// In case of quoting trailing backslashes need to be duplicated and the
		// trailing double quote needs to be appended.
		if (quote) {
			for (int index = buffer.length() - 1; index > -1 && buffer.charAt(index) == '\\'; index -= 1) {
				buffer.append('\\');
			}
			buffer.append('"');
		}

		return buffer.toString();
	}

	/**
	 * Returns a command to launch {@code processBuilder} via command line.
	 *
	 * <p>
	 * This method is most likely used for logging and user output or debugging.
	 *
	 * <p>
	 * The results of this method depend on the Operating System as command line
	 * arguments on Unix and Windows are quoted and escaped differently.
	 *
	 * @param processBuilder          the process definition
	 * @param prependWorkingDirectory if {@code true} the working directory of
	 *                                {@code processBuilder} is prepended, else the
	 *                                returned value consists of the command line
	 *                                only.
	 * @return the escaped command
	 */
	public static String toCommandLine(final ProcessBuilder processBuilder, final boolean prependWorkingDirectory) {
		final StringBuilder builder = new StringBuilder();

		// Working Directory
		if (prependWorkingDirectory) {
			builder.append(Nullables.orElseGet(processBuilder.directory(),
					() -> Paths.get(".").toAbsolutePath().normalize().toString())).append("> ");
		}

		// Command and Arguments
		final UnaryOperator<String> escapeArgument = SystemUtils.isWindows()
				? ProcessBuilders::escapeArgumentOnWindows
				: ProcessBuilders::escapeArgumentOnUnix;
		builder.append(processBuilder.command().stream().map(escapeArgument).collect(joining(" ")));

		// Redirects (Standard Input, Output, Error)
		appendRedirects(builder, processBuilder);

		return builder.toString();
	}
}
