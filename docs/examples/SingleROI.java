import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class SingleROI {

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
     * Function to draw a rectangle on a image.
     *
     * @param img image on which rectangle is to be drawn
     * @param pos position array
     * @param ext extents array
     */
    private static void drawRect(BufferedImage img, double[] pos, double[] ext) {
        Graphics2D graph = img.createGraphics();
        graph.drawRect((int) pos[1], (int) pos[0], (int) ext[1], (int) ext[0]);
    }

    /**
     * Function to plot data
     *
     * @param tag tag
     */
    private static void plotData(Tag tag) {
        // get data array
        DataArray dataArray = tag.getReferences().get(0);

        // retrieve image data as bytes
        byte[] img_data = new byte[(int) dataArray.getDataExtent().getElementsProduct()];
        dataArray.getData(img_data, dataArray.getDataExtent(), new NDSize());

        // retrieve position and extents arrays
        double[] pos = tag.getPosition();
        double[] ext = tag.getExtent();

        try {
            // create image
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(img_data));

            // draw rectangle
            drawRect(img, pos, ext);

            // write image to file
            ImageIO.write(img, "png", new java.io.File("single_roi_output.png"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {

        byte[] pix = loadImage();

        // create a new file overwriting any existing content
        String fileName = "single_roi.h5";
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

        // create a Tag, position and extent must be 3-D since the data is 3-D
        double[] position = {250, 250, 0};
        double[] extent = {30, 100, 3};
        Tag tag = block.createTag("Region of interest", "nix.roi", position);
        tag.setExtent(extent);
        tag.setReferences(Arrays.asList(data));

        // let's plot the data from the stored information
        plotData(tag);

        file.close();
    }
}
