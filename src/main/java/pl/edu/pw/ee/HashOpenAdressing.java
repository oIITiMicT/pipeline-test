package pl.edu.pw.ee;

import pl.edu.pw.ee.services.HashTable;

public abstract class HashOpenAdressing<T extends Comparable<T>> implements HashTable<T> {

    private final T nil = null;
    private int size;
    private int nElems;
    private T[] hashElems;
    private final double correctLoadFactor;

    HashOpenAdressing() {
        this(2039); // initial size as random prime number
    }

    HashOpenAdressing(int size) {
        validateHashInitSize(size);

        this.size = size;
        this.hashElems = (T[]) new Comparable[this.size];
        this.correctLoadFactor = 0.75;
    }

    private class Del implements Comparable<Del> {
        @Override
        public int compareTo(HashOpenAdressing<T>.Del o) {
            return 0;
        }
    }

    @Override
    public void put(T newElem) {
        validateInputElem(newElem);
        resizeIfNeeded();

        int key = newElem.hashCode();
        int i = 0;
        int hashId = hashFunc(key, i);

        while (hashElems[hashId] != nil && hashElems[hashId].getClass() != Del.class) {
            if (hashElems[hashId] == newElem) { //avoid duplicates
                break;
            }
            i = (i + 1) % size;
            hashId = hashFunc(key, i);
        }
        if (hashElems[hashId] != newElem) {
            hashElems[hashId] = newElem;
            nElems++;
        }
    }

    private int getHashIdOfElem(T elem) {
        int i = 0;
        int key = elem.hashCode();
        int hashId = hashFunc(key, i);

        while (hashElems[hashId] != elem && hashElems[hashId] != nil) {
            i = (i + 1) % size;
            hashId = hashFunc(key, i);
        }

        return hashId;
    }

    @Override
    public T get(T elem) {
        if (nElems < 1) {
            throw new IllegalStateException("Cannot get from empty table");
        }

        if (elem == null) {
            throw new IllegalArgumentException("Cannot get a null element");
        }

        int hashId = getHashIdOfElem(elem);
        return hashElems[hashId];
    }

    @Override
    public void delete(T elem) {
        if (nElems < 1) {
            throw new IllegalStateException("Cannot delete from empty table");
        }

        if (elem == null) {
            throw new IllegalArgumentException("Cannot delete a null element");
        }

        int hashId = getHashIdOfElem(elem);
        if (hashElems[hashId] != nil) {
            nElems--;
            hashElems[hashId] = (T) new Del();
        }
    }

    private void validateHashInitSize(int initialSize) {
        if (initialSize < 1) {
            throw new IllegalArgumentException("Initial size of hash table cannot be lower than 1!");
        }
    }

    private void validateInputElem(T newElem) {
        if (newElem == null) {
            throw new IllegalArgumentException("Input elem cannot be null!");
        }
    }

    abstract int hashFunc(int key, int i);

    int getSize() {
        return size;
    }

    private void resizeIfNeeded() {
        double loadFactor = countLoadFactor();

        if (loadFactor >= correctLoadFactor) {
            doubleResize();
        }
    }

    private double countLoadFactor() {
        return (double) nElems / size;
    }

    protected int f(int key) {
        return Math.abs(key % getSize());
    }

    private void doubleResize() {
        int index = 0;
        T[] oldElems = (T[]) new Comparable[nElems];
        for (T elem : hashElems) {
            if (elem != nil) {
                oldElems[index++] = elem;
            }
        }
        this.size *= 2;
        this.hashElems = (T[]) new Comparable[size];
        this.nElems = 0;
        for (T elem : oldElems) {
            if (elem != null) {
                put(elem);
            }
        }
    }

}