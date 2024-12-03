# Code of your exercise

Put here all the code created for this exercise

```
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
```

```
package fr.istic.vv.cc;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.Map;

public class CyclomaticComplexityCalculator extends VoidVisitorAdapter<Void> {

    private final Map<String, Integer> methodComplexities = new HashMap<>();

    @Override
    public void visit(MethodDeclaration method, Void arg) {
        CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();
        method.getBody().ifPresent(body -> body.accept(visitor, null));
        String signature = method.getDeclarationAsString(true, true);
        methodComplexities.put(signature, visitor.getComplexity());
    }

    public Map<String, Integer> getMethodComplexities() {
        return methodComplexities;
    }

}
```

```
package fr.istic.vv.cc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CyclomaticComplexityReport {

    public static void generateReport(String filePath, Map<String, Integer> complexities) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("# Cyclomatic Complexity Report\n\n");
            writer.write("| Method Signature | Cyclomatic Complexity |\n");
            writer.write("|------------------|------------------------|\n");

            for (Map.Entry<String, Integer> entry : complexities.entrySet()) {
                writer.write("| " + entry.getKey() + " | " + entry.getValue() + " |\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

```
package fr.istic.vv.cc;

import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CyclomaticComplexityVisitor extends VoidVisitorAdapter<Void> {

    private int complexity = 1;

    @Override
    public void visit(IfStmt stmt, Void arg) {
        complexity++;
        super.visit(stmt, arg);
    }

    @Override
    public void visit(ForStmt stmt, Void arg) {
        complexity++;
        super.visit(stmt, arg);
    }

    @Override
    public void visit(WhileStmt stmt, Void arg) {
        complexity++;
        super.visit(stmt, arg);
    }

    @Override
    public void visit(DoStmt stmt, Void arg) {
        complexity++;
        super.visit(stmt, arg);
    }

    @Override
    public void visit(SwitchEntry stmt, Void arg) {
        complexity++;
        super.visit(stmt, arg);
    }

    @Override
    public void visit(CatchClause stmt, Void arg) {
        complexity++;
        super.visit(stmt, arg);
    }

    public int getComplexity() {
        return complexity;
    }
}
```