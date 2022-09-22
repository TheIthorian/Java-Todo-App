@ECHO OFF
cd src
start javac -O -d ../bin Todo.java 
cd ../bin
start jar -cfe Todo.jar Todo *.class