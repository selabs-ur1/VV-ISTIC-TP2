package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;
import fr.istic.vv.cc.CyclomaticComplexityCalculator;
import fr.istic.vv.cc.CyclomaticComplexityReport;
import fr.istic.vv.fields.MarkdownGenerator;
import fr.istic.vv.fields.PrivateElementWithoutGetterPrinter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if(!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        SourceRoot root = new SourceRoot(file.toPath());
        PrivateElementWithoutGetterPrinter printer = new PrivateElementWithoutGetterPrinter(new ArrayList<>(),new HashMap<>());
        CyclomaticComplexityCalculator calculator = new CyclomaticComplexityCalculator();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));
            result.ifSuccessful(compilationUnit -> compilationUnit.accept(calculator, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
        MarkdownGenerator.generateMarkdownReport(args[1], printer.getFieldsWithoutGetters());
        CyclomaticComplexityReport.generateReport(args[2],calculator.getMethodComplexities());

    }


}
