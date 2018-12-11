# Utility Annotations
This package offers annotations with class retention. Therefore it is required to be in class path on compile time, but must not be delivered for runtime purpose.

Currently only one annotation is part of this package:
* `de.larssh.utils.annotations.NonNullByDefault` indicates that all members of a class or package are annotated with `NonNull`. The default behavior can be overwritten using `Nullable`.

## Getting started
The package is part of the project `de.lars-sh:parent`.

In case you want to use this package separately, here's a Maven dependency example:

	<dependency>
		<groupId>de.lars-sh</groupId>
		<artifactId>utils-annotations</artifactId>
		<version><!-- TODO --></version>
		<scope>provided</scope>
	</dependency>

To learn more about the available annotations check out the JavaDoc.