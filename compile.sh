#!/bin/bash
if [ ! -d bin ]; then mkdir bin; fi
javac -d bin/ `find -L src/ -name '*.java'` -cp lib/jgraph-5.4.7.jar:lib/jgrapht-0.5.3.jar:lib/log4j-1.2.9.jar
cp log4j.properties mocca.properties bin/
