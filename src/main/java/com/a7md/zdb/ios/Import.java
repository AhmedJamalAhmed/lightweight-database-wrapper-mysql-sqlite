package com.a7md.zdb.ios;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.scene.Node;
import javafx.stage.FileChooser;

public class Import {

    public static void ImportDate(Node node, Consumer<ArrayList<String[]>> rowsConsumer) throws IOException, InvalidFormatException {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(node.getScene().getWindow());
        if (file != null) {
            new ExcelView(file.getAbsolutePath(), 0).showInNewWindow();
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);
            rowsConsumer.accept(getSheetValues(sheet));
        }
    }


    public static ArrayList<String[]> getSheetValues(Sheet sheet) {
        try {
            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();
            Row row;
            Cell cell;
            int cols = 0; // No of columns
            int tmp;
            // This trick ensures that we get the data properly even if it doesn't start from first few rows
            for (int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if (tmp > cols) cols = tmp;
                }
            }
            ArrayList<String[]> data = new ArrayList<>();


            for (int r = 0; r < rows; r++) {
                row = sheet.getRow(r);
                String[] rowData = new String[cols];
                if (row != null) {
                    for (short c = 0; c < cols; c++) {
                        cell = row.getCell(c);
                        if (cell != null) {
                            rowData[c] = cell.getStringCellValue();
                        }
                    }
                }
                data.add(rowData);
            }
            return data;
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        return new ArrayList<>();
    }
}
