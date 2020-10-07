package de.larssh.utils.xml;

import java.util.Locale;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

import lombok.experimental.UtilityClass;

/**
 * A collection of attributes/properties to be used for instances of
 * {@link DocumentBuilderFactory}, {@link SAXParser} and {@link XMLReader}.
 */
@UtilityClass
@SuppressWarnings("PMD.ClassNamingConventions")
public class XmlReadingProperties {
	/**
	 * If you have created a DOM document builder or a SAX parser using the JAXP
	 * interfaces, the following instructions tell you how to set properties on
	 * document builders and SAX parsers created from the JAXP interfaces.
	 *
	 * <p>
	 * The method
	 * {@link javax.xml.parsers.DocumentBuilderFactory#setAttribute(String, Object)}
	 * may provide a means to set properties on the underlying parser. When using
	 * Xerces, you can set the value of a property with this method. For example:
	 *
	 * <pre>
	 * import javax.xml.parsers.DocumentBuilderFactory;
	 *
	 * DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	 * documentBuilderFactory.setNamespaceAware(true);
	 * XercesProperties.DOM.DOCUMENT_CLASS_NAME.set(documentBuilderFactory, "org.apache.xerces.dom.DocumentImpl");
	 * </pre>
	 *
	 * <p>
	 * The method {@link javax.xml.parsers.SAXParser#setProperty(String, Object)}
	 * can be used to set properties on the underlying implementation of
	 * {@link org.xml.sax.XMLReader}. You can also retrieve the underlying
	 * {@link org.xml.sax.XMLReader} from the {@link javax.xml.parsers.SAXParser}
	 * allowing you to set and query properties on it directly. For example:
	 *
	 * <pre>
	 * import javax.xml.parsers.SAXParser;
	 * import org.xml.sax.XMLReader;
	 *
	 * SAXParser saxParser = ...;
	 * XMLReader xmlReader = saxParser.getXMLReader();
	 * XercesProperties.INPUT_BUFFER_SIZE.set(xmlReader, 2048);
	 * </pre>
	 */
	@UtilityClass
	@SuppressWarnings("PMD.ClassNamingConventions")
	public static class Xerces {
		/**
		 * General Properties
		 */
		@UtilityClass
		@SuppressWarnings("PMD.ShortClassName")
		public static class General {
			/**
			 * Get the string of characters associated with the current event. If the parser
			 * recognizes and supports this property but is not currently parsing text, it
			 * should return null.
			 *
			 * <p>
			 * This property is currently not supported because the contents of the XML
			 * string returned by this property is not well defined.
			 */
			public static final XmlReadingProperty<String> XML_STRING
					= new XmlReadingProperty<>("http://xml.org/sax/properties/xml-string");

			/**
			 * The XML Schema Recommendation explicitly states that the inclusion of
			 * {@code schemaLocation} and {@code noNamespaceSchemaLocation} attributes is
			 * only a hint; it does not mandate that these attributes must be used to locate
			 * schemas. Similar situation happens to {@code <import>} element in schema
			 * documents. This property allows the user to specify a list of schemas to use.
			 * If the {@code targetNamespace} of a schema (specified using this property)
			 * matches the {@code targetNamespace} of a schema occurring in the instance
			 * document in {@code schemaLocation} attribute, or if the
			 * {@code targetNamespace} matches the namespace attribute of {@code <import>}
			 * element, the schema specified by the user using this property will be used
			 * (i.e., the {@code schemaLocation} attribute in the instance document or on
			 * the {@code <import>} element will be effectively ignored).
			 *
			 * <p>
			 * The syntax is the same as for {@code schemaLocation} attributes in instance
			 * documents: e.g, {@code http://www.example.com file_name.xsd}. The user can
			 * specify more than one XML Schema in the list.
			 */
			public static final XmlReadingWritableProperty<String> SCHEMA_EXTERNAL_SCHEMA_LOCATION
					= new XmlReadingWritableProperty<>(
							"http://apache.org/xml/properties/schema/external-schemaLocation");

			/**
			 * This property allows the user to specify an XML Schema with no namespace.
			 *
			 * <p>
			 * The syntax is a same as for the {@code noNamespaceSchemaLocation} attribute
			 * that may occur in an instance document: e.g. {@code file_name.xsd}. The user
			 * may specify only one XML Schema. For more information see the documentation
			 * for the {@link #SCHEMA_EXTERNAL_SCHEMA_LOCATION} property.
			 */
			public static final XmlReadingWritableProperty<String> SCHEMA_EXTERNAL_NO_NAMESPACE_SCHEMA_LOCATION
					= new XmlReadingWritableProperty<>(
							"http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation");

