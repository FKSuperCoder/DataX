package com.volta.datax.playground.test5_swingGUI.stag;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class TableData implements TableModel {
    private String[] title = {"用户名", "帖子信息", "更新时间"};
    private String[][] data = {
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"},
            {"1", "2", "2022"}
    };


    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return data[0].length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return title[columnIndex];
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = (String) aValue;
    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
