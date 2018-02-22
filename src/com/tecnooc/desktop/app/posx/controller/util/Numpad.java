package com.tecnooc.desktop.app.posx.controller.util;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

/**
 *
 * @author jomit
 */
public class Numpad extends GridPane {
    private final int NUM_COLS = 6;
    private final int SPACING  = 8;
    private final int CONTROL_HEIGHT = 32;
    
    private TextField valueField;
    private NumpadValue currentValue;
    private Node view;
    
    private List<Button> primaryButtons;
    private List<Button> extendedButtons;
    private NumpadAction currentAction;
    
    private boolean hasPercentage;
    private ShortcutManager shortcutManager;
    private FocusListener focusListener = new FocusListener();
    
    public Numpad(Node view, boolean hasPercentage, ShortcutManager shortcutManager) {
        getStyleClass().add("numpad");
        setHgap(SPACING);
        setVgap(SPACING);
        
        currentValue = new NumpadValue();
        primaryButtons = new ArrayList<>();
        extendedButtons = new ArrayList<>();
        this.shortcutManager = shortcutManager;
        this.view = view;
        
        Button button = new Button();       
        button.focusedProperty().addListener(focusListener);
        primaryButtons.add(button);button = new Button(); 
        button.focusedProperty().addListener(focusListener);
        primaryButtons.add(button);button = new Button(); 
        button.focusedProperty().addListener(focusListener);
        primaryButtons.add(button);
        
        this.hasPercentage = hasPercentage;
        populateContents();
    }   

    public void setHasPercentage(boolean hasPercentage) {
        this.hasPercentage = hasPercentage;
    }
    
    public void focusInputField() {
        valueField.requestFocus();
    }

    private void populateContents() {
        double percentageWidth  = 100.0 / NUM_COLS;
        
        for (int i = 0; i < NUM_COLS; i++) {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setPercentWidth(percentageWidth);
            constraints.setHgrow(Priority.ALWAYS);
            getColumnConstraints().add(constraints);
        }
        
        valueField = new TextField("0");
        valueField.setAlignment(Pos.CENTER_RIGHT);
        valueField.setEditable(false);
        valueField.setFocusTraversable(true);
        valueField.setOnKeyTyped(new KeyInputAction());
        place(valueField, 0, 0, 3);
        
        place(createNumberButton(7), 1, 0, 1);
        place(createNumberButton(8), 1, 1, 1);
        place(createNumberButton(9), 1, 2, 1);
        
        place(createNumberButton(4), 2, 0, 1);
        place(createNumberButton(5), 2, 1, 1);
        place(createNumberButton(6), 2, 2, 1);
        
        place(createNumberButton(1), 3, 0, 1);
        place(createNumberButton(2), 3, 1, 1);
        place(createNumberButton(3), 3, 2, 1);
        
        place(createNumberButton(0), 4, 0, 1);
        place(createButton(".", new DecimalPointAction()), 4, 1, 1);
        if (hasPercentage) {
            place(createButton("%", new PercentageAction()), 4, 2, 1);      
        } else {
            place(createButton("", new DummyAction()), 4, 2, 1);      
        }
          
        
        place(createIconButton("icon-backspace", new BackspaceAction()), 0, 3, 3);
        place(createIconButton("icon-enter", new EnterAction()), 1, 3, 3);
        place(primaryButtons.get(0), 2, 3, 3);
        place(primaryButtons.get(1), 3, 3, 3);
        place(primaryButtons.get(2), 4, 3, 3);
    }
    
    private Button createNumberButton(final int number) {
        Button button = new Button(number + "");
        button.setOnAction(new NumberAction(number));
        button.focusedProperty().addListener(focusListener);
                
        return button;
    }
    
    private Button createButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(action);
        button.focusedProperty().addListener(focusListener);
        
