import os
import sys

from termcolor import cprint


def debug(message):
    if 'DEBUG' in os.environ:
        cprint('[DEBUG] {0}'.format(message), 'blue', file=sys.stderr)


def info(message):
    cprint('[INFO] {0}'.format(message), 'white', file=sys.stdout)


def warning(message):
    cprint('[WARNING] {0}'.format(message), 'yellow', file=sys.stderr)


def error(message):
    cprint('[ERROR] {0}'.format(message), 'red', file=sys.stderr)
