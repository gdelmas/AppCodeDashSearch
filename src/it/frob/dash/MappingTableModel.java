package it.frob.dash;

import com.intellij.ui.AddEditRemovePanel.TableModel;
import org.jetbrains.annotations.Nullable;

/**
 * Table model for IntelliJ's own Add/Edit/Delete table-based pane.
 */
class MappingTableModel extends TableModel<DocsetMapping> {
	// TODO: Localisation

	/**
	 * Column names.
	 */
	private final String[] mColumnNames = new String[] { "Type", "Docset" };

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Nullable
	@Override
	public String getColumnName(int index) {
		return mColumnNames[index];
	}

	@Override
	public Object getField(DocsetMapping mappingEntry, int index) {
		switch (index) {
			case 0:
				return mappingEntry.getEntry();

			case 1:
				return mappingEntry.getDocset();

			default:
				return null;
		}
	}

	@Override
	public boolean isEditable(int column) {
		// TODO: Is a modal dialog needed here instead?
		return true;
	}

	@Override
	public void setValue(Object aValue, DocsetMapping data, int columnIndex) {
		switch (columnIndex) {
			case 0:
				data.setEntry((String) aValue);
				break;

			case 1:
				data.setDocset((String) aValue);
				break;

			default:
				break;
		}
	}
}
