package de.larssh.utils.maven;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Dependency scope is used to limit the transitivity of a dependency, and also
 * to affect the class path used for various build tasks.
 *
 * <p>
 * As described at <a href=
 * "https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html#Dependency_Scope">maven.apache.org</a>.
 */
@Getter
@RequiredArgsConstructor
public enum DependencyScope {
	/**
	 * This is the default scope, used if none is specified. Compile dependencies
	 * are available in all class paths of a project. Furthermore, those
	 * dependencies are propagated to dependent projects.
	 */
	COMPILE("compile"),

	/**
	 * This is much like {@link #COMPILE}, but indicates you expect the JDK or a
	 * container to provide the dependency at runtime. For example, when building a
	 * web application for the Java Enterprise Edition, you would set the dependency
	 * on the Servlet API and related Java EE APIs to scope {@code provided} because
	 * the web container provides those classes. This scope is only available on the
	 * compilation and test class path, and is not transitive.
	 */
	PROVIDED("provided"),

	/**
	 * This scope indicates that the dependency is not required for compilation, but
	 * is for execution. It is in the runtime and test class paths, but not the
	 * compile class path.
	 */
	RUNTIME("runtime"),

	/**
	 * This scope indicates that the dependency is not required for normal use of
	 * the application, and is only available for the test compilation and execution
	 * phases. This scope is not transitive.
	 */
	TEST("test"),

	/**
	 * This scope is similar to {@link #PROVIDED} except that you have to provide
	 * the JAR which contains it explicitly. The artifact is always available and is
	 * not looked up in a repository.
	 */
	SYSTEM("system"),

	/**
	 * This scope is only supported on a dependency of type {@code pom} in the
	 * {@code dependencyManagement} section. It indicates the dependency to be
	 * replaced with the effective list of dependencies in the specified POM's
	 * {@code dependencyManagement} section. Since they are replaced, dependencies
	 * with a scope of {@code import} do not actually participate in limiting the
	 * transitivity of a dependency.
	 */
	IMPORT("import");

	/**
	 * Value inside POM
	 *
	 * @return value inside POM
	 */
	String value;
}
