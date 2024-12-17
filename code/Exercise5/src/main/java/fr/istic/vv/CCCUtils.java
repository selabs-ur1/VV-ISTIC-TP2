package fr.istic.vv;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CCCUtils {

    public static String currentClassName;
    private static String table = "";// "packageName.ClassName methodName(param) CCvalue\n";
    private final static Map<Integer, Integer> histo = new HashMap<>();

    /**
     * Stocke le nombre de noeuds et d'arcs sur le graphe de flot d'une méthode
     */
    public static class MethodFlow {
        public final String c;
        public final String m;
        private int n;// noeuds
        private int e;// arc

        public MethodFlow(String methodName) {
            c = currentClassName;
            m = methodName;
            n = 2;// noeuds Debut et Fin
            e = 1;// arc sortant de Debut
        }

        /**
         * "Node edge"
         */
        public void plus(int nodes, int edges, String reminder) {
            n += nodes;
            e += edges;
        }

        public int getN() {
            return n;
        }

        public int getE() {
            return e;
        }
    }

    /**
     * 
     * @param flot
     */
    public static void includeToResult(MethodFlow flot) {
        // v(G)=E−V+2P, mais P=1 car le calcul porte sur la méthode seule donc CC =
        // E+2-V
        int cc = flot.e + 2 - flot.n;
        table = table + '\n' + flot.c + ' ' + flot.m + ' ' + cc;

        Integer oldVal = histo.remove(cc);
        histo.put(cc, 1 + ((oldVal == null) ? 0 : oldVal));
    }

    public static String getResultPrint() {
        table = table + "\n\n";
        for (Entry<Integer, Integer> entry : histo.entrySet()) {
            String line = "*".repeat(entry.getValue());
            table = table + entry.getKey() + ' ' + line + "\n";
        }
        return table;
    }
}
