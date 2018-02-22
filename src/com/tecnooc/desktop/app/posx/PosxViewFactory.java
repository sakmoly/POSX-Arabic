package com.tecnooc.desktop.app.posx;

import com.tecnooc.desktop.app.posx.controller.base.ViewController;
import com.tecnooc.desktop.app.posx.controller.impl.CloseDialogViewController;
import com.tecnooc.desktop.app.posx.controller.impl.EditItemViewController;
import com.tecnooc.desktop.app.posx.controller.impl.EditTenderViewController;
import com.tecnooc.desktop.app.posx.controller.impl.LoginViewController;
import com.tecnooc.desktop.app.posx.controller.impl.MainViewController;
import com.tecnooc.desktop.app.posx.controller.impl.OpenDialogViewController;
import com.tecnooc.desktop.app.posx.controller.impl.ReceiptListViewController;
import com.tecnooc.desktop.app.posx.controller.impl.ReceiptViewController;
import com.tecnooc.desktop.app.posx.controller.impl.SearchCustomerViewController;
import com.tecnooc.desktop.app.posx.controller.impl.SearchItemViewController;
import com.tecnooc.desktop.app.posx.controller.impl.SplashViewController;
import com.tecnooc.desktop.app.posx.controller.impl.TenderCreditCardViewController;
import com.tecnooc.desktop.app.posx.controller.impl.TenderViewController;
import com.tecnooc.desktop.app.posx.controller.impl.ZoutViewContoller;
import com.tecnooc.desktop.app.posx.controller.util.InventoryItemHBox;
import com.tecnooc.desktop.app.posx.controller.util.ShortcutManager;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author jomit
 */
@Configuration
@ImportResource("classpath:res/config/application-context.xml")
@Lazy
public class PosxViewFactory {
    public static final String FXML_BASE_LOCATION = "res/skin/basic/fxml/";
    
    @Bean
    public ShortcutManager shortcutManager() {
        return new ShortcutManager();
    }
        
    @Bean
    public LoginViewController loginViewController() throws IOException {
        return (LoginViewController) loadController("login.fxml");
    }
    
    @Bean
    public MainViewController mainViewController() throws IOException {
        return (MainViewController) loadController("main.fxml");
    }

    @Bean
    public ReceiptViewController receiptViewController() throws IOException {
        return (ReceiptViewController) loadController("main_receipt.fxml");
    }
    
    @Bean
    public SearchCustomerViewController searchCustomerViewController() throws IOException {
        return (SearchCustomerViewController) loadController("main_customer_search.fxml");
    }  
    
    @Bean
    public SearchItemViewController searchItemViewController() throws IOException {
        return (SearchItemViewController) loadController("main_receipt_search.fxml");
    }
    
    @Bean
    public EditItemViewController editItemViewController() throws IOException {
        return (EditItemViewController) loadController("main_receipt_edit.fxml");
    } 
    
    @Bean
    public EditTenderViewController editTenderViewController() throws IOException {
        return (EditTenderViewController) loadController("main_receipt_edit_tender.fxml");
    } 

    @Bean
    public TenderViewController tenderViewController() throws IOException {
        return (TenderViewController) loadController("main_receipt_tender.fxml");
    }  
    
    @Bean
    public TenderCreditCardViewController tenderCreditCardViewController() throws IOException {
        return (TenderCreditCardViewController) loadController("main_receipt_tender_credit_card.fxml");
    }
    
    @Bean
    public ReceiptListViewController receiptListViewController() throws IOException {
        return (ReceiptListViewController) loadController("main_receipt_hold.fxml");
    }

    @Bean
    public OpenDialogViewController openDialogViewController() throws IOException {
        return (OpenDialogViewController) loadController("main_open_dialog.fxml");
    }

    @Bean
    public CloseDialogViewController closeDialogViewController() throws IOException {
        return (CloseDialogViewController) loadController("main_close_dialog.fxml");
    }
        
    @Bean
    public ZoutViewContoller zoutViewContoller() throws IOException {
        return (ZoutViewContoller) loadController("main_zout.fxml");
    }
        
    @Bean
    public SplashViewController splashViewController() throws IOException {
        return (SplashViewController) loadController("splash.fxml");
    }

    @Bean
    @Scope(value = "prototype")
    public InventoryItemHBox inventoryItemListCell() throws IOException {
        String url = FXML_BASE_LOCATION + "main_receipt_search_listcell.fxml";
        InventoryItemHBox cell = new InventoryItemHBox();
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(url));
        loader.setRoot(cell);
        loader.setController(cell);
        loader.load();
        
        return cell;
    }
    
    protected ViewController loadController(String fxmlName) throws IOException {
        String url = FXML_BASE_LOCATION + fxmlName;
                   
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(url));
        Node view = (Node) loader.load();
        ViewController controller = (ViewController) loader.getController();
        controller.setView(view);
        return controller;
    }
}
