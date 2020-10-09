package de.larssh.utils.xml;

import de.larssh.utils.Finals;
import lombok.experimental.UtilityClass;

/**
 * A collection of features to be used for instances of
 * {@link javax.xml.parsers.DocumentBuilderFactory},
 * {@link javax.xml.parsers.SAXParser} and {@link org.xml.sax.XMLReader}.
 */
@UtilityClass
@SuppressWarnings("PMD.ClassNamingConventions")
public class XmlReadingFeatures {
	/**
	 * Xerces Features
	 *
	 * <p>
	 * Based on <a href=
	 * "http://svn.apache.org/viewvc/xerces/site/trunk/production/xerces2-j/features.html?r1=1872634">revision
	 * 1872634</a> of the
	 * <a href="https://xerces.apache.org/xerces2-j/features.html">official Xerces
	 * features documentation</a>
	 *
	 * <p>
	 * If you have created a DOM document builder or a SAX parser using the JAXP
	 * interfaces, the following instructions tell you how to set features on
	 * document builders and SAX parsers created from the JAXP interfaces.
	 *
	 * <p>
	 * The method
	 * {@link javax.xml.parsers.DocumentBuilderFactory#setFeature(String, boolean)}
	 * can be used to set features on the underlying parser. For example:
	 *
	 * <pre>
	 * import javax.xml.parsers.DocumentBuilderFactory;
	 *
	 * DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	 * documentBuilderFactory.setNamespaceAware(true);
	 *
	 * documentBuilderFactory.setFeature(XercesFeatures.General.ALLOW_JAVA_ENCODINGS, true);
	 * </pre>
	 *
	 * <p>
	 * The method
	 * {@link javax.xml.parsers.SAXParserFactory#setFeature(String, boolean)} method
	 * can be used to set features on the underlying implementation of
	 * {@link org.xml.sax.XMLReader}. Once you create the
	 * {@link javax.xml.parsers.SAXParser} you can retrieve the underlying
	 * {@link org.xml.sax.XMLReader} allowing you to set and query features on it
	 * directly. For example:
	 *
	 * <pre>
	 * import javax.xml.parsers.SAXParser;
	 * import org.xml.sax.XMLReader;
	 *
	 * SAXParser saxParser = ...;
	 * XMLReader xmlReader = saxParser.getXMLReader();
	 * xmlReader.setFeature(XercesFeatures.General.ALLOW_JAVA_ENCODINGS, true);
	 * </pre>
	 */
	@UtilityClass
	public static class Xerces {
		/**
		 * General Features
		 */
		@UtilityClass
		public static class General {
			/**
			 * {@code true} (default): Perform namespace processing: prefixes will be
			 * stripped off element and attribute names and replaced with the corresponding
			 * namespace URIs. By default, the two will simply be concatenated, but the
			 * namespace-sep core property allows the application to specify a delimiter
			 * string for separating the URI part and the local part.
			 *
			 * <p>
			 * {@code false}: Do not perform namespace processing.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * <p>
			 * Note: If the validation feature is set to {@code true}, then the document
			 * must contain a grammar that supports the use of namespaces.
			 *
			 * @see SAX#NAMESPACE_PREFIXES
			 * @see #VALIDATION
			 */
			public static final String NAMESPACES = Finals.constant("http://xml.org/sax/features/namespaces");

			/**
			 * {@code true} (default): The methods of the
			 * {@link org.xml.sax.ext.EntityResolver2} interface will be used when an object
			 * implementing this interface is registered with the parser using
			 * {@code setEntityResolver}.
			 *
			 * <p>
			 * {@code false}: The methods of the {@link org.xml.sax.ext.EntityResolver2}
			 * interface will not be used.
			 *
			 * <p>
			 * Access: read-write
			 *
			 * <p>
			 * Note: If the disallow {@code DOCTYPE} declaration feature is set to
			 * {@code true}
			 * {@link org.xml.sax.ext.EntityResolver2#getExternalSubset(String, String)}
			 * will not be called when the document contains no {@code DOCTYPE} declaration.
			 *
			 * @see #DISALLOW_DOCTYPE_DECLARATION
			 * @see #VALIDATION
			 * @see #NONVALIDATING_LOAD_EXTERNAL_DTD
			 * @since Xerces-J 2.7.0
			 */
			public static final String USE_ENTITY_RESOLVER_2
					= Finals.constant("http://xml.org/sax/features/use-entity-resolver2");

