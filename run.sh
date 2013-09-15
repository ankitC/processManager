#!/bin/bash

bash package.sh

if [ ! -z $1 ] 
then 
    bash execute.sh $1
else
    bash execute.sh
fi
