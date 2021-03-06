package org.dromelvan.tools.parser.javascript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class JavaScriptParser<T extends JavaScriptVariables> {

	private ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
	@Inject
	private Provider<T> javaScriptVariablesProvider;
	private final static Logger logger = LoggerFactory.getLogger(JavaScriptParser.class);

	public T parse(Document document) {
		T javaScriptVariables = javaScriptVariablesProvider.get();

		for (Element element : document.getElementsByTag("script")) {
			try {
				scriptEngine.eval(element.data());
			} catch (ScriptException e) {
				logger.debug("Could not parse javascript: {}\n{}.", element.data(), e.getMessage());
			}
		}

		Bindings bindings = scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE);

		for (String key : bindings.keySet()) {
			Object value = bindings.get(key);
			javaScriptVariables.put(key, parseValue(value));
		}

		javaScriptVariables.init();
		return javaScriptVariables;
	}

	private Object parseValue(Object value) {
		if (value instanceof Map) {
			return parseMap((Map) value);
		}
		return value;
	}

	private Object parseMap(Map map) {
		int maxIndex = -1;
		for (Object key : map.keySet()) {
			try {
				int index = Integer.parseInt((String) key);
				if (index > maxIndex) {
					maxIndex = index;
				}
			} catch (Exception e) {
				maxIndex = -1;
				break;
			}
		}

		if (maxIndex >= 0) {
			List values = new ArrayList();
			for (int i = 0; i <= maxIndex; ++i) {
				Object value = parseValue(map.get(i));
				values.add(value);
			}
			return values;
		} else {
			Map values = new HashMap();
			for (Object key : map.keySet()) {
				Object value = map.get(key);
				values.put(key, parseValue(value));
			}
			return values;
		}
	}
}
