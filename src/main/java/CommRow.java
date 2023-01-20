import com.healthmarketscience.jackcess.impl.expr.DateTimeValue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommRow {
    private Date date;
    private BigDecimal sale;
    private BigDecimal gp;
    private int saleType;
    private BigDecimal overage;
    private String customer;
    private int salesPerson;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public CommRow() {
        date = null;
        sale = new BigDecimal(-1).setScale(2, RoundingMode.DOWN);
        gp = new BigDecimal(-1).setScale(2, RoundingMode.DOWN);
        saleType = 1;
        overage = new BigDecimal(-1).setScale(2, RoundingMode.DOWN);
        customer = null;
        salesPerson = 1;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getSale() {
        return sale;
    }

    public void setSale(BigDecimal sale) {
        this.sale = sale.setScale(2, RoundingMode.DOWN);
    }

    public BigDecimal getGp() {
        return gp;
    }

    public void setGp(BigDecimal gp) {
        this.gp = gp.setScale(2, RoundingMode.DOWN);
    }

    public int getSaleType() {
        return saleType;
    }

    public void setSaleType(int saleType) {
        this.saleType = saleType;
    }

    public BigDecimal getOverage() {
        return overage;
    }

    public void setOverage(BigDecimal overage) {
        this.overage = overage.setScale(2, RoundingMode.DOWN);
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(int salesPerson) {
        this.salesPerson = salesPerson;
    }

    @Override
    public String toString() {
        return  " | Date: " + dateFormat.format(date) +
                " | Sale: " + sale +
                " | GP: " + gp +
                " | Sale Type: " + saleType +
                " | Overage: " + overage +
                " | Customer: '" + customer + '\'' +
                " | Sales Person: " + salesPerson;
    }
}
