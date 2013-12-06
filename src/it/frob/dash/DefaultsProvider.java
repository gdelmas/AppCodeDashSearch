package it.frob.dash;

import com.intellij.openapi.application.ApplicationInfo;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class DefaultsProvider {

	private final static DefaultsProvider INSTANCE = new DefaultsProvider();

	// TODO: Replace this with a DI framework.

	private final static String ANDROID_STUDIO_PRODUCT_CODE = "AI";
	private final static String INTELLIJ_IDEA_PRODUCT_CODE = "IC";

	private DefaultsProvider() {
	}

	public static DefaultsProvider getInstance() {
		return INSTANCE;
	}

	public Map<String, String> getDefaults() {
		String productCode = ApplicationInfo.getInstance().getBuild().getProductCode();

		if (ANDROID_STUDIO_PRODUCT_CODE.equals(productCode)) {
			return getAndroidStudioDefaultsMap();
		}

		if (INTELLIJ_IDEA_PRODUCT_CODE.equals(productCode)) {
			return getIntelliJDefaults();
		}

		return getStandardDefaultsMap();
	}

	private Map<String, String> getStandardDefaultsMap() {
		ResourceBundle resourceBundle =
				ResourceBundle.getBundle("it.frob.dash.Defaults");

		Map<String, String> defaults = new HashMap<String, String>();

		Enumeration<String> keys = resourceBundle.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement().trim();
			if ("".equals(key) || defaults.containsKey(key)) {
				throw new IllegalArgumentException(
						String.format("Duplicate key %s", key));
			}

			String value = resourceBundle.getString(key).trim();
			if ("".equals(value)) {
				continue;
			}

			defaults.put(key, value);
		}

		return defaults;
	}

	private Map<String, String> getIntelliJDefaults() {
		return getStandardDefaultsMap();
	}

	private Map<String, String> getAndroidStudioDefaultsMap() {
		Map<String, String> defaults = getStandardDefaultsMap();

		Pattern javaPattern = Pattern.compile("java[67]", Pattern.CASE_INSENSITIVE);
		for (Entry<String, String> entry : defaults.entrySet()) {
			if (javaPattern.matcher(entry.getValue()).matches()) {
				defaults.put(entry.getKey(), "android");
			}
		}

		return defaults;
	}
}
