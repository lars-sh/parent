package de.larssh.utils.xml;

import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Allows reading attributes of {@link DocumentBuilderFactory} and properties of
 * {@link XMLReader} in a typed way.
 *
 * <p>
 * While this class offers read support only, the extended class
 * {@link WritableXmlProperty} shall be used for read-write accessible
 * properties.
 *
 * @param <T> the properties type
 */
@Getter
@RequiredArgsConstructor
public class XmlProperty<T> {
	/**
	 * Property Name
	 *
	 * @return the property name
	 */
	String name;

	/**
	 * Allows the user to retrieve specific attributes on the underlying
	 * implementation.
	 *
	 * @param documentBuilderFactory the document builder factory
	 * @return the value of the attribute
	 * @throws IllegalArgumentException thrown if the underlying implementation
	 *                                  doesn't recognize the attribute.
	 * @see DocumentBuilderFactory#getAttribute(String)
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public T get(final DocumentBuilderFactory documentBuilderFactory) {
		return (T) documentBuilderFactory.getAttribute(getName());
	}

	/**
	 * Look up the value of a property.
	 *
	 * <p>
	 * It is possible for an {@code XMLReader} to recognize a property name but
	 * temporarily be unable to return its value. Some property values may be
	 * available only in specific contexts, such as before, during, or after a
	 * parse.
	 *
	 * <p>
	 * {@code XMLReader}s are not required to recognize any specific property names.
	 *
	 * @param xmlReader the XML reader
	 * @return the current value of the property
	 * @throws SAXNotRecognizedException if the property value can't be assigned or
	 *                                   retrieved.
	 * @throws SAXNotSupportedException  when {@code xmlReader} recognizes the
	 *                                   property name but cannot determine its
	 *                                   value at this time.
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public T get(final XMLReader xmlReader) throws SAXNotRecognizedException, SAXNotSupportedException {
		return (T) xmlReader.getProperty(getName());
	}
}
