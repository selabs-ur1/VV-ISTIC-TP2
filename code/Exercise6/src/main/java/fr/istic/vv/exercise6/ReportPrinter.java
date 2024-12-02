package fr.istic.vv.exercise6;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import org.jfree.chart.ChartUtils;

import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.Name;

public class ReportPrinter {
    Collection<Object[]> methods = new ArrayList<>();

    public void add(SimpleName className, Name packageName, SimpleName methodName, double tightClassCohesion) {
        methods.add(new Object[] { packageName, className, methodName, tightClassCohesion });
    }

    public void generate() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("report.csv"), StandardCharsets.UTF_8,
                java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write("Package;Class;Method;TCC");
            writer.newLine();

            for (Object[] method : methods) {
                writer.write(method[0] + ";" + method[1] + ";" + method[2] + ";" + method[3]);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        HistogramGenerator generator = new HistogramGenerator(methods);
        try {
            ChartUtils.saveChartAsPNG(new File("tcc-chart.png"), generator.getChart(), 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
