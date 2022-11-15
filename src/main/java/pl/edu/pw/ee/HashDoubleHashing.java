package pl.edu.pw.ee;

public class HashDoubleHashing<T extends Comparable<T>> extends HashOpenAdressing<T> {


    private final int value;

    private void checkStability(int size) {
        if (size % value == 0) {
            throw new IllegalStateException("a hash table with hashing type \"DoubleHashing\" with the selected value(" + value + ") and size(" + size +
                    ") may be unstable (there is a chance to get into a loop)");
        }
    }

    public HashDoubleHashing(int size) {
        super(size);
        this.value = 197;
        checkStability(size);
    }

    public HashDoubleHashing() {
        super();
        this.value = 197;
        checkStability(getSize());
    }

    @Override
    int hashFunc(int key, int i) {
        return (f(key) + g(key) * i) % getSize();
    }


    /*
        I did not use the provided function in the materials g = 1 + (key% (size - 3),
        because this function gets into a loop more often than the one I selected g = value - (key % value).
        I also found that such a function can get into a loop only if size % value = 0, so I created
        a checkStability function that will throw an exception if arguments can disrupt the stability of the function.
        To allow the table to be created even with the threat of looping, it is enough to remove the call to the checkStabilty function in both constructors
        P.S. the implemented checkStability function will work only if, when increasing the size of the table,
        we use a multiplier(x) that meets the requirement of value % x = 0.
        In the current implementation, we use double resize, and value = 3, so we avoid the threat of looping
     */
    private int g(int key) {
        return (value - (key % value));
    }

}
