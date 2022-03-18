package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import com.github.javaparser.utils.Pair;

import java.util.*;

/**
 * Notes:
 * 1. parcourir tous les méthodes, trouver Nom de méthode, new list de variable(): variable de class
 * 2. for ( c c: method.children ) { c.accept(this, arg) } ;
 * // attention: effacer le contenu de list avant de l'ajoute pour prochaine méthode
 * 3. table.add(nom, list)
 *
 * fieldaccess.visit() -> ajouter variables dans la liste
 */

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class TCCCalculator extends VoidVisitorWithDefaults<Void> {

    private Map<String, List> table = new HashMap<> ();
    private List fieldList = new ArrayList();
    private Map<Pair<String, String>, List> connections = new HashMap<>();

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
       // if(!declaration.isPublic()) return;
       // System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }

        for (FieldDeclaration field: declaration.getFields()) {
            field.accept(this, arg);
        }

        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }

        // create all pairs of methods and the instance variables

        Map<String, List> tableCompared = new HashMap<>();
        tableCompared.putAll(table);


        Iterator it = table.entrySet().iterator();
        while (it.hasNext()) {
            // remove the entry to compare in the table to be compared
            Map.Entry<String, List> entryToCompare = (Map.Entry<String, List>) it.next();
            tableCompared.remove(entryToCompare.getKey());

                // add the pair of methods and the variables in common in connections
                for (Map.Entry otherEntry: tableCompared.entrySet()) {
                  List commonVariables =  containValue (entryToCompare.getValue(), (List) otherEntry.getValue());
                  if (!commonVariables.isEmpty()) {
                      connections.put(new Pair(entryToCompare.getKey(), otherEntry.getKey()), commonVariables);

                  }
                }
        }

        // calculate the value of TCC
        int nbMethods = declaration.getMethods().size();
        int nbPairsMethods = nbMethods * (nbMethods - 1) / 2;

        System.out.println("TCC value of class " + declaration.getFullyQualifiedName().orElse("[Anonymous]")
         + " : " + connections.size() + "/" + nbPairsMethods);

    }

    /**
     * find common values in two lists
     * @param list1
     * @param list2
     * @return list with common values
     */
    private List containValue(List list1, List list2) {
        List result = new ArrayList();
        for (Object e: list1 ) {
            if (list2.contains(e)) {
                result.add(e);
            }
        }
        return result;
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

        List fieldListInMethod = new ArrayList();

        for (Node c: declaration.getChildNodes()) {
            c.accept(this, arg);
        }
        fieldListInMethod.addAll(fieldList);
        table.put(declaration.getNameAsString(), fieldListInMethod);

       /* System.out.println("table of the method - " + declaration.getNameAsString()
                + " : fieldList : " + fieldList + " - table : " + table.entrySet());*/
        fieldList.clear();
    }


    @Override
    public void visit(FieldDeclaration n, Void arg) {
    }


    @Override
    public void visit(FieldAccessExpr n, Void arg) {
        fieldList.add(n.getNameAsString());
       // System.out.println("FieldAccessExpr : " + n.getNameAsString() + " : "+ fieldList);
    }

    @Override
    public void visit(BlockStmt n, Void arg) {
        for (Statement s: n.getStatements()) {
            s.accept(this, arg);
        }
    }

    @Override
    public void visit(ReturnStmt n, Void arg) {
        if(n.getExpression().isPresent()) {
            n.getExpression().get().accept(this, arg);
        }

    }

    @Override
    public void visit(AssignExpr n, Void arg) {
        n.getTarget().accept(this, arg);
        n.getValue().accept(this, arg);
    }

    @Override
    public void visit(ExpressionStmt n, Void arg) {
        n.getExpression().accept(this, arg);
    }

    @Override
    public void visit(Parameter n, Void arg) {
        n.getType().accept(this, arg);
    }

    @Override
    public void visit(IfStmt n, Void arg) {
        n.getCondition().accept(this, arg);
        n.getThenStmt().accept(this, arg);
        if(n.getElseStmt().isPresent()) {
            n.getElseStmt().get().accept(this, arg);
        }
    }
}
