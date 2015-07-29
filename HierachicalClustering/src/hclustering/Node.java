package hclustering;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a graph node with an associated set of edges.
 *
 * @modified 10/10/14 Jaimes
 */
public class Node
{

  
    private int nodeID; // The integer id of the node
    private String nodeLabel; // The name of the node. May not be used.
    // private ArrayList<Integer> edges; // The edges (other nodes) connected to
    // this node.
    private Set<Integer> edges;

    /**
     * Constructor for new Nodes.
     *
     * @param nodeID The integer id number of the node.
     */
 

    public Node(int nodeID)
    {
        this.nodeID = nodeID;
        edges = new HashSet<Integer>();
        nodeLabel = "";
    }

    /**
     * Adds an edge to the node.
     *
     * @param edgeID The integer of the node connected to.
     */
    public void addEdge(int edgeID)
    {
        edges.add(edgeID);
    }

    /**
     * Sets the label of this node.
     *
     * @param label The string label of this node to set
     */
    public void setNodeLabel(String label)
    {
        this.nodeLabel = label;
    }

    /**
     * Gets this nodes integer ID representation.
     *
     * @return This node's integer id
     */
    public int getID()
    {
        return nodeID;
    }

    public Set<Integer> getEdges()
    {
        return edges;
    }

    public String toString()
    {
        return "" + nodeID;
    }
}
