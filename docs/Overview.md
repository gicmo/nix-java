Overview
--------

### Design Principles

The design of the data model tries to draw on similarities of different data types and structures and and come up with entities that are as generic and versatile as meaningful. At the same time we aim for clearly established links between different entities to keep the model as expressive as possible.

Most entities of the NIX-model have a name and a type field which are meant to provide information about the entity. While the name can be freely chose, the type is meant to provide semantic information about the entity and we aim at definitions of different types. Via the type, the generic entities can become domain specific.

For the electrophysiology disicplines of the neurosciences, an INCF working groups has set out to define such data types. For more information see [here](http://crcns.org/files/data/nwb/ephys_requirements_v0_72.pdf).


#### Creating a file
 
 So far we have implemented the nix model only for the HDF5 file format. In order to store data in a file we need to create one.
 
 ```java
 
        File nixFile = File.open("example.h5", FileMode.Overwrite);
 ```
 
 The File entity is the root of this document and it has only two children the data and metadata nodes. You may want to use the hdfview tool to open the file and look at it. Of course you can access both parts using the File API.
 
 All information directly related to a chunk of data is stored in the data node as children of a top-level entity called Block. A Block is a grouping element that can represent many things. For example it can take up everything that was recorded in the same session. Therefore, the Block has a name and a type.
 
 ```java
  
        Block block = nixFile.createBlock("Test block", "nix.session");
 ```
 
 Names can be freely chosen. Duplication of names on the same hierarchy-level is not allowed. In this example creating a second Block with the very same name leads to an error. Names must not contain ‘/’ characters since they are path separators in the HDF5 file. To avoid collisions across files every created entity has an unique id (UUID).
 
 ```java
   
        block.getId();
        System.out.println(block.getId());
          
        bbb5a112-a382-4ada-b51e-777c08ddc3c2
 ```
  
 
#### Storing data

 The heart of our data model is an entity called DataArray. This is the entity that actually stores all data. It can take n-dimensional arrays and provides sufficient information to create a basic plot of the data. To achieve this, one essential parts is to define what kind of data is stored. Hence, every dimension of the stored data must be defined using the available Dimension descriptors (below). The following code snippets show how to create an DataArray and how to store data in it.

  ```java
   
        double[] some_array = new double[2 * 1000];
        DataArray data = block.createDataArray("my data", "nix.sampled", DataType.Double,
            new NDSize(new int[]{some_array.length}));
        data.setData(some_array, data.getDataExtent(), new NDSize());
  ```
  
  Using this call will create a DataArray, set name and type, set the dataType according to the dtype of the passed data, and store the data in the file. You can also create empty DataArrays to take up data-to-be-recorded. In this case you have to provide the space that will be needed in advance.
  
  If you do not know the size of the data in advance, you can append data to an already existing DataArray. Beware: Though it is possible to extend the data, it is not possible to change the dimensionality (rank) of the data afterwards.
  
  ```java
     
        double[] some_other_array = new double[2 * 1010];
        data.setDataExtent(new NDSize(new int[]{2 * 1010}));
        data.setData(some_other_array, data.getDataExtent(), new NDSize());
  ```
  
  
#### Dimension descriptors

 In the above examples we have created DataArray entities that are used to store the data. Goal of our model design is that the data containing structures carry enough information to create a basic plot. Let’s assume a time-series of data needs to be stored: The data is just a vector of measurements (e.g. voltages). The data would be plotted as a line-plot. We thus need to define the x- and the y-axis of the plot. The y- or value axis is defined by setting the label and the unit properties of the DataArray, the x-axis needs a dimension descriptor. In the nix model three different dimension descriptors are defined. SampledDimension, RangeDimension, and SetDimension which are used for (i) data that has been sampled in space or time in regular intervals, (ii) data that has been sampled in irregular intervals, and (iii) data that belongs to categories.

 ```java
     
        double sampleInterval = 0.001;
        SampledDimension dim = data.appendSampledDimension(sampleInterval);
        dim.setLabel("time");
        dim.setUnit("s");
 ```
 The SampledDimension can also be used to desribe space dimensions, e.g. in case of images.
 
 If the data was sampled at irregular intervals the sample points of the x-axis are defined using the ticks property of a RangeDimension.
 
 ```java
     
        double[] sampleTimes = {1.0, 3.0, 4.2, 4.7, 9.6};
        RangeDimension rangedim = data.appendRangeDimension(sampleTimes);
        rangedim.setLabel("time");
        rangedim.setUnit("s");
 ``` 

 Finally, some data belongs into categroies which do not necessarly have a natural order. In these cases a SetDimension is used. This descriptor can store for each category an optional label.
 
 ```java
     
        int[] observations = {0, 0, 5, 20, 45, 40, 28, 12, 2, 0, 1, 0};
        List<String> categories = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        SetDimension setdim = data.appendSetDimension();
        setdim.setLabels(categories);
 ```  
  
  
#### Annotate regions in the data

 Annotating points of regions of interest is one of the key features of the nix data-model. There are two entities for this purpose: (i) the Tag is used for single points or regions while the (ii) MultiTag is used to mark multiple of these. Tags have one or many positions and extents which define the point or the region in the referenced DataArray. Further they can have Features to store additional information about the positions (see tutorials below).

##### Tag

  The tag is a relatively simple structure directly storing the position the tag points and the, optional, extent of this region. Each of these are vectors of a length matching the dimensionality of the referenced data.
  
  ```java
  
          double[] position = {10, 10};
          double[] extent = {5, 20};
          Tag tag = block.createTag("interesting part", "nix.roi", position);
          tag.setExtent(extent);
          // finally, add the referenced data to this tag
          tag.setReferences(Arrays.asList(data));
  ```

##### MultiTag
  
  MultiTags are made to tag multiple points (regions) at once. The main difference to the Tag is that position and extent are stored in DataArray entities. These entities must be 2-D. Both dimensions are SetDimensions. The first dimension represents the individual positions, the second dimension takes the coordinates in the referenced n-dimensional DataArray.
  
  ```java
        
        // fake data
        double[] frame = new double[100];
        data = block.createDataArray("random image", "nix.image", DataType.Double, new NDSize(new int[]{frame.length}));
        SampledDimension dimX = data.appendSampledDimension(1.0);
        dimX.setLabel("x");

        SampledDimension dimY = data.appendSampledDimension(1.0);
        dimY.setLabel("y");

        double[] p = {
                10, 10,
                20, 10
        };
        DataArray positions = block.createDataArray("special points", "nix.positions", DataType.Int32, new NDSize(new int[]{p.length}));
        positions.setData(p, positions.getDataExtent(), new NDSize());

        positions.appendSetDimension();
        SetDimension setdim = positions.appendSetDimension();
        setdim.setLabels(Arrays.asList("x", "y"));
        // create a multi tag
        MultiTag tag = block.createMultiTag("interesting points", "nix.multiple_roi", positions);
        tag.setReferences(Arrays.asList(data));
  ```  
  
  
#### Adding further information

 The tags establish links between datasets. If one needs to attach further information to each of the regions defined by the tag, one can add Features to them. A Feature references a DataArray as its data and specifies with the link_type how the link has to be interpreted. The link_type can either be tagged, indexed, or untagged indicating that the tag should be applied also to the feature data (tagged), for each position given in the tag, a slice of the feature data (ith index along the first dimension) is the feature (indexed), or all feature data applies for all positions (untagged).

 Let’s say we want to give each point a name, we can create a feature like this:

 ```java
 
        DataArray spotNames = ...
        Feature feature = tag.createFeature(spotNames, LinkType.Indexed);
 ```
 
 We could also say that each point in the tagged data (e.g. a matrix of measurements) has a corresponding point in an input matrix.

 ```java
 
        DataArray inputData = ...
        tag.createFeature(inputData, LinkType.Tagged);
 ```

 Finally, one could need to attach the same information to all positions defined in the tag. In this case the feature is untagged

 ```java
 
        DataArray commonFeature = ...
        tag.createFeature(commonFeature, LinkType.Untagged);
 ```
 

#### Defining the Source of the data

 In cases in which we want to store where the data originates Source entities can be used. Almost all entities of the NIX-model can have Sources. For example, if the recorded data originates from experiments done with one specific experimental subject. Sources have a name and a type and can have some definition.
 
 ```java
 
        Source subject = block.createSource("subject A", "nix.experimental_subject");
        subject.setDefinition("The experimental subject used in this experiment");
        data.setSources(Arrays.asList(subject));
 ``` 
 
 Sources may depend on other Sources. For example, in an electrophysiological experiment we record from different cells in the same brain region of the same animal. To represent this hierarchy, Sources can be nested, create a tree-like structure.
  
 ```java
  
        Source subject = block.createSource("subject A", "nix.experimental_subject");
        Source brain_region = subject.createSource("hippocampus", "nix.experimental_subject");
        Source cellA = brain_region.createSource("Cell 1", "nix.experimental_subject");
        Source cellB = brain_region.createSource("Cell 2", "nix.experimental_subject");
 ``` 
 
 
#### Defining the Source of the data

The entities discussed so far carry just enough information to get a basic understanding of the stored data. Often much more information than that is required. Storing additional metadata is a central part of the NIX concept. We use a slightly modified version of the odML data model for metadata to store additional information. In brief: the model consists of Sections that contain Properties which in turn contain one or more Values. Again, Sections can be nested to represent logical dependencies in the hierarchy of a tree. While all data entities discussed above are children of Block entities, the metadata lives parallel to the Blocks. The idea behind this is that several blocks may refer to the same metadata, or, the other way round the metadata applies to data entities in several blocks. The types used for the Sections in the following example are defined in the odml terminologies.

Most of the data entities can link to metadata sections.

 ```java
  
        Section sec = nixFile.createSection("recording session", "odml.recording");
        sec.createProperty("experimenter", new Variant("John Doe"));

        Section subject = sec.createSection("subject", "odml.subject");
        subject.createProperty("id", new Variant("mouse xyz"));

        Section cell = subject.createSection("cell", "odml.cell");
        Variant val = new Variant(-64.5);

        Property property = cell.createProperty("resting potential", val);
        property.setUnit("mV");
        // set the recording block metadata
 ``` 

#### Units

 In NIX we accept only SI units (plus dB, %) wherever units can be given. We also accept compound units like mV/cm. Units are most of the times handled transparently. That is, when you tag a region of data that has been specified with a time axis in seconds and use e.g. the tag.retrieve_data method to get this data slice, the API will handle unit scaling. The correct data will be returned even if the tag’s position is given in ms.
