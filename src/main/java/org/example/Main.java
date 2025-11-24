package org.example;

import org.example.model.InputData;
import org.example.util.CaseProcessor;
import org.example.util.ParseFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        // Lista wejścia
        List<String> inputFiles = List.of("input/case1.txt", "input/case2.txt");

        for (String file : inputFiles) {
            // Parsowanie pliku do foramtu InputData
            InputData data = ParseFile.load(file);

            // Tworzenie ścierzki do wyjścia
            String outputFile = Paths.get("output",
                    Paths.get(file).getFileName().toString().replace(".txt", ".dot")).toString();

            // Główna funkcjionlaność (znajdowanie zależności, FNF i tworzenie grafu)
            CaseProcessor processor = new CaseProcessor(data.transactions(), data.word(), outputFile);
            processor.proces();
        }
    }
}