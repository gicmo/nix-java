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
        $ mvn clean package -DincludePath=/usr/local/include/ -DboostIncludePath=/usr/include/boost/ -DlinkPath=/usr/local/lib/
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
        > mvn clean package -DincludePath=C:/nix/include -DboostIncludePath=%BOOST_INCLUDEDIR% -DlinkPath=C:/nix/build/Release
    ```
    




