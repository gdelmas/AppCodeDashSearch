package it.frob.dash;

import com.intellij.ui.table.JBTable;
import de.dreamlab.dash.KeywordLookup;

import javax.swing.table.AbstractTableModel;
import java.util.Map.Entry;
import java.util.TreeSet;

public class ConfigurationUI extends JBTable {

	public ConfigurationUI() {
		ConfigurationTableModel tableModel = new ConfigurationTableModel();
		setModel(tableModel);
	}

	private static class ConfigurationTableModel extends AbstractTableModel {
		private String[] values;
		private final String[] columns = new String[] {
				"Type/Extension", "Docset"
		};

		public ConfigurationTableModel() {
			super();

			TreeSet<String> keys = new TreeSet<String>();
			for (Entry<String, String> entry : KeywordLookup.getInstance().getValues().entrySet()) {
				keys.add(entry.getKey());
			}
			values = new String[keys.size()];
			keys.toArray(values);
		}

		@Override
		public int getRowCount() {
			return values.length;
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return columns[columnIndex];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			String key = values[rowIndex];
			switch (columnIndex) {
				case 0:
					return key;

				case 1:
					return KeywordLookup.getInstance().getValues().get(key);

				default:
					return null;
			}
		}
	}
}
