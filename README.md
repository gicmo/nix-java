[![Build Status](https://travis-ci.org/G-Node/nix-java.svg?branch=master)](https://travis-ci.org/G-Node/nix-java)

--

Java bindings for the NIX library
================================

This project is an extension for the [NIX](https://github.com/G-Node/nix) library and provides Java bindings for nix.


Development Status
------------------

This project is still work in progress and should not be seen as a final product.

Getting Started
---------------

Build instructions

[NIX](https://github.com/G-Node/nix) needs to be installed.

Build project and generate jars:

```
$ mvn clean package
```

To specify include and link path
```
$ mvn clean package -DnixIncludePath=<path> -DnixLinkPath=<path>
```

Sample usage
```
$ mvn clean package -DnixIncludePath=/usr/local/include/ -DnixLinkPath=/usr/local/lib/
```
