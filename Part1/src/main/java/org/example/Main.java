package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.ejml.simple.SimpleMatrix;
import org.example.custom_dct.DCT;
import org.jtransforms.dct.DoubleDCT_2D;

public class Main {
    /**
     * Costante per convertire il tempo da nanosecondi a millisecondi
     * 1 millisecondo = 1.000.000 nanosecondi
     */
    private static final int CONVERT_IN_MILLI = 1_000_000;

    /**
     * Punto di ingresso dell'applicazione. Esegue il benchmark della DCT.
     * @param args
     */
    public static void main(String[] args) {
        //test();
        benchmarkDCT();
    }

    /**
     * Esegue il benchmark della DCT personalizzata e della DCT di JTransforms su matrici di dimensioni crescenti.
     * I tempi di esecuzione vengono salvati in un file CSV nella cartella "results".
     * La DCT personalizzata viene misurata utilizzando la classe DCT, mentre la DCT di JTransforms viene misurata utilizzando la classe DoubleDCT_2D.
     * 
     */
    public static void benchmarkDCT(){
        int n = 1000; //Limite massima delle matrici
        double[][] matrix;
        SimpleMatrix sm;
        ArrayList<Double> customDctTime = new ArrayList<>();
        ArrayList<Double> fftDctTime = new ArrayList<>();
        long startTime;
        long endTime;
        DCT customDct = new DCT();
        DoubleDCT_2D fftDct;
        for(int i = 10; i <= n; i+=10) {
            System.out.println("Matrix dimension: " + i);
            matrix = generateMatrix(i);
            sm = new SimpleMatrix(matrix);
            //Misura del tempo per la DCT personalizzata
            startTime = System.nanoTime();
            customDct.dct2(sm);
            endTime = System.nanoTime();
            customDctTime.add((double)(endTime - startTime)/CONVERT_IN_MILLI); // Converti in millisecondi
            //Misura del tempo per la DCT di JTransforms
            fftDct = new DoubleDCT_2D(i, i);
            startTime = System.nanoTime();
            fftDct.forward(matrix, true);
            endTime = System.nanoTime();
            fftDctTime.add((double)(endTime - startTime)/CONVERT_IN_MILLI); // Converti in millisecondi
        }
        // Salva i dati in CSV nella cartella results
        saveToCsv("./Part1/results/dct_times.csv", customDctTime, fftDctTime);
    }

    /**
     * Genera una matrice quadrata di dimensione n x n con valori casuali compresi tra 0 e 100.
     * 
     * @param n Dimensione della matrice
     * @return Matrice generata
     */
    public static double[][] generateMatrix(int n){
        double[][] matrix = new double[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                matrix[i][j] = Math.random()*100;
            }
        }
        return matrix;
    }
    
    /**
     * Salva i tempi di esecuzione della DCT personalizzata e della DCT di JTransforms in un file CSV.
     * Il file CSV conterrà le dimensioni delle matrici e i tempi di esecuzione corrispondenti per entrambe le implementazioni.
     * 
     * @param filename Nome del file CSV da salvare
     * @param customDctTime Lista dei tempi di esecuzione della DCT personalizzata in millisecondi
     * @param fftDctTime Lista dei tempi di esecuzione della DCT di JTransforms in millisecondi
     */
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

    /**
     * Metodo di test per verificare la correttezza della DCT personalizzata e la correttezza della DCT di JTransforms.
     * Viene dato una matrice 8x8 con valori predefiniti, viene calcolata la DCT utilizzando entrambe le implementazioni e vengono stampati i risultati.
     * 
     */
    public static void test(){
        SimpleMatrix m = new SimpleMatrix(new double[][]{
                {231, 32, 233, 161, 24, 71, 140, 245},
                {247, 40, 248, 245, 124, 204, 36, 107},
                {234, 202, 245, 167, 9, 217, 239, 173},
                {193, 190, 100, 167, 43, 180, 8, 70},
                {11, 24, 210, 177, 81, 243, 8, 112},
                {97, 195, 203, 47, 125, 114, 165, 181},
                {193, 70, 174, 167, 41, 30, 127, 245},
                {87, 149, 57, 192, 65, 129, 178, 228}
        });
        DCT dct = new DCT();
        SimpleMatrix c = dct.dct2(m);
        System.out.println("DCT:");
        c.print();
        SimpleMatrix f = dct.idct2(c);
        System.out.println("IDCT:");
        f.print();

        double[][] a = c.toArray2();
        DoubleDCT_2D fftDct = new DoubleDCT_2D(8, 8);
        fftDct.forward(a, true);
        System.out.println("DCT JTransforms:");
        for (double[] doubles : a) {
            for (double aDouble : doubles) {
                System.out.print(aDouble + " ");
            }
        }
    }

}
