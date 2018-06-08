package adriancardenas.com.ehealth.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class StringDateComparator implements Comparator<String>
{
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public int compare(String lhs, String rhs)
    {
        try {
            return dateFormat.parse(lhs).compareTo(dateFormat.parse(rhs));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
