package edu.ncsu.csc.realsearch.slr.search;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;

public class CitationSearch {
    private IndexSearcher searcher = null;
    private QueryParser parser = null;

    /** Creates a new instance of SearchEngine */
    public CitationSearch() throws IOException
    {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("indexOutput"))));
        parser = new QueryParser("title-keyword-abstract", new StandardAnalyzer());
    }

    public void performSearch(String queryString, int n) throws IOException, ParseException
    {
        Query query = parser.parse(queryString);
        TopDocs td = searcher.search(query, n);
      	
		ScoreDoc[] hits = td.scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			int docId = hits[i].doc;
			Document d = getDocument(docId);
			System.out.println(hits[i].score + "-->" + queryString + "\t" + d.get("id") +" "+d.get("title"));
		}
    }

    public Document getDocument(int docId) throws IOException
    {
        return searcher.doc(docId);
    }
}
