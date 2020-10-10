package de.larssh.utils.xml;

import static java.util.function.Function.identity;

import java.util.function.Function;

import javax.xml.transform.Transformer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Allows reading and writing output properties of {@link Transformer} in a
 * typed way.
 *
 * @param <T> the output property type
 */
@Getter
@RequiredArgsConstructor
public class TransformerOutputProperty<T> {
	/**
	 * Boolean output property value representing {@code false}
	 */
	private static final String BOOLEAN_VALUE_FALSE = "no";

	/**
	 * Boolean output property value representing {@code true}
	 */
	private static final String BOOLEAN_VALUE_TRUE = "yes";

	/**
	 * Creates a boolean {@link TransformerOutputProperty}.
	 *
	 * <p>
	 * Value {@code yes} means {@code true} and value {@code no} means
	 * {@code false}.
	 *
	 * @param name the output property name
	 * @return the {@link TransformerOutputProperty}
	 */
	public static TransformerOutputProperty<Boolean> booleanOutputProperty(final String name) {
		return new TransformerOutputProperty<>(name, //
				value -> value ? BOOLEAN_VALUE_TRUE : BOOLEAN_VALUE_FALSE,
				BOOLEAN_VALUE_TRUE::equalsIgnoreCase);
	}

	/**
	 * Creates a class {@link TransformerOutputProperty}.
	 *
	 * @param name the output property name
	 * @return the {@link TransformerOutputProperty}
	 */
	public static TransformerOutputProperty<Class<?>> classOutputProperty(final String name) {
		return new TransformerOutputProperty<>(name, Class::getName, value -> {
			try {
				return Class.forName(value);
			} catch (final ClassNotFoundException e) {
				throw new IllegalArgumentException(e);
			}
		});
	}

	/**
	 * Creates an integer {@link TransformerOutputProperty}.
	 *
	 * @param name the output property name
	 * @return the {@link TransformerOutputProperty}
	 */
	public static TransformerOutputProperty<Integer> integerOutputProperty(final String name) {
		return new TransformerOutputProperty<>(name, //
				value -> Integer.toString(value),
				Integer::parseInt);
	}

	/**
	 * Creates a string {@link TransformerOutputProperty}.
	 *
	 * @param name the output property name
	 * @return the {@link TransformerOutputProperty}
	 */
	public static TransformerOutputProperty<String> stringOutputProperty(final String name) {
		return new TransformerOutputProperty<>(name, //
				identity(),
				identity());
	}

	/**
	 * Output Property Name
	 *
	 * @return the output property name
	 */
	String name;

	/**
	 * Output Property Serializer
	 *
	 * @return the output property serializer
	 */
	@Getter(AccessLevel.PROTECTED)
	Function<T, String> serializer;

	/**
	 * Output Property Deserializer
	 *
	 * @return the output property deserializer
	 */
	@Getter(AccessLevel.PROTECTED)
	Function<String, T> deserializer;

	/**
	 * Get an output property that is in effect for the transformer.
	 *
	 * <p>
	 * If a property has been set using
	 * {@link Transformer#setOutputProperty(String, String)}, that value will be
	 * returned. Otherwise, if a property is explicitly specified in the stylesheet,
	 * that value will be returned. If the value of the property has been defaulted,
	 * that is, if no value has been set explicitly either with
	 * {@link Transformer#setOutputProperty(String, String)} or in the stylesheet,
	 * the result may vary depending on implementation and input stylesheet.
	 *
	 * @param transformer the transformer
	 * @return the string value of the output property, or {@code null} if no
	 *         property was found
	 * @throws IllegalArgumentException if the property is not supported
	 * @see TransformerOutputProperties
	 */
	public T get(final Transformer transformer) {
		return getDeserializer().apply(transformer.getOutputProperty(getName()));
	}

	/**
	 * Set an output property that will be in effect for the transformation.
	 *
	 * <p>
	 * Pass a qualified property name as a two-part string, the namespace URI
	 * enclosed in curly braces ({@code {}}), followed by the local name. If the
	 * name has a {@code null} URL, the String only contain the local name. An
	 * application can safely check for a non-{@code null} URI by testing to see if
	 * the first character of the name is a '{' character.
	 *
	 * <p>
	 * For example, if a URI and local name were obtained from an element defined
	 * with {@code &lt;xyz:foo xmlns:xyz="http://xyz.foo.com/yada/baz.html"/&gt;},
	 * then the qualified name would be "{http://xyz.foo.com/yada/baz.html}foo".
	 * Note that no prefix is used.
	 *
	 * <p>
	 * The Properties object that was passed to
	 * {@link Transformer#setOutputProperties(java.util.Properties)} won't be
	 * effected by calling this method.
	 *
	 * @param transformer the transformer
	 * @param value       The non-null string value of the output property.
	 * @throws IllegalArgumentException if the property is not supported, and is not
	 *                                  qualified with a namespace
	 * @see TransformerOutputProperties
	 */
	public void set(final Transformer transformer, final T value) {
		transformer.setOutputProperty(getName(), getSerializer().apply(value));
	}
}
