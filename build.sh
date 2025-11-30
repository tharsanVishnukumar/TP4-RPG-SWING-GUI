#!/bin/sh

javac -d out $(find src -name "*.java") && cd out && java Main
