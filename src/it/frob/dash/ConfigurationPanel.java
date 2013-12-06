package it.frob.dash;

import com.intellij.ui.AddEditRemovePanel;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Configuration panel for adding/removing/editing mappings.
 */
class ConfigurationPanel extends AddEditRemovePanel<DocsetMapping> {

	/**
	 * Localisable resources.
	 */
	private static ResourceBundle resourceBundle =
			ResourceBundle.getBundle("it.frob.dash.Strings");

	/**
	 * Flag indicating whether the current mapping set has been modified by a
	 * configuration dialog.
	 */
	private boolean modified = false;

	/**
	 * Constructor.
	 *
	 * @param data mappings to fill the panel with.
	 */
	ConfigurationPanel(List<DocsetMapping> data) {
		super(new MappingTableModel(), data,
				resourceBundle.getString("panel.label"));
	}

	@Nullable
	@Override
	protected DocsetMapping addItem() {
		DocsetMapping docset = MappingEditorDialog.getMapping();
		modified |= docset != null;
		return docset;
	}

	@Override
	protected boolean removeItem(DocsetMapping docsetMapping) {
		List<DocsetMapping> data = getData();

		if (data.contains(docsetMapping)) {
			modified |= true;
			data.remove(docsetMapping);
			return true;
		}

		return false;
	}

	@Nullable
	@Override
	protected DocsetMapping editItem(DocsetMapping docsetMapping) {
		DocsetMapping mapping = MappingEditorDialog.getMapping(
				docsetMapping.getType(), docsetMapping.getDocset());
		modified |= mapping != null;
		return mapping;
	}

	/**
	 * Returns the modification status of the panel.
	 *
	 * @return true if there are unsaved modifications, false otherwise.
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * Resets the modification flag.
	 */
	public void clearModified() {
		modified = false;
	}
}
