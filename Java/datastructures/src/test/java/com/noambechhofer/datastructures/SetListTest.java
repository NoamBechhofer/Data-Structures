package com.noambechhofer.datastructures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
// import com.noambechhofer.datastructures.DuplicateElementException;

import com.noambechhofer.datastructures.utils.DuplicateElementException;

public class SetListTest {
        @Test
    void testSize() {
        SetList<String> list = new SetList<>();
        assertEquals(0, list.size());
        list.add("Test");
        assertEquals(1, list.size());
    }

    @Test
    void testIsEmpty() {
        SetList<String> list = new SetList<>();
        assertTrue(list.isEmpty());
        list.add("Test");
        assertFalse(list.isEmpty());
    }

    @Test
    void testContains() {
        SetList<String> list = new SetList<>();
        list.add("Test");
        assertTrue(list.contains("Test"));
        assertFalse(list.contains("Other"));
    }

    @Test
    void testAdd() {
        SetList<String> list = new SetList<>();
        assertTrue(list.add("Test"));
        assertFalse(list.add("Test"));
        assertEquals(1, list.size());
    }

    @Test
    void testAddAtIndex() {
        SetList<String> list = new SetList<>();
        list.add(0, "Test");
        assertThrows(DuplicateElementException.class, () -> list.add(0, "Test"));
        assertEquals(1, list.size());
    }

    @Test
    void testRemoveObject() {
        SetList<String> list = new SetList<>();
        list.add("Test");
        assertTrue(list.remove("Test"));
        assertFalse(list.remove("Test"));
    }

    @Test
    void testRemoveAtIndex() {
        SetList<String> list = new SetList<>();
        list.add("Test");
        assertEquals("Test", list.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
    }

    @Test
    void testClear() {
        SetList<String> list = new SetList<>();
        list.add("Test");
        list.clear();
        assertEquals(0, list.size());
    }

    @Test
    void testGet() {
        SetList<String> list = new SetList<>();
        list.add("Test");
        assertEquals("Test", list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    void testSet() {
        SetList<String> list = new SetList<>();
        list.add("Test");
        assertThrows(DuplicateElementException.class, () -> list.set(0, "Test"));
        assertEquals("Test", list.set(0, "NewValue"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(1, "Other"));
    }

    @Test
    void testIndexOf() {
        SetList<String> list = new SetList<>();
        list.add("Test");
        assertEquals(0, list.indexOf("Test"));
        assertEquals(-1, list.indexOf("Other"));
    }

    @Test
    void testLastIndexOf() {
        SetList<String> list = new SetList<>();
        list.add("Test");
        assertEquals(0, list.lastIndexOf("Test"));
        assertEquals(-1, list.lastIndexOf("Other"));
    }

    @Test
    public void testSort() {
        SetList<Integer> sl = new SetList<>();
        Random rn = new Random();

        for (int i = 0; i < 10000; i++)
            try {
                sl.add(rn.nextInt());
            } catch (DuplicateElementException e) {
                continue;
            }

        Collections.sort(sl);

        // now assert sorted
        int tmp = sl.get(0);
        for (int i = 1; i < sl.size(); i++) {
            System.out.println(tmp);
            assertTrue(sl.get(i) > tmp);
            tmp = sl.get(i);
        }
        System.out.println(tmp);

    }

    @Test
    public void testAdd3() {
        SetList<Integer> sl = new SetList<>();

        sl.add(1);

        assertTrue(sl.contains(1));

        assertFalse(sl.add(1));

    }

    @Test
    public void testAdd2() {
        SetList<Integer> sl = new SetList<>();

        sl.add(0, 1);
        assertTrue(sl.contains(1));

        sl.add(1, 2);
        assertTrue(sl.contains(2));

        sl.add(0, 3);
        assertTrue(sl.contains(3));

        assertThrows(DuplicateElementException.class, () -> sl.add(0, 2));

        assertThrows(IndexOutOfBoundsException.class, () -> sl.add(4, 4));
    }

    @Test
    public void testAddAll() {

    }

    @Test
    public void testAddAll2() {

    }

    @Test
    public void testClear2() {

    }

    @Test
    public void testContains2() {
        SetList<Integer> list = new SetList<>();

        list.add(1);

        assertTrue(list.contains(1));
        assertFalse(list.contains(2));
    }

    @Test
    public void testContainsAll() {

    }

    @Test
    public void testGet2() {
        SetList<Integer> list = new SetList<>();

        list.add(1);
        list.add(2);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }

    @Test
    public void testIndexOf2() {
        SetList<Integer> list = new SetList<>();

        list.add(1);
        list.add(2);

        assertEquals(0, list.indexOf(1));
        assertEquals(1, list.indexOf(2));
    }

    @Test
    public void testIsEmpty2() {
        List<Integer> list = new SetList<>();
        assertTrue(list.isEmpty());
        list.add(1);
        assertFalse(list.isEmpty());
        list.remove(0);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testIterator() {

    }

    @Test
    public void testListIterator() {

    }

    @Test
    public void testListIterator2() {

    }

    @Test
    public void testRemove() {

    }

    @Test
    public void testRemove2() {

    }

    @Test
    public void testRemoveAll() {

    }

    @Test
    public void testRetainAll() {

    }

    @Test
    public void testSubList() {

    }

    @Test
    public void testToArray() {

    }

    @Test
    public void testToArray2() {
        SetList<Integer> sl = new SetList<>();

        sl.add(1);
        sl.add(2);

        final String[] strArrSmall = new String[0];
        assertThrows(ArrayStoreException.class, () -> sl.toArray(strArrSmall));

        final String[] strArrBig = new String[2];
        assertThrows(ArrayStoreException.class, () -> sl.toArray(strArrBig));

        final Object[] objArr = sl.toArray(new Object[10]);
        for (int i = 2; i < objArr.length; i++)
            assertNull(objArr[i]);

        final String[] nullArray = null;
        assertThrows(NullPointerException.class, () -> sl.toArray(nullArray));

        SetList<String> strList = new SetList<>();
        strList.add("This");
        strList.add("is");
        strList.add("a");
        strList.add("test.");

        strList.toArray(new String[0]);
    }
}
