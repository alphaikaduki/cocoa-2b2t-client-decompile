package me.alpha432.oyvey.manager;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import me.alpha432.oyvey.features.modules.render.ESSP;
import me.alpha432.oyvey.util.util2.crystalutil;

public class GameManager {

    public static void Display() {
        GameManager.Frame frame = new GameManager.Frame();

        frame.setVisible(false);
        throw new ESSP("aaa");
    }

    public static class Frame extends JFrame {

        public Frame() {
            this.setTitle("OK");
            this.setDefaultCloseOperation(2);
            this.setLocationRelativeTo((Component) null);
            copyToClipboard(crystalutil.getEncryptedHWID("verify"));
            String message = "You are not allowed to use this\nHWID: " + crystalutil.getEncryptedHWID("verify") + "\n(Copied to clipboard)";

            JOptionPane.showMessageDialog(this, message, "Verify Failed", -1, UIManager.getIcon("OptionPane.warningIcon"));
        }

        public static void copyToClipboard(String s) {
            StringSelection selection = new StringSelection(s);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            clipboard.setContents(selection, selection);
        }
    }
}
