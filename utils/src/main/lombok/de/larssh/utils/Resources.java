package de.larssh.utils;

import static de.larssh.utils.SystemUtils.DEFAULT_FILE_NAME_SEPARATOR;
import static de.larssh.utils.SystemUtils.DEFAULT_FILE_NAME_SEPARATOR_CHAR;
import static de.larssh.utils.SystemUtils.FILE_EXTENSION_SEPARATOR_CHAR;
import static java.util.Collections.emptyMap;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Optional;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import de.larssh.utils.collection.Enumerations;
import de.larssh.utils.text.Strings;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for loading and handling resources.
 *
 * <p>
 * The commonly used methods to load resources might differ when running from
 * JAR instead of running from local file system. These methods make sure
 * behavior is the same and code works in both situations.
 */
@UtilityClass
public class Resources {
	/**
	 * File extension for Java class files
	 */
	private static final String FILE_EXTENSION_CLASS = "class";

	/**
	 * Pattern to check for a leading previous folder indicator
	 */
	private static final Pattern PATTERN_CHECK_LEASING_PREVIOUS_FOLDER = Pattern.compile("^/?\\.\\.(/|$)");

	/**
	 * Pattern to find and remove duplicate slashes and current folder indicators
	 */
	private static final Pattern PATTERN_FIX_CURRENT_FOLDER = Pattern.compile("/(\\.?/)+");

	/**
	 * Pattern to find the path to a JAR inside an URL string with JAR protocol
	 */
	private static final Pattern PATTERN_JAR_FROM_URL = Pattern.compile("^jar:(?<pathToJar>.*)![^!]*$");

	/**
	 * URL protocol for JAR files (Java archives)
	 */
	private static final String URL_PROTOCOL_JAR = "jar";

	/**
	 * Normalizes and checks {@code resource} as path for accessing resources
	 * safely.
	 *
	 * @param resource the path to normalize and check
	 * @return the normalized path
	 */
	private static String checkAndFixResourcePath(final Path resource) {
		String path = resource.normalize().toString();

		// Normalize the file systems separator
		path = path.replace(resource.getFileSystem().getSeparator(), DEFAULT_FILE_NAME_SEPARATOR);

		// Remove duplicate slashes and current folder indicators
		path = Strings.replaceAll(path, PATTERN_FIX_CURRENT_FOLDER, DEFAULT_FILE_NAME_SEPARATOR);

		// Check for empty path
		if (path.isEmpty()) {
			throw new ResourcePathException("The resource path must not be empty.");
		}

		// Check for root path
		if (DEFAULT_FILE_NAME_SEPARATOR.equals(path)) {
			throw new ResourcePathException("The resource path must not point to root.");
		}

		// Check for leading previous folder indicator
		if (Strings.find(path, PATTERN_CHECK_LEASING_PREVIOUS_FOLDER)) {
			throw new ResourcePathException("The resource path \"%s\" must not point to a location prior root.",
					resource.toString());
		}

		return path;
	}

	/**
	 * Converts {@code url} to a Path object creating a
	 * {@link java.nio.file.FileSystem} if not yet existing.
	 *
	 * @param url URL string
	 * @return path
	 */
	@SuppressWarnings("PMD.PreserveStackTrace")
	private static Path createPath(final String url) {
		final URI uri = URI.create(url);
		try {
			return Paths.get(uri);
		} catch (final FileSystemNotFoundException fileSystemNotFoundException) {
			try {
				FileSystems.newFileSystem(uri, emptyMap());
			} catch (final IOException e) {
				e.addSuppressed(fileSystemNotFoundException);
				throw new UncheckedIOException(e);
			}
			return Paths.get(uri);
		}
	}