			/**
			 * {@code true}: Validate the document and report validity errors.
			 *
			 * <p>
			 * {@code false} (default): Do not report validity errors.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * <p>
			 * Note: If this feature is set to {@code true}, the document must specify a
			 * grammar. By default, validation will occur against DTD. For more information,
			 * please, refer to the FAQ. If this feature is set to {@code false}, and
			 * document specifies a grammar that grammar might be parsed but no validation
			 * of the document contents will be performed.
			 *
			 * @see #VALIDATION_DYNAMIC
			 * @see #NAMESPACES
			 * @see #NONVALIDATING_LOAD_EXTERNAL_DTD
			 */
			public static final String VALIDATION = Finals.constant("http://xml.org/sax/features/validation");

			/**
			 * {@code true}: The parser will validate the document only if a grammar is
			 * specified.
			 *
			 * <p>
			 * {@code false} (default): Validation is determined by the state of the
			 * validation feature.
			 *
			 * @see #VALIDATION
			 */
			public static final String VALIDATION_DYNAMIC
					= Finals.constant("http://apache.org/xml/features/validation/dynamic");

			/**
			 * {@code true}: Turn on XML Schema validation by inserting an XML Schema
			 * validator into the pipeline.
			 *
			 * <p>
			 * {@code false} (default): Do not report validation errors against XML Schema.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * <p>
			 * Note: Validation errors will only be reported if the validation feature is
			 * set to {@code true}. For more information, please, refer to the FAQ
			 *
			 * <p>
			 * Checking of constraints on a schema grammar which are either time-consuming
			 * or memory intensive such as unique particle attribution will only occur if
			 * the schema full checking feature is set to {@code true}.
			 *
			 * @see #VALIDATION
			 * @see #VALIDATION_DYNAMIC
			 * @see #NAMESPACES
			 */
			public static final String VALIDATION_SCHEMA
					= Finals.constant("http://apache.org/xml/features/validation/schema");

			/**
			 * {@code true}: Enable full schema grammar constraint checking, including
			 * checking which may be time-consuming or memory intensive. Currently, unique
			 * particle attribution constraint checking and particle derivation restriction
			 * checking are controlled by this option.
			 *
			 * <p>
			 * {@code false} (default): Disable full constraint checking.
			 *
			 * <p>
			 * Note: This feature checks the Schema grammar itself for additional errors
			 * that are time-consuming or memory intensive. It does not affect the level of
			 * checking performed on document instances that use Schema grammars.
			 */
			public static final String VALIDATION_SCHEMA_FULL_CHECKING
					= Finals.constant("http://apache.org/xml/features/validation/schema-full-checking");

			/**
			 * {@code true} (default): Expose via SAX and DOM XML Schema normalized values
			 * for attributes and elements.
			 *
			 * <p>
			 * {@code false}: Expose the infoset values
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * <p>
			 * Note: XML Schema normalized values will be exposed only if both schema
			 * validation and validation features are set to {@code true}.
			 *
			 * @see #VALIDATION
			 * @see #VALIDATION_SCHEMA
			 * @see #VALIDATION_SCHEMA_ELEMENT_DEFAULT
			 */
			public static final String VALIDATION_SCHEMA_NORMALIZED_VALUE
					= Finals.constant("http://apache.org/xml/features/validation/schema/normalized-value");

			/**
			 * {@code true} (default): Send XML Schema element default values via
			 * {@code characters()}.
			 *
			 * <p>
			 * {@code false}: Do not send XML Schema default values in XNI
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * <p>
			 * Note: XML Schema default values will be send via {@code characters()} if both
			 * schema validation and validation features are set to {@code true}.
			 *
			 * @see #VALIDATION
			 * @see #VALIDATION_SCHEMA
			 * @see #VALIDATION_SCHEMA_NORMALIZED_VALUE
			 */
			public static final String VALIDATION_SCHEMA_ELEMENT_DEFAULT
					= Finals.constant("http://apache.org/xml/features/validation/schema/element-default");

			/**
			 * {@code true} (default): Augment Post-Schema-Validation-Infoset.
			 *
			 * <p>
			 * {@code false}: Do not augment Post-Schema-Validation-Infoset.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * <p>
			 * Note: This feature can be turned off to improve parsing performance.
			 *
			 * @see #VALIDATION
			 * @see #VALIDATION_SCHEMA
			 */
			public static final String VALIDATION_SCHEMA_AUGMENT_POST_SCHEMA_VALIDATION_INFOSET
					= Finals.constant("http://apache.org/xml/features/validation/schema/augment-psvi");

