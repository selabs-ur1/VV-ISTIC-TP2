import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;

public class Main {
	
	private static Map<String, List<MethodDeclaration>> getAllFields(TypeDeclaration<?> type){
		Map<String, List<MethodDeclaration>> res = new HashMap<String, List<MethodDeclaration>>();
		for(BodyDeclaration<?> bd: type.getMembers()) {
			if(bd.isFieldDeclaration()) {
				for(VariableDeclarator v: ((FieldDeclaration)bd).getVariables()) {
					res.put(v.getName().asString(), new ArrayList<MethodDeclaration>());
				}
			}
		}
		return res;
	}
	
	private static List<MethodDeclaration> getAllMethods(TypeDeclaration<?> type){
		List<MethodDeclaration> res = new ArrayList<MethodDeclaration>();
		for(BodyDeclaration<?> bd: type.getMembers()) {
			if(bd.isMethodDeclaration()) {
				res.add((MethodDeclaration) bd);
			}
		}
		return res;
	}
	
	private static void checkExpression(Expression e, Map<String, List<MethodDeclaration>> fields, MethodDeclaration method) {
		if(e.isNameExpr()) {
			if(fields.containsKey(e.asNameExpr().getNameAsString())) {
	        	fields.get(e.asNameExpr().getNameAsString()).add(method);
	        }
		}
		if(e.isAssignExpr()) {
			checkExpression(e.asAssignExpr().getTarget(), fields, method);
			checkExpression(e.asAssignExpr().getValue(), fields, method);
		}
		if(e.isMethodCallExpr()) {
			if(e.asMethodCallExpr().getScope().isPresent())
				checkExpression(e.asMethodCallExpr().getScope().get(), fields, method);
		}
	}
	
	private static void checkStatement(Statement s, Map<String, List<MethodDeclaration>> fields, MethodDeclaration method) {
		if(s.isReturnStmt()) {
			if(s.asReturnStmt().getExpression().isPresent()){
				checkExpression(s.asReturnStmt().getExpression().get(), fields, method);
			}
		}
		if(s.isBlockStmt()) {
			for(Statement s2: s.asBlockStmt().getStatements()) {
				checkStatement(s2, fields, method);
			}
		}
		if(s.isForStmt()) {
			for(Expression e: s.asForStmt().getInitialization()){
				checkExpression(e, fields, method);
			}
			checkStatement(s.asForStmt().getBody(), fields, method);
		}
		if(s.isForEachStmt()) {
			checkExpression(s.asForEachStmt().getIterable(), fields, method);
			checkStatement(s.asForEachStmt().getBody(), fields, method);
		}
		if(s.isWhileStmt()) {
			checkExpression(s.asWhileStmt().getCondition(), fields, method);
			checkStatement(s.asWhileStmt().getBody(), fields, method);
		}
		if(s.isIfStmt()) {
			checkExpression(s.asIfStmt().getCondition(), fields, method);
			checkStatement(s.asIfStmt().getThenStmt(), fields, method);
			if(s.asIfStmt().getElseStmt().isPresent())
				checkStatement(s.asIfStmt().getElseStmt().get(), fields, method);
		}
		if(s.isSwitchStmt()) {
			checkExpression(s.asSwitchStmt().getSelector(), fields, method);
			for(SwitchEntry e: s.asSwitchStmt().getEntries()) {
				for(Statement s2: e.getStatements()) {
					checkStatement(s2, fields, method);
				}
			}
		}
		if(s.isExpressionStmt()) {
			checkExpression(s.asExpressionStmt().getExpression(), fields, method);
		}
	}
	
	private static void checkFile(File file, BufferedWriter bw) throws IOException {
		JavaParser parser = new JavaParser();
		
		
		CompilationUnit cu = parser.parse(file).getResult().get();
		for(TypeDeclaration<?> type: cu.getTypes()) {
			Map<String, List<MethodDeclaration>> fields = getAllFields(type);
			List<MethodDeclaration> methods = getAllMethods(type);
			for(MethodDeclaration method: methods) {
				if(method.getBody().isPresent()) {
					if(method.getBody().get().isBlockStmt()) {
						for(Statement s: method.getBody().get().asBlockStmt().getStatements()) {
							checkStatement(s, fields, method);
						}
					}
				}
			}
			int nbConn = 0;
			for(String v: fields.keySet()) {
				int nbMethods = fields.get(v).size();
				if(nbMethods > 1) {
					nbConn += nbMethods*(nbMethods-1)/2;
				}
			}
			bw.write("TCC = " + nbConn + " / " + (methods.size() * (methods.size()-1) / 2) + " : "
					+ type.getName().asString() + " : " 
					+ cu.getPackageDeclaration().get().getName().asString() + "\n");
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
