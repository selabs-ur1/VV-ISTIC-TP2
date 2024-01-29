package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Objects;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        for(FieldDeclaration field : declaration.getFields()){
            if (!field.isPublic() && !hasGetter(field, declaration)) {
                writeFieldInfos(field, declaration);
            }
        }
    }

    private void writeFieldInfos(FieldDeclaration field, TypeDeclaration<?> classOrInterface) {
        PackageDeclaration packageDeclaration = classOrInterface.findCompilationUnit().get().getPackageDeclaration().orElse(null);
        String packageName = Objects.isNull(packageDeclaration) ? "" : packageDeclaration.getNameAsString() + ".";

        try {
            FileOutputStream outputFile = new FileOutputStream("no_getter_results.txt");
            PrintWriter pw = new PrintWriter(outputFile);

            pw.println(packageName + classOrInterface.getName() + "." + field.getVariable(0).getNameAsString() + " has no getter");

            pw.close();
            outputFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasGetter(FieldDeclaration field, TypeDeclaration<?> classOrInterface) {
        String fieldName = field.getVariable(0).getNameAsString();
        String getterFieldName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        for (MethodDeclaration method : classOrInterface.getMethods()) {
            if (method.isPublic() && method.getName().asString().equals(getterFieldName)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

}
