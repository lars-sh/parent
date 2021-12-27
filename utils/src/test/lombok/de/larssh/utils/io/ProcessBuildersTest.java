package de.larssh.utils.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.larssh.utils.SystemUtils;
import de.larssh.utils.annotations.PackagePrivate;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Test {@link ProcessBuilders}
 */
@Getter
@NoArgsConstructor
public class ProcessBuildersTest {
	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnUnix(String)}
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnUnixEmpty() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnUnix("");

		// then
		assertThat(value).isEqualTo("\"\"");
	}

	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnUnix(String)}
	 *
	 * <p>
	 * inspired by
	 * <a href="https://newbedev.com/escape-command-line-arguments-in-c">"Escape
	 * command line arguments in c#" by newbedev</a>
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnUnixSingleWord() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnUnix("hello");

		// then
		assertThat(value).isEqualTo("hello");
	}

	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnUnix(String)}
	 *
	 * <p>
	 * inspired by
	 * <a href="https://newbedev.com/escape-command-line-arguments-in-c">"Escape
	 * command line arguments in c#" by newbedev</a>
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnUnixBackslashes() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnUnix("\\hello\\12\\3\\");

		// then
		assertThat(value).isEqualTo("\"\\\\hello\\\\12\\\\3\\\\\"");
	}

	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnUnix(String)}
	 *
	 * <p>
	 * inspired by
	 * <a href="https://newbedev.com/escape-command-line-arguments-in-c">"Escape
	 * command line arguments in c#" by newbedev</a>
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnUnixMultipleWords() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnUnix("hello world");

		// then
		assertThat(value).isEqualTo("\"hello world\"");
	}

	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnUnix(String)}
	 *
	 * <p>
	 * inspired by
	 * <a href="https://newbedev.com/escape-command-line-arguments-in-c">"Escape
	 * command line arguments in c#" by newbedev</a>
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnUnixEscapedQuotes() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnUnix("\\\"hello\\\"");

		// then
		assertThat(value).isEqualTo("\"\\\\\\\"hello\\\\\\\"\"");
	}

	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnUnix(String)}
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnUnixEscapedSpecialCharacters() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnUnix("\r\n!\"$\\\\`");

		// then
		assertThat(value).isEqualTo("\"\\\r\\\n\\!\\\"\\$\\\\\\\\\\`\"");
	}

	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnUnix(String)}
	 *
	 * <p>
	 * inspired by
	 * <a href="https://newbedev.com/escape-command-line-arguments-in-c">"Escape
	 * command line arguments in c#" by newbedev</a>
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnUnixEscapedQuotesAndMultipleWords() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnUnix("\\\"hello\\ world");

		// then
		assertThat(value).isEqualTo("\"\\\\\\\"hello\\\\ world\"");
	}

	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnUnix(String)}
	 *
	 * <p>
	 * inspired by
	 * <a href="https://newbedev.com/escape-command-line-arguments-in-c">"Escape
	 * command line arguments in c#" by newbedev</a>
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnUnixDoubleEscapedQuotesAndMultipleWords() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnUnix("\\\"hello\\\\\\ world\\");

		// then
		assertThat(value).isEqualTo("\"\\\\\\\"hello\\\\\\\\\\\\ world\\\\\"");
	}

	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnUnix(String)}
	 *
	 * <p>
	 * inspired by
	 * <a href="https://newbedev.com/escape-command-line-arguments-in-c">"Escape
	 * command line arguments in c#" by newbedev</a>
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnUnixMultipleWordsEndingWithBackslashes() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnUnix("hello world\\\\");

		// then
		assertThat(value).isEqualTo("\"hello world\\\\\\\\\"");
	}

	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnWindows(String)}
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnWindowsEmpty() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnWindows("");

		// then
		assertThat(value).isEqualTo("\"\"");
	}

	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnWindows(String)}
	 *
	 * <p>
	 * based on
	 * <a href="https://newbedev.com/escape-command-line-arguments-in-c">"Escape
	 * command line arguments in c#" by newbedev</a>
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnWindowsSingleWord() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnWindows("hello");

		// then
		assertThat(value).isEqualTo("hello");
	}

	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnWindows(String)}
	 *
	 * <p>
	 * based on
	 * <a href="https://newbedev.com/escape-command-line-arguments-in-c">"Escape
	 * command line arguments in c#" by newbedev</a>
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnWindowsBackslashes() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnWindows("\\hello\\12\\3\\");

		// then
		assertThat(value).isEqualTo("\\hello\\12\\3\\");
	}

	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnWindows(String)}
	 *
	 * <p>
	 * based on
	 * <a href="https://newbedev.com/escape-command-line-arguments-in-c">"Escape
	 * command line arguments in c#" by newbedev</a>
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnWindowsMultipleWords() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnWindows("hello world");

		// then
		assertThat(value).isEqualTo("\"hello world\"");
	}

	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnWindows(String)}
	 *
	 * <p>
	 * based on
	 * <a href="https://newbedev.com/escape-command-line-arguments-in-c">"Escape
	 * command line arguments in c#" by newbedev</a>
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnWindowsEscapedQuotes() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnWindows("\\\"hello\\\"");

		// then
		assertThat(value).isEqualTo("\\\\\\\"hello\\\\\\\"");
	}

	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnWindows(String)}
	 *
	 * <p>
	 * based on
	 * <a href="https://newbedev.com/escape-command-line-arguments-in-c">"Escape
	 * command line arguments in c#" by newbedev</a>
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnWindowsEscapedQuotesAndMultipleWords() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnWindows("\\\"hello\\ world");

		// then
		assertThat(value).isEqualTo("\"\\\\\\\"hello\\ world\"");
	}

	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnWindows(String)}
	 *
	 * <p>
	 * based on
	 * <a href="https://newbedev.com/escape-command-line-arguments-in-c">"Escape
	 * command line arguments in c#" by newbedev</a>
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnWindowsDoubleEscapedQuotesAndMultipleWords() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnWindows("\\\"hello\\\\\\ world\\");

		// then
		assertThat(value).isEqualTo("\"\\\\\\\"hello\\\\\\ world\\\\\"");
	}

	/**
	 * Test {@link ProcessBuilders#escapeArgumentOnWindows(String)}
	 *
	 * <p>
	 * based on
	 * <a href="https://newbedev.com/escape-command-line-arguments-in-c">"Escape
	 * command line arguments in c#" by newbedev</a>
	 */
	@Test
	@PackagePrivate
	void testEscapeArgumentOnWindowsMultipleWordsEndingWithBackslashes() {
		// given

		// when
		final String value = ProcessBuilders.escapeArgumentOnWindows("hello world\\\\");

		// then
		assertThat(value).isEqualTo("\"hello world\\\\\\\\\"");
	}

	/**
	 * Test {@link ProcessBuilders#toCommandLine()}
	 */
	@Test
	@PackagePrivate
	void testToCommandLineUnixWithoutWorkingDirectory() {
		// given
		final ProcessBuilder processBuilder = new ProcessBuilder("a", "b\\ \"c");

		try (MockedStatic<?> mockedSystemUtils = mockStatic(SystemUtils.class)) {
			mockedSystemUtils.when(SystemUtils::isWindows).thenReturn(false);

			// when
			final String value = ProcessBuilders.toCommandLine(processBuilder, false);

			// then
			assertThat(value).isEqualTo("a \"b\\\\ \\\"c\"");
		}
	}

	/**
	 * Test {@link ProcessBuilders#toCommandLine()}
	 */
	@Test
	@PackagePrivate
	void testToCommandLineUnixWithWorkingDirectory() {
		// given
		final ProcessBuilder processBuilder = new ProcessBuilder("a");

		try (MockedStatic<?> mockedSystemUtils = mockStatic(SystemUtils.class)) {
			mockedSystemUtils.when(SystemUtils::isWindows).thenReturn(false);

			// when
			final String value = ProcessBuilders.toCommandLine(processBuilder, true);

			// then
			final Path workingDirectory = Paths.get(".").toAbsolutePath().normalize();
			assertThat(value).isEqualTo(workingDirectory.toString() + "> a");
		}
	}

	/**
	 * Test {@link ProcessBuilders#toCommandLine()}
	 */
	@Test
	@PackagePrivate
	void testToCommandLineUnixWithStandardInput() {
		// given
		final ProcessBuilder processBuilder = new ProcessBuilder("a");
		final Path input = Paths.get(".");
		processBuilder.redirectInput(input.toFile());

		try (MockedStatic<?> mockedSystemUtils = mockStatic(SystemUtils.class)) {
			mockedSystemUtils.when(SystemUtils::isWindows).thenReturn(false);

			// when
			final String value = ProcessBuilders.toCommandLine(processBuilder, false);

			// then
			final String expected = "a < ";
			assertThat(value).startsWith(expected).hasSizeGreaterThan(expected.length());
		}
	}

	/**
	 * Test {@link ProcessBuilders#toCommandLine()}
	 */
	@Test
	@PackagePrivate
	void testToCommandLineUnixWithStandardOutput() {
		// given
		final ProcessBuilder processBuilder = new ProcessBuilder("a");
		final Path output = Paths.get(".");
		processBuilder.redirectOutput(output.toFile());

		try (MockedStatic<?> mockedSystemUtils = mockStatic(SystemUtils.class)) {
			mockedSystemUtils.when(SystemUtils::isWindows).thenReturn(false);

			// when
			final String value = ProcessBuilders.toCommandLine(processBuilder, false);

			// then
			final String expected = "a > ";
			assertThat(value).startsWith(expected).hasSizeGreaterThan(expected.length());
		}
	}

	/**
	 * Test {@link ProcessBuilders#toCommandLine()}
	 */
	@Test
	@PackagePrivate
	void testToCommandLineUnixWithStandardError() {
		// given
		final ProcessBuilder processBuilder = new ProcessBuilder("a");
		final Path error = Paths.get(".");
		processBuilder.redirectError(error.toFile());

		try (MockedStatic<?> mockedSystemUtils = mockStatic(SystemUtils.class)) {
			mockedSystemUtils.when(SystemUtils::isWindows).thenReturn(false);

			// when
			final String value = ProcessBuilders.toCommandLine(processBuilder, false);

			// then
			final String expected = "a 2> ";
			assertThat(value).startsWith(expected).hasSizeGreaterThan(expected.length());
		}
	}

	/**
	 * Test {@link ProcessBuilders#toCommandLine()}
	 */
	@Test
	@PackagePrivate
	void testToCommandLineUnixWithRedirectedErrorStream() {
		// given
		final ProcessBuilder processBuilder = new ProcessBuilder("a");
		processBuilder.redirectErrorStream(true);

		try (MockedStatic<?> mockedSystemUtils = mockStatic(SystemUtils.class)) {
			mockedSystemUtils.when(SystemUtils::isWindows).thenReturn(false);

			// when
			final String value = ProcessBuilders.toCommandLine(processBuilder, false);

			// then
			assertThat(value).isEqualTo("a 2>&1");
		}
	}

	/**
	 * Test {@link ProcessBuilders#toCommandLine()}
	 */
	@Test
	@PackagePrivate
	void testToCommandLineWindows() {
		// given
		final ProcessBuilder processBuilder = new ProcessBuilder("a", "b\\ \"c");

		try (MockedStatic<?> mockedSystemUtils = mockStatic(SystemUtils.class)) {
			mockedSystemUtils.when(SystemUtils::isWindows).thenReturn(true);

			// when
			final String value = ProcessBuilders.toCommandLine(processBuilder, true);

			// then
			final Path workingDirectory = Paths.get(".").toAbsolutePath().normalize();
			assertThat(value).isEqualTo(workingDirectory.toString() + "> a \"b\\ \\\"c\"");
		}
	}
}
