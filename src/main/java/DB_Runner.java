import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DB_Runner {

    private static final CommQueries cq = new CommQueries();
    private static final String commLoc = "C:\\Users\\blake\\Downloads\\";
    private static final Scanner sc = new Scanner(System.in);
    private static String[] names;

    public static void main(String[] args) {
        int numWorkbooks;

        System.out.println("How many workbooks do you want to check?");
        numWorkbooks = sc.nextInt();
        sc.nextLine();
        names = new String[numWorkbooks];

        System.out.println("Enter names below:");
        for(int x = 0; x < numWorkbooks; x++) {
            names[x] = sc.nextLine();
        }

        sc.close();

        for(int x = 0; x < numWorkbooks; x++) {
            System.out.println("Showing results for " + names[x] + "'s workbook:");

            try {
                Workbook wb = setUpWorkbook(names[x]);
                wb2dbCompare(wb, names[x]);
                wb.close();
            } catch (IOException e) {
                System.out.println(e);
            }

            System.out.println();
        }
    }

    private static Workbook setUpWorkbook(String name) throws IOException {
        FileInputStream inputStream = new FileInputStream(commLoc+name+".xlsx");
        Workbook workbook = new XSSFWorkbook(inputStream);
        inputStream.close();

        return workbook;
    }

    private static void wb2dbCompare(Workbook workbook, String salesPerson) {
        int invoice;
        CommRow commRow;

        //Row iterator
        Iterator<Row> rowIterator = workbook.getSheetAt(1).iterator();

        //Cell iterator objects
        Row nextRow = rowIterator.next();
        Iterator<Cell> cellIterator;
        Cell cell;

        //Go through all the rows and collect info
        while (rowIterator.hasNext()) {
            //Object to hold all the row info
            commRow = new CommRow();

            //Next row, iterate through row
            nextRow = rowIterator.next();
            cellIterator = nextRow.cellIterator();

            //Iterate through the row and collect all the info
            cell = cellIterator.next();
            invoice = (int)cell.getNumericCellValue();
            cell = cellIterator.next();
            commRow.setDate(cell.getDateCellValue());
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

            System.out.print(cq.compareInfo(invoice, commRow, salesPerson));
        }
    }
}