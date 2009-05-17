package cc.scada;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

/**
 * Class to generate the pdf reports
 * Author: CakeChuff team
 */
public class GeneratePDF {
	
	private String procesed_packs;
	private String faulty_packs;
	private String total_ok_cakes;
	private String total_ko_cakes;
	private String starts;
	private String stops;
	private String emergency;
	
	public GeneratePDF (String p, String f, String ok, String ko, String sts, String stp, String e){
		this.procesed_packs = p;
		this.faulty_packs = f;
		this.total_ok_cakes =ok;
		this.total_ko_cakes =ko;
		this.starts =sts;
		this.stops =stp;
		this.emergency = e;		
	}


	public void generate() {

		createPdf("reports/CakeChuff Summary Report1.pdf");

		try {
			// we create a PdfReader object
			PdfReader reader = new PdfReader("reports/CakeChuff Summary Report1.pdf");
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
					"reports/CakeChuff Summary Report.pdf"));
			// we create an Image we'll use as a Watermark
			Image img = Image.getInstance("src/cc/images/logoCakeChuff.png");
			img.setAbsolutePosition(350, 500);
			// we create a Font for the text to add
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			// these are the canvases we are going to use
			PdfContentByte under, over;
			int total = reader.getNumberOfPages() + 1;
			for (int i = 1; i < total; i++) {
				under = stamper.getUnderContent(i);
				under.addImage(img);
				// text over the existing page
				over = stamper.getOverContent(i);
				over.beginText();
				over.setFontAndSize(bf, 18);
				over.setTextMatrix(30, 30);
				over.endText();
				over.setRGBColorStroke(0xFF, 0x00, 0x00);
				over.setLineWidth(5f);
				over.stroke();
			}
			stamper.close();
		} catch (IOException e) {
			System.out.println("To generate the report please, close the file \" CakeChuff Summary Report.pdf\" ");
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Generates a PDF file.
	 * 
	 * @param filename The name of the PDF file.
	 */
	private void createPdf(String filename) {
		// we create a document with multiple pages and bookmarks
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(filename));
			document.open();
			document.addAuthor("CakeChuff Team");
			document.addTitle("CakeChuff Summary Report");
			document.addCreator("CakeChuff");
			//each call to document.add add a new page to the document
			
			
			String title = "SUMMARY REPORT ABOUT THE RESULTS OF CAKECHUFF'S ACTIVITIES";
				
			Paragraph tit = new Paragraph(title, new Font(
					Font.HELVETICA, 20, Font.UNDERLINE));
			tit.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(tit);
			
			PdfSpotColor psc_cmyk = new PdfSpotColor("iTextSpotColorCMYK",
					0.9f, new CMYKColor(0.7f, .7f, 0.0f, 0.0f));
			SpotColor sc_cmyk = new SpotColor(psc_cmyk);


			String sectitle = "\n\n On Last Execution: ";
			document.add(new Paragraph(sectitle, new Font(
					Font.HELVETICA, 14, Font.ITALIC, sc_cmyk)));
			String text = "\n\t\t\t\t\t\t Processed Blisters: " + procesed_packs;
			document.add(new Paragraph(text));
			text = "\t\t\t\t\t\t Faulty Blisters: " + faulty_packs + "\n";
			document.add(new Paragraph(text));
			
			sectitle = "\n Since The First Startup: ";
			document.add(new Paragraph(sectitle, new Font(
					Font.HELVETICA, 14, Font.ITALIC, sc_cmyk)));
			text = "\n\t\t\t\t\t\t Total Number of Correct Cakes: " + total_ok_cakes;
			document.add(new Paragraph(text));
			text = "\t\t\t\t\t\t Total Number of Faulty Cakes: " + total_ko_cakes + "\n";
			document.add(new Paragraph(text));
			

			sectitle = "\n Information About Executions Since the Installation: ";
			document.add(new Paragraph(sectitle, new Font(
					Font.HELVETICA, 14, Font.ITALIC, sc_cmyk)));
			text = "\n\t\t\t\t\t\t Total Number of Starts: " + starts;
			document.add(new Paragraph(text));
			text = "\t\t\t\t\t\t Total Number of Stops: " + stops;
			document.add(new Paragraph(text));
			text = "\t\t\t\t\t\t Total Number of Emergency Stops: " + emergency ;
			document.add(new Paragraph(text));
			
			
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}
}