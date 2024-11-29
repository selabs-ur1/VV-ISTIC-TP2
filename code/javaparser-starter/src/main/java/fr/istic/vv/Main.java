package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if (!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        SourceRoot root = new SourceRoot(file.toPath());
        PrivateFieldNoGetterPrinter privateFieldNoGetterPrinter = new PrivateFieldNoGetterPrinter();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(privateFieldNoGetterPrinter, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        // Generate a CSV report with an understandable filename
        String reportPath = "private_fields_no_getters.csv";
        privateFieldNoGetterPrinter.generateReport(reportPath);
        System.out.println("Report generated: " + reportPath);
    }
}