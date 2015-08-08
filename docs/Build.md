Build Guidelines
================

Building NIX-Java on Linux
--------------------------

1. **Prior requirements**

   - Build NIX on Linux. 
   - Instructions available [here](https://github.com/G-Node/nix).
   
2. **Build NIX-Java**

    - Build project and generate jars. Uses default include and link path.
    
    ```
        $ mvn clean package
    ```
    
    - The include and link paths can be explicitly defined by 
    
    ```
        $ mvn clean package -DnixIncludePath=/usr/local/include/ -DboostIncludePath=/usr/include/boost/ -DnixLinkPath=/usr/local/lib/ -Dhdf5LinkPath=/usr/local/lib/
    ```
    

Building NIX-Java on Windows
----------------------------

1. **Prior requirements**

   - Build NIX on Windows. 
   - Instructions available [here](https://github.com/G-Node/nix/blob/master/Win32.md).
   
2. **Build NIX-Java**

    - Build project and generate jars. Uses default include and link path.
    
    ```
        > mvn clean package
    ```
    
    - The include and link paths can be explicitly defined by 
    
    ```
        > mvn clean package -DnixIncludePath=C:/nix/include -DboostIncludePath=%BOOST_INCLUDEDIR% -DnixLinkPath=C:/nix/build/Release -Dhdf5LinkPath=%HDF5_BASE%/bin
    ```
    




