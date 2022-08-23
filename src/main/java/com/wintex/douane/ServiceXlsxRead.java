package com.wintex.douane;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ServiceXlsxRead {

	public static List<Record> readXlsxFromFile(File file) {
		List<Record> records = new ArrayList<>();
		try {
			FileInputStream stream = new FileInputStream(file.getAbsolutePath());
			XSSFWorkbook workbook = new XSSFWorkbook(stream);
			XSSFSheet sheet = workbook.getSheetAt(0);

			int startRow = 11;
			int idTrackingColumn = 1;
			int numberTrackingColumn = 2;
			int fullNameColumn = 3;
			int addressColumn = 9;

			for (int i = startRow; i < sheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = sheet.getRow(i);
				Cell idTrackingCell = row.getCell(idTrackingColumn);
				Cell numberTrackingCell = row.getCell(numberTrackingColumn);
				Cell fullNameCell = row.getCell(fullNameColumn);
				Cell addressCell = row.getCell(addressColumn);

				if (idTrackingCell.getCellType().equals(CellType.STRING)
						&& numberTrackingCell.getCellType().equals(CellType.STRING)
						&& fullNameCell.getCellType().equals(CellType.STRING)
						&& addressCell.getCellType().equals(CellType.STRING)) {
					
					Record record = new Record();
					record.setIdTracking(idTrackingCell.getStringCellValue());
					record.setNumberTacking(numberTrackingCell.getStringCellValue());
					record.setFullName(fullNameCell.getStringCellValue());
					record.setAddresse(addressCell.getStringCellValue());
					
					records.add(record);
				} else {
					break;
				}
			}

			stream.close();
			workbook.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		ServicePhonetic.calculatePhonetic(records);
		
		return records;
	}
}
