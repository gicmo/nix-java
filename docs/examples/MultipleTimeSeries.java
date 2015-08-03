import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

public class MultipleTimeSeries {

    /**
     * Class to store generated sine and cosine values.
     */
    private static class Data {
        double[] sine;
        double[] cosine;

        public Data(double[] sine, double[] cosine) {
            this.sine = sine;
            this.cosine = cosine;
        }
    }

    /**
     * Function to create sample sine and cosine values.
     *
     * @param duration  duration
     * @param frequency frequency
     * @param stepsize  step size
     * @return generated sine and cosine values
     */
    private static Data createSineAndCosineWave(double duration, double frequency,
                                                double stepsize) {
        int size = (int) Math.ceil(duration * 2 * Math.PI / stepsize);

        double[] sine = new double[size];
        double[] cosine = new double[size];
        for (int i = 0; i < size; i++) {
            double x = i * stepsize;

            // sine
            sine[i] = Math.sin(frequency * x);

            // cosine
            cosine[i] = Math.cos(frequency * x);
        }
        return new Data(sine, cosine);
    }

    /**
     * Function to play data.
     *
     * @param dataArray dataArray
     */
    private static void plotData(DataArray dataArray) {
        // retrieve information
        double[] yData = new double[(int) dataArray.getDataExtent()
                .getElementsProduct()];
        dataArray.getData(yData, dataArray.getDataExtent(), new NDSize());

        // separate sine and cosine values
        int size = yData.length / 2;
        double[] sine = new double[size];
        double[] cosine = new double[size];

        for (int i = 0; i < size; i++) {
            sine[i] = yData[i];
            cosine[i] = yData[i + size];
        }

        // get x coordinates data
        SampledDimension dim = dataArray.getDimensions().get(1)
                .asSampledDimension();
        double[] xData = dim.getAxis(size);

        // retrieve labels
        SetDimension set_dim = dataArray.getDimensions().get(0).asSetDimension();
        List<String> labels = set_dim.getLabels();

        DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries(labels.get(0), new double[][]{xData, sine});
        dataset.addSeries(labels.get(1), new double[][]{xData, cosine});

        // Create the chart
        JFreeChart chart = ChartFactory.createXYLineChart(dataArray.getName(),
                dim.getLabel() + " [" + dim.getUnit() + "]",
                dataArray.getLabel() + " [" + dataArray.getUnit() + "]",
                dataset, PlotOrientation.VERTICAL, false, false, false);

        chart.getXYPlot().getRenderer().setSeriesPaint(0, Color.BLUE);
        chart.getPlot().setBackgroundPaint(Color.WHITE);

        // save it to a file
        java.io.File imageFile = new java.io.File("multiple_time_series_output.png");
        try {
            ChartUtilities.saveChartAsPNG(imageFile, chart, 1000, 600);
        } catch (IOException ioe) {
            System.out.println("Error");
        }
    }

    public static void main(String[] args) {

        // fake some data
        double duration = 2;
        double frequency = 2;
        double stepsize = 0.02;

        Data res = createSineAndCosineWave(duration, frequency, stepsize);
        double[] sine = res.sine;
        double[] cosine = res.cosine;

        double[] y = DoubleStream.concat(Arrays.stream(sine), Arrays.stream(cosine)).toArray();

        // create a new file overwriting any existing content
        String fileName = "multiple_regular_data_example.h5";
        File file = File.open(fileName, FileMode.Overwrite);

        // create a 'Block' that represents a grouping object. Here, the
        // recording session.
        // it gets a name and a type
        Block block = file.createBlock("block name", "nix.session");

        // create a 'DataArray' to take the sinewave, add some information about
        // the signal
        DataArray data = block.createDataArray("waveforms", "nix.regular_sampled.multiple_series", DataType.Double, new NDSize(
                new int[]{y.length}));
        data.setData(y, data.getDataExtent(), new NDSize());
        data.setUnit("mV");
        data.setLabel("voltage");

        // descriptor for first dimension is a set
        SetDimension set_dim = data.appendSetDimension();
        set_dim.setLabels(Arrays.asList("sin", "cos"));

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