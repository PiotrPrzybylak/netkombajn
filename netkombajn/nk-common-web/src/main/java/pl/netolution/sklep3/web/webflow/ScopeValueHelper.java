package pl.netolution.sklep3.web.webflow;

import java.util.LinkedList;
import java.util.List;

import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import org.springframework.webflow.execution.ScopeType;

public class ScopeValueHelper {

	static List<ScopeType> scopeSequence;
	static {
		scopeSequence = new LinkedList<ScopeType>();
		scopeSequence.add(ScopeType.REQUEST);
		scopeSequence.add(ScopeType.FLASH);
		scopeSequence.add(ScopeType.VIEW);
		scopeSequence.add(ScopeType.FLOW);
		scopeSequence.add(ScopeType.CONVERSATION);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getFromScope(final String name, T typeToConvert) {

		for (ScopeType scope : scopeSequence) {
			MutableAttributeMap attributeMap = getAttributeMap(scope);
			T value = (T) getSearchedValue(name, attributeMap);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getFromScope(final String name, final T typeToConvert, final ScopeType scope) {
		checkScope(scope);

		MutableAttributeMap attributeMap = getAttributeMap(scope);
		T value = (T) getSearchedValue(name, attributeMap);

		return value;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getAttributeFromRequestContext(final String name, T typeToConvert) {

		MutableAttributeMap attributeMap = getAttributeMap(ScopeType.REQUEST);
		T value = (T) getSearchedValue(name, attributeMap);
		return value;
	}

	@SuppressWarnings("unchecked")
	private static <T> T getSearchedValue(final String name, MutableAttributeMap map) {
		T value;
		try {
			value = (T) map.get(name);
		} catch (ClassCastException cce) {
			throw new RuntimeException("The value in the scope is not from the correct type.", cce);
		}
		return value;
	}

	private static MutableAttributeMap getAttributeMap(final ScopeType scope) {
		RequestContext context = RequestContextHolder.getRequestContext();
		checkContext(context);

		MutableAttributeMap map = scope.getScope(context);
		if (map == null) {
			throw new RuntimeException("The scope " + scope + " has no value map.");
		}
		return map;
	}

	private static void checkContext(RequestContext context) {
		if (context == null) {
			throw new RuntimeException(
					"Could not retrieve thecontext of the actual request. Please take care that you use that class inside a flow.");
		}
	}

	private static void checkScope(final ScopeType scope) {
		if (scope == null) {
			throw new RuntimeException("A scope must be set.");
		}
	}
}
