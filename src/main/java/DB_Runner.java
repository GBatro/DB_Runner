import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DB_Runner {

    private static final CommQueries cq = new CommQueries();;
    private static final String commLoc = "";

    public static void main(String[] args) {
        Workbook name1 = null;
        Workbook name2 = null;
        Workbook name3 = null;

        try {
            name1 = setUpWoorkbook("");
            name2 = setUpWoorkbook("");
            name3 = setUpWoorkbook("");

            wb2dbCompare(name1);
            //wb2dbCompare(name2);
            //wb2dbCompare(name3);

            name1.close();
            name2.close();
            name3.close();
        } catch(IOException e) {
            System.out.println(e);
        }
    }

    private static Workbook setUpWoorkbook(String name) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(commLoc+name+".xlsx"));
        Workbook workbook = new XSSFWorkbook(inputStream);
        inputStream.close();

        return workbook;
    }

    private static void wb2dbCompare(Workbook workbook) {
        int invoice;
        CommRow commRow;

        //Row iterator
        Iterator<Row> iterator = workbook.getSheetAt(1).iterator();

        //Cell iterator objects
        Row nextRow = iterator.next();;
        Iterator<Cell> cellIterator;
        Cell cell;

        //Go through all of the rows and collect info
        while (iterator.hasNext()) {
            //Object to hold all of the row info
            commRow = new CommRow();

            //Next row, iterate through row
            nextRow = iterator.next();
            cellIterator = nextRow.cellIterator();

            //Iterate through the row and collect all the info
            cell = cellIterator.next();
            invoice = (int)cell.getNumericCellValue();
            cell = cellIterator.next();
            commRow.setDate((Date)cell.getDateCellValue());
            cell = cellIterator.next();
            commRow.setSale(new BigDecimal(cell.getNumericCellValue()));
            cell = cellIterator.next();
            commRow.setGp(new BigDecimal(cell.getNumericCellValue()));
            cell = cellIterator.next();
            commRow.setSaleType((int)cell.getNumericCellValue());
            cell = cellIterator.next();
            commRow.setOverage(new BigDecimal(cell.getNumericCellValue()));
            cell = cellIterator.next();
            commRow.setCustomer(cell.getStringCellValue());
            cell = cellIterator.next();
            commRow.setSalesPerson((int)cell.getNumericCellValue());

            //System.out.print("\n" + invoice + commRow.toString());

            System.out.print(cq.compareInfo(invoice, commRow));
        }
    }
}