# Parent POM
This parent POM is made for great Java projects, handling [Eclipse](https://eclipse.org/downloads/) and optional [Project Lombok](https://projectlombok.org/) integration, having consistent [Checkstyle](https://checkstyle.org/) and [SpotBugs](https://spotbugs.github.io/) (successor of FindBugs) definitions in addition to even more Maven pre-configuration.

Used technologies and main focus are:
* Maven for up-to-date and consistent dependencies
* JUnit and JavaDoc as defacto standards
* Project Lombok to allow writing less boilerplate
* Checkstyle for clean code
* SpotBugs, PMD and CPD for safe code and following best practices
* Eclipse Integration to see warnings as easy as possible
* Until further notice we aim at using Java 8.

Read below descriptions and tips to get started. In case you run into problems [open an issue](https://github.com/lars-sh/parent/issues), in case you'd like to help with this document or one of the submodules feel free to [create pull requests](https://github.com/lars-sh/parent/pulls). There is still much that can be done.

## Preconditions
This POM is made for development using Eclipse Photon or later by handling some of its settings to comply with the projects settings. However you can use this POM together with any other IDE for sure.

## Getting started
When starting a new project all you need to do is following (a) the following generic and (b) one of the below environment specific steps.

1. Create an empty folder. This one will contain your projects data.
2. Create a file called `pom.xml` with the following content. Remember to replace the comments with a valid group ID, artifact ID and the latest `de.lars-sh:parent` version.

	<?xml version="1.0" encoding="UTF-8"?>
	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
		<modelVersion>4.0.0</modelVersion>
	
		<groupId><!-- TODO --></groupId>
		<artifactId><!-- TODO --></artifactId>
	
		<parent>
			<groupId>de.lars-sh</groupId>
			<artifactId>parent</artifactId>
			<version><!-- TODO --></version>
		</parent>
	</project>

### ...with Eclipse
3. In Eclipse choose `File`, `Import...`, `Existing Maven Projects`, `Next`, point the root directory to your newly created folder and press `Finish`.
4. Wait until your workspace is built. In case it does not build automatically, remember to trigger it!
5. **Restart Eclipse** to apply changes to project settings.

### ...with Maven
3. Open a command line and call `mvn package` to test the POM and your infrastructure.

## Ingredients
Taking your first steps using this POM is as simple as not using it. However it comes with a lot nicely pre-configured *ingredients*.

### Project Lombok
Or simply *Lombok* - is a Java library, that aims at minimizing boilerplate code, such as getters and setters or `equals` and `hashCode` without additional runtime dependencies.

#### Getting started
Usage of Project Lombok is highly appreciated as it allows saving a lot boilerplate code to help focusing on the relevant parts of your code. Using this POM you are offered the folder `src/main/lombok` for your Lombok sources.

For existing Java sources you can create the folder `src/main/java`, which is not affected by Lombok. Sources of both folders can operate with each other. The `src/test` folder is structured just the same way.

At compile-time instead of Project Lombok its counterpart [Delombok](https://projectlombok.org/features/delombok) is used to generate Java sources from your Lombok sources inside `target/generated-sources/delombok` in case you need those for yourself to understand what's going on, for debugging purposes, for documentation or even for revision control.

#### First steps
Most Project Lombok features are annotation based. However Lombok comes with some additional functionality making your code even more safe. This POM activates them.

Therefore all non-static fields in your Lombok sources are private and final instead of package-private and non-final. To change the visibility of fields use `public`, `protected` or `@PackagePrivate`. To make a field modifiable use `@NonFinal`.

Project Lombok examples with generated Java code as comment:

	// private final String name;
	String name;

	// private String age;
	@NonFinal
	String age;

#### Configure your IDE
While Project Lombok comes pre-configured for Maven builds you still need to run its installer once to allow your IDE handle Lombok sources beautifully.
1. Therefore execute  `mvn de.lars-sh:jar-runner-maven-plugin:run -Dartifact=org.projectlombok:lombok:LATEST`.
2. A window pops up explaining the installer. The installer might appear damaged on high-DPI monitors and buttons might not be readable. Therefore follow the hints in parenthesis below.
3. In case you do not see your IDE, choose `Specify location...` (bottom left) and select the IDEs path.
4. Check the IDE and click `Install / Update` (bottom right, just above `Quit Installer`).
5. **Restart your IDE**

#### The good parts
Project Lombok consists of many different concepts to avoid boilerplate. The following ones are the most likely to use. See JavaDoc or the [Features page](https://projectlombok.org/features) for further details.

##### @NoArgsConstructor, @RequiredArgsConstructor, @AllArgsConstructor
In case your constructor does not require checks feel free to to use one of the above constructor generating annotations.

##### @Getter, @Setter
The two annotations `@Getter` and `@Setter` can be used to generate getters and setters on either single fields or all fields of a class.

##### @ToString, @EqualsAndHashCode
Writing a `toString` method is either exhausting or requires to bundle external libraries and a lot times writing `equals` and `hashCode` is just the same mess. These annotations handle those cases with no extra work at runtime.

##### @UtilityClass
As Java does not have a real namespace system, all methods need to be part of a class. This leads to helper classes containing only static methods while the *class* shouldn't be instantiated at any time.

Warning: This annotations forces all methods inside its class to be static. In theory that's absolutely fine, in practice that might bring you into trouble. So remember to make your methods static yourself! 

##### @Builder, @Log
I did not play around with these, though they seem to simplify two quite common cases.

#### The bad parts
The following Project Lombok features are disabled on default to minimize your risk.

##### @NonNull
This Lombok annotation adds non-null-checks to method and constructor parameters. Those are simple runtime checks, however we decided to go with compile-time checks as described in the next section.

##### @Cleanup
Use try-with-resource statements instead.

##### @Data, @Value
These annotations both do not have their own implementation. Instead they apply some of the above described annotations to a class. That's all fine.

However people tend to confuse some functionality, while using annotations on their own makes things even more clear to Lombok newbies.

`@Data` is the equivalent to `@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode`. `@Value` is the equivalent to `@Getter @FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE) @AllArgsConstructor @ToString @EqualsAndHashCode`.

##### @Getter(lazy=true)
Use `de.larssh.utils.Finals.lazy(...)` instead.

Usage example:

	// this::expensive is not called on initialization.
	Supplier<T> cached = Finals.lazy(this::expensive);
	
	// Instead this::expensive is called on get.
	cached.get();
	
	// And its result is cached for all following calls of get.
	cached.get();

##### @SneakyThrows
Use `de.larssh.utils.SneakyException` instead.

Usage example:

	try {
		...
	} catch (ExceptionToBeThrownInASneakyWay e) {
		throw new SneakyException(e);
	}

##### var
Use the `var` statement supported by Java 9 and upper.

##### val
This is a pseudo-type similar to the `var` statement, but meant for local variables making them final. It is prohibited to not be confused with the `var` statement. In addition Eclipse is configued to make local variables final while formatting and saving.

### Null Values
When Java was introduced it came with null values, which can be compared to a reference into no-where. It's often used for missing values or simply as additional or special value. Though null values need to be handled by developers, leading to `NullPointerException`s whenever they are not.

Finally Java 8 introduced the class `Optional<T>`. Optionals are simple containers for either the value *empty* or any typed non-null value. Therefore using optional objects force developers, force yourself, to think about edge cases.

#### Null Checks
Talking about non-null-checks often means talking about runtime checks. We decided to go with IDE supported compile-time checks instead of runtime checks to allow code checks while reducing runtime work load.

These compile-time checks work via type annotations introduced below.

##### @NonNullByDefault
As we decided to go with optionals any field or argument can be *non-null by default*. This can be done on package level.

Therefore create a file called `package-info.java` inside your package and insert the following code. That's all.

	@de.larssh.utils.annotations.NonNullByDefault
	package TODO;

In case you forgot to add that annotation Eclipse shows a warning.

##### @Nullable
Add `edu.umd.cs.findbugs.annotations.Nullable` to any method (for its return value) or argument in case it still needs to handle null values. This might be required while handling objects from outside your code or when inheriting classes with nullable values.

### Build Process
Inside your favorite IDE feel free to use its Maven-compatible build infrastrucure, e.g. auto-building in Eclipse. For release artifacts and in case of IDE problems the following Maven commands will help you. More information can be found at the [Maven Lifecycle Reference](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference).

`mvn clean` empties Mavens `target` directory

`mvn install` compiles and packages your project. Afterwards the packaged artifacts are installed to your local repository. Use `mvn package` instead to compile and package without installing or `mvn compile` to compile only.

`mvn test-compile site` runs additional reports on your project. Open `target/site/index.html` to check its results. Note: Executing `mvn site` might result in unexpected behavior. PMD requires compilation together with the site generation.

#### JAR Generation
Wherever possible the following four JAR files are packaged:
* Compiled classes (*.jar)
* Sources (*-source.jar)
* JavaDoc (*-javadoc.jar)
* Unit tests (*-test.jar)

JAR files contain your CHANGELOG.md, README.md and the to-be-created LICENSE.txt file inside their META-INF folder. Those two are meant to be used for documentation. See the below section about generated files for further information.

In addition the JARs manifest includes a generated Class-Path to simplify execution and version information.

Optionally dependencies can be bundled to deploy and run a *full-blown* JAR file. See more information about this at the description of the Maven Property `project.build.packageDependenciesPhase`.

#### Maven Properties
This parent POM either predefines existing Maven Properties or introduces some own.

`project.build.mainClass` is meant to be used by child POMs to configure a default main class, e.g. "de.larssh.Main". Default value: *empty*

`project.build.packageDependenciesPhase` is meant to be used by child POMs to configure if an archive containing dependencies should be created. Values can be `none` (disabled) and `package` (enabled) as this property is used with the <phase> tag. Default value: `none`

`spotbugs.excludeFilterFile` is meant to be used by child POMs to configure a SpotBugs excludes file. Example value: `${project.basedir}/spotbugs-excludes.xml`, Default value: *not set*

	maven.compiler.failOnWarning:     true
	maven.compiler.source:            1.8
	maven.compiler.target:            1.8
	project.build.sourceEncoding:     UTF-8
	project.reporting.outputEncoding: UTF-8

#### Generated Files
During the build process some project files are generated. Those files and their creation concept are described below.

`.gitignore` tells [Git](https://git-scm.com/) which files to ignore. It is overwritten at every run. We plan to change this behaviour in future releases.

`.travis.yml` tells [Travis](https://travis-ci.org/) which kind of project to build. It is created only if it does not exist.

`CHANGELOG.md` and `README.md` are *your* places. Insert your changes, a short project introduction, getting started information and user documentation. Templates are created only if the file does not exist.

`LICENSE.txt` is **not** created. You need to create one yourself! See the section about JAR Generation above for more information.

##### Project Lombok
Project Lombok sources are meant to be used inside `lombok` folders only. Its usage is restricted to prevent you from using functionality that might lead to problems. Outside Project Lombok is prohibited at all.

`src/lombok.config` prohibits Project Lombok usage from any source file. It is overwritten at every run. We plan to change this behaviour in future releases.

`src/main/lombok/lombok.config` and `src/test/lombok/lombok.config` allow some Project Lombok functionality inside those directories. They are overwritten at every run. We plan to change this behaviour in future releases.

##### Eclipse Integration
Our Eclipse Integration mostly synchronizes settings of Maven plugins with your Eclipse settings. Remember to restart Eclipse after building a new or updated child project.

`.checkstyle` contains the Eclipse Checkstyle plugin configuration. It gets overwritten with each update from inside Eclipse.

`.settings/checkstyle.xml` contains the Checkstyle rules. It gets overwritten with each update from inside Eclipse.

`.settings/checkstyle-suppressions.xml` contains the default Checkstyle suppressions. It gets overwritten with each update from inside Eclipse.

`.settings/edu.umd.cs.findbugs.core.prefs` contains the Eclipse SpotBugs Plugin configuration. Some properties are overwritten with each update from inside Eclipse.

`.settings/org.eclipse.core.resources.prefs` contains the default sources encoding. Some properties are overwritten with each update from inside Eclipse.

`.settings/org.eclipse.core.runtime.prefs` contains the default sources line feed. Some properties are overwritten with each update from inside Eclipse.

`.settings/org.eclipse.jdt.core.prefs` contains Compiler and Code Completion configuration and Formatter rules. Some properties are overwritten with each update from inside Eclipse.

`.settings/org.eclipse.jdt.ui.prefs` contains Save Actions and Cleanup configuration. Some properties are overwritten with each update from inside Eclipse.

##### Build Process
The following files are generated for the build process itself. You should not need to know them for your regular work.

`target/pmd/pmd-ruleset.xml` contains the PMD rule set. It is overwritten at the Maven goal `generate-source`.

`target/checkstyle.xml` contains the Checkstyle rules. It is overwritten at the Maven goal `generate-source`.

`target/checkstyle-suppressions.xml` contains the default Checkstyle suppressions. It is overwritten at the Maven goal `generate-source`.

#### Suppress Warnings
As this POM comes with some code check and validation tools you might need to suppress false-positives.

##### Checkstyle
Create a file called `checkstyle-suppressions.xml`. See [SuppressionXPathFilter](http://checkstyle.sourceforge.net/config_filters.html#SuppressionXpathFilter) and [SuppressionFilter](http://checkstyle.sourceforge.net/config_filters.html#SuppressionFilter) for more information. The following lines show an example suppressions file.

	<?xml version="1.0" encoding="UTF-8"?>
	<!DOCTYPE suppressions PUBLIC "-//Checkstyle//DTD SuppressionXpathFilter Experimental Configuration 1.2//EN" "https://checkstyle.org/dtds/suppressions_1_2_xpath_experimental.dtd">
	<suppressions>
		<!-- Allow magic numbers inside static initialization blocks -->
		<suppress-xpath checks="MagicNumberCheck" query="//STATIC_INIT/descendant-or-self::node()" />
		
		<!-- Lombok: Suppress specific unused imports -->
		<suppress checks="UnusedImports" message="^Unused import - lombok\.ToString\.$" />
		
		<!-- Unit Tests -->
		<suppress checks="JavadocPackageCheck" files="^.*[/\\]generated-test-sources[/\\].*$" />
	</suppressions>

##### SpotBugs
More information about SpotBugs warnings can be found at one of the below resources.
* [SpotBugs](https://spotbugs.readthedocs.io/en/latest/bugDescriptions.html)
* [Find Security Bugs](https://find-sec-bugs.github.io/bugs.htm)
* [fb-contrib](http://fb-contrib.sourceforge.net/bugdescriptions.html)

There are two ways to suppress SpotBugs warnings.
* Either go the recommended way by using the `SuppressFBWarnings` annotation as shown below. Inserting a `justification` information is mandatory.

	@SuppressFBWarnings(value = "REC_CATCH_EXCEPTION", justification = "catching any exception at execution root")

* Or add `<spotbugs.excludeFilterFile>${project.basedir}/spotbugs-excludes.xml</spotbugs.excludeFilterFile>` to your Maven Properties and create a file called `spotbugs-excludes.xml`. See [Filter](https://spotbugs.readthedocs.io/en/stable/filter.html) for more information.

##### CPD
Add `<cpd.excludeFromFailureFile>${project.basedir}/cpd-excludes.csv</cpd.excludeFromFailureFile>` to your Maven Properties and create a file called `cpd-excludes.csv`. See [Violation Exclusions](http://maven.apache.org/plugins/maven-pmd-plugin/examples/violation-exclusions.html) for more information. The following lines show an example file.

	com.example.ClassA,com.example.ClassB

##### PMD
There are two ways to suppress PMD warnings.
* Either go the recommended way by using the `java.lang.SuppressWarnings` annotation as shown below.

	@SuppressWarnings("PMD.EmptyCatchBlock")

	@SuppressWarnings({ "PMD.EmptyCatchBlock", "PMD.UnusedPrivateField" })

* Or create a file called `pmd-excludes.properties`. See [Violation Exclusions](http://maven.apache.org/plugins/maven-pmd-plugin/examples/violation-exclusions.html) for more information. The following lines show an example file.

    com.example.ClassA=UnusedPrivateField
    com.example.ClassB=EmptyCatchBlock,UnusedPrivateField

#### Dependencies
We aim at using up-to-date dependencies and Maven plugins and minimizing runtime dependencies while still increasing safety and development ease.

##### Runtime Dependencies
The only runtime dependency (Maven scope: `compile`) used is `de.lars-sh:utils`, which comes with a bunch of generic utilities.

While it's highly dicouraged you might need to remove that dependency in rare cases. Maven does not allow exluding dependencies from parent POMs however you can change the dependencies scope to `test` in such case.

	<dependency>
		<groupId>de.lars-sh</groupId>
		<artifactId>utils</artifactId>
		<scope>test</scope>
	</dependency>

##### Compile-time Dependencies
The following dependencies are used at compile-time (Maven scope: `provided`) only:

* [Lombok Annotations](https://lars-sh/lombok-annotations) (`de.lars-sh:lombok-annotations`)
* [SpotBugs Annotations](https://spotbugs.github.io/) (`com.github.spotbugs:spotbugs-annotations`)
* `de.lars-sh:utils-annotations`

##### Test Dependencies
To simplify writing unit tests the following dependencies are available for unit tests.

* `de.lars-sh:utils-test`
* [jOOR](https://github.com/jOOQ/jOOR) (fluent reflection API, `org.jooq:joor-java-8`)
* [JUnit](https://junit.org/) (`org.junit.jupiter:junt-jupiter-api`, `org.junit.jupiter:junt-jupiter-engine`, `org.junit.platform:junt-platform-runner`)
* [Mockito](https://site.mockito.org/) (`org.mockito:mockito-core`)

### Unit Tests
TODO

#### Jacoco
TODO

### Project Site
TODO

#### Dependencies
TODO

### Checks
TODO

#### Checkstyle, CPD, Spotbugs
TODO: These are run at compile time. Describe Eclipse plugins.

#### PMD
TODO: This is run for `mvn site` only. Describe Eclipse plugin.

### Documentation
TODO: JavaDoc is run at compile time.

### Publishing at Maven Central
TODO: Publishing requires signing your JARs. Follow [Working with GPG Signatures](https://central.sonatype.org/pages/working-with-pgp-signatures.html) as short introduction about signing and [Guide to uploading artifacts to the Central Repository](https://maven.apache.org/repository/guide-central-repository-upload.html) for more generic information.

#### Metadata Example
TODO

## Appendix
In this appendix you can find additional technical sheets, referenced by aboves topics.

### Build Process: Maven Lifecycle
The following shows at which point in the Maven lifecycle plugins do their work.

* clean
    * pre-clean
    * clean
        * maven-clean-plugin:clean (default-clean)
    * post-clean
* default
    * validate
        * jacoco-maven-plugin:prepare-agent (default)
    * initialize
    * generate-sources
        * maven-antrun-plugin:run (gitignore)
        * maven-antrun-plugin:run (project-readme-md-exists)
        * maven-antrun-plugin:run (readme-md)
        * maven-antrun-plugin:run (source-directories)
        * maven-antrun-plugin:run (lombok-config)
        * maven-antrun-plugin:run (checkstyle-xml)
        * maven-antrun-plugin:run (checkstyle-suppressions-xml)
        * maven-antrun-plugin:run (pmd-ruleset-xml)
        * maven-antrun-plugin:run (project-travis-yml-exists)
        * maven-antrun-plugin:run (travis-ci)
        * lombok-maven-plugin:delombok (default)
    * process-sources
    * generate-resources
    * process-resources
        * maven-resources-plugin:resources (default-resources)
    * compile
        * maven-compiler-plugin:compile (default-compile)
    * process-classes
    * generate-test-sources
        * lombok-maven-plugin:testDelombok (default)
    * process-test-sources
    * generate-test-resources
    * process-test-resources
        * maven-resources-plugin:testResources (default-testResources)
    * test-compile
        * maven-compiler-plugin:testCompile (default-testCompile)
    * process-test-classes
    * test
        * maven-surefire-plugin:test (default-test)
    * prepare-package
        * maven-checkstyle-plugin:check (default)
    * package
        * maven-jar-plugin:jar (default-jar)
        * maven-jar-plugin:test-jar (default)
        * maven-source-plugin:jar-no-fork (default)
        * maven-source-plugin:test-jar-no-fork (default)
        * maven-javadoc-plugin:jar (default)
        * maven-javadoc-plugin:test-jar (default)
    * pre-integration-test
    * integration-test
    * post-integration-test
    * verify
        * jacoco-maven-plugin:report (default)
        * maven-gpg-plugin:sign (default)
    * install
        * maven-install-plugin:install (default-install)
    * deploy
        * maven-deploy-plugin:deploy (default-deploy)
* site
    * pre-site
    * site
        * site:site (default-site)
            * maven-checkstyle-plugin:checkstyle
            * spotbugs-maven-plugin:spotbugs
            * maven-pmd-plugin:pmd
            * maven-surefire-report-plugin:report-only
            * maven-dependency-plugin:analyze-report
            * versions-maven-plugin:dependency-updates-report
            * versions-maven-plugin:plugin-updates-report
            * maven-javadoc-plugin:javadoc-no-fork
            * maven-jxr-plugin:jxr
            * maven-jxr-plugin:test-jxr
            * maven-project-info-reports-plugin:dependencies
            * maven-project-info-reports-plugin:dependency-info
            * maven-project-info-reports-plugin:dependency-management
            * maven-project-info-reports-plugin:distribution-management
            * maven-project-info-reports-plugin:index
            * maven-project-info-reports-plugin:licenses
            * maven-project-info-reports-plugin:plugin-management
            * maven-project-info-reports-plugin:plugins
            * maven-project-info-reports-plugin:summary
            * maven-project-info-reports-plugin:team
    * post-site
    * site-deploy
        * site:deploy
