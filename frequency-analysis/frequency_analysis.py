#!/usr/bin/python3

import re
import os
import argparse
import json
from collections import Counter

parser = argparse.ArgumentParser()
parser.add_argument('-s','--scope', nargs="+", help="Set the scope in which the instrumentation should take place.", required=True)
parser.add_argument('-i','--ignore', nargs="+", help="Set the ignore packages om which instrumentation shouldn't take place.")
args = parser.parse_args()

def check_in_scope(scope_list, method):
  for item in scope_list:
    if item in method:
      return True
  
  return False

method_list = []
scope_list = args.scope
ignore_list = args.ignore

files_names = os.listdir("./coverage_files")

with open("coverage.out", "w+") as outfile:
  for file_name in files_names:
    with open(os.path.join("./coverage_files", file_name)) as infile:
      outfile.write(infile.read())

with open('coverage.out') as f:
  for l in f:
    result = re.search("(?:\(-{0,1}\d{1,10}\) )([\w/#<>$]{1,}\(\)\:\d{1,})(?: (?:--> |\[-{0,1}\d{1,}]))([\w/#<>$]{1,}|)", l)
    if result != None:
        method_list.append(str(result.group(1)))
        #method_list.append(str(result.group(2)))

method_list = list(filter(("").__ne__, method_list))

frequency_dict = dict(Counter(method_list))

methods_to_instrument = []
for key, value in frequency_dict.items():
  if(value >= 1 and check_in_scope(scope_list, key)):
    temp_list = key.split(":")
    method = temp_list[0]
    line = int(temp_list[1])
    methods_to_instrument.append(method+":"+str(line-2))
    methods_to_instrument.append(method+":"+str(line-1))
    methods_to_instrument.append(key)
    methods_to_instrument.append(method+":"+str(line+1))
    methods_to_instrument.append(method+":"+str(line+2))

with open('frequency.json', 'w') as fp:
    json.dump(frequency_dict, fp)

with open('allowed_class_methods.out', 'w') as f:
    for item in methods_to_instrument:
        f.write("%s\n" % item)

if scope_list and len(scope_list) > 0:
  with open("scope.out", 'w') as f:
    for scope in scope_list:
      f.write("%s\n" % scope)

if ignore_list and len(ignore_list) > 0:
  with open("ignore.out", 'w') as f:
    for ignore in ignore_list:
      f.write("%s\n" % ignore)

print('Done!')