package com.tecnooc.desktop.app.posx.controller.impl;

import com.tecnooc.desktop.app.posx.controller.base.AbstractViewController;
import com.tecnooc.desktop.app.posx.controller.base.Dialog;
import com.tecnooc.desktop.app.posx.controller.base.DialogListener;
import com.tecnooc.desktop.app.posx.controller.base.DialogView;
import com.tecnooc.desktop.app.posx.controller.base.DialogViewController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

/**
 *
 * @author jomit
 */
public class OpenDialogViewController extends AbstractViewController implements DialogView {
    @FXML private TextField openingBalanceField;
    
    public double getOpeningBalance() {
        try {
            return Double.parseDouble(openingBalanceField.getText());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
    
    public void show(Scene currentScene, DialogListener listener) {
        Dialog.showCustomDialog(currentScene, "Open Day", this, Dialog.DialogType.OK_CANCEL, listener);
    }

    @Override
    public void onBeforeShow() {
        openingBalanceField.setText("0");
        openingBalanceField.requestFocus();
    }

    @Override
    public boolean onBeforeHide(Dialog.ButtonType button) { return true; }
}
