import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

public class Main {
	
	private static Map<VariableDeclarator, String> getAllFields(NodeList<TypeDeclaration<?>> nodeList){
		Map<VariableDeclarator, String> res = new HashMap<VariableDeclarator, String>();
		for(TypeDeclaration<?> td: nodeList) {
			for(BodyDeclaration<?> bd: td.getMembers()) {
				if(bd.isFieldDeclaration()) {
					for(VariableDeclarator v: ((FieldDeclaration)bd).getVariables()) {
						res.put(v, td.getName().asString());
					}
				}
			}
		}
		return res;
	}
	
	private static MethodDeclaration checkAsGetter(VariableDeclarator v, NodeList<TypeDeclaration<?>> nodeList){
		String s = v.getName().asString();
		for(TypeDeclaration<?> td: nodeList) {
			for(BodyDeclaration<?> bd: td.getMembers()) {
				if(bd.isMethodDeclaration()) {
					String sName = Character.toUpperCase(s.charAt(0)) + s.substring(1);
					if(((MethodDeclaration)bd).getName().asString().equals("get"+sName) &&
							((MethodDeclaration)bd).getBody().isPresent() &&
							((MethodDeclaration)bd).getModifiers().size() == 1 &&
							((MethodDeclaration)bd).getModifiers().contains(Modifier.publicModifier())) {
						return ((MethodDeclaration)bd);
					}
				}
			}
		}
		return null;
	}
	
	private static Boolean checkOnlyReturnField(VariableDeclarator v, MethodDeclaration method){
		if(method.getParameters().isEmpty() && method.getType().equals(v.getType())
				&& method.getBody().get().getStatements().get(0).isReturnStmt()) {
			if(method.getBody().get().getStatements().get(0).asReturnStmt().getExpression().get().
					isNameExpr()) {
				return method.getBody().get().getStatements().get(0).asReturnStmt().getExpression().get().
						asNameExpr().getName().asString().equals(v.getName().asString());
			}
		}
		return false;
	}
	
	private static void checkFile(File file, BufferedWriter bw) throws IOException {
		JavaParser parser = new JavaParser();
		
		
		CompilationUnit cu = parser.parse(file).getResult().get();
		Map<VariableDeclarator, String> fields = getAllFields(cu.getTypes());
		for(VariableDeclarator v: fields.keySet()) {
			MethodDeclaration method = checkAsGetter(v, cu.getTypes());
			if(method == null || !checkOnlyReturnField(v, method)) {
				bw.write(v.getName().asString() + " : "
						+ fields.get(v) + " : " 
						+ cu.getPackageDeclaration().get().getName().asString() + "\n");
			}
		}
		
	}
	
	private static void recurseFile(File file, BufferedWriter bw) throws IOException {
		if(file.exists() && !file.getName().startsWith(".")) {
			if(file.isDirectory()){
				for(File f: file.listFiles()) {
					recurseFile(f, bw);
				}
			}else {
				if(file.getName().endsWith(".java")) checkFile(file, bw);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		
		if(args.length == 1 && Files.exists(Paths.get(args[0]))) {
			File res = new File("resultat.txt");
			res.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(res));
			
			recurseFile(new File(args[0]), bw);
			bw.close();
		}
		
	}

}
