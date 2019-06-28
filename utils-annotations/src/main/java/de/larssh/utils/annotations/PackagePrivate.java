package de.larssh.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the target to be package private.
 *
 * <p>
 * This annotation is validated by PMDs rule
 * {@code category/java/codestyle.xml/CommentDefaultAccessModifier}.
 */
@Retention(RetentionPolicy.CLASS)
@Target({
		ElementType.ANNOTATION_TYPE,
		ElementType.CONSTRUCTOR,
		ElementType.FIELD,
		ElementType.METHOD,
		ElementType.TYPE })
public @interface PackagePrivate {
	// no annotation type elements needed
}
