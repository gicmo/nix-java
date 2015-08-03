Additional information
----------------------

### Storing the origin of data

Let’s assume we want to note the origin of the data. For example they have been obtained from a certain experiment or an experimental subject. For this purpose Source entities are used. Sources can be nested to reflect dependencies between different sources. For example One may record data from different neurons in the same brain region of the same animal.

```java
        
        // create some source entities
        Source subject = block.createSource("mouese A", "nix.experimental_subject");
        Source brain_region = subject.createSource("hippocampus", "nix.experimental_subject");
        Source cell_1 = brain_region.createSource("CA1 1", "nix.experimental_subject");
        Source cell_2 = brain_region.createSource("CA1 2", "nix.experimental_subject");

        // add them to the data.
        DataArray da1 = block.createDataArray("cell1 response", "nix.regular_sampled", DataType.Double, new NDSize(new int[]{response_1.length}));
        da1.setData(response_1, da1.getDataExtent(), new NDSize());
        da1.getSources().add(cell_1);
        
        DataArray da2 = block.createDataArray("cell2 response", "nix.regular_sampled", DataType.Double, new NDSize(new int[]{response_2.length}));
        da2.setData(response_2, da2.getDataExtent(), new NDSize());
        da2.getSources().add(cell_2);     

```


### Adding arbitrary metadata

Almost all entities allow to attach arbitray metadata. The basic concept of the metadata model is that Properties are oragnized in Sections which in turn can be nested to represent hierarchical structures. The Sections basically act like python dictionaries. How to create sections and properties is demonstrated by attaching information about the ‘Lenna’ image used above.

```java
       
        // attached information
        List<Property> properties = section.getProperties();
        Object[][] data = new Object[properties.size()][2];
        for (int i = 0; i < data.length; i++) {
            Property property = properties.get(i);
            data[i][0] = property.getName();
            data[i][1] = property.getValues().get(0).getString();
        }

        // create table
        JTable table = new JTable(data, columnNames);
```

###### Output

![Output Image](output/image_with_metadata_output.png)

Source code of this example: [ImageWithMetaData.java](examples/ImageWithMetaData.java)

