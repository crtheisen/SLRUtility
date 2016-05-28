# SLRUtility
SLR helper scripts. Original version by jtking. Reworked for an attack surface SLR.

Notes from Jason below (should be rewritten in another voice)

## search.py

```
usage: search.py [-h] [-s {ieee,acm,springer}] search result

Script to search results exported from scientific indexing services to
identify the recall of certain literary works.

positional arguments:
  search                Path to the file containing the title of the literary
                        works to be searched.
  result                Path to the file containing the exported results.

optional arguments:
  -h, --help            show this help message and exit
  -s {ieee,acm,springer}
                        The indexing service from which the results were
                        exported. Default is "default".
```

## parse.py

```
usage: parse.py [-h] [-b {phantom,firefox}] [--start START] [--stop STOP]
                {ieee,acm,springer} url output

Script to scrape search results from indexing services.

positional arguments:
  {ieee,acm,springer}   The indexing service from which the results are to be
                        parsed.
  url                   The URL of the search results. Use {} as the
                        placeholder for page number.
  output                Path to the file to which the parse results should be
                        written.

optional arguments:
  -h, --help            show this help message and exit
  -b {phantom,firefox}  The browser to use when retrieving the search results.
  --start START         Index of the page of results to start parsing from.
  --stop STOP           Index of the page of results to stop parsing to.
```

##ACMDownloader:
Uses Selenium to open a web browser and wait for you to enter search criteria into the advanced ACM search page

Automatically pulls the text from each page of ACM results and puts it in a single .txt file

I think every other database besides ACM had some sort of mass export citation feature.

##SLRHelper:

Citation Loaders -- these will each load citations from whatever was exported from each of the database searches. For example, for the .txt file saved by the ACMDownloader, the citation loader pulls out each field (title, authors, year, publication, etc.) and creates a Citation object. 

I have functionality to combine all the Citation objects into a single file.

I have functionality to calculate frequencies of terms and phrases based on titles, abstracts, and keywords of the total set of citations of the quasi-gold standard for identifying search terms for the final search query.

I also have functionality to compare search results to the quasi gold standard papers (http://dl.acm.org/citation.cfm?id=1968314). This is the main reason I created the script, because I had to go through 14 rounds of tweaking my search terms to get a good enough search query based on the quasi-sensitivty metric! And I was NOT going to do that manually each time.

The input and output directories show you the format of the input and output. For the input, I don't think I changed anything from the default exported set of citations from the databases.
