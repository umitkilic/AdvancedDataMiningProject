/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package advanceddataminingproject;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author umit
 */
public class MyListCellThing extends JLabel implements ListCellRenderer{
    List<Integer> indexes=new ArrayList<>();
    
    public MyListCellThing(List<Integer> L) {
        setOpaque(true);
        this.indexes=L;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        // Assumes the stuff in the list has a pretty toString
        setText(value.toString());

        // based on the index you set the color.  This produces the every other effect.
        if (indexes.contains(index)){
            setBackground(Color.RED);
        }
        else {
            setBackground(Color.GREEN);
        }

        return this;
    }
}
