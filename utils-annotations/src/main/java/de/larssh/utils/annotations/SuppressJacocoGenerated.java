package de.larssh.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Indicates that JaCoCo should ignore the annotated type, constructor or
 * method.
 *
 * <p>
 * <b>Implementation Notice:</b> As of JaCoCo 0.8.3 <i>classes and methods
 * annotated by annotation whose retention policy is {@code runtime} or
 * {@code class} and whose simple name contains "Generated" are filtered out
 * during generation of report</i>.
 */
@Retention(RetentionPolicy.CLASS)
@SuppressFBWarnings
@Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
public @interface SuppressJacocoGenerated {
	/**
	 * Documentation of the reason why JaCoCo is suppressed
	 */
	String justification();
}
