// WorldSimulator.java
package org.example;

import org.example.kafka.KafkaConsumer;
import org.example.kafka.KafkaProducer;
import org.example.models.Tower;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.Executors;

public class WorldSimulator {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel controlPanel;
    private int targetX = 100;
    private int targetY = 100;
    private boolean isPlaying = false;

    private KafkaProducer kafkaProducer = KafkaProducer.getInstance();

    public void startSimulation() {
        createAndShowGUI();
        KafkaConsumer kafkaConsumer = new KafkaConsumer();
        Executors.newSingleThreadExecutor().execute(kafkaConsumer);
    }

    private void createAndShowGUI() {
        frame = new JFrame("World Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        controlPanel = new JPanel(new FlowLayout());

        JPanel scenePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCoordinatePlane(g);
                drawTarget(g);
                drawTowers(g);
            }
        };
        scenePanel.setPreferredSize(new Dimension(400, 300));

        JButton playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isPlaying = true;
                simulateTargetMovement();
            }
        });

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isPlaying = false;
            }
        });

        controlPanel.add(playButton);
        controlPanel.add(stopButton);

        mainPanel.add(scenePanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void drawCoordinatePlane(Graphics g) {
        g.setColor(Color.BLACK);
        // L harfi çizimi
        g.drawLine(20, 20, 20, 350); // Dikey çizgi
        g.drawLine(20, 350, 350, 350); // Yatay çizgi
    }


    private void drawTarget(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(targetX, targetY, 10, 10); // Target
    }

    private void drawTowers(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(50, 230, 20, 60); // Tower 1
        g.fillRect(300, 230, 20, 60); // Tower 2
        Tower t1 = new Tower(50, 230);
        kafkaProducer.produceTowerPositionData(t1);
        Tower t2 = new Tower(300, 230);
        kafkaProducer.produceTowerPositionData(t2);
    }

    private void simulateTargetMovement() {
        Timer timer = new Timer(1000, e -> {
            if (isPlaying) {
                Random rand = new Random();
                int randomX = rand.nextInt(200) + 200; // Sadece pozitif x ekseninde
                targetX = randomX;
                frame.repaint();
            }
        });
        timer.start();
    }
}
