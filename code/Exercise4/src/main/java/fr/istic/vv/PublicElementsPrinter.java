package fr.istic.vv;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {
//liste attribut prives
//liste meth pulbic
//comparer en parcourant les 2 listes pour dire "cet attribut prive na pas de getter"
//javaparser peut declencher des comportements en fonction du type de noeud sur lequel il est


    public List<String> attributsPrives = new ArrayList<>();
    public List<String> methodesPubliques = new ArrayList<>();

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) { //pr chaque class
        if(!declaration.isPublic()) return;
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        for(MethodDeclaration method : declaration.getMethods()) { //appele le visiteur de methoed
            method.accept(this, arg);
        }//ajoute boucle pour les attributs ici

        for(FieldDeclaration fields : declaration.getFields()) { //fields=attributs, ptetre devoir faire un visit pour les fields
            fields.accept(this, arg);
        }
        
        
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg){
        if(declaration.isPrivate()){
            System.out.println(" " + declaration.toString());//.toString()
            for (VariableDeclarator fieldInDecl : declaration.getVariables()) {
                attributsPrives.add(fieldInDecl.getNameAsString());
            }
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
    public void visit(MethodDeclaration declaration, Void arg) { //appelé qd noeud parcourut = methode
        if(!declaration.isPublic()) return;
        System.out.println("  " + declaration.getDeclarationAsString(true, true));
        methodesPubliques.add(declaration.getNameAsString()); //recupere le NOM de la methode publique et seulement son nom
    }

    /**
     * Compare la lsite des attributs privés et la liste des méthdodes publiques,
     * remplit dans une troisième liste les attributs n'ayant pas de getter,
     * puis renvoie un message selon si tous les attributs privés ont un getter ou pas.
     */
    public void hasGetter(){
        List<String> fieldWithoutGetter = new ArrayList<>(); //la liste des attributs sans getter

        for (String attribut : attributsPrives) {
            for (String methodePub : methodesPubliques) {
                if(!methodePub.toLowerCase().contains("get"+attribut.toLowerCase())){ //pb: contient TT le string de declaraiton pas seulement le nom de la meth ou att
                    fieldWithoutGetter.add(attribut);
                }
            }
        }

        /*for (String string : fieldWithoutGetter) {
            System.out.println("field without getter: "+string+" ");
        }*/

        if(!fieldWithoutGetter.isEmpty()){ //boucle pour afficher les attributs sans getter
            System.out.println("Ce / Ces attribut(s) n'ont pas de Getter associé: ");
            for (String field : fieldWithoutGetter) {
                System.out.println(field);
            }
        }
        else System.out.println("Tous les attributs privés ont un getter publique.");
    }
}
