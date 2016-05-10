package edu.ncsu.csc.realsearch.slr.io.citation_loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import edu.ncsu.csc.realsearch.slr.data.Citation;
import edu.ncsu.csc.realsearch.slr.data_manager.CitationManager;


public class SpringerLinkCitationLoader extends CitationLoader {
	
	public SpringerLinkCitationLoader(String f)
	{
		super(f);
	}

	@Override
	public void parseCitationFile() throws FileNotFoundException
	{
		Workbook wb;
		try {
			wb = WorkbookFactory.create(new File(fileName));
			Sheet s = wb.getSheetAt(0);
			parseSheet(s);
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void parseSheet(Sheet s)
	{
		int lastRow = s.getLastRowNum();
		for(int i = 1; i <= lastRow; i++)
		{
			Row r = s.getRow(i);
			String title = r.getCell(0).getRichStringCellValue().toString();
			String venue = r.getCell(1).getRichStringCellValue().toString();
			String doi = r.getCell(5).getRichStringCellValue().toString();
			Cell ce = r.getCell(6);
			String authors = "";
			if(ce != null)
			{
				authors = ce.getStringCellValue().toString();
			}
			Double year = r.getCell(7).getNumericCellValue();
			
			Citation c = new Citation(CitationManager.getNextID());
			c.setTitle(title);
			c.setVenue(venue);
			c.setDoi(doi);
			c.addAuthor(authors);
			c.setYear(((int)(year+1-1)));
			addCitation(c);		
		}
		printSummary();
	}

}