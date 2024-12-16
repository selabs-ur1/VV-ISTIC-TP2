package fr.istic.vv;

import com.github.javaparser.ast.type.TypeParameter;

import java.util.List;
import java.util.stream.Collectors;

//the package and name of the declaring class, the name of the method, the types of the parameters and the value of CC.
public record CyclicComplexityAnalysis(
        String packageName,
        String className,
        String methodeName,
        List<TypeParameter> typeParameters,
        int cyclicComplexity
) {

    public static String[] getCsvHeader() {
        return new String[] { "packageName", "className", "methodeName", "typeParameters", "cyclicComplexity" };
    }

    public String[] getCsvString() {
        final String typeParamString = typeParameters.stream()
                .map(TypeParameter::toString)
                .collect(Collectors.joining(","));
        return new String[] { packageName, className, methodeName, typeParamString, cyclicComplexity + "" };
    }
}
