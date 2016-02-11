package edu.ncsu.csc.realsearch.slr.io.citation_loader;

import java.io.FileNotFoundException;

import edu.ncsu.csc.realsearch.slr.data.Citation;
import edu.ncsu.csc.realsearch.slr.data_manager.CitationManager;

public abstract class CitationLoader {
	
	private CitationManager citationManager;
	protected String fileName;
	private int count;
	
	public CitationLoader(String file)
	{
		count = 0;
		fileName = file;
		citationManager = CitationManager.getInstance();
	}
	
	public abstract void parseCitationFile() throws FileNotFoundException;
	
	public void addCitation(Citation c)
	{
		String source = getClass().getSimpleName();
		source = source.replaceAll("CitationLoader","");
		c.setSource(source);
		count++;
		System.out.println("   ADDING from " + getClass().getName()+ ": " + count + " " + c.getId() + " " + c.getTitle());
		citationManager.addCitation(c);
	}
	
	public void printSummary()
	{
		System.out.println(">>LOADED " + count + " citations from " + getClass().getName());
	}
}
