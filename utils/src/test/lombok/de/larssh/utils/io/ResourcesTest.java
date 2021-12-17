package de.larssh.utils.io;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import lombok.NoArgsConstructor;

/**
 * Tests for {@link Resources}
 */
@NoArgsConstructor
public class ResourcesTest {
	/**
	 * Test {@link Resources#getResourceRelativeTo(Class, Path)} with a path
	 * starting with point-relative path.
	 */
	@Test
	public void testGetResourceRelativeTo_pointRelativePath() {
		// given
		final Class<?> clazz = getClass();
		final Path resource = Paths.get("./Resources_testGetResourceRelativeTo_pointRelativePath.txt");

		// when
		final Optional<Path> path = Resources.getResourceRelativeTo(clazz, resource);

		// then
		assertThat(path).isPresent();
	}
}
