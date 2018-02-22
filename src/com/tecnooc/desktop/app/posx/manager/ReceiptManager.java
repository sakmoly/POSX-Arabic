package com.tecnooc.desktop.app.posx.manager;

import com.tecnooc.desktop.app.posx.controller.util.Util;
import com.tecnooc.desktop.app.posx.dto.CustomerDto;
import com.tecnooc.desktop.app.posx.dto.DtoException;
import com.tecnooc.desktop.app.posx.dto.InventoryItemDto;
import com.tecnooc.desktop.app.posx.dto.InventoryNumberDto;
import com.tecnooc.desktop.app.posx.dto.ReceiptDto;
import com.tecnooc.desktop.app.posx.dto.ReceiptItemDto;
import com.tecnooc.desktop.app.posx.dto.ReceiptTenderDto;
import com.tecnooc.desktop.app.posx.model.InventoryNumber;
import com.tecnooc.desktop.app.posx.service.CustomerService;
import com.tecnooc.desktop.app.posx.service.InventoryNumberService;
import com.tecnooc.desktop.app.posx.service.InvoiceItemNumberService;
import com.tecnooc.desktop.app.posx.service.ReceiptService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jomit
 */
@Component
public class ReceiptManager implements InitializingBean {        
    @Autowired private CustomerService customerService;
    @Autowired private ReceiptService receiptService;
    @Autowired private InventoryNumberService inventoryNumberService;
    @Autowired private SessionManager sessionManager;
    @Autowired private ReportManager reportManager;
    @Autowired private InventoryNumberHelper inventoryNumberHelper;
    @Autowired private InvoiceItemNumberService invoiceItemNumberService;
    
    private CustomerDto defaultCustomer;
    private ReceiptDto currentReceipt; 
    private ItemTotalChangeListener itemTotalChangeListener;
    private TenderAmountChangeListener tenderAmountChangeListener;
    
    private ObjectProperty<CustomerDto> currentCustomerProperty;
    private ObservableList<ReceiptItemDto> currentItemsProperty;
    private ObservableList<ReceiptTenderDto> currentTendersProperty;
    private ObjectProperty<BigDecimal> globalDiscountAmountProperty;
    private ObjectProperty<BigDecimal> subtotalProperty;
    private ObjectProperty<BigDecimal> taxAmountProperty;
    private ObjectProperty<BigDecimal> totalProperty;
    
    private ObjectProperty<ReceiptItemDto> selectedItemProperty;
    private ObjectProperty<ReceiptTenderDto> selectedTenderProperty;
    private IntegerProperty invoiceNumberProperty;    
    
    private ObservableList<ReceiptDto> heldReceiptsProperty;
    
    private boolean lastReceiptHeld = false;
    private boolean canUpdateSequenceNumber = true;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        defaultCustomer    = customerService.getDefaultCustomer();
        itemTotalChangeListener = new ItemTotalChangeListener();
        tenderAmountChangeListener = new TenderAmountChangeListener();
        
        currentCustomerProperty = new SimpleObjectProperty<>(defaultCustomer);
        currentItemsProperty    = FXCollections.observableArrayList();
        currentTendersProperty  = FXCollections.observableArrayList();
        globalDiscountAmountProperty = new SimpleObjectProperty<>(BigDecimal.ZERO);
        subtotalProperty             = new SimpleObjectProperty<>(BigDecimal.ZERO);
        taxAmountProperty            = new SimpleObjectProperty<>(BigDecimal.ZERO);
        totalProperty                = new SimpleObjectProperty<>(BigDecimal.ZERO);
        
        selectedItemProperty = new SimpleObjectProperty<>();
        selectedTenderProperty = new SimpleObjectProperty<>();
        invoiceNumberProperty = new SimpleIntegerProperty(sessionManager.nextSequenceValue());
        currentReceipt = new ReceiptDto();
        
