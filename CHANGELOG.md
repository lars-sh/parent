# Changelog
All notable changes to this project will be documented in this file.

<a name="0.9.15"></a>

## [0.9.15-SNAPSHOT](https://github.com/lars-sh/parent/compare/66575e54e8da5f2e68b2dd1c7a2f27b486e2bf5c...master)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:parent-archetype%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.15)

### Added
TODO

### Changed
* `de.larssh.utils.Finals.lazy` now returns a `CachingSupplier` to allow other to determine if the lazy-loading value has been calculated already

### Fixed
* `de.larssh.utils.text.Strings.toLowerCaseAscii` and `de.larssh.utils.text.Strings.toUpperCaseAscii` for single character strings

### Removed
* `de.larssh.utils.maven.AetherUtils` and `de.larssh.utils.maven.DependencyScope`
* Triggering builds on all pushes to avoid duplicate triggering. Instead, builds are triggered for pull requests and pushes to the master branch only.

<a name="0.9.14"></a>

## [0.9.14](https://github.com/lars-sh/parent/compare/01adecb082f55fca1e6f83e38a65f6e1bd3027a1...66575e54e8da5f2e68b2dd1c7a2f27b486e2bf5c)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:parent-archetype%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.14)

### Fixed
* Possible `NullPointerException` in `de.larssh.utils.text.Lines.consecutive(...)`

### Removed
* POM Property `jar.manifest.addClasspath`

<a name="0.9.13"></a>

## [0.9.13](https://github.com/lars-sh/parent/compare/244da485b4b6225726946e4ab751034eda07a46c...01adecb082f55fca1e6f83e38a65f6e1bd3027a1) (2021-12-27)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:parent-archetype%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.13)

### Added
* Methods to handle lower and upper case handling on an ASCII basis: `de.larssh.utils.text.Characters.*Ascii*` and `de.larssh.utils.text.Strings.*Ascii`
* Structures to handle CSV data (`de.larssh.utils.text.Csv`) and corresponding methods in `de.larssh.utils.text.StringConverters`
* Support being unmodifiable on demand for `de.larssh.utils.collection.Proxied*`
  Therefore protected getters were renamed to `getProxied*` and now differentiate between read-only access and the need to check for write access.
  In addition a method `isModifiable` is forced to be implemented.
* Method overloads to retrieve the first non-null or present value: `de.larssh.utils.Nullables.getFirst` and `de.larssh.utils.Optionals.getFirst`
* Method `de.larssh.utils.io.ProcessBuilders.toCommandLine` to create command line string out of `ProcessBuilder` instances

### Changed
* Renamed constants of `de.larssh.utils.text.SplitLimit` to be more precise
* Renamed methods `de.larssh.utils.text.Strings.toNeutral*` to `de.larssh.utils.text.Strings.to*Neutral` to match the naming of the new ASCII methods.
* Most methods inside `de.larssh.utils.text.Strings` allow to pass any kind of `CharSequence`, not just `String`.
* Allow underscores in the name of test methods.

### Fixed
* Support point-relative paths in `de.larssh.utils.io.Resources`

### Removed
* `de.larssh.utils.test.Assertions#assertEqualsAndHashCode` as most `equals` and `hashCode` implementations don't need to be tested in a that specific way.

<a name="0.9.12"></a>

## [0.9.12](https://github.com/lars-sh/parent/compare/8ba38d6ee1c58c02e5011e40d013b94cfaf59957...244da485b4b6225726946e4ab751034eda07a46c) (2021-04-03)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:parent-archetype%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.12)

### Added
* Creation of a GitHub Action for release deployment to OSSRH (Maven Central via Sonartype Nexus).  
  You can enable this feature by setting the Maven property `parent-pom.create-github-release-yml` to `true`.
* Possibility to add custom Checkstyle rules on a per-project basis.

### Changed
* GitHub snapshot builds are signed by default.
  This might require additional action to specify the private key and its passphrase in GitHub. Checkout the [Readme](README.md) for more information.
* Providing snapshot builds through GitHub Packages
* Using JDK16 for builds instead of JDK14.

<a name="0.9.11"></a>

