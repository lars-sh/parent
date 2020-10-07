package de.larssh.utils.xml;

import javax.xml.XMLConstants;

import lombok.experimental.UtilityClass;

/**
 * A collection of features to be used for instances of
 * {@link javax.xml.transform.TransformerFactory}.
 */
@UtilityClass
@SuppressWarnings("PMD.ClassNamingConventions")
public class TransformerFactoryFeatures {
	/**
	 * Feature for secure processing.
	 *
	 * @see XmlConstants#FEATURE_SECURE_PROCESSING
	 */
	public static final String SECURE_PROCESSING = XMLConstants.FEATURE_SECURE_PROCESSING;
}
