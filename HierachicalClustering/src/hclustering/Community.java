package hclustering;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Community
{

    private List<Node> vertices;
    public String forest = ""; // ( # #(# #))

    public Community()
    {
        vertices = new ArrayList<Node>();
    }

    public List<Node> getMembers()
    {
        List<Node> list = new ArrayList<Node>();
        list.addAll(vertices);
        return list;
    }

    public boolean addVertex(Node v)
    {
        forest = " " + v.getID();
        return vertices.add(v);
    }

    public void merge(Community c)
    {
        forest = "(" + forest + c.toString() + ")";
        this.vertices.addAll(c.getMembers());
    }

    public double density()
    {
        double density;

        double total = this.vertices.size() * (this.vertices.size() - 1) * 1.0 / 2.0;
        int existing_edges = 0;
        for (int i = 0; i < this.vertices.size(); i++)
        {
            for (int j = i + 1; j < this.vertices.size(); j++)
            {
                Node n1 = this.vertices.get(i);
                Node n2 = this.vertices.get(j);
                if (n1.getEdges().contains(n2.getID()))
                {
                    existing_edges++;
                }
            }
        }
        density = existing_edges * 1.0 / total;

        return density;
    }

    public int getIE()
    {
        int e = 0;
        Set<Integer> s = new HashSet<Integer>();
        for (Node n : vertices)
        {
            s.add(n.getID());
            for (Integer i : n.getEdges())
            {
                if (vertices.contains(i))
                {
                    if (!s.contains(i))
                    {
                        e++;
                    }
                }
            }
        }
        return e;
    }

    public int getOE(Community other)
    {
        int e = 0;
        for (Node n : vertices)
        {
            for (Integer i : n.getEdges())
            {
                for (Node o : other.getMembers())
                {
                    if (o.getID() == i)
                    {
                        e++;
                    }
                }
            }
        }
        return e;
    }

    public String toString()
    {

        return forest;
    }

}
