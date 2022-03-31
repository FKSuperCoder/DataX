package com.volta.datax.playground.test5_swingGUI.stag;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

// 提供panel，并放入主动创建的JFrame中
public class DataXGUI {
    private JPanel rootPanel;
    private JPanel headerBar;
    private JPanel sideListPanel;
    private JPanel content;
    private JList list1;
    private JTable dataTable;
    private JPanel bottomInfoPanel;
    private JPanel tableContentPanel;

    public DataXGUI() {

        dataTable.setModel(new TableData());
    }


    public JTable getDataTable() {
        String[] columnNames = {"用户名", "用户回答", "回答日期"};
        Object[][] rowData = {
                {"1", "2", "2022-2-13"},
                {"1", "2", "2022-2-13"},
                {"1", "2", "2022-2-13"},
        };
        return new JTable(rowData, columnNames);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("DataX GUI");
        JFrame.setDefaultLookAndFeelDecorated(false);

        frame.setContentPane((new DataXGUI()).rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }


}
