package edu.ncsu.csc.realsearch.slr.indexer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ncsu.csc.realsearch.slr.data.Citation;
import edu.ncsu.csc.realsearch.slr.data_manager.CitationManager;

public class SimpleFileIndexer {

	private Directory indexDir;
	private IndexWriterConfig config;

	private static IndexWriter indexWriter;
	
	private static String indexDirectory;
	
	public SimpleFileIndexer()
	{
		indexDirectory = "indexOutput/";
	}

	public static IndexWriter getInstance() throws IOException {
		if (indexWriter == null) {
			Directory indexDir = FSDirectory.open(new File(indexDirectory));
			
			StopAnalyzer snowballAnalyzer = new StopAnalyzer(Version.LUCENE_4_10_4);               
			ShingleAnalyzerWrapper shingleAnalyzer = new ShingleAnalyzerWrapper(snowballAnalyzer, 3);
			
			IndexWriterConfig config = new IndexWriterConfig(
					Version.LUCENE_4_10_4, shingleAnalyzer);
			indexWriter = new IndexWriter(indexDir, config);
		}
		return indexWriter;
	}

	public void rebuildIndexes() throws IOException {
		//
		// Erase existing index
		//
		getInstance();
		//
		// Index all hotel entries
		//
		List<Citation> cList = CitationManager.getCitationList();
		for (Citation c : cList) {
			indexCitation(c);
		}
		//
		// Don't forget to close the index writer when done
		//
		closeIndexWriter();
	}
	
	public void indexCitation(Citation c) throws IOException {        

        System.out.println("Indexing citation: " + c);
        IndexWriter writer = getInstance();
		Document doc = new Document();
		
		FieldType TYPE_STORED = new FieldType();
		TYPE_STORED.setIndexed(true);
        TYPE_STORED.setTokenized(true);
        TYPE_STORED.setStored(true);
        TYPE_STORED.setStoreTermVectors(true);
        TYPE_STORED.setStoreTermVectorPositions(true);
        TYPE_STORED.freeze();
		
		Field f = new Field("id",c.getId()+"", TYPE_STORED);
		doc.add(f);
		
		Field tf = new Field("title",c.getTitle()+"", TYPE_STORED);
		doc.add(tf);
		
		tf = new Field("authors",c.getAuthorList()+"", TYPE_STORED);
		doc.add(tf);
		
		tf = new Field("keywords",c.getKeywords()+"", TYPE_STORED);
		doc.add(tf);
		
		tf = new Field("abstract",c.getAbstractText()+"", TYPE_STORED);
		doc.add(tf);
		
		tf = new Field("DOI",c.getDoi()+"", TYPE_STORED);
		doc.add(tf);
		
		tf = new Field("year",c.getYear()+"", TYPE_STORED);
		doc.add(tf);
		
		tf = new Field("title-keyword-abstract",c.getTitle()+" " + c.getKeywords() + " " + c.getAbstractText(), TYPE_STORED);
		doc.add(tf);
      
        writer.addDocument(doc);
    }   

	public void closeIndexWriter() throws IOException {
		if (indexWriter != null) {
			indexWriter.close();
		}
	}

}
