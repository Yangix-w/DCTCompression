package org.example.core;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageService {

    private final DctService dctService;

    public ImageService() {
        this.dctService = new DctService();
    }

    /**
     * Elabora l'immagine originale e restituisce quella compressa.
     *
     * @param imageFile Il file originale
     * @param F         La dimensione dei macro-blocchi
     * @param d         La soglia di taglio
     * @return L'immagine BufferedImage processata
     * @throws IOException Se ci sono problemi nella lettura del file
     */
    public BufferedImage processImage(File imageFile, int F, int d) throws IOException {
        // Leggi l'immagine
        BufferedImage originalImg = ImageIO.read(imageFile);

        int origWidth = originalImg.getWidth();
        int origHeight = originalImg.getHeight();

        // Calcola le nuove dimensioni scartando gli avanzi (multipli esatti di F)
        int newWidth = (origWidth / F) * F;
        int newHeight = (origHeight / F) * F;

        // Crea una nuova immagine vuota per i risultati in toni di grigio
        BufferedImage resultImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        // Scorri l'immagine a blocchi di F x F
        for (int y = 0; y < newHeight; y += F) {
            for (int x = 0; x < newWidth; x += F) {

                // 1. Estrai il blocco
                double[][] block = new double[F][F];
                for (int i = 0; i < F; i++) {
                    for (int j = 0; j < F; j++) {
                        // Estrai il valore del pixel.
                        // Usiamo Color per prendere il canale Red (essendo scala di grigi R=G=B)
                        Color color = new Color(originalImg.getRGB(x + j, y + i));
                        block[i][j] = color.getRed();
                    }
                }

                // 2. Comprimi il blocco usando il DctService
                int[][] processedBlock = dctService.compressBlock(block, F, d);

                // 3. Ricomponi il blocco nell'immagine finale
                for (int i = 0; i < F; i++) {
                    for (int j = 0; j < F; j++) {
                        int grayValue = processedBlock[i][j];

                        // Ricrea il colore RGB a partire dal singolo valore di grigio
                        // Il formato è Alpha-Red-Green-Blue
                        int rgb = new Color(grayValue, grayValue, grayValue).getRGB();
                        resultImg.setRGB(x + j, y + i, rgb);
                    }
                }
            }
        }

        return resultImg;
    }
}