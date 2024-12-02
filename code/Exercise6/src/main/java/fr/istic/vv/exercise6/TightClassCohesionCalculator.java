package fr.istic.vv.exercise6;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TightClassCohesionCalculator {
    private TightClassCohesionCalculator() {
        throw new IllegalStateException("Utility class");
    }

    public static double calculate(ClassOrInterfaceDeclaration declaration) {
        ClassVisitor visitor = new ClassVisitor();
        visitor.visit(declaration, null);
        return visitor.getTCC();
    }

    private static class ClassVisitor extends VoidVisitorAdapter<Void> {
        double tcc = 0;
        Map<MethodDeclaration, Set<VariableDeclarator>> methodFieldAccessMap = new HashMap<>();

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
            super.visit(n, arg);

            List<MethodDeclaration> methods = n.getMethods();

            int directlyConnectedPairs = 0;
            int totalPairs = methods.size() * (methods.size() - 1) / 2;

            for (int i = 0; i < methods.size(); i++) {
                for (int j = i + 1; j < methods.size(); j++) {
                    Set<VariableDeclarator> fields1 = methodFieldAccessMap.get(methods.get(i));
                    Set<VariableDeclarator> fields2 = methodFieldAccessMap.get(methods.get(j));
                    fields1.retainAll(fields2);
                    if (!fields1.isEmpty()) {
                        directlyConnectedPairs++;
                    }
                }
            }

            tcc = totalPairs == 0 ? 1 : (double) directlyConnectedPairs / totalPairs;
        }

        @Override
        public void visit(MethodDeclaration method, Void arg) {
            Set<VariableDeclarator> accessedFields = new HashSet<>();
            method.findAll(VariableDeclarator.class).forEach(accessedFields::add);
            methodFieldAccessMap.put(method, accessedFields);
        }

        public double getTCC() {
            return tcc;
        }
    }
}