# Progetto 2 - Compressione di immagini tramite la DCT2

### Membri: Hicham Benbouzid, Le Yang Shi, Elena Zen

---

## Introduzione

Questo repository contiene il codice sorgente e le risorse relative al **Progetto 2** del corso di Metodi del Calcolo Scientifico. L'obiettivo principale del lavoro è l'implementazione della Trasformata Discreta del Coseno bidimensionale (DCT-2) per studiare gli effetti di un algoritmo di compressione in stile JPEG (senza l'uso di matrici di quantizzazione) su immagini in toni di grigio.

Il progetto, sviluppato e testato in ambiente Windows avvalendosi del linguaggio Java, si articola in due macro-sezioni:

* **Benchmarking Prestazionale (DCT-2 vs FFT):** Implementazione "from scratch" della trasformata DCT-2 e confronto dei tempi di esecuzione con un algoritmo ottimizzato basato su FFT fornito dalla libreria standard. L'analisi punta a verificare gli andamenti di complessità temporale teorici previsti al crescere delle dimensioni dell'array: $O(N^{3})$ per l'algoritmo custom e $O(N^{2} \log N)$ per la versione ottimizzata.
* **Software di Compressione con GUI:** Sviluppo di un'applicazione dotata di interfaccia grafica che permette di caricare un'immagine `.bmp` in scala di grigi e di applicare la compressione. L'immagine viene suddivisa in blocchi $F \times F$ sui quali viene applicata la DCT-2. L'utente ha la libertà di impostare l'ampiezza dei blocchi $F$ e la soglia di taglio delle frequenze $d$, filtrando i coefficienti $c_{kl}$ tali per cui $k+l>d$. Al termine del processo tramite IDCT-2 inversa, il software affianca l'immagine originale e la sua versione compressa per un rapido confronto visivo.

## Parte 1: Benchmarking DCT2 vs FFT

Questa fase del progetto confronta le prestazioni di due diverse implementazioni della trasformata DCT-2:
* **DCT-2 Custom:** Un'implementazione "fatta in casa" basata sulla definizione matematica diretta.
* **Versione Fast (FFT):** Un'implementazione ottimizzata che sfrutta la libreria open-source **JTransforms**.

Il test valuta i tempi di esecuzione applicando gli algoritmi su array quadrati di dimensione crescente $N \times N$. L'obiettivo è tracciare un grafico in scala semilogaritmica per l'asse delle ordinate e verificare la complessità computazionale teorica: tempi di calcolo proporzionali a $N^3$ per l'algoritmo custom e a $N^2 \log(N)$ per l'algoritmo veloce di JTransforms.

### Run del codice
```powershell
# Nella cartella ./Part1
mvn clean package -DskipTests

java -jar target\Part1-1.0-SNAPSHOT.jar
```
I risultati dei tempi di esecuzione verranno salvati in un file `.csv` all'interno della directory `./Part1/results`. Per generare e visualizzare il grafico finale in scala semilogaritmica, è sufficiente eseguire lo script **MATLAB** presente all'interno della medesima cartella.