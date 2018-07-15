package gov.nist.itl.csd;

/**
 * Created by dyaga on 6/7/2016.
 */
public class RelationOutput
{
    public String PassingValues;
    public String NonPassingValues;

    public RelationOutput(String passing, String nonPassing)
    {
        PassingValues = passing;
        NonPassingValues = nonPassing;
    }

    @Override
    public String toString()
    {
        return PassingValues + " - " + NonPassingValues;
    }
}
