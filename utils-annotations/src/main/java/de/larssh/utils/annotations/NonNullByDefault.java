package de.larssh.utils.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.meta.TypeQualifierNickname;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Indicates that all members of the class or package are annotated with
 * {@link NonNull}. The default behavior can be overwritten using
 * {@link edu.umd.cs.findbugs.annotations.Nullable}.
 *
 * <p>
 * Using {@code @NonNullByDefault} is similar to
 * {@code @DefaultAnnotation(NonNull.class)}, but can be used together with
 * Eclipse settings.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@TypeQualifierNickname
@Target({ ElementType.PACKAGE, ElementType.TYPE })
@DefaultAnnotation(NonNull.class)
public @interface NonNullByDefault {
	// empty by design
}
