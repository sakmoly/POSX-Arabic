package com.tecnooc.desktop.app.posx;

import com.tecnooc.desktop.app.posx.controller.impl.LoginViewController;
import com.tecnooc.desktop.app.posx.controller.util.ShortcutManager;
import com.tecnooc.posx.licence.controller.LicenceController;
import com.tecnooc.posx.licence.util.LicenceUtil;
import java.lang.reflect.Field;
import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 *
 * @author jomit
 */
public class PosxApp extends Application {
    private AbstractApplicationContext appContext;
    private static Scene appScene;
    
    private Stage primaryStage;
    private Stage splashStage;
    private Stage licenceStage;
    
    private String title = "POS-X Terminal v1.1u8";
    
    public static Scene getApplicationScene() {
        return appScene;
    }
    
    public String getTitle() {
        return title;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        
        if (!LicenceUtil.isActivated()) {
            showApp();
        } else {
            licenceStage = new Stage();
            LicenceController.showActivationView(this, licenceStage);
        }
    }
    
    private void loadFonts()
    {
//        Font.loadFont(PosxApp.class.getResource("Cantarell-Regular.ttf").toExternalForm(), 14);
//        Font.loadFont(PosxApp.class.getResource("Cantarell-Bold.ttf").toExternalForm(), 14);
        try {
            Class<?> macFontFinderClass = Class.forName("com.sun.t2k.MacFontFinder");
            Field psNameToPathMap = macFontFinderClass.getDeclaredField("psNameToPathMap");
            psNameToPathMap.setAccessible(true);
            psNameToPathMap.set(null, new HashMap<String, String>());
        } catch (Exception e) {
            // ignore
        }
    }
    
    public void showApp() {
        if (licenceStage != null) {
            licenceStage.hide();
            
            if (!LicenceUtil.isActivated()) {
                LicenceUtil.scheduleTrialTimeCounter();
            }
        }
        
        showSplash();
        //loadFonts();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {

                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showPrimary();
                    }
                });
            }
        });
        thread.start();
    }
    
    public void showSplash() {
        splashStage = new Stage(StageStyle.UNDECORATED);
        splashStage = new Stage(StageStyle.UNDECORATED);
        splashStage.getIcons().add(
                new Image(getClass().getClassLoader().getResource("res/skin/basic/image/logo_16.png").toExternalForm(),
                        16, 16, false, true));
        splashStage.getIcons().add(
                new Image(getClass().getClassLoader().getResource("res/skin/basic/image/logo_32.png").toExternalForm(),
                        32, 32, false, true));
        splashStage.getIcons().add(
                new Image(getClass().getClassLoader().getResource("res/skin/basic/image/logo_48.png").toExternalForm(),
                        48, 48, false, true));
        splashStage.getIcons().add(
                new Image(getClass().getClassLoader().getResource("res/skin/basic/image/logo_64.png").toExternalForm(),
                        64, 64, false, true));
        splashStage.getIcons().add(
                new Image(getClass().getClassLoader().getResource("res/skin/basic/image/logo_128.png").toExternalForm(),
                        128, 128, false, true));
        splashStage.getIcons().add(
                new Image(getClass().getClassLoader().getResource("res/skin/basic/image/logo_256.png").toExternalForm(),
                        256, 256, false, true));
        splashStage.getIcons().add(
                new Image(getClass().getClassLoader().getResource("res/skin/basic/image/logo_512.png").toExternalForm(),
                        512, 512, false, true));

        StackPane pane = new StackPane();
        pane.getChildren().add(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("res/skin/basic/image/splash.png"))));
        splashStage.setScene(new Scene(pane, 620, 300));
        splashStage.show();
    }
    
    public void showPrimary() {
        appContext = new AnnotationConfigApplicationContext(PosxViewFactory.class);
        appContext.registerShutdownHook();

        LoginViewController controller = (LoginViewController) appContext.getBean(LoginViewController.class);
        controller.setPrimaryStage(primaryStage);

        StackPane root = new StackPane();
        root.getChildren().add((Parent) controller.getView());

        // Start Window Maximized.
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        Scene scene = new Scene(root);
        appScene = scene;
        scene.getStylesheets().add("/res/skin/basic/css/main.css");

        primaryStage.setScene(scene);
        ShortcutManager shortcutManager = (ShortcutManager) appContext.getBean(ShortcutManager.class);
        shortcutManager.init(scene);
        
        /*shortcutManager.setShortcutListener(Shortcut.EXIT, new ShortcutActionListener() {
            @Override
            public void shortcutActionPerformed(int shortcutId, KeyEvent event) {
                appContext.close();
                System.exit(0);
            }
        });*/

        //root.setPrefSize(bounds.getWidth(), bounds.getHeight());
        //primaryStage.setFullScreen(true);
        
        primaryStage.setTitle(title);
        primaryStage.show();

        splashStage.hide();
    }

    @Override
    public void stop() throws Exception {
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
