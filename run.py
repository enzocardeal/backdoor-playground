#!/usr/bin/python

import os
import argparse
import shutil

parser = argparse.ArgumentParser()
parser.add_argument('-f','--fuzz',help="Pass the period you want to fuzz. Ex.: '10s'")
parser.add_argument('-s','--scope', nargs="+", help="Set the scope in which the instrumentation should take place.", required=True)
parser.add_argument('-i','--ignore', nargs="+", help="Set the ignore list in which the instrumentation shouldn't take place.")
args = parser.parse_args()

if(args.fuzz):
    os.chdir("./backdoor-playground")
    os.system("mvn test-compile")
    os.system(f'mvn jqf:fuzz -Dclass=br.usp.pcs.control.UserFuzzTest -Dmethod=testGetUser -Din=target/fuzz-input/ -Dtime={args.fuzz}')
    os.chdir("..")

os.chdir("./backdoor-playground")
coverage_files = os.listdir("./target/fuzz-results/br.usp.pcs.control.UserFuzzTest/testGetUser/corpus")
coverage_files.extend(os.listdir("./target/fuzz-results/br.usp.pcs.control.UserFuzzTest/testGetUser/failures"))

for file in coverage_files:
    index = file.split("_")[1]
    os.system(f'mvn jqf:repro -DprintArgs -Dclass=br.usp.pcs.control.UserFuzzTest -Dmethod=testGetUser -Dinput=target/fuzz-results/br.usp.pcs.control.UserFuzzTest/testGetUser/corpus/{file} -DlogCoverage=coverage_{index}.out')
    shutil.copyfile(f'coverage_{index}.out', f'../frequency-analysis/coverage_files/coverage_{index}.out')
    os.remove(f'coverage_{index}.out')
os.chdir("..")

os.chdir("./frequency-analysis")
scope_list_string = "-s "+" ".join(args.scope)
ignore_list_string = " "
if(args.ignore):
    ignore_list_string = "-i "+" ".join(args.ignore)
os.system(f"python frequency_analysis.py {scope_list_string} {ignore_list_string}")
shutil.copy("./allowed_class_methods.out", "../backdoor-playground/allowed_class_methods.out")
shutil.copy("./scope.out", "../backdoor-playground/scope.out")
os.remove("./scope.out")
if(os.path.exists("./ignore.out")):
    shutil.copy("./ignore.out", "../backdoor-playground/ignore.out")
    os.remove("./ignore.out")
os.chdir("..")
