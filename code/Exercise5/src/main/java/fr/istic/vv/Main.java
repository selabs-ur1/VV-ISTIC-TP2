package fr.istic.vv;

import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if (!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory : " + args[0]);
            System.exit(2);
        }

        String reportFilePath = "cyclomatic_complexity_report.html";

        SourceRoot root = new SourceRoot(file.toPath());
        CyclomaticComplexityPrinter complexityAnalyzer = new CyclomaticComplexityPrinter();

        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(complexityAnalyzer, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        String htmlReport = complexityAnalyzer.generateHTMLTable();

        try (FileWriter writer = new FileWriter(reportFilePath)) {
            writer.write(htmlReport);
        }

        System.out.println("Cyclomatic complexity report saved to: " + reportFilePath);

    }

}
