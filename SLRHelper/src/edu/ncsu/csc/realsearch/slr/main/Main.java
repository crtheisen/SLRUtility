package edu.ncsu.csc.realsearch.slr.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import edu.ncsu.csc.realsearch.slr.data.Frequency;
import edu.ncsu.csc.realsearch.slr.data_manager.CitationManager;
import edu.ncsu.csc.realsearch.slr.data_manager.QuasiGoldStandardComparer;
import edu.ncsu.csc.realsearch.slr.indexer.SimpleFileIndexer;
import edu.ncsu.csc.realsearch.slr.io.citation_loader.ACMCitationLoader;
import edu.ncsu.csc.realsearch.slr.io.citation_loader.CitationLoader;
import edu.ncsu.csc.realsearch.slr.io.citation_loader.EngineeringVillageCitationLoader;
import edu.ncsu.csc.realsearch.slr.io.citation_loader.IEEECitationLoader;
import edu.ncsu.csc.realsearch.slr.io.citation_loader.QuasiGoldStandardCitationLoader;
import edu.ncsu.csc.realsearch.slr.io.citation_loader.ScienceDirectCitationLoader;
import edu.ncsu.csc.realsearch.slr.io.citation_loader.SpringerLinkCitationLoader;
import edu.ncsu.csc.realsearch.slr.io.citation_loader.WebOfScienceCitationLoader;
import edu.ncsu.csc.realsearch.slr.io.output.CitationCombiner;
import edu.ncsu.csc.realsearch.slr.search.CitationSearch;
import edu.ncsu.csc.realsearch.slr.search.FrequencyAnalyzer;

public class Main {

	
	public static void main(String[] args)
	{		
		CitationManager cm = CitationManager.getInstance();
		CitationManager cmGS = CitationManager.getInstance();
		
		QuasiGoldStandardComparer gsCompare = new QuasiGoldStandardComparer();
		
		String round = "round14";
		
		List<CitationLoader> loaders = new LinkedList<CitationLoader>();
		loaders.add(new IEEECitationLoader("inputs/"+round+"/ieee"));
		loaders.add(new WebOfScienceCitationLoader("inputs/"+round+"/webOfScience.bib"));
		loaders.add(new ScienceDirectCitationLoader("inputs/"+round+"/science.txt"));
		loaders.add(new ACMCitationLoader("inputs/"+round+"/ACM.txt"));
		loaders.add(new SpringerLinkCitationLoader("inputs/"+round+"/springerLink.xlsx"));
		loaders.add(new EngineeringVillageCitationLoader("inputs/"+round+"/engineeringVillage.xlsx"));
		//loaders.add(new QuasiGoldStandardCitationLoader("inputs/goldStandard.xlsx"));
		
		try {
			for(CitationLoader l : loaders)
			{
				l.parseCitationFile();
			}
			//cm.printCitationList();
			
			CitationCombiner cb = new CitationCombiner();
			cb.loadCitations(cm.getCitationList());
			cb.outputCombinedCitations();
			cb.removeDuplicateCitations();
			gsCompare.loadSearchResults(cm.getCitationList());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		
		
		
		QuasiGoldStandardCitationLoader qgsLoader = new QuasiGoldStandardCitationLoader("inputs/goldStandard.xlsx");
		try {
			qgsLoader.parseCitationFile();
			gsCompare.loadQuasiGoldStandard(cmGS.getCitationList());
			gsCompare.compare();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		SimpleFileIndexer sfi = new SimpleFileIndexer();
//		try {
//			sfi.rebuildIndexes();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		try {
//			CitationSearch search = new CitationSearch();
//			search.performSearch("logging and audit", 50);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		FrequencyAnalyzer fa = new FrequencyAnalyzer();
//		fa.analyze();
	    
		
	}
}
