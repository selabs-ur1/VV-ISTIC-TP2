package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.Set;

public class NoGetterDetector extends VoidVisitorAdapter<Void> {

    private Set<String> privateFields = new HashSet<>();
    private Set<String> getters = new HashSet<>();
    private String currentClassName;
    private String currentPackageName;
    private StringBuilder report = new StringBuilder("Field ,Class ,Package\n");

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        currentPackageName = unit.getPackageDeclaration().map(pd -> pd.getNameAsString()).orElse("");
        for (TypeDeclaration<?> type : unit.getTypes()) {
            visitTypeDeclaration(type, arg);
        }

        // on ajoute chaque champ privé qui n'a pas de getter dans la liste
        for (String field : privateFields) {
            if (!getters.contains(field)) {
                report.append(field).append(",").append(currentClassName).append(",").append(currentPackageName)
                        .append("\n");
            }
        }
        System.out.println(report.toString());

        privateFields.clear();
        getters.clear();
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic()) return; // Que les classes publiques
        currentClassName = declaration.getNameAsString();
        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }

        for (FieldDeclaration field : declaration.getFields()) {
            field.accept(this, arg);
        }

        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }
    }

     @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if (!declaration.isPublic()) return; // Que si la méthode est publique
        String methodName = declaration.getNameAsString();
        if (methodName.startsWith("get") && declaration.getParameters().isEmpty()) {
            String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
            getters.add(fieldName);
        }
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        if (!declaration.isPrivate()) return; // Que les champs privés
        for (VariableDeclarator variable : declaration.getVariables()) {
            privateFields.add(variable.getNameAsString());
        }
    }

    // Ici je fais juste un print de la liste, mais on peut écrire la logique pour créer un fichier CSV
    public void writeReport() {
        System.out.println(report.toString());
    }
}