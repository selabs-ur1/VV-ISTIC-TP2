package fr.istic.vv;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.javaparser.ast.stmt.Statement;
import java.io.File;

public class CyclomaticComplexityAnalyzer {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Provide the path to the source code");
            System.exit(1);
        }

        File projectPath = new File(args[0]);

        SourceRoot sourceRoot = new SourceRoot(projectPath.toPath());
        Map<Integer, Integer> complexityHistogram = new HashMap<>();

        System.out.println(sourceRoot.tryToParse());

        List<MethodDeclaration> methods = sourceRoot.tryToParse()
                .stream()
                .flatMap(result -> {
                    try {
                        return result.getResult().orElseThrow(() -> new IOException("Error parsing file")).findAll(MethodDeclaration.class).stream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        for (MethodDeclaration method : methods) {
            int complexity = calculateCyclomaticComplexity(method);
            System.out.println("Method: " + method.getName() + ", Cyclomatic Complexity: " + complexity);
            complexityHistogram.put(complexity, complexityHistogram.getOrDefault(complexity, 0) + 1);
        }

        System.out.println("Cyclomatic Complexity Histogram:");
        for (Map.Entry<Integer, Integer> entry : complexityHistogram.entrySet()) {
            System.out.println("CC: " + entry.getKey() + ", Occurrences: " + entry.getValue());
        }
    }

    private static int calculateCyclomaticComplexity(MethodDeclaration method) {
        List<Class<? extends com.github.javaparser.ast.Node>> controlFlowStatements = Arrays.asList(
                IfStmt.class, SwitchStmt.class, WhileStmt.class, ForStmt.class, SwitchEntry.class,
                TryStmt.class
        );

        int controlFlowEdges = method.findAll(Statement.class)
                .stream()
                .filter(statement -> controlFlowStatements.contains(statement.getClass()))
                .mapToInt(statement -> 1)
                .sum();

        return controlFlowEdges + 1;
    }
}