			/**
			 * A {@link javax.xml.namespace.QName} or {@code XSElementDeclaration} object
			 * representing the top-level element declaration used when validating the root
			 * element of a document or document fragment (also known as the validation
			 * root). If the value of this property is non-null the validation root will be
			 * validated against the specified element declaration regardless of the actual
			 * name of the root element in the instance document. If the value is a
			 * {@link javax.xml.namespace.QName} and a element declaration cannot be found
			 * an error will be reported.
			 *
			 * <p>
			 * Type: {@link javax.xml.namespace.QName} or
			 * {@code org.apache.xerces.xs.XSElementDeclaration}
			 *
			 * <p>
			 * Note: If the {@link #VALIDATION_SCHEMA_ROOT_TYPE_DEFINITION} property has
			 * been set this property takes precedence if its value is non-null.
			 *
			 * <p>
			 * If the value specified is an {@code XSElementDeclaration} it must be an
			 * object obtained from Xerces and must also be an object which is known to the
			 * schema validator, for example one which would be returned from an
			 * {@code XMLGrammarPool}. If these constraints are not met a
			 * {@link ClassCastException} may be thrown or processing of substitution
			 * groups, {@code xsi:type} and wildcards may fail to locate otherwise available
			 * schema components.
			 *
			 * @see #VALIDATION_SCHEMA_ROOT_TYPE_DEFINITION
			 * @since Xerces-J 2.10.0
			 */
			public static final XmlReadingWritableProperty<Object> VALIDATION_SCHEMA_ROOT_ELEMENT_DECLARATION
					= new XmlReadingWritableProperty<>(
							"http://apache.org/xml/properties/validation/schema/root-element-declaration");

			/**
			 * A {@link javax.xml.namespace.QName} or {@code XSTypeDefinition} object
			 * representing the top-level type definition used when validating the root
			 * element of a document or document fragment (also known as the validation
			 * root). If the value of this property is non-null and the
			 * {@link #VALIDATION_SCHEMA_ROOT_ELEMENT_DECLARATION} property is not set the
			 * validation root will not be validated against any element declaration. If the
			 * value is a {@link javax.xml.namespace.QName} and a type definition cannot be
			 * found an error will be reported.
			 *
			 * <p>
			 * Type: {@link javax.xml.namespace.QName} or
			 * {@code org.apache.xerces.xs.XSTypeDefinition}
			 *
			 * <p>
			 * Note: If the {@link #VALIDATION_SCHEMA_ROOT_ELEMENT_DECLARATION} property has
			 * been set this property is ignored.
			 *
			 * <p>
			 * If the value specified is an {@code XSTypeDefinition} it must be an object
			 * obtained from Xerces and must also be an object which is known to the schema
			 * validator, for example one which would be returned from an
			 * {@code XMLGrammarPool}. If these constraints are not met a
			 * {@link ClassCastException} may be thrown or processing of substitution
			 * groups, {@code xsi:type} and wildcards may fail to locate otherwise available
			 * schema components.
			 *
			 * <p>
			 * Prior to Xerces-J 2.10.0 setting the value of this property to an
			 * {@code XSTypeDefinition} was not supported.
			 *
			 * @see #VALIDATION_SCHEMA_ROOT_ELEMENT_DECLARATION
			 * @since Xerces-J 2.8.0
			 */
			public static final XmlReadingWritableProperty<Object> VALIDATION_SCHEMA_ROOT_TYPE_DEFINITION
					= new XmlReadingWritableProperty<>(
							"http://apache.org/xml/properties/validation/schema/root-type-definition");

			/**
			 * The size of the input buffer in the readers. This determines how many bytes
			 * to read for each chunk.
			 *
			 * <p>
			 * Some tests indicate that a bigger buffer size can improve the parsing
			 * performance for relatively large files. The default buffer size in Xerces is
			 * 2K. This would give a good performance for small documents (less than 10K).
			 * For documents larger than 10K, specifying the buffer size to 4K or 8K will
			 * significantly improve the performance. But it's not recommended to set it to
			 * a value larger than 16K. For really tiny documents (1K, for example), you can
			 * also set it to a value less than 2K, to get the best performance.
			 *
			 * <p>
			 * There are some conditions where the size of the parser's internal buffers may
			 * be increased beyond the size specified for the input buffer. This would
			 * happen in places where the text in the document cannot be split, for instance
			 * if the document contains a name which is longer than the input buffer.
			 *
			 * @since Xerces-J 2.1.0
			 */
			public static final XmlReadingWritableProperty<Integer> INPUT_BUFFER_SIZE
					= new XmlReadingWritableProperty<>("http://apache.org/xml/properties/input-buffer-size");

