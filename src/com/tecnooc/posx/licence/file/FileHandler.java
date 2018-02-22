/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tecnooc.posx.licence.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author jomit
 */
public class FileHandler {
    
    private Stage stage;
    
    public void write(String key) throws IOException {        
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save System Code");
        File keyFile = chooser.showSaveDialog(stage);
        
        if (keyFile == null) {
            return;
        } 
        
        writeFileContent(keyFile, key);
    }
    
    public String read() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Product Key File");
        File codeFile = chooser.showOpenDialog(stage);
        
        if (codeFile == null) {
            return null;
        }        
        
        return readFileContent(codeFile);
    }
    
    public String readFileContent(File file) throws IOException {
        Path path = Paths.get(file.toURI());
        return new String(Files.readAllBytes(path));
    }
    
    public void writeFileContent(File file, String key) throws IOException {
        Path path = Paths.get(file.toURI());
        Files.write(path, key.getBytes());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
