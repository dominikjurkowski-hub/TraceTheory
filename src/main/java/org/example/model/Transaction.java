package org.example.model;

import java.util.Set;

public class Transaction {
    public char name;
    public Set<String> readVariabls;
    public Set<String> writeVariables;

    public Transaction(char name, Set<String> readVariabls, Set<String> writeVariables){
        this.name = name;
        this.readVariabls = readVariabls;
        this.writeVariables = writeVariables;
    }

    @Override
    public String toString() {
        return "Transaction{" + "name=" + name + ", read=" + readVariabls + ", write=" + writeVariables + '}';
    }
}