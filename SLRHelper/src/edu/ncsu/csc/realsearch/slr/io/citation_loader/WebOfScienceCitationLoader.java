package edu.ncsu.csc.realsearch.slr.io.citation_loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc.realsearch.slr.data.Citation;
import edu.ncsu.csc.realsearch.slr.data_manager.CitationManager;

public class WebOfScienceCitationLoader extends CitationLoader {

	public WebOfScienceCitationLoader(String f)
	{
		super(f);
	}
	
	@Override
	public void parseCitationFile() throws FileNotFoundException
	{
		Scanner scan = new Scanner(new File(fileName));
		Citation c = null;
		
		while(scan.hasNext())
		{
			String line = scan.nextLine();
			if(line.length() < 5)
			{
				continue;
			}
			
			String prefix = "";
			if(!line.contains("{"))
			{
				continue;
			} else
			{
				prefix = line.substring(0, line.indexOf("{"));
			}
			
			String content = "";
			while(!line.contains("article") && !line.contains("}"))
			{
				line += scan.nextLine();
			}
			content = line.substring(line.indexOf("{")+1,line.length());
			
			switch(prefix)
			{
				case "@article":
				case "@inproceedings":
					if(c != null)
					{
						addCitation(c);
					}
					c = new Citation(CitationManager.getNextID());
					break;
					
				case "Journal = ":
					c.setVenue(clean(content));
					break;
					
				case "Author = ":
					c.addAuthor(clean(content));
					break;
					
				case "Keywords = ":
					c.addKeyword(clean(content));
					break;
					
				case "Title = ":
					c.setTitle(clean(content));
					break;
					
				case "Year = ":
					c.setYear(Integer.parseInt(clean(content)));
					break;
					
				case "DOI = ":
					c.setDoi(clean(content));
					break;
					
				case "Abstract = ":
					c.setAbstractText(clean(content));
					break;
			}
		}
		addCitation(c);
		printSummary();
		
	}
	
	private String clean(String s)
	{
		String temp = s.replaceAll("\\{","");
		temp = temp.replaceAll("\\}", "");
		temp = temp.replaceAll(",","");
		return temp;
	}

	
}
