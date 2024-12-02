package fr.istic.vv;

import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        PublicElementsPrinter printer = new PublicElementsPrinter();
        PrivateElementWithoutGetterPrinter printer2 = new PrivateElementWithoutGetterPrinter(new ArrayList<>(),new HashMap<>());
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer2, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
        Map<String, List<String>> toPrint = printer2.getFieldsWithoutGetters();
        for (Map.Entry<String, List<String>> entry : toPrint.entrySet()) {
            System.out.println(entry.getKey() + ": " );
            for (String field : entry.getValue()) {
                System.out.println("   " + field);
            }
        }
    }


}
