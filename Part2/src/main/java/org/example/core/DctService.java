package org.example.core;

import org.jtransforms.dct.DoubleDCT_2D;

public class DctService {

    /**
     * Comprime un singolo blocco F x F.
     *
     * @param f La matrice dei pixel del blocco originale
     * @param F La dimensione del blocco
     * @param d La soglia di taglio delle frequenze
     * @return La matrice dei pixel del blocco compresso e normalizzato
     */
    public int[][] compressBlock(double[][] f, int F, int d) {
        // 1. Inizializza la DCT2 per una matrice F x F
        DoubleDCT_2D dct2 = new DoubleDCT_2D(F, F);

        // 2. Calcola c = DCT2(f).
        // Il parametro 'true' applica lo scaling ortonormale.
        dct2.forward(f, true);

        // 3. Taglio delle frequenze: azzera i coefficienti con k + l > d
        for (int k = 0; k < F; k++) {
            for (int l = 0; l < F; l++) {
                if (k + l >= d) {
                    f[k][l] = 0.0;
                }
            }
        }

        // 4. Applica la DCT2 inversa: ff = IDCT2(c)
        dct2.inverse(f, true);

        // 5. Arrotondamento e saturazione per byte (0-255)
        int[][] ff = new int[F][F];
        for (int k = 0; k < F; k++) {
            for (int l = 0; l < F; l++) {
                // Arrotonda all'intero più vicino
                long rounded = Math.round(f[k][l]);

                // Metti a zero i valori negativi e a 255 quelli maggiori di 255
                if (rounded < 0) {
                    ff[k][l] = 0;
                } else if (rounded > 255) {
                    ff[k][l] = 255;
                } else {
                    ff[k][l] = (int) rounded;
                }
            }
        }

        return ff;
    }
}