			/**
			 * {@code true}: {@code xsi:type} attributes will be ignored until a global
			 * element declaration has been found, at which point {@code xsi:type}
			 * attributes will be processed on the element for which the global element
			 * declaration was found as well as its descendants.
			 *
			 * <p>
			 * {@code false} (default): Do not ignore {@code xsi:type} attributes.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * @see #VALIDATION
			 * @see #VALIDATION_SCHEMA
			 * @since Xerces-J 2.8.0
			 */
			public static final String VALIDATION_SCHEMA_IGNORE_XSI_TYPE_UNTIL_ELEMENT_DECLARATION = Finals
					.constant("http://apache.org/xml/features/validation/schema/ignore-xsi-type-until-elemdecl");

			/**
			 * {@code true}: Enable generation of synthetic annotations. A synthetic
			 * annotation will be generated when a schema component has non-schema
			 * attributes but no child annotation.
			 *
			 * <p>
			 * {@code false} (default): Do not generate synthetic annotations.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * @see #VALIDATION
			 * @see #VALIDATION_SCHEMA
			 * @see #VALIDATE_ANNOTATIONS
			 * @since Xerces-J 2.7.0
			 */
			public static final String GENERATE_SYNTHETIC_ANNOTATIONS
					= Finals.constant("http://apache.org/xml/features/generate-synthetic-annotations");

			/**
			 * {@code true}: Schema annotations will be laxly validated against available
			 * schema components.
			 *
			 * <p>
			 * {@code false} (default): Do not validate schema annotations.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * @see #VALIDATION
			 * @see #VALIDATION_SCHEMA
			 * @see #GENERATE_SYNTHETIC_ANNOTATIONS
			 * @since Xerces-J 2.7.0
			 */
			public static final String VALIDATE_ANNOTATIONS
					= Finals.constant("http://apache.org/xml/features/validate-annotations");

			/**
			 * {@code true}: All schema location hints will be used to locate the components
			 * for a given target namespace.
			 *
			 * <p>
			 * {@code false} (default): Only the first schema location hint encountered by
			 * the processor will be used to locate the components for a given target
			 * namespace.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * @since Xerces-J 2.7.0
			 */
			public static final String HONOUR_ALL_SCHEMA_LOCATIONS
					= Finals.constant("http://apache.org/xml/features/honour-all-schemaLocations");

			/**
			 * {@code true} (default): Include external general entities.
			 *
			 * <p>
			 * {@code false}: Do not include external general entities.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * @see #EXTERNAL_PARAMETER_ENTITIES
			 */
			public static final String EXTERNAL_GENERAL_ENTITIES
					= Finals.constant("http://xml.org/sax/features/external-general-entities");

			/**
			 * {@code true} (default): Include external parameter entities and the external
			 * DTD subset.
			 *
			 * <p>
			 * {@code false}: Do not include external parameter entities or the external DTD
			 * subset.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * @see #EXTERNAL_GENERAL_ENTITIES
			 */
			public static final String EXTERNAL_PARAMETER_ENTITIES
					= Finals.constant("http://xml.org/sax/features/external-parameter-entities");

			/**
			 * {@code true}: Construct an optimal representation for DTD content models to
			 * significantly reduce the likelihood a {@link java.lang.StackOverflowError}
			 * will occur when large content models are processed.
			 *
			 * <p>
			 * {@code false} (default): Do not invest processing time to construct an
			 * optimal representation for DTD content models.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * <p>
			 * Note: Enabling this feature may cost your application some performance when
			 * DTDs are processed so it is recommended that it only be turned on when
			 * necessary.
			 *
			 * @since Xerces-J 2.8.0
			 */
			public static final String VALIDATION_BALANCE_SYNTAX_TREES
					= Finals.constant("http://apache.org/xml/features/validation/balance-syntax-trees");

