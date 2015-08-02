import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;

import java.io.IOException;
import java.util.Arrays;

public class SpikeTagging {

    private static RunStimObject fakeNeuron() {
        LIF lif_model = new LIF();
        return lif_model.runConstStim(10000, 0.005);
    }

    private static void plotData(MultiTag tag) {
        // voltage positions
        DataArray dataArray = tag.getReferences().get(0);
        double[] voltage = new double[(int) dataArray.getDataExtent().getElementsProduct()];
        dataArray.getData(voltage, dataArray.getDataExtent(), new NDSize());

        // voltage x coordinates
        SampledDimension dim = dataArray.getDimensions().get(0)
                .asSampledDimension();
        double[] xData = dim.getAxis(voltage.length);

        // spike positions
        DataArray position = tag.getPositions();
        double[] spike_times = new double[(int) position.getDataExtent().getElementsProduct()];
        position.getData(spike_times, position.getDataExtent(), new NDSize());

        // spike positions y coordinate
        double[] spikeY = new double[spike_times.length];
        double max = Arrays.stream(voltage).max().orElse(0.0);
        Arrays.fill(spikeY, max);

        // XY plot
        XYPlot plot = new XYPlot();

        // line properties
        DefaultXYDataset line = new DefaultXYDataset();
        line.addSeries(dataArray.getLabel(), new double[][]{xData, voltage});
        XYItemRenderer lineRenderer = new XYLineAndShapeRenderer(true, false);
        lineRenderer.setSeriesPaint(0, Color.BLUE);
        plot.setDataset(0, line);
        plot.setRenderer(0, lineRenderer);
        plot.setDomainAxis(0, new NumberAxis(dim.getLabel() + " [" + dim.getUnit() + "]"));
        plot.setRangeAxis(0, new NumberAxis(dataArray.getLabel() + " [" + dataArray.getUnit() + "]"));
        plot.mapDatasetToDomainAxis(0, 0);
        plot.mapDatasetToRangeAxis(0, 0);

        // scatter properties
        DefaultXYDataset scatter = new DefaultXYDataset();
        scatter.addSeries(tag.getName(), new double[][]{spike_times, spikeY});
        XYItemRenderer scatterRenderer = new XYLineAndShapeRenderer(false, true);
        scatterRenderer.setSeriesPaint(0, Color.RED);
        plot.setDataset(1, scatter);
        plot.setRenderer(1, scatterRenderer);
        plot.mapDatasetToDomainAxis(1, 0);
        plot.mapDatasetToRangeAxis(1, 0);

        // plot chart
        JFreeChart chart = new JFreeChart(dataArray.getLabel(), JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.getPlot().setBackgroundPaint(Color.WHITE);

        // save it to a file
        java.io.File imageFile = new java.io.File("spike_tagging_output.png");
        try {
            ChartUtilities.saveChartAsPNG(imageFile, chart, 1000, 600);
        } catch (IOException ioe) {
            System.out.println("Error");
        }
    }

    public static void main(String[] args) {

        RunStimObject res = fakeNeuron();
        double[] time = res.time;
        double[] voltage = res.membrane_voltage;
        double[] spike_times = res.spike_voltage;

        // create a new file overwriting any existing content
        String fileName = "spike_tagging.h5";
        File file = File.open(fileName, FileMode.Overwrite);

        // create a 'Block' that represents a grouping object. Here, the
        // recording session.
        // it gets a name and a type
        Block block = file.createBlock("block name", "nix.session");

        // create a 'DataArray' to take the sinewave, add some information about
        // the signal
        DataArray data = block.createDataArray("membrane voltage",
                "nix.regular_sampled", DataType.Double, new NDSize(
                        new int[]{voltage.length}));
        data.setData(voltage, data.getDataExtent(), new NDSize());
        data.setLabel("membrane voltage");
        // add descriptors for time axis
        SampledDimension time_dim = data.appendSampledDimension(time[1] - time[0]);
        time_dim.setLabel("time");
        time_dim.setUnit("s");

        // create the positions DataArray
        DataArray positions = block.createDataArray("times", "nix.positions", DataType.Double,
                new NDSize(new int[]{spike_times.length}));
        positions.setData(spike_times, positions.getDataExtent(), new NDSize());
        positions.appendSetDimension();
        positions.appendSetDimension();

        // create a MultiTag
        MultiTag multi_tag = block.createMultiTag("spike times", "nix.events.spike_times", positions);
        multi_tag.setReferences(Arrays.asList(data));

        // let's plot the data from the stored information
        plotData(multi_tag);

        file.close();
    }
}
