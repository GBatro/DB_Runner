/**
import java.util.Hashtable;


public class Dictionaries {
    private Hashtable<int, String> salesType = new Hashtable<int, String>();
    private Hashtable<int, String> salesPerson = new Hashtable<int, String>();

    public Dictionaries() {
        {
            salesType.put(1, "Undefined");
            salesType.put(2, "next");
        }
        {
            salesPerson.put(1, "Undefined");
        }
    }

    public String getSalesType(int key) {
        return salesType.get(key);
    }

    public String getSalesPerson(int key) {
        return salesPerson.get(key);
    }
}
*/
