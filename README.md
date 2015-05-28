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

Build project and generate jars :

```
$ mvn clean package
```

To specify include and link path
```
$ mvn clean package -DincludePath=<path> -DlinkPath=<path>
```

Sample usage
```
$ mvn clean package -DincludePath=/usr/local/include/ -DlinkPath=/usr/local/lib/
```
