package com.noambechhofer.datastructures;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Random;

import org.junit.jupiter.api.Test;
// import com.noambechhofer.datastructures.DuplicateElementException;

import com.noambechhofer.datastructures.utils.DuplicateElementException;

public class SetListTest {
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
    public void testAdd() {
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
    public void testClear() {

    }

    @Test
    public void testContains() {
        SetList<Integer> list = new SetList<>();

        list.add(1);

        assertTrue(list.contains(1));
        assertFalse(list.contains(2));
    }

    @Test
    public void testContainsAll() {

    }

    @Test
    public void testGet() {

    }

    @Test
    public void testIndexOf() {

    }

    @Test
    public void testIsEmpty() {

    }

    @Test
    public void testIterator() {

    }

    @Test
    public void testLastIndexOf() {

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
    public void testSet() {

    }

    @Test
    public void testSize() {

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
