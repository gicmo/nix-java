import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

import java.io.IOException;
import java.util.Arrays;

public class IrregularlySampledData {

    /**
     * Function to get Poisson value.
     *
     * @param lambda lambda value
     * @return poisson value
     */
    private static int getPoisson(double lambda) {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do {
            k++;
            p *= Math.random();
        } while (p > L);

        return k - 1;
    }

    /**
     * Class to store generated data
     */
    private static class Data {
        double[] times;
        double[] y;

        public Data(double[] times, double[] y) {
            this.times = times;
            this.y = y;
        }
    }

    /**
     * Create sample data
     *
     * @param duration duration
     * @param interval interval
     * @return generated values
     */
    private static Data createData(double duration, double interval) {
        double[] times = new double[(int) (1.5 * duration / interval)];
        times = Arrays.stream(times).map(x -> getPoisson(interval * 1000) / 1000.0).toArray();

        for (int i = 1; i < times.length; i++) {
            times[i] += times[i - 1];
        }

        double round = 1000.0;
        times = Arrays.stream(times).map(x -> (Math.round(x * round) / round)).toArray();
        times = Arrays.stream(times).filter(x -> x <= duration).toArray();

        double stepsize = 0.001;
        double frequency = 5;
        int size = (int) Math.ceil(times[times.length - 1] * 2 * Math.PI / stepsize);
        double[] y = new double[size];
        for (int i = 0; i < size; i++) {
            double x = i * stepsize;
            y[i] = Math.sin(frequency * x);
        }

        double[] ind = Arrays.stream(times).map(x -> x / 0.001 * 2 * Math.PI).toArray();
        double[] yData = new double[ind.length];
        for (int i = 0; i < yData.length; i++) {
            yData[i] = y[(int) ind[i]];
        }
        return new Data(times, yData);
    }

    /**
     * Function to plot data
     *
     * @param dataArray dataArray
     */
    private static void plotData(DataArray dataArray) {
        // get y values
        double[] yData = new double[(int) dataArray.getDataExtent()
                .getElementsProduct()];
        dataArray.getData(yData, dataArray.getDataExtent(), new NDSize());

        // get x axis values
        RangeDimension dim = dataArray.getDimensions().get(0)
                .asRangeDimension();
        double[] xData = dim.getTicks();

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
        java.io.File imageFile = new java.io.File("irregularly_sampled_data_output.png");
        try {
            ChartUtilities.saveChartAsPNG(imageFile, chart, 1000, 600);
        } catch (IOException ioe) {
            System.out.println("Error");
        }
    }

    public static void main(String[] args) {

        Data res = createData(1.0, 0.02);
        double[] times = res.times;
        double[] y = res.y;

        String fileName = "irregular_data_example.h5";
        File file = File.open(fileName, FileMode.Overwrite);

        // create a 'Block' that represents a grouping object. Here, the recording session.
        // it gets a name and a type
        Block block = file.createBlock("block name", "nix.session");

        // create a 'DataArray' to take the data, add some information about the signal
        DataArray data = block.createDataArray("sinewave", "nix.irregular_sampled", DataType.Double, new NDSize(
                new int[]{y.length}));
        data.setData(y, data.getDataExtent(), new NDSize());
        data.setUnit("mV");
        data.setLabel("voltage");

        // add a descriptor for the xaxis
        RangeDimension dim = data.appendRangeDimension(times);
        dim.setUnit("s");
        dim.setLabel("time");

        // let's plot the data from the stored information
        plotData(data);

        file.close();
    }
}
