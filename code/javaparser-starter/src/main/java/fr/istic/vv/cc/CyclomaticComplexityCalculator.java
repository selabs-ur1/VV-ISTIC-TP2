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
