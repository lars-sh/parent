package de.larssh.utils.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Indicates that all parameters, return values and fields of the class or
 * package are annotated with {@link NonNull}. The default behavior can be
 * overwritten using {@link edu.umd.cs.findbugs.annotations.Nullable}.
 *
 * <p>
 * While <a href=
 * "https://static.javadoc.io/com.google.code.findbugs/jsr305/3.0.2/javax/annotation/ParametersAreNonnullByDefault.html">{@literal @ParametersAreNonnullByDefault}</a>
 * means "the method parameters in that element are nonnull by default",
 * {@code @NonNullByDefault} handles parameters and also return values and
 * fields.
 *
 * <p>
 * {@code @NonNullByDefault} is similar to <a href=
 * "https://static.javadoc.io/com.github.spotbugs/spotbugs-annotations/3.1.12/edu/umd/cs/findbugs/annotations/DefaultAnnotation.html">{@code @DefaultAnnotation(NonNull.class)}</a>,
 * but can be used together with Eclipse settings.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.PACKAGE, ElementType.TYPE })
@DefaultAnnotation(NonNull.class)
public @interface NonNullByDefault {
	// empty by design
}
