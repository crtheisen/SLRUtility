package edu.ncsu.csc.realsearch.slr.io.citation_loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc.realsearch.slr.data.Citation;
import edu.ncsu.csc.realsearch.slr.data_manager.CitationManager;

public class ScienceDirectCitationLoader extends CitationLoader {
	
	public ScienceDirectCitationLoader(String f)
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
			
			String temp = line.substring(0,2);
			String prefix = "";
			String content = "";
		//	System.out.println("LINE: " + line);
			if(temp.matches("[A-Z]{1}[A-Z0-9]{1}"))
			{
				prefix = line.substring(0, 2);
				content = line.substring(6,line.length());
			}
			else
			{
				prefix = "AB";
				content = line;
			}
			
			switch(prefix)
			{
				case "TY":
					if(c != null)
					{
						addCitation(c);
					}
					c = new Citation(CitationManager.getNextID());
					break;
					
				case "JO":
	//				System.out.println("\tVENUE: " + content);
					c.setVenue(content);
					break;
					
				case "AU":
	//				System.out.println("\tAUTHOR: " + content);
					c.addAuthor(content);
					break;
					
				case "KW":
	//				System.out.println("\tKEYWORD: " + content);
					c.addKeyword(content);
					break;
					
				case "TI":
				case "T1":
	//				System.out.println("TITLE: " + content);
					c.setTitle(content);
					break;
					
				case "PY":
	//				System.out.println("\tYEAR: " + content);
					if(content.length() > 4)
					{
						content = content.substring(0,4);
					}
					if(content.length() == 0)
					{
						content = "0000";
					}
					c.setYear(Integer.parseInt(content));
					break;
					
				case "DO":
	//				System.out.println("\tDOI: " + content);
					c.setDoi(content);
					break;
					
				case "AB":
	//				System.out.println("ABSTRACT: " + c.getAbstractText() + " " + content);
					c.setAbstractText(c.getAbstractText()+" "+content);
					break;
			}
		}
		addCitation(c);
		printSummary();
		
	}

	
}
