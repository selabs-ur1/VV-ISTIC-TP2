package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import static com.github.javaparser.utils.Utils.capitalize;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    private int cyclomaticComplexity = 1;
    private FileWriter writer;

    public PublicElementsPrinter(FileWriter writer) {
        this.writer = writer;
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) throws IOException {
        if (!declaration.isPublic()) return;
        writer.write(declaration.getFullyQualifiedName().orElse("[Anonymous]") + "\n");
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        for (FieldDeclaration field : declaration.getFields()) {
            if (!field.isPublic()) {
                for (VariableDeclarator variable : field.getVariables()) {
                    String fieldName = variable.getNameAsString();
                    boolean hasPublicGetter = declaration.getMethods().stream()
                            .anyMatch(method -> isPublicGetterForField(method, fieldName));
                    if (!hasPublicGetter) {
                        writer.write("  Private field without public getter: " + fieldName + "\n");
                        System.out.println("  Private field without public getter: " + fieldName);
                    }
                }
            }
        }
        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Printing nested types in the top level
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration) {
                member.accept(this, arg);
            }
        }
    }

    private boolean isPublicGetterForField(MethodDeclaration method, String fieldName) {
        if (!method.isPublic()) return false;

        String expectedGetterName = "get" + capitalize(fieldName.charAt(0) + fieldName.substring(1));
        return method.getNameAsString().equals(expectedGetterName) && method.getParameters().isEmpty();
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        try {
            visitTypeDeclaration(declaration, arg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        try {
            visitTypeDeclaration(declaration, arg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if (!declaration.isPublic()) return;
        try {
            writer.write("  " + declaration.getDeclarationAsString(true, true) + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("  " + declaration.getDeclarationAsString(true, true));
        Optional<BlockStmt> blockStmtOptional = declaration.getBody();
        blockStmtOptional.ifPresent(blockStmt -> blockStmt.accept(this, arg));
        try {
            writer.write("   Cyclomatic Complexity = " + cyclomaticComplexity + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("   Cyclomatic Complexity = " + cyclomaticComplexity);
        cyclomaticComplexity = 1;
    }

    @Override
    public void visit(BlockStmt blockStmt, Void arg) {
        for (Statement statement : blockStmt.getStatements()) {
            statement.accept(this, arg);
        }
    }

    @Override
    public void visit(IfStmt statement, Void arg) {
        cyclomaticComplexity++;
        super.visit(statement, arg);

        statement.getElseStmt().ifPresent(elseStmt -> elseStmt.accept(this, arg));
    }

    @Override
    public void visit(ForStmt statement, Void arg) {
        cyclomaticComplexity++;
        super.visit(statement, arg);
    }

    @Override
    public void visit(WhileStmt statement, Void arg) {
        cyclomaticComplexity++;
        super.visit(statement, arg);
    }

    @Override
    public void visit(SwitchStmt statement, Void arg) {
        cyclomaticComplexity += statement.getEntries().size();
        super.visit(statement, arg);
    }

}
