package edu.ncsu.csc.realsearch.slr.io.citation_loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc.realsearch.slr.data.Citation;
import edu.ncsu.csc.realsearch.slr.data_manager.CitationManager;

public class IEEECitationLoader extends CitationLoader {
	
	public IEEECitationLoader(String f)
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
			String prefix = line.substring(0, 2);
			String content = line.substring(6,line.length());
			
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
					c.setVenue(content);
					break;
					
				case "AU":
					c.addAuthor(content);
					break;
					
				case "KW":
					c.addKeyword(content);
					break;
					
				case "TI":
					c.setTitle(content);
					break;
					
				case "PY":
					c.setYear(Integer.parseInt(content));
					break;
					
				case "DO":
					c.setDoi(content);
					break;
			}
		}
		addCitation(c);
		printSummary();
		
	}

	
}
