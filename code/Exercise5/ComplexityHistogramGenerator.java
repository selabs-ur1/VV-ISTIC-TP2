package fr.istic.vv;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ComplexityHistogramGenerator {
    public void generateHistogram(List<Integer> complexities, String outputFile) throws IOException {
        // Convert List to double array
        double[] complexityArray = complexities.stream()
                .mapToDouble(Integer::doubleValue)
                .toArray();

        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Cyclomatic Complexity", complexityArray, 10);

        JFreeChart histogram = ChartFactory.createHistogram(
                "Cyclomatic Complexity Distribution",  // Chart title
                "Complexity",                          // X-Axis Label
                "Frequency",                           // Y-Axis Label
                dataset,                               // Dataset
                PlotOrientation.VERTICAL,              // Plot Orientation
                true,                                  // Show Legend
                true,                                  // Use tooltips
                false                                  // Configure chart to generate URLs?
        );

        // Save the histogram as a PNG
        ChartUtils.saveChartAsPNG(new File(outputFile), histogram, 800, 600);
    }
}