# Changelog
All notable changes to this project will be documented in this file.

<a name="0.9.3"></a>

## [0.9.3-SNAPSHOT](https://github.com/lars-sh/parent/compare/dcd5e2319393a95ac13db6ad55d6baf2a1cd7d31...master)

Download at [Maven Search](https://search.maven.org/search?q=g:de.lars-sh%20AND%20%28a:parent%20a:parent-archetype%20a:root%20a:utils%20a:utils-annotations%20a:utils-test%29%20AND%20v:0.9.3)

### Added
* The new properties `dirty` and `dirty-package` allow to skip multiple checks at once.
* Allow updating Eclipse settings manually

### Changed
* Run PMD as build goal instead of as report
* Hide CPD warnings about suppressed classes
* Remove redundant semicolons when running save actions or doing clean up

### Removed
* Methods Completable.getLock() and ClosableStopwatch.getLock()

### Fixed
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
