package com.tecnooc.desktop.app.posx.controller.base;

import java.net.URL;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author jomit
 */
public interface ViewController extends Initializable, InitializingBean {
    Node getView();
    void setView(Node view);
}
