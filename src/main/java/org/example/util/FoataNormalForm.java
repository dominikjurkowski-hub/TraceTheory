package org.example.util;

import org.example.model.Pair;
import org.example.model.Transaction;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FoataNormalForm {
    private final String word;
    private final Dependencies dependencies;

    public FoataNormalForm(String word, Dependencies dependencies) {
        this.word = word;
        this.dependencies = dependencies;
    }

    // Tworzy FNF
    public List<Set<Character>> compute() {
        List<Set<Character>> levels = new ArrayList<>();

        // Dla każdej litery w słowie sprawdza do której warstwy należy
        for (char c : word.toCharArray()) {
            boolean placed = false;

            // sprawdzamy czy c jest zależne z jakąs inna literą w warstwie
            for (Set<Character> level : levels) {
                boolean conflict = false;
                for (char x : level) {
                    if (areDependent(c, x)) {
                        conflict = true;
                        break;
                    }
                }
                // jeśli nie jest zależna to dodajemy ją do warstwy
                if (!conflict) {
                    level.add(c);
                    placed = true;
                    break;
                }
            }

            // jeśli nie znaleźliśmy warstwy dla c to tworzymy nową i wstawiamy tam c
            if (!placed) {
                Set<Character> newLevel = new LinkedHashSet<>();
                newLevel.add(c);
                levels.add(newLevel);
            }
        }

        return levels;
    }

    private boolean areDependent(char a, char b) {
        for (Pair<Transaction, Transaction> p : dependencies.getDependent()) {
            if ((p.getKey().name == a && p.getValue().name == b) ||
                    (p.getKey().name == b && p.getValue().name == a)) {
                return true;
            }
        }
        return false;
    }

    // Zapisuje graf w formacie DOT
    public void saveDot(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("digraph G {\n");
            writer.write("    rankdir=LR;\n");
            writer.write("    node [shape=circle];\n\n");

            // Tworzymy węzły (dodajemy _n, żeby poźniej rozróżnić sytuacjie, gdy litery w słowie się powtarzają)
            char[] chars = word.toCharArray();
            List<String> nodeNames = new ArrayList<>();

            for (int i = 0; i < chars.length; i++) {
                String nodeName = chars[i] + "_" + (i + 1);
                nodeNames.add(nodeName);
                writer.write("    " + nodeName + " [label=\"" + chars[i] + "\"];\n");
            }
            writer.write("\n");

            // Znajdujemy krawędzie
            Set<String> edges = new HashSet<>();
            for (int i = 0; i < nodeNames.size(); i++) {
                for (int j = i + 1; j < nodeNames.size(); j++) {
                    String from = nodeNames.get(i);
                    String to = nodeNames.get(j);
                    char fromChar = from.charAt(0);
                    char toChar = to.charAt(0);

                    if (areDependent(fromChar, toChar)) {
                        edges.add(from + " -> " + to);
                    }
                }
            }

            // Usuwamy krawędzie redundantne (wykorzystujemy do tego algorytm Floyda-Warshalla)
            Set<String> minimalEdges = removeEdges(edges, nodeNames);

            // Zapisujemy krawędzie
            for (String edge : minimalEdges) {
                writer.write("    " + edge + ";\n");
            }

            writer.write("}\n");
            System.out.println("Zapisano do: " + filename);

        } catch (IOException e) {
            System.err.println("Błąd: " + e.getMessage());
        }
    }


    // Usuwa redundantne krawędzie
    private Set<String> removeEdges(Set<String> edges, List<String> nodeNames) {
        // Tworzymy macierz sąsiedztwa
        int n = nodeNames.size();
        boolean[][] adjacency = new boolean[n][n];

        // Inicjalizacja macierzy sąsiedztwa
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            int fromIndex = nodeNames.indexOf(parts[0]);
            int toIndex = nodeNames.indexOf(parts[1]);
            adjacency[fromIndex][toIndex] = true;
        }


        boolean[][] transitiveClosure = new boolean[n][n];

        // Kopiujemy macierz sąsiedztwa
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                transitiveClosure[i][j] = adjacency[i][j];
            }
        }

        // Algorytm Floyda-Warshalla
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (transitiveClosure[i][k] && transitiveClosure[k][j]) {
                        transitiveClosure[i][j] = true;
                    }
                }
            }
        }

        // Minimalne krawędzie dla grafu
        Set<String> minimalEdges = new HashSet<>();

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            int fromIndex = nodeNames.indexOf(parts[0]);
            int toIndex = nodeNames.indexOf(parts[1]);

            // Sprawdzamy czy istnieje ścieżka przez inne węzły
            boolean isRedundant = false;

            for (int k = 0; k < n; k++) {
                if (k != fromIndex && k != toIndex) {
                    if (transitiveClosure[fromIndex][k] && transitiveClosure[k][toIndex]) {
                        isRedundant = true;
                        break;
                    }
                }
            }

            if (!isRedundant) {
                minimalEdges.add(edge);
            }
        }

        return minimalEdges;
    }
}