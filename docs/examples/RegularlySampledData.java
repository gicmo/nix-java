import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

import java.io.IOException;

public class RegularlySampledData {

    /**
     * Function to create sample sine values.
     *
     * @param duration  duration
     * @param frequency frequency
     * @param stepsize  step size
     * @return generated sine values
     */
    private static double[] createSineWave(double duration, double frequency,
                                           double stepsize) {
        int size = (int) Math.ceil(duration * 2 * Math.PI / stepsize);

        double[] y = new double[size];
        for (int i = 0; i < size; i++) {
            double x = i * stepsize;

            // sine
            y[i] = Math.sin(frequency * x);
        }
        return y;
    }

    /**
     * Function to plot data.
     *
     * @param dataArray data array
     */
    private static void plotData(DataArray dataArray) {
        // retrieve data
        double[] yData = new double[(int) dataArray.getDataExtent()
                .getElementsProduct()];
        dataArray.getData(yData, dataArray.getDataExtent(), new NDSize());

        // get x coordinates data
        SampledDimension dim = dataArray.getDimensions().get(0)
                .asSampledDimension();
        double[] xData = dim.getAxis(yData.length);

        DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries("Sine", new double[][]{xData, yData});

        // Create the chart
        JFreeChart chart = ChartFactory.createXYLineChart(dataArray.getName(),
                dim.getLabel() + " [" + dim.getUnit() + "]",
                dataArray.getLabel() + " [" + dataArray.getUnit() + "]",
                dataset, PlotOrientation.VERTICAL, false, false, false);

        chart.getXYPlot().getRenderer().setSeriesPaint(0, Color.BLUE);
        chart.getPlot().setBackgroundPaint(Color.WHITE);

        // save it to a file
        java.io.File imageFile = new java.io.File("regularly_sampled_data_output.png");
        try {
            ChartUtilities.saveChartAsPNG(imageFile, chart, 1000, 600);
        } catch (IOException ioe) {
            System.out.println("Error");
        }
    }

    public static void main(String[] args) {

        // fake some data
        double duration = 2;
        double frequency = 20;
        double stepsize = 0.02;

        double[] y = createSineWave(duration, frequency, stepsize);

        // create a new file overwriting any existing content
        String fileName = "regular_data_example.h5";
        File file = File.open(fileName, FileMode.Overwrite);

        // create a 'Block' that represents a grouping object. Here, the
        // recording session.
        // it gets a name and a type
        Block block = file.createBlock("block name", "nix.session");

        // create a 'DataArray' to take the sinewave, add some information about
        // the signal
        DataArray data = block.createDataArray("sinewave",
                "nix.regular_sampled", DataType.Double, new NDSize(
                        new int[]{y.length}));
        data.setData(y, data.getDataExtent(), new NDSize());
        data.setUnit("mV");
        data.setLabel("voltage");

        // add a descriptor for the xaxis
        SampledDimension dim = data.appendSampledDimension(stepsize);
        dim.setUnit("s");
        dim.setLabel("time");
        dim.setOffset(0.0);

        // let's plot the data from the stored information
        plotData(data);

        file.close();
    }
}