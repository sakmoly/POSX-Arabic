package com.tecnooc.desktop.app.posx.controller.impl;

import com.lowagie.text.DocumentException;
import com.tecnooc.desktop.app.posx.PosxApp;
import com.tecnooc.desktop.app.posx.controller.base.AbstractViewController;
import com.tecnooc.desktop.app.posx.controller.base.Dialog;
import com.tecnooc.desktop.app.posx.controller.base.DialogListener;
import com.tecnooc.desktop.app.posx.dto.ReceiptDto;
import com.tecnooc.desktop.app.posx.dto.ReceiptItemDto;
import com.tecnooc.desktop.app.posx.dto.ReceiptTenderDto;
import com.tecnooc.desktop.app.posx.dto.ZoutCurrencyDto;
import com.tecnooc.desktop.app.posx.dto.ZoutDto;
import com.tecnooc.desktop.app.posx.manager.ReportManager;
import com.tecnooc.desktop.app.posx.manager.SessionManager;
import com.tecnooc.desktop.app.posx.service.ReceiptService;
import com.tecnooc.desktop.app.posx.service.ZoutService;
import java.awt.Font;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 *
 * @author noufal
 */
public class ZoutViewContoller extends AbstractViewController {
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private ReportManager reportManager;
    @Autowired
    private ReceiptService receiptService;
    @Autowired
    private ZoutService zoutService;
    private ZoutDto zout;
    @FXML
    Label lblReportTitle;
    @FXML
    Label lblReportContent;
    
    private String currentReport = "";
    
