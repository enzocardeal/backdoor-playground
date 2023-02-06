#!/usr/bin/python
import os
import argparse

parser = argparse.ArgumentParser()
parser.add_argument('-c','--clean', action=argparse.BooleanOptionalAction, help="Clean instrumentation files")
args = parser.parse_args()

if(args.clean):
    os.remove("./allowed_class_methods.out")
    if(os.path.exists("./scope.out")):
        os.remove("./scope.out")
    if(os.path.exists("./ignore.out")):
        os.remove("./ignore.out")
else:
    os.system("java -javaagent:../instrument/target/jqf-instrument-1.8-SNAPSHOT.jar  -jar target/backdoor-playground-0.0.1-SNAPSHOT.jar")
