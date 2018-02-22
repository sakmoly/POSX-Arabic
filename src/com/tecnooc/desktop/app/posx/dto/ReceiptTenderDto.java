package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.InvoiceTender;
import java.math.BigDecimal;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author jomit
 */
public class ReceiptTenderDto extends Dto<InvoiceTender> {
    public static final int TENDER_CASH = 0;
    public static final int TENDER_CREDIT_CARD = 1;
    public static final int TENDER_STORE_CREDIT = 2;
    public static final int TENDER_CREDIT = 3;
    public static final int TENDER_DEBIT_CARD = 4;
    public static final int TENDER_SPLIT = 5;
    private StringProperty tenderNameProperty;
    private IntegerProperty tenderTypeProperty;
    private ObjectProperty<BigDecimal> takenAmountProperty;
    private ObjectProperty<BigDecimal> givenAmountProperty;
    private ObjectProperty<BigDecimal> amountProperty;
    private StringProperty cardNumberProperty;
    private IntegerProperty cardTypeProperty;
    private IntegerProperty cardExpYearProperty;
    private IntegerProperty cardExpMonthProperty;
    private StringProperty cardAuthNumberProperty;

    public ReceiptTenderDto() {
        super(new InvoiceTender());
        init();
    }

    public ReceiptTenderDto(InvoiceTender entity) {
        super(entity);
        init();
    }

    private void init() {
        tenderNameProperty = new SimpleStringProperty("");
        tenderTypeProperty = new SimpleIntegerProperty(entity.getTenderType());
        takenAmountProperty = new SimpleObjectProperty<>(entity.getTaken());
        givenAmountProperty = new SimpleObjectProperty<>(entity.getGiven());
        amountProperty = new SimpleObjectProperty<>(entity.getAmt());
        cardNumberProperty = new SimpleStringProperty(entity.getCrdContrNo());
        cardTypeProperty = new SimpleIntegerProperty(entity.getCrdType() == null ? 0 : entity.getCrdType());
        cardExpYearProperty = new SimpleIntegerProperty(entity.getCrdExpYear() == null ? 0 : entity.getCrdExpYear());
        cardExpMonthProperty = new SimpleIntegerProperty(entity.getCrdExpMonth() == null ? 0 : entity.getCrdExpMonth());
        cardAuthNumberProperty = new SimpleStringProperty(entity.getAuth());
    }

    public StringProperty tenderNameProperty() {
        return tenderNameProperty;
    }

    public IntegerProperty tenderTypeProperty() {
        return tenderTypeProperty;
    }

    public ObjectProperty<BigDecimal> takenAmountProperty() {
        return takenAmountProperty;
    }

    public ObjectProperty<BigDecimal> givenAmountProperty() {
        return givenAmountProperty;
    }

    public ObjectProperty<BigDecimal> amountProperty() {
        return amountProperty;
    }

    public StringProperty cardNumberProperty() {
        return cardNumberProperty;
    }

    public IntegerProperty cardTypeProperty() {
        return cardTypeProperty;
    }

    public IntegerProperty cardExpYearProperty() {
        return cardExpYearProperty;
    }

    public IntegerProperty cardExpMonthProperty() {
        return cardExpMonthProperty;
    }

    public StringProperty cardAuthNumberProperty() {
        return cardAuthNumberProperty;
    }

    public int getTenderType() {
        return tenderTypeProperty.get();
    }

    public String getCardNumber() {
        return cardNumberProperty.get();
    }

    public BigDecimal getAmount() {
        return amountProperty.get();
    }

    public void setTenderType(int tenderType) {
        entity.setTenderType(tenderType);
        tenderTypeProperty.set(tenderType);
        switch (tenderType) {
            case TENDER_CASH:
                tenderNameProperty.set("Cash");
                break;
            case TENDER_CREDIT_CARD:
                tenderNameProperty.set("Credit Card");
                break;
            case TENDER_STORE_CREDIT:
                tenderNameProperty.set("Store Credit");
                break;
            case TENDER_DEBIT_CARD:
                tenderNameProperty.set("Debit Card");
                break;
            case TENDER_CREDIT:
                tenderNameProperty.set("Credit");
                break;
            case TENDER_SPLIT:
                tenderNameProperty.set("Split");
                break;
        }
    }

    public void setTakenAmount(BigDecimal amount) {
        entity.setTaken(amount);
        takenAmountProperty.set(amount);
    }

    public void setGivenAmount(BigDecimal amount) {
        entity.setGiven(amount);
        givenAmountProperty.set(amount);
    }

    public void setAmount(BigDecimal amount) {
        entity.setAmt(amount);
        amountProperty.set(amount);
    }

    public void setCardNumber(String cardNumber) {
        entity.setCrdContrNo(cardNumber);
        cardNumberProperty.set(cardNumber);
    }

    public void setCardType(Integer cardType) {
        entity.setCrdType(cardType);
        cardTypeProperty.set(cardType);
    }

    public void setExpYear(Integer year) {
        entity.setCrdExpYear(year);
        cardExpYearProperty.set(year);
    }

    public void setExpMonth(Integer month) {
        entity.setCrdExpMonth(month);
        cardExpMonthProperty.set(month);
    }

    public void setCardAuthNumber(String number) {
        entity.setAuth(number);
        cardAuthNumberProperty.set(number);
    }
}