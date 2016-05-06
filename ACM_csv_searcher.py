import argparse
import csv
import sys

sys.dont_write_bytecode = True


TITLE = {'ieee': 0, 'acm': 6, 'springer': 0, 'default': 0}
SKIP = {'ieee': 2, 'acm': 1, 'springer': 1, 'default': 0}


def main(search, result):
    for title in search:
        pretty = (title[:75] + '...') if len(title) > 75 else title
        if title.lower() in result:
            print('\033[92m✓\033[0m {}'.format(pretty))
        else:
            print('\033[91m✘\033[0m {}'.format(pretty))


def _read(file_, service='default'):
    titles = None

    for _ in range(SKIP[service]):
        next(file_)
    reader = csv.reader(file_)
    titles = [row[TITLE[service]] for row in reader]

    return titles


if __name__ == '__main__':
    parser = argparse.ArgumentParser(
            description=(
                'Script to search results exported from scientific indexing '
                'services to identify the recall of certain literary works.'
            )
        )
    parser.add_argument(
            '-s', dest='service', default='default',
            choices=['ieee', 'acm', 'springer'],
            help=(
                'The indexing service from which the results were exported. '
                'Default is "default".'
            )
        )
    parser.add_argument(
            'search', type=argparse.FileType('r'),
            help=(
                'Path to the file containing the title of the literary works '
                'to be searched.'
            )
        )
    parser.add_argument(
            'result', type=argparse.FileType('r'),
            help='Path to the file containing the exported results.'
        )
    args = parser.parse_args()

    search = _read(args.search)
    result = _read(args.result, args.service)
    result = [title.lower() for title in result]

    main(search, result)