        heldReceiptsProperty = FXCollections.observableArrayList(receiptService.findHeldReceipts());
    }
    
    public ObjectProperty<CustomerDto> currentCustomerProperty() {
        return currentCustomerProperty;
    }
    
    public ObservableList<ReceiptItemDto> currentItemsProperty() {
        return currentItemsProperty;
    }
    
    public ObservableList<ReceiptTenderDto> currentTendersProperty() {
        return currentTendersProperty;
    }
    
    public ObjectProperty<BigDecimal> globalDiscountAmountProperty() {
        return globalDiscountAmountProperty;
    }
    
    public ObjectProperty<BigDecimal> subtotalProperty() {
        return subtotalProperty;
    }
    
    public ObjectProperty<BigDecimal> taxAmountProperty() {
        return taxAmountProperty;
    }
    
    public ObjectProperty<BigDecimal> totalProperty() {
        return totalProperty;
    }
    
    public ObjectProperty<ReceiptItemDto> selectedItemProperty() {
        return selectedItemProperty;
    }
    
    public ObjectProperty<ReceiptTenderDto> selectedTenderProperty() {
        return selectedTenderProperty;
    }
    
    public IntegerProperty invoiceNumberProperty() {
        return invoiceNumberProperty;
    }
    
    public ObservableList<ReceiptDto> heldReceiptsProperty() {
        return heldReceiptsProperty;
    }
    
    public void setGlobalDiscountAmount(BigDecimal amount) throws DtoException {
        BigDecimal total = totalProperty.get().add(globalDiscountAmountProperty.get());
        BigDecimal subtotal = subtotalProperty.get().abs();
        if (amount.compareTo(subtotal) > 0) {
            throw new DtoException("You cannot give discount more than total.");
        }
        
        globalDiscountAmountProperty.set(amount);
        totalProperty.set(total.subtract(amount));
    }
    
    public void setGlobalDiscountPercentage(BigDecimal discPerc) throws DtoException {
        BigDecimal total = totalProperty.get().add(globalDiscountAmountProperty.get());
        BigDecimal subtotal = subtotalProperty.get().abs();
        if (discPerc.doubleValue() > 100.0) {
            throw new DtoException("You cannot give discount more than total.");
        }
        BigDecimal discAmt = subtotal.multiply(discPerc).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
        globalDiscountAmountProperty.set(discAmt);
        totalProperty.set(total.subtract(discAmt));
    }
    
    public void setCurrentCustomer(CustomerDto customer) {
        resetCredits();
        if (customer == null) {
            currentCustomerProperty.set(defaultCustomer);
        } else {
            currentCustomerProperty.set(customer);
        }
    }
    
    public CustomerDto getCurrentCustomer() {
        return currentCustomerProperty.get();
    }
    
    public void selectItem(ReceiptItemDto item) {
        selectedItemProperty.set(item);
        selectedTenderProperty.set(null);
    }
    
    public void selectTender(ReceiptTenderDto tender) {        
        selectedItemProperty.set(null);
        selectedTenderProperty.set(tender);
    }
    
    public ReceiptItemDto getSelectedItem() {
        return selectedItemProperty.get();
    }
    
    public ReceiptTenderDto getSelectedTender() {
        return selectedTenderProperty.get();
    }
    
    private void resetCredits() {
        ReceiptTenderDto storeCreditTender = null;
        ReceiptTenderDto creditTender = null;
        
        for (ReceiptTenderDto tender : currentTendersProperty) {
            if (tender.getTenderType() == ReceiptTenderDto.TENDER_STORE_CREDIT) {
                CustomerDto currentCustomer = getCurrentCustomer();
                int storeCredit = currentCustomer.getStoreCreditAvailable() + tender.getAmount().intValue();
                currentCustomer.setStoreCredit(storeCredit);
                storeCreditTender = tender;
            } else if (tender.getTenderType() == ReceiptTenderDto.TENDER_CREDIT) {
                CustomerDto currentCustomer = getCurrentCustomer();
                int creditRemaining = currentCustomer.getCreditRemaining() + tender.getAmount().intValue();
                currentCustomer.setCreditRemaining(creditRemaining);
                creditTender = tender;
            }
        }
        
        // Removing from inside above loop will cause concurrentmodificationexception
        // See: http://stackoverflow.com/questions/8189466/java-util-concurrentmodificationexception
        if (storeCreditTender != null)  {
            removeTender(storeCreditTender);
        }
        if (creditTender != null) {
            removeTender(creditTender);
        }        
    }
    
    public void addItem(String searchText, InventoryItemDto item) {
        addItem(searchText, item, null, null);
    }
    
    public void addItem(String searchText, InventoryItemDto item, BigDecimal quantity) {
        addItem(searchText, item, quantity, null);
    }
    
    public void addItem(String searchText, InventoryItemDto item, BigDecimal quantity, double price) {
        addItem(searchText, item, quantity, new BigDecimal(price));
    }
    
    private void addItem(String searchText, final InventoryItemDto item, final BigDecimal quantity, final BigDecimal price) {
        inventoryNumberHelper.init(item, searchText, getItem(item.getItemId()));
        inventoryNumberHelper.validate(new InventoryNumberHelper.ValidationListener() {
            @Override
            public void validationCompleted(InventoryItemDto itemDto, boolean isValid) {
                if (isValid) {
                    addItem(item, quantity, price);
                }
            }
        });
    }

    private ReceiptItemDto getItem(Long itemId) {
        for (ReceiptItemDto receiptItem : currentItemsProperty) {
            Long id = receiptItem.getItemId();
            if (itemId.equals(id)) {
                return receiptItem;
            }
        }

        return null;
    }
    
    private void addItem(InventoryItemDto item, BigDecimal quantity, BigDecimal price) {                
        Long itemId = item.getItemId();
        ReceiptItemDto receiptItem = getItem(itemId);

        if (receiptItem != null) {
            Long id = receiptItem.getItemId();
            if (itemId.equals(id)) {
                if (quantity == null) {
                    receiptItem.incrementQuantity();
                } else {
                    receiptItem.setQuantity(quantity.add(receiptItem.getQuantity()));
                }
                
                if (price != null) {                    receiptItem.setSellingPrice(price.divide(quantity, 2, RoundingMode.HALF_EVEN).setScale(2, RoundingMode.HALF_EVEN));
                }
                
                InventoryNumberDto inventoryNumber = item.getInventoryNumberDto();
                if (inventoryNumber != null) {
                    receiptItem.getInventoryNumbers().add(inventoryNumber);
                }
                
                // no need to recalc total here, changelistener will take care of it.
                return;
            }
        }

        receiptItem = new ReceiptItemDto(item);
        
        if (quantity != null) {
            receiptItem.setQuantity(quantity);
        }
        
        if (price != null) {
            receiptItem.setSellingPrice(price.divide(quantity, 2, RoundingMode.HALF_EVEN));
        }
        
        receiptItem.totalPriceProperty().addListener(itemTotalChangeListener);
        currentItemsProperty.add(receiptItem);
        selectItem(receiptItem);

        BigDecimal subtotal = subtotalProperty.get();
        BigDecimal total = totalProperty.get();
        BigDecimal taxAmount = taxAmountProperty.get();
        
        subtotalProperty.set(subtotal.add(receiptItem.getTotalPrice()));
        taxAmountProperty.set(taxAmount.add(receiptItem.getTaxAmount()));
        totalProperty.set(total.add(receiptItem.getTotalPrice()));
    }
    
    private void removeItem(ReceiptItemDto item) {        
        if (currentItemsProperty.remove(item)) {
            item.totalPriceProperty().removeListener(itemTotalChangeListener);
            
            BigDecimal subtotal = subtotalProperty.get();
            BigDecimal total = totalProperty.get();
            BigDecimal taxAmount = taxAmountProperty.get();
            
            subtotalProperty.set(subtotal.subtract(item.getTotalPrice()));
            taxAmountProperty.set(taxAmount.subtract(item.getTaxAmount()));
            totalProperty.set(total.subtract(item.getTotalPrice()));
            
            selectItem(null);
        }
        
        if (currentItemsProperty.isEmpty()) {
            resetCredits();
        }
    }
    
    public void addTender(ReceiptTenderDto tender) {
        BigDecimal total = totalProperty.get();
        tender.amountProperty().addListener(tenderAmountChangeListener);
        int newTenderType = tender.getTenderType();   
        
        if (newTenderType != ReceiptTenderDto.TENDER_CREDIT_CARD) {
            for (int i = 0; i < currentTendersProperty.size(); i++) {
                ReceiptTenderDto receiptTender = currentTendersProperty.get(i);
                int receiptTenderType = receiptTender.getTenderType();

                if (newTenderType == receiptTenderType) {
                    if (newTenderType == ReceiptTenderDto.TENDER_CASH) {
                        BigDecimal newValue = receiptTender.getAmount().add(tender.getAmount());
                        tender.setAmount(newValue);
                    } 
                    
                    currentTendersProperty.set(i, tender);
                    total = total.add(receiptTender.getAmount());
                    total = total.subtract(tender.getAmount());
                    totalProperty.set(total);

                    receiptTender.amountProperty().removeListener(tenderAmountChangeListener);
                    return;
                }
            }
        }
        
        currentTendersProperty.add(tender);
        totalProperty.set(total.subtract(tender.getAmount()));
        selectTender(tender);
    }
    
    public boolean hasReturnedItem() {
        for (ReceiptItemDto receiptItemDto : currentItemsProperty) {
            if (receiptItemDto.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasAllReturnedItem() {
        for (ReceiptItemDto receiptItemDto : currentItemsProperty) {
            if (receiptItemDto.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
                return false;
            }
        }
        return true;
    }
    
    public void useStoreCredit(boolean returnInclude) {
        int total = totalProperty.get().setScale(2, RoundingMode.CEILING).intValue();
        int storeCredit = getCurrentCustomer().getStoreCreditAvailable();
        
        if (storeCredit > 0 || total < 0) {
            if (total < 0 && !hasReturnedItem()) {
                return;
            }
            if (total < 0 && !returnInclude) {
                return;
            }
            
            ReceiptTenderDto tender = new ReceiptTenderDto();
            tender.setTenderType(ReceiptTenderDto.TENDER_STORE_CREDIT);
            
            int amount = Math.min(storeCredit, total);
            getCurrentCustomer().useStoreCredit(amount);
            
            for (ReceiptTenderDto receiptTender : currentTendersProperty) {
                if (receiptTender.getTenderType() == ReceiptTenderDto.TENDER_STORE_CREDIT) {
                    amount = amount + receiptTender.getAmount().intValue();
                    break;
                }
            }
            tender.setAmount(new BigDecimal(amount));
            if (amount < 0) {
                tender.setGivenAmount(new BigDecimal(amount * -1));
            }
            
            addTender(tender);
        }
    }
    
    public void useCreditSale() {
        int total = totalProperty.get().setScale(2, RoundingMode.CEILING).intValue();
        int creditRemaining = getCurrentCustomer().getCreditRemaining();
        
        if (total > 0 && creditRemaining > 0) {
            ReceiptTenderDto tender = new ReceiptTenderDto();
            tender.setTenderType(ReceiptTenderDto.TENDER_CREDIT);
            
            int amount = Math.min(creditRemaining, total);
            getCurrentCustomer().useCredit(amount);
            
            for (ReceiptTenderDto receiptTender : currentTendersProperty) {
                if (receiptTender.getTenderType() == ReceiptTenderDto.TENDER_CREDIT) {
                    amount = amount + receiptTender.getAmount().intValue();
                    break;
                }
            }
            tender.setAmount(new BigDecimal(amount));
            addTender(tender);
        }
    }
    
    public void useCreditSale(int amount) {        
        ReceiptTenderDto tender = new ReceiptTenderDto();
        tender.setTenderType(ReceiptTenderDto.TENDER_CREDIT);
        
        getCurrentCustomer().useCredit(amount);

        for (ReceiptTenderDto receiptTender : currentTendersProperty) {
            if (receiptTender.getTenderType() == ReceiptTenderDto.TENDER_CREDIT) {
                amount = amount + receiptTender.getAmount().intValue();
                break;
            }
        }
        tender.setAmount(new BigDecimal(amount));
        if (amount < 0) {
            tender.setGivenAmount(new BigDecimal(-amount));
        }
        addTender(tender);
    }
    
    public int getIntegerTotal() {
        return totalProperty.get().setScale(2, RoundingMode.CEILING).intValue();
    }
    
    private void removeTender(ReceiptTenderDto tender) {
        if (currentTendersProperty.remove(tender)) {
            tender.amountProperty().removeListener(tenderAmountChangeListener);
            
            BigDecimal total = totalProperty.get();
            totalProperty.set(total.add(tender.getAmount()));
            selectTender(null);
        }
    }

    public void removeSelectedItem() {
        if (getSelectedItem() != null) {
            removeItem(getSelectedItem());
        }
    }
    
    public void removeSelectedTender() {
        ReceiptTenderDto tender = getSelectedTender();
        if (tender != null) {
            if (tender.getTenderType() == ReceiptTenderDto.TENDER_STORE_CREDIT) {
                CustomerDto currentCustomer = getCurrentCustomer();
                int storeCredit = currentCustomer.getStoreCreditAvailable() + tender.getAmount().intValue();
                currentCustomer.setStoreCredit(storeCredit);
            } else if (tender.getTenderType() == ReceiptTenderDto.TENDER_CREDIT) {
                CustomerDto currentCustomer = getCurrentCustomer();
                int creditRemaining = currentCustomer.getCreditRemaining() + tender.getAmount().intValue();
                currentCustomer.setCreditRemaining(creditRemaining);
            }
            
            removeTender(tender);
        }
    }
    
    public void removeAllTenders() {
        for (ReceiptTenderDto tender : currentTendersProperty) {
            if (tender != null) {
                if (tender.getTenderType() == ReceiptTenderDto.TENDER_STORE_CREDIT) {
                    CustomerDto currentCustomer = getCurrentCustomer();
                    int storeCredit = currentCustomer.getStoreCreditAvailable() + tender.getAmount().intValue();
                    currentCustomer.setStoreCredit(storeCredit);
                } else if (tender.getTenderType() == ReceiptTenderDto.TENDER_CREDIT) {
                    CustomerDto currentCustomer = getCurrentCustomer();
                    int creditRemaining = currentCustomer.getCreditRemaining() + tender.getAmount().intValue();
                    currentCustomer.setCreditRemaining(creditRemaining);
                }
                
                tender.amountProperty().removeListener(tenderAmountChangeListener);                
                BigDecimal total = totalProperty.get();
                totalProperty.set(total.add(tender.getAmount()));
                selectTender(null);
            }
        }
        
        currentTendersProperty.clear();
    }
    
    public void updateReceipt() {            
        if (!currentItemsProperty.isEmpty()) {
            if (currentReceipt.getReceiptSid() != null) {
                // remove previous references.
                try {
                    receiptService.remove(currentReceipt.getReceiptSid());
                } catch (Exception ex) {
                    // the receipt maynot exists sometimes, especially on new db, ignore the exception on remove.
                }
                
                currentReceipt.setModifiedDate(new Date());
            }

            saveReceipt(false);

            canUpdateSequenceNumber = true;
            startNewReceipt();
        }
    }
    
    public void openNewReceipt() {
        if (!currentItemsProperty.isEmpty()) {
            if (currentReceipt.getReceiptSid() != null) {
                // remove previous references.
                receiptService.remove(currentReceipt.getReceiptSid());
                currentReceipt.setModifiedDate(new Date());
            }
            
            saveReceipt(true);
        
            canUpdateSequenceNumber = false;
            startNewReceipt();
        }
    }
    
    private void saveReceipt(boolean held) { 
        if (hasAllReturnedItem()) {
            currentReceipt.setInvoiceType(2);
        } else {
            currentReceipt.setInvoiceType(0);
        }
        lastReceiptHeld = held;
//        if (held) {
//            currentReceipt.setInvoiceNo(0);
//        } else {
            currentReceipt.setInvoiceNo(invoiceNumberProperty.get());
//        }
        
        currentReceipt.setHeld(held);
        currentReceipt.setSubsidiaryNo(sessionManager.getSubsidiaryNumber());

        currentReceipt.setCustomer(currentCustomerProperty.get());
        currentReceipt.setStore(sessionManager.getStore());
        currentReceipt.setUser(sessionManager.getUser());

        currentReceipt.setItemsList(currentItemsProperty);
        currentReceipt.setTenderList(currentTendersProperty);
               
        currentReceipt.setDiscountAmount(globalDiscountAmountProperty.get());
        BigDecimal discPerc = BigDecimal.ZERO;
        if (subtotalProperty.get().compareTo(BigDecimal.ZERO) > 0) {
            discPerc = globalDiscountAmountProperty.get().divide(
                                    subtotalProperty.get(), 2, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
        }
        currentReceipt.setDiscountPercentage(discPerc);
        //BigDecimal discPercSpread = discPerc.divide(new BigDecimal(currentItemsProperty.size()));
        currentReceipt.setDiscountPercentageSpread(discPerc);
        
        for (ReceiptItemDto receiptItemDto : currentItemsProperty) {
            receiptItemDto.setSpreadDiscPercentage(discPerc);
        }
        
        for (ReceiptTenderDto receiptTenderDto : currentTendersProperty) {
            if (receiptTenderDto.getTenderType() == ReceiptTenderDto.TENDER_CASH) {
                BigDecimal given = BigDecimal.ZERO;
                if (totalProperty.get().compareTo(BigDecimal.ZERO) < 0) {
                    given = totalProperty.get().abs();
                }
                
                BigDecimal taken = receiptTenderDto.getAmount();
                BigDecimal amt   = taken.subtract(given);
                                
                receiptTenderDto.setTakenAmount(taken);
                receiptTenderDto.setGivenAmount(given);
                receiptTenderDto.setAmount(amt);
                
                break;
            }
        }
        
        // Set the receipt id last to avoid nullpointer exception when setting invoiceitempk
        currentReceipt.setReceiptSid(Util.generateSid());
        receiptService.save(currentReceipt);
        customerService.save(getCurrentCustomer());
        
        if (!held) {
            reportManager.updateInvoiceSid(currentReceipt);
            updateSerialStatus(currentItemsProperty);
        }
    }
    
    private void updateSerialStatus(ObservableList<ReceiptItemDto> items) {
        Long invcSid = currentReceipt.getReceiptSid();
        
        for (ReceiptItemDto item : items) {
            Long itemSid = item.getItemId();
            List<InventoryNumberDto> inventoryNumbers = item.getInventoryNumbers();
            
            for (InventoryNumberDto number : inventoryNumbers) {
                if (number != null) {
                    Long inventoryNumberRef = number.getEntity().getLogicalRef();
                    invoiceItemNumberService.insert(invcSid, itemSid, inventoryNumberRef);
                    
                    if (number.getEntity().getType() == 1) {
                        number.getEntity().setSerialSold(1);
                        inventoryNumberService.save(number);
                    }
                }
            }
        }
    }
    
    private void startNewReceipt() {
        currentReceipt = new ReceiptDto();
        setCurrentCustomer(defaultCustomer);
        currentItemsProperty.clear();
        currentTendersProperty.clear();
        try {
            setGlobalDiscountAmount(BigDecimal.ZERO);
        } catch (DtoException ex) {System.out.println(ex);}
        subtotalProperty.set(BigDecimal.ZERO);
        taxAmountProperty.set(BigDecimal.ZERO);
        totalProperty.set(BigDecimal.ZERO);
        selectItem(null);
        selectTender(null);
        
        if (canUpdateSequenceNumber) {
            sessionManager.updateSequenceValue();
        }
        lastReceiptHeld = false;
        invoiceNumberProperty.set(sessionManager.nextSequenceValue());
        
        heldReceiptsProperty.clear();
        heldReceiptsProperty.addAll(receiptService.findHeldReceipts());
    }
    
    public void removeReceipt() {
        if (currentReceipt.getReceiptSid() != null) {
            // remove previous references.
            receiptService.remove(currentReceipt.getReceiptSid());
        }
        
        canUpdateSequenceNumber = false;
        startNewReceipt();
    }
    
    public boolean isCurrentReceiptEmpty() {
        return currentItemsProperty.isEmpty() 
                && currentTendersProperty.isEmpty() 
                && currentCustomerProperty.get() == defaultCustomer;
    }
    
    public boolean isTendered() {
        return !currentTendersProperty.isEmpty();
    }
    
    public boolean isLoaded(ReceiptDto receipt) {
        return currentReceipt.getReceiptSid() != null && currentReceipt.getReceiptSid().equals(receipt.getReceiptSid());
    }

    public void loadReceipt(ReceiptDto receipt) {
        if (isLoaded(receipt)) {
            return;
        }
        
        if (!currentItemsProperty.isEmpty()) {
            if (currentReceipt.getReceiptSid() != null) {
                // remove previous references.
                receiptService.remove(currentReceipt.getReceiptSid());
                currentReceipt.setModifiedDate(new Date());
            }
            
            saveReceipt(true);
        }
        
        currentReceipt = receipt;
        setCurrentCustomer(receipt.getCustomer());
        
        currentItemsProperty.clear();
        currentItemsProperty.addAll(receipt.getItemsList());
        currentTendersProperty.clear();
        currentTendersProperty.addAll(receipt.getTenderList());
        
        try {
            setGlobalDiscountAmount(receipt.getDiscountAmount());
        } catch (DtoException ex) {
            System.out.println(ex);
        }
        
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;
        for (ReceiptItemDto receiptItem : currentItemsProperty) {
            receiptItem.totalPriceProperty().addListener(itemTotalChangeListener);
            subtotal = subtotal.add(receiptItem.getTotalPrice());
            taxAmount = taxAmount.add(receiptItem.getTaxAmount());
        }
        BigDecimal tenderTotal = BigDecimal.ZERO;
        for (ReceiptTenderDto receiptTender : currentTendersProperty) {
            receiptTender.amountProperty().removeListener(tenderAmountChangeListener);
            tenderTotal = tenderTotal.add(receiptTender.getAmount());
        }
        
        subtotalProperty.set(subtotal.setScale(2, RoundingMode.HALF_EVEN));
        taxAmountProperty.set(taxAmount.setScale(2, RoundingMode.HALF_EVEN));
        totalProperty.set(subtotal.subtract(tenderTotal).setScale(2, RoundingMode.HALF_EVEN));
        selectItem(null);
        selectTender(null);
        invoiceNumberProperty.set(sessionManager.nextSequenceValue());

        heldReceiptsProperty.clear();
        heldReceiptsProperty.addAll(receiptService.findHeldReceipts());        
    }
    
    private class ItemTotalChangeListener implements ChangeListener<BigDecimal> {
        @Override
        public void changed(ObservableValue<? extends BigDecimal> observable, BigDecimal oldTotal, BigDecimal newTotal) {
            BigDecimal subtotal = subtotalProperty.get();
            BigDecimal total = totalProperty.get();
            
            subtotalProperty.set(subtotal.subtract(oldTotal).add(newTotal));
            totalProperty.set(total.subtract(oldTotal).add(newTotal));
            
            BigDecimal taxAmount = BigDecimal.ZERO;
            for (ReceiptItemDto receiptItem : currentItemsProperty) {
                taxAmount = taxAmount.add(receiptItem.getTaxAmount());
            }
            taxAmountProperty.set(taxAmount.setScale(2, RoundingMode.HALF_EVEN));
            
            BigDecimal globalDisc = globalDiscountAmountProperty.get();
            if (globalDisc.compareTo(subtotalProperty.get().abs()) > 0) {
                totalProperty.set(totalProperty.get().add(globalDiscountAmountProperty.get()));
                globalDiscountAmountProperty.set(BigDecimal.ZERO);
            }
        }
    }
    
    private class TenderAmountChangeListener implements ChangeListener<BigDecimal> {
        @Override
        public void changed(ObservableValue<? extends BigDecimal> observable, BigDecimal oldAmount, BigDecimal newAmount) {
            BigDecimal total = totalProperty.get();

            totalProperty.set(total.subtract(oldAmount).add(newAmount));
        }
    }
}
