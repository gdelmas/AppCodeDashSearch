package it.frob.dash;

import com.intellij.ui.AddEditRemovePanel.TableModel;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;

/**
 * Table model for IntelliJ's own Add/Edit/Delete table-based pane.
 */
class MappingTableModel extends TableModel<DocsetMapping> {

	/**
	 * Resource bundle.
	 */
	private static ResourceBundle resourceBundle =
			ResourceBundle.getBundle("it.frob.dash.Strings");

	/**
	 * Column names.
	 */
	private static final String[] COLUMN_NAMES = new String[] {
			resourceBundle.getString("table.titles.type"),
			resourceBundle.getString("table.titles.docset")
	};

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Nullable
	@Override
	public String getColumnName(int index) {
		return COLUMN_NAMES[index];
	}

	@Override
	public Object getField(DocsetMapping mappingEntry, int columnIndex) {
		switch (columnIndex) {
			case 0:
				return mappingEntry.getType();

			case 1:
				return mappingEntry.getDocset();

			default:
				throw new IllegalArgumentException(
						String.format("Unknown column index %d", columnIndex));
		}
	}

	@Override
	public boolean isEditable(int column) {
		return false;
	}

	@Override
	public void setValue(Object aValue, DocsetMapping data, int columnIndex) {
		switch (columnIndex) {
			case 0:
				data.setType((String) aValue);
				break;

			case 1:
				data.setDocset((String) aValue);
				break;

			default:
				throw new IllegalArgumentException(
						String.format("Unknown column index %d", columnIndex));
		}
	}
}
