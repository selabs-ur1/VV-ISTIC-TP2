# Code of your exercise

I modified this function
```java
public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
    if(!declaration.isPublic()) return;
    System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
    for(FieldDeclaration field : declaration.getFields()){
        if (!field.isPublic() && !hasGetter(field, declaration)) {
            writeFieldInfos(field, declaration);
        }
    }
}
```

Added those two
```java
private void writeFieldInfos(FieldDeclaration field, TypeDeclaration<?> classOrInterface) {
    PackageDeclaration packageDeclaration = classOrInterface.findCompilationUnit().get().getPackageDeclaration().orElse(null);
    String packageName = Objects.isNull(packageDeclaration) ? "" : packageDeclaration.getNameAsString() + ".";

    try {
        FileOutputStream outputFile = new FileOutputStream("no_getter_results.txt");
        PrintWriter pw = new PrintWriter(outputFile);

        pw.println(packageName + classOrInterface.getName() + "." + field.getVariable(0).getNameAsString() + " has no getter");

        pw.close();
        outputFile.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private boolean hasGetter(FieldDeclaration field, TypeDeclaration<?> classOrInterface) {
    String fieldName = field.getVariable(0).getNameAsString();
    String getterFieldName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

    for (MethodDeclaration method : classOrInterface.getMethods()) {
        if (method.isPublic() && method.getName().asString().equals(getterFieldName)) {
            return true;
        }
    }

    return false;
}
```

And deleted the `visit()` implementation with `EnumDeclaration` and `MethodDeclaration` as parameter.