			/**
			 * {@code true} (default): Enable checking of ID/IDREF constraints.
			 *
			 * <p>
			 * {@code false}: Disable checking of ID/IDREF constraints. Validation will not
			 * fail if there are non-unique ID values or dangling IDREF values in the
			 * document.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * <p>
			 * Note: This feature only applies to schema validation.
			 *
			 * @see #VALIDATION
			 * @see #VALIDATION_SCHEMA
			 * @since Xerces-J 2.8.0
			 */
			public static final String VALIDATION_ID_IDREF_CHECKING
					= Finals.constant("http://apache.org/xml/features/validation/id-idref-checking");

			/**
			 * {@code true} (default): Enable identity constraint checking.
			 *
			 * <p>
			 * {@code false}: Disable identity constraint checking.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * @see #VALIDATION
			 * @see #VALIDATION_SCHEMA
			 * @since Xerces-J 2.8.0
			 */
			public static final String VALIDATION_IDENTITY_CONSTRAINT_CHECKING
					= Finals.constant("http://apache.org/xml/features/validation/identity-constraint-checking");

			/**
			 * {@code true}: Enable XSD 1.1 CTA full XPath 2.0 checking.
			 *
			 * <p>
			 * {@code false} (default): Disable XSD 1.1 CTA full XPath 2.0 checking.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * <p>
			 * Note: Setting this feature to {@code true}, would allow CTA XPath expressions
			 * to have full XPath 2.0 syntax. The default is to recognize the CTA XPath
			 * subset, defined by XSD 1.1 language. If the CTA XPath expressions in a schema
			 * document falls within the XPath subset, then the use of default Xerces CTA
			 * XPath processor may result in an efficient schema processing.
			 *
			 * @see #VALIDATION
			 * @see #VALIDATION_SCHEMA
			 * @since Xerces-J 2.12.0
			 */
			public static final String VALIDATION_CTA_FULL_XPATH_CHECKING
					= Finals.constant("http://apache.org/xml/features/validation/cta-full-xpath-checking");

			/**
			 * {@code true}: Enable occurrence of comments and PIs within XPath Data Model
			 * (XDM) instance, for XSD 1.1 assertion processing.
			 *
			 * <p>
			 * {@code false} (default): Disable occurrence of comments and PIs within XPath
			 * Data Model (XDM) instance, for XSD 1.1 assertion processing.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * <p>
			 * Note: Setting this feature to {@code true}, would allow assertions to access
			 * comments and PIs from an XML instance document and correspondingly test for
			 * their absence, presence or do string processing on them for the purpose of
			 * XSD validation.
			 *
			 * @see #VALIDATION
			 * @see #VALIDATION_SCHEMA
			 * @since Xerces-J 2.12.0
			 */
			public static final String VALIDATION_ASSERT_COMMENTS_AND_PI_CHECKING
					= Finals.constant("http://apache.org/xml/features/validation/assert-comments-and-pi-checking");

			/**
			 * {@code true} (default): Check that each value of type {@code ENTITY} matches
			 * the name of an unparsed entity declared in the DTD.
			 *
			 * <p>
			 * {@code false}: Do not check that each value of type {@code ENTITY} matches
			 * the name of an unparsed entity declared in the DTD.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * <p>
			 * Note: This feature only applies to schema validation.
			 *
			 * @see #VALIDATION
			 * @see #VALIDATION_SCHEMA
			 * @since Xerces-J 2.8.0
			 */
			public static final String VALIDATION_UNPARSED_ENTITY_CHECKING
					= Finals.constant("http://apache.org/xml/features/validation/unparsed-entity-checking");

			/**
			 * {@code true}: Report a warning when a duplicate attribute is re-declared.
			 *
			 * <p>
			 * {@code false} (default): Do not report a warning when a duplicate attribute
			 * is re-declared.
			 */
			public static final String VALIDATION_WARN_ON_DUPLICATE_ATTRIBUTE_DEFINITION
					= Finals.constant("http://apache.org/xml/features/validation/warn-on-duplicate-attdef");

			/**
			 * {@code true}: Report a warning if an element referenced in a content model is
			 * not declared.
			 *
			 * <p>
			 * {@code false} (default): Do not report a warning if an element referenced in
			 * a content model is not declared.
			 */
			public static final String VALIDATION_WARN_ON_UNDECLARED_ELEMENT_DEFINITION
					= Finals.constant("http://apache.org/xml/features/validation/warn-on-undeclared-elemdef");

