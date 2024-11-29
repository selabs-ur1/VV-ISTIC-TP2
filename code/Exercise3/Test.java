public class Test {
    public void test() {

        if (true) {
            if (true) {
                if (true) {
                    // should warn for this nested if
                }
            }
        }

        if (false) {
            if (false) {
                if (false) {
                    // should warn for this nested if
                }
            }
        }

        if (false) {
            if (false) {
                while (true) {
                    if (false) {
                        // should warn for this nested if
                    }
                }
            }
        }

        if (false) {
            if(false){
                while (false){
                    // should not warn for this
                }
            }
        }

        if (false) {
            if(false){

            }
            if(false){

            }
            if(false){
                // should not warn for this nested if
            }
        }
    }

    public static int size(final Object object) {
        if (object == null) {
            return 0;
        }
        int total = 0;
        if (object instanceof Map<?, ?>) {
            total = ((Map<?, ?>) object).size();
        } else if (object instanceof Collection<?>) {
            total = ((Collection<?>) object).size();
        } else if (object instanceof Iterable<?>) {
            total = IterableUtils.size((Iterable<?>) object);
        } else if (object instanceof Object[]) {
            total = ((Object[]) object).length;
        } else if (object instanceof Iterator<?>) {
            total = IteratorUtils.size((Iterator<?>) object);
        } else if (object instanceof Enumeration<?>) {
            final Enumeration<?> it = (Enumeration<?>) object;
            while (it.hasMoreElements()) {
                total++;
                it.nextElement();
            }
        } else {
            try {
                total = Array.getLength(object);
            } catch (final IllegalArgumentException ex) {
                throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
            }
        }
        return total;
    }
}