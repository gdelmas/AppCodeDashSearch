package it.frob.dash;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.AddEditRemovePanel;
import de.dreamlab.dash.KeywordLookup;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Configuration entry point for the Dash plugin.
 */
public class Configuration implements Configurable {

	private static ResourceBundle resourceBundle =
			ResourceBundle.getBundle("it.frob.dash.Strings");

	/**
	 * Flag indicating whether the current mapping set has been modified by the
	 * current configuration session.
	 */
	private boolean modified = false;

	/**
	 * Current mapping list.
	 */
	private List<DocsetMapping> mMappingList = KeywordLookup.getInstance().getList();

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
		AddEditRemovePanel<DocsetMapping> mainPanel = new AddEditRemovePanel<DocsetMapping>(new MappingTableModel(), mMappingList) {
			@Nullable
			@Override
			protected DocsetMapping addItem() {
				DocsetMapping docset = MappingEditorDialog.getMapping();
				modified |= docset != null;
				return docset;
			}

			@Override
			protected boolean removeItem(DocsetMapping mappingEntry) {
				if (mMappingList.contains(mappingEntry)) {
					modified |= true;
					mMappingList.remove(mappingEntry);
					return true;
				}

				return false;
			}

			@Nullable
			@Override
			protected DocsetMapping editItem(DocsetMapping mappingEntry) {
				DocsetMapping mapping = MappingEditorDialog.getMapping(
						mappingEntry.getType(), mappingEntry.getDocset());
				modified |= mapping != null;
				return mapping;
			}
		};
		mainPanel.getTable().setShowColumns(true);

		return mainPanel;
	}

	@Override
	public boolean isModified() {
		return modified;
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