			/**
			 * {@code true}: Report a warning for duplicate entity declaration.
			 *
			 * <p>
			 * {@code false} (default): Do not report warning for duplicate entity
			 * declaration.
			 */
			public static final String WARN_ON_DUPLICATE_ENTITY_DEFINITION
					= Finals.constant("http://apache.org/xml/features/warn-on-duplicate-entitydef");

			/**
			 * {@code true}: Allow Java encoding names in {@code XMLDecl} and
			 * {@code TextDecl} line.
			 *
			 * <p>
			 * {@code false} (default): Do not allow Java encoding names in {@code XMLDecl}
			 * and {@code TextDecl} line.
			 *
			 * <p>
			 * Note: A {@code true} value for this feature allows the encoding of the file
			 * to be specified as a Java encoding name as well as the standard ISO encoding
			 * name. Be aware that other parsers may not be able to use Java encoding names.
			 * If this feature is set to {@code false}, an error will be generated if Java
			 * encoding names are used.
			 */
			public static final String ALLOW_JAVA_ENCODINGS
					= Finals.constant("http://apache.org/xml/features/allow-java-encodings");

			/**
			 * {@code true}: Attempt to continue parsing after a fatal error.
			 *
			 * <p>
			 * {@code false} (default): Stops parse on first fatal error.
			 *
			 * <p>
			 * Note: The behavior of the parser when this feature is set to {@code true} is
			 * undetermined! Therefore use this feature with extreme caution because the
			 * parser may get stuck in an infinite loop or worse.
			 */
			public static final String CONTINUE_AFTER_FATAL_ERROR
					= Finals.constant("http://apache.org/xml/features/continue-after-fatal-error");

			/**
			 * {@code true} (default): Load the DTD and use it to add default attributes and
			 * set attribute types when parsing.
			 *
			 * <p>
			 * {@code false}: Build the grammar but do not use the default attributes and
			 * attribute types information it contains.
			 *
			 * <p>
			 * Note: This feature is always on when validation is on.
			 *
			 * @see #VALIDATION
			 * @see #NONVALIDATING_LOAD_EXTERNAL_DTD
			 */
			public static final String NONVALIDATING_LOAD_DTD_GRAMMAR
					= Finals.constant("http://apache.org/xml/features/nonvalidating/load-dtd-grammar");

			/**
			 * {@code true} (default): Load the external DTD.
			 *
			 * <p>
			 * {@code false}: Ignore the external DTD completely.
			 *
			 * <p>
			 * Note: This feature is always on when validation is on.
			 *
			 * @see #VALIDATION
			 * @see #NONVALIDATING_LOAD_DTD_GRAMMAR
			 */
			public static final String NONVALIDATING_LOAD_EXTERNAL_DTD
					= Finals.constant("http://apache.org/xml/features/nonvalidating/load-external-dtd");

			/**
			 * {@code true}: Notifies the handler of character reference boundaries in the
			 * document via the {@code start}/{@code endEntity} callbacks.
			 *
			 * <p>
			 * {@code false} (default): Does not notify of character reference boundaries.
			 *
			 * @see #SCANNER_NOTIFY_BUILTIN_REFERENCES
			 */
			public static final String SCANNER_NOTIFY_CHARACTER_REFERENCES
					= Finals.constant("http://apache.org/xml/features/scanner/notify-char-refs");

			/**
			 * {@code true}: Notifies the handler of built-in entity boundaries (e.g
			 * {@code &amp;}) in the document via the {@code start}/{@code endEntity}
			 * callbacks.
			 *
			 * <p>
			 * {@code false} (default): Does not notify of built-in entity boundaries.
			 *
			 * @see #SCANNER_NOTIFY_CHARACTER_REFERENCES
			 */
			public static final String SCANNER_NOTIFY_BUILTIN_REFERENCES
					= Finals.constant("http://apache.org/xml/features/scanner/notify-builtin-refs");

			/**
			 * {@code true}: A fatal error is thrown if the incoming document contains a
			 * {@code DOCTYPE} declaration.
			 *
			 * <p>
			 * {@code false} (default): {@code DOCTYPE} declaration is allowed.
			 *
			 * @since Xerces-J 2.3.0
			 */
			public static final String DISALLOW_DOCTYPE_DECLARATION
					= Finals.constant("http://apache.org/xml/features/disallow-doctype-decl");

