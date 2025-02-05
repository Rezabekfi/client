package com.quoridor.UI.Windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A simple popup window that displays a message and an OK button. 
 */
public class PopupWindow extends JDialog {
    
    // Constructor (not used)
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
    
    // Static method to show a message in a new popup window
    // This method is used significantly more than the constructor
    public static void showMessage(String message) {
        new Thread(() -> {
            // Ensure we're on EDT for Swing operations
            SwingUtilities.invokeLater(() -> {
                JOptionPane pane = new JOptionPane(
                    message,
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                JDialog dialog = pane.createDialog("Message");
                dialog.setAlwaysOnTop(true);  // Make sure it's visible
                dialog.setModal(true);  // We can keep it modal since it's in its own thread
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);  // This will block until dialog is closed
            });
        }).start();
    }
} 