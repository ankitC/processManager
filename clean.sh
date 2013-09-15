#!/bin/bash

find src/ -iname *.class -exec rm '{}' ';'
rm src/dslab.jar
