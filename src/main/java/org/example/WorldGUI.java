package org.example;

import javax.swing.*;
import java.awt.*;

public class WorldGUI {
    public WorldGUI() {
        JFrame frame = new JFrame("World Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600); // Pencere boyutunu ayarla
        frame.setLocationRelativeTo(null); // Pencereyi ekranın ortasına yerleştir
        frame.setVisible(true);
    }
}
