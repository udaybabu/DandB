package in.otpl.dnb.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil{

	private static final Logger LOG = Logger.getLogger(ExcelUtil.class);

	private static final int SHEET_HEADING_ROWNUM = 0;// ROW 1 will be empty
	private static final int COLUMN_HEADING_ROWNUM = 2;
	private static final int DATA_START_ROWNUM = 3;
	private static final String HEADING_FONT_TYPE = "Arial";

	public void generateExcel(List<?> sheetsDataList, OutputStream os) {
		if (sheetsDataList == null || sheetsDataList.size() <= 0) {
			LOG.info("ExcelUtil.generateExcel: Data is not provided for the Excel Sheet");
			return;
		}

		XSSFWorkbook workbook = new XSSFWorkbook();
		for (int i = 0; i < sheetsDataList.size(); i++) {
			ExcelSheetDataBean sheetDataBean = ((ExcelSheetDataBean) (sheetsDataList.get(i)));
			String sheetName = sheetDataBean.getSheetName();
			XSSFSheet sheet = workbook.createSheet(sheetName);
			XSSFDrawing patriarch = sheet.createDrawingPatriarch();
			int numColumns = sheetDataBean.getColumnHeadings().size();
			XSSFCellStyle sheetHeadStyle = setSheetHeadingStyle(workbook);
			populateSheetHeading(sheet, sheetDataBean.getSheetHeading(), numColumns, sheetHeadStyle);

			XSSFCellStyle sheetHeaderStyle = setSheetHeaderStyle(workbook);
			XSSFCellStyle sheetdataStyle = setSheetDataStyle(workbook);
			populateData(workbook, sheet, patriarch, sheetDataBean, sheetHeaderStyle, sheetdataStyle);
		}
		writeWorkbook(workbook, os);
	}

	protected XSSFCellStyle setSheetHeadingStyle(XSSFWorkbook workbook) {
		XSSFCellStyle sheetHeadStyle = workbook.createCellStyle();
		XSSFFont headingFont = workbook.createFont();
		headingFont.setColor(HSSFColor.WHITE.index);
		headingFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// headingFont.setFontHeightInPoints((short)HEADING_FONT_SIZE);
		headingFont.setFontName(HEADING_FONT_TYPE);

		sheetHeadStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		sheetHeadStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		sheetHeadStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		sheetHeadStyle.setFont(headingFont);

		return sheetHeadStyle;
	}

	protected XSSFCellStyle setSheetHeaderStyle(XSSFWorkbook workbook) {
		XSSFCellStyle sheetHeadStyle = workbook.createCellStyle();
		XSSFFont headingFont = workbook.createFont();
		headingFont.setColor(HSSFColor.WHITE.index);
		headingFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// headingFont.setFontHeightInPoints((short)HEADING_FONT_SIZE);
		headingFont.setFontName(HEADING_FONT_TYPE);

		sheetHeadStyle.setFillForegroundColor(HSSFColor.GREEN.index);
		sheetHeadStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		sheetHeadStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		sheetHeadStyle.setFont(headingFont);
		sheetHeadStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		sheetHeadStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		sheetHeadStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		sheetHeadStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);

		return sheetHeadStyle;
	}

	protected XSSFCellStyle setSheetDataStyle(XSSFWorkbook workbook) {
		XSSFCellStyle sheetdataStyle = workbook.createCellStyle();
		XSSFFont dataFont = workbook.createFont();
		dataFont.setFontName(HEADING_FONT_TYPE);
		sheetdataStyle.setFont(dataFont);
		sheetdataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		sheetdataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		sheetdataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		sheetdataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		return sheetdataStyle;
	}

	protected void populateSheetHeading(XSSFSheet sheet, String sheetHeading, int numColumns, XSSFCellStyle sheetHeadingStyle) {
		XSSFRow row = sheet.createRow(SHEET_HEADING_ROWNUM);
		XSSFCell cell = row.createCell(0);
		cell.setCellStyle(sheetHeadingStyle);
		cell.setCellValue(sheetHeading);

		for (int i = 1; i < numColumns; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(sheetHeadingStyle);
		}

		// Create an empty row after the Heading
		row = sheet.createRow(SHEET_HEADING_ROWNUM + 1);
		for (int i = 0; i < numColumns; i++) {
			cell = row.createCell(i);
		}
	}

	protected void populateData(XSSFWorkbook workbook, XSSFSheet sheet, XSSFDrawing patriarch, ExcelSheetDataBean sheetDataBean, XSSFCellStyle sheetHeaderStyle, XSSFCellStyle sheetdataStyle ){
		if (sheetDataBean == null || sheetDataBean.getRowDataBeanList() == null || sheetDataBean.getRowDataBeanList().size() <= 0) {
			LOG.info("ExcelUtil.populateData: Sheet Data is not populated");
			return;
		}

		int numOfColumns = populateColumnHeading(workbook,sheet, sheetDataBean.getColumnHeadings(), sheetHeaderStyle);
		if (numOfColumns == -1) {
			LOG.info("ExcelUtil.populateData: Columns Heading could not be drawn");
			return;
		}
		populateRowData(workbook, sheet, patriarch, sheetDataBean.getRowDataBeanList(), sheetDataBean.getColumnHeadings(), sheetdataStyle);
	}
	
	protected int populateColumnHeading(XSSFWorkbook wb,XSSFSheet sheet, Map<?, ?> columnHeadings, XSSFCellStyle sheetHeaderStyle){
		if (columnHeadings == null) {
			LOG.info("ExcelUtil.populateColumnHeading: Data not present for first row");
			return -1;
		}

		XSSFRow row = sheet.createRow(COLUMN_HEADING_ROWNUM);
		Iterator<?> headings = columnHeadings.values().iterator();

		int i = 0;
		while (headings.hasNext()) {
			XSSFCell cell = row.createCell(i);
			cell.setCellStyle(sheetHeaderStyle);
			cell.setCellValue((String) headings.next());
			i++;
		}
		return columnHeadings.size();
	}

	protected void populateRowData(XSSFWorkbook wb, XSSFSheet sheet, XSSFDrawing patriarch, List<?> rowDataBeanList, Map<?, ?> columnHeadings,XSSFCellStyle sheetdataStyle) {
		int numRows = rowDataBeanList.size();
		int z =0;
		for (int i = 0; i < numRows; i++) {
			XSSFRow row = sheet.createRow((DATA_START_ROWNUM + z) + i);			
			Object rowDataObj = rowDataBeanList.get(i);

			Iterator<?> headings = columnHeadings.keySet().iterator();

			int j = 0;
			int counter = 0;

			while(headings.hasNext()){
				String fieldName = (String)headings.next();
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Method method = null;
				try {
					method = rowDataObj.getClass().getMethod(getMethodName);
				} catch (SecurityException e) {
					LOG.error(e);
				} catch (NoSuchMethodException e) {
					LOG.error(e);
				}

				XSSFCell cell = row.createCell(j);		
				cell.setCellStyle(sheetdataStyle);
				try {
					Object obj = method.invoke(rowDataObj);
					if (obj instanceof Integer) {
						cell.setCellValue((Integer)obj);
					} else if(obj instanceof Short){
						cell.setCellValue((Short)obj);
					} else if(obj instanceof Float){
						cell.setCellValue((Float)obj);
					} else if(obj instanceof Double){
						cell.setCellValue((Double)obj);
					} else if(obj instanceof Long){
						cell.setCellValue((Long)obj);
					}else if(obj instanceof Date){
						cell.setCellValue((String)PlatformUtil.format((Date)obj, PlatformUtil.DATE_SHORT_TIME));
					}else{
						cell.setCellValue((String)obj);
						cell.setCellStyle(sheetdataStyle);
					}
				} catch (IllegalArgumentException e1) {
					LOG.error(e1);
				} catch (IllegalAccessException e1) {
					LOG.error(e1);
				} catch (InvocationTargetException e1) {
					LOG.error(e1);
				} j++;

			} z = z+counter;		
		}		
	}

	protected void writeWorkbook(XSSFWorkbook wb, OutputStream out) {
		try {
			wb.write(out);
			out.flush();
			out.close();
		} catch (IOException ioe) {
			LOG.error("ExcelUtils.generateExcel: File could not be created at provided path");
			return;
		}
	}

}
