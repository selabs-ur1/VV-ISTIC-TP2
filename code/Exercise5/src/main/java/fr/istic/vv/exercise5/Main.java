package fr.istic.vv.exercise5;

import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;

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
        ReportPrinter reportPrinter = new ReportPrinter();
        MethodsFinder finder = new MethodsFinder(reportPrinter);
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(finder, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
        reportPrinter.generate();
    }
}
