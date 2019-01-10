package de.larssh.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for system needs.
 *
 * <p>
 * Constants have been taken from {@link System#getProperties()} with the
 * following exclusions:
 * <table summary="Excluded Properties">
 * <tr>
 * <th>System Property
 * <th>Direct Representation
 * <th>Representation for technical usage
 * </tr>
 * <tr>
 * <td>{@code file.separator}
 * <td>{@link File#separatorChar}
 * <td>
 * </tr>
 * <tr>
 * <td>{@code path.separator}
 * <td>{@link File#pathSeparatorChar}
 * <td>
 * </tr>
 * <tr>
 * <td>{@code line.separator}
 * <td>{@link System#lineSeparator()}
 * <td>{@link de.larssh.utils.text.Strings#NEW_LINE}
 * </tr>
 * <tr>
 * <td>{@link java.nio.charset.Charset}
 * <td>{@link java.nio.charset.Charset#defaultCharset()}
 * <td>{@link de.larssh.utils.text.Strings#DEFAULT_CHARSET}
 * </tr>
 * <tr>
 * <td>{@link java.util.Locale}
 * <td>{@link java.util.Locale#getDefault()}
 * <td>{@link de.larssh.utils.text.Strings#DEFAULT_LOCALE}
 * </tr>
 * </table>
 */
@UtilityClass
public class SystemUtils {
	/**
	 * Java Runtime Environment version
	 */
	public static final String JAVA_VERSION = "java.version";

	/**
	 * Java Runtime Environment vendor
	 */
	public static final String JAVA_VENDOR = "java.vendor";

	/**
	 * Java vendor URL
	 */
	public static final String JAVA_VENDOR_URL = "java.vendor.url";

	/**
	 * Java installation directory
	 */
	public static final String JAVA_HOME = "java.home";

	/**
	 * Java Virtual Machine specification version
	 */
	public static final String JAVA_VM_SPECIFICATION_VERSION = "java.vm.specification.version";

	/**
	 * Java Virtual Machine specification vendor
	 */
	public static final String JAVA_VM_SPECIFICATION_VENDOR = "java.vm.specification.vendor";

	/**
	 * Java Virtual Machine specification name
	 */
	public static final String JAVA_VM_SPECIFICATION_NAME = "java.vm.specification.name";

	/**
	 * Java Virtual Machine implementation version
	 */
	public static final String JAVA_VM_VERSION = "java.vm.version";

	/**
	 * Java Virtual Machine implementation vendor
	 */
	public static final String JAVA_VM_VENDOR = "java.vm.vendor";

	/**
	 * Java Virtual Machine implementation name
	 */
	public static final String JAVA_VM_NAME = "java.vm.name";

	/**
	 * Java Runtime Environment specification version
	 */
	public static final String JAVA_SPECIFICATION_VERSION = "java.specification.version";

	/**
	 * Java Runtime Environment specification vendor
	 */
	public static final String JAVA_SPECIFICATION_VENDOR = "java.specification.vendor";

	/**
	 * Java Runtime Environment specification name
	 */
	public static final String JAVA_SPECIFICATION_NAME = "java.specification.name";

	/**
	 * Java class format version number
	 */
	public static final String JAVA_CLASS_VERSION = "java.class.version";

	/**
	 * Java class path
	 */
	public static final String JAVA_CLASS_PATH = "java.class.path";

	/**
	 * List of paths to search when loading libraries
	 */
	public static final String JAVA_LIBRARY_PATH = "java.library.path";

	/**
	 * Default temporary file path
	 *
	 * <p>
	 * Better not rely on this in case
	 * {@link java.nio.file.Files#createTempDirectory(Path, String, java.nio.file.attribute.FileAttribute...)}
	 * or
	 * {@link java.nio.file.Files#createTempFile(Path, String, String, java.nio.file.attribute.FileAttribute...)}
	 * are appropriate alternatives.
	 */
	public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

	/**
	 * Name of JIT compiler to use
	 */
	public static final String JAVA_COMPILER = "java.compiler";

	/**
	 * Path of extension directory or directories
	 *
	 * @deprecated This property, and the mechanism which implements it, may be
	 *             removed in a future release.
	 */
	@Deprecated
	public static final String JAVA_EXT_DIRS = "java.ext.dirs";

	/**
	 * Operating system name
	 */
	public static final String OS_NAME = "os.name";

	/**
	 * Operating system architecture
	 */
	public static final String OS_ARCH = "os.arch";

	/**
	 * Operating system version
	 */
	public static final String OS_VERSION = "os.version";

	/**
	 * User's account name
	 *
	 * <p>
	 * Better not rely on this in case {@link #getUserName()} is an appropriate
	 * alternative.
	 */
	public static final String USER_NAME = "user.name";

	/**
	 * User's home directory
	 *
	 * <p>
	 * Better not rely on this in case {@link #getUserHome()} is an appropriate
	 * alternative.
	 */
	public static final String USER_HOME = "user.home";

	/**
	 * User's current working directory
	 */
	public static final String USER_DIR = "user.dir";

	/**
	 * Determines the system specific path to the current JVMs binary executable.
	 *
	 * @return path to JVM executable
	 */
	public static Path getJavaExecutable() {
		return Paths.get(System.getProperty(JAVA_HOME), "bin", "java" + (isWindows() ? ".exe" : ""));
	}

	/**
	 * User's account name
	 *
	 * @return users's account name
	 */
	public static String getUserName() {
		return System.getProperty(USER_NAME);
	}

	/**
	 * User's home directory
	 *
	 * @return users's home directory
	 */
	public static Path getUserHome() {
		return Paths.get(System.getProperty(USER_HOME));
	}

	/**
	 * Validates the system property {@code os.name} and returns true on Windows OS.
	 *
	 * <p>
	 * <b>Usage of feature detection is highly preferred. Use with caution!</b>
	 *
	 * @return {@code true} when running on Windows, otherwise {@code false}
	 */
	public static boolean isWindows() {
		return System.getProperty(OS_NAME).startsWith("Win");
	}
}
