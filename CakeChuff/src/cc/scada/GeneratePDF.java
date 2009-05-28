package cc.scada;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

/**
 * PDF generator for the CakeChuff system reports 
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class GeneratePDF {
	
	private String procesed_packs;
	private String faulty_packs;
	private String total_ok_cakes;
	private String total_ko_cakes;
	private String starts;
	private String stops;
	private String emergency;
	
	private String path; 
	
	  /**
	   * Constructor
	   * Set the values of the report
	   * @param path Path of the file to be generated
	   * @param p Number of processed packets
	   * @param f Number of faulty packets
	   * @param ok Number of total OK cakes
	   * @param ko Number of total KO cakes
	   * @param sts Number of starts
	   * @param stp Number of stops
	   * @param e Number of emergency stops
	   */
	public GeneratePDF (String path, String p, String f, String ok, String ko, String sts, String stp, String e){
		this.procesed_packs = p;
		this.faulty_packs = f;
		this.total_ok_cakes =ok;
		this.total_ko_cakes =ko;
		this.starts =sts;
		this.stops =stp;
		this.emergency = e;	
		
		this.path = path;
	}

	/**
	 * Load the PDF model to be generated
	 * Remove the last PDF report generated if named in the same way
	 * @return true If it is ready to generate the PDF report
	 */
	public boolean generate() {
		
		boolean success = true; 

		if(!createPdf(path + "1.pdf")){
			success = false;
		}else{

			try {
				// we create a PdfReader object
				PdfReader reader = new PdfReader(path + "1.pdf");
				PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(path + ".pdf"));
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
				success = false;
			} catch (DocumentException e) {
				success = false;
			}
		}
		
		File fichero = new File(path + "1.pdf");

		if (!fichero.delete())
			success = false;
		
		return success;

	}

	/**
	 * Generate the PDF report from the loaded model
	 * @param filename Name of the file to be generated
	 * @return true If the PDF report is generated correctly
	 */
	private boolean createPdf(String filename) {
		boolean success = true; 
		
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
			success = false;
		} catch (IOException ioe) {
			success = false;
		}
		document.close();
		
		return success;
	}
}