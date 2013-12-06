package it.frob.dash;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ResourceBundle;

/**
 * Mapping add/edit dialog.
 */
class MappingEditorDialog extends DialogWrapper {

	/**
	 * Localisable resources.
	 */
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle(
			"it.frob.dash.Strings");

	/**
	 * Type text field.
	 */
	private JTextField typeTextField;

	/**
	 * Docset text field.
	 */
	private JTextField docsetTextField;

	/**
	 * Dialog OK button event listener.
	 */
	private DocumentListener documentListener = new DocumentAdapter() {
		@Override
		protected void textChanged(DocumentEvent documentEvent) {
			updateButtons();
		}
	};

	/**
	 * Constructor for adding mappings.
	 */
	private MappingEditorDialog() {
		super(true);

		setTitle(resourceBundle.getString("dialog.addnew"));
		setupUi("", "");
		getOKAction().setEnabled(false);
	}

	/**
	 * Constructor for editing existing mappings.
	 *
	 * @param type the mapping type string.
	 * @param docset the mapping docset string.
	 */
	private MappingEditorDialog(String type, String docset) {
		super(true);

		setTitle(resourceBundle.getString("dialog.edit"));
		setupUi(type, docset);
	}

	/**
	 * Constructor for editing existing mappings.
	 *
	 * @param mapping the mapping to base edit operations on.
	 */
	private MappingEditorDialog(DocsetMapping mapping) {
		super(true);

		setTitle(resourceBundle.getString("dialog.edit"));
		setupUi(mapping.getType(), mapping.getDocset());
	}

	/**
	 * Common UI initialisation code.
	 *
	 * @param type the type string to fill the appropriate text field with.
	 * @param docset the docset string to fill the appropriate text field with.
	 */
	private void setupUi(String type, String docset) {
		typeTextField = new JBTextField(type);
		typeTextField.getDocument().addDocumentListener(documentListener);
		docsetTextField = new JBTextField(docset);
		docsetTextField.getDocument().addDocumentListener(documentListener);
		init();
	}

	/**
	 * Returns the dialog data as a DocsetMapping object, or null if it
	 * contains invalid or missing data.
	 *
	 * @return a DocsetMapping with the dialog data or null if data is invalid
	 *         or incomplete.
	 */
	private DocsetMapping getCurrentMapping() {
		return isValid() ? new DocsetMapping(typeTextField.getText(),
				docsetTextField.getText()) : null;
	}

	/**
	 * Utility method to get a DocsetMapping from a dialog.
	 *
	 * @return a DocsetMapping if one can be created, or null otherwise.
	 */
	public static DocsetMapping getMapping() {
		return getMapping("", "");
	}

	/**
	 * Utility method to get a DocsetMapping from a dialog.
	 *
	 * @param type the mapping type to fill the appropriate text field with.
	 * @param docset the mapping docset to fill the appropriate text field with.
	 *
	 * @return a DocsetMapping if one can be created, or null otherwise.
	 */
	public static DocsetMapping getMapping(String type, String docset) {
		MappingEditorDialog dialog = new MappingEditorDialog(type, docset);
		return dialog.showAndGet() ? dialog.getCurrentMapping() : null;
	}

	@Nullable
	@Override
	protected JComponent createCenterPanel() {
		JPanel panel = new JBPanel();

		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 0;
		constraints.anchor = GridBagConstraints.LINE_START;
		JLabel typeLabel = new JBLabel(resourceBundle.getString(
				"dialog.labels.type"));
		panel.add(typeLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add(typeTextField, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weightx = 0;
		constraints.anchor = GridBagConstraints.LINE_START;
		JLabel docsetLabel = new JBLabel(resourceBundle.getString(
				"dialog.labels.docset"));
		panel.add(docsetLabel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add(docsetTextField, constraints);

		return panel;
	}

	@Override
	protected void doOKAction() {
		if (isValid()) {
			super.doOKAction();
		}
	}

	/**
	 * Checks validity of dialog data.
	 *
	 * @return true if data in the dialog is valid, false otherwise.
	 */
	private boolean isValid() {
		return typeTextField.getText().length() > 0 &&
				docsetTextField.getText().length() > 0;
	}

	/**
	 * Updates the status of the OK button in the dialog according to
	 * data validity.
	 */
	private void updateButtons() {
		getOKAction().setEnabled(isValid());
	}

	@Nullable
	@Override
	public JComponent getPreferredFocusedComponent() {
		return typeTextField;
	}
}
