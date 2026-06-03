package org.example.custom_dct;

import org.ejml.simple.SimpleMatrix;

public class DCT {

    /**
     * Calcola la matrice D utilizzata per la DCT. La matrice D è una matrice di dimensione n x n che contiene i coefficienti di coseno necessari per calcolare la DCT.
     * La matrice D viene calcolata utilizzando la formula:
     * D[k][j] = cos(pi * k * (2 * j + 1) / (2 * n)) * alpha
     * dove alpha è una costante che dipende da k e n. Se k è 0, alpha è 1 / sqrt(n), altrimenti alpha è sqrt(2 / n).
     * 
     * @param n Dimensione della matrice D da calcolare
     * @return Matrice D calcolata
     */
    public SimpleMatrix computeD(int n){
        SimpleMatrix d = new SimpleMatrix(n,n);
        double alpha;
        for(int k = 0; k < n; k++){
            if(k == 0){
                alpha = 1 / Math.sqrt(n);
            } else {
                alpha = Math.sqrt((double) 2 / n);
            }
            for(int j = 0; j < n; j++){
                d.set(k, j, Math.cos(Math.PI * k * (2 * j + 1) / (2 * n)) * alpha);
            }

        }
        return d;
    }

    /**
     * Calcola la DCT di una matrice utilizzando la matrice D calcolata con il metodo computeD. 
     * 
     * @param matrix Matrice di input per la quale calcolare la DCT
     * @param n Numero di righe della matrice di input
     * @param m Numero di colonne della matrice di input
     * @param dn Matrice D calcolata per le righe della matrice di input
     * @param dm Matrice D calcolata per le colonne della matrice di input
     * @return Matrice DCT calcolata
     */
    private SimpleMatrix computeDct(SimpleMatrix matrix, int n, int m, SimpleMatrix dn, SimpleMatrix dm) {
        for(int j = 0; j < m; j++){
            // moltiplicate dn to the column j of c and store the result in the column j of c
            matrix.setColumn(j, 0, dn.mult(matrix.extractVector(false, j)).getDDRM().getData());
        }
        for(int l = 0; l < n; l++){
            // moltiplicate dm to the row l of c and store the result in the row l of c
            matrix.setRow(l, 0, dm.mult(matrix.extractVector(true, l).transpose()).getDDRM().getData());
        }
        return matrix;
    }

    /**
     * Calcola la DCT bidimensionale di una matrice.
     * 
     * @param f Matrice di input per la quale calcolare la DCT
     * @return Matrice DCT calcolata
     */
    public SimpleMatrix dct2(SimpleMatrix f){
        SimpleMatrix c = f.copy();
        int n = f.getNumRows();
        int m = f.getNumCols();
        SimpleMatrix dn = computeD(n);
        SimpleMatrix dm = computeD(m);

        return computeDct(c, n, m, dn, dm);
    }

    /**
     * Calcola la DCT inversa di una matrice. La DCT inversa viene calcolata utilizzando la matrice D calcolata con il metodo computeD, ma trasposta.
     * 
     * @param c Matrice di input per la quale calcolare la DCT inversa
     * @return Matrice DCT inversa calcolata
     */
    public SimpleMatrix idct2(SimpleMatrix c){
        SimpleMatrix f = c.copy();
        int n = c.getNumRows();
        int m = c.getNumCols();
        SimpleMatrix dn = computeD(n).transpose();
        SimpleMatrix dm = computeD(m).transpose();

        return computeDct(f, n, m, dn, dm);
    }
}
