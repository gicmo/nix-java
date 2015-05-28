Java bindigs for the NIX library
================================

This project is an extension for the [NIX](https://github.com/G-Node/nix) library and provides Java bindings for nix.
The project is part of the 2015 Google Summer of Code and is performed by:

* Sujith V (Student)
* Adrian Stoewer (Mentor)

Development Status
------------------

This project is still work in progress and should not be seen as a final product.

Getting Started (Linux)
-----------------------

Build instructions

Install JavaCPP into Maven local repository :

```
$ cd libs
$ mvn install:install-file -Dfile=javacpp.jar -DgroupId=org.bytedeco -DartifactId=javacpp -Dversion=0.11 -Dpackaging=jar
$ cd ..
```


Build project and generate jars :

```
$ mvn clean package --projects nix
```
