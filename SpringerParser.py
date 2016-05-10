import sys
from selenium import webdriver
from selenium.webdriver.common.keys import Keys

if len(sys.argv) != 2:
  print('Usage: SpringerParser.py <output>')
  exit()

driver = webdriver.Firefox()
driver.get("http://link.springer.com/search/page/1?date-facet-mode=between&showAll=true&query=security+AND+%22entry+point%22+AND+%22attack+surface%22")
assert "Springer" in driver.title

write_result = open(sys.argv[1], "w")

for x in range(1, 6):
  driver = webdriver.Firefox()
  driver.get("http://link.springer.com/search/page/" + str(x) + "?date-facet-mode=between&showAll=true&query=security+AND+%22entry+point%22+AND+%22attack+surface%22")
  assert "Springer" in driver.title

  items = driver.find_elements_by_class_name('content-item-list')
  for item in items:
    my_string = item.text
    new_string = my_string.split("\n")
    i = 0
    for attr in new_string:
      if (attr == "Chapter" or attr == "Article"):
        write_result.write("")
      elif (attr == "Look Inside Get Access"):
        write_result.write("\n")
      else:
        write_result.write(attr + ",")
      i += 1
  driver.close()