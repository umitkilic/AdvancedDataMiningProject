/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package advanceddataminingproject;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author umit
 */
public class MyCustomFilter extends FileFilter{

    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getAbsolutePath().endsWith(".arff");
    }

    @Override
    public String getDescription() {
           return "Text documents (*.arff)";
    }
    
}
