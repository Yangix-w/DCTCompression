package org.example.core;

import org.jtransforms.dct.DoubleDCT_1D;
import org.jtransforms.dct.DoubleDCT_2D;

import java.util.Arrays;

public class DctTest {

    public static void main(String[] args) {
        // --- TEST 1: Blocco 8x8 (DCT2) ---
        System.out.println("=== TEST DCT2 (Blocco 8x8) ===");

        // Inizializzo la matrice 8x8 con i valori forniti a pagina 3
        double[][] block8x8 = {
                {231,  32, 233, 161,  24,  71, 140, 245},
                {247,  40, 248, 245, 124, 204,  36, 107},
                {234, 202, 245, 167,   9, 217, 239, 173},
                {193, 190, 100, 167,  43, 180,   8,  70},
                { 11,  24, 210, 177,  81, 243,   8, 112},
                { 97, 195, 203,  47, 125, 114, 165, 181},
                {193,  70, 174, 167,  41,  30, 127, 245},
                { 87, 149,  57, 192,  65, 129, 178, 228}
        };

        // Istanzio e calcolo la DCT2 usando lo scaling (parametro true)
        DoubleDCT_2D dct2 = new DoubleDCT_2D(8, 8);
        dct2.forward(block8x8, true);

        // Stampo i risultati in notazione scientifica (es. 1.11e+03)
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.printf("%9.2e  ", block8x8[i][j]);
            }
            System.out.println();
        }

        System.out.println();
        System.out.println(Arrays.deepToString(block8x8));

        System.out.println("\n--------------------------------------------------\n");

        // --- TEST 2: Prima riga (DCT monodimensionale) ---
        System.out.println("=== TEST DCT1 (Prima Riga) ===");

        // Inizializzo l'array 1D con la prima riga del blocco
        double[] row = {231, 32, 233, 161, 24, 71, 140, 245};

        // Istanzio e calcolo la DCT 1D usando lo scaling (parametro true)
        DoubleDCT_1D dct1 = new DoubleDCT_1D(8);
        dct1.forward(row, true);

        // Stampo i risultati della prima riga
        for (int i = 0; i < 8; i++) {
            System.out.printf("%9.2e  ", row[i]);
        }
        System.out.println();
    }
}