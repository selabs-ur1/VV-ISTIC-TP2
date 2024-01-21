# Code of your exercise

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CC {
	
	static String  result = "";
	
	public static void main(String[] args) {
        String projectPath = "C:\\Users\\user\\git\\vv\\TP2\\VV-ISTIC-TP2\\code\\javaparser-starter"; // Replace with the actual path to your project

        try {
            measureCC(projectPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        try (PrintWriter out = new PrintWriter(new FileWriter("C:\\Users\\user\\Downloads\\CC.txt"))) {
			out.print(result);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	// A function that iterates recursively other a given path in order to find the subdirectories that hold .java files
	private static void scanForJavaFiles(File directory, List<File> javaFiles) {
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    scanForJavaFiles(file, javaFiles);
                } else if (file.getName().endsWith(".java")) {
                    javaFiles.add(file);
                }
            }
        }
	}
	
	// A function that measures iteratively the Cyclic Complexity (CC) value of the methods from the project found in the given path
	// This function uses the JavaParser's 'CompilationUnit' that iterates other the nodes of the program ASt (Abstract Syntax Tree)
	// The 'cu.accept()' calls the MethodVisitor's overriden 'visit()' method which in our case analyzes the 'MethodDeclaration' nodes of the given program's AST
	public static void measureCC(String path) throws FileNotFoundException {
		
		File projectDir = new File(path);
		
		List<File> javaFiles = new ArrayList<>();
		
		scanForJavaFiles (projectDir, javaFiles);
		
		String indent = "  ";
		String classIndent = indent + indent;
		
		for (File java: javaFiles) {
			CompilationUnit cu = StaticJavaParser.parse(java);
			String packageName = java.getParentFile().getName();
			
			result += "Package: " + packageName + '\n';
			
			List<ClassOrInterfaceDeclaration> listClasses = cu.findAll(ClassOrInterfaceDeclaration.class);
			for (ClassOrInterfaceDeclaration clazz: listClasses) {
				String className = clazz.getNameAsString();
				result += classIndent + "Class: " + className + '\n';
				
				cu.accept(new MethodVisitor(), null);
			}
		}
	}
	
	private static class MethodVisitor extends VoidVisitorAdapter<Void> {
		private String methodName;
		private int methodCC;
		private Map<String, String> methodParams = new HashMap<String, String>();
		
		private String indent = "   ";
		private String methodIndent = indent + indent + indent;
		private String ccIndent = methodIndent + indent;
		
		// Function that looks at the elements in the sub-tree of the 'MethodDeclaration' node of a given method and gathers all the relevant information and calls the cc measuring method
		@Override
		public void visit(MethodDeclaration method, Void arg) {
			super.visit(method, arg);
			
			methodParams = new HashMap<String, String>();
			
			this.methodName = method.getNameAsString();
			
			List<Parameter> methodParameters = method.getParameters();
			
			for(Parameter param: methodParameters) {
				String paramType = param.getTypeAsString();
				String paramName = param.getNameAsString();
				this.methodParams.put(paramType, paramName);
			}
			
			this.methodCC = measureMethodCC(method);
			
			printResult();
		}
		
		// Function that calculates the CC value of a gevin method based on the number and nature of decision points (e.g., number of conditions in an 'if'/'while' statement, number of cases in a 'switch' statement)
		private int measureMethodCC(MethodDeclaration method) {
			List<IfStmt> listIf = method.findAll(IfStmt.class);
			int ifCC = 0;
			
			for (IfStmt ifStmt: listIf) 
				ifCC += countNbConditions(ifStmt.getCondition());
			
			
			List<WhileStmt> listWhile = method.findAll(WhileStmt.class);
			int wlCC = 0;
			
			for (WhileStmt wl: listWhile)
				wlCC += countNbConditions(wl.getCondition());
			
			List<SwitchStmt> listSwitch = method.findAll(SwitchStmt.class);
			int swCC = 0;
			for (SwitchStmt swStmt: listSwitch)
				swCC += swStmt.getEntries().size();
			
			int nbEdges = ifCC + wlCC + swCC;
			
			List<ForStmt> listFor = method.findAll(ForStmt.class);
			
			int nbNodes = listFor.size() + listWhile.size() + listSwitch.size();
			
			return nbEdges - nbNodes + 2;
		}
		
		// Function that measures the number of conditions in an 'if'/'while' statement
		private int countNbConditions(Expression expr) {
			if (expr.isBinaryExpr()) {
				
				BinaryExpr binExpr = expr.asBinaryExpr();
				
				int leftCount = countNbConditions(binExpr.getLeft());
				int rightCount = countNbConditions(binExpr.getRight());
				
				return leftCount + rightCount;
			}
			else
				return 1;
		}
		
		// Function that completes the global String value with relevant information; the global variable will be used to output the information in an .txt file
		public void printResult() {
			
			result += methodIndent + "Method: " + methodName + '\n';
			result += ccIndent + "CC = " + methodCC + '\n';
			result += ccIndent + "Parameters: ";
			
			if (methodParams.size() == 0)
				result += "None";
			
			else
				for(Map.Entry<String, String> param: methodParams.entrySet()) 
					result += ' ' + param.getKey() + ' ' + param.getValue() + " |";
			
			result += "\n\n";
		}
	}
}