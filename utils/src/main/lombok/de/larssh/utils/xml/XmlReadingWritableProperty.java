package de.larssh.utils.xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;

import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.Getter;

/**
 * Allows reading and writing attributes of {@link DocumentBuilderFactory} and
 * properties of {@link SAXParser} and {@link XMLReader} in a typed way.
 *
 * @param <T> the property type
 */
@Getter
public class XmlReadingWritableProperty<T> extends XmlReadingProperty<T> {
	/**
	 * Allows reading and writing attributes of {@link DocumentBuilderFactory} and
	 * properties of {@link XMLReader} in a typed way.
	 *
	 * @param name the property name
	 */
	public XmlReadingWritableProperty(final String name) {
		super(name);
	}

	/**
	 * Allows the user to set specific attributes on the underlying implementation.
	 *
	 * @param documentBuilderFactory the document builder factory
	 * @param value                  the value of the attribute
	 * @see DocumentBuilderFactory#setAttribute(String, Object)
	 */
	public void set(final DocumentBuilderFactory documentBuilderFactory, @Nullable final T value) {
		documentBuilderFactory.setAttribute(getName(), value);
	}

	/**
	 * Sets the particular property in the underlying implementation of
	 * {@link XMLReader}. A list of the core features and properties can be found at
	 * <a href="http://sax.sourceforge.net/?selected=get-set">
	 * http://sax.sourceforge.net/?selected=get-set</a>.
	 *
	 * @param saxParser the SAX parser
	 * @param value     the value of the property to be set
	 *
	 * @throws SAXNotRecognizedException when the underlying {@link XMLReader} does
	 *                                   not recognize the property name
	 * @throws SAXNotSupportedException  when the underlying {@link XMLReader}
	 *                                   recognizes the property name but doesn't
	 *                                   support the property
	 *
	 * @see #set(XMLReader, Object)
	 */
	public void set(final SAXParser saxParser, @Nullable final T value)
			throws SAXNotRecognizedException, SAXNotSupportedException {
		saxParser.setProperty(getName(), value);
	}

	/**
	 * Set the value of a property.
	 *
	 * <p>
	 * It is possible for an {@link XMLReader} to recognize a property name but to
	 * be unable to change the current value. Some property values may be immutable
	 * or mutable only in specific contexts, such as before, during, or after a
	 * parse.
	 *
	 * <p>
	 * {@link XMLReader}s are not required to recognize setting any specific
	 * property names.
	 *
	 * @param xmlReader the XML reader
	 * @param value     the requested value for the property
	 * @throws SAXNotRecognizedException if the property value can't be assigned or
	 *                                   retrieved
	 * @throws SAXNotSupportedException  when {@code xmlReader} recognizes the
	 *                                   property name but cannot set the requested
	 *                                   value
	 * @see XMLReader#setProperty(String, Object)
	 */
	public void set(final XMLReader xmlReader, @Nullable final T value)
			throws SAXNotRecognizedException, SAXNotSupportedException {
		xmlReader.setProperty(getName(), value);
	}
}
