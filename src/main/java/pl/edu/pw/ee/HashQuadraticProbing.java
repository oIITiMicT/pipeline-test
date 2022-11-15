package pl.edu.pw.ee;

public class HashQuadraticProbing<T extends Comparable<T>> extends HashOpenAdressing<T> {

    private final double firstFactor, secondFactor;

    public HashQuadraticProbing() {
        super();
        this.firstFactor = 0.75;
        this.secondFactor = 0.75;
    }

    public HashQuadraticProbing(int size, double firstFactor, double secondFactor) {
        super(size);
        if (Double.compare(0.0, firstFactor) == 0 || Double.compare(0.0, secondFactor) == 0) {
            throw new IllegalArgumentException("factors cannot be zero");
        }
        this.firstFactor = firstFactor;
        this.secondFactor = secondFactor;
    }

    public HashQuadraticProbing(int size) {
        super(size);
        this.firstFactor = 0.75;
        this.secondFactor = 0.75;
    }

    @Override
    int hashFunc(int key, int i) {
        return (int)(f(key) + firstFactor * i + secondFactor * i * i) % getSize();
    }
}
