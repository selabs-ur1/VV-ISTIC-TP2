import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.ArrayList;
import java.util.List;

public class NoGetterPrinter extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        List<String> noGetterFields = new ArrayList<>();

        for(FieldDeclaration field : declaration.getFields()){
            if(field.isPrivate()){
                //Si le champ est privé, vérifier présence d'un getter public
                String nameOfField = field.getVariable(0).getNameAsString();

                String nof = nameOfField.substring(0,1).toUpperCase()
                        + nameOfField.substring(1);

                //Si un getter existe, il est du format getNameOfField ou isNameOfField si c'est un booléen
                String getterName1 = "get" + nof;
                String getterName2 = "is" + nof;

                boolean getterExist = declaration.getMethodsByName(getterName1).stream().anyMatch(
                        method -> method.isPublic())
                        || declaration.getMethodsByName(getterName2).stream().anyMatch(
                        method -> method.isPublic()) ;

                if(!getterExist){
                    noGetterFields.add(nameOfField);
                }
            }
        }

        System.out.println(
                "Class : " + declaration.getNameAsString() + "\n"
                + "Package : " + declaration.findCompilationUnit().flatMap(CompilationUnit::getPackageDeclaration).get().getNameAsString() + "\n"
                + "No getter field : " + noGetterFields.toString() + "\n"
        );
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }
}
