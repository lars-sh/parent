package de.larssh.utils.xml;

import javax.xml.transform.TransformerFactory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Allows reading and writing attributes of {@link TransformerFactory} in a
 * typed way.
 *
 * @param <T> the attribute type
 */
@Getter
@RequiredArgsConstructor
public class TransformerFactoryAttribute<T> {
	/**
	 * Attribute Name
	 *
	 * @return the attribute name
	 */
	String name;

	/**
	 * Allows the user to retrieve specific attributes of the underlying
	 * implementation.
	 *
	 * <p>
	 * An {@code IllegalArgumentException} is thrown if the underlying
	 * implementation doesn't recognize the attribute.
	 *
	 * @param transformerFactory the transformer factory
	 * @return value the value of the attribute
	 * @throws IllegalArgumentException if implementation does not recognize the
	 *                                  attribute
	 */
	@SuppressWarnings("unchecked")
	public T get(final TransformerFactory transformerFactory) throws IllegalArgumentException {
		return (T) transformerFactory.getAttribute(getName());
	}

	/**
	 * Allows the user to set specific attributes on the underlying implementation.
	 * An attribute in this context is defined to be an option that the
	 * implementation provides.
	 *
	 * <p>
	 * An {@code IllegalArgumentException} is thrown if the underlying
	 * implementation doesn't recognize the attribute.
	 *
	 * @param transformerFactory the transformer factory
	 * @param value              the value of the attribute
	 * @throws IllegalArgumentException if implementation does not recognize the
	 *                                  attribute
	 */
	public void set(final TransformerFactory transformerFactory, final T value) throws IllegalArgumentException {
		transformerFactory.setAttribute(getName(), value);
	}
}
