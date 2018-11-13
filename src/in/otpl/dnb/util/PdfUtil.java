package in.otpl.dnb.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfUtil extends AbstractITextPdfView {

	private static final Logger LOG = Logger.getLogger(PdfUtil.class);
	
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response){
		try{
			// get data model which is passed by the Spring container
			/*@SuppressWarnings("unchecked")
			List<User> listUsers = (List<User>) model.get("listUsers");
			document = new Document(PageSize.A3.rotate());
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=UserList.pdf" );
			PdfWriter.getInstance(document, response.getOutputStream());
			//document.add(new Paragraph("User List"));
*/

			PdfPTable table = new PdfPTable(7);
			table.setWidthPercentage(100.0f);
			table.setWidths(new float[] {2.0f, 1.0f, 1.0f, 1.0f, 2.0f,2.0f, 1.0f});
			table.setSpacingBefore(10);

			// define font for table header row
			Font font = FontFactory.getFont(FontFactory.HELVETICA);
			font.setColor(BaseColor.WHITE);

			// define table header cell
			PdfPCell cell = new PdfPCell();
			cell.setBackgroundColor(BaseColor.BLUE);
			cell.setPadding(7);

			// write table header
			cell.setPhrase(new Phrase("User Name", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Login Id", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Employee Number", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Status", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Email Address", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Mobile Number", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("User Type", font));
			table.addCell(cell);

			// write table row data
			/*for (User userDetails : listUsers) {
				table.addCell(userDetails.getName());
				table.addCell(userDetails.getLoginId());
				table.addCell(userDetails.getEmployeeNumber());
				table.addCell(String.valueOf(userDetails.getStatus()));
				table.addCell(userDetails.getEmailAddress());
				table.addCell(userDetails.getMobileNo());
				table.addCell(String.valueOf(userDetails.getUserTypeId()));
			}*/
			document.open();
			document.add(new Paragraph("User List"));
			document.add(table);
			document.close();

		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
	}

}

