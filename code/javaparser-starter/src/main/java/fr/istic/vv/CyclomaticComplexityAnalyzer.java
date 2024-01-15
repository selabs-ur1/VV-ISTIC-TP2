package fr.istic.vv;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CyclomaticComplexityAnalyzer {
    public static String outFile = "_output.txt";

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

        System.out.println("File " + file);

        SourceRoot root = new SourceRoot(file.toPath());

        CyclomaticComplexityVisitor ccv = new CyclomaticComplexityVisitor();


        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> {
                System.out.println("success");
                unit.accept(ccv, null);
            });
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        generateReport(ccv.getCcMap());

    }


    private static void generateHistogram(Map<String, Integer> ccMap) {
        //TODO
    }

    private static void generateReport(Map<String, Integer> ccMap) throws IOException {
        try(FileWriter writer = new FileWriter("report"+outFile)) {
            writer.write("Package, Class, Method, CC\n");

            for(Map.Entry<String, Integer> entry : ccMap.entrySet()){
                String[] parts = entry.getKey().split("\\.");
                writer.write(parts[0] + "," + parts[1] + "," +
                        parts[2] + "," + entry.getValue() + "\n");
            }
        }

    }


}

