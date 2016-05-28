import traceback

from selenium import webdriver
from selenium.common.exceptions import WebDriverException
from selenium.webdriver.common.by import By

from logger import *

# Convenience Aliases
CLASS = By.CLASS_NAME
CSS = By.CSS_SELECTOR
ID = By.ID
TAG = By.TAG_NAME


class Parser(object):
    def __init__(self, service, browser):
        self.service = service
        self.browser = browser
        self.driver = None

    def get_header(self):
        if self.service == 'acm':
            pass
        elif self.service == 'ieee':
            pass
        elif self.service == 'springer':
            return ('title', 'authors', 'venue', 'year', 'link')

    def parse(self, url):
        debug('Parsing {}'.format(url))
        if self.service == 'acm':
            return self._parse_acm(url)
        elif self.service == 'ieee':
            return self._parse_ieee(url)
        elif self.service == 'springer':
            return self._parse_springer(url)

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

    def _parse_ieee(self, url):
        raise NotImplementedError('Implementation does not exist.')

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

    def _contains(self, element, by, value):
        elements = element.find_elements(by, value)
        return len(elements) > 0
