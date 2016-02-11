package edu.ncsu.csc.realsearch.slr.data_manager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.ncsu.csc.realsearch.slr.data.Citation;

public class QuasiGoldStandardComparer {
	
	private List<Citation> goldStandardList;
	private List<Citation> resultsList;
	
	private final String OUTPUT_FILE = "output/quasiGoldStandardMarked.xlsx";

	public QuasiGoldStandardComparer()
	{
		goldStandardList = new LinkedList<Citation>();
		resultsList = new LinkedList<Citation>();
	}
	
	public void loadQuasiGoldStandard(List<Citation> list)
	{
		goldStandardList = new LinkedList<Citation>(list);
	}
	
	public void loadSearchResults(List<Citation> list)
	{
		resultsList = new LinkedList<Citation>(list);
	}
	
	public void compare()
	{
		for(Citation gsC : goldStandardList)
		{
			for(Citation rC : resultsList)
			{
				if(gsC.getTitle().trim().equalsIgnoreCase(rC.getTitle().trim()))
				{
					System.out.println("POSSIBLE MATCH! " + rC.getSource() + " " + gsC.getTitle() + " | " + rC.getTitle());
					rC.setInQuasiGoldStandard(true);
				}
			}
		}
		Collections.sort(resultsList);
		
		try {
			outputCombinedCitations();
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
	
	public void outputCombinedCitations() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		FileOutputStream fout = new FileOutputStream(OUTPUT_FILE);
		Workbook wb = new XSSFWorkbook();
		
		Sheet s = wb.createSheet();
		
		Row r = s.createRow(0);
		r.createCell(0).setCellValue("QuasiGoldStandard");
		r.createCell(1).setCellValue("CitationID");
		r.createCell(2).setCellValue("Source");
		r.createCell(3).setCellValue("Title");
		r.createCell(4).setCellValue("Authors");
		r.createCell(5).setCellValue("Keywords");
		r.createCell(6).setCellValue("Abstract");
		r.createCell(7).setCellValue("DOI");
		r.createCell(8).setCellValue("Venue");
		r.createCell(9).setCellValue("Year");
		
		for(int i = 1; i <= resultsList.size(); i++)
		{
			r = s.createRow(i);
			r.createCell(0).setCellValue(resultsList.get(i-1).isInQuasiGoldStandard());
			r.createCell(1).setCellValue(resultsList.get(i-1).getId());
			r.createCell(2).setCellValue(resultsList.get(i-1).getSource());
			r.createCell(3).setCellValue(resultsList.get(i-1).getTitle());
			r.createCell(4).setCellValue(resultsList.get(i-1).getAuthorList().toString());
			r.createCell(5).setCellValue(resultsList.get(i-1).getKeywords().toString());
			r.createCell(6).setCellValue(resultsList.get(i-1).getAbstractText());
			r.createCell(7).setCellValue(resultsList.get(i-1).getDoi());
			r.createCell(8).setCellValue(resultsList.get(i-1).getVenue());
			r.createCell(9).setCellValue(resultsList.get(i-1).getYear());
		}
		
		wb.write(fout);
		fout.close();
		wb.close();
	}
}
