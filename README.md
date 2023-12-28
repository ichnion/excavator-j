# Excavator-J

This is an updated Excavator that specifically targets 
*Google's Takeout Location History*. This updated is needed since Google modified
the directory layout as well as the JSON data format. This project can be 
folded back into the original Rust Excavator application if there is a strong
need.

#### Java

The Java Language was chosen to be used for this version of the Excavator due to
the difficulties of having to build and compile the original Rust appication
for each platform (Linux, MacOS, Windows,etc.). With Java, once a JAR file has
been created it can be used as is, on whatever platform that has a Java Runtime
Environment installed. Although the Rust application will be more performant, 
the difference though with that of Java is almost negligible. 

#### Usage

* Download the latest JAR File release.


* Download and extract Google's Takeout Location History


* Run the application:

```
java -jar excavator.jar -i Takeout -d ichnion.db
```

#### Compile

* Download the latest source code


* compile using maven:

```
mvn clean package
```

* created jar file with all the dependencies included will be in:

```
target/excavator-jar-with-dependencies.jar
```

#### Wiki

* [Viewing Location Points in QGIS](https://github.com/ichnion/excavator-j/wiki/Location-History-Points)

* [Viewing Location Activity Segments in QGIS](https://github.com/ichnion/excavator-j/wiki/Location-History-Activity-Segments)



