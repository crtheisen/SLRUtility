package edu.ncsu.csc.realsearch.slr.io.output;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.ncsu.csc.realsearch.slr.data.Citation;

public class CitationCombiner {
	
	private final String OUTPUT_FILE = "output/combined.xlsx";
	private List<Citation> citationList;
	
	public CitationCombiner()
	{
		citationList = new LinkedList<Citation>();
	}
	
	public void removeDuplicateCitations() throws IOException
	{
		int duplicateCounter = 0;
		List<Citation> singleList = new LinkedList<Citation>();
		for(Citation c : citationList)
		{
			if(!singleList.contains(c))
			{
				singleList.add(c);
			}
			else
			{
				duplicateCounter++;
			}
		}
		
		System.out.println("DUPLICATES: " + duplicateCounter);
		
		outputCitations(singleList, "output/combined_duplicatesRemoved.xlsx");
	}
	
	private void outputCitations(List<Citation> cList, String fileName) throws IOException
	{
		FileOutputStream fout = new FileOutputStream(fileName);
		Workbook wb = new XSSFWorkbook();
		
		Sheet s = wb.createSheet();
		
		Row r = s.createRow(0);
		r.createCell(0).setCellValue("CitationID");
		r.createCell(1).setCellValue("Source");
		r.createCell(2).setCellValue("Title");
		r.createCell(3).setCellValue("Authors");
		r.createCell(4).setCellValue("Keywords");
		r.createCell(5).setCellValue("Abstract");
		r.createCell(6).setCellValue("DOI");
		r.createCell(7).setCellValue("Venue");
		r.createCell(8).setCellValue("Year");
		
		for(int i = 1; i <= cList.size(); i++)
		{
			r = s.createRow(i);
			r.createCell(0).setCellValue(cList.get(i-1).getId());
			r.createCell(1).setCellValue(cList.get(i-1).getSource());
			r.createCell(2).setCellValue(cList.get(i-1).getTitle());
			r.createCell(3).setCellValue(cList.get(i-1).getAuthorList().toString());
			r.createCell(4).setCellValue(cList.get(i-1).getKeywords().toString());
			r.createCell(5).setCellValue(cList.get(i-1).getAbstractText());
			r.createCell(6).setCellValue(cList.get(i-1).getDoi());
			r.createCell(7).setCellValue(cList.get(i-1).getVenue());
			r.createCell(8).setCellValue(cList.get(i-1).getYear());
		}
		
		wb.write(fout);
		fout.close();
		wb.close();
	}

	public void outputCombinedCitations() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		outputCitations(citationList, OUTPUT_FILE);
	}
	
	public void loadCitations(List<Citation> list)
	{
		citationList = new LinkedList(list);
	}

	
}
