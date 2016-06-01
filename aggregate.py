import argparse
import csv
import operator

from logger import *

TITLE = {
    'acm': 6, 'dtic': 0, 'ieee': 0, 'proquest': 0, 'scholar': 0, 'springer': 0
}
SKIP = {
    'acm': 1, 'dtic': 1, 'ieee': 2, 'proquest': 1, 'scholar': 1, 'springer': 1
}
LINK = {
    'acm': 6, 'dtic': 2, 'ieee': 15, 'proquest': 1, 'scholar': 4, 'springer': 8
}


def aggregate(directory):
    results = dict()
    for filename in os.listdir(directory):
        info('Processing {}'.format(filename))
        with open(os.path.join(directory, filename)) as file_:
            for metadata in _read(file_, os.path.splitext(filename)[0]):
                title = metadata['title']
                link = metadata['link']
                debug('{} {}'.format(title, link))
                if title not in results:
                    results[title] = link
        info('There are {} results so far'.format(len(results)))
    return results


def _read(file_, service='default'):
    metadata = None

    for _ in range(SKIP[service]):
        next(file_)
    reader = csv.reader(file_)
    metadata = [
            {'title': row[TITLE[service]], 'link': row[LINK[service]]}
            for row in reader
        ]
    return metadata


if __name__ == '__main__':
    parser = argparse.ArgumentParser(
            description=(
                'Script to aggregate the search results from multiple '
                'academic literature indexing services.'
            )
        )
    parser.add_argument(
            'directory',
            help=(
                'Path to the directory containing the CSV files that inturn '
                'contain the results exported from the indexing services\' '
                'search results.'
            )
        )
    parser.add_argument(
            'output', help=(
                'Path to the file to which the aggregated results should be '
                'written.'
            )
        )
    args = parser.parse_args()

    results = aggregate(args.directory)
    if results:
        with open(args.output, 'w') as file_:
            writer = csv.writer(file_)
            sorted_results = sorted(
                    results.items(), key=operator.itemgetter(0)
                )
            for (result, link) in sorted_results:
                writer.writerow((result, link))
    info('{} results written to {}'.format(len(results), args.output))