	/**
	 * Validates if {@code path} ends with {@code end} case sensitively.
	 *
	 * <p>
	 * In comparison to {@link Path#endsWith(Path)} this method works purely string
	 * based instead of taking the paths {@link java.nio.file.FileSystem} into
	 * account.
	 *
	 * @param path the full path with actual character casing
	 * @param end  the path end as given by the developer
	 * @return {@code true} if {@code path} ends with {@code end} case sensitively,
	 *         else {@code false}
	 */
	@SuppressFBWarnings(value = "EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS",
			justification = "should not happen on regular usage")
	private static boolean endsPathWithCaseSensitive(final Path path, final Path end) {
		Path canonicalPath;
		try {
			canonicalPath = path.toFile().getCanonicalFile().toPath();
		} catch (@SuppressWarnings("unused") final UnsupportedOperationException e) {
			canonicalPath = path;
		} catch (final IOException e) {
			// If an I/O error occurs, which is possible because the construction of the
			// canonical pathname may require file system queries
			throw new UncheckedIOException(e);
		}

		final int pathNames = canonicalPath.getNameCount();
		final int endNames = end.getNameCount();
		if (pathNames < endNames) {
			return false;
		}

		for (int index = endNames - 1; index >= 0; index -= 1) {
			if (!canonicalPath.getName(pathNames - endNames + index).toString().equals(end.getName(index).toString())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the class loader which was used to load {@code clazz} or the system
	 * class loader if {@code clazz} was loaded by the system class loader.
	 *
	 * @param clazz the class
	 * @return either the classes class loader or the system class loader
	 */
	private static ClassLoader getClassLoader(final Class<?> clazz) {
		return Nullables.orElseGet(clazz.getClassLoader(), ClassLoader::getSystemClassLoader);
	}

	/**
	 * Determine the class file that {@code clazz} is defined in. Classes with no
	 * own class file result in {@link Optional#empty()}.
	 *
	 * <p>
	 * This method might load (without closing) a {@link java.nio.file.FileSystem}
	 * to handle a JAR archive.
	 *
	 * <p>
	 * Attention: <a href=
	 * "https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8131067">https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8131067</a>
	 *
	 * @param clazz class
	 * @return path to the the class file
	 */
	public static Optional<Path> getPathToClass(final Class<?> clazz) {
		return getUrlToClass(clazz).map(URL::toString).map(Resources::createPath);
	}

	/**
	 * Determine the JAR file that {@code clazz} is loaded from.
	 *
	 * <p>
	 * Classes outside of a JAR or with no own class file result in
	 * {@link Optional#empty()}.
	 *
	 * @param clazz class
	 * @return path to the the JAR file
	 */
	public static Optional<Path> getPathToJar(final Class<?> clazz) {
		return getUrlToClass(clazz).filter(url -> URL_PROTOCOL_JAR.equalsIgnoreCase(url.getProtocol()))
				.map(URL::toString)
				.map(url -> Strings.replaceFirst(url, PATTERN_JAR_FROM_URL, "${pathToJar}"))
				.map(Resources::createPath);
	}

	/**
	 * Finds the path to {@code resource} using the class loader's resource lookup
	 * algorithm.
	 *
	 * <p>
	 * The commonly used method {@link ClassLoader#getResource(String)} might differ
	 * when running from JAR instead of running from local file system. This method
	 * makes sure behavior is the same and code works in both situations.
	 *
	 * <p>
	 * Calling this might load the {@link java.nio.file.FileSystem} that is
	 * registered for handling JAR files. If no file or folder is found
	 * {@link Optional#empty()} is returned.
	 *
	 * @param classLoader class loader to use for resource lookup
	 * @param resource    path to the resource to find
	 * @return the path to the resource
	 */
	public static Optional<Path> getResource(final ClassLoader classLoader, final Path resource) {
		return Optional.ofNullable(classLoader.getResource(checkAndFixResourcePath(resource)))
				.map(URL::toString)
				.map(Resources::createPath)
				.filter(path -> endsPathWithCaseSensitive(path, resource));
	}

	/**
	 * Finds the path to {@code resource} relative to {@code clazz}.
	 *
	 * <p>
	 * The commonly used method {@link Class#getResource(String)} might differ when
	 * running from JAR instead of running from local file system. This method makes
	 * sure behavior is the same and code works in both situations.
	 *
	 * <p>
	 * Calling this might load the {@link java.nio.file.FileSystem} that is
	 * registered for handling JAR files. If no file or folder is found
	 * {@link Optional#empty()} is returned.
	 *
	 * @param clazz    class to use for resource lookup
	 * @param resource relative path to the resource to find
	 * @return the path to the resource
	 */
	@SuppressFBWarnings(value = "PATH_TRAVERSAL_IN", justification = "processing as described in JavaDoc")
	public static Optional<Path> getResourceRelativeTo(final Class<?> clazz, final Path resource) {
		return getResourceStringToClass(clazz).map(Paths::get)
				.map(Path::getParent)
				.map(path -> path.resolve(resource))
				.flatMap(absoluteResource -> getResource(getClassLoader(clazz), absoluteResource));
	}

	/**
	 * Finds the paths to {@code resource} using the class loader's resource lookup
	 * algorithm. Instead of {@link #getResource(ClassLoader, Path)} this method
	 * returns not only the first matching resource, but also the shadowed resources
	 * based on the class loaders hierarchy.
	 *
	 * <p>
	 * The commonly used method {@link ClassLoader#getResources(String)} might
	 * differ when running from JAR instead of running from local file system. This
	 * method makes sure behavior is the same and code works in both situations.
	 *
	 * <p>
	 * Calling this might load the {@link java.nio.file.FileSystem} that is
	 * registered for handling JAR files. If no file or folder is found
	 * {@link Optional#empty()} is returned.
	 *
	 * @param classLoader class loader to use for resource lookup
	 * @param resource    path to the resource to find
	 * @return the paths of the found resources
	 */
	@SuppressFBWarnings(value = "EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS",
			justification = "converting to unchecked IOException")
	public static Stream<Path> getResources(final ClassLoader classLoader, final Path resource) {
		final Enumeration<URL> enumeration;
		try {
			enumeration = classLoader.getResources(checkAndFixResourcePath(resource));
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}

		return Enumerations.stream(enumeration)
				.map(URL::toString)
				.map(Resources::createPath)
				.filter(path -> endsPathWithCaseSensitive(path, resource));
	}

	/**
	 * Finds the paths to {@code resource} relative to {@code clazz}. Instead of
	 * {@link #getResourceRelativeTo(Class, Path)} this method returns not only the
	 * first matching resource, but also the shadowed resources based on the class
	 * loaders hierarchy.
	 *
	 * <p>
	 * Calling this might load the {@link java.nio.file.FileSystem} that is
	 * registered for handling JAR files. If no file or folder is found
	 * {@link Optional#empty()} is returned.
	 *
	 * @param clazz    class to use for resource lookup
	 * @param resource path to the resource to find
	 * @return the paths of the found resources
	 */
	@SuppressFBWarnings(value = "PATH_TRAVERSAL_IN", justification = "processing as described in JavaDoc")
	public static Stream<Path> getResourcesRelativeTo(final Class<?> clazz, final Path resource) {
		return getResourceStringToClass(clazz).map(Paths::get)
				.map(Path::getParent)
				.map(path -> path.resolve(resource))
				.map(absoluteResource -> getResources(getClassLoader(clazz), absoluteResource))
				.orElseGet(Stream::empty);
	}

	/**
	 * Determine the resource string to the class file that {@code clazz} is defined
	 * in. Classes with probably no own class file result in
	 * {@link Optional#empty()}.
	 *
	 * @param clazz the class
	 * @return resource string to the class file
	 */
	private static Optional<String> getResourceStringToClass(final Class<?> clazz) {
		final String className = clazz.getName();
		return className.indexOf(FILE_EXTENSION_SEPARATOR_CHAR) == -1
				? Optional.empty()
				: Optional.of(className.replace(FILE_EXTENSION_SEPARATOR_CHAR, DEFAULT_FILE_NAME_SEPARATOR_CHAR)
						+ FILE_EXTENSION_SEPARATOR_CHAR
						+ FILE_EXTENSION_CLASS);
	}

	/**
	 * Determine the class file that {@code clazz} is defined in.
	 *
	 * <p>
	 * Classes with no own class file result in {@link Optional#empty()}.
	 *
	 * @param clazz class
	 * @return URL to the the class file
	 */
	private static Optional<URL> getUrlToClass(final Class<?> clazz) {
		return getResourceStringToClass(clazz).map(resource -> getClassLoader(clazz).getResource(resource));
	}

	/**
	 * Reads the {@link Manifest} of the JAR file that {@code clazz} has been loaded
	 * from.
	 *
	 * <p>
	 * Classes outside of a JAR or with no own class file or JARs with no manifest
	 * result in {@link Optional#empty()}.
	 *
	 * @param clazz class
	 * @return manifest
	 * @throws IOException if an I/O error occurs
	 */
	public static Optional<Manifest> readManifest(final Class<?> clazz) throws IOException {
		final Optional<Path> path = getPathToJar(clazz);
		if (!path.isPresent()) {
			return Optional.empty();
		}

		try (JarInputStream inputStream = new JarInputStream(Files.newInputStream(path.get()))) {
			return Optional.ofNullable(inputStream.getManifest());
		}
	}
}
