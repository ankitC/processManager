#!/bin/bash

if [ ! -z $1 ] 
then 
    java -cp src/dslab.jar main/Main -s $1
else
    java -cp src/dslab.jar main/Main
fi