    private void prepareReport() {
//        String sid = JOptionPane.showInputDialog("Enter Zout Sid:");
//        this.zout = zoutService.findById(Long.parseLong(sid));
        
        this.zout = zoutService.findLastReport();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
        Date now = new Date();
        
        List<ReceiptDto> receipts = receiptService.findByReceiptSidBetween(zout.getOpenInvcSid(), zout.getCloseInvcSid());
        BigDecimal salesNet = BigDecimal.ZERO;
        BigDecimal salesTax = BigDecimal.ZERO;
        BigDecimal salesDiscount = BigDecimal.ZERO;
        BigDecimal returnNet = BigDecimal.ZERO;
        BigDecimal returnTax = BigDecimal.ZERO;
        BigDecimal returnDiscount = BigDecimal.ZERO;
        int numTransactions = receipts.size();
        int numItems = 0;

        System.out.println("--- Start CSV ---");
        System.out.println("invc_sid,invc_no,created_date,item_sid,qty,selling_price,disc_amount,tax");
        for (ReceiptDto receipt : receipts) {
            if (receipt.isHeld()) {
                continue;
            }
            
            salesDiscount = salesDiscount.add(receipt.getDiscountAmount());
            for (ReceiptItemDto item : receipt.getItemsList()) {
                if (item.getTransactionType() == ReceiptItemDto.ITEM_SOLD) {
                    salesNet = salesNet.add(item.getQuantity().multiply(item.getSellingPrice()));
                    salesTax = salesTax.add(item.getTaxAmount());
                    salesDiscount = salesDiscount.add(item.getDiscountAmount());
                    numItems++;
                } else if (item.getTransactionType() == ReceiptItemDto.ITEM_RETURN) {
                    returnNet = returnNet.add(item.getQuantity().multiply(item.getSellingPrice()));
                    returnTax = returnTax.add(item.getTaxAmount());
                    returnDiscount = returnDiscount.add(item.getDiscountAmount());
                    numItems++;
                }
                
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.printf("\"%d\",\"%d\",\"%s\",\"%d\",\"%d\",\"%8.4f\",\"%8.4f\",\"%8.4f\"\n",
                        receipt.getReceiptSid(), receipt.getInvoiceNo(), 
                        formatter.format(receipt.getEntity().getCreatedDate()),
                        item.getItemId(), item.getQuantity().intValue(),
                        item.getSellingPrice().doubleValue(), 
                        item.getDiscountAmount().doubleValue(),
                        item.getTaxAmount().doubleValue());
            }
        }
        System.out.println("--- End CSV ---");
        
        returnNet    = returnNet.abs();
        returnDiscount = returnDiscount.abs();
        
        BigDecimal salesGross = salesNet.add(salesDiscount).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal returnGross = returnNet.add(returnDiscount).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal receiptNetAmount = salesGross.add(salesTax).subtract(returnGross.add(returnTax)).setScale(2, RoundingMode.HALF_EVEN);
        
        BigDecimal cash = BigDecimal.ZERO;
        BigDecimal creditCard = BigDecimal.ZERO;
        BigDecimal storeCredit = BigDecimal.ZERO;
        BigDecimal creditSale = BigDecimal.ZERO;

        for (ReceiptDto receipt : receipts) {
            for (ReceiptTenderDto tender : receipt.getTenderList()) {
                switch (tender.getTenderType()) {
                    case ReceiptTenderDto.TENDER_CASH:
                        cash = cash.add(tender.getAmount());
                        break;
                    case ReceiptTenderDto.TENDER_CREDIT_CARD:
                        creditCard = creditCard.add(tender.getAmount());
                        break;
                    case ReceiptTenderDto.TENDER_STORE_CREDIT:
                        storeCredit = storeCredit.add(tender.getAmount());
                        break;
                    case ReceiptTenderDto.TENDER_CREDIT:
                        creditSale = creditSale.add(tender.getAmount());
                        break;
                }
            }
        }
        
        currentReport = 
                  "Print Time\n"
                + "Date : " + new SimpleDateFormat("dd/MM/yyyy").format(now) + " \n"
                + "Time : " + new SimpleDateFormat("hh:mm:ss a").format(now) + " \n"
                + "                  \n"
                + "Subsidiary  : " + sessionManager.getSubsidiaryNumber() + "\n"
                + "Store       : " + sessionManager.getStore().getEntity().getStoreLogref() + ", " + sessionManager.getStore().getEntity().getStoreName() + "\n"
                + "Station     : " + sessionManager.getTerminal().getEntity().getTerminalLogref() + ", " + sessionManager.getTerminal().getTerminalName() + "\n"
                + "                        \n"
                + "Cashier     : " + sessionManager.getUser().getEntity().getUserId() + ", " + sessionManager.getUser().getEntity().getUserName() + "\n"
                + "Created By  : " + zout.getUser().getUserId() + ", " + zout.getUser().getUserName() + "\n"
                + "                     \n"
                + "DATE and TIME\n"
                + "From        : " + dateFormat.format(zout.getOpenInvcSid().getCreatedDate()) + "\n"
                + "To          : " + dateFormat.format(zout.getCloseInvcSid() == null ? now : zout.getCloseInvcSid().getCreatedDate()) + "\n"
                + "\n"
                + "SALES         \n"
                + "Gross       : " + salesGross.setScale(2, RoundingMode.HALF_EVEN) + "\n"
                + "Discounts   : " + salesDiscount.setScale(2, RoundingMode.HALF_EVEN) + "\n"
                + "Net         : " + salesNet.setScale(2, RoundingMode.HALF_EVEN) + "\n"
                + "VAT 5%      : " + salesTax.setScale(2, RoundingMode.HALF_EVEN) + "\n"
                + "Total       : " + salesGross.add(salesTax).setScale(2, RoundingMode.HALF_EVEN) + "\n"
                + "\n"
                + "RETURNS       \n"
                + "Gross       : " + returnGross.setScale(2, RoundingMode.HALF_EVEN) + "\n"
                + "Discounts   : " + returnDiscount.setScale(2, RoundingMode.HALF_EVEN) + "\n"
                + "Net         : " + returnNet.setScale(2, RoundingMode.HALF_EVEN) + "\n"
                + "VAT 5%      : " + returnTax.setScale(2, RoundingMode.HALF_EVEN) + "\n"
                + "Total       : " + returnGross.add(returnTax).setScale(2, RoundingMode.HALF_EVEN) + "\n"
                + "\n"
                + "Grant Total : " + receiptNetAmount + "\n"
                + "\n"
                + "RECEIPT COUNTS            \n"
                + "No of Transactions  : " + numTransactions + "\n"
                + "Item Counts         : " + numItems + "\n"
                + "First Receipt No    : " + (zout.getOpenInvcSid() == null ? 0 : zout.getOpenInvcSid().getInvcNo()) + "\n"
                + "\n"
                + "BREAKUP\n"
                + "\n"
                + "COLLECTIONS\n"
                + "Cash         : " + cash.setScale(2, RoundingMode.HALF_EVEN) + "\n"
                + "Credit Card  : " + creditCard.setScale(2, RoundingMode.HALF_EVEN) + "\n"
                + "Store Credit : " + storeCredit.setScale(2, RoundingMode.HALF_EVEN) + "\n"
                + "Credit Sale  : " + creditSale.setScale(2, RoundingMode.HALF_EVEN) + "\n"
                + "\n";
        
        if (zout.getTenderTotalClose() != null) {
            currentReport += "MEDIA COUNTS\n";
                    
            List<ZoutCurrencyDto> denominations = zout.getZoutCurrencyList();
            int count = 0;
            int i = 0;
            for (ZoutCurrencyDto currency : denominations) {
                count += currency.getMultiplier();

                currentReport += String.format("%10s : %d\n", 
                        denominations.get(i).getEntity().getCurrencyDenomination().getCurrencyDenomName(),
                        currency.getMultiplier());
                i++;
            }
            
            BigDecimal mediaCountTotal = zout.getTenderTotalClose().setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal drawerLeaveAmt = zout.getDrawerLeaveAmt().setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal openingAmt = zout.getTenderTotalOpen().setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal overShort = (mediaCountTotal.add(drawerLeaveAmt)).subtract((openingAmt.add(receiptNetAmount)));
            
            currentReport += "\n";
            currentReport += "Count            : " + count + "\n";
            currentReport += "Opening Amt      : " + openingAmt + "\n";
            currentReport += "Receipt Net Amt  : " + receiptNetAmount + "\n";
            currentReport += "Drawer(Leave Amt): " + drawerLeaveAmt + "\n";
            currentReport += "Deposit Amt      : " + zout.getDepositAmt().setScale(2, RoundingMode.HALF_EVEN) + "\n";
            currentReport += "Media Count Total: " + mediaCountTotal + "\n";
            //currentReport += "Cash Drop      : " + count + "\n";
            currentReport += "Over/Short       : " + overShort + "\n";
            
            lblReportTitle.setText("Z-OUT Report");
        } else {
            lblReportTitle.setText("X-OUT Report");
        }
    }

