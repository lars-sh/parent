package de.larssh.utils.xml;

import lombok.experimental.UtilityClass;

/**
 * A collection of attributes to be used for instances of
 * {@link javax.xml.transform.TransformerFactory}.
 */
@UtilityClass
@SuppressWarnings("PMD.ClassNamingConventions")
public class TransformerFactoryAttributes {
	/**
	 * Xalan Attributes
	 *
	 * <p>
	 * Based on <a href=
	 * "http://svn.apache.org/viewvc/xalan/site/docs/xalan/xalan-j/features.html?r1=1595241">revision
	 * 1595241</a> of the <a href=
	 * "https://xml.apache.org/xalan-j/features.html#factoryattribute">official
	 * Xalan documentation</a>
	 */
	@UtilityClass
	public static class Xalan {
		/**
		 * Optimize stylesheet processing. By default, this attribute is set to
		 * {@code true}. You may need to set it to {@code false} for tooling
		 * applications. For more information, see
		 * <a href="https://xml.apache.org/xalan-j/dtm.html#optimize">DTM optimize</a>.
		 */
		public static final TransformerFactoryAttribute<Boolean> OPTIMIZE
				= new TransformerFactoryAttribute<>("http://xml.apache.org/xalan/features/optimize");

		/**
		 * Produce output incrementally, rather than waiting to finish parsing the input
		 * before generating any output. By default this attribute is set to
		 * {@code false}. You can turn this attribute on to transform large documents
		 * where the stylesheet structure is optimized to execute individual templates
		 * without having to parse the entire document. For more information, see
		 * <a href="https://xml.apache.org/xalan-j/dtm.html#incremental">DTM
		 * incremental</a>.
		 */
		public static final TransformerFactoryAttribute<Boolean> INCREMENTAL
				= new TransformerFactoryAttribute<>("http://xml.apache.org/xalan/features/incremental");

		/**
		 * Provide a <a href=
		 * "https://xml.apache.org/xalan-j/apidocs/javax/xml/transform/SourceLocator.html">SourceLocator</a>
		 * that can be used during a transformation to obtain the location of individual
		 * nodes in a source document (system ID, line number, and column number).
		 *
		 * <p>
		 * By default, this attribute is set to {@code false}. Setting this attribute to
		 * {@code true} involves a substantial increase in storage cost per source
		 * document node. If you want to use the <a href=
		 * "https://xml.apache.org/xalan-j/extensionslib.html#nodeinfo">NodeInfo</a>
		 * extension functions (or some other mechanism) to provide this information
		 * during a transform, you must set the attribute to {@code true} before
		 * generating the {@link javax.xml.transform.Transformer} and processing the
		 * stylesheet.
		 */
		public static final TransformerFactoryAttribute<Boolean> SOURCE_LOCATION
				= new TransformerFactoryAttribute<>("http://xml.apache.org/xalan/properties/source-location");
	}
}