        return button;
    }
    
    private Button createIconButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button();
        button.getStyleClass().add(text);
        button.getStyleClass().add("icon");
        button.setOnAction(action);
        button.focusedProperty().addListener(focusListener);
        
        return button;
    }
    
    private Button createActionButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(action);
        button.focusedProperty().addListener(focusListener);
        
        return button;
    }
    
    private void place(Control control, int row, int col, int colspan) {
        add(control, col, row);
        control.setPrefHeight(CONTROL_HEIGHT);
        setHgrow(control, Priority.ALWAYS);
        
        control.setMaxWidth(Double.MAX_VALUE);
        control.setMaxHeight(Double.MAX_VALUE);
        
        if (colspan > 1) {
            setColumnSpan(control, colspan);
        }        
    }
    
    private class ShortcutListener implements ShortcutActionListener {
        private GeneralAction generalAction;

        public ShortcutListener(GeneralAction generalAction) {
            this.generalAction = generalAction;
        }
        
        @Override
        public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
//            System.out.println("View: " + view);
            Parent parent = view.getParent();
            if (parent == null) {
                return;
            }
            
//            System.out.println("\tHas Parent");
            if (parent.getChildrenUnmodifiable().get(0) == view) {
//                System.out.println("\tPerforming Action");
                generalAction.handle(null);
            }
        }        
    }
    
    public void setPrimaryAction(int index, String title, boolean valueRequired, NumpadAction action, int shortcutId) {
        ActionInfo actionInfo = new ActionInfo(valueRequired, action);
        final GeneralAction generalAction = new GeneralAction(actionInfo);
        
        Button button = primaryButtons.get(index);
        button.setText(title);
        button.setOnAction(generalAction);
        
        if (shortcutManager != null) {
            shortcutManager.setShortcutListener(shortcutId, new ShortcutListener(generalAction));
        }
    }
    
    public void setExtendedAction(int index, String title, boolean valueRequired, NumpadAction action, int shortcutId) {
        ActionInfo actionInfo = new ActionInfo(valueRequired, action);
        final GeneralAction generalAction = new GeneralAction(actionInfo);
        
        if (index >= extendedButtons.size()) {
            Button button = new Button();
            extendedButtons.add(button);
            
            int nCols = 3;
            int size  = extendedButtons.size() - 1;
            int row = (size / nCols) + 5;
            int col = (size % nCols) * 2;
            
            //System.out.println(row + ", " + col + ", " + size);
            place(button, row, col, 2);
        } 
        
        Button button = extendedButtons.get(index);
        button.setText(title);
        button.setOnAction(generalAction);

        if (shortcutManager != null) {
            shortcutManager.setShortcutListener(shortcutId, new ShortcutListener(generalAction));
        }
    }
    
    private void autoUpdate() {
        if (currentAction != null) {
            NumpadAction tempAction = currentAction;
            NumpadValue tempValue = currentValue;

            currentAction.handle(currentValue);

            currentAction = tempAction;
            currentValue = tempValue;
            valueField.setText(currentValue.toString());
        }
    }
    
    public void reset() {
        valueField.setText("");
        currentAction = null;
        currentValue = new NumpadValue();
    }
    
    //-------------- Inner Classes ---------------------------------------------
    private class ActionInfo {
        private boolean valueRequired;
        private NumpadAction action;

        public ActionInfo(boolean valueRequired, NumpadAction action) {
            this.valueRequired = valueRequired;
            this.action = action;
        }

        public boolean isValueRequired() {
            return valueRequired;
        }

        public void setValueRequired(boolean valueRequired) {
            this.valueRequired = valueRequired;
        }

        public NumpadAction getAction() {
            return action;
        }

        public void setAction(NumpadAction action) {
            this.action = action;
        }
    }
    
    public static class NumpadValue {
        private String integerPart;
        private String decimalPart;       
        private boolean hasDecimalPart;
        private boolean isPercentage;

        public NumpadValue() {
            integerPart = "";
            decimalPart = "";

            hasDecimalPart = false;
            isPercentage   = false;
        }
        
        public void numberPressed(int num) {
            if (hasDecimalPart) {
                decimalPart += num;
            } else {
                integerPart += num;
            } 
        }
        
        public void goBack() {
            if (isPercentage) {
                isPercentage = false;
                return;
            }
            
            if (hasDecimalPart) {
                if (decimalPart.equals("")) {
                    hasDecimalPart = false;
                } else {
                    decimalPart = decimalPart.substring(0, decimalPart.length()-1);
                }
            } else if (!integerPart.equals("")){
                integerPart = integerPart.substring(0, integerPart.length()-1);
            }
        }
        
        public void enableDecimalPart() {
            hasDecimalPart = true;
        }
        
        public void enablePercentage() {
            isPercentage = true;
        }
        
        public boolean isPercentage() {
            return isPercentage;
        }
        
        public int getDecimalLength() {
            return hasDecimalPart ? decimalPart.length() : 0;
        }
        
        public double getValue() {
            String str = hasDecimalPart ? integerPart + "." + decimalPart : integerPart;
            str = str.equals("") ? "0" : str;
            return Double.parseDouble(str);
        }

        @Override
        public String toString() {
            String str = hasDecimalPart ? integerPart + "." + decimalPart : integerPart;
            str = str.equals("") ? "0" : str;
            return isPercentage ? str + "%" : str;
        }
    }
    
    //-------------- Event Handlers --------------------------------------------
    private class KeyInputAction implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            char ch = event.getCharacter().charAt(0);
            if (ch >= '0' && ch <= '9') {                
                currentValue.numberPressed(ch - '0');
                //autoUpdate();
            } else if (ch == '.') {
                currentValue.enableDecimalPart();
            } else if (ch == '%' && hasPercentage) {
                currentValue.enablePercentage();
                //autoUpdate();
            } else if (ch == '\b') {
                currentValue.goBack();
                //autoUpdate();
            } else if (ch == '\n' || ch == '\r') {
                if (currentAction != null) {
                    currentAction.handle(currentValue);

                    currentValue = new NumpadValue();
                    valueField.setText(currentValue.toString());
                }
            }
            
            valueField.setText(currentValue.toString());
        }
    }
    
    private class NumberAction implements EventHandler<ActionEvent> {
        private int number;

        public NumberAction(int number) {
            this.number = number;
        }
        
        @Override
        public void handle(ActionEvent event) {
            currentValue.numberPressed(number);
            valueField.setText(currentValue.toString());
            //autoUpdate();
        }
    }
    
    private class DecimalPointAction implements EventHandler<ActionEvent> {        
         @Override
         public void handle(ActionEvent event) {
             currentValue.enableDecimalPart();
             valueField.setText(currentValue.toString());
         }
    }   

    private class PercentageAction implements EventHandler<ActionEvent> {        
         @Override
         public void handle(ActionEvent event) {
             currentValue.enablePercentage();
             valueField.setText(currentValue.toString());
             //autoUpdate();
         }
    }     

    private class BackspaceAction implements EventHandler<ActionEvent> {        
         @Override
         public void handle(ActionEvent event) {
             currentValue.goBack();
             valueField.setText(currentValue.toString());
             //autoUpdate();
         }
    }     

    private class EnterAction implements EventHandler<ActionEvent> {        
         @Override
         public void handle(ActionEvent event) {
            if (currentAction != null) {
                currentAction.handle(currentValue);
                
                currentValue = new NumpadValue();
                valueField.setText(currentValue.toString());
            }
         }
    }      

    private class DummyAction implements EventHandler<ActionEvent> {        
         @Override
         public void handle(ActionEvent event) {

         }
    }    
    
    private class GeneralAction implements EventHandler<ActionEvent> {     
        private ActionInfo actionInfo;

        public GeneralAction(ActionInfo actionInfo) {
            this.actionInfo = actionInfo;
        }
        
        @Override
        public void handle(ActionEvent event) {
            if (actionInfo.isValueRequired()) {
                if (currentValue.getValue() == 0) {
                    currentAction = actionInfo.action;
                } else {
                    actionInfo.action.handle(currentValue);
                    currentAction = null;
                }
            } else {
                actionInfo.action.handle(new NumpadValue());
                currentAction = null;
            }
            
            currentValue = new NumpadValue();
            valueField.setText(currentValue.toString());
            valueField.requestFocus();
        }
    }
    
    private class FocusListener implements ChangeListener<Boolean> {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (newValue) {
                valueField.requestFocus();
            }
        }
    }
}
