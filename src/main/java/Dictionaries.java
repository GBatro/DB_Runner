import java.util.Hashtable;


public class Dictionaries {
    private Hashtable<Integer, String> salesType = new Hashtable<Integer, String>();
    private Hashtable<Integer, String> salesPerson = new Hashtable<Integer, String>();

    public Dictionaries() {
        //Sales Type DICT
        salesType.put(1, "Undefined");
        salesType.put(2, "DI65");
        salesType.put(4, "Marine (less)");
        salesType.put(5, "Marine (over)");
        salesType.put(6, "Shop cart");
        salesType.put(7, "10%");
        salesType.put(8, "20%");
        salesType.put(9, "30%");
        salesType.put(10, "40%");
        salesType.put(11, "Steel");

        //Sales Person DICT
        salesPerson.put(1, "Undefined");
        salesPerson.put(2, "Glen");
        salesPerson.put(3, "Grace");
        salesPerson.put(4, "Gray");
        salesPerson.put(5, "Matt");
        salesPerson.put(6, "Morgan");
        salesPerson.put(7, "Ransom");
        salesPerson.put(8, "Brice");
        salesPerson.put(9, "Chase");

    }

    public String getSalesType(int key) {
        return salesType.get(key);
    }

    public String getSalesPerson(int key) {
        return salesPerson.get(key);
    }
}

