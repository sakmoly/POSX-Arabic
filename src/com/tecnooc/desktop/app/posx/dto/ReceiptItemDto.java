package com.tecnooc.desktop.app.posx.dto;

import com.tecnooc.desktop.app.posx.model.Inventory;
import com.tecnooc.desktop.app.posx.model.InvoiceItem;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author jomit
 */
public class ReceiptItemDto extends Dto<InvoiceItem> {
    public static final int ITEM_SOLD = 0;
    public static final int ITEM_RETURN = 1;
    public static final int ITEM_VOID = 2;
    
    private StringProperty itemNameProperty;
    private ObjectProperty<BigDecimal> quantityProperty;
    private IntegerProperty transactionTypeProperty;
    private ObjectProperty<BigDecimal> discountAmountProperty;
    private ObjectProperty<BigDecimal> discountPercentageProperty;
    private ObjectProperty<BigDecimal> sellingPriceProperty;
    private ObjectProperty<BigDecimal> taxAmountProperty;
    private ObjectProperty<BigDecimal> totalPriceProperty;
    
    private List<InventoryNumberDto> numberDtoList;
    
    public ReceiptItemDto(InvoiceItem entity) {
        super(entity);
        
        initProperties();
        numberDtoList = new ArrayList<>();
    }

    public ReceiptItemDto(InventoryItemDto item) {
        super(new InvoiceItem());
        
        numberDtoList = new ArrayList<>();
        InventoryNumberDto inventoryNumber = item.getInventoryNumberDto();
        if (inventoryNumber != null) {
            numberDtoList.add(inventoryNumber);
        }
        
        Inventory inventory = item.getEntity();
        entity.setInventory(inventory);
        entity.setBasePrice(inventory.getBasePrice());
        entity.setDocDiscAmount(BigDecimal.ZERO);
        entity.setDocDiscPercDoc(BigDecimal.ZERO);
        entity.setCost(item.getCost());
        entity.setTaxType(item.getTaxType());
        entity.setTaxRate(item.getTaxRate());
        
        initProperties();
        
        setQuantity(BigDecimal.ONE);
        entity.setSellingPrice(inventory.getSellingPrice());   
        sellingPriceProperty.set(inventory.getSellingPrice().setScale(2, RoundingMode.HALF_EVEN));
        
        setTransactionType(ITEM_SOLD);
        try {
            setDiscountPercentage(BigDecimal.ZERO, true);
        } catch (DtoException ex) { System.out.println(ex);}
                   
        recalcTotal();
    }    
    
    private void initProperties() {        
        itemNameProperty = new SimpleStringProperty(entity.getInventory().getDescription1());
        quantityProperty = new SimpleObjectProperty<>(entity.getQty());
        transactionTypeProperty = new SimpleIntegerProperty(entity.getTransactionType());
        discountAmountProperty = new SimpleObjectProperty<>(entity.getDiscAmount());
        discountPercentageProperty = new SimpleObjectProperty<>(entity.getDiscPerc());
        sellingPriceProperty = new SimpleObjectProperty<>(entity.getSellingPrice());
        taxAmountProperty = new SimpleObjectProperty<>();
        totalPriceProperty = new SimpleObjectProperty<>();
        recalcTotal();
    }
    
    public List<InventoryNumberDto> getInventoryNumbers() {
        return numberDtoList;
    }
    
    public ReadOnlyStringProperty itemNameProperty() {  
        return itemNameProperty;
    }    
    
    public ReadOnlyObjectProperty<BigDecimal> quantityProperty() {
        return quantityProperty;
    }
    
    public ReadOnlyIntegerProperty transactionTypeProperty() {
        return transactionTypeProperty;
    }
    
    public ReadOnlyObjectProperty<BigDecimal> discountAmountProperty() {
        return discountAmountProperty;
    }
    
    public ReadOnlyObjectProperty<BigDecimal> discountPercentageProperty() {
        return discountPercentageProperty;
    }
    
    public ReadOnlyObjectProperty<BigDecimal> sellingPriceProperty() {
        return sellingPriceProperty;
    }
    
    public ReadOnlyObjectProperty<BigDecimal> totalPriceProperty() {        
        return totalPriceProperty;
    }
    
    public ReadOnlyObjectProperty<BigDecimal> taxAmountProperty() {        
        return taxAmountProperty;
    }
    
    public Long getItemId() {
        return this.entity.getInventory().getItemSid();
    }
    public BigDecimal getTaxAmount() {        
        return taxAmountProperty.get();
    }
    
    public BigDecimal getTotalPrice() {        
        return totalPriceProperty.get();
    }
    
    public Integer getTransactionType() {
        return transactionTypeProperty.get();
    }
    
    public String getItemName() {
        return itemNameProperty.get();
    }
    
    public String getDescription2() {
        return entity.getInventory().getDescription2();
    }

    public BigDecimal getQuantity() {
        return quantityProperty.get();
    }

    public BigDecimal getDiscountAmount() {
        return discountAmountProperty.get();
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentageProperty.get();
    }

    public BigDecimal getSellingPrice() {
        return sellingPriceProperty.get();
    }
    
    public void setSpreadDiscPercentage(BigDecimal percentage) {
        entity.setDocDiscPercDoc(percentage);
        BigDecimal discAmt = totalPriceProperty.get().multiply(percentage).divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
        entity.setDocDiscAmount(discAmt);
    }
    
