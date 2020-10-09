package de.larssh.utils.xml;

import static de.larssh.utils.xml.TransformerOutputProperty.booleanOutputProperty;
import static de.larssh.utils.xml.TransformerOutputProperty.classOutputProperty;
import static de.larssh.utils.xml.TransformerOutputProperty.integerOutputProperty;
import static de.larssh.utils.xml.TransformerOutputProperty.stringOutputProperty;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;

import lombok.experimental.UtilityClass;

/**
 * A collection of properties to be used for instances of
 * {@link javax.xml.transform.Transformer}.
 */
@UtilityClass
@SuppressWarnings("PMD.ClassNamingConventions")
public class TransformerOutputProperties {
	/**
	 * JDK Output Properties
	 *
	 * <p>
	 * Based on {@link XMLConstants}
	 */
	@UtilityClass
	@SuppressWarnings("PMD.ShortClassName")
	public static class JDK {
		/**
		 * Restrict access to external DTDs and external Entity References to the
		 * protocols specified. If access is denied due to the restriction of this
		 * property, a runtime exception that is specific to the context is thrown. In
		 * the case of {@link javax.xml.parsers.SAXParser} for example,
		 * {@link org.xml.sax.SAXException} is thrown.
		 *
		 * @see XMLConstants#ACCESS_EXTERNAL_DTD
		 */
		public static final TransformerOutputProperty<String> ACCESS_EXTERNAL_DTD
				= stringOutputProperty(XMLConstants.ACCESS_EXTERNAL_DTD);

		/**
		 * Restrict access to the protocols specified for external reference set by the
		 * schemaLocation attribute, Import and Include element. If access is denied due
		 * to the restriction of this property, a runtime exception that is specific to
		 * the context is thrown. In the case of
		 * {@link javax.xml.validation.SchemaFactory} for example,
		 * org.xml.sax.SAXException is thrown.
		 *
		 * @see XMLConstants#ACCESS_EXTERNAL_SCHEMA
		 */
		public static final TransformerOutputProperty<String> ACCESS_EXTERNAL_SCHEMA
				= stringOutputProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA);

