package fr.istic.vv;

import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length < 2) {
            System.err.println("Vous devez fournir le chemin vers le code source et le chemin du fichier de rapport.");
            System.exit(1);
        }

        File file = new File(args[0]);
        if(!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Veuillez fournir un chemin vers un répertoire existant et lisible");
            System.exit(2);
        }

        // Chemin du fichier de rapport fourni en argument
        String reportPath = args[1];
        File reportFile = new File(reportPath);
        if (!reportFile.exists()) {
            // Si le fichier n'existe pas, le créer
            reportFile.createNewFile();
        }

        // Initialisation du SourceRoot pour analyser les fichiers
        SourceRoot root = new SourceRoot(file.toPath());

        // Le visiteur pour analyser les éléments publics dans les fichiers
        PublicElementsPrinter printer = new PublicElementsPrinter();

        // Analyse des fichiers dans le répertoire et ses sous-répertoires
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> {
                // Appel à la méthode qui identifie les champs privés sans getter public
                NoGetters(unit, reportFile);
                unit.accept(printer, null);  // Votre visiteur pour les éléments publics
            });
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
    }

    // Méthode pour trouver les attributs privés sans getter public
    private static void NoGetters(CompilationUnit unit, File reportFile) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            // Vérifier que la classe est publique
            if (type.isPublic()) {
                for (FieldDeclaration field : type.getFields()) {
                    // Filtrer les champs privés
                    if (field.isPrivate()) {
                        String fieldName = field.getVariables().get(0).getNameAsString();
                        boolean hasGetter = false;

                        // Vérifier si un getter public existe pour ce champ
                        for (MethodDeclaration method : type.getMethods()) {
                            if (method.isPublic() && method.getNameAsString().equalsIgnoreCase("get" + fieldName)) {
                                hasGetter = true;
                                break;
                            }
                        }

                        // Enregistrer dans le fichier de rapport si pas de getter public
                        if (!hasGetter) {
                            String className = type.getNameAsString();
                            String packageName = unit.getPackageDeclaration().map(pkg -> pkg.getNameAsString()).orElse("default");
                            try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile, true))) {
                                // Ajout des informations au fichier rapport.md
                                writer.println("### Champ sans getter public");
                                writer.println("- **Champ** : " + fieldName);
                                writer.println("- **Classe** : " + className);
                                writer.println("- **Package** : " + packageName);
                                writer.println();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
