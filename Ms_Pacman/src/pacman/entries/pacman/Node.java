package pacman.entries.pacman;

import java.io.BufferedWriter;
import java.util.HashMap;


public class Node {

    public HashMap <String, Node> nodeChildren = new HashMap<String,Node>();
    private BufferedWriter writer;
    private String nodeLabel;

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


    public String getLabel () {
                return nodeLabel;
    }

    public void addChild (String edgeLabel, Node destinationVertex){
                nodeChildren.put(edgeLabel, destinationVertex);
                nodeChildren.put(edgeLabel, destinationVertex);
            }

            public Node getChild (String key){
                return nodeChildren.get(key);
            }
}