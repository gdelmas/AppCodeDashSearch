package com.paperetto.dash;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.util.ExecUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.vfs.VirtualFile;
import de.dreamlab.dash.KeywordLookup;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ResourceBundle;

class DashLauncherAction extends AnAction {

	/**
	 * Localisable resources.
	 */
	private static ResourceBundle resourceBundle =
			ResourceBundle.getBundle("it.frob.dash.Strings");

	private final static String RUBY_FILE_IDENTIFIER = "Ruby";
	private final static String NOTIFICATION_DISPLAY_ID = "Dash Notifications";

    private KeywordLookup keywordLookup;
    private String fileType;

    public DashLauncherAction() {
		keywordLookup = KeywordLookup.getInstance();
    }

    @Override
    public void update(AnActionEvent actionEvent) {
        actionEvent.getPresentation().setEnabled(
				PlatformDataKeys.EDITOR.getData(actionEvent.getDataContext()) != null);
    }

    private String getWordAtCursor(CharSequence editorText, int cursorOffset) {
        int editorTextLength = editorText.length();

        if ( editorTextLength == 0 ) {
            return null;
        }

        if ( (cursorOffset >= editorTextLength) || (cursorOffset > 1 && !isIdentifierPart(editorText.charAt(cursorOffset) ) && isIdentifierPart(editorText.charAt(cursorOffset - 1))) ) {
            cursorOffset--;
        }

        if ( isIdentifierPart(editorText.charAt(cursorOffset)) ) {
            int start = cursorOffset;
            int end = cursorOffset;

            while ( start > 0 && isIdentifierPart(editorText.charAt(start-1)) ) {
                start--;
            }

            while ( end < editorTextLength && isIdentifierPart(editorText.charAt(end)) ) {
                end++;
            }

            return editorText.subSequence(start, end).toString();
        }
        return null;
    }

    public void actionPerformed(AnActionEvent actionEvent) {
        VirtualFile virtualFile = actionEvent.getData(PlatformDataKeys.VIRTUAL_FILE);
		if (virtualFile != null) {
			fileType = keywordLookup.cleanType(virtualFile.getFileType().getName());
		}

        Editor editor = PlatformDataKeys.EDITOR.getData(actionEvent.getDataContext());
		if (editor == null) {
			return;
		}

        int offset = editor.getCaretModel().getOffset();
        CharSequence editorText = editor.getDocument().getCharsSequence();

        SelectionModel selectionModel = editor.getSelectionModel();
		String word = selectionModel.hasSelection() ?
				selectionModel.getSelectedText() :
				getWordAtCursor(editorText, offset);
		if (word == null) {
			return;
		}

        String keyword = null;

        if (virtualFile != null) {
            keyword = keywordLookup.findKeyword(virtualFile.getFileType().getName(),
					virtualFile.getExtension());
			if (keyword != null) {
				keyword += ":";
			} else {
				keyword = "";
			}
        }

        String searchWord;
        try {
            searchWord = URLEncoder.encode(word, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException exception) {
			Notifications.Bus.notify(new Notification(NOTIFICATION_DISPLAY_ID,
						resourceBundle.getString("error.invalidencoding"),
						exception.getLocalizedMessage(),
						NotificationType.ERROR));
			return;
        }

        String[] command = new String[] {
				ExecUtil.getOpenCommandPath()
		};
        try {
            final GeneralCommandLine commandLine = new GeneralCommandLine(command);
            commandLine.addParameter(String.format("dash://%s%s", keyword, searchWord));
            commandLine.createProcess();
        } catch (ExecutionException exception) {
			Notifications.Bus.notify(new Notification(NOTIFICATION_DISPLAY_ID,
					resourceBundle.getString("error.cannotexecute"),
					exception.getLocalizedMessage(),
					NotificationType.ERROR));
        }
    }

    private boolean isIdentifierPart(char ch) {
		return Character.isJavaIdentifierPart(ch) ||
				(RUBY_FILE_IDENTIFIER.equalsIgnoreCase(fileType) && ch == '?');
    }
}
