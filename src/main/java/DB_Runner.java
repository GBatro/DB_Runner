import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DB_Runner {

    private static final CommQueries cq = new CommQueries();
    private static final Scanner sc = new Scanner(System.in);
    private static String[] names;
    private static List invoiceList = new ArrayList<Integer>();
    private static List invoiceListFile = new ArrayList<Integer>();

    //Location where related files can be found
    private static final String fileLocations = "C:\\Users\\blake\\Downloads\\";

    public static void main(String[] args) {
        int numWorkbooks;

        System.out.println("How many workbooks do you want to check?");
        numWorkbooks = sc.nextInt();
        sc.nextLine();
        names = new String[numWorkbooks];

        System.out.println("Enter names (filenames, specifically) below:");
        for(int x = 0; x < numWorkbooks; x++) {
            names[x] = sc.nextLine();
        }

        readTxt("archive.txt");

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

        System.out.println("Do you want to archive now? (y/n)");
        String archive;

        archive = sc.next();
        sc.close();

        if(archive.compareTo("y") == 0) {
            saveToTxt("archive.txt");
        }
    }

    private static Workbook setUpWorkbook(String name) throws IOException {
        FileInputStream inputStream = new FileInputStream(fileLocations +name+".xlsx");
        Workbook workbook = new XSSFWorkbook(inputStream);
        inputStream.close();

        return workbook;
    }

    private static void wb2dbCompare(Workbook workbook, String salesPerson) {
        Integer invoice;
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
            if(isInArchive(invoice)) {
                System.out.println("---------- " + invoice + " was found to be in an old commission sheet.");
            }
            invoiceList.add(invoice);
        }
    }

    private static void saveToTxt(String fileName) {
        invoiceList.addAll(invoiceListFile);
        Collections.sort(invoiceList);
        int size = invoiceList.size();

        try {
            FileWriter fw = new FileWriter(new File(fileLocations + fileName));
            for (int i = 0; i < size; i++) {
                fw.write(invoiceList.remove(0) + "\n");
            }
            fw.close();
        } catch(IOException e) {
            System.out.println("File \"" + fileName + "\" was not found or was not accessible.");
        }
    }

    private static void readTxt(String fileName) {
        try
        {
            Scanner scan = new Scanner(new File(fileLocations + fileName));
            while(scan.hasNextInt())
            {
                invoiceListFile.add(scan.nextInt());
            }
        }
        catch (IOException fe)
        {
            System.out.println("File \"" + fileName + "\" was not found or was not accessible.");
        }
    }

    private static boolean isInArchive(Integer invoice) {
        return invoiceListFile.contains(invoice);
    }
}