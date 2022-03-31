package com.volta.datax.playground.test5_swingGUI;

import javax.swing.*;
import java.awt.*;

public class test1 {
    /**
     * {
     * 创建并显示GUI。出于线程安全的考虑，
     * 这个方法在事件调用线程中调用。
     */
    private static void createAndShowGUI() {
        // 确保一个漂亮的外观风格
        JFrame.setDefaultLookAndFeelDecorated(true);

        // 创建及设置窗口
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建网格布局
        GridLayout layout = new GridLayout(2,2);

        JPanel panel = new JPanel(layout);
        // 添加 "Hello World" 标签
        JLabel label = new JLabel("Hello World");
        panel.add(label);

        // 添加按钮
        JButton btn = new JButton("测试按钮");
        panel.add(btn);


        frame.setContentPane(panel);

        // 显示窗口
        frame.pack();
        frame.setSize(800, 600);// 在pack之后

        frame.setVisible(true);

    }

    public static void main(String[] args) {
        // 显示应用 GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
