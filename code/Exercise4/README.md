# Code of your exercise

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class PrivateVars {
	
	// Class that holds the relevant information about global variables; in this case, private variables that do not have a public getter
	private static class VarInfo {
		private String varName;
		private String className;
		private String packageName;
		
		public VarInfo(String varName, String className, String packageName) {
			super();
			this.varName = varName;
			this.className = className;
			this.packageName = packageName;
		}
		
		public String toString() {
			return "Variable: " + varName + " | Class: " + className + " | Package: " + packageName;
		}
	}
	
	// Function that iterates other the subdirectories of a given path using the 'walkFileTree()' method, and calls the 'findPrivateVars()' function that returns private vars with the relevant information.
	public static List<VarInfo> noGetterVars(String path) throws FileNotFoundException {
		
		ArrayList<VarInfo> noGetterVars = new ArrayList<>();
		
		try {
			Files.walkFileTree(Paths.get(path), Set.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
				
				@Override
				public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) {
					if (filePath.toString().endsWith(".java")) {
						
						try {
							CompilationUnit cu = StaticJavaParser.parse(filePath.toFile());
							findPrivateVars(cu, noGetterVars, filePath);
							
						} catch (FileNotFoundException e){
							e.printStackTrace();
						}
					}
					return FileVisitResult.CONTINUE;
				}
				
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return noGetterVars;
	}
	
	// A function that uses JavaParser's 'CompilationUnit' in order to find private global variables
	private static void findPrivateVars(CompilationUnit cu, List<VarInfo> noGetterVars, Path path) {
		List<ClassOrInterfaceDeclaration> listClasses = cu.findAll(ClassOrInterfaceDeclaration.class);
		
		for(ClassOrInterfaceDeclaration clazz: listClasses) {
			List<FieldDeclaration> allVars = clazz.findAll(FieldDeclaration.class);
			List<FieldDeclaration> privateVars = new ArrayList<FieldDeclaration>();
			
			for(FieldDeclaration var: allVars) {
				if(var.isPrivate())
					privateVars.add(var);
			}
			
			for (FieldDeclaration field: privateVars) {
				String varName = field.getVariable(0).getNameAsString();
				String className = clazz.getNameAsString();
				String packageName = path.getParent().getFileName().toString();
				
				List<MethodDeclaration> varGetter = clazz.getMethodsByName("get" + var2UpperCase(varName));
				
				if (varGetter.isEmpty() || !varGetter.get(0).isPublic()) 
					noGetterVars.add(new VarInfo(varName, className, packageName));
			}
		}
	}
	
	// Function that writes in a .txt file the private variables with all the relevant information using the VarInfo's 'toString()' method
	public static void var2TextFile(List<VarInfo> noGetterVars) throws IOException{
		
		try (PrintWriter out = new PrintWriter(new FileWriter("path/to/file/destination"))) {
			
			for (VarInfo varInfo: noGetterVars)
				out.println(varInfo);
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	// Function that returns the name of a declared variable with the first letter to upper case
	// Used in 'privateVars()' function in order to create the name of the variable's getter method
	private static String var2UpperCase(String var) {
        return var.substring(0, 1).toUpperCase() + var.substring(1);
    }
    
	
	public static void main(String[] args) {
		
		String projectPath = "path/to/the/project";
        try {
            List<VarInfo> noGetterVars = noGetterVars(projectPath);
            
            for (VarInfo fieldInfo : noGetterVars) {
                System.out.println(fieldInfo);
            }
            
            try {
            	var2TextFile(noGetterVars);
            } catch (IOException e){
            	e.printStackTrace();
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}	
}