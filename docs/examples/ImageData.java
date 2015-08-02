import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ImageData {

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
     * Plot Data
     *
     * @param dataArray data array
     */
    private static void plotData(DataArray dataArray) {
        // get image bytes
        byte[] img_data = new byte[(int) dataArray.getDataExtent().getElementsProduct()];
        dataArray.getData(img_data, dataArray.getDataExtent(), new NDSize());

        try {
            // create image from bytes
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(img_data));

            // write to file
            ImageIO.write(img, "png", new java.io.File("image_data_output.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        byte[] pix = loadImage();

        // create a new file overwriting any existing content
        String fileName = "image_example.h5";
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

        // let's plot the data from the stored information
        plotData(data);

        file.close();
    }
}
