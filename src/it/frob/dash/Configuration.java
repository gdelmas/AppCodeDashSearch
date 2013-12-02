package it.frob.dash;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;

public class Configuration implements Configurable {
	@Nls
	@Override
	public String getDisplayName() {
		return "Dash";
	}

	@Nullable
	@Override
	public String getHelpTopic() {
		return null;
	}

	@Nullable
	@Override
	public JComponent createComponent() {
		ConfigurationUI configurationUI = new ConfigurationUI();
		JBScrollPane scrollPane = new JBScrollPane(configurationUI);
		configurationUI.setFillsViewportHeight(true);

		return scrollPane;
	}

	@Override
	public boolean isModified() {
		return false;
	}

	@Override
	public void apply() throws ConfigurationException {
	}

	@Override
	public void reset() {
	}

	@Override
	public void disposeUIResources() {
	}
}
