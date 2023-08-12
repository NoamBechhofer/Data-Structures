package com.noambechhofer.algorithms;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

class SortTest {
    private static <E extends Comparable<E>> boolean isSorted(E[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i].compareTo(arr[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }

    private static <E extends Comparable<E>> boolean isSorted(List<E> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).compareTo(list.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }

    <E extends Comparable<E>> void testArraySortForCorrectness(E[] arr, Consumer<E[]> sort) {
        sort.accept(arr);
        assertTrue(isSorted(arr));
    }

    <E extends Comparable<E>> void testListSortForCorrectness(
            List<E> list, Consumer<List<E>> sort) {
        sort.accept(list);
        assertTrue(isSorted(list), String.format("list %s is not sorted", list.toString()));
    }

    /**
     * Generates a list of arrays to test sorting algorithms on. This method is
     * useful as a mini database of array edge cases.
     */
    private Integer[][] getTestArrays() {
        return new Integer[][] {
                // empty array
                new Integer[] {},
                // array of size 1
                new Integer[] { 1 },
                // array of size 2
                new Integer[] { 1, 2 },
                // array with duplicate elements
                new Integer[] { 1, 2, 3, 4, 5, 5, 4, 3, 2, 1 },
        };
    }

    private List<Integer>[] getTestList() {
        Function<Integer[], List<Integer>> lambda = (Integer[] arr) -> Arrays.asList(arr);

        return Arrays.stream(getTestArrays())
                .map(lambda)
                .toArray((size) -> (List<Integer>[]) Array.newInstance(List.class, size));
    }

    @Test
    void testInsertionSort() {
        Integer[][] arrays = getTestArrays();
        for (var arr : arrays) {
            testArraySortForCorrectness(arr, Sort::insertionSort);
        }

        List<Integer>[] lists = getTestList();
        for (var list : lists) {
            testListSortForCorrectness(list, Sort::insertionSort);
        }
    }

    @Test
    void testBinaryInsert() {
        var aList = Arrays.asList(new Integer[] {});
        assertEquals(
                "empty list, no item to insert",
                assertThrows(IllegalArgumentException.class, () -> {
                    Sort.binaryInsert(aList);
                }).getMessage());

        var b = new Integer[] { 0 };
        var bList = Arrays.asList(b);
        Sort.binaryInsert(b, 0, 0);
        Sort.binaryInsert(bList);
        assertArrayEquals(new Integer[] { 0 }, b);
        assertIterableEquals(Arrays.asList(0), bList);

        var c = new Integer[] { 1, 0 };
        var cList = Arrays.asList(c);
        Sort.binaryInsert(c, 0, 1);
        Sort.binaryInsert(cList);
        assertArrayEquals(new Integer[] { 0, 1 }, c);
        assertIterableEquals(Arrays.asList(0, 1), cList);

        var d = new Integer[] { 0, 1 };
        var dList = Arrays.asList(d);
        Sort.binaryInsert(d, 0, 1);
        Sort.binaryInsert(dList);
        assertArrayEquals(new Integer[] { 0, 1 }, d);
        assertIterableEquals(Arrays.asList(0, 1), dList);

        var e = new Integer[] { -1, 1, 0 };
        var eList = Arrays.asList(e);
        Sort.binaryInsert(e, 0, 2);
        Sort.binaryInsert(eList);
        assertArrayEquals(new Integer[] { -1, 0, 1 }, e);
        assertIterableEquals(Arrays.asList(-1, 0, 1), eList);

        var e2 = new Integer[] { -1, 1, 0 };
        var e2List = Arrays.asList(e2);
        Sort.binaryInsert(e2, 1, 2);
        Sort.binaryInsert(e2List.subList(1, 3));
        assertArrayEquals(new Integer[] { -1, 0, 1 }, e2);
        assertIterableEquals(Arrays.asList(-1, 0, 1), e2List);

        var f = new Integer[] { 1, 2, 0 };
        var fList = Arrays.asList(f);
        Sort.binaryInsert(f, 1, 2);
        Sort.binaryInsert(fList.subList(1, 3));
        assertArrayEquals(new Integer[] { 1, 0, 2 }, f);
        assertIterableEquals(Arrays.asList(1, 0, 2), fList);
        Sort.binaryInsert(f, 0, 1);
        Sort.binaryInsert(fList.subList(0, 2));
        assertArrayEquals(new Integer[] { 0, 1, 2 }, f);
        assertIterableEquals(Arrays.asList(0, 1, 2), fList);
        Sort.binaryInsert(f, 0, 2);
        Sort.binaryInsert(fList);
        assertArrayEquals(new Integer[] { 0, 1, 2 }, f);
        assertIterableEquals(Arrays.asList(0, 1, 2), fList);

        var g = new Integer[] { -2, -1, 0 };
        var gList = Arrays.asList(g);
        Sort.binaryInsert(g, 0, 2);
        Sort.binaryInsert(gList);
        assertArrayEquals(new Integer[] { -2, -1, 0 }, g);
        assertIterableEquals(Arrays.asList(-2, -1, 0), gList);

        assertEquals(
                "start -1 end 1: out of bounds for array of length 3",
                assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
                    Sort.binaryInsert(g, -1, 1);
                }).getMessage());

        assertEquals(
                "start 0 end -1: end must be greater than start",
                assertThrows(IllegalArgumentException.class, () -> {
                    Sort.binaryInsert(g, 0, -1);
                }).getMessage());
    }

    @Test
    void testBinaryInsertionSort() {
        Integer[][] arrays = getTestArrays();
        for (var arr : arrays) {
            testArraySortForCorrectness(arr, Sort::binaryInsertionSort);
        }

        List<Integer>[] lists = getTestList();
        for (var list : lists) {
            testListSortForCorrectness(list, Sort::binaryInsertionSort);
        }
    }

    @Test
    void testMerge() {
        Integer[] a = new Integer[] { 1, 7, 9, 10 };
        List<Integer> aList = Arrays.asList(Arrays.copyOf(a, a.length));
        Integer[] b = new Integer[] { 2, 3, 4, 5, 6, 8 };
        List<Integer> bList = Arrays.asList(Arrays.copyOf(b, b.length));

        assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, Sort.merge(a, b));
        assertIterableEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), Sort.merge(aList, bList));
    }

}
