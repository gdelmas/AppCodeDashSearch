package it.frob.dash;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import de.dreamlab.dash.KeywordLookup;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import java.util.ResourceBundle;

/**
 * Configuration entry point for the Dash plugin.
 */
class Configuration implements Configurable {

	/**
	 * Localisable resources.
	 */
	private static ResourceBundle resourceBundle =
			ResourceBundle.getBundle("it.frob.dash.Strings");

	/**
	 * Configuration panel instance.
	 */
	private ConfigurationPanel configurationPanel;

	@Nls
	@Override
	public String getDisplayName() {
		return resourceBundle.getString("panel.title");
	}

	@Nullable
	@Override
	public String getHelpTopic() {
		// TODO: Write this.
		return null;
	}

	@Nullable
	@Override
	public JComponent createComponent() {
		configurationPanel = new ConfigurationPanel(
				KeywordLookup.getInstance().getList());
		return configurationPanel;
	}

	@Override
	public boolean isModified() {
		return configurationPanel.isModified();
	}

	@Override
	public void apply() throws ConfigurationException {
		KeywordLookup keywordLookup = KeywordLookup.getInstance();
		keywordLookup.updateValues(configurationPanel.getData());
		keywordLookup.writeValues();
		configurationPanel.clearModified();
	}

	@Override
	public void reset() {
		configurationPanel.setData(KeywordLookup.getInstance().getList());
		configurationPanel.getTable().updateUI();
	}

	@Override
	public void disposeUIResources() {
	}
}
