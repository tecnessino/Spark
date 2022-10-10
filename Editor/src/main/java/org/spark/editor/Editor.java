package org.spark.editor;

import javax.swing.*;
import java.awt.*;

public class Editor {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setSize(500,200);
        frame.setBackground(new Color(40, 37, 37));
        frame.getContentPane().setBackground(new Color(40, 37, 37));
        frame.setVisible(true);
    }
}