			/**
			 * {@code true}: Requires that a URI has to be provided where a URI is expected.
			 *
			 * <p>
			 * {@code false} (default): Some invalid URI's are accepted as valid values when
			 * a URI is expected. Examples include: using platform dependent file separator
			 * in place of {@code /}; using Windows/DOS path names like {@code C:\blah} and
			 * {@code \\host\dir\blah}; using invalid URI characters (space, for example).
			 *
			 * <p>
			 * Note: It's recommended to set this feature to {@code true} if you want your
			 * application/documents to be truly portable across different XML processors.
			 *
			 * @since Xerces-J 2.3.0
			 */
			public static final String STANDARD_URI_CONFORMANT
					= Finals.constant("http://apache.org/xml/features/standard-uri-conformant");

			/**
			 * {@code true}: Enable XInclude processing.
			 *
			 * <p>
			 * {@code false} (default): Do not perform XInclude processing.
			 *
			 * @see #XINCLUDE_FIXUP_BASE_URIS
			 * @see #XINCLUDE_FIXUP_LANGUAGE
			 * @since Xerces-J 2.7.0
			 */
			public static final String XINCLUDE = Finals.constant("http://apache.org/xml/features/xinclude");

			/**
			 * {@code true} (default): Perform base URI fixup as specified by the XInclude
			 * Recommendation.
			 *
			 * <p>
			 * {@code false}: Do not perform base URI fixup. The XInclude processor will not
			 * add {@code xml:base} attributes.
			 *
			 * @see #XINCLUDE
			 * @since Xerces-J 2.7.0
			 */
			public static final String XINCLUDE_FIXUP_BASE_URIS
					= Finals.constant("http://apache.org/xml/features/xinclude/fixup-base-uris");

			/**
			 * {@code true} (default): Perform language fixup as specified by the XInclude
			 * Recommendation.
			 *
			 * <p>
			 * {@code false}: Do not perform language fixup. The XInclude processor will not
			 * add {@code xml:lang} attributes.
			 *
			 * @see #XINCLUDE
			 * @since Xerces-J 2.7.0
			 */
			public static final String XINCLUDE_FIXUP_LANGUAGE
					= Finals.constant("http://apache.org/xml/features/xinclude/fixup-language");
		}

		/**
		 * DOM Features
		 */
		@UtilityClass
		@SuppressWarnings("PMD.ShortClassName")
		public static class DOM {
			/**
			 * {@code true} (default): Lazily expand the DOM nodes.
			 *
			 * <p>
			 * {@code false}: Fully expand the DOM nodes.
			 *
			 * <p>
			 * Note: In the {@link org.w3c.dom.ls.LSParser} implementation the default value
			 * of this feature is {@code false}.
			 *
			 * <p>
			 * When this feature is set to {@code true}, the DOM nodes in the returned
			 * document are expanded as the tree is traversed. This allows the parser to
			 * return a document faster than if the tree is fully expanded during parsing
			 * and improves memory usage when the whole tree is not traversed.
			 */
			public static final String DEFER_NODE_EXPANSION
					= Finals.constant("http://apache.org/xml/features/dom/defer-node-expansion");

			/**
			 * {@code true} (default): Create {@link org.w3c.dom.EntityReference} nodes in
			 * the DOM tree. The {@link org.w3c.dom.EntityReference} nodes and their child
			 * nodes will be read-only.
			 *
			 * <p>
			 * {@code false}: Do not create {@link org.w3c.dom.EntityReference} nodes in the
			 * DOM tree. No {@link org.w3c.dom.EntityReference} nodes will be created, only
			 * the nodes corresponding to their fully expanded substitution text will be
			 * created.
			 *
			 * <p>
			 * Note: This feature only affects the appearance of
			 * {@link org.w3c.dom.EntityReference} nodes in the DOM tree. The document will
			 * always contain the entity reference child nodes.
			 */
			public static final String CREATE_ENTITY_REFERENCE_NODES
					= Finals.constant("http://apache.org/xml/features/dom/create-entity-ref-nodes");

			/**
			 * {@code true} (default): Include text nodes that can be considered "ignorable
			 * whitespace" in the DOM tree.
			 *
			 * <p>
			 * {@code false}: Do not include ignorable whitespace in the DOM tree.
			 *
			 * <p>
			 * Note: The only way that the parser can determine if text is ignorable is by
			 * reading the associated grammar and having a content model for the document.
			 * When ignorable whitespace text nodes are included in the DOM tree, they will
			 * be flagged as ignorable. The ignorable flag can be queried by calling the
			 * {@link org.w3c.dom.Text#isElementContentWhitespace()} method. This feature is
			 * relevant only when the grammar is DTD.
			 */
			public static final String INCLUDE_IGNORABLE_WHITESPACE
					= Finals.constant("http://apache.org/xml/features/dom/include-ignorable-whitespace");
		}

