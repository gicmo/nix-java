Working with files
------------------

The following code shows how to create a new nix-file, close it and re-open them with different access rights.

```java
        
        String fileName = "file_create_example.h5";

        // create a new file overwriting any existing content
        File file = File.open(fileName, FileMode.Overwrite);

        System.out.println("File format     : " + file.getFormat());
        int[] version = file.getVersion();
        System.out.println("File version    : " + version[0] + "." + version[1] + "." + version[2]);
        System.out.println("File created at : " + file.getCreatedAt());

        // close file
        file.close();

        // re-open file for read-only access
        file = File.open(fileName, FileMode.ReadOnly);

        // this command will fail putting out HDF5 Errors
        file.createBlock("test block", "test");

        file.close();

        // re-open for read-write access
        file = File.open(fileName, FileMode.ReadWrite);

        // the following command now works fine
        file.createBlock("test block", "test");

        file.close();
```


Source code of this example: [FileCreate.java](examples/FileCreate.java)


