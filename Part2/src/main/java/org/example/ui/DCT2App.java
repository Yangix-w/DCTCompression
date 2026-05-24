package org.example.ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class DCT2App {

    static JLabel imageLabel = new JLabel("Nessuna immagine caricata", SwingConstants.CENTER);
    static JLabel outputLabel = new JLabel("Output", SwingConstants.CENTER);
    static BufferedImage loadedImage = null;
    static final Color BG_COLOR = new Color(45, 45, 45);
    static final Color FG_COLOR = new Color(240, 240, 240);

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        JFrame frame = new JFrame("DCT2 Image Compressor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        Font myFont = new Font("Arial", Font.PLAIN, 14);
        Font myFontBig = new Font("Arial", Font.PLAIN, 24);
        UIManager.put("Label.font", myFont);
        UIManager.put("Button.font", myFont);
        UIManager.put("Spinner.font", myFont);
        UIManager.put("Border.font", myFont);
        imageLabel.setFont(myFontBig);
        outputLabel.setFont(myFontBig);

        JPanel paramsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        
        Font titleFont = myFont.deriveFont(Font.BOLD, 14f);
        paramsPanel.setBackground(BG_COLOR);
        paramsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2), "Parametri", 0, 0, titleFont, FG_COLOR));

        JButton loadBtn = new JButton("Carica BMP");
        loadBtn.setFocusPainted(false);
        loadBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        JSpinner spinnerF = new JSpinner(new SpinnerNumberModel(8, 1, 64, 1));
        ((JSpinner.DefaultEditor) spinnerF.getEditor()).getTextField().setColumns(3);
        spinnerF.setPreferredSize(new Dimension(200, 30));
        spinnerF.setMaximumSize(new Dimension(200, 30));
    
        
        SpinnerNumberModel modelD = new SpinnerNumberModel(8, 0, 14, 1);
        JSpinner spinnerD = new JSpinner(modelD);
        ((JSpinner.DefaultEditor) spinnerD.getEditor()).getTextField().setColumns(3);
        spinnerD.setPreferredSize(new Dimension(200, 30));
        spinnerD.setMaximumSize(new Dimension(200, 30));
        
        JLabel rangeLabel = new JLabel("(range d: [0, 14])");
        JButton applyBtn = new JButton("Applica DCT2");
        applyBtn.setEnabled(false);
        applyBtn.setFocusPainted(false);
        applyBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Logic
        spinnerF.addChangeListener(e -> {
            int F = (int) spinnerF.getValue();
            int dMax = 2 * F - 2;
            modelD.setMaximum(dMax);
            rangeLabel.setText("(range d: [0, " + dMax + "])");
        });
        JLabel labelF = new JLabel("  F:");
        JLabel labelD = new JLabel("  d:");

        rangeLabel.setForeground(FG_COLOR);
        labelF.setForeground(FG_COLOR);
        labelD.setForeground(FG_COLOR);
        imageLabel.setForeground(FG_COLOR);
        outputLabel.setForeground(FG_COLOR);

        paramsPanel.add(loadBtn);
        paramsPanel.add(labelF); paramsPanel.add(spinnerF);
        paramsPanel.add(labelD); paramsPanel.add(spinnerD);
        paramsPanel.add(rangeLabel);
        paramsPanel.add(applyBtn);

        // images panel
        imageLabel.setPreferredSize(new Dimension(400, 400));
        outputLabel.setPreferredSize(new Dimension(400, 400));

        // update icon when the label is resized so image fills available space
        imageLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateImageIcon();
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, imageLabel, outputLabel);
        splitPane.setResizeWeight(0.5);
        splitPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
        splitPane.setDividerSize(4);
        splitPane.setBackground(BG_COLOR);

        // main gui
        frame.setLayout(new BorderLayout());
        frame.add(paramsPanel, BorderLayout.NORTH);
        frame.add(splitPane, BorderLayout.CENTER);

        // load image
        loadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(frame);
            try {
                File file = fileChooser.getSelectedFile();
                if (file != null) {
                    loadedImage = ImageIO.read(file);
                    SwingUtilities.invokeLater(() -> {
                        updateImageIcon();
                        applyBtn.setEnabled(true);
                    });
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });


        applyBtn.addActionListener(e -> { /* ... logica DCT ... */ });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static ImageIcon scaledIcon(BufferedImage img, int maxW, int maxH) {
        double scale = Math.min((double) maxW / img.getWidth(), (double) maxH / img.getHeight());
        return new ImageIcon(img.getScaledInstance((int)(img.getWidth()*scale), (int)(img.getHeight()*scale), Image.SCALE_SMOOTH));
    }

    // scale currently loaded image to fit the imageLabel's size
    static void updateImageIcon() {
        if (loadedImage == null) return;
        int w = imageLabel.getWidth();
        int h = imageLabel.getHeight();
        if (w <= 0 || h <= 0) return;
        imageLabel.setIcon(scaledIcon(loadedImage, w, h));
        imageLabel.setText(null);
    }
}