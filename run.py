#!/usr/bin/python3

import os
import argparse
import shutil

parser = argparse.ArgumentParser()
parser.add_argument('-f','--fuzz',help="Pass the period you want to fuzz. Ex.: '10s'")
parser.add_argument('-s','--scope', nargs="+", help="Set the scope in which the instrumentation should take place.", required=True)
parser.add_argument('-i','--ignore', nargs="+", help="Set the ignore list in which the instrumentation shouldn't take place.")
args = parser.parse_args()

if(args.fuzz):
    print("Fuzzing...")
    os.chdir("./backdoor-playground")
    os.system("mvn test-compile")
    os.system(f'mvn jqf:fuzz -Dclass=br.usp.pcs.back.api.user.ApiFuzzTest -Dmethod=userLoginTest -Din=resources/fuzz-input/login-input -Dtime={args.fuzz}')
    os.system(f'mvn jqf:fuzz -Dclass=br.usp.pcs.back.api.user.ApiFuzzTest -Dmethod=userSignUpTest -Din=resources/fuzz-input/signup-input -Dtime={args.fuzz}')
    os.system(f'mvn jqf:fuzz -Dclass=br.usp.pcs.back.api.user.ApiFuzzTest -Dmethod=wrongMethodTest -Din=resources/fuzz-input/wrong-method-input -Dtime={args.fuzz}')
    os.chdir("..")
    print("Done Fuzzing.")
    print("############################################################################################")

print("Generating code coverage...")
os.chdir("./backdoor-playground")
#coverage_files = os.listdir("./target/fuzz-results/br.usp.pcs.control.UserFuzzTest/testGetUser/corpus")
#coverage_files.extend(os.listdir("./target/fuzz-results/br.usp.pcs.control.UserFuzzTest/testGetUser/failures"))

#for file in coverage_files:
#    index = file.split("_")[1]
#    os.system(f'mvn jqf:repro -DprintArgs -Dclass=br.usp.pcs.control.UserFuzzTest -Dmethod=testGetUser -Dinput=target/fuzz-results/br.usp.pcs.control.UserFuzzTest/testGetUser/corpus/{file} -DlogCoverage=coverage_{index}.out')
#    shutil.copyfile(f'coverage_{index}.out', f'../frequency-analysis/coverage_files/coverage_{index}.out')
#    os.remove(f'coverage_{index}.out')

os.system(f'mvn jqf:repro -DprintArgs -Dclass=br.usp.pcs.back.api.user.ApiFuzzTest -Dmethod=userLoginTest -Dinput=target/fuzz-results/br.usp.pcs.back.api.user.ApiFuzzTest/userLoginTest/corpus -DlogCoverage=coverage_00.out')
os.system(f'mvn jqf:repro -DprintArgs -Dclass=br.usp.pcs.back.api.user.ApiFuzzTest -Dmethod=userLoginTest -Dinput=target/fuzz-results/br.usp.pcs.back.api.user.ApiFuzzTest/userLoginTest/failures -DlogCoverage=coverage_01.out')
if(len(os.listdir("./target/fuzz-results/br.usp.pcs.back.api.user.ApiFuzzTest/userLoginTest/corpus")) > 0):
    shutil.copyfile(f'coverage_00.out', f'../frequency-analysis/coverage_files/coverage_00.out')
if(len(os.listdir("./target/fuzz-results/br.usp.pcs.back.api.user.ApiFuzzTest/userLoginTest/failures")) > 0):
    shutil.copyfile(f'coverage_01.out', f'../frequency-analysis/coverage_files/coverage_01.out')
os.remove(f'coverage_00.out')
os.remove(f'coverage_01.out')

os.system(f'mvn jqf:repro -DprintArgs -Dclass=br.usp.pcs.back.api.user.ApiFuzzTest -Dmethod=userSignUpTest -Dinput=target/fuzz-results/br.usp.pcs.back.api.user.ApiFuzzTest/userSignUpTest/corpus -DlogCoverage=coverage_10.out')
os.system(f'mvn jqf:repro -DprintArgs -Dclass=br.usp.pcs.back.api.user.ApiFuzzTest -Dmethod=userSignUpTest -Dinput=target/fuzz-results/br.usp.pcs.back.api.user.ApiFuzzTest/userSignUpTest/failures -DlogCoverage=coverage_11.out')
if(len(os.listdir("./target/fuzz-results/br.usp.pcs.back.api.user.ApiFuzzTest/userSignUpTest/corpus")) > 0):
    shutil.copyfile(f'coverage_10.out', f'../frequency-analysis/coverage_files/coverage_10.out')
if(len(os.listdir("./target/fuzz-results/br.usp.pcs.back.api.user.ApiFuzzTest/userSignUpTest/failures")) > 0):
    shutil.copyfile(f'coverage_11.out', f'../frequency-analysis/coverage_files/coverage_11.out')
os.remove(f'coverage_10.out')
os.remove(f'coverage_11.out')

os.system(f'mvn jqf:repro -DprintArgs -Dclass=br.usp.pcs.back.api.user.ApiFuzzTest -Dmethod=wrongMethodTest -Dinput=target/fuzz-results/br.usp.pcs.back.api.user.ApiFuzzTest/wrongMethodTest/corpus -DlogCoverage=coverage_20.out')
os.system(f'mvn jqf:repro -DprintArgs -Dclass=br.usp.pcs.back.api.user.ApiFuzzTest -Dmethod=wrongMethodTest -Dinput=target/fuzz-results/br.usp.pcs.back.api.user.ApiFuzzTest/wrongMethodTest/failures -DlogCoverage=coverage_21.out')
if(len(os.listdir("./target/fuzz-results/br.usp.pcs.back.api.user.ApiFuzzTest/wrongMethodTest/corpus")) > 0):
    shutil.copyfile(f'coverage_20.out', f'../frequency-analysis/coverage_files/coverage_20.out')
if(len(os.listdir("./target/fuzz-results/br.usp.pcs.back.api.user.ApiFuzzTest/wrongMethodTest/failures")) > 0):
    shutil.copyfile(f'coverage_21.out', f'../frequency-analysis/coverage_files/coverage_21.out')
os.remove(f'coverage_20.out')
os.remove(f'coverage_21.out')

os.chdir("..")
print("Done Generating code coverage.")
print("############################################################################################")

print("Generating frequency analysis...")
os.chdir("./frequency-analysis")
scope_list_string = "-s "+" ".join(args.scope)
ignore_list_string = " "
if(args.ignore):
    ignore_list_string = "-i "+" ".join(args.ignore)
os.system(f"python3 frequency_analysis.py {scope_list_string} {ignore_list_string}")
shutil.copy("./allowed_class_methods.out", "../backdoor-playground/allowed_class_methods.out")
shutil.copy("./scope.out", "../backdoor-playground/scope.out")
os.remove("./scope.out")
if(os.path.exists("./ignore.out")):
    shutil.copy("./ignore.out", "../backdoor-playground/ignore.out")
    os.remove("./ignore.out")
os.chdir("..")
print("############################################################################################")
