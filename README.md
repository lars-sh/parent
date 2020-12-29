# Parent POM
This parent POM is made for great Java projects, handling [Eclipse](https://eclipse.org/downloads/) (partly IntelliJ IDEA) and optionally [Project Lombok](https://projectlombok.org/) integration, having consistent [Checkstyle](https://checkstyle.org/) and [SpotBugs](https://spotbugs.github.io/) (successor of FindBugs) definitions in addition to even more Maven pre-configuration.

[Changelog](CHANGELOG.md)  |  [JavaDoc](https://lars-sh.github.io/parent/apidocs)  |  [Generated Reports](https://lars-sh.github.io/parent/project-reports.html)

Used technologies and main focus are:
* Maven for up-to-date and consistent dependencies
* JUnit and JavaDoc as defacto standards
* Project Lombok to allow writing less boilerplate
* Checkstyle and formatter for clean code
* SpotBugs, PMD and CPD for safe code and following best practices
* Eclipse (and partly IntelliJ IDEA) integration to see warnings as easy as possible
* Until further notice we aim at code compatibility with Java 8.

Read below descriptions and tips to get started. In case you run into problems [open an issue](https://github.com/lars-sh/parent/issues), in case you'd like to help with this document or one of the submodules feel free to [create pull requests](https://github.com/lars-sh/parent/pulls). There is still much that can be done.

## Preconditions
This POM is made for development using Maven and Eclipse by handling some of its settings to comply with the projects settings. However you can use this POM together with any other IDE (such as IntelliJ IDEA) for sure.

## Getting started
### With a new Project
Creating a new project has been simplified as much as possible.

1. Open the command line of your choice and navigate to the place where you'd like to create a new project folder.
2. Run the following command to create your new projects folder and a `pom.xml` file pointing at the Parent POM project.

```Shell
mvn archetype:generate -DarchetypeGroupId=de.lars-sh -DarchetypeArtifactId=parent-archetype
```

3. Your artifacts will be signed digitally. Follow the configuration instructions at [Working with GPG Signatures](https://central.sonatype.org/pages/working-with-pgp-signatures.html) to set up your personal GPG keys.
4. Change into the new projects directory and run `mvn verify` to test the POM and your infrastructure.

### With an existing Project
1. Here's a Maven parent example:

```XML
<parent>
	<groupId>de.lars-sh</groupId>
	<artifactId>parent</artifactId>
	<version><!-- TODO --></version>
	<relativePath></relativePath>
</parent>
```

2. Your artifacts will be signed digitally. Follow the configuration instructions at [Working with GPG Signatures](https://central.sonatype.org/pages/working-with-pgp-signatures.html) to set up your personal GPG keys.
3. Change into the projects directory and run `mvn verify` to test the POM and your infrastructure.

Remember to **restart Eclipse** to apply changes to project settings.

### Import into Eclipse
1. If not done earlier, install Project Lombok into Eclipse [using the official installer](https://projectlombok.org/setup/eclipse) by calling `mvn de.lars-sh:jar-runner-maven-plugin:run -Dartifact=org.projectlombok:lombok:LATEST` on the command line.

2. Run `mvn initialize -P update-eclipse` via command line to initialize the Eclipse settings.
3. In Eclipse choose `File`, `Import...`, `Existing Maven Projects` and press `Next`.
4. Point the root directory to your newly created folder and press `Finish`.

#### Solve Problems
* In case of a problem update the Maven project (`Alt + F5`) to synchronize the Eclipse configuration with your `pom.xml`.
* Wait until your workspace is built. In case it does not build automatically, remember to trigger it!

### Open with IntelliJ IDEA
1. If not done earlier, [install the Lombok plugin](https://projectlombok.org/setup/intellij).
2. Open your project inside IntelliJ IDEA.
3. Set the `Generated sources folders` setting to `Don't detect`.

#### Solve Problems
* In case of a problem reimport the Maven project to synchronize the IDEA settings with your `pom.xml`.
* In case of a "duplicate classes" error rightclick the folder `target/generated-sources/delombok` and select `Mark Directory as`, `Unmark as Sources Root` to remove it from the class path.

#### Code Formatter
There are multiple ways to set up the formatter as defined by this Parent POM. You might use the built-in formatter as described below or take the [Eclipse Code Formatter plugin](https://plugins.jetbrains.com/plugin/6546) (recommended).

##### Internal Code Formatter
1. Choose `File`, `Settings...`
2. Open the tree element `Editor`, `Code Style`, `Java`.
3. Click the gear beneath the `Scheme` selection and choose `Import Scheme`, `Eclipse XML Profile`.
4. Open the project directory and select `target/formatter.xml`. Press `OK`.

Remark: When saving the formatter in IntelliJ IDEA you might get `Cannot Save Settings`. In that case some Eclipse formatter settings are not compatible with IntelliJ IDEA and need to be handled manually. Therefore go through each of the tabs and fix the boxes highlighted with a red border.

#### Save Actions
Even some of the predefined Save Actions can be configured inside IntelliJ IDEA through the [Save Actions plugin](https://plugins.jetbrains.com/plugin/7642).

### Skip Validations
Upgrading existing projects to use this parent POM can be done step by step. As this parent specifies some strict rules, some validations might need to be skipped until others pass. The following sections describe the corresponding Maven Properties.

#### Dirty
Skip the below checks and tests.

```
-P dirty
```

##### Skip Tests
```XML
<skipTests>true</skipTests>
```

For projects of packaging type `maven-archetype` tests can be skipped using:
```XML
<archetype.test.skip>true</archetype.test.skip>
```

##### Skip Checkstyle
```XML
<checkstyle.skip>true</checkstyle.skip>
```

##### Skip Formatter
```XML
<formatter.skip>true</formatter.skip>
```

##### Skip JaCoCo
```XML
<jacoco.skip>true</jacoco.skip>
```

##### Skip CPD
```XML
<cpd.skip>true</cpd.skip>
```

##### Skip PMD
```XML
<pmd.skip>true</pmd.skip>
```

##### Skip SpotBugs
```XML
<spotbugs.skip>true</spotbugs.skip>
```

##### Skip Dependency Analysis
```XML
<mdep.analyze.skip>true</mdep.analyze.skip>
```

##### Skip Tidy
```XML
<tidy.skip>true</tidy.skip>
```

#### Dirty Package
Skip the creation of optional packages and signing.

```
-P dirty-package
```

##### Skip Sources
```XML
<maven.source.skip>true</maven.source.skip>
```

##### Skip JavaDoc
```XML
<maven.javadoc.skip>true</maven.javadoc.skip>
```

##### Skip GPG Signing
```XML
<gpg.skip>true</gpg.skip>
```

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

```Java
// private final String name;
String name;

// private String age;
@NonFinal
String age;
```

#### Configure your IDE
While Project Lombok comes pre-configured for Maven builds you still need to run its installer once to allow your IDE handle Lombok sources beautifully.
1. Therefore execute `mvn de.lars-sh:jar-runner-maven-plugin:run -Dartifact=org.projectlombok:lombok:LATEST`.
2. A window pops up explaining the installer. The installer might appear damaged on high-DPI monitors and buttons might not be readable. Therefore follow the hints in parenthesis below.
3. In case you do not see your IDE, choose `Specify location...` (bottom left) and select the IDEs path.
4. Check the IDE and click `Install / Update` (bottom right, just above `Quit Installer`).
5. **Restart your IDE**

#### Excluding Project Lombok
In the rare case that you might explicitely avoid using lombok add the following properties to your Maven configuration.

```XML
<parent-pom.create-lombok-config>false</parent-pom.create-lombok-config>
<parent-pom.default-sources-folder>java</parent-pom.default-sources-folder>
```

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

##### @Log
I did not play around with these, though they seem to simplify two quite common cases.

#### The bad parts
The following Project Lombok features are disabled on default to minimize your risk.

##### @Builder
The annotation `@Builder` is a great helper in writing builder classes. While its compatibility with null checks has been improved greatly, it still fails in Eclipse and IntelliJ IDEA and therefore is not recommended to be used.

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

```Java
// this::expensive is not called on initialization.
Supplier<T> cached = Finals.lazy(this::expensive);

// Instead this::expensive is called on get.
cached.get();

// And its result is cached for all following calls of get.
cached.get();
```

##### @SneakyThrows
Use `de.larssh.utils.SneakyException` instead.

Usage example:

```Java
try {
	...
} catch (ExceptionToBeThrownInASneakyWay e) {
	throw new SneakyException(e);
}
```

##### val
This is a pseudo-type similar to the `var` statement, but meant for local variables making them final. It is prohibited to not be confused with the `var` statement. In addition Eclipse is configured to make local variables final while formatting and saving.

##### var
Use the `var` statement supported by Java 9 and later.

### Null Values
When Java was introduced it came with null values, which can be compared to a reference into no-where. It's often used for missing values or simply as additional or special value. Though null values need to be handled by developers, leading to `NullPointerException`s whenever they are not.

Finally Java 8 introduced the class `Optional<T>`. Optionals are simple containers for either the value *empty* or any typed non-null value. Therefore using optional objects force developers, force yourself, to think about edge cases.

#### Null Checks
Talking about non-null-checks often means talking about runtime checks. We decided to go with IDE supported compile-time checks instead of runtime checks to allow code checks while reducing runtime work load.

These compile-time checks work via type annotations introduced below.

##### @NonNullByDefault
As we decided to go with optionals any field or argument can be *non-null by default*. This can be done on package level.

Therefore create a file called `package-info.java` inside your package and insert the following code. That's all.

```Java
@de.larssh.utils.annotations.NonNullByDefault
package ...;
```

In case you forgot to add that annotation Eclipse shows a warning.

##### @Nullable
Add `edu.umd.cs.findbugs.annotations.Nullable` to any method (for its return value) or argument in case it still needs to handle null values. This might be required while handling objects from outside your code or when inheriting classes with nullable values.

### Build Process
Inside your favorite IDE feel free to use its Maven-compatible build infrastrucure, e.g. auto-building in Eclipse. For release artifacts and in case of IDE problems the following Maven commands will help you. More information can be found at the [Maven Lifecycle Reference](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference).

`mvn clean` empties Mavens `target` directory

`mvn install` compiles and packages your project. Afterwards the packaged artifacts are installed to your local repository. Use `mvn verify` instead to compile and package without installing or `mvn compile` to compile only.

`mvn site` runs additional reports on your project. Open `target/site/index.html` to check its results afterwards.

#### JAR Generation
Wherever possible the following four JAR files are packaged:
* Compiled classes (*.jar)
* Sources (*-source.jar)
* JavaDoc (*-javadoc.jar)
* Unit tests (*-test.jar)

JAR files contain your CHANGELOG.md, README.md and the to-be-created LICENSE.txt file inside their META-INF folder. Those two are meant to be used for documentation. See the below section about generated files for further information.

In addition the JARs manifest includes a generated Class-Path to simplify execution and version information.

Optionally dependencies can be bundled to deploy and run a *full-blown* JAR file. See more information about this at the description of the Maven Property `project.build.packageDependenciesPhase`.

#### Maven Profiles
The following profiles can be activated by hand to handle some rare cases.

`-P dirty` skips code checks and tests

`-P dirty-package` skips the creation of optional packages

Additional profiles might be activated based on the build environment to guarantee compatibility.

#### Maven Properties
This parent POM either predefines existing Maven Properties or introduces some own.

`parent-pom.create-changelog-md` handles if the projects `CHANGELOG.md` file should be generated. Set to `false` if the file should not be created if not existing. Default value is `true`, except if the `CHANGELOG.md` file already exists.

`parent-pom.create-dependabot-yml` handles if the projects `.github/dependabot.yml` file should be generated. Set to `false` if the file should not be created or overwritten. Default value is `true`.

`parent-pom.create-gitignore` handles if the projects `.gitignore` file should be generated. Set to "false" if the file should not be created or overwritten. Default value is `true`.

`parent-pom.create-lombok-config` handles if the projects `lombok.config` files should be generated. Set to `false` if the files should not be created or overwritten. Default value is `true`, except for packaging types `pom` and `archetype`.

`parent-pom.create-readme-md` handles if the projects `README.md` file should be generated. Set to `false` if the file should not be created if not existing. Default value is `true`, except if the `README.md` file already exists.

`parent-pom.create-source-directories` handles if the projects source directories shall be created. Set to `false` if the folder should not be created. Default value is `true`, except for packaging types `pom` and `archetype`.

`parent-pom.create-travis-yml` handles if the projects `.travis.yml` file should be generated. Set to `false` if the file should not be created or overwritten. Default value is `true`.

`parent-pom.default-sources-folder` is the name if the default source folders to be created. Default value is `lombok`.

`parent-pom.github.organization` is the GitHub organization name, used for documentary needs. Default value is `lars-sh`.

`parent-pom.github.project` is the GitHub project name, used for documentary needs. Default value is `${project.artifactId}`.

`eclipse.compiler.codegen.methodParameters` configures Eclipse to respect the value specified for `maven.compiler.parameters`. Values can be `generate` or `do not generate`. Default value: `do not generate`, except if property `maven.compiler.parameters` equals `true`.

`eclipse.compiler.javadoc` is meant to be used by child POMs to configure if Eclipse should validate JavaDoc comments. Values can be `enabled` or `disabled`. Default value: `enabled`

`eclipse.compiler.missingNonNullByDefaultAnnotation` is meant to be used by child POMs to configure if Eclipse should enforce @NonNullByDefault annotations on every package. Values can be `error`, `warning`, `info` or `ignore` (disabled). Default value: `warning`

`eclipse.compiler.nonnull.secondary` is meant to be used by child POMs to configure secondary @NonNull annotations that Eclipse should handle. Multiple values must be separated by comma. Default value: *empty*

`eclipse.compiler.nonnullbydefault.secondary` is meant to be used by child POMs to configure secondary @NonNullByDefault annotations that Eclipse should handle. Multiple values must be separated by comma. Default value: *empty*

`eclipse.compiler.nullAnalysis` is meant to be used by child POMs to configure if Eclipse should do annotation based null analysis. Values can be `enabled` or `disabled`. Default value: `enabled`

`eclipse.compiler.nullable.secondary` is meant to be used by child POMs to configure secondary @Nullable annotations that Eclipse should handle. Multiple values must be separated by comma. Default value: `edu.umd.cs.findbugs.annotations.CheckForNull`

`eclipse.compiler.potentialNullReference` is meant to be used by child POMs to configure if Eclipse should warn on potential null pointer access. Values can be `error`, `warning`, `info` or `ignore` (disabled). Default value: `error`

`jar.manifest.mainClass` is meant to be used by child POMs to configure a default main class, e.g. "de.larssh.Main". Default value: *empty*

`shade.packageDependenciesPhase` is meant to be used by child POMs to configure if an archive containing dependencies should be created. Values can be `none` (disabled) and `package` (enabled) as this property is used with the <phase> tag. Default value: `none`

```
aggregate:                                         true
checkstyle.config.location:                        ${project.build.directory}/checkstyle.xml
checkstyle.consoleOutput:                          true
cpd.aggregate:                                     false
cpd.excludeFromFailureFile:                        ${project.basedir}/cpd-excludes.csv (if existing)
cpd.printFailingErrors:                            true
dependency.failOnWarning:                          true
enforcer.requiredMavenVersion:                     3.3.9
formatter.configFile:                              ${project.build.directory}/formatter.xml
formatter.lineEnding:                              LF
jar.manifest.addClasspath:                         true
jar.manifest.addDefaultImplementationEntries:      true
jar.manifest.addDefaultSpecificationEntries:       true
jar.manifest.mainClass:                            
jar.skipIfEmpty:                                   true
javadoc.quiet:                                     true
maven.compiler.failOnWarning:                      true
maven.compiler.showDeprecation:                    true
maven.compiler.showWarnings:                       true
maven.compiler.source:                             1.8
maven.compiler.target:                             1.8
maven.javadoc.failOnWarnings:                      true
maven.version.rules:                               file:///${project.build.directory}/versions-ruleset.xml
nexus-staging.autoReleaseAfterClose:               true
pmd.aggregate:                                     false
pmd-only.excludeFromFailureFile:                   ${project.basedir}/pmd-excludes.properties
pmd-only.printFailingErrors:                       true
pmd-only.ruleset:                                  ${project.build.directory}/pmd-ruleset.xml
pmd.analysisCache:                                 true
project.build.sourceEncoding:                      UTF-8
project.reporting.outputEncoding:                  UTF-8
shade.dependencyReducedPomLocation:                ${project.build.directory}/dependency-reduced-pom.xml
shade.packageDependenciesPhase:                    none
spotbugs.effort:                                   Max
spotbugs.threshold:                                Low
```

#### Generated Files
During the build process some project files are generated. Those files and their creation concept are described below.

`.github/dependabot.yml` tells [Dependabot](https://dependabot.com/) which project dependencies to check. Use the Maven property `parent-pom.create-dependabot-yml` to suppress writing this file.

`.gitignore` tells [Git](https://git-scm.com/) which files to ignore. It is overwritten at every run to keep it up-to-date. Use the Maven property `parent-pom.create-gitignore` to suppress writing this file.

`.travis.yml` tells [Travis](https://travis-ci.org/) which kind of project to build. It is overwritten at every run to keep it up-to-date. Use the Maven property `parent-pom.create-travis-yml` to suppress writing this file.

`CHANGELOG.md` and `README.md` are *your* places. Insert your changes, a short project introduction, getting started information and user documentation. Templates are created only if the files do not exist, yet.

`LICENSE.txt` is **not** created. You need to create one yourself! See the section about JAR Generation for more information.

##### Project Lombok
Project Lombok sources are meant to be used inside `lombok` folders only. Its usage is restricted to prevent you from using functionality that might lead to problems. Outside Project Lombok is prohibited at all.

`src/lombok.config` prohibits Project Lombok usage from any source file. It is overwritten at every run. We plan to change this behaviour in future releases.

`src/main/lombok/lombok.config` and `src/test/lombok/lombok.config` allow some Project Lombok functionality inside those directories. They are overwritten at every run. We plan to change this behaviour in future releases.

##### Eclipse Integration
Our Eclipse Integration mostly synchronizes settings of Maven plugins with your Eclipse settings. Remember to restart Eclipse after building a new or updated child project.

`.checkstyle` contains the Eclipse Checkstyle plugin configuration. It gets overwritten with each update from inside Eclipse.

`.settings/checkstyle.xml` contains the Checkstyle rules. It gets overwritten with each update from inside Eclipse.

`.settings/edu.umd.cs.findbugs.core.prefs` contains the Eclipse SpotBugs Plugin configuration. Some properties are overwritten with each update from inside Eclipse.

`.settings/org.eclipse.core.resources.prefs` contains the default sources encoding. Some properties are overwritten with each update from inside Eclipse.

`.settings/org.eclipse.core.runtime.prefs` contains the default sources line feed. Some properties are overwritten with each update from inside Eclipse.

`.settings/org.eclipse.jdt.core.prefs` contains Compiler and Code Completion configuration and Formatter rules. Some properties are overwritten with each update from inside Eclipse.

`.settings/org.eclipse.jdt.ui.prefs` contains Save Actions and Cleanup configuration. Some properties are overwritten with each update from inside Eclipse.

##### Build Process
The following files are generated for the build process itself. You should not need to know them for your regular work.

`target/checkstyle.xml` contains the Checkstyle rules. It is overwritten at the Maven goal `initialize`.

`target/formatter.xml` contains the formatting rules. It is overwritten at the Maven goal `initialize`.

`target/pmd/pmd-ruleset.xml` contains the PMD rule set. It is overwritten at the Maven goal `initialize`.

`target/spotbugs-excludes-fix-jdk11-and-later.xml` contains Spotbugs eclusions for compatibility with JDK11 and later. It is overwritten at the Maven goal `initialize` while executing Maven with Java 11 and later.

`target/travis-suppressions-parent.sh` contains a script that filters the Maven errors and warnings output when processed by Travis CI based on a list of global suppressions. It is overwritten at the Maven goal `initialize`.

`target/versions-ruleset.xml` contains a rule set used by the Maven Versions Plugin to ignore pre-release versions. It is overwritten at the Maven goal `initialize`.

#### Suppress Warnings
As this POM comes with some code check and validation tools you might need to suppress false-positives.

##### Checkstyle
There are two ways to suppress Checkstyle warnings.
* Either go the recommended way by using the `SuppressWarnings` annotation as shown below.

```Java
@SuppressWarnings("checkstyle:MagicNumber")
```

* Or create a file called `checkstyle-suppressions.xml`. See [SuppressionXPathFilter](https://checkstyle.org/config_filters.html#SuppressionXpathFilter) and [SuppressionFilter](https://checkstyle.org/config_filters.html#SuppressionFilter) for more information. The following lines show an example suppressions file.

```XML
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suppressions PUBLIC "-//Checkstyle//DTD SuppressionXpathFilter Experimental Configuration 1.2//EN" "https://checkstyle.org/dtds/suppressions_1_2_xpath_experimental.dtd">
<suppressions>
	<!-- Allow magic numbers inside static initialization blocks -->
	<suppress-xpath checks="MagicNumberCheck" query="//STATIC_INIT/descendant-or-self::node()" />
	
	<!-- Lombok: Suppress specific unused imports -->
	<suppress checks="UnusedImports" message="^Unused import - lombok\.ToString\.$" />
	
	<!-- Unit Tests -->
	<suppress checks="JavadocPackage" files="^.*[/\\]generated-test-sources[/\\].*$" />
</suppressions>
```

##### SpotBugs
More information about SpotBugs warnings can be found at one of the below resources.
* [SpotBugs](https://spotbugs.readthedocs.io/en/latest/bugDescriptions.html)
* [Find Security Bugs](https://find-sec-bugs.github.io/bugs.htm)
* [fb-contrib](http://fb-contrib.sourceforge.net/bugdescriptions.html)

There are two ways to suppress SpotBugs warnings.
* Either go the recommended way by using the `SuppressFBWarnings` annotation as shown below. Inserting a `justification` information is mandatory.

```Java
@SuppressFBWarnings(value = "REC_CATCH_EXCEPTION", justification = "catching any exception at execution root")
```

* Or create a file called `spotbugs-excludes.xml`. See [Filter](https://spotbugs.readthedocs.io/en/stable/filter.html) for more information.

##### CPD
Create a file called `cpd-excludes.csv`. See [Violation Exclusions](https://maven.apache.org/plugins/maven-pmd-plugin/examples/violation-exclusions.html) for more information. The following lines show an example file.

```CSV
com.example.ClassA,com.example.CopyOfClassA
com.example.ClassB,com.example.CopyOfClassB
```

Remark: While the notation inside the CPD exclude files seems class-path-alike, matching is based on the scheme `pathOfFileWithDuplicate.replace('/', '.').replace('\\', '.').contains(classPathAlikeExclude)`. Therefore subclasses cannot be matched in an exact manner. Use the files path (replacing slashes with dots) instead.

##### PMD
There are two ways to suppress PMD warnings.
* Either go the recommended way by using the `java.lang.SuppressWarnings` annotation as shown below.

```Java
@SuppressWarnings("PMD.EmptyCatchBlock")
```

```Java
@SuppressWarnings({ "PMD.EmptyCatchBlock", "PMD.UnusedPrivateField" })
```

* Or create a file called `pmd-excludes.properties`. See [Violation Exclusions](https://maven.apache.org/plugins/maven-pmd-plugin/examples/violation-exclusions.html) for more information. The following lines show an example file.

```Java Properties
com.example.ClassA=UnusedPrivateField
com.example.ClassB=EmptyCatchBlock,UnusedPrivateField
```

##### JaCoCo
Use the `de.larssh.utils.annotations.SuppressJacocoGenerated` annotation to indicate that JaCoCo should ignore the annotated type, constructor or method.

##### Travis CI
Create a file called `travis-suppressions.sh` that filters the Maven errors and warnings output on `stdin` when processed by Travis CI. The following lines show an example file.

```Shell
# Suppress lines that contain the word "first"
function suppressFirst() {
	cat < /dev/stdin | grep --invert-match "first"
}

# Suppress lines that contain the word "second"
function suppressSeconf() {
	cat < /dev/stdin | grep --invert-match "second"
}

cat < /dev/stdin \
| suppressFirst \
| suppressSecond
```

#### Dependencies
We aim at using up-to-date dependencies and Maven plugins and minimizing runtime dependencies while still increasing safety and development ease.

##### Runtime Dependencies
The only runtime dependency (Maven scope: `compile`) used is `de.lars-sh:utils`, which comes with a bunch of generic utilities.

While it's highly dicouraged you might need to remove that dependency in rare cases. Maven does not allow exluding dependencies from parent POMs however you can change the dependencies scope to `test` in such case.

```XML
<dependency>
	<groupId>de.lars-sh</groupId>
	<artifactId>utils</artifactId>
	<scope>test</scope>
</dependency>
```

##### Compile-time Dependencies
The following dependencies are used at compile-time (Maven scope: `provided`) only:

* [Lombok Annotations](https://lars-sh/lombok-annotations) (`de.lars-sh:lombok-annotations`)
* [SpotBugs Annotations](https://spotbugs.github.io/) (`com.github.spotbugs:spotbugs-annotations`)
* `de.lars-sh:utils-annotations`

##### Test Dependencies
To simplify writing unit tests the following dependencies are available for unit tests.

* `de.lars-sh:utils-test`
* [AssertJ](https://assertj.github.io/doc/) (fluent assertion API, `org.assertj:assertj-core`)
* [jOOR](https://github.com/jOOQ/jOOR) (fluent reflection API, `org.jooq:joor-java-8`)
* [JUnit](https://junit.org/) (`org.junit.jupiter:junt-jupiter-api`, `org.junit.jupiter:junt-jupiter-engine`, `org.junit.platform:junt-platform-runner`)
* [Mockito](https://site.mockito.org/) (`org.mockito:mockito-core`)

### Unit Tests
TODO

#### Jacoco
TODO

### Checks
TODO

#### Checkstyle, CPD, PMD and Spotbugs
TODO: These are run at compile time. Describe Eclipse plugins.

### Documentation
TODO: JavaDoc is run at compile time.

### Publishing at Maven Central
TODO: Publishing requires signing your JARs. Follow [Working with GPG Signatures](https://central.sonatype.org/pages/working-with-pgp-signatures.html) as short introduction about signing and [Guide to uploading artifacts to the Central Repository](https://maven.apache.org/repository/guide-central-repository-upload.html) for more generic information.

#### Metadata Example
TODO

## Appendix
In this appendix you can find additional technical sheets, referenced by aboves topics.

### Maven Tips

#### Log Timestamp
By default the Maven output contains no timestamp. This tip describes how to change that on your computer. Sadly this needs to be configured globally and not on a per-project base.

1. Open `<maven-install-directory>/conf/logging/simplelogger.properties` in your favorite folder.
2. Add/modify the following properties:

```Java Properties
org.slf4j.simpleLogger.showDateTime=true
org.slf4j.simpleLogger.dateTimeFormat=HH:mm:ss,SSS
```
