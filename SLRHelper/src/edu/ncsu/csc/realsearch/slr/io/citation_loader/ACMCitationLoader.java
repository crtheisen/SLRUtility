package edu.ncsu.csc.realsearch.slr.io.citation_loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc.realsearch.slr.data.Citation;
import edu.ncsu.csc.realsearch.slr.data_manager.CitationManager;

public class ACMCitationLoader extends CitationLoader {
	
	public ACMCitationLoader(String f)
	{
		super(f);
	}

	@Override
	public void parseCitationFile() throws FileNotFoundException
	{
		Scanner scan = new Scanner(new File(fileName));
		Citation c = null;
		
		String line = scan.nextLine();
		while(scan.hasNext())
		{
			//System.out.println("LINE: " + line);
			line = line.trim();
			
			if(!line.matches("[\\d]+"))
			{
				line = scan.nextLine();
			}
			else
			{
				c = new Citation(CitationManager.getNextID());
				
				line = scan.nextLine().trim();
				while(line.length() == 0)
				{
					line = scan.nextLine().trim();
				}
				
			//	System.out.println("TITLE: " + line);
				c.setTitle(line);
				
				line = scan.nextLine().trim();
				
				String authorCheck = line.substring(line.length()-4,line.length());
				if(authorCheck.matches("[\\d]{4}"))
				{
					// No Author
					c.setYear(Integer.parseInt(authorCheck));
				}
				else
				{
			//		System.out.println("AUTHOR: " + line);
					c.addAuthor(line);
					
					line = scan.nextLine().trim();
				//	System.out.println("YEAR: " + line);
					c.setYear(Integer.parseInt(line.substring(line.length()-4,line.length())));
				}
				
				line = scan.nextLine().trim();
			//	System.out.println("VENUE: " + line);
				c.setVenue(line);
				
				line = scan.nextLine();
				while(!line.contains("Bibliometrics:"))
				{
					line = scan.nextLine();
				}
				
				line = scan.nextLine().trim();
				if(line.matches("[\\d]+"))
				{
					addCitation(c);
					continue;
				}
				while(line.length() == 0)
				{
					line = scan.nextLine();
				}
				
				if(!line.contains("Keywords:"))
				{
			//		System.out.println("ABSTRACT: " + line);
					c.setAbstractText(line);
				}
				if(!scan.hasNext())
				{
					addCitation(c);
					break;
				}
				line = scan.nextLine();
				if(line.matches("[\\d]+"))
				{
					addCitation(c);
					continue;
				}
				while(!line.contains("Keywords:"))
				{
					line = scan.nextLine();
					if(line.matches("[\\d]+"))
					{
						addCitation(c);
						continue;
					}
				}
				
			//	System.out.println("KEYWORDS: " + line);
				c.addKeyword(line.replaceAll("Keywords:",""));
				
				addCitation(c);
				while(scan.hasNext() && !line.matches("[\\d]+"))
				{
					line = scan.nextLine();
				}
			}
		}
		printSummary();
		
	}
	
	 

}
