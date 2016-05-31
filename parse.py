import argparse
import csv
import sys

from selenium import webdriver
from selenium.webdriver.common.keys import Keys

from logger import *
from parser import Parser


def parse(service, url, start, stop, browser):
    results = list()

    if '{}' not in url:
        warning('URL does not have a placeholder for page number.')

    try:
        parser = Parser(service, browser)
        parser.setup()

        header = parser.get_header()
        if header:
            results.append(header)

        index = 1
        for page in range(start, stop + 1):
            results += parser.parse(url.format(page))
            info('{} results after {} page(s)'.format(len(results) - 1, index))
            index += 1
    except KeyboardInterrupt:
        sys.stdout.write('\r')
        info('Exiting...')
    finally:
        parser.teardown()

    return results


if __name__ == '__main__':
    parser = argparse.ArgumentParser(
            description=(
                'Script to scrape search results from indexing services.'
            )
        )
    parser.add_argument(
            '-b', dest='browser', default='firefox',
            choices=['phantom', 'firefox'],
            help='The browser to use when retrieving the search results.'
        )
    parser.add_argument(
            '--start', dest='start', type=int, default=1,
            help='Index of the page of results to start parsing from.'
        )
    parser.add_argument(
            '--stop', dest='stop', type=int, default=1,
            help='Index of the page of results to stop parsing to.'
        )
    parser.add_argument(
            'service',
            choices=['acm', 'dtic', 'ieee', 'proquest', 'springer', 'scholar'],
            help=(
                'The indexing service from which the results are to be parsed.'
            )
        )
    parser.add_argument(
            'url',
            help=(
                'The URL of the search results. Use {} as the placeholder for '
                'page number.'
            )
        )
    parser.add_argument(
            'output', help=(
                'Path to the file to which the parse results should be '
                'written.'
            )
        )
    args = parser.parse_args()

    info('Parsing {}'.format(args.url))
    results = parse(
            args.service, args.url, args.start, args.stop, args.browser
        )
    if results:
        with open(args.output, 'w') as file_:
            writer = csv.writer(file_)
            writer.writerows(results)
    info('Results written to {}'.format(args.output))
