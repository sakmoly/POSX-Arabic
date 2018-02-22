package com.tecnooc.desktop.app.posx.controller.base;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Node;

/**
 *
 * @author jomit
 */
public abstract class AbstractViewController implements ViewController {
    private Node view;
    
    @Override
    public Node getView() {
        return view;
    }

    @Override
    public void setView(Node view) {
        this.view = view;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
