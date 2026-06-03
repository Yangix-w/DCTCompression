package org.example.ui;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DctController {
    private BufferedImage loadedImage;
    private File loadedFile;
    private BufferedImage outputImage;

    public DctController() {
        
    }

    protected void attachListeners(
        JButton loadBtn,
        JButton applyBtn,
        JButton saveBtn,
        JSpinner spinnerF,
        JSpinner spinnerD,
        JLabel outputLabel,
        JFrame frame,
        JLabel imageLabel
    ) {
        loadBtn.addActionListener(e -> {

            JFileChooser fileChooser = new JFileChooser("./Part2/images");
            fileChooser.setFileFilter(new FileNameExtensionFilter("BMP images", "bmp"));
            fileChooser.showOpenDialog(frame);
            try {
                File file = fileChooser.getSelectedFile();
                if (file != null) {
                    if (loadedImage != null) {
                        loadedImage = null;
                        loadedFile = null;
                        outputImage = null;
                        imageLabel.setIcon(null);
                        imageLabel.setText("Nessuna immagine caricata");
                        outputLabel.setIcon(null);
                        outputLabel.setText("Output");
                        applyBtn.setEnabled(false);
                    }
                    loadedFile = file;
                    loadedImage = ImageIO.read(loadedFile);
                    SwingUtilities.invokeLater(() -> {
                        updateImageIcon(imageLabel);
                        applyBtn.setEnabled(true);
                    });
                }

                // set max F to smallest side of loaded image
                int maxF = Math.min(loadedImage.getWidth(), loadedImage.getHeight());
                spinnerF.setModel(new SpinnerNumberModel(8, 1, maxF, 1));
                
            } catch (Exception ex) {
                ex.printStackTrace();
                //JOptionPane.showMessageDialog(frame, "Errore caricamento immagine: " + ex.getMessage());
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
                        outputImage = out;
                        outputLabel.setIcon(scaledIcon(out, outputLabel.getWidth(), outputLabel.getHeight()));
                        outputLabel.setText(null);

                        saveBtn.setEnabled(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Errore: " + ex.getMessage());
                    } finally {
                        applyBtn.setEnabled(true);
                    }
                }
            }.execute();
        });

        saveBtn.addActionListener(e -> {
            if (outputImage == null) {
                JOptionPane.showMessageDialog(frame, "Nessun output disponibile da salvare. Applica la DCT prima.");
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("BMP images", "bmp"));
            // propose a default filename based on the loaded file
            String defaultName = "output_compressed.bmp";
            try {
                if (loadedFile != null) {
                    String name = loadedFile.getName();
                    int dot = name.lastIndexOf('.');
                    if (dot > 0) name = name.substring(0, dot);
                    defaultName = name + "_compressed.bmp";
                }
            } catch (Exception ignored) {}
            fileChooser.setSelectedFile(new File(fileChooser.getCurrentDirectory(), defaultName));
            int res = fileChooser.showSaveDialog(frame);
            if (res != JFileChooser.APPROVE_OPTION) return;
            try {
                File file = fileChooser.getSelectedFile();
                if (file == null) return;
                String path = file.getAbsolutePath();
                if (!path.toLowerCase().endsWith(".bmp")) {
                    file = new File(path + ".bmp");
                }
                if (file.exists()) {
                    int ok = JOptionPane.showConfirmDialog(frame, "File esistente. Sovrascrivere?", "Conferma sovrascrittura", JOptionPane.YES_NO_OPTION);
                    if (ok != JOptionPane.YES_OPTION) return;
                }
                ImageIO.write(outputImage, "bmp", file);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Errore salvataggio immagine: " + ex.getMessage());
            }
        });
    }

    static ImageIcon scaledIcon(BufferedImage img, int maxW, int maxH) {
        double scale = Math.min((double) maxW / img.getWidth(), (double) maxH / img.getHeight());
        int targetW = (int) (img.getWidth() * scale);
        int targetH = (int) (img.getHeight() * scale);

        // Creiamo una nuova immagine forzando il color model standard RGB
        // Questo risolve i problemi di gamma/luminosità con i file BMP in scala di grigi
        BufferedImage resizedImg = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();

        // Impostiamo l'alta qualità per l'interpolazione (sostituisce Image.SCALE_SMOOTH)
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Disegniamo l'immagine originale ridimensionata sul nuovo buffer
        g2.drawImage(img, 0, 0, targetW, targetH, null);
        g2.dispose();

        return new ImageIcon(resizedImg);
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

    protected void updateOutputIcon(JLabel outputLabel) {
        if (outputImage == null) return;
        int w = outputLabel.getWidth(), h = outputLabel.getHeight();
        if (w <= 0 || h <= 0) return;
        outputLabel.setIcon(scaledIcon(outputImage, w, h));
        outputLabel.setText(null);
    }
}
