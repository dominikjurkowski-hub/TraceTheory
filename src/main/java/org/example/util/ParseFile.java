package org.example.util;

import org.example.model.InputData;
import org.example.model.Transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ParseFile {

    public static InputData load(String filename) throws IOException {
        ArrayList<Transaction> transactions = new ArrayList<>();
        String word = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // pomijamy puste linie
                if (line.isEmpty()) continue;



                // jeśli linia zaczyna się od "(" to tranakcja
                if (line.startsWith("(")) {
                    Transaction a = parseTransaction(line);
                    transactions.add(a);
                }

                // jeśli linia zaczyna się od "w"
                else if (line.startsWith("w")) {
                    String[] parts = line.split("=");
                    word = parts[1].trim();
                }

                else if (line.startsWith("A")) continue;
            }
        }


        return new InputData(transactions, word);
    }

    public static Transaction parseTransaction(String line){
        line = line.trim();
        // extract name
        int start = line.indexOf('(');
        int end = line.indexOf(')');
        char name = line.charAt(start + 1);

        // extract variables

        String expr = line.substring(end + 1).trim();
        String[] sides = expr.split(":=");
        String left = sides[0].trim();
        String right = sides[1].trim();
        Set<String> writeVars = new HashSet<>();
        writeVars.add(left);

        String cleaned = right.replaceAll("[^a-zA-Z]", " ");
        String[] vars = cleaned.trim().split("\\s+");
        Set<String> readVars = new HashSet<>();
        for (String v : vars) {
            if (!v.isEmpty()) readVars.add(v);
        }

        return new Transaction(name, readVars, writeVars);

    }
}
