package hclustering;

public class DensityQualityFunction implements QualityFunction
{

    @Override
    public double calculate_cluster_coefficient(Community c1, Community c2)
    {
        Community new_community = new Community();
        new_community.merge(c1);
        new_community.merge(c2);
       
        return new_community.density();
    }
}
