package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Afficher le menu principal
        System.out.println("=== Menu d'Analyse de Code ===");
        System.out.println("1. Analyse des champs privés sans getters publics");
        System.out.println("2. Analyse de la complexité cyclomatique");
        System.out.println("3. Analyse des éléments publics");
        System.out.println("0. Quitter");
        System.out.print("Choisissez une option : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer le saut de ligne après l'entrée

        // Demander le chemin du projet source
        System.out.print("Entrez le chemin du projet source : ");
        String cheminProjet = scanner.nextLine();
        File projet = new File(cheminProjet);

        if (!projet.exists() || !projet.isDirectory() || !projet.canRead()) {
            System.err.println("Le chemin fourni est invalide ou non accessible.");
            System.exit(1);
        }

        // Initialiser SourceRoot
        SourceRoot root = new SourceRoot(projet.toPath());

        // Exécuter l'analyse sélectionnée
        switch (choix) {
            case 1:
                System.out.println("=== Analyse des champs privés sans getters publics ===");
                PrivateFieldNoGetterPrinter privateFieldAnalyzer = new PrivateFieldNoGetterPrinter();
                root.parse("", (localPath, absolutePath, result) -> {
                    result.ifSuccessful(unit -> unit.accept(privateFieldAnalyzer, null));
                    return SourceRoot.Callback.Result.DONT_SAVE;
                });
                String privateFieldsReportPath = "private_fields_no_getters.csv";
                privateFieldAnalyzer.generateReport(privateFieldsReportPath);
                System.out.println("Rapport généré : " + privateFieldsReportPath);
                break;

            case 2:
                System.out.println("=== Analyse de la complexité cyclomatique ===");
                CyclomaticComplexityCalculator ccAnalyzer = new CyclomaticComplexityCalculator();
                root.parse("", (localPath, absolutePath, result) -> {
                    result.ifSuccessful(ccAnalyzer::analyze);
                    return SourceRoot.Callback.Result.DONT_SAVE;
                });
                String ccReportPath = "cyclomatic_complexity.csv";
                ccAnalyzer.generateReport(ccReportPath);
                System.out.println("Rapport généré : " + ccReportPath);
                break;

            case 3:
                System.out.println("=== Analyse des éléments publics ===");
                PublicElementsPrinter publicElementsAnalyzer = new PublicElementsPrinter();
                root.parse("", (localPath, absolutePath, result) -> {
                    result.ifSuccessful(unit -> unit.accept(publicElementsAnalyzer, null));
                    return SourceRoot.Callback.Result.DONT_SAVE;
                });
                break;

            case 0:
                System.out.println("Programme terminé.");
                System.exit(0);
                break;

            default:
                System.err.println("Option invalide. Veuillez réessayer.");
                break;
        }

        scanner.close();
    }
}