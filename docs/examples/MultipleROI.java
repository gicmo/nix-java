import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class MultipleROI {

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
     * @param multiTag tag
     */
    private static void plotData(MultiTag multiTag) {
        // get data array
        DataArray dataArray = multiTag.getReferences().get(0);

        // retrieve image data as bytes
        byte[] img_data = new byte[(int) dataArray.getDataExtent().getElementsProduct()];
        dataArray.getData(img_data, dataArray.getDataExtent(), new NDSize());

        // get position and extent data array
        DataArray position = multiTag.getPositions();
        DataArray extent = multiTag.getExtents();

        // get position data
        double[] position_data = new double[(int) position.getDataExtent().getElementsProduct()];
        position.getData(position_data, position.getDataExtent(), new NDSize());

        // get extents data
        double[] extent_data = new double[(int) extent.getDataExtent().getElementsProduct()];
        extent.getData(extent_data, extent.getDataExtent(), new NDSize());

        try {
            // create image
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(img_data));

            // draw rectangles
            int size = position_data.length;
            for (int i = 0; i < size; i += 3) {
                double[] pos = {position_data[i], position_data[i + 1], position_data[i + 2]};
                double[] ext = {extent_data[i], extent_data[i + 1], extent_data[i + 2]};
                drawRect(img, pos, ext);
            }

            // write image to file
            ImageIO.write(img, "png", new java.io.File("multiple_roi_output.png"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {

        byte[] pix = loadImage();

        // create a new file overwriting any existing content
        String fileName = "multiple_roi.h5";
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

        // some space for three regions-of-interest
        double[] roi_starts = {
                250, 245, 0,
                250, 315, 0,
                340, 260, 0
        };

        double[] roi_extents = {
                30, 45, 3,
                30, 40, 3,
                25, 65, 3
        };

        // create the positions DataArray
        DataArray positions = block.createDataArray("ROI positions", "nix.positions",
                DataType.Double, new NDSize(new int[]{3 * 3}));
        positions.setData(roi_starts, positions.getDataExtent(), new NDSize());
        positions.appendSetDimension(); // these can be empty
        positions.appendSetDimension();

        // create the extents DataArray
        DataArray extents = block.createDataArray("ROI extents", "nix.extents",
                DataType.Double, new NDSize(new int[]{3 * 3}));
        extents.setData(roi_extents, extents.getDataExtent(), new NDSize());
        extents.appendSetDimension();
        extents.appendSetDimension();

        // create a MultiTag
        MultiTag multi_tag = block.createMultiTag("Regions of interest", "nix.roi", positions);
        multi_tag.setExtents(extents);
        multi_tag.setReferences(Arrays.asList(data));

        // let's plot the data from the stored information
        plotData(multi_tag);

        file.close();
    }
}
