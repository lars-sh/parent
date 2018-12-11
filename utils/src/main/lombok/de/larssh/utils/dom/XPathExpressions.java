package de.larssh.utils.dom;

import java.util.List;
import java.util.Optional;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link XPathExpression}.
 */
@UtilityClass
public final class XPathExpressions {
	/**
	 * Evaluates the compiled XPath expression in the specified context and returns
	 * a {@link Boolean}.
	 *
	 * @param node       the starting context
	 * @param expression the XPath expression
	 * @return boolean evaluation result
	 * @throws XPathExpressionException If the expression cannot be evaluated.
	 */
	public static boolean getBoolean(final Node node, final XPathExpression expression)
			throws XPathExpressionException {
		return (boolean) expression.evaluate(node, XPathConstants.BOOLEAN);
	}

	/**
	 * Evaluates the compiled XPath expression in the specified context and
	 * optionally returns a {@link Node}.
	 *
	 * @param            <T> expected node type
	 * @param node       the starting context
	 * @param expression the XPath expression
	 * @return evaluation result as optional {@code T extends Node}
	 * @throws XPathExpressionException If the expression cannot be evaluated.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Node> Optional<T> getNode(final Node node, final XPathExpression expression)
			throws XPathExpressionException {
		return Optional.ofNullable((T) expression.evaluate(node, XPathConstants.NODE));
	}

	/**
	 * Evaluates the compiled XPath expression in the specified context and returns
	 * a list of {@link Node}.
	 *
	 * @param            <T> expected node type
	 * @param node       the starting context
	 * @param expression the XPath expression
	 * @return evaluation result as list of {@code T extends Node}
	 * @throws XPathExpressionException If the expression cannot be evaluated.
	 */
	public static <T extends Node> List<T> getNodes(final Node node, final XPathExpression expression)
			throws XPathExpressionException {
		return NodeLists.<T>asList(getNodeList(node, expression));
	}

	/**
	 * Evaluates the compiled XPath expression in the specified context and returns
	 * a {@link NodeList}.
	 *
	 * @param node       the starting context
	 * @param expression the XPath expression
	 * @return evaluation result as {@link NodeList}
	 * @throws XPathExpressionException If the expression cannot be evaluated.
	 */
	public static NodeList getNodeList(final Node node, final XPathExpression expression)
			throws XPathExpressionException {
		return (NodeList) expression.evaluate(node, XPathConstants.NODESET);
	}

	/**
	 * Evaluates the compiled XPath expression in the specified context and returns
	 * a {@link Double}.
	 *
	 * @param node       the starting context
	 * @param expression the XPath expression
	 * @return evaluation result as {@link Double}
	 * @throws XPathExpressionException If the expression cannot be evaluated.
	 */
	public static double getNumber(final Node node, final XPathExpression expression) throws XPathExpressionException {
		return (double) expression.evaluate(node, XPathConstants.NUMBER);
	}

	/**
	 * Evaluates the compiled XPath expression in the specified context and returns
	 * a {@link String}.
	 *
	 * @param node       the starting context
	 * @param expression the XPath expression
	 * @return evaluation result as {@link String}
	 * @throws XPathExpressionException If the expression cannot be evaluated.
	 */
	public static String getString(final Node node, final XPathExpression expression) throws XPathExpressionException {
		return (String) expression.evaluate(node, XPathConstants.STRING);
	}
}
