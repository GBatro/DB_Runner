import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommQueries {
    private String problems;

    private Connection con = null;
    private Statement sst = null;
    private final String dbLoc = "V:\\Shared Files\\.Scaffoldmart_Record_Database\\Scaffoldmart Records_be.accdb";

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public CommQueries() {
        //Create Connection
        try {
            //DB Connection
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection("jdbc:ucanaccess://" + dbLoc);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public String compareInfo(int invoiceNumber, CommRow commRow, String salesPerson) {
        problems = "" + invoiceNumber;

        try {
            //Set Up Queries
            String order = "select * from order where ID='"+invoiceNumber+"' ";
            String trans = "select * from order_transaction where order_ID='"+invoiceNumber+"' order by transaction_date desc;";

            //Run Queries
            ResultSet orderResults = runQueries(order);
            ResultSet transResults = runQueries(trans);

            //Do Comparisons
            //TODO: fix input variable for shipping comparison
            //problems += compareShipping(transResults, 0);
            problems += compareDate(transResults, commRow.getDate());
            problems += compareSalesPerson(orderResults, commRow.getSalesPerson());
            problems += compareSales(transResults, commRow.getSale());
            problems += compareGP(transResults, commRow.getGp());
            problems += compareSaleType(orderResults, commRow.getSaleType());
        } catch (Exception e) {
            System.out.println(e);
        }

        return problems + "\n";
    }
    private ResultSet runQueries(String query) throws Exception {
        sst = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT);
        return sst.executeQuery(query);
    }
    private boolean compareBigDecVariance(BigDecimal bd1, BigDecimal bd2, double variance) {
        return Math.abs(Double.parseDouble(bd1.subtract(bd2).toString())) > variance;
    }
    private String compareShipping(ResultSet rs, BigDecimal commShip) throws SQLException {
        //System.out.println("---Compare Shipping---");
        BigDecimal shipping = new BigDecimal(0);
        BigDecimal temp;

        rs.beforeFirst();

        while(rs.next()) {
            temp = new BigDecimal(rs.getString(rs.findColumn("shipping"))).setScale(2, RoundingMode.DOWN);
            //System.out.println("GP: " + temp);
            shipping = shipping.add(temp);
        }

        if(compareBigDecVariance(commShip, shipping, .50)) {
            return " | Sales: " + shipping;
        } else {
            return "";
        }
    }

    private String compareSalesPerson(ResultSet rs, int commSalesPerson) throws SQLException {
        //System.out.println("---Compare Sales Person---");
        int salesPerson = 1;
        int temp;
        Dictionaries dict = new Dictionaries();

        rs.beforeFirst();

        if(rs.next()) {
            temp = rs.getInt(rs.findColumn("sales_person"));
            //System.out.println("Sales Person: " + dict.getSalesPerson(temp));
            salesPerson = temp;
        }

        if(commSalesPerson != salesPerson) {
            return " | Sales Person: " + dict.getSalesPerson(salesPerson);
        } else {
            return "";
        }
    }
    private String compareDate(ResultSet rs, Date commDate) throws SQLException {
        //System.out.println("---Compare Date---");
        Date dbDate = null;
        Date temp;

        rs.beforeFirst();

        if(rs.next()) {
            temp = (Date)rs.getDate(rs.findColumn("transaction_date"));
            //System.out.println("Date: " + temp);
            dbDate = temp;
        }

        if(commDate.compareTo(dbDate) != 0) {
            return " | Date: " + dateFormat.format(dbDate);
        } else {
            return "";
        }
    }
    private String compareSales(ResultSet rs, BigDecimal commSales) throws SQLException {
        //System.out.println("---Compare Sales---");
        BigDecimal sales = new BigDecimal(0);
        BigDecimal temp;

        rs.beforeFirst();

        while(rs.next()) {
            temp = new BigDecimal(rs.getString(rs.findColumn("sale_amt"))).setScale(2, RoundingMode.DOWN);
            temp = temp.add(new BigDecimal(rs.getString(rs.findColumn("sale_di"))).setScale(2, RoundingMode.DOWN));
            temp = temp.add(new BigDecimal(rs.getString(rs.findColumn("sale_lvl"))).setScale(2, RoundingMode.DOWN));
            //System.out.println("Sale: " + temp);
            sales = sales.add(temp);
        }

        if(compareBigDecVariance(commSales, sales, .50)) {
            return " | Sales: " + sales;
        } else {
            return "";
        }
    }
    private String compareGP(ResultSet rs, BigDecimal commGP) throws SQLException {
        //System.out.println("---Compare GP---");
        BigDecimal gp = new BigDecimal(0);
        BigDecimal temp;

        rs.beforeFirst();

        while (rs.next()) {
            temp = new BigDecimal(rs.getString(rs.findColumn("gp_amt"))).setScale(2, RoundingMode.DOWN);
            temp = temp.add(new BigDecimal(rs.getString(rs.findColumn("gp_di"))).setScale(2, RoundingMode.DOWN));
            temp = temp.add(new BigDecimal(rs.getString(rs.findColumn("gp_lvl"))).setScale(2, RoundingMode.DOWN));
            //System.out.println("GP: " + temp);
            gp = gp.add(temp);
        }

        if(compareBigDecVariance(commGP, gp, .50)) {
            return " | GP: " + gp;
        } else {
            return "";
        }
    }
    private String compareSaleType(ResultSet rs, int commSaleType) throws SQLException {
        //System.out.println("---Compare Sale Type---");
        int saleType = 0;
        int temp;
        Dictionaries dict = new Dictionaries();

        rs.beforeFirst();

        if(rs.next()) {
            temp = rs.getInt(rs.findColumn("sale_type"));
            //System.out.println("Sale Type: " + temp);
            saleType = temp;
        }

        if(commSaleType != saleType) {
            return " | Sale Type: " + dict.getSalesType(saleType);
        } else {
            return "";
        }
    }
}
