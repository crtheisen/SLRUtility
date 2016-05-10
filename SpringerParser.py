import sys
from selenium import webdriver
from selenium.webdriver.common.keys import Keys

if len(sys.argv) != 2:
  print('Usage: SpringerParser.py <output>.tsv')
  exit()

write_result = open(sys.argv[1], "w")
driver = webdriver.Firefox()

write_result.write("Title\tLink\n")

for x in range(1, 6):
  driver.get("http://link.springer.com/search/page/" + str(x) + "?date-facet-mode=between&showAll=true&query=security+AND+%22entry+point%22+AND+%22attack+surface%22")
  assert "Springer" in driver.title

  items = driver.find_elements_by_class_name('title')
  x = 0
  for item in items:
    print(item.get_attribute("href"))
    write_result.write(item.text + "\t" + item.get_attribute("href") + "\n")

driver.close()
write_result.close()