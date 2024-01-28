# Cyclomatic Complexity with JavaParser

With the help of JavaParser implement a program that computes the Cyclomatic Complexity (CC) of all methods in a given Java project. The program should take as input the path to the source code of the project. It should produce a report in the format of your choice (TXT, CSV, Markdown, HTML, etc.) containing a table showing for each method: the package and name of the declaring class, the name of the method, the types of the parameters and the value of CC.
Your application should also produce a histogram showing the distribution of CC values in the project. Compare the histogram of two or more projects.


Include in this repository the code of your application. Remove all unnecessary files like compiled binaries. Do include the reports and plots you obtained from different projects. See the [instructions](../sujet.md) for suggestions on the projects to use.

You may use [javaparser-starter](../code/javaparser-starter) as a starting point.


## Answer


```java
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {
    public Map<Integer, Integer> map = new HashMap<Integer, Integer>();

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }
        System.out.println(declaration.getFullyQualifiedName().orElse("[unknown]"));
        for(MethodDeclaration method : declaration.getMethods()){
            AtomicInteger cc = new AtomicInteger(1);
            method.findAll(Statement.class).forEach(node -> {
                if(node instanceof IfStmt
                    || node instanceof ForStmt
                    || node instanceof WhileStmt
                    || node instanceof DoStmt
                    || node instanceof SwitchStmt
                    || node instanceof ForEachStmt
                    || node instanceof TryStmt){
                    cc.getAndIncrement();
                }
            });
            StringBuilder paramTypes = new StringBuilder();
            for (Parameter param : method.getParameters()) {
                paramTypes.append(param.getType().toString());
            }
            map.put(cc.get(), map.get(cc.get()) == null ? 1 : map.get(cc.get()) + 1);

            System.out.println("  " + method.getNameAsString() + " (" + paramTypes + ") : " + cc.get());
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
    public void visit(MethodDeclaration declaration, Void arg) {

        //System.out.println("  " + declaration.getDeclarationAsString(true, true));
    }

}
```
