package hclustering;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Creates an array list of nodes based on data in a file called by the load
 * method.
 *
 * @author Jared Kwok, Jaimes Booth 1305390 10/10/14
 */
public class LoadGML
{

    // The set of Nodes to populate.
    private final ArrayList<Node> nodeArray;
    private static String fileName = "";

    /**
     * Creates a test LoadGML object to run.
     *
     * @param args Command line arguments.
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException
    {
        //Load a test file
        LoadGML testRun = new LoadGML();

    }

    public ArrayList<Node> getArray()
    {
        return nodeArray;
    }

    /**
     * This constructor loads the hard-coded file into the array of nodes.
     *
     * @throws java.io.IOException
     */
    public LoadGML() throws IOException
    {
        nodeArray = new ArrayList<>();

        load("dolphins.gml");

    }

    /**
     * Loads digraph data stored in the specified file. Works with gml format.
     * The first half of the gml file lists the Nodes. The second half, lists
     * the edges.
     *
     * @param fileName The name of the file to parse.
     * @throws java.io.IOException
     */
    public final void load(String fileName) throws IOException
    {
        Path path = Paths.get(fileName);
        String lineFromFile;
        final Scanner scanner = new Scanner(path);
        Scanner subScanner; //= new Scanner(lineFromFile);

        System.out.println("Load " + fileName);
        System.out.println("");

        try
        {

            // Search line by line until "id" is encountered        
            while (scanner.hasNextLine())
            {

                lineFromFile = scanner.nextLine();


                // First half of file defines nodes

                // if line has id
                if (lineFromFile.contains("id"))
                {

                    subScanner = new Scanner(lineFromFile);

                    // Extract the integer nodeID
                    // skip the string token ("id")
                    subScanner.next();
                    // get the "id" integer 
                    int nodeID = subScanner.nextInt();

                    // add this new node to set of nodes.
                    Node newNode = new Node(nodeID);
                    nodeArray.add(newNode);
                  //  System.out.println("Node: " + nodeID);

                    // skip the rest of the current line
                    lineFromFile = scanner.nextLine();

                    // Search next line for "label"
                    subScanner = new Scanner(lineFromFile);
                    // skip the string token ("label")
                    subScanner.next();

                    // Extract the label String 
                   String nodeLabel = subScanner.next();

                    // skip the rest of the current line
                    scanner.nextLine();

                    // add the string to node label
                   newNode.setNodeLabel(nodeLabel);
                 //   System.out.println("Label: " + nodeLabel);

                }

                // Second half of file defines edges

                // if line has "source"
                if (lineFromFile.contains("source"))
                {
                    subScanner = new Scanner(lineFromFile);
                    // skip this token ("source")
                    subScanner.next();

                    // Extract the integer source node
                    int sourceNodeID = subScanner.nextInt();
                 //   System.out.println("sourceNode: " + sourceNodeID);

                    // skip the rest of the current line
                    lineFromFile = scanner.nextLine();

                    subScanner = new Scanner(lineFromFile);
                    // skip the token ("target")
                    subScanner.next();

                    // get the target (edge) id
                    // Extract the integer edgeID
                    int targetNodeID = subScanner.nextInt();

                    // skip the rest of the current line
                    scanner.nextLine();

                    // add the targetID edge to the source node in the set of nodes.
                    // TODO: More efficient way to refence source node?
                    for (Node node : nodeArray)
                    {
                        if (node.getID() == sourceNodeID)
                        {
                            node.addEdge(targetNodeID);
                        }
                        /////// add edge from other direction
                        if (node.getID() == targetNodeID)
                        {
                            node.addEdge(sourceNodeID);
                        }

                    }

               //     System.out.println("Source Node: " + sourceNodeID
               //             + "; Target Node (edge): " + targetNodeID);

                }
            }

            scanner.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("");
        System.out.println(fileName + " loaded");
    }
}
