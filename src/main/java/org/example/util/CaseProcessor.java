package org.example.util;

import org.example.model.Transaction;

import java.util.List;
import java.util.Set;

public class CaseProcessor {
    public Dependencies dependencies;
    public FoataNormalForm foataNormalForm;
    public List<Transaction> transactions;
    public String word;
    public String outputFile;

    public CaseProcessor(List<Transaction> transactions, String word, String outputFile) {
        this.word = word;
        this.transactions = transactions;
        this.dependencies = new Dependencies(transactions);
        this.outputFile = outputFile;
    }

    public void proces() {
        // Wyświetlanie danych
        for (Transaction a : transactions) {
            System.out.println(a.toString());
        }
        System.out.println("Word = " + word);
        System.out.println(dependencies.toString());

        // Tworzenie FNF
        foataNormalForm = new FoataNormalForm(word, dependencies);
        List<Set<Character>> levels = foataNormalForm.compute();
        System.out.println("FNF: " + levels);

        // Tworzenie pliku z grafem w formacie DOT
        foataNormalForm.saveDot(outputFile);
    }
}