		/**
		 * SAX Features
		 */
		@UtilityClass
		@SuppressWarnings("PMD.ShortClassName")
		public static class SAX {
			/**
			 * {@code true}: Report the original prefixed names and attributes used for
			 * namespace declarations.
			 *
			 * <p>
			 * {@code false} (default): Do not report attributes used for Namespace
			 * declarations, and optionally do not report original prefixed names.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 */
			public static final String NAMESPACE_PREFIXES
					= Finals.constant("http://xml.org/sax/features/namespace-prefixes");

			/**
			 * {@code true} (default): All element names, prefixes, attribute names,
			 * namespace URIs, and local names are internalized using the
			 * {@link String#intern()} method.
			 *
			 * <p>
			 * {@code false}: Names are not necessarily internalized.
			 *
			 * <p>
			 * Access: read-only (parsing), read-write (not parsing)
			 *
			 * <p>
			 * Note: Xerces-J always internalizes all strings mentioned above using the
			 * {@link String#intern()} method. This feature can only be set to {@code true}.
			 */
			public static final String STRING_INTERNING
					= Finals.constant("http://xml.org/sax/features/string-interning");

			/**
			 * {@code true} (default): Report the beginning and end of parameter entities to
			 * a registered {@link org.xml.sax.ext.LexicalHandler}.
			 *
			 * <p>
			 * {@code false}: Do not report the beginning and end of parameter entities to a
			 * registered {@link org.xml.sax.ext.LexicalHandler}.
			 *
			 * @since Xerces-J 2.7.0
			 */
			public static final String LEXICAL_HANDLER_PARAMETER_ENTITIES
					= Finals.constant("http://xml.org/sax/features/lexical-handler/parameter-entities");

			/**
			 * {@code true}: The document specified {@code standalone="yes"} in its XML
			 * declaration.
			 *
			 * <p>
			 * {@code false}: The document specified {@code standalone="no"} in its XML
			 * declaration or the standalone document declaration was absent.
			 *
			 * <p>
			 * Access: read-only (parsing), none (not parsing)
			 *
			 * @since Xerces-J 2.7.0
			 */
			public static final String IS_STANDALONE = Finals.constant("http://xml.org/sax/features/is-standalone");

			/**
			 * {@code true} (default): The system identifiers passed to the
			 * {@code notationDecl}, {@code unparsedEntityDecl}, and
			 * {@code externalEntityDecl} events will be absolutized relative to their base
			 * URIs before reporting.
			 *
			 * <p>
			 * {@code false}: System identifiers in declarations will not be absolutized
			 * before reporting.
			 *
			 * <p>
			 * Note: This feature does not apply to
			 * {@link org.xml.sax.EntityResolver#resolveEntity(String, String)}, which is
			 * not used to report declarations, or to
			 * {@link org.xml.sax.ext.LexicalHandler#startDTD(String, String, String)},
			 * which already provides the non-absolutized URI.
			 *
			 * @since Xerces-J 2.7.0
			 */
			public static final String RESOLVE_DTD_URIS
					= Finals.constant("http://xml.org/sax/features/resolve-dtd-uris");

			/**
			 * {@code true}: Perform Unicode normalization checking (as described in section
			 * 2.13 and Appendix B of the XML 1.1 Recommendation) and report normalization
			 * errors.
			 *
			 * <p>
			 * {@code false} (default): Do not report Unicode normalization errors.
			 *
			 * <p>
			 * Note: As there is currently no support for Unicode normalization checking,
			 * this feature can only be set to {@code false}.
			 *
			 * @since Xerces-J 2.7.0
			 */
			public static final String UNICODE_NORMALIZATION_CHECKING
					= Finals.constant("http://xml.org/sax/features/unicode-normalization-checking");

