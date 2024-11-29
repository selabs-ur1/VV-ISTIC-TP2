package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    private final File reportFile;
    private final Map<Integer, Integer> ccDistribution = new HashMap<>();

    public PublicElementsPrinter(File reportFile) {
        this.reportFile = reportFile;
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        // Parcours des types dans le fichier source
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        // On ne s'intéresse qu'aux classes publiques
        if (!declaration.isPublic()) return;

        // Parcours des méthodes de la classe
        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }

        // Parcours des membres imbriqués de type (pour les classes internes)
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration method, Void arg) {
        // Calcul de la complexité cyclomatique de la méthode
        int cc = calculateCyclomaticComplexity(method);

        // Enregistrement des données de la méthode dans le rapport
        String className = method.findAncestor(ClassOrInterfaceDeclaration.class).map(ClassOrInterfaceDeclaration::getNameAsString).orElse("Unknown");
        String packageName = method.findCompilationUnit().flatMap(CompilationUnit::getPackageDeclaration).map(pkg -> pkg.getNameAsString()).orElse("default");
        String methodName = method.getNameAsString();
        String params = getMethodParameters(method);

        try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile, true))) {
            writer.printf("| %s | %s | %s | %s | %d |\n", packageName, className, methodName, params, cc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Mise à jour de la distribution des CC
        ccDistribution.put(cc, ccDistribution.getOrDefault(cc, 0) + 1);
    }

    // Calcul de la complexité cyclomatique
    private int calculateCyclomaticComplexity(MethodDeclaration method) {
        int cc = 1; // La CC de base est 1 (pour la méthode elle-même)
        
        // Récupération des instructions dans le bloc de la méthode (s'il existe)
        NodeList<Statement> statements = method.getBody()
                                               .map(BlockStmt::getStatements)
                                               .orElse(new NodeList<>()); // NodeList<Statement> au lieu de List<Statement>

        // On parcourt les instructions et on compte les structures de contrôle
        for (Statement stmt : statements) {
            if (stmt instanceof IfStmt || stmt instanceof ForStmt || stmt instanceof WhileStmt || stmt instanceof SwitchStmt) {
                cc++; // Chaque instruction de contrôle ajoute 1 à la CC
            }
        }

        return cc;
    }


    // Extraction des paramètres de la méthode
    private String getMethodParameters(MethodDeclaration method) {
        StringBuilder sb = new StringBuilder();
        method.getParameters().forEach(param -> sb.append(param.getType().asString()).append(" ").append(param.getNameAsString()).append(", "));
        if (sb.length() > 0) sb.setLength(sb.length() - 2); // Supprimer la dernière virgule
        return sb.toString();
    }

    // Affichage de la distribution des CC dans la console (Histogramme)
    public void printCCDistribution() {
        System.out.println("Distribution des valeurs de la complexité cyclomatique : ");
        ccDistribution.forEach((cc, count) -> {
            System.out.println("CC = " + cc + ": " + "*".repeat(count));
        });
    }
    

    public void initializeReport() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {
            
            writer.println("\n| Package | Class | Method | Parameters | Cyclomatic Complexity |");
            writer.println("|---------|-------|--------|------------|-----------------------|");

            // Ajout de l'histogramme textuel dans le rapport
            writer.println("\n### Histogramme de la Complexité Cyclomatique (Distribution)");
            ccDistribution.forEach((cc, count) -> {
                String stars = "*".repeat(count); // Crée une barre de * représentant le nombre de méthodes avec ce CC
                try (PrintWriter writer2 = new PrintWriter(new FileWriter(reportFile, true))) {
                    writer2.printf("CC = %d: %s\n", cc, stars);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void ajouthisto() {
    	// Créer un fichier temporaire pour stocker les données avec le texte ajouté au début
        File tempFile = new File("temp_report.txt");

        // Copier le contenu du fichier original tout en ajoutant le texte au début
        try (PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {
            // Écrire le contenu à ajouter au début
            writer.println("# Cyclomatic Complexity Report\n");
            writer.println("### Distribution des valeurs de la complexité cyclomatique :");

            // Itérer sur chaque élément de la distribution et écrire dans le fichier temporaire
            ccDistribution.forEach((cc, count) -> {
                writer.printf("CC = %d: %s%n", cc, "*".repeat(count));  // Affiche les étoiles pour chaque CC
            });

            // Ensuite, copier le contenu du fichier original (s'il existe) dans le fichier temporaire
            if (reportFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(reportFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.println(line);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();  // Gérer les erreurs d'écriture
        }

        // Supprimer l'ancien fichier et renommer le fichier temporaire
        if (reportFile.delete()) {
            System.out.println("Ancien fichier supprimé.");
        }
        if (tempFile.renameTo(reportFile)) {
            System.out.println("Le fichier a été mis à jour avec succès.");
        } else {
            System.out.println("Erreur lors du renommage du fichier.");
        }
    }
}
