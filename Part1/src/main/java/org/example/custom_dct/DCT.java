package org.example.custom_dct;

import org.ejml.simple.SimpleMatrix;

public class DCT {

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

    public SimpleMatrix dct2(SimpleMatrix f){
        SimpleMatrix c = f.copy();
        int n = f.getNumRows();
        int m = f.getNumCols();
        SimpleMatrix dn = computeD(n);
        SimpleMatrix dm = computeD(m);

        return computeDct(c, n, m, dn, dm);
    }

    public SimpleMatrix idct2(SimpleMatrix c){
        SimpleMatrix f = c.copy();
        int n = c.getNumRows();
        int m = c.getNumCols();
        SimpleMatrix dn = computeD(n).transpose();
        SimpleMatrix dm = computeD(m).transpose();

        return computeDct(f, n, m, dn, dm);
    }
}
