# Code of your exercise

### How to run
```shell
cd code/javaparser-starter
mvn install && java -jar target/javaparser-starter-1.0-jar-with-dependencies.jar `pwd`/target
```
A file `reportNoGetters.txt` will be created with a list of all attributes without getters in the form
`package.class::attribute`.  

Code :
```java
class PublicElementsPrinter{
    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        List<String> attributes = new ArrayList<>();
        //System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        for(FieldDeclaration field : declaration.getFields()){
            //ajout de tous les attributs à la liste
            attributes.add(field.getVariable(0).getNameAsString());
        }
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);

            //si attributes contient le nom de la methode sans "get" c'est que l'attribut a un getter
            //donc on l'enleve de la liste
            attributes.remove(method.getNameAsString().replace("get",""));
        }
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }

        attributes = attributes.stream().map(e -> e = declaration.getFullyQualifiedName().orElse("[Anonymous]") + "::" + e).collect(Collectors.toList());
        System.out.println("Attributes without getters : "+attributes);
        writeToFile(attributes);
    }

    private void writeToFile(List<String> items){
        try{
            FileWriter writer = new FileWriter("reportNoGetters.txt", true);
            for (String e : items) {
                writer.write(e+"\n");
            }
            writer.close();
        }
        catch (IOException e){
            System.err.println("Error writing report :"+e.getMessage());
            e.printStackTrace();
        }
    }
}
```
