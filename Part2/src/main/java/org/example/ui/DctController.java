package org.example.ui;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DctController {
    private BufferedImage loadedImage;
    private File loadedFile;

    public DctController() {
        
    }

    protected void attachListeners(
        JButton loadBtn,
        JButton applyBtn,
        JSpinner spinnerF,
        JSpinner spinnerD,
        JLabel outputLabel,
        JFrame frame,
        JLabel imageLabel
    ) {
        loadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("BMP images", "bmp"));
            fileChooser.showOpenDialog(frame);
            try {
                File file = fileChooser.getSelectedFile();
                if (file != null) {
                    loadedFile = file;
                    loadedImage = ImageIO.read(loadedFile);
                    SwingUtilities.invokeLater(() -> {
                        updateImageIcon(imageLabel);
                        applyBtn.setEnabled(true);
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Errore caricamento immagine: " + ex.getMessage());
            }
        });

        applyBtn.addActionListener(e -> {
            if (loadedFile == null) {
                JOptionPane.showMessageDialog(frame, "Carica un'immagine prima di applicare la DCT.");
                return;
            }
            int F = (int) spinnerF.getValue();
            int d = (int) spinnerD.getValue();
            applyBtn.setEnabled(false);
            outputLabel.setText("Elaborazione in corso...");
            new SwingWorker<BufferedImage, Void>() {
                @Override
                protected BufferedImage doInBackground() throws Exception {
                    return new org.example.core.ImageService().processImage(loadedFile, F, d);
                }

                @Override
                protected void done() {
                    try {
                        BufferedImage out = get();
                        outputLabel.setIcon(scaledIcon(out, outputLabel.getWidth(), outputLabel.getHeight()));
                        outputLabel.setText(null);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Errore: " + ex.getMessage());
                    } finally {
                        applyBtn.setEnabled(true);
                    }
                }
            }.execute();
        });
    }

    static ImageIcon scaledIcon(BufferedImage img, int maxW, int maxH) {
        double scale = Math.min((double) maxW / img.getWidth(), (double) maxH / img.getHeight());
        return new ImageIcon(img.getScaledInstance((int)(img.getWidth()*scale), (int)(img.getHeight()*scale), Image.SCALE_SMOOTH));
    }

    // scale currently loaded image to fit the imageLabel's size
    protected void updateImageIcon(JLabel imageLabel) {
        if (loadedImage == null) return;
        int w = imageLabel.getWidth();
        int h = imageLabel.getHeight();
        if (w <= 0 || h <= 0) return;
        imageLabel.setIcon(scaledIcon(loadedImage, w, h));
        imageLabel.setText(null);
    }
}
