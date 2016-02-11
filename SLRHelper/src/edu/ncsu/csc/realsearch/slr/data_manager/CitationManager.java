package edu.ncsu.csc.realsearch.slr.data_manager;

import java.util.LinkedList;
import java.util.List;

import edu.ncsu.csc.realsearch.slr.data.Citation;

public class CitationManager {

	private static List<Citation> citationList;
	private static CitationManager manager;
	private static long nextID;
	
	private CitationManager()
	{
		citationList = new LinkedList<Citation>();
		nextID = 1;
	}
	
	public static CitationManager getInstance()
	{
		if(manager == null)
		{
			return new CitationManager();
		}
		return manager;
	}
	
	public static void addCitation(Citation c)
	{
		citationList.add(c);
		nextID++;
	}
	
	public static long getNextID()
	{
		return nextID;
	}
	
	public static void printCitationList()
	{
		for(Citation c : citationList)
		{
			System.out.println(c);
		}
	}
	
	public static List<Citation> getCitationList()
	{
		return new LinkedList<Citation>(citationList);
	}
}
