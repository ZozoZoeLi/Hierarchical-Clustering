package hclustering;

import java.util.ArrayList;
import java.util.List;

public class HClustering
{

    private List<Node> vertices;
    private List<Community> communities;

    public HClustering(List<Node> vertices)
    {
        this.vertices = new ArrayList();
        this.vertices.addAll(vertices);
        communities = new ArrayList();
        init();
    }

    public void init()
    {
        this.communities.clear();
        for (Node v : vertices)
        {
            Community c = new Community();
            c.addVertex(v);
            communities.add(c);
        }
    }

    public List<Community> cluster(QualityFunction q, double thresh)
    {
        init();
        double current;
        Community m1 = null;
        Community m2 = null;
        do
        {
            current = Double.MIN_VALUE;
            for (int i = 0; i < communities.size(); i++)
            {
                Community c1 = communities.get(i);
                for (int j = i + 1; j < communities.size(); j++)
                {
                    Community c2 = communities.get(j);
                    double q_ = q.calculate_cluster_coefficient(c1, c2);
//                    System.out.println(">>>> "+ q_ +" <<<<");
                    if (q_ > current)
                    {
                        m1 = c1;
                        m2 = c2;
                        current = q_;
                    }
                }
            }

            if (current >= thresh && m1 != null && m2 != null)
            {
                m1.merge(m2);
   
                System.out.println("merging: " + m1);
                communities.remove(m2);
            }

        }
        while (current >= thresh && communities.size() > 1);
        return communities;
    }

    public List<Community> getComs()
    {
        return communities;
    }
}
