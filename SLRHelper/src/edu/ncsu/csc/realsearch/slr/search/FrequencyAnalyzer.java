package edu.ncsu.csc.realsearch.slr.search;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.ncsu.csc.realsearch.slr.data.Frequency;

public class FrequencyAnalyzer {

	private List<Frequency> freqList;
	
	private final String OUTPUT_FILE = "output/freqAnalysis.xlsx";
	
	public FrequencyAnalyzer()
	{
		freqList = new LinkedList<Frequency>();
	}
	
	public void analyze() {
		try {

			freqList = new LinkedList<Frequency>();

			IndexReader reader = DirectoryReader.open(FSDirectory
					.open(new File("indexOutput")));

			Bits liveDocs = MultiFields.getLiveDocs(reader);
			for (int i = 0; i < reader.maxDoc(); i++) {
				if (liveDocs != null && !liveDocs.get(i)) {
					continue;
				}
				Document doc = reader.document(i);
				System.out.println("Processing file: " + doc.get("title"));

				Terms termVector = reader.getTermVector(i,
						"title-keyword-abstract");
				TermsEnum itr = termVector.iterator(null);
				BytesRef term = null;

				while ((term = itr.next()) != null) {
					String termText = term.utf8ToString();
					Term termInstance = new Term("title-keyword-abstract", term);
					long termFreq = reader.totalTermFreq(termInstance);
					long docCount = reader.docFreq(termInstance);

					Frequency tempFreq = new Frequency(termText, termFreq,
							docCount);
					if (freqList.contains(tempFreq)) {
						Frequency oldFreq = freqList.get(freqList
								.indexOf(tempFreq));
						Frequency newFreq = new Frequency(termText,
								oldFreq.getTermFrequency() + termFreq,
								oldFreq.getDocumentFrequency() + docCount);
						// freqList.remove(oldFreq);
						// freqList.add(newFreq);
					} else {
						freqList.add(tempFreq);
					}

					// System.out.println("term: "+termText+", termFreq = "+termFreq+", docCount = "+docCount);
				}
			}

			Collections.sort(freqList);
			Collections.reverse(freqList);

			for (Frequency f : freqList) {
				System.out.println("TERM: " + f.getTerm() + "\t\tTermFreq:"
						+ f.getTermFrequency() + "\tDocCount:"
						+ f.getDocumentFrequency());
			}

			reader.close();
			
			try {
				outputResults();
			} catch (EncryptedDocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void outputResults() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		FileOutputStream fout = new FileOutputStream(OUTPUT_FILE);
		Workbook wb = new XSSFWorkbook();
		
		Sheet s = wb.createSheet();
		
		Row r = s.createRow(0);
		r.createCell(0).setCellValue("Term");
		r.createCell(1).setCellValue("TermFrequency");
		r.createCell(2).setCellValue("DocumentCount");
		
		for(int i = 1; i <= freqList.size(); i++)
		{
			r = s.createRow(i);
			r.createCell(0).setCellValue(freqList.get(i-1).getTerm());
			r.createCell(1).setCellValue(freqList.get(i-1).getTermFrequency());
			r.createCell(2).setCellValue(freqList.get(i-1).getDocumentFrequency());
		}
		
		wb.write(fout);
		fout.close();
		wb.close();
	}

}
