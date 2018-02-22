/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.controller.base;

/**
 *
 * @author jomit
 */
public interface DialogView extends ViewController {
    public void onBeforeShow();
    public boolean onBeforeHide(Dialog.ButtonType button);
}
