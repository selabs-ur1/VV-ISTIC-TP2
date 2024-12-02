package fr.istic.vv.exercise6;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;

import java.util.ArrayList;
import java.util.Collection;

public class HistogramGenerator {

    JFreeChart chart;

    public HistogramGenerator(Collection<Object[]> methods) {
        IntervalXYDataset dataset = createDataset(methods);
        this.chart = createChart(dataset);
    }

    public JFreeChart getChart() {
        return this.chart;
    }

    private static IntervalXYDataset createDataset(Collection<Object[]> methods) {
        HistogramDataset dataset = new HistogramDataset();

        ArrayList<Double> values = new ArrayList<>();
        for (Object[] method : methods) {
            values.add((Double) method[3]);
        }

        dataset.addSeries("Tight Class Cohesion", values.stream().mapToDouble(Double::doubleValue).toArray(), 10);

        return dataset;
    }

    private static JFreeChart createChart(IntervalXYDataset dataset) {
        return ChartFactory.createHistogram(
                "Tight Class Cohesion Histogram",
                "Tight Class Cohesion",
                "Count",
                dataset);
    }
}