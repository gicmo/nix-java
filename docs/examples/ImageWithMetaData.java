import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ImageWithMetaData {

    /**
     * Obtain bytes from image 'lenna.png'
     *
     * @return byte array
     */
    private static byte[] loadImage() {
        byte[] pix = null;
        try {
            BufferedImage img = ImageIO.read(new java.io.File("lenna.png"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            baos.flush();
            pix = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pix;
    }

    /**
     * Generates image for a table
     *
     * @param section metadata information
     * @return image
     */
    private static BufferedImage printMetadataInformation(Section section) {
        // header names
        String[] columnNames = {"Name", "Value"};

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
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setWidth(100);
        table.getColumnModel().getColumn(1).setWidth(200);
        table.setSize(300, 30 * data.length);

        // create image
        BufferedImage bi = new BufferedImage(table.getWidth(), table.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bi.createGraphics();
        table.getTableHeader().paint(graphics);
        table.paint(graphics);
        graphics.dispose();

        return bi;
    }

    /**
     * Function to plot data
     *
     * @param dataArray data array
     */
    private static void plotData(DataArray dataArray) {
        try {
            // image
            byte[] img_data = new byte[(int) dataArray.getDataExtent().getElementsProduct()];
            dataArray.getData(img_data, dataArray.getDataExtent(), new NDSize());
            BufferedImage img1 = ImageIO.read(new ByteArrayInputStream(img_data));

            // table
            BufferedImage img2 = printMetadataInformation(dataArray.getMetadata());

            // joining both images
            int w = img1.getWidth() + img2.getWidth();
            int h = Math.max(img1.getHeight(), img2.getHeight());
            BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

            // paint both images
            Graphics2D g = combined.createGraphics();
            g.setBackground(Color.WHITE);
            g.clearRect(0, 0, combined.getWidth(), combined.getHeight());
            g.drawImage(img1, 0, 0, null);
            g.drawImage(img2, img1.getWidth(), 0, null);

            // write to file
            ImageIO.write(combined, "png", new java.io.File("image_with_metadata_output.png"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static Section addImageInformation(File file) {
        // create metadata section
        Section section = file.createSection("Image metadata", "image_source");

        // add information
        section.createProperty("Original name", new Variant("Lenna"));
        section.createProperty("Journal", new Variant("Playboy Magazine"));
        section.createProperty("Year", new Variant("1972"));
        section.createProperty("Month", new Variant("November"));
        section.createProperty("Author", new Variant("Dwight Hooker"));
        section.createProperty("Source", new Variant("http://en.wikipedia.org/wiki/File:Lenna.png#mediaviewer/File:Lenna.png"));
        section.createProperty("Comment", new Variant("512x512 electronic/mechanical scan of a section of the full portrait" +
                ": Alexander Sawchuk and two others[1] - The USC-SIPI image database."));
        section.createProperty("Model", new Variant("Lena Soederberg"));

        return section;
    }

    public static void main(String[] args) {

        byte[] pix = loadImage();

        // create a new file overwriting any existing content
        String fileName = "image_with_source_example.h5";
        File file = File.open(fileName, FileMode.Overwrite);

        // create a 'Block' that represents a grouping object. Here, the recording session.
        //it gets a name and a type
        Block block = file.createBlock("block name", "nix.session");

        //create a 'DataArray'
        DataArray data = block.createDataArray("lenna", "nix.image.rgb", DataType.Int8, new NDSize(new int[]{pix.length}));
        data.setData(pix, data.getDataExtent(), new NDSize());

        // add descriptors for width, height and channels
        SampledDimension height_dim = data.appendSampledDimension(1);
        height_dim.setLabel("height");
        SampledDimension width_dim = data.appendSampledDimension(1);
        width_dim.setLabel("width");
        SetDimension color_dim = data.appendSetDimension();
        color_dim.setLabels(Arrays.asList("R", "G", "B"));

        Section section = addImageInformation(file);
        data.setMetadata(section);

        // let's plot the data from the stored information
        plotData(data);

        file.close();
    }
}
