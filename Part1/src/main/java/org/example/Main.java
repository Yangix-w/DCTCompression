package org.example;

import org.ejml.simple.SimpleMatrix;
import org.example.custom_dct.DCT;

public class Main {
    static void main() {
        DCT dct = new DCT();
        SimpleMatrix f = new SimpleMatrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });
        SimpleMatrix c = dct.dct2(f);
        System.out.println("DCT of f:");
        c.print();
        SimpleMatrix f_reconstructed = dct.idct2(c);
        System.out.println("Reconstructed f:");
        f_reconstructed.print();
    }
}
