cd src
javac -O -d ../bin Todo.java
cd ../bin
jar -cfe Todo.jar Todo *.class