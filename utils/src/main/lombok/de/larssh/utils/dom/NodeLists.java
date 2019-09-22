package de.larssh.utils.dom;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for {@link NodeList}.
 */
@UtilityClass
public class NodeLists {
	/**
	 * Converts {@code nodeList} into a {@link List}.
	 *
	 * <p>
	 * If {@code nodeList} is an instance of {@link List} already, it is casted
	 * only. Else all elements of {@code nodeList} are retrieved and copied into a
	 * new list. Therefore lazy behavior of {@link NodeList} implementations might
	 * not work on the returned list.
	 *
	 * @param <T>      expected node type
	 * @param nodeList list of nodes
	 * @return list of {@code nodeList} elements
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Node> List<T> asList(final NodeList nodeList) {
		if (nodeList instanceof List) {
			return (List<T>) nodeList;
		}

		final int size = nodeList.getLength();
		final List<T> list = new ArrayList<>(size);
		for (int index = 0; index < size; index += 1) {
			list.add((T) nodeList.item(index));
		}
		return list;
	}
}
