package com.tecnooc.desktop.app.posx.controller.impl;

import com.tecnooc.desktop.app.posx.controller.base.AbstractViewController;
import com.tecnooc.desktop.app.posx.controller.base.Dialog;
import com.tecnooc.desktop.app.posx.controller.base.DialogListener;
import com.tecnooc.desktop.app.posx.controller.base.DialogView;
import com.tecnooc.desktop.app.posx.controller.base.DialogViewController;
import com.tecnooc.desktop.app.posx.dto.ReceiptTenderDto;
import com.tecnooc.desktop.app.posx.model.CreditCard;
import com.tecnooc.desktop.app.posx.model.CreditCardRange;
import com.tecnooc.desktop.app.posx.service.CreditCardService;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jomit
 */
public class TenderCreditCardViewController extends AbstractViewController implements DialogView, DialogListener {
    @Autowired private CreditCardService creditCardService;
    private List<CreditCard> activeCards;
    
//    private final String VISA = "^4[0-9]{12}(?:[0-9]{3})?$";
//    private final String MASTER = "^5[1-5][0-9]{14}$";
//    private final String EXPRESS = "^3[47][0-9]{13}$";
//    private final String DINERS = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
//    private final String DISCOVER = "^6(?:011|5[0-9]{2})[0-9]{12}$";
//    private final String JSB = "^(?:2131|1800|35\\d{3})\\d{11}$";
//    private final String[] CARD_TYPES = {
//        "Unknown", "Visa", "Master", "Express", "Diners", "Discover", "JSB"
//    };
    
    @FXML private TextField cardNoField;
    @FXML private ComboBox<Integer> expiryYearCombo;
    @FXML private ComboBox<Integer> expiryMonthCombo;
    @FXML private TextField authCodeField;    
    @FXML private ComboBox<String> cardTypeCombo;
    
    private ReceiptTenderDto creditCardInfo;
    private DialogListener listener;

    public void onInit() {        
        ObservableList<Integer> years  = FXCollections.observableArrayList();
        ObservableList<Integer> months = FXCollections.observableArrayList();
        //Date dt = new Date();
        int currentyear  = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int uptoYear     = currentyear + 20;
        
        for (int i = currentyear; i < uptoYear; i++) {
            years.add(i);
        }
        
        for (int i = 1; i <= 12; i++) {
            months.add(i);
        }
        
        expiryYearCombo.setItems(years);
        expiryYearCombo.selectionModelProperty().get().selectFirst();
        expiryMonthCombo.setItems(months);
        expiryMonthCombo.selectionModelProperty().get().select(currentMonth);
        
        activeCards = creditCardService.findAllActiveCards();
        List<String> cardList = new ArrayList<>();
        for (CreditCard creditCard : activeCards) {
            cardList.add(creditCard.getCardName());
        }
        cardList.add("Unknown");
        cardTypeCombo.setItems(FXCollections.observableArrayList(cardList));
        cardTypeCombo.selectionModelProperty().get().selectLast();
        
        cardNoField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                detectCard(newValue);
            }
        });
    }
    
    @Override
    public void onBeforeShow() {
        onInit();
        creditCardInfo = null;
    }

    public boolean doOK() {
        creditCardInfo = new ReceiptTenderDto();
        creditCardInfo.setTenderType(ReceiptTenderDto.TENDER_CREDIT_CARD);
        creditCardInfo.setCardAuthNumber(authCodeField.getText());
        creditCardInfo.setExpYear(expiryYearCombo.getSelectionModel().getSelectedItem());
        creditCardInfo.setExpMonth(expiryMonthCombo.getSelectionModel().getSelectedItem());
        creditCardInfo.setCardNumber(cardNoField.getText());
        creditCardInfo.setCardType(getSelectedCard());
        
        authCodeField.setText("");
        expiryYearCombo.selectionModelProperty().get().clearSelection();
        expiryMonthCombo.selectionModelProperty().get().clearSelection();
        cardNoField.setText("");
        cardTypeCombo.selectionModelProperty().get().clearSelection();
        
        return true; 
    }

    public boolean doCancel() {
        authCodeField.setText("");
        expiryYearCombo.selectionModelProperty().get().clearSelection();
        expiryMonthCombo.selectionModelProperty().get().clearSelection();
        cardNoField.setText("");
        cardTypeCombo.selectionModelProperty().get().clearSelection();
        
        return true;
    }

    public ReceiptTenderDto getCreditCardInfo() {
        return creditCardInfo;
    }
    
    public void detectCard(String text) {        
        cardTypeCombo.selectionModelProperty().get().select(getCardPosition(text));
    }
    
    public Integer getSelectedCard() {
        Integer position = cardTypeCombo.selectionModelProperty().get().getSelectedIndex();

        if (position == activeCards.size()) {
            return 0;
        }

        return activeCards.get(position).getCardId();
    }
    
    public Integer getCardType(String cardNumber) {
        Integer position = getCardPosition(cardNumber);
        
        if (position == activeCards.size()) {
            return 0;
        }
        
        return activeCards.get(position).getCardId();
    }
    
    public Integer getCardPosition(String cardNumber) {
        try {
            BigInteger number = new BigInteger(cardNumber);
            
            int i = 0;
            for (CreditCard creditCard : activeCards) {
                for (CreditCardRange creditCardRange : creditCard.getCreditCardRangeList()) {
                    if (creditCardRange.isInRange(number)) {
                        return i;
                    }
                }
                
                i++;
            }
        } catch (NumberFormatException ex) {}
        
        return activeCards.size();
//        for (int i = 1; i <= 6; i++) {
//            switch (i) {
//                case 1: {
//                    Pattern visa = Pattern.compile(VISA);
//                    Matcher m = visa.matcher(cardNumber);
//                    if (m.matches()) {
//                        return 1; // visa
//                    }
//                    break;
//                }
//                case 2: {
//                    Pattern master = Pattern.compile(MASTER);
//                    Matcher m = master.matcher(cardNumber);
//                    if (m.matches()) {
//                        return 2; // master
//                    }
//                    break;
//                }
//                case 3: {
//                    Pattern express = Pattern.compile(EXPRESS);
//                    Matcher m = express.matcher(cardNumber);
//                    if (m.matches()) {
//                        return 3; // express
//                    }
//                    break;
//                }
//                case 4: {
//                    Pattern diners = Pattern.compile(DINERS);
//                    Matcher m = diners.matcher(cardNumber);
//                    if (m.matches()) {
//                        return 4; // diners
//                    }
//                    break;
//                }
//                case 5: {
//                    Pattern discover = Pattern.compile(DISCOVER);
//                    Matcher m = discover.matcher(cardNumber);
//                    if (m.matches()) {
//                        return 5; // discover
//                    }
//                    break;
//                }
//                case 6: {
//                    Pattern jsb = Pattern.compile(JSB);
//                    Matcher m = jsb.matcher(cardNumber);
//                    if (m.matches()) {
//                        return 6; // jsb
//                    }
//                    break;
//                }
//            }
//        }
//
//        return 0;
    }

    public void show(Scene currentScene, DialogListener listener) {
        this.listener = listener;
        Dialog.showCustomDialog(currentScene, "Credit Card Info", this, Dialog.DialogType.OK_CANCEL, this);
    }

    @Override
    public boolean onBeforeHide(Dialog.ButtonType button) { return true;}

    @Override
    public void dialogDismissed(Dialog.ButtonType buttonPressed) {
        if (buttonPressed == Dialog.ButtonType.OK) {
            doOK();
        } else {
            doCancel();
        }
        
        listener.dialogDismissed(buttonPressed);
    }
}
