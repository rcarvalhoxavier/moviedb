/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.moviedb.ui;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author rodrigo
 */
public class ObjectTableModel extends DefaultTableModel {

    private Object rowObject[];

    public ObjectTableModel(Object[][] os, Object[] os1) {
        super(os, os1);        
    }

    public void setRowObject(Object[] objs)
    {
        rowObject = objs;
        super.setRowCount(rowObject.length);
    }
    
    public Object getRowObject(int row)
    {
        return rowObject[row];
    }
    
    

   
    
}
