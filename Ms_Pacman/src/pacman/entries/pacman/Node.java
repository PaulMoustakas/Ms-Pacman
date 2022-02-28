package pacman.entries.pacman;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Node {

    private BufferedWriter writer;

    private String label;

    public HashMap<String,Node> children = new HashMap<String,Node>();

    public Node(){}

    public Node(String label) {
        this.label = label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() { return label; }

    public void addChild(String edgeLabel,Node destinationVertex) {
        children.put(edgeLabel,destinationVertex);
    }

    public Node getChild(String key) {
        return children.get(key);
    }

    //Print code stolen from stack overflow

    public void print(){
        try {
            PrintStream filePrint = new PrintStream("W:\\git\\DA272A\\Ms_Pacman\\src\\pacman\\entries\\pacman\\outputTree.txt");
            System.setOut(filePrint);
            print("");
        } catch (Exception e) {
            print("");
            System.err.println("Failed to write to file.");
            e.printStackTrace();
        }

    }

    private void print(String space) {

        if (children.isEmpty()) {
            System.out.print(space);
            System.out.println("  L " + getLabel());
        }
        Map.Entry<String, Node>[] nodes = children.entrySet().toArray(new Map.Entry[0]);
        for (int i = 0; i < nodes.length; i++) {
            System.out.print(space);
            if (i == nodes.length - 1) {
                System.out.println("L \"" + label + "\" = " + nodes[i].getKey() + ":");
                nodes[i].getValue().print(space + "    ");
            } else {
                System.out.println("|- \"" + label + "\" = " + nodes[i].getKey() + ":");
                nodes[i].getValue().print(space + '|' + "   ");
            }
        }
    }
}