# Code of your exercise

Put here all the code created for this exercise

---

methods added to PublicsElementsPrinter.java:

```java

    /**
     * Map<K, V>
     *     K: class name
     *     V: list of private attributes w/o getter
     */
    private Map<String, List<String>> result = new HashMap<>();

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        List<String> attributes = new ArrayList<>();
        String className = declaration.getName().asString();

        for (FieldDeclaration field : declaration.getFields()) {
            //check for each attributes if it is private and has no getter
            if(field.isPrivate() && !hasGetterMethod(declaration, field)){
                attributes.add(field.getVariable(0).getName().asString());
            }
        }

        this.result.put(className, attributes);
        super.visit(declaration, arg);
    }

    //check if the attribut has getter
    private boolean hasGetterMethod(ClassOrInterfaceDeclaration clazz, FieldDeclaration f ) {
        String fieldName = f.getVariable(0).getNameAsString();
        String getter = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return clazz.getMethodsByName(getter)
                .stream()
                .anyMatch(methodDeclaration -> !methodDeclaration.getType().asString().equals("void"));
    }

    // Convert Map to Json File
    public void toJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            //Json Beautify
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            //write result into json file
            objectMapper.writeValue(new File("output.json"), this.result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

```

modification of main.java:

```java
public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if(!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        SourceRoot root = new SourceRoot(file.toPath());
        PublicElementsPrinter printer = new PublicElementsPrinter();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        //writting our result in json file
        printer.toJson();
    }

}
```

example of result:

```json
{
  "User" : [ "id", "name" ],
  "Book" : [ "id" ]
}
```