    void showReport() {
        prepareReport();
        lblReportContent.setText(currentReport);
    }    
    
    public void exportReport(ActionEvent ae) {
        String out = "<!DOCTYPE html>"
                + "<html>"
                + "    <head>"
                + "        <title>XOUT REPORT</title>"
                + "        <style type=\"text/css\">" 
                + "            body {font-size: 8pt;}"
                + "            @page {"
                + "                size: 74mm 105mm;"
                + "                background: white;"
                + "                margin: 5mm;"
                + "            }"
                + "        </style>"
                + "    </head>"
                + "    <body>"
                + "            <div>"
                + "                <pre>"
                + lblReportTitle.getText() + "\n"
                + currentReport
                + "                </pre>"
                + "            </div>"
                + "    </body>"
                + "</html>";
        
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(getView().getScene().getWindow());
        if (file != null) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(out);
            renderer.layout();
            try {
                OutputStream os = new FileOutputStream(file);
                renderer.createPDF(os);
                os.close();
            } catch (DocumentException | IOException ex) {
                Dialog.showMessageDialog(getView().getScene(), "Error Exporting", ex.getMessage());
            }
        }
    }
    

    @FXML
    public void printReport(ActionEvent ae) {       
        Dialog.showMessageDialog(
                getView().getScene(),
                "Choose Action",
                "What you want to do?\nPrint the report or export it as PDF?",
                Dialog.DialogType.PRINT_EXPORT_CANCEL,
                new DialogListener() {
            @Override
            public void dialogDismissed(Dialog.ButtonType pressedButton) {
                if (pressedButton == Dialog.ButtonType.PRINT) {

                    String out = lblReportTitle.getText() + "\n" + currentReport;
                    JTextComponent c = new JTextArea(out);
                    c.setFont(new Font(Font.MONOSPACED, Font.BOLD, 8));
                    try {
                        PrintRequestAttributeSet attrSet = new HashPrintRequestAttributeSet();
                        attrSet.add(MediaSizeName.ISO_A7);
                        attrSet.add(OrientationRequested.PORTRAIT);
                        c.print(null, null, false, null, attrSet, false);
                    } catch (PrinterException ex) {
                        Dialog.showMessageDialog(PosxApp.getApplicationScene(), "Error in Printing", ex.getMessage());
                    }
                } else if (pressedButton == Dialog.ButtonType.EXPORT) {
                    exportReport(null);
                }
            }
        });
    }
}