		/**
		 * Restrict access to the protocols specified for external references set by the
		 * stylesheet processing instruction, Import and Include element, and document
		 * function. If access is denied due to the restriction of this property, a
		 * runtime exception that is specific to the context is thrown. In the case of
		 * constructing new {@link javax.xml.transform.Transformer} for example,
		 * {@link javax.xml.transform.TransformerConfigurationException} will be thrown
		 * by the {@link javax.xml.transform.TransformerFactory}.
		 *
		 * @see XMLConstants#ACCESS_EXTERNAL_SCHEMA
		 */
		public static final TransformerOutputProperty<String> ACCESS_EXTERNAL_STYLESHEET
				= stringOutputProperty(XMLConstants.ACCESS_EXTERNAL_STYLESHEET);
	}

	/**
	 * XML 1.0 Output Properties
	 *
	 * <p>
	 * Based on <a href="https://www.w3.org/TR/1999/REC-xslt-19991116#output">XSL
	 * Transformations (XSLT) 1.0</a>
	 */
	@UtilityClass
	@SuppressWarnings("PMD.ShortClassName")
	public static class XML {
		/**
		 * The value of the method property identifies the overall method that should be
		 * used for outputting the result tree.
		 *
		 * @see OutputKeys#METHOD
		 */
		public static final TransformerOutputProperty<String> METHOD = stringOutputProperty(OutputKeys.METHOD);

		/**
		 * Specifies the version of the output method.
		 *
		 * @see OutputKeys#VERSION
		 */
		public static final TransformerOutputProperty<String> VERSION = stringOutputProperty(OutputKeys.VERSION);

		/**
		 * Specifies the preferred character encoding that the Transformer should use to
		 * encode sequences of characters as sequences of bytes.
		 *
		 * @see OutputKeys#ENCODING
		 */
		public static final TransformerOutputProperty<String> ENCODING = stringOutputProperty(OutputKeys.ENCODING);

		/**
		 * Specifies whether the XSLT processor should output an XML declaration.
		 *
		 * @see OutputKeys#OMIT_XML_DECLARATION
		 */
		public static final TransformerOutputProperty<Boolean> OMIT_XML_DECLARATION
				= booleanOutputProperty(OutputKeys.OMIT_XML_DECLARATION);

		/**
		 * Specifies whether the Transformer should output a standalone document
		 * declaration.
		 *
		 * @see OutputKeys#STANDALONE
		 */
		public static final TransformerOutputProperty<Boolean> STANDALONE
				= booleanOutputProperty(OutputKeys.STANDALONE);

		/**
		 * See the documentation for the {@link OutputKeys#DOCTYPE_SYSTEM} property for
		 * a description of what the value of the key should be.
		 *
		 * @see OutputKeys#DOCTYPE_PUBLIC
		 */
		public static final TransformerOutputProperty<String> DOCTYPE_PUBLIC
				= stringOutputProperty(OutputKeys.DOCTYPE_PUBLIC);

		/**
		 * Specifies the system identifier to be used in the document type declaration.
		 *
		 * @see OutputKeys#DOCTYPE_SYSTEM
		 */
		public static final TransformerOutputProperty<String> DOCTYPE_SYSTEM
				= stringOutputProperty(OutputKeys.DOCTYPE_SYSTEM);

		/**
		 * Specifies a whitespace delimited list of the names of elements whose text
		 * node children should be output using CDATA sections. Note that these names
		 * must use the format described in the section Qualfied Name Representation in
		 * {@link javax.xml.transform}.
		 *
		 * @see OutputKeys#CDATA_SECTION_ELEMENTS
		 */
		public static final TransformerOutputProperty<String> CDATA_SECTION_ELEMENTS
				= stringOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS);

		/**
		 * Specifies whether the Transformer may add additional whitespace when
		 * outputting the result tree.
		 *
		 * @see OutputKeys#INDENT
		 */
		public static final TransformerOutputProperty<Boolean> INDENT = booleanOutputProperty(OutputKeys.INDENT);

		/**
		 * Specifies the media type (MIME content type) of the data that results from
		 * outputting the result tree.
		 *
		 * @see OutputKeys#MEDIA_TYPE
		 */
		public static final TransformerOutputProperty<String> MEDIA_TYPE = stringOutputProperty(OutputKeys.MEDIA_TYPE);
	}

	/**
	 * Xalan Output Properties
	 *
	 * <p>
	 * Based on <a href=
	 * "http://svn.apache.org/viewvc/xalan/site/docs/xalan/xalan-j/usagepatterns.html?r1=1595241">revision
	 * 1595241</a> of the <a href=
	 * "https://xml.apache.org/xalan-j/usagepatterns.html#outputprops">official
	 * Xalan documentation</a>
	 */
	@UtilityClass
	public static class Xalan {
		/**
		 * The content handling class specifies the default name of the Java class that
		 * implements the {@link org.xml.sax.ContentHandler} interface and receives
		 * calls during result tree serialization. If you specify an alternate Java
		 * class it must implement the ContentHandler interface.
		 *
		 * <p>
		 * The default value for XML is {@code org.apache.xml.serializer.ToXMLStream},
		 * for HTML it is {@code org.apache.xml.serializer.ToHTMLStream} and for Text it
		 * is {@code org.apache.xml.serializer.ToTextStream}.
		 */
		public static final TransformerOutputProperty<Class<?>> CONTENT_HANDLER
				= classOutputProperty("{http://xml.apache.org/xslt}:content-handler");

		/**
		 * The amount of characters used when indenting.
		 *
		 * <p>
		 * Used for XML and HTML only. The default value is {@code 0}.
		 */
		public static final TransformerOutputProperty<Integer> INDENT_AMOUNT
				= integerOutputProperty("{http://xml.apache.org/xslt}:indent-amount");

		/**
		 * type: string, only xml and html, default varies
		 */
		public static final TransformerOutputProperty<String> ENTITIES
				= stringOutputProperty("{http://xml.apache.org/xslt}:entities");

		/**
		 * type: boolean, default: yes, only html
		 */
		public static final TransformerOutputProperty<Boolean> USE_URL_ESCAPING
				= booleanOutputProperty("{http://xml.apache.org/xslt}:use-url-escaping");

		/**
		 * type: boolean, default: no, only html
		 */
		public static final TransformerOutputProperty<Boolean> OMIT_META_TAG
				= booleanOutputProperty("{http://xml.apache.org/xslt}:omit-meta-tag");
	}
}
