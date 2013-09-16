#!/bin/bash

find src/ -iname *.class -exec rm '{}' ';'
rm src/dslab.jar
mkdir tmp
cp demo/demo.txt tmp/demo.txt
rm -r demo/*
cp tmp/demo.txt demo/demo.txt
rm -r tmp
