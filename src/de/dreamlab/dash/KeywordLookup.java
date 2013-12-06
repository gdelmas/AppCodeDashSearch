package de.dreamlab.dash;

import com.intellij.ide.util.PropertiesComponent;
import it.frob.dash.DefaultsProvider;
import it.frob.dash.DocsetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class KeywordLookup {

	private static final KeywordLookup INSTANCE = new KeywordLookup();
    private static final String CONFIG_KEYWORDS = "DASH_PLUGIN_KEYWORDS";
    private final Map<String, String> typeMap = new HashMap<String, String>();
    private final Map<String, String> extensionMap = new HashMap<String, String>();

    private KeywordLookup() {
		loadDefaults();

		try {
			loadConfiguration();
		} catch (IllegalStateException exception) {
			PropertiesComponent.getInstance().unsetValue(CONFIG_KEYWORDS);
		}
    }

	private void loadDefaults() {
		DefaultsProvider defaultsProvider = DefaultsProvider.getInstance();
		Map<String, String> defaults = defaultsProvider.getDefaults();

		for (Entry<String, String> entry : defaults.entrySet()) {
			if (entry.getKey().startsWith(".")) {
				extensionMap.put(entry.getKey().substring(1), entry.getValue());
			} else {
				typeMap.put(entry.getKey(), entry.getValue());
			}
		}
	}

	private void loadConfiguration() {
		String propertiesComponent = PropertiesComponent.getInstance().getValue(
				CONFIG_KEYWORDS, "");

		if ("".equals(propertiesComponent)) {
			PropertiesComponent.getInstance().unsetValue(CONFIG_KEYWORDS);
			return;
		}

		Map<String, String> types = new HashMap<String, String>();
		Map<String, String> extensions = new HashMap<String, String>();

		for (String item : propertiesComponent.split(";")) {
			String[] keyValue = item.split("=");
			if (keyValue.length != 2) {
				throw new IllegalStateException("Invalid key/value pair");
			}

			String key = keyValue[0].trim();
			if ("".equals(key)) {
				throw new IllegalStateException("Empty key");
			}

			String value = keyValue[1].trim();
			if ("".equals(value)) {
				throw new IllegalStateException("Empty value");
			}

			if (key.startsWith(".")) {
				if (extensions.containsKey(key.substring(1))) {
					throw new IllegalStateException("Duplicate key");
				}

				extensions.put(key.substring(1), value);
			} else {
				if (types.containsKey(key)) {
					throw new IllegalStateException("Duplicate key");
				}

				types.put(key, value);
			}
		}

		typeMap.clear();
		typeMap.putAll(types);
		extensionMap.clear();
		extensionMap.putAll(extensions);
	}

	public void updateValues(List<DocsetMapping> values) {
		typeMap.clear();
		extensionMap.clear();

		if (values.size() == 0) {
			loadDefaults();
		} else {
			for (DocsetMapping docset : values) {
				if (docset.isExtension()) {
					extensionMap.put(docset.getType(), docset.getDocset());
				} else {
					typeMap.put(docset.getType(), docset.getDocset());
				}
			}
		}
	}

	public void writeValues() {
		StringBuilder outputValue = new StringBuilder();
		for (Entry<String, String> type : typeMap.entrySet()) {
			outputValue.append(String.format("%s=%s;", type.getKey(), type.getValue()));
		}
		for (Entry<String, String> extension : extensionMap.entrySet()) {
			outputValue.append(String.format("%s=%s;", extension.getKey(), extension.getValue()));
		}

		PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
		propertiesComponent.setValue(CONFIG_KEYWORDS, outputValue.toString());
	}

	public List<DocsetMapping> getList() {
		List<DocsetMapping> list = new ArrayList<DocsetMapping>();

		for (Entry<String, String> entries : getValues().entrySet()) {
			list.add(new DocsetMapping(entries.getKey(), entries.getValue()));
		}

		return list;
	}

	TreeMap<String, String> getValues() {
		TreeMap<String, String> output = new TreeMap<String, String>();
		output.putAll(typeMap);
		output.putAll(extensionMap);

		return output;
	}

    public String findKeyword(String type, String extension) {
		return extensionMap.containsKey(extension) ?
				extensionMap.get(extension) : typeMap.get(cleanType(type));
    }

    public String cleanType(String type) {
        return type.replaceFirst("\\(.*\\)", "").replace("files", "").trim();
    }

	public static KeywordLookup getInstance() {
		return INSTANCE;
	}
}
