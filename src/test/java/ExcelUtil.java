import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtil {
    private Workbook workbook;
    private Sheet sheet;

    public ExcelUtil(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCellData(int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        Cell cell = row.getCell(colNum);
        return cell.toString();
    }

    public int getRowCount() {
        return sheet.getLastRowNum();
    }
}