			/**
			 * {@code true}: The Attributes objects passed by the parser in
			 * {@link org.xml.sax.ContentHandler#startElement(String, String, String, org.xml.sax.Attributes)}
			 * implement the {@link org.xml.sax.ext.Attributes2} interface.
			 *
			 * <p>
			 * {@code false}: The Attributes objects passed by the parser do not implement
			 * the {@link org.xml.sax.ext.Attributes2} interface.
			 *
			 * <p>
			 * Access: read-only
			 *
			 * <p>
			 * Note: Xerces-J will always report Attributes objects that also implement
			 * {@link org.xml.sax.ext.Attributes2} so the value of this feature will always
			 * be {@code true}.
			 *
			 * @since Xerces-J 2.7.0
			 */
			public static final String USE_ATTRIBUTES_2
					= Finals.constant("http://xml.org/sax/features/use-attributes2");

			/**
			 * {@code true}: The {@link org.xml.sax.Locator} objects passed by the parser in
			 * {@link org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)}
			 * implement the {@link org.xml.sax.ext.Locator2} interface.
			 *
			 * <p>
			 * {@code false}: The {@link org.xml.sax.Locator} objects passed by the parser
			 * do not implement the {@link org.xml.sax.ext.Locator2} interface.
			 *
			 * <p>
			 * Access: read-only
			 *
			 * <p>
			 * Note: Xerces-J will always report {@link org.xml.sax.Locator} objects that
			 * also implement {@link org.xml.sax.ext.Locator2} so the value of this feature
			 * will always be {@code true}.
			 *
			 * @since Xerces-J 2.7.0
			 */
			public static final String USE_LOCATOR_2 = Finals.constant("http://xml.org/sax/features/use-locator2");

			/**
			 * {@code true}: When the namespace-prefixes feature is set to {@code true},
			 * namespace declaration attributes will be reported as being in the
			 * {@code http://www.w3.org/2000/xmlns/} namespace.
			 *
			 * <p>
			 * {@code false} (default): Namespace declaration attributes are reported as
			 * having no namespace.
			 *
			 * @see General#NAMESPACES
			 * @see #NAMESPACE_PREFIXES
			 * @since Xerces-J 2.7.0
			 */
			public static final String XMLNS_URIS = Finals.constant("http://xml.org/sax/features/xmlns-uris");

			/**
			 * {@code true}: The parser supports both XML 1.0 and XML 1.1.
			 *
			 * <p>
			 * {@code false}: The parser supports only XML 1.0.
			 *
			 * <p>
			 * Access: read-only
			 *
			 * <p>
			 * Note: The value of this feature will depend on whether the parser
			 * configuration owned by the SAX parser is known to support XML 1.1.
			 *
			 * @since Xerces-J 2.7.0
			 */
			public static final String XML_1_1 = Finals.constant("http://xml.org/sax/features/xml-1.1");
		}

		/**
		 * XInclude Features (experimental)
		 */
		@UtilityClass
		public static class XInclude {
			/**
			 * {@code true} (default): Allows {@code notationDecl} and
			 * {@code unparsedEntityDecl} events to be sent in the XNI pipeline after the
			 * {@code endDTD} event has been sent.
			 *
			 * <p>
			 * {@code false}: Ensures that {@code notationDecl} and
			 * {@code unparsedEntityDecl} events are not sent after the {@code endDTD} event
			 * has been sent (default SAX behavior).
			 *
			 * <p>
			 * Note: The default value for this feature is {@code true}, except when using
			 * SAX, because SAX requires that no DTD events be sent after the {@code endDTD}
			 * event. Thus, in order to maintain SAX compatibility, this feature cannot be
			 * {@code true} by default for SAX. Setting this feature to {@code false} can
			 * result in loss of information, if notations and unparsed entities were needed
			 * to resolve references in the document.
			 *
			 * <p>
			 * This feature is only relevant when XInclude processing is being done. Due to
			 * the nature of implementing XInclude in a stream-based API, it is not possible
			 * to know the complete set of required unparsed entities and notations before
			 * the {@code endDTD} event from the source document is sent. If an
			 * XIncludeHandler is not present in your pipeline, the value of this feature is
			 * irrelevant.
			 *
			 * <p>
			 * This feature is currently experimental, and might be removed or changed in
			 * the next release. If you have any concerns or suggestions about its use,
			 * please contact the {@code j-users@xerces.apache.org} mailing list.
			 *
			 * @since Xerces-J 2.5.0
			 */
			public static final String ALLOW_DTD_EVENTS_AFTER_ENDDTD
					= Finals.constant("http://xml.org/sax/features/allow-dtd-events-after-endDTD");
		}
	}
}
