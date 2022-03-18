package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class NoGetterPrinter extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);

         /*   if (unit.getPackageDeclaration().isPresent()) {
                System.out.println(unit.getPackageDeclaration().get().getNameAsString());
            }*/

        }

    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;

      //  declaration.getFields().forEach(System.out::println);

        for (FieldDeclaration field: declaration.getFields()) {
            field.accept(this, arg);
            if (!field.isPrivate()) return;

            // get field name
            String fieldName = field.getVariables().get(0).getName().toString();
            // Capitalize the field name
            String fieldNameUpperCase = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);

            boolean hasGetter = false;
            boolean returnFieldValue = false;
            boolean isGetNameOfFiled;

            for(MethodDeclaration method : declaration.getMethods()) {
                method.accept(this, arg);

                // check if the method simply returns the value of the field
                if (method.getBody().isPresent()){
                    returnFieldValue = method.getBody().get().getStatements().getFirst().toString().equals("return " + fieldName + ";");
                }

                // check if the method name is get<name-of-the-field>
                isGetNameOfFiled = method.getName().toString().equals("get" + fieldNameUpperCase);
                if (isGetNameOfFiled && returnFieldValue) {
                    hasGetter = true;
                    break;
                }
            }
           // list its name, the name of the declaring class and the package of the declaring class.
            if (!hasGetter) {
                System.out.println("Field : " + fieldName
                        + " - Class : " + declaration.getFullyQualifiedName().orElse("[Anonymous]") );
            }

        }

        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if(!declaration.isPublic()) return;
       // System.out.println("  " + declaration.getDeclarationAsString(true, true));
    }

/*    @Override
    public void visit(PackageDeclaration declaration, Void arg) {
         declaration.getName().accept(this, arg);
        System.out.println(declaration.getNameAsString());
    }*/

    @Override
    public void visit(PackageDeclaration n, Void arg) {
        super.visit(n, arg);
        System.out.println(n.getNameAsString());
    }
}
