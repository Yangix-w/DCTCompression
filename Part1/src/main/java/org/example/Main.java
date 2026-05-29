package org.example;

import org.ejml.simple.SimpleMatrix;
import org.example.custom_dct.DCT;
import org.jtransforms.dct.DoubleDCT_2D;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        int n = 1000; //Grandezza massima delle matrici
        double[][] matrix;
        SimpleMatrix sm;
        ArrayList<Double> customDctTime = new ArrayList<>();
        ArrayList<Double> fftDctTime = new ArrayList<>();
        long startTime;
        long endTime;
        DCT customDct = new DCT();
        DoubleDCT_2D fftDct;
        for(int i = 10; i < n; i+=10) {
            System.out.println("Matrix dimension: " + i);
            matrix = generateMatrix(i);
            sm = new SimpleMatrix(matrix);
            //Misura del tempo per la DCT personalizzata
            startTime = System.nanoTime();
            customDct.dct2(sm);
            endTime = System.nanoTime();
            customDctTime.add((double)(endTime - startTime)/1000000); // Converti in millisecondi
            //Misura del tempo per la DCT di JTransforms
            fftDct = new DoubleDCT_2D(i, i);
            startTime = System.nanoTime();
            fftDct.forward(matrix, true);
            endTime = System.nanoTime();
            fftDctTime.add((double)(endTime - startTime)/1000000); // Converti in millisecondi
         }

         // Salva i dati in CSV nella cartella results
         saveToCsv("results/dct_times.csv", customDctTime, fftDctTime);
     }

    public static double[][] generateMatrix(int n){
         double[][] matrix = new double[n][n];
         for(int i = 0; i < n; i++){
             for(int j = 0; j < n; j++){
                 matrix[i][j] = Math.random()*100;
             }
         }
         return matrix;
     }

      public static void saveToCsv(String filename, ArrayList<Double> customDctTime, ArrayList<Double> fftDctTime) {
          try {
              // Crea la cartella "results" se non esiste
              String dirPath = "results";
              Files.createDirectories(Paths.get(dirPath));

              // Scrivi il file CSV
              try (FileWriter writer = new FileWriter(filename)) {
                  // Scrivi intestazioni
                  writer.append("Matrix Dimension,Custom DCT Time (ms),JTransforms DCT Time (ms)\n");

                  // Scrivi i dati
                  for (int i = 0; i < customDctTime.size(); i++) {
                      int dimension = 10 + (i * 10);
                      writer.append(String.valueOf(dimension)).append(",").append(String.valueOf(customDctTime.get(i))).append(",").append(String.valueOf(fftDctTime.get(i))).append("\n");
                  }

                  System.out.println("File CSV salvato: " + filename);
              }
          } catch (IOException e) {
              System.err.println("Errore durante la scrittura del file CSV: " + e.getMessage());
              e.printStackTrace();
          }
      }
}
