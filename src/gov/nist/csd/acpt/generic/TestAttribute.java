package gov.nist.csd.acpt.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TestAttribute
{
    private String       name     = null;
    private String       type     = null;
    private String       choice     = null;
    private List<String> elements = new ArrayList<String>();

    public TestAttribute(String name, String type, List<String> elements)
    {
        this.name = name;
        this.type = type;
        this.elements = elements;
    }

    public TestAttribute(String nametype, String elms)
    {

        StringTokenizer st1 = new StringTokenizer(nametype, ";");
        int count = 0;
        while (st1.hasMoreTokens())
        {
            String tk = st1.nextToken();
            if (count == 0)
            {
                this.name = tk;
            }
            else if (count == 1)
            {
                this.type = tk;
            }
            count++;
        }

        //@Wei Bao
        this.choice = elms;
//        StringTokenizer st = new StringTokenizer(elms, ",");
//        while (st.hasMoreTokens())
//        {
//            String tk = st.nextToken();
//            elements.add(tk);
//        }

    }

    /*
     * public TestAttribute(String string) {
     *
     * // System.err.println("Hwang"+string); // StringTokenizer st = new
     * StringTokenizer(string, "[,;:] "); StringTokenizer st = new
     * StringTokenizer(string, "[,;] "); int count = 0; while
     * (st.hasMoreTokens()) { String tk = st.nextToken(); if (count ==
     * 0){this.name = tk;} else if (count == 1){this.type = tk;} else {
     * elements.add(tk); } count++; } // printOut(); }
     */
    public void printOut()
    {
        System.out.println(this.name + ";" + this.type);
        for (int i = 0; i < this.elements.size(); i++)
        {
            System.out.print(this.elements.get(i) + ",");
        }
        System.out.println();
    }

    public String getName()
    {
        return this.name;
    }

    public String getType()
    {
        return this.type;
    }

    public String getChoice()
    {
        return this.choice;
    }

    public List<String> getElements()
    {
        return this.elements;
    }
}
