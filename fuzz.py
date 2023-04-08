#!/usr/bin/python3

import os
import argparse

parser = argparse.ArgumentParser()
parser.add_argument('-f','--fuzz',help="Pass the period you want to fuzz. Ex.: '10s'", required=True)
args = parser.parse_args()

if(args.fuzz):
    os.chdir("./backdoor-playground")
    os.system("mvn test-compile")
    os.system(f'mvn jqf:fuzz -Dclass=br.usp.pcs.back.api.user.ApiFuzzTest -Dmethod=userLoginTest -Din=resources/fuzz-input/login-input -Dtime={args.fuzz}')
    os.system(f'mvn jqf:fuzz -Dclass=br.usp.pcs.back.api.user.ApiFuzzTest -Dmethod=userSignUpTest -Din=resources/fuzz-input/signup-input -Dtime={args.fuzz}')
    os.system(f'mvn jqf:fuzz -Dclass=br.usp.pcs.back.api.user.ApiFuzzTest -Dmethod=wrongMethodTest -Din=resources/fuzz-input/wrong-method-input -Dtime={args.fuzz}')
    os.chdir("..")