# SLRUtility
These scripts are designed to help researchers perform systematic literature reviews (SLRs). These scripts are domain-agnostic, so anyone who would like to systematically explore literature in an area can use them.

## Dependencies

These scripts are written in Python 3. They require Selenium and termcolor to run.

## agregate.py

```
usage: aggregate.py [-h] directory output

Script to aggregate the search results from multiple academic literature
indexing services.

positional arguments:
  directory   Path to the directory containing the CSV files that inturn
              contain the results exported from the indexing services' search
              results.
  output      Path to the file to which the aggregated results should be
              written.

optional arguments:
  -h, --help  show this help message and exit
```

## search.py

```
usage: search.py [-h] [-s {ieee,acm,springer,dtic,proquest,scholar}] search result

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
                {ieee,acm,springer,dtic,proquest,scholar} url output

Script to scrape search results from indexing services.

positional arguments:
  {ieee,acm,springer,     The indexing service from which the results are to be
  dtic,proquest,scholar}  parsed.
  url                     The URL of the search results. Use {} as the
                          placeholder for page number.
  output                  Path to the file to which the parse results should be
                          written.

optional arguments:
  -h, --help            show this help message and exit
  -b {phantom,firefox}  The browser to use when retrieving the search results.
  --start START         Index of the page of results to start parsing from.
  --stop STOP           Index of the page of results to stop parsing to.
```
