package com.quoridor.UI.Windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PopupWindow extends JDialog {
    
    public PopupWindow(String message, String title, int width, int height) {
        setTitle(title);
        setSize(width, height);
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add message label
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        mainPanel.add(messageLabel, BorderLayout.CENTER);
        
        // Add OK button
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(okButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    public static void showMessage(String message) {
        // Create a temporary label to calculate required width
        JLabel dummyLabel = new JLabel(message);
        Dimension preferredSize = dummyLabel.getPreferredSize();
        
        // Add padding and calculate dimensions
        int width = Math.max(300, preferredSize.width + 50);  // minimum 300px, or message width + padding
        int height = Math.max(150, preferredSize.height + 100);  // minimum 150px, or message height + padding
        
        PopupWindow popup = new PopupWindow(message, "Message", width, height);
        popup.setVisible(true);
    }
} 