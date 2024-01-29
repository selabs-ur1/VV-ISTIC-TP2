# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

We ran the command two times with two different rulesets.

### The following one is the first run :

we have run the pmd command on the java parser project :
`pmd check -f text -R assets/quickstart.xml -d src/main/java -r output.`

See [quickstart.xml](./assets/quickstart.xml) for details of the ruleset.

the output errors found by pmd :

```shell
src/main/java/fr/istic/vv/Main.java:3:	UnnecessaryImport:	Unused import 'com.github.javaparser.Problem'
src/main/java/fr/istic/vv/Main.java:4:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.CompilationUnit'
src/main/java/fr/istic/vv/Main.java:5:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.body.ClassOrInterfaceDeclaration'
src/main/java/fr/istic/vv/Main.java:6:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.body.MethodDeclaration'
src/main/java/fr/istic/vv/Main.java:7:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.visitor.VoidVisitor'
src/main/java/fr/istic/vv/Main.java:8:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.visitor.VoidVisitorAdapter'
src/main/java/fr/istic/vv/Main.java:13:	UnnecessaryImport:	Unused import 'java.nio.file.Path'
src/main/java/fr/istic/vv/Main.java:14:	UnnecessaryImport:	Unused import 'java.nio.file.Paths'
src/main/java/fr/istic/vv/Main.java:16:	UseUtilityClass:	This utility class has a non-private constructor
src/main/java/fr/istic/vv/PublicElementsPrinter.java:4:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.body.*'
src/main/java/fr/istic/vv/PublicElementsPrinter.java:20:	ControlStatementBraces:	This statement should have braces
src/main/java/fr/istic/vv/PublicElementsPrinter.java:28:	ControlStatementBraces:	This statement should have braces
src/main/java/fr/istic/vv/PublicElementsPrinter.java:44:	ControlStatementBraces:	This statement should have braces
```

We have removed the unnused imports in the main.java class to resolve some errors :

the errors output :
```shell
cat output
src/main/java/fr/istic/vv/Main.java:11:	UseUtilityClass:	This utility class has a non-private constructor
src/main/java/fr/istic/vv/PublicElementsPrinter.java:4:	UnnecessaryImport:	Unused import 'com.github.javaparser.ast.body.*'
src/main/java/fr/istic/vv/PublicElementsPrinter.java:20:	ControlStatementBraces:	This statement should have braces
src/main/java/fr/istic/vv/PublicElementsPrinter.java:28:	ControlStatementBraces:	This statement should have braces
src/main/java/fr/istic/vv/PublicElementsPrinter.java:44:	ControlStatementBraces:	This statement should have braces
```

See [`captures PMD 1 & 2`](./assets)

### Second run :

Command `pmd check -f text --rulesets=assets/bestpractices.xml -d src/main/java -r output` ran in javaparser-starter project using a complete ruleset found in the official git repo.

See [bestpractices.xml](./assets/bestpractices.xml) for details of the ruleset.

Output of the previous command can be found [`here`](./assets/output).

An issue found that could be solved (true positive) would be :

LocalVariableCouldBeFinal:	Local variable 'file' could be declared final

This concerns a variable which could be declared final because the value stays the same during all the execution of our code, we just have to add final keyword.

An issue found that should not be solved (false positive) would be :

ShortClassName:	Avoid short class names like Main

I would not solve this issue because short class name are not necessarily bad, in this case : Main is comprehensive enough
because it concerns the main class executing all the code.