			/**
			 * The locale to use for reporting errors and warnings. When the value of this
			 * property is {@code null} the platform default returned from
			 * {@link Locale#getDefault()} will be used.
			 *
			 * <p>
			 * If no messages are available for the specified locale the platform default
			 * will be used. If the platform default is not English and no messages are
			 * available for this locale then messages will be reported in English.
			 *
			 * @since Xerces-J 2.10.0
			 */
			public static final XmlReadingWritableProperty<Locale> LOCALE
					= new XmlReadingWritableProperty<>("http://apache.org/xml/properties/locale");

			/**
			 * It is possible to create XML documents whose processing could result in the
			 * use of all system resources. This property enables Xerces to detect such
			 * documents, and abort their processing.
			 *
			 * <p>
			 * Type: {@code org.apache.xerces.util.SecurityManager}
			 *
			 * <p>
			 * Note: The {@code SecurityManager} class contains a number of methods that
			 * allow applications to tailor Xerces's tolerance of document constructs that
			 * could result in the heavy consumption of system resources (see the JavaDoc of
			 * this class for details). Default values that should be appropriate for many
			 * environments are provided when the class is instantiated. Xerces will not
			 * behave in a strictly specification compliant way when this property is set.
			 * By default, this property is not set; Xerces's behaviour is therefore
			 * strictly specification compliant by default.
			 *
			 * @since Xerces-J 2.3.0
			 */
			public static final XmlReadingWritableProperty<Object> SECURITY_MANAGER
					= new XmlReadingWritableProperty<>("http://apache.org/xml/properties/security-manager");
		}

		/**
		 * DOM Properties
		 */
		@UtilityClass
		@SuppressWarnings("PMD.ShortClassName")
		public static class DOM {
			/**
			 * The current DOM element node while parsing.
			 *
			 * <p>
			 * This property is useful for determining the location with a DOM document when
			 * an error occurs.
			 */
			public static final XmlReadingProperty<Element> CURRENT_ELEMENT_NODE
					= new XmlReadingProperty<>("http://apache.org/xml/properties/dom/current-element-node");

			/**
			 * The fully qualified name of the class implementing the
			 * {@link org.w3c.dom.Document} interface. The implementation used must have a
			 * zero argument constructor.
			 *
			 * <p>
			 * Default: {@code org.apache.xerces.dom.DocumentImpl}
			 *
			 * <p>
			 * Note: When the document class name is set to a value other than the name of
			 * the default document factory, the deferred node expansion feature does not
			 * work.
			 */
			public static final XmlReadingWritableProperty<String> DOCUMENT_CLASS_NAME
					= new XmlReadingWritableProperty<>("http://apache.org/xml/properties/dom/document-class-name");
		}

		/**
		 * SAX Properties
		 */
		@UtilityClass
		@SuppressWarnings("PMD.ShortClassName")
		public static class SAX {
			/**
			 * Set the handler for DTD declarations.
			 */
			public static final XmlReadingWritableProperty<DeclHandler> DECLARATION_HANDLER
					= new XmlReadingWritableProperty<>("http://xml.org/sax/properties/declaration-handler");

			/**
			 * Set the handler for lexical parsing events.
			 */
			public static final XmlReadingWritableProperty<LexicalHandler> LEXICAL_HANDLER
					= new XmlReadingWritableProperty<>("http://xml.org/sax/properties/lexical-handler");

			/**
			 * The DOM node currently being visited, if SAX is being used as a DOM iterator.
			 * If the parser recognizes and supports this property but is not currently
			 * visiting a DOM node, it should return {@code null}.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * <p>
			 * Note: This property is only for SAX parser implementations used as DOM tree
			 * walkers. Currently, Xerces does not have this functionality.
			 */
			public static final XmlReadingWritableProperty<Node> DOM_NODE
					= new XmlReadingWritableProperty<>("http://xml.org/sax/properties/dom-node");

			/**
			 * A literal string describing the actual XML version of the document, such as
			 * {@code 1.0} or {@code 1.1}.
			 *
			 * <p>
			 * This property may only be examined during a parse after the
			 * {@code startDocument} callback has been completed.
			 *
			 * @since Xerces-J 2.7.0
			 */
			public static final XmlReadingProperty<String> DOCUMENT_XML_VERSION
					= new XmlReadingProperty<>("http://xml.org/sax/properties/document-xml-version");
		}
	}
}
