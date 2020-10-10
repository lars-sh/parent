package de.larssh.utils.xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;

import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Allows reading attributes of {@link DocumentBuilderFactory} and properties of
 * {@link SAXParser} and {@link XMLReader} in a typed way.
 *
 * <p>
 * While this class offers read support only, the extended class
 * {@link XmlReadingWritableProperty} shall be used for read-write accessible
 * properties.
 *
 * @param <T> the property type
 */
@Getter
@RequiredArgsConstructor
public class XmlReadingProperty<T> {
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
	 * Returns the particular property requested for in the underlying
	 * implementation of {@link XMLReader}.
	 *
	 * @param saxParser the SAX parser
	 * @return the current value of the property
	 * @throws SAXNotRecognizedException when the underlying {@link XMLReader} does
	 *                                   not recognize the property name.
	 * @throws SAXNotSupportedException  when the underlying {@link XMLReader}
	 *                                   recognizes the property name but doesn't
	 *                                   support the property.
	 *
	 * @see XMLReader#getProperty
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public T get(final SAXParser saxParser) throws SAXNotRecognizedException, SAXNotSupportedException {
		return (T) saxParser.getProperty(getName());
	}

	/**
	 * Look up the value of a property.
	 *
	 * <p>
	 * It is possible for a {@link XMLReader} to recognize a property name but
	 * temporarily be unable to return its value. Some property values may be
	 * available only in specific contexts, such as before, during, or after a
	 * parse.
	 *
	 * <p>
	 * {@link XMLReader}s are not required to recognize any specific property names.
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
