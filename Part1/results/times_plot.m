% 1. Leggi il file CSV
opts = detectImportOptions('dct_times.csv');
opts.VariableNamingRule = 'preserve';
data = readtable('dct_times.csv', opts);

% 2. Estrai i dati
matrix_dim       = data{:, 1}; 
custom_dct_time  = data{:, 2}; 
jtransforms_time = data{:, 3}; 

% 3. Calcolo delle curve teoriche O(n^3) e O(n^2 * log(n))
n_last = matrix_dim(end);

% Costante e curva per O(n^3)
c1 = custom_dct_time(end) / (n_last^3);
curve_n3 = c1 .* (matrix_dim.^3);

% Costante e curva per O(n^2 * log(n))
c2 = jtransforms_time(end) / (n_last^2 * log(n_last));
curve_n2logn = c2 .* (matrix_dim.^2 .* log(matrix_dim));

% Assegniamo la figura a una variabile 'fig' per salvarla comodamente dopo
fig = figure; 

semilogy(matrix_dim, custom_dct_time, '-o', 'LineWidth', 1.5, 'DisplayName', 'Custom DCT');
hold on;
semilogy(matrix_dim, jtransforms_time, '-x', 'LineWidth', 1.5, 'DisplayName', 'JTransforms DCT');
semilogy(matrix_dim, curve_n3, '--', 'LineWidth', 1.5, 'Color', [0.4 0.4 0.4], 'DisplayName', 'Andamento O(n^3)');
semilogy(matrix_dim, curve_n2logn, '-.', 'LineWidth', 1.5, 'Color', [0 0 0], 'DisplayName', 'Andamento O(n^2 log n)');
hold off;

% 5. Formattazione
title('Confronto dei Tempi DCT con Complessità Teoriche');
xlabel('Dimensione della Matrice (n)');
ylabel('Tempo (ms)');
legend('Location', 'northwest');
grid on;

% 6. Salvataggio del plot in output
exportgraphics(fig, 'confronto_tempi_dct.png', 'Resolution', 300);

% Formato pdf
% exportgraphics(fig, 'confronto_tempi_dct.pdf', 'ContentType', 'vector');

disp('Plot generato e salvato con successo come "confronto_tempi_dct.png"');