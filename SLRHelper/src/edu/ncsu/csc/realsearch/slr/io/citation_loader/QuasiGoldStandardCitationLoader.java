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


public class QuasiGoldStandardCitationLoader extends CitationLoader {

	public QuasiGoldStandardCitationLoader(String f)
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
		for(int i = 1; i < lastRow; i++)
		{
			Row r = s.getRow(i);
			String title = r.getCell(2).getRichStringCellValue().toString();
			String keywords = "";
			if(r.getCell(3) != null)
			{
				keywords = r.getCell(3).getRichStringCellValue().toString();
			}
			String abstractText = r.getCell(4).getRichStringCellValue().getString();
			
			Citation c = new Citation(CitationManager.getNextID());
			c.setTitle(title);
			c.addKeyword(keywords);
			c.setAbstractText(abstractText);
			addCitation(c);		
		}
		printSummary();
	}

}
