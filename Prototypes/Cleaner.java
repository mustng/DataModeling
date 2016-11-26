import java.util.ArrayList;

/**
 * Created by logan on 11/19/2016.
 *
 */
public class Cleaner
{
    int size;
    ArrayList<String> qry;
    ArrayList<String>[] fQry;
    public Cleaner(ArrayList<String> qq)
    {
        qry = qq;
        size = 1;
    }

    public ArrayList<String>[] formater()
    {
        ArrayList<String>[] fQry;

        if (qry.contains("<"))
            size++;

        fQry = new ArrayList[size];

        fQry[1] = carrotFormat();
        fQry[0] = qry;

        return fQry;
    }

    private ArrayList<String> carrotFormat()
    {
        int c = 0;
        int i = -1;
        ArrayList<String> cQry = new ArrayList<>();
        i = qry.indexOf("<");

        c++;
        if (qry.get(i - 1).matches(","))
        {
            i = i -1;
            cQry.add(qry.get(i));
            qry.remove(i);
        }
        while (c > 0)
        {
            cQry.add(qry.get(i));
            qry.remove(i);

            if (qry.get(i).matches("<"))
                c++;
            if (qry.get(i).matches(">"))
                c--;
        }
        if (qry.get(i).matches(","))
        {
            cQry.add(qry.get(i));
            qry.remove(i);
        }

        cQry = qry;

        return cQry;
    }
    public String dataTypeConv(String dataType)
    {
        dataType.toLowerCase();
        if (dataType.matches("(character|varchar|character varying)(.*)"))
            return "string";
        else if (dataType.matches("boolean"))
            return "boolean";
        else if (dataType.matches("(integer|smallint|bigint|decimal|numeric)(.*)"))
            return "decimal";
        else if (dataType.matches("(float|real)(.*)"))
            return "float";
        else if (dataType.matches("double precision"))
            return "double";
        else if (dataType.matches("interval"))
            return "duration";
        else if (dataType.matches("timestamp"))
            return "dateTime";
        else if (dataType.matches("time"))
            return "time";
        else if (dataType.matches("date"))
            return "date";
        else
            return null;
    }
}
