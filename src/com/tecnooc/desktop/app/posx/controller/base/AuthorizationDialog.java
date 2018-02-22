/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tecnooc.desktop.app.posx.controller.base;

import javafx.scene.Scene;

/**
 *
 * @author jomit
 */
public class AuthorizationDialog {
    public static Scene scene;
    public static void show() {
        Dialog.showMessageDialog(scene, "Authorization Required", "Your are not authorized to perform this action.");
    }
}
