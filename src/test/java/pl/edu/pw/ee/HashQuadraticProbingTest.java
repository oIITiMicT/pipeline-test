package pl.edu.pw.ee;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.edu.pw.ee.services.HashTable;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HashQuadraticProbingTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private int getNumberOfElements(HashTable<?> hashTable) {
        try {
            if (hashTable == null) {
                throw new IllegalArgumentException("hashTable cannot be null");
            }
            final String neededField = "nElems";
            Field field = hashTable.getClass().getSuperclass().getDeclaredField(neededField);
            field.setAccessible(true);
            int numberOfElements = field.getInt(hashTable);
            field.setAccessible(true);
            return numberOfElements;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void Should_ThrowIllegalArgumentException_When_TableSizeLesserThanOne() {
        int illegalSize = 0;
        String expectedMessage = "Initial size of hash table cannot be lower than 1!";
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(expectedMessage);
        HashTable<String> hashTable = new HashQuadraticProbing<>(illegalSize);
    }

    @Test
    public void Should_AvoidPutDuplicates_When_TryToPutTwoSameElements() {
        int expectedCount = 1;
        HashTable<String> hashTable = new HashQuadraticProbing<>();
        String elem = "element";
        hashTable.put(elem);
        hashTable.put(elem);
        assertEquals(expectedCount, getNumberOfElements(hashTable));
    }

    @Test
    public void Should_IncreaseCountOfElement_When_PutUniqueElement() {
        HashTable<Integer> hashTable = new HashQuadraticProbing<>();
        for (int i = 1; i <= 3; i++) {
            hashTable.put(i);
            assertEquals(i, getNumberOfElements(hashTable));
        }
    }

    @Test
    public void Should_NotResize_When_TryToPutDuplicate() {
        int initialSize = 2;
        int expectedSize = 2;
        int elem = 1;
        HashTable<Integer> hashTable = new HashQuadraticProbing<>(initialSize);
        for (int i = 1; i <= 10; i++) {
            hashTable.put(elem);
        }
        assertEquals(expectedSize, ((HashOpenAdressing<Integer>) hashTable).getSize());
    }

    @Test
    public void Should_DoubleResize_When_LoadFactorBeforePutIsBiggerThanNeeded() {
        int size = 1;
        int expectedSize = 2;
        HashTable<String> hashTable = new HashQuadraticProbing<>(size);
        String firstElem = "element1";
        String secondElem = "element2";
        hashTable.put(firstElem);
        hashTable.put(secondElem);
        assertEquals(expectedSize, ((HashOpenAdressing<String>) hashTable).getSize());
    }

    @Test
    public void Should_ThrowIllegalArgumentException_When_TryToPutNull() {
        HashTable<String> hashTable = new HashQuadraticProbing<>();
        String expectedMessage = "Input elem cannot be null!";
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(expectedMessage);
        hashTable.put(null);
    }

    @Test
    public void Should_ThrowIllegalArgumentException_When_TryToGetNull() {
        HashTable<String> hashTable = new HashQuadraticProbing<>();
        String elem = "element";
        hashTable.put(elem);
        String expectedMessage = "Cannot get a null element";
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(expectedMessage);
        hashTable.get(null);
    }

    @Test
    public void Should_ThrowIllegalStateException_When_TryToGetFromEmptyTable() {
        HashTable<String> hashTable = new HashQuadraticProbing<>();
        String elem = "element";
        String expectedMessage = "Cannot get from empty table";
        exception.expect(IllegalStateException.class);
        exception.expectMessage(expectedMessage);
        hashTable.get(elem);
    }

    @Test
    public void Should_ThrowIllegalStateException_When_TryToDeleteFromEmptyTable() {
        HashTable<String> hashTable = new HashQuadraticProbing<>();
        String elem = "element";
        String expectedMessage = "Cannot delete from empty table";
        exception.expect(IllegalStateException.class);
        exception.expectMessage(expectedMessage);
        hashTable.delete(elem);
    }

    @Test
    public void Should_ThrowIllegalArgumentException_When_TryToDeleteNull() {
        HashTable<String> hashTable = new HashQuadraticProbing<>();
        String elem = "element";
        hashTable.put(elem);
        String expectedMessage = "Cannot delete a null element";
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(expectedMessage);
        hashTable.delete(null);
    }

    @Test
    public void Should_GetElement_When_ElementIsPresentInTable() {
        HashTable<String> hashTable = new HashQuadraticProbing<>();
        String expectedElem = "element";
        hashTable.put(expectedElem);
        assertEquals(expectedElem, hashTable.get(expectedElem));
    }

    @Test
    public void ShouldGetNull_When_ElementIsNotPresentInTable() {
        HashTable<String> hashTable = new HashQuadraticProbing<>();
        String existingElem = "element";
        String notExistingElem = "not existing elem";
        hashTable.put(existingElem);
        assertNull(hashTable.get(notExistingElem));
    }

    @Test
    public void Should_DeleteElement_When_ElementIsPresentInTable() {
        HashTable<String> hashTable = new HashQuadraticProbing<>();
        String elem = "element";
        hashTable.put(elem);
        int numOfElementBeforeDelete = getNumberOfElements(hashTable);
        hashTable.delete(elem);
        int numOfElementAfterDelete = getNumberOfElements(hashTable);
        assertEquals(numOfElementAfterDelete + 1, numOfElementBeforeDelete);
    }

    @Test
    public void Should_NothingToDelete_When_ElementIsNotPresentInTable() {
        HashTable<String> hashTable = new HashQuadraticProbing<>();
        String existingElement = "element";
        String notExistingElement = "not existing element";
        hashTable.put(existingElement);
        int numOfElementBeforeDelete = getNumberOfElements(hashTable);
        hashTable.delete(notExistingElement);
        int numOfElementAfterDelete = getNumberOfElements(hashTable);
        assertEquals(numOfElementAfterDelete, numOfElementBeforeDelete);
    }

    @Test
    public void Should_ThrowIllegalArgumentException_When_CallConstructorWithFactorsEqualsZero() {
        int size = 1;
        double illegalFirstFactor = 0;
        double illegalSecondFactor = 0;
        String expectedMessage = "factors cannot be zero";
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(expectedMessage);
        HashTable<String> hashTable = new HashQuadraticProbing<>(size, illegalFirstFactor, illegalSecondFactor);
    }
}
