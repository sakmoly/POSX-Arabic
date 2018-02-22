package com.tecnooc.desktop.app.posx.controller.util;

import com.tecnooc.desktop.app.posx.model.Shortcut;
import com.tecnooc.desktop.app.posx.service.ShortcutService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jomit
 */
public class ShortcutManager {
    @Autowired ShortcutService shortcutService;
    
    private Map<String, List<Shortcut>> shortcutMap = new HashMap<>();
    private Map<Integer, List<ShortcutActionListener>> actionMap = new HashMap<>();
    
    public void init(Scene scene) {   
        System.out.println("Initializing shortcuts...");
        
        List<Shortcut> shortcuts = shortcutService.findAllActiveShortcuts();
        for (Shortcut shortcut : shortcuts) {
            String keyName = shortcut.getKey();
            if (shortcutMap.containsKey(keyName)) {
                shortcutMap.get(keyName).add(shortcut);
            } else {
                List<Shortcut> shortcutList = new ArrayList<>();
                shortcutList.add(shortcut);
                shortcutMap.put(shortcut.getKey(), shortcutList);
            }
        }
        
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                String keyName = event.getCode().getName();
//                System.out.println("Key Released: " + keyName);
                if (shortcutMap.containsKey(keyName)) {
                    List<Shortcut> shortcutList = shortcutMap.get(keyName);
//                    System.out.println("\tGot Shortcut");
//                    System.out.println("\tShift: " + event.isShiftDown());
//                    System.out.println("\tCtrl : " + event.isControlDown());
//                    System.out.println("\tAlt  : " + event.isAltDown());
//                    System.out.println("\tShortcut Shift: " + shortcut.getShift());
//                    System.out.println("\tShortcut Ctrl : " + shortcut.getCtrl());
//                    System.out.println("\tShortcut Alt  : " + shortcut.getAlt());
                    
                    for (Shortcut shortcut : shortcutList) {
                        if (shortcut.getShift() == event.isShiftDown()
                                && shortcut.getCtrl() == event.isControlDown()
                                && shortcut.getAlt() == event.isAltDown()) {
//                        System.out.println("\tGot Match");
                            int shortcutId = shortcut.getId();
                            if (actionMap.containsKey(shortcutId)) {
//                            System.out.println("\tAction Exists");
                                for (ShortcutActionListener action : actionMap.get(shortcutId)) {
                                    action.shortcutActionPerformed(shortcutId, event);
                                }
                            }
                        }                        
                    }
                }
            }
        });
    }
    
    public void setShortcutListener(int shortcutId, ShortcutActionListener listener) {
        if (actionMap.containsKey(shortcutId)) {
            actionMap.get(shortcutId).add(listener);
        } else {
            List<ShortcutActionListener> actionList = new ArrayList<>();
            actionList.add(listener);
            actionMap.put(shortcutId, actionList);
        }
    }
    
    public boolean hasShortcutListener(int shortcutId) {
        return actionMap.containsKey(shortcutId);
    }
    
    public void removeShortcutListener(int shortcutId) {
        actionMap.remove(shortcutId);
    }
}
