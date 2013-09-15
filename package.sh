#!/bin/bash

rm dslab.jar
cd src
make
jar cf dslab.jar *
