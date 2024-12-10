# Faux positif et vrai positif
````java
public static void main(String[] args) {
    try {
        BufferedReader reader = new BufferedReader(new FileReader("fichier.txt"));
        Stream<String> lines = reader.lines();
        processStream(lines);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
public static void processStream(Stream<String> stream) {
    try {
        stream.filter(line -> line.contains("test"))
                .forEach(System.out::println);
    }
    catch(Exception e){

    } finally {
        closeStream(stream);
    }
}

public static void closeStream(Stream<String> stream) {
    stream.close();
}
````
### Faux positif
PMD nous affiche ``.\src\main\java\TP2\Main.java:17:	CloseResource:	Ensure that resources like this BufferedReader object are closed after use``
Or comme ce n'est qu'un analyseur statique, il ne comprends pas que la méthode ``closeStream(...)`` ferme le flux.

### Vrai positif
Ici il nous affiche ``.\src\main\java\TP2\Main.java:29:	EmptyCatchBlock:	Avoid empty catch blocks`` et il a raison