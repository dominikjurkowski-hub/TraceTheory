package org.example.util;

import org.example.model.Pair;
import org.example.model.Transaction;

import java.util.*;

public class Dependencies {
    private final Set<Pair<Transaction, Transaction>> dependent;
    private final Set<Pair<Transaction, Transaction>> independent;

    public Dependencies(List<Transaction> transactions) {
        this.dependent = new HashSet<>();
        this.independent = new HashSet<>();
        computeDependencies(transactions);
    }

    // Sprawdza czy transakcjia a jest zależna od transakcji b (a.read jest w b.write i w drugą strone)
    private boolean areDependent(Transaction a, Transaction b) {
        for (String read : a.readVariabls) {
            if (b.writeVariables.contains(read)) return true;
        }
        for (String write : a.writeVariables) {
            if (b.readVariabls.contains(write)) return true;
        }
        for (String write : a.writeVariables) {
            if (b.writeVariables.contains(write)) return true;
        }
        return false;
    }

    // Dla każdej pary transakcji sprawdza zależność, zapisuje je w Pair
    private void computeDependencies(List<Transaction> transactions) {
        for (Transaction a : transactions) {
            for (Transaction b : transactions) {
                if (a.name == b.name) {
                    // Transakcja jest zawsze zależna od siebie samej
                    dependent.add(new Pair<>(a, b));
                } else if (areDependent(a, b)) {
                    dependent.add(new Pair<>(a, b));
                } else {
                    independent.add(new Pair<>(a, b));
                }
            }
        }
    }

    public Set<Pair<Transaction, Transaction>> getDependent() {
        return dependent;
    }

    public Set<Pair<Transaction, Transaction>> getIndependent() {
        return independent;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Dependent: { ");
        for (Pair<Transaction, Transaction> p : dependent) {
            result.append("(").append(p.getKey().name).append(",").append(p.getValue().name).append(") ");
        }
        result.append("}\n");
        result.append("Independent: { ");
        for (Pair<Transaction, Transaction> p : independent) {
            result.append("(").append(p.getKey().name).append(",").append(p.getValue().name).append(") ");
        }
        result.append("}");
        return result.toString();
    }
}