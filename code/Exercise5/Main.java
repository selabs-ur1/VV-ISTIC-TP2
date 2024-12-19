package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Vous devez fournir le chemin vers le code source et le chemin du fichier de rapport.");
            System.exit(1);
        }

        // Vérification du répertoire source
        File sourceDir = new File(args[0]);
        if (!sourceDir.exists() || !sourceDir.isDirectory() || !sourceDir.canRead()) {
            System.err.println("Veuillez fournir un chemin vers un répertoire existant et lisible.");
            System.exit(2);
        }

        // Chemin du fichier de rapport
        String reportPath = args[1];
        File reportFile = new File(reportPath);
        if (!reportFile.exists()) {
            reportFile.createNewFile();
        }

        // Initialisation de SourceRoot pour analyser les fichiers Java
        SourceRoot root = new SourceRoot(sourceDir.toPath());

        // Visiteur pour calculer la complexité cyclomatique et enregistrer les résultats
        PublicElementsPrinter printer = new PublicElementsPrinter(reportFile);

        // Initialisation du rapport README.md
        printer.initializeReport();

        // Analyse des fichiers Java dans le répertoire source
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> {
                unit.accept(printer, null); // Appliquer le visiteur à chaque fichier
            });
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        // Affichage de l'histogramme de la distribution des CC
        printer.printCCDistribution();
        
        printer.ajouthisto();
    }
}
