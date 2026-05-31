package org.example.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class DctApp {

    private final Color BG_COLOR = new Color(45, 45, 45);
    private final Color FG_COLOR = new Color(240, 240, 240);

    private JFrame frame;
    private JLabel imageLabel;
    private JLabel outputLabel;

    // Controls
    private JSpinner spinnerF;
    private JSpinner spinnerD;
    private SpinnerNumberModel modelD;
    private JButton applyBtn;
    private JButton loadBtn;
    private JButton saveBtn;

    DctController controller = new DctController();

    public DctApp() {
        createUI();
        controller.attachListeners(loadBtn, applyBtn, saveBtn, spinnerF, spinnerD, outputLabel, frame, imageLabel);
    }

    private void createUI() {
        frame = new JFrame("DCT2 Image Compressor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        Font myFont = new Font("Arial", Font.PLAIN, 14);
        Font myFontBig = new Font("Arial", Font.PLAIN, 24);
        UIManager.put("Label.font", myFont);
        UIManager.put("Button.font", myFont);
        UIManager.put("Spinner.font", myFont);
        UIManager.put("Border.font", myFont);

        imageLabel = new JLabel("Nessuna immagine caricata", SwingConstants.CENTER);
        outputLabel = new JLabel("Output", SwingConstants.CENTER);
        imageLabel.setFont(myFontBig);
        outputLabel.setFont(myFontBig);

        // params panel
        JPanel paramsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        Font titleFont = myFont.deriveFont(Font.BOLD, 14f);
        paramsPanel.setBackground(BG_COLOR);
        paramsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2), "Parametri", 0, 0, titleFont, FG_COLOR));

        loadBtn = new JButton("Carica BMP");
        loadBtn.setFocusPainted(false);
        loadBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        spinnerF = new JSpinner(new SpinnerNumberModel(8, 1, 64, 1));
        ((JSpinner.DefaultEditor) spinnerF.getEditor()).getTextField().setColumns(3);
        spinnerF.setPreferredSize(new Dimension(150, 30));

        modelD = new SpinnerNumberModel(8, 0, 14, 1);
        spinnerD = new JSpinner(modelD);
        ((JSpinner.DefaultEditor) spinnerD.getEditor()).getTextField().setColumns(3);
        spinnerD.setPreferredSize(new Dimension(150, 30));

        JLabel rangeLabel = new JLabel("(range d: [0, 14])");
        applyBtn = new JButton("Applica DCT2");
        applyBtn.setEnabled(false);
        applyBtn.setFocusPainted(false);
        applyBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        saveBtn = new JButton("Salva BMP");
        saveBtn.setEnabled(false);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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
        paramsPanel.add(saveBtn);

        // images panel
        imageLabel.setPreferredSize(new Dimension(400, 400));
        outputLabel.setPreferredSize(new Dimension(400, 400));

        imageLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                controller.updateImageIcon(imageLabel);
            }
        });

        outputLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                controller.updateOutputIcon(outputLabel);
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, imageLabel, outputLabel);
        splitPane.setResizeWeight(0.5);
        splitPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
        splitPane.setDividerSize(4);
        splitPane.setBackground(BG_COLOR);

        frame.setLayout(new BorderLayout());
        frame.add(paramsPanel, BorderLayout.NORTH);
        frame.add(splitPane, BorderLayout.CENTER);
    }

    public void show() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new DctApp().show());
    }
}