    public final void setTransactionType(int transactionType) {
        if (entity.getTransactionType() == transactionType) {
            return;
        }
        
        switch (transactionType) {            
            case ITEM_SOLD:
                entity.setTransactionType(ITEM_SOLD);
                transactionTypeProperty.set(ITEM_SOLD);
                entity.setQty(BigDecimal.ONE);
                break;
            case ITEM_RETURN:
                entity.setTransactionType(ITEM_RETURN);
                transactionTypeProperty.set(ITEM_RETURN);
                entity.setQty(new BigDecimal(-1));
                break;
            case ITEM_VOID:
                entity.setTransactionType(ITEM_VOID);
                transactionTypeProperty.set(ITEM_VOID);
                entity.setQty(BigDecimal.ZERO);
                break;
        }
    }
    
    public final void setSellingPrice(BigDecimal price) {
        entity.setSellingPrice(price);
        sellingPriceProperty.set(price);
        
        recalcTotal();
    }
    
    public final void setDiscountPercentage(BigDecimal discPerc, boolean unlimitedDiscount) throws DtoException {
        BigDecimal price = entity.getSellingPrice();
        BigDecimal maxDiscPerc = unlimitedDiscount ? new BigDecimal(100) : entity.getInventory().getMaxDiscPerc1();
        maxDiscPerc = maxDiscPerc == null ? BigDecimal.ZERO : maxDiscPerc;
        
        if (discPerc.compareTo(maxDiscPerc) > 0) {
            throw new DtoException("Maximum discount percentage allowed is " + maxDiscPerc.setScale(2, RoundingMode.HALF_EVEN), maxDiscPerc);
        }
        BigDecimal discAmt = price.multiply(discPerc).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
        
        discPerc = discPerc.setScale(2, RoundingMode.HALF_EVEN);
        discAmt  = discAmt.setScale(2, RoundingMode.HALF_EVEN);
        
        entity.setDiscPerc(discPerc);
        entity.setDiscAmount(discAmt);
        discountPercentageProperty.set(discPerc);
        discountAmountProperty.set(discAmt);

        recalcTotal();
    }

    public final void setDiscountAmount(BigDecimal discAmt, boolean unlimitedDiscount) throws DtoException {
        BigDecimal price = entity.getSellingPrice();
        BigDecimal discPerc = discAmt.multiply(BigDecimal.valueOf(100)).divide(price, 2, RoundingMode.HALF_EVEN);
        BigDecimal maxDiscPerc = unlimitedDiscount ? new BigDecimal(100) : entity.getInventory().getMaxDiscPerc1();
        BigDecimal maxDiscAmt = price.multiply(maxDiscPerc).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
        
        if (discAmt.compareTo(maxDiscAmt) > 0) {
            throw new DtoException("Maximum discount amount allowed is " + maxDiscAmt.setScale(2, RoundingMode.HALF_EVEN), maxDiscPerc);
        }
        
        discPerc = discPerc.setScale(2, RoundingMode.HALF_EVEN);
        discAmt = discAmt.setScale(2, RoundingMode.HALF_EVEN);

        entity.setDiscPerc(discPerc);
        entity.setDiscAmount(discAmt);
        discountPercentageProperty.set(discPerc);
        discountAmountProperty.set(discAmt);

        recalcTotal();
    }
    
    public final void setQuantity(BigDecimal quantity) {
        switch(entity.getTransactionType()) {
            case ITEM_SOLD:
                if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
                    quantity = BigDecimal.ONE;
                }
                break;
            case ITEM_RETURN:
                if (quantity.compareTo(BigDecimal.ZERO) > 0) {
                    quantity = quantity.negate();
                } else if (quantity.compareTo(BigDecimal.ZERO) == 0) {
                    quantity = new BigDecimal(-1);
                }
                break;
            case ITEM_VOID:
                quantity = BigDecimal.ZERO;
                break;
        }
        entity.setQty(quantity);
        quantityProperty.set(quantity);
        recalcTotal();
    }
    
    public void incrementQuantity() {
        switch(entity.getTransactionType()) {
            case ITEM_SOLD:
                setQuantity(entity.getQty().add(BigDecimal.ONE));
                break;
            case ITEM_RETURN:
                setQuantity(entity.getQty().subtract(BigDecimal.ONE));
                break;
        }
    }

    private void recalcTotal() {
        BigDecimal quantity = entity.getQty();
        BigDecimal price = entity.getSellingPrice();
        BigDecimal discAmt = entity.getDiscAmount();
        BigDecimal globalDiscAmt = entity.getDocDiscAmount();
        BigDecimal taxRate = entity.getTaxRate() == null ? new BigDecimal(0) : entity.getTaxRate();
        
        if (price == null || discAmt == null || globalDiscAmt == null) {
            return;
        }
        
        BigDecimal cost = price.multiply(quantity);
        BigDecimal discount = discAmt.multiply(quantity.abs()).add(globalDiscAmt);
        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            discount = discount.multiply(BigDecimal.valueOf(-1));
        }
        
        cost = cost.subtract(discount);
        //entity.setCost(cost);
        
        BigDecimal tax = cost.multiply(taxRate).divide(new BigDecimal(100));
        cost = cost.add(tax);
        
        entity.setTaxAmount(tax);
                
        taxAmountProperty.set(tax.setScale(2, RoundingMode.HALF_EVEN));
        totalPriceProperty.set(cost.setScale(2, RoundingMode.HALF_EVEN));
    }
    
    @Override
    public int hashCode() {
        return this.getItemId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final ReceiptItemDto other = (ReceiptItemDto) obj;
        Long thisId  = this.getItemId();
        Long otherId = other.getItemId();
        if (!thisId.equals(otherId)) {
            return false;
        }
        return true;
    }
}
