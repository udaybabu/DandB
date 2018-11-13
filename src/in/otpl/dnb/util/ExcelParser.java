package in.otpl.dnb.util;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelParser {

private static final Logger log = Logger.getLogger(ExcelParser.class);
	
	public List<Map<String,String>> parse(byte[] file) {
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();

		try {

			XSSFWorkbook wb = null;
			try {
				InputStream is = new ByteArrayInputStream(file);
				//POIFSFileSystem fs = new POIFSFileSystem(is);
				wb = new XSSFWorkbook(is);
			} catch (IOException e) {
				log.error(ErrorLogHandler.getStackTraceAsString(e));
				return null;
			}

			int numOfSheets = wb.getNumberOfSheets();

			for (int i = 0; i < numOfSheets; i++) {
				XSSFSheet sheet = wb.getSheetAt(i);

				Iterator<?> rows = sheet.rowIterator();
				List<String> columnHeaders = new ArrayList<String>();

				if (rows.hasNext()) {
					XSSFRow columnHeaderRow = (XSSFRow) rows.next();
					short c1 = columnHeaderRow.getFirstCellNum();
					short c2 = columnHeaderRow.getLastCellNum();
					for (short c = c1; c < c2; c++) {
						@SuppressWarnings("deprecation")
						XSSFCell cell = columnHeaderRow.getCell(c);
						if (cell != null) {
							String cellValue = getCellValue(cell);
							if (cellValue != null && cellValue.trim().length() > 0) {
								columnHeaders.add(cellValue);
							}
						}
					}

				}

				while (rows.hasNext()) {
					XSSFRow row = (XSSFRow) rows.next();
					short c1 = row.getFirstCellNum();
					short c2 = row.getLastCellNum();


					if(isBlankRow(row)) {
						continue;
					}
					Map<String,String> map = new HashMap<String,String>();
					// loop for every cell in each row
					for (short c = c1; c < c2; c++) {
						@SuppressWarnings("deprecation")
						XSSFCell cell = row.getCell(c);
						if (cell != null) {
							String cellValue = getCellValue(cell);
							if (cellValue != null && cellValue.trim().length() > 0) {
								String columnHeader = "";
								try {
									columnHeader = (String) columnHeaders.get(c);
								} catch (Exception e) {
									log.error(ErrorLogHandler.getStackTraceAsString(e));

									columnHeader = "Unknown " + c;
								}

								map.put(columnHeader, cellValue);
							}
						}
					}

					list.add(map);
				}

			}
		} catch (Exception ex) {
			log.error(ErrorLogHandler.getStackTraceAsString(ex));
		}
		return list;
	
	}
	
	@SuppressWarnings("deprecation")
	private String getCellValue(XSSFCell cell) {
		if (cell == null)
			return null;
        
		String result = null;
   
		int cellType = cell.getCellType();
		switch (cellType) {
		case XSSFCell.CELL_TYPE_BLANK:
			result = "";
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			result = cell.getBooleanCellValue() ? "true" : "false";
			break;
		case XSSFCell.CELL_TYPE_ERROR:
			result = "ERROR: " + cell.getErrorCellValue();
			break;
		case XSSFCell.CELL_TYPE_FORMULA:
			result = cell.getCellFormula();
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			XSSFCellStyle cellStyle = cell.getCellStyle();
			short dataFormat = cellStyle.getDataFormat();

			// assumption is made that dataFormat = 14,
			// when cellType is HSSFCell.CELL_TYPE_NUMERIC
			// is equal to a DATE format.
			final int DATA_FORMAT_VALUE = 14;
			if (dataFormat == DATA_FORMAT_VALUE) {
				result = cell.getDateCellValue().toString();
			} else {
				BigDecimal decimal = new BigDecimal(cell.getNumericCellValue());
				if(decimal.scale() > 0){
					result = decimal.setScale(14, RoundingMode.CEILING).toPlainString();
				}else{
					result = decimal.toPlainString();
				}
				//result = String.valueOf(cell.getNumericCellValue());
			}

			break;
		case XSSFCell.CELL_TYPE_STRING:
			result = cell.getStringCellValue();
			break;
		default:
			break;
		}

		return result;
	}

	
	private boolean isBlankRow(XSSFRow row) {
		if (row == null)
			return true;
		else {
			int noOfColumns = row.getLastCellNum();
			for (int i = 0; i < noOfColumns; ++i) {
				XSSFCell cell = row.getCell(i);
				if (cell == null)
					continue;

				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_BLANK:
					continue;
				case XSSFCell.CELL_TYPE_ERROR:
					continue;
				case XSSFCell.CELL_TYPE_NUMERIC:
					double db = cell.getNumericCellValue();
					if (new Double(db).toString().equals(""))
						continue;
					else
						return false;
				default:
					@SuppressWarnings("deprecation")
					String str = cell.getStringCellValue();
					if (str == null)
						continue;
					str = str.trim();
					if ("".equals(str))
						continue;
					return false;
				}
			}
			return true;
		}
	}
	
	
	
}