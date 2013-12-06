package de.dreamlab.dash;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.application.ApplicationInfo;
import it.frob.dash.DocsetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

public class KeywordLookup {

	private static KeywordLookup INSTANCE = new KeywordLookup();

    private static final String CONFIG_KEYWORDS = "DASH_PLUGIN_KEYWORDS";
    private static final String DEFAULT_KEYWORDS = "ActionScript=actionscript;C++=cpp;CoffeeScript=coffee;Perl=perl;CSS=css;Erlang=erlang;Haskell=haskell;HTML=html;JAVA=java7;CLASS=java7;JavaScript=javascript;LESS=less;PHP=php;SASS=sass;Ruby=ruby";
    private static final String ANDROID_STUDIO_PRODUCT_CODE = "AI";

    private HashMap<String, String> typeMap;
    private HashMap<String, String> extensionMap;

    private KeywordLookup()
    {
        initDefaults();

        extensionMap = new HashMap<String, String>();
        typeMap = new HashMap<String, String>();

        String[] associations = PropertiesComponent.getInstance().getValue(CONFIG_KEYWORDS).split(";");
        for ( String association : associations ) {
            String[] values = association.split("=");

            if ( values.length == 2 ) {
                if ( values[0].substring(0, 1).equals(".") ) {
                    extensionMap.put(values[0].substring(1), values[1]);
                }
                else {
                    typeMap.put(values[0], values[1]);
                }
            }
        }
    }

    private void initDefaults()
    {
        /*
            Associations are customizable in "~/Library/Preferences/%IDE_NAME%/options/options.xml" under the property "DASH_PLUGIN_KEYWORDS"
            %IDE_NAME% might be "WebIde60", "IdeaIC12", or "AndroidStudioPreview".

            Values pairs can be provided in a semi-colon delimited list. The value pair consists of FILE_TYPE=KEYWORD
            File type names can be found in the IDE settings. Instead of file types file extensions can be used. The file extension has to start with a dot.

             ex: HTML=html;.xhtml=html
                  |           |
                  |          Uses Dash keyword "html" for files with .xhtml extension (extensions have priority over file types)
                 Uses Dash keyword "html" for files of type HTML
         */

        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();

        if ( !propertiesComponent.isValueSet(CONFIG_KEYWORDS) ) {
			resetDefaults();
        }
    }

	void resetDefaults() {
		PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();

		// If it's Android Studio, use the Android docset instead of Java's.
		if (ANDROID_STUDIO_PRODUCT_CODE.equals(
				ApplicationInfo.getInstance().getBuild().getProductCode())) {

			// Really revolting hack but it gets the job done.
			propertiesComponent.setValue(CONFIG_KEYWORDS,
					DEFAULT_KEYWORDS.replace("JAVA=java7;", "JAVA=android;"));
		} else {
			propertiesComponent.setValue(CONFIG_KEYWORDS, DEFAULT_KEYWORDS);
		}
	}

	public void updateValues() {
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

    public String findKeyword(String type, String extension)
    {
        if ( extensionMap.containsKey(extension) ) {
            return extensionMap.get(extension);
        }
        else {
            return typeMap.get(cleanType(type));
        }
    }

    public String cleanType(String type)
    {
        type = type.replaceFirst("\\(.*\\)", "");
        type = type.replace("files", "");
        type = type.trim();

        return type;
    }

	public static KeywordLookup getInstance() {
		return INSTANCE;
	}
}
