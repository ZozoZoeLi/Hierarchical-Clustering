package hclustering;

import java.io.IOException;
import java.util.Scanner;

public class MainActivity
{

    public static void main(String[] args) throws IOException
    {
        
        System.out.println("Please input thresh of H Clustering.");
        Scanner scan = new Scanner(System.in);
        double userInput = scan.nextDouble();
        System.out.println("Your input number is: " + userInput);
        LoadGML gml = new LoadGML();
        HClustering hc = new HClustering(gml.getArray());
        QualityFunction qf = new DensityQualityFunction();
        System.out.println("======================================");
        System.out.println("Total number of nodes: " + hc.getComs().size());
        hc.cluster(qf, userInput);
        System.out.println(hc.getComs());
        System.out.println("There are " + hc.getComs().size() + " clusters formed.");
    }
}
