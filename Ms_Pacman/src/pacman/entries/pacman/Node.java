package pacman.entries.pacman;

import java.io.BufferedWriter;
import java.util.HashMap;

public class Node {

    private String nodeLabel;
    public HashMap <String, Node> nodeChildren = new HashMap<String,Node>();

    public Node () {
    }

    public Node (String nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    public String getNodeLabel () {
        return nodeLabel;
    }

    public void setLabel (String label) {
        this.nodeLabel = label;
    }

    public void addChild(String edgeLabel,Node destinationVertex) {
        nodeChildren.put(edgeLabel,destinationVertex);
    }

    public Node getChild(String key) {
        return nodeChildren.get(key);
    }

}