## [0.9.11](https://github.com/lars-sh/parent/compare/0a877872f94acddb1e4bfaf53585dbf0e69f83aa...8ba38d6ee1c58c02e5011e40d013b94cfaf59957) (2021-01-04)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:parent-archetype%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.11)

### Added
* Creation of GitHub Action configuration instead of Travis CI file.  
  You can still enable the creation of `.travis.yml` by setting the Maven property `parent-pom.create-travis-yml` to `true`.
* Creation of a GitHub native Dependabot configuration file  
  Use the Maven property `parent-pom.create-dependabot-yml` to suppress writing this file.
* Class `de.larssh.utils.io.MultiInputStream`
* Class `de.larssh.utils.io.MultiReader`

### Changed
* Moved class `Resources` to new package `de.larssh.utils.io`
* Rename file `travis-suppressions.sh` to `mvn-suppressions.sh`
* Update formatter and clean-up settings for Eclipse 2020-12

### Fixed
* Fix formatting of multiline `else if` statements
* Fix creation of the Spotbugs Eclipse plugin configuration

<a name="0.9.10"></a>

## [0.9.10](https://github.com/lars-sh/parent/compare/3e680a221a2e8913a7d4d15500a6eea0092a9d17...0a877872f94acddb1e4bfaf53585dbf0e69f83aa) (2020-10-18)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:parent-archetype%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.10)

### Added
* Checkstyle check `XAvoidSerialVersionUID`, warning on usage of `serialVersionUID`.
* Collection of XML processing attributes, features and properties inside package `de.larssh.utils.xml`

