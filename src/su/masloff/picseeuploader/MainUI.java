/*
 * Copyright (c) 2018 Kirill Maslov
 * All Rights Reserved
 */

package su.masloff.picseeuploader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainUI extends JFrame {
    private JLabel uploadURLLabel;
    private JTextField uploadURLTextField;
    private JButton uploadButton;
    private JFrame dialogFrame;
    private JLabel dialogURLLabel;
    private JTextField dialogUploadedURLTextField;
    private JButton dialogButton;

    public MainUI() {
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Picsee Uploader");

        JPanel panel = new JPanel();

        // upload url
        uploadURLLabel = new JLabel("URL:");
        panel.add(uploadURLLabel);

        uploadURLTextField = new JTextField(15);
        panel.add(uploadURLTextField);

        // upload button
        uploadButton = new JButton("Upload");
        ListenForButton listenForButton = new ListenForButton();
        uploadButton.addActionListener(listenForButton);
        panel.add(uploadButton);

        this.add(panel);

        this.setVisible(true);
    }

    private String uploadFile(String fileToUploadURL) {
        PicseeUploader picseeUploader = PicseeUploader.getInstance();

        String uploadedFileURL = picseeUploader.uploadFileByURL(fileToUploadURL);

        return uploadedFileURL;
    }

    private void showUploadedFileURL(String uploadedFileURL) {
        dialogFrame = new JFrame();
        dialogFrame.setSize(400, 100);
        dialogFrame.setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialogFrame.setTitle("Success");

        JPanel panel = new JPanel();
        dialogURLLabel = new JLabel("URL:");
        panel.add(dialogURLLabel);
        dialogUploadedURLTextField = new JTextField(15);
        dialogUploadedURLTextField.setEditable(false);
        dialogUploadedURLTextField.setText(uploadedFileURL);
        dialogUploadedURLTextField.selectAll();
        panel.add(dialogUploadedURLTextField);

        dialogButton = new JButton("OK");
        panel.add(dialogButton);

        ListenForButton listenForButton = new ListenForButton();
        dialogButton.addActionListener(listenForButton);

        dialogFrame.add(panel);

        dialogFrame.setVisible(true);
    }

    private void showErrorMessage() {
        JOptionPane.showMessageDialog(
                this,
                "Couldn't upload file",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private class ListenForButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == uploadButton) {
                String fileToUploadURL = uploadURLTextField.getText();

                String uploadedFileURL = uploadFile(fileToUploadURL);

                if (uploadedFileURL.isEmpty()) {
                    showErrorMessage();
                } else {
                    showUploadedFileURL(uploadedFileURL);
                }
            } else if (e.getSource() == dialogButton) {
                dialogFrame.dispose();
            }
        }
    }
}
