import re
import traceback

from selenium import webdriver
from selenium.common.exceptions import WebDriverException
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.ui import WebDriverWait

from logger import *

# Convenience Aliases
CLASS = By.CLASS_NAME
CSS = By.CSS_SELECTOR
ID = By.ID
TAG = By.TAG_NAME

# Date Parsing
DATE_RE = re.compile('(\d{4})-(\d{2})-(\d{2})')
YEAR_RE = re.compile('(\d{4})')


class Parser(object):
    def __init__(self, service, browser):
        self.service = service
        self.browser = browser
        self.driver = None

    def get_header(self):
        if self.service == 'acm':
            pass
        elif self.service == 'dtic':
            return ('title', 'year', 'link')
        elif self.service == 'ieee':
            pass
        elif self.service == 'proquest':
            return ('title', 'authors', 'venue', 'year', 'link')
        elif self.service == 'springer':
            return ('title', 'authors', 'venue', 'year', 'link')
        elif self.service == 'scholar':
            return ('title', 'authors', 'venue', 'year', 'link')
        else:
            return None

    def parse(self, url):
        debug('Parsing {}'.format(url))
        if self.service == 'acm':
            return self._parse_acm(url)
        elif self.service == 'dtic':
            return self._parse_dtic(url)
        elif self.service == 'ieee':
            return self._parse_ieee(url)
        elif self.service == 'proquest':
            return self._parse_proquest(url)
        elif self.service == 'springer':
            return self._parse_springer(url)
        elif self.service == 'scholar':
            return self._parse_google_scholar(url)

    def setup(self):
        self.driver = self._get_driver(self.browser)

    def teardown(self):
        if self.driver:
            self.driver.close()

    def _get_driver(self, browser):
        driver = None

        if browser == 'firefox':
            driver = webdriver.Firefox()
        elif browser == 'phantom':
            driver = webdriver.PhantomJS()
        else:
            error('Cannot create driver for browser {}'.format(browser))
            sys.exit(1)

        return driver

    def _parse_acm(self, url):
        raise NotImplementedError('Implementation does not exist.')

    def _parse_dtic(self, url):
        _results = list()

        try:
            self.driver.get(url)
            if self.driver.title == '':
                error('{} could not be retrieved.'.format(url))
                sys.exit(1)
            results = self.driver.find_element(ID, 'content-container1') \
                .find_element(TAG, 'ol')

            for result in results.find_elements(TAG, 'li'):
                title, year, link = '', None, ''
                title = result.find_element(CSS, 'a:first-child').text
                link = result.find_element(CSS, 'a:first-child')\
                    .get_attribute('href')

                for metadata in result.find_elements(CSS, 'font'):
                    match = DATE_RE.search(metadata.text)
                    if match:
                        year = match.group(1)

                _results.append((title, year, link))
        except WebDriverException:
            extype, exvalue, extrace = sys.exc_info()
            traceback.print_exception(extype, exvalue, extrace)

        return _results

    def _parse_ieee(self, url):
        raise NotImplementedError('Implementation does not exist.')

    def _parse_proquest(self, url):
        _results = list()

        try:
            self.driver.get(url)
            results = self.driver.find_element(CLASS, 'resultItems')
            for result in results.find_elements(CSS, 'li.resultItem'):
                title, authors, venue, year, link = '', '', '', None, ''

                title = result.find_element(CLASS, 'previewTitle').text
                debug(title)
                link = result.find_element(CLASS, 'previewTitle') \
                    .get_attribute('href')
                elms = result.find_element(CSS, 'div.contentArea') \
                    .find_element(CSS, 'div.results_list_copy') \
                    .find_elements(CSS, 'span.titleAuthorETC')
                if elms:
                    authors = elms[0].text

                    if len(elms) == 2:
                        match = None
                        if self._contains(elms[1], TAG, 'strong'):
                            venue = elms[1].find_element(TAG, 'strong').text
                            match = YEAR_RE.search(elms[1].text)
                        else:
                            venue = elms[1].text
                            match = YEAR_RE.search(venue)

                        if match:
                            year = match.group(1)

                _results.append((title, authors, venue, year, link))
        except WebDriverException:
            extype, exvalue, extrace = sys.exc_info()
            traceback.print_exception(extype, exvalue, extrace)

        return _results

    def _parse_springer(self, url):
        _results = list()

        try:
            self.driver.get(url)
            results = self.driver.find_element(ID, 'results-list')
            for result in results.find_elements(TAG, 'li'):
                title, authors, venue, year, link = '', '', '', None, ''

                title = result.find_element(TAG, 'h2').text.replace('\n', ' ')
                link = result.find_element(CSS, 'h2 a').get_attribute('href')
                debug(title)
                metadata = result.find_element(CSS, 'p.meta')
                if self._contains(metadata, CLASS, 'authors'):
                    elms = metadata.find_elements(CSS, '.authors a')
                    authors = [elm.text for elm in elms]
                    if self._contains(metadata, CSS, '.authors span'):
                        items = metadata.find_element(CSS, '.authors span') \
                            .get_attribute('title').split(',')
                        authors += [i.strip() for i in items]
                    authors = ','.join(authors)
                if self._contains(metadata, CSS, '.enumeration a'):
                    venue = metadata.find_element(CSS, '.enumeration a') \
                        .get_attribute('title')
                year = metadata.find_element(CSS, '.enumeration span') \
                    .get_attribute('title')

                _results.append((title, authors, venue, year, link))
        except WebDriverException:
            extype, exvalue, extrace = sys.exc_info()
            traceback.print_exception(extype, exvalue, extrace)

        return _results

    def _parse_google_scholar(self, url):
        _results = list()

        try:
            self.driver.get(url)
            results = self.driver.find_element(ID, 'gs_ccl')
            for result in results.find_elements(CLASS, 'gs_r'):
                debug('here')
                title, authors, venue, year, link = '', '', '', None, ''

                title = result.find_element(CSS, 'h3 a').get_attribute('text')
                link = result.find_element(CSS, 'h3 a').get_attribute('href')
                debug(title)
                debug(link)

                _results.append((title, authors, venue, year, link))
        except WebDriverException:
            extype, exvalue, extrace = sys.exc_info()
            traceback.print_exception(extype, exvalue, extrace)

        return _results

    def _contains(self, element, by, value):
        elements = element.find_elements(by, value)
        return len(elements) > 0