### Changed
* [Avoid using serialVersionUID](https://github.com/rzwitserloot/lombok/wiki/WHY-NOT:-serialVersionUID).
* Suppressing additional SpotBugs null checks when using JDK 11 and later as they might fail when using try-with-resource statements as of https://github.com/spotbugs/spotbugs/issues/259 and https://github.com/spotbugs/spotbugs/issues/756
* Missing dependency scopes inside the `utils` project

<a name="0.9.9"></a>

## [0.9.9](https://github.com/lars-sh/parent/compare/ef0887e461aec61ee0f75ff8f5ddefbf2f74ac5c...3e680a221a2e8913a7d4d15500a6eea0092a9d17) (2020-08-20)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:parent-archetype%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.9)

### Added
* Interface `de.larssh.utils.collection.PeekableIterator`
* Method `de.larssh.utils.collection.Iterators.iterator(elements supplier)`
* Method `de.larssh.utils.collection.Iterators.peekableIterator(iterator)`
* Method `de.larssh.utils.collection.Iterators.stream(iterator)`
* Method `de.larssh.utils.collection.Iterators.stream(elements supplier)`
* Method `de.larssh.utils.collection.Streams.indexed(stream)`
* Method `de.larssh.utils.collection.Streams.indexedLong(stream)`
* Method `de.larssh.utils.text.Patterns.quote(input)`

### Changed
* Method `Maps.Builder#get()` is not typed any longer to simplify generics of `Maps.Builder`
* Methods inside `de.larssh.utils.text.Lines` no longer require Google Guava to be part of your dependencies.
* Updated the list of JDKs to be used by Travis CI based on the list of currently supported JDKs.

### Fixed
* Travis didn't fail, even though Maven failed building
* PMD errors were not printed and therefore didn't let the build fail.
* PMD checks are no longer processed in aggregate mode unless building reports.

<a name="0.9.8"></a>

## [0.9.8](https://github.com/lars-sh/parent/compare/c1419c24b7ddb93a75b751e34c2d54d452a07b01...ef0887e461aec61ee0f75ff8f5ddefbf2f74ac5c) (2020-07-03)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:parent-archetype%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.8)

### Added
* Methods `de.larssh.text.Lines.consecutive(...)`, `de.larssh.text.Lines.grouped(...)` and `de.larssh.text.Lines.lines(...)` with credits to [Olaf Neumann](https://github.com/noxone/commons/tree/develop/commons-io/src/main/java/org/olafneumann/files/LinesReader.java)
* Travis CI validates its Maven output for previously silent errors and warnings.

### Changed
* Improved Maven site generation.
* Maven property `parent-pom.create-travis-yml` changed to be `true` by default to override existing files
* Methods of `de.larssh.utils.Resources` force case sensitivity to enforce compatibility between file systems.
* Activate spotbugs for JDK 11 and later

### Removed
* Method `Strings.lines(String)` (use `Lines.lines(String)` instead)

### Fixed
* Checkstyle check XFinalsConstant
* Changed the dependency scope of the utility annotations to `provided`.
* Changed the dependency scope of the utilities for tests to `test`.
* Compatibility with OpenJDK 9 to 13
* Relative Path of the Parent POM inside Parent Archetype

<a name="0.9.7"></a>

## [0.9.7](https://github.com/lars-sh/parent/compare/888c0ccaa1b0d95bd5afce8dcfac8c9accdb296e...c1419c24b7ddb93a75b751e34c2d54d452a07b01) (2020-02-26)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:parent-archetype%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.7)

### Added
* Maven property `eclipse.compiler.nullAnalysis` allows to disable the null analysis feature of Eclipse.
* Maven property `eclipse.compiler.potentialNullReference` allows adjusting the potential null pointer access feature of Eclipse.

### Changed
* Project Lombok has been configured to add nullable annotations by itself. This simplifies e.g. @EqualsAndHashCode usage.
* Do not execute the delombok plugin unless a lombok sources folder exists
* Improved generics for methods inside `de.larssh.utils.Optionals`

### Fixed
* Ignoring Spotbugs Annotations in the dependency plugin

<a name="0.9.6"></a>

## [0.9.6](https://github.com/lars-sh/parent/compare/910893179255a2a139856edf72bc5525bb9f3e40...888c0ccaa1b0d95bd5afce8dcfac8c9accdb296e) (2020-01-27)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:parent-archetype%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.6)

### Added
* Method `de.larssh.utils.Maps.builder()`
* Method `de.larssh.utils.Maps.builder(Map)`
* Method `de.larssh.utils.Optionals.comparator()`
* Method `de.larssh.utils.Optionals.comparator(Comparator)`
* Configure Eclipse to respect the value specified for `maven.compiler.parameters`

### Changed
* Update formatter and clean-up settings for Eclipse 2019-09
* `de.larssh.utils.net.InsecureConnections.getTrustManagersThatDoNotVerifyCertificates()` is now based on `javax.net.ssl.X509ExtendedTrustManager`

### Fixed
* Work around a bug in Eclipse 2019-12 when performing clean up or save actions to handle `this`.
* Maven property `eclipse.compiler.javadoc`

<a name="0.9.5"></a>

## [0.9.5](https://github.com/lars-sh/parent/compare/35490fbfcecc4c4eace0d3d3f630bd121bd0878a...910893179255a2a139856edf72bc5525bb9f3e40) (2019-12-03)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:parent-archetype%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.5)

### Added
* Check code format via command line prior compiling. This simplifies working in teams with different editors.
* IntelliJ IDEA compatibility instructions as of `README.md`
* AssertJ test dependency
* Method `de.larssh.utils.Either.ifPresent(firstConsumer, secondConsumer)`
* Method `de.larssh.utils.Either.ifFirstIsPresent(consumer)`
* Method `de.larssh.utils.Either.ifSecondIsPresent(consumer)`
* Constant `de.larssh.utils.SystemUtils.DEFAULT_FILE_NAME_SEPARATOR`
* Constant `de.larssh.utils.SystemUtils.DEFAULT_FILE_NAME_SEPARATOR_CHAR`
* Constant `de.larssh.utils.SystemUtils.FILE_EXTENSION_SEPARATOR`
* Constant `de.larssh.utils.SystemUtils.FILE_EXTENSION_SEPARATOR_CHAR`
* Class `de.larssh.collections.Enumerations`
* Class `de.larssh.utils.Resources`
* Additional POM properties to configure generation of files and folders

### Removed
* Checkstyle nearby comment filter for the MultipleStringLiterals check. Use `@SuppressWarnings` instead.
* Renamed the Maven property `org.eclipse.jdt.core.compiler.annotation.missingNonNullByDefaultAnnotation`.
 Use `eclipse.compiler.missingNonNullByDefaultAnnotation` instead.
* Renamed the Maven property `org.eclipse.jdt.core.compiler.annotation.nonnull.secondary`.
Use `eclipse.compiler.nonnull.secondary` instead.
* Renamed the Maven property `org.eclipse.jdt.core.compiler.annotation.nonnullbydefault.secondary`.
Use `eclipse.compiler.nonnullbydefault.secondary` instead.
* Renamed the Maven property `org.eclipse.jdt.core.compiler.annotation.nullable.secondary`.
Use `eclipse.compiler.nullable.secondary` instead.
* Renamed the Maven property `org.eclipse.jdt.core.compiler.doc.comment.support`.
Use `eclipse.compiler.javadoc` instead.
* Renamed the Maven property `project.build.mainClass`.
Use `jar.manifest.mainClass` instead.
* Renamed the Maven property `project.build.packageDependenciesPhase`.
Use `shade.packageDependenciesPhase` instead.

### Changed
* Moved execution of the `tidy-maven-plugin` to `validate` and creation of project files to the Maven lifecycle phase `initialize`.
* Checkstyle no longer runs on delomboked sources.
* Avoid some ignorable warnings inside the Maven output.

### Fixed
* Lifecycle mapping documentation inside `README.md`

<a name="0.9.4"></a>

## [0.9.4](https://github.com/lars-sh/parent/compare/50d456db5add133c16d6bdbf9b9b20d6149c2967...35490fbfcecc4c4eace0d3d3f630bd121bd0878a) (2019-08-29)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:parent-archetype%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.4)

### Added
* Annotation `de.larssh.utils.annotations.SuppressJacocoGenerated` to indicate that JaCoCo should ignore the annotated type, constructor or method
* Method `Maps.entry(K, V)`

### Changed
* Travis CI: JDK 11, Caching and speed improvements. Run `mvn generate-sources -P update-travis-yml` to update existing `.travis.yml` files.
* The properties `dirty` and `dirty-package` changed into profiles. Instead of `-Ddirty=true` you need to use `-P dirty` now.
* CPD and SpotBugs exclude files no longer require the additional property to be set manually.
* Automatically releasing after closing deployment
* Finals.constant should not be used for private constants.

### Fixed
* Fix checkstyle rule XFinalsConstantPrivate for static imports
* Fix SpotBugs when using JDK 11
* Fix Eclipse Oxygen failing while trying to save charset preferences
* Fix `Stopwatch.waitFor(...)` when reaching the timeout value exactly

<a name="0.9.3"></a>

## [0.9.3](https://github.com/lars-sh/parent/compare/dcd5e2319393a95ac13db6ad55d6baf2a1cd7d31...50d456db5add133c16d6bdbf9b9b20d6149c2967) (2019-07-22)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:parent-archetype%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.3)

### Added
* Properties `dirty` and `dirty-package` allow to skip multiple checks at once
* Allow updating Eclipse settings manually
* SplitLimit utility to handle `Pattern.split` and `String.split` in a clean way
* New method `Stopwatch.waitFor(...)` to simplify working with timeouts
* New method `SecureConnections.getHttpsUrlConnection()`
* New utility class `InsecureConnections`
* Disallow `super()` using checkstyle
* Tip in the README that describes how to add timestamps to the Maven output

### Changed
* Fail on JavaDoc warnings
* Disallow the usage of @lombok.Builder as of compatibility problems with the default non-null concept
* Disallow parameter names that match `arg` or `arg0` and similar
* Hide CPD warnings about suppressed classes
* Run PMD as build goal instead of as report
* Remove redundant semicolons when running save actions or doing clean up

### Removed
* Methods `Completable.getLock()` and `ClosableStopwatch.getLock()`
* Dependency Management entries inside POM that were not used by one of the parent projects

### Fixed
* `SecureConnections.getSocketFactory(...)` not returning a SSLSocketFactory
* JavaDoc processing when using JDK 9 and later
* Working around bad formatter behavior related to indentation of blocks after long lines.
* Scope of maven-resolver-util dependency

<a name="0.9.2"></a>

## [0.9.2](https://github.com/lars-sh/parent/compare/3547b85c9a72fd0c10c00be9dd40ceee65fec7cf...dcd5e2319393a95ac13db6ad55d6baf2a1cd7d31) (2019-05-06)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:parent-archetype%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.2)

### Added
* Archetype
* Changelog
* Class de.larssh.utils.Nullables
* Class de.larssh.utils.maven.DependencyScope
* Class de.larssh.utils.net.GlobalProxyConfiguration
* Class de.larssh.utils.time.ClosableStopwatch
* Class de.larssh.utils.time.LoggingStopwatch
* Class de.larssh.utils.time.Stopwatch
* Method de.larssh.utils.Collectors.toLinkedHashMap()
* Method de.larssh.utils.Collectors.toMap(mapSupplier)
* Method de.larssh.utils.Collectors.toMap(keyMapper, valueMapper \[, mergeFunction\] \[, mapSupplier\])
* Method de.larssh.utils.OptionalDoubles.ofNullable(value)
* Method de.larssh.utils.OptionalInts.ofNullable(value)
* Method de.larssh.utils.OptionalLongs.ofNullable(value)
* Method de.larssh.utils.Optionals.getFirst(isPresent, suppliers)
* Method de.larssh.utils.Optionals.getFirstValue(isPresent, values)
* Method de.larssh.utils.Optionals.ofNonEmpty(array|collection)
* Method de.larssh.utils.Optionals.ofSingle(array|iterable|iterator|stream|string)
* Method de.larssh.utils.text.Characters.equalsIgnoreCase(first, second)
* Method de.larssh.utils.text.Strings.endsWithIgnoreCase(value, suffix)
* Method de.larssh.utils.text.Strings.find(input, pattern)
* Method de.larssh.utils.text.Strings.matches(input, pattern)
* Method de.larssh.utils.text.Strings.startsWithIgnoreCase(value, prefix)
* Method de.larssh.utils.text.Strings.startsWithIgnoreCase(value, prefix, offset)
* Annotation de.larssh.utils.annotations.PackagePrivate

### Changed
* Disallow usage of java.lang.StringBuffer
* Disallow catching java.lang.NullPointerException
* Update README.md with information on skipping plugins
* Allow overwriting some predefined Eclipse settings using Maven Properties
* Update formatter settings for Eclipse 2019-03
* Enforce Maven version 3.3.9 (as JaCoCo requires it)
* Execute SpotBugs, copy-paste-detection (CPD) and dependency analysis from Maven reporting to build phase
* Validate Javadoc inside Travis CI
* Make classes Completable and Finals thread-safe
* NumericTextComparator handles numerics as signed only if the sign is preceeded by a whitespace or at a strings start
* Moved de.larssh.utils.AetherUtils to de.larssh.utils.maven.AetherUtils
* ParseException initializes cause
* Update dependencies and Maven plugins

#### Checkstyle
* Validate lombok sources.
* Allow empty // comments at the end of lines with code.
* Force Finals.constant for string and primitive constants.
* Force @SuppressFBWarnings to include a justification that is not-empty and not "TODO".
* Force usage of OptionalDouble, OptionalInt and OptionalLong instead of Optionals.
* Force usage of Strings.replaceAll/replaceFirst as far as possible.
* Warnings may be suppressed using @SuppressWarnings
* Suppressions file name changed from "suppressions.xml" to "checkstyle-suppressions.xml"
* For unit tests suppressing checks ExecutableStatementCountCheck, MagicNumberCheck and MethodLengthCheck
* Refined checkstyles SeparatorWrap checks.

### Fixed
* Maven plugins were not compatible with Maven versions unequal to 3.6.0
* Formatter tags

<a name="0.9.1"></a>

## [0.9.1](https://github.com/lars-sh/parent/compare/55696d71cc8c2946710a803945c0425967e4e83c...3547b85c9a72fd0c10c00be9dd40ceee65fec7cf) (2018-12-12)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.1)

### Fixed
* Handling of parent projects module version

<a name="0.9.0"></a>

## [0.9.0](https://github.com/lars-sh/parent/commit/55696d71cc8c2946710a803945c0425967e4e83c) (2018-12-10)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.0)

* Initial release
