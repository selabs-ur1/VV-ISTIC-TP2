package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainCC {

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
        ComplexiteCyclomatique printer = new ComplexiteCyclomatique();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
        writeInFile(printer.getBuilderContain() );
    }

    private static void writeInFile(String contain){
        try {

            FileWriter writer = new FileWriter("cc.csv");
            contain = "Class,Method,CC\n" + contain;
            writer.write(contain);
            writer.close();
            System.out.println("Successfully wrote text to file.");

        } catch (IOException ignore) {
            System.out.println("Error to make the file\n" + contain);
        }
    }
}
