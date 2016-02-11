package edu.ncsu.csc.realsearch.slr.data;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Citation implements Comparable<Citation> {

	private long id;
	private String source;
	private String title;
	private List<String> authorList;
	private List<String> keywordList;
	private String abstractText;
	private String doi;
	private String venue;
	private int year;
	private boolean inQuasiGoldStandard;
	
	public Citation(long i)
	{
		id = i;
		authorList = new LinkedList<String>();
		keywordList = new LinkedList<String>();
		abstractText = "";
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getAuthorList() {
		return new LinkedList<String>(authorList);
	}
	public void addAuthor(String a) {
		authorList.add(a);
	}
	public List<String> getKeywords() {
		return keywordList;
	}
	public void addKeyword(String k) {
		keywordList.add(k);
	}
	public String getAbstractText() {
		return abstractText;
	}
	public void setAbstractText(String abstractText) {
		this.abstractText = abstractText;
	}
	public String getDoi() {
		return doi;
	}
	public void setDoi(String doi) {
		this.doi = doi;
	}
	public String getVenue() {
		return venue;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "Citation [id=" + id + ", title=" + title + ", authorList="
				+ authorList + ", doi=" + doi + ", venue=" + venue + ", year="
				+ year + "]";
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isInQuasiGoldStandard() {
		return inQuasiGoldStandard;
	}

	public void setInQuasiGoldStandard(boolean inQuasiGoldStandard) {
		this.inQuasiGoldStandard = inQuasiGoldStandard;
	}

	@Override
	public int compareTo(Citation other) {
		String bool = "b";
		String bool2 = "b";
		if(inQuasiGoldStandard)
		{
			bool = "a";
		}
		if(other.inQuasiGoldStandard)
		{
			bool2 = "a";
		}
		
		if(!bool.equals(bool2))
		{
			return bool.compareTo(bool2);
		}
		return title.compareTo(other.title);
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Citation))
			return false;
		Citation other = (Citation) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else
		{
			List<String> thisTitleTokens = new LinkedList<String>();
			List<String> otherTitleTokens = new LinkedList<String>();
			
			Scanner scan = new Scanner(title);
			while(scan.hasNext()){
				thisTitleTokens.add(scan.next());
			}
			
			Scanner scan2 = new Scanner(other.getTitle());
			while(scan2.hasNext()){
				otherTitleTokens.add(scan2.next());
			}
			
			if(thisTitleTokens.size() != otherTitleTokens.size())
			{
				return false;
			}
			
			for(int i = 0; i < thisTitleTokens.size(); i++){
				if(0 != thisTitleTokens.get(i).compareToIgnoreCase(otherTitleTokens.get(i)))
				{
					return false;
				}
			}
			
			
		}
		return true;
	}
	
	
	
	
	
	
}
