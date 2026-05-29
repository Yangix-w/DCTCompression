package org.example;

import org.ejml.simple.SimpleMatrix;
import org.example.custom_dct.DCT;

public class Main {
    public static void main(String[] args) {
        DCT dct = new DCT();
        SimpleMatrix f = new SimpleMatrix(new double[][]{
                {231, 32, 233, 161, 24, 71, 140, 245},
                {247, 40, 248, 245, 124, 204, 36, 107},
                {234, 202, 245, 167, 9, 217, 239, 173},
                {193, 190, 100, 167, 43, 180, 8, 70},
                {11, 24, 210, 177, 81, 243, 8, 112},
                {97, 195, 203, 47, 125, 114, 165, 181},
                {193, 70, 174, 167, 41, 30, 127, 245},
                {87, 149, 57, 192, 65, 129, 178, 228}
        });
        SimpleMatrix c = dct.dct2(f);
        System.out.println("DCT of f:");
        c.print();
        SimpleMatrix f_reconstructed = dct.idct2(c);
        System.out.println("Reconstructed f:");
        f_reconstructed.print();
    }
}
