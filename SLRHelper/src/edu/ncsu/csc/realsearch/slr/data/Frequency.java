package edu.ncsu.csc.realsearch.slr.data;

public class Frequency implements Comparable<Frequency> {
	private String term;
	private long termFrequency;
	private long documentFrequency;
	
	public Frequency(String t, long tf, long doc)
	{
		term = t;
		termFrequency = tf;
		documentFrequency = doc;
	}
	
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public long getTermFrequency() {
		return termFrequency;
	}
	public void setTermFrequency(long termFrequency) {
		this.termFrequency = termFrequency;
	}
	public long getDocumentFrequency() {
		return documentFrequency;
	}
	public void setDocumentFrequency(long documentFrequency) {
		this.documentFrequency = documentFrequency;
	}

	@Override
	public int compareTo(Frequency arg0) {
		if(this.term.equals(arg0.term))
		{
			return 0;
		}
		if(this.termFrequency != arg0.termFrequency)
		{
			if(this.termFrequency < arg0.termFrequency)
			{
				return -1;
			} else
			{
				return 1;
			}
		}
		if(this.documentFrequency != arg0.documentFrequency)
		{
			if(this.documentFrequency < arg0.documentFrequency)
			{
				return -1;
			} else
			{
				return 1;
			}
		}
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((term == null) ? 0 : term.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Frequency other = (Frequency) obj;
		if (term == null) {
			if (other.term != null)
				return false;
		} else if (!term.equals(other.term))
			return false;
		return true;
	}
	
	
	
	
	
}
