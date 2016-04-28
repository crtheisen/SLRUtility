import sys
sys.dont_write_bytecode = True

if len(sys.argv) != 3:
  print 'Usage: ACM_csv_searcher.csv <gold_standard> <target>'
  exit()

gold_standard = open(sys.argv[1],"rU")
target = open(sys.argv[2],"rU")

gs_dict = {}
total_gold_papers = 0
for line in gold_standard:
  if line.rstrip('\n').rstrip(',') not in gs_dict:
    gs_dict[line.rstrip('\n').rstrip(',')] = 0
    total_gold_papers += 1

for line in target:
  line = line.split(",")
  for item in gs_dict:
    if line[6] == item:
      gs_dict[item] = 1
      # print "Found match for: " + item

toPrint = ""
inc = 0
for item in gs_dict:
  if gs_dict[item] == 1:
    toPrint += item + "\n"
    inc += 1

print "List of included papers (" + str(inc) + " of " + str(total_gold_papers) + "):" 
print toPrint + "\n"

toPrint = ""
exc = 0
for item in gs_dict:
  if gs_dict[item] == 0:
    toPrint += item + "\n"
    exc += 1

print "List of excluded papers (" + str(exc) + " of " + str(total_gold_papers) + "):" 
print toPrint + "\n"

# headers = False
# for i in f:
#   if (headers):
#     i = i.split(",")
#     sys.stdout.write("'" + i[0] + "',")
#   else:
#     headers = True