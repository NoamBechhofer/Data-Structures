package com.noambechhofer.algorithms;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains static methods for sorting arrays and lists.
 * 
 * @author Noam Bechhofer
 */
public final class Sort {
    /**
     * This class should not be instantiated.
     */
    private Sort() {
    }

    /**
     * @param arr   array to be sorted
     * @param start inclusive
     * @param end   exclusive
     */
    private static <E extends Comparable<E>> void insertionSort(E[] arr, int start, int end) {
        /** items below the partition are sorted */
        int partition = start + 1;
        for (; partition < end; partition++) {
            E item = arr[partition];
            int i = partition - 1;
            while (i >= start && arr[i].compareTo(item) > 0) {
                arr[i + 1] = arr[i];
                i--;
            }
            arr[i + 1] = item;
        }
    }

    /**
     * @param list  list to be sorted
     * @param start inclusive
     * @param end   exclusive
     */
    private static <E extends Comparable<E>> void insertionSort(List<E> list, int start, int end) {
        /** items below the partition are sorted */
        int partition = start + 1;
        for (; partition < end; partition++) {
            E item = list.get(partition);
            int i = partition - 1;
            while (i >= start && list.get(i).compareTo(item) > 0) {
                list.set(i + 1, list.get(i));
                i--;
            }
            list.set(i + 1, item);
        }
    }

    /**
     * Sorts the array using insertion sort.
     * 
     * @param <E> type of elements in the array
     * @param arr array to be sorted
     */
    public static <E extends Comparable<E>> void insertionSort(E[] arr) {
        insertionSort(arr, 0, arr.length);
    }

    /**
     * Sorts the list using insertion sort.
     * 
     * @param <E>  type of elements in the list
     * @param list list to be sorted
     */
    public static <E extends Comparable<E>> void insertionSort(List<E> list) {
        insertionSort(list, 0, list.size());
    }

    /**
     * takes a (sub)array which is sorted except for the last element and inserts
     * the item arr[end] into the rest of the array. Undefined behavior if the
     * array is not sorted.
     * 
     * @param start inclusive
     * @param end   the index after the sorted portion. the item to be inserted
     *              lives at this index.
     */
    static <E extends Comparable<E>> void binaryInsert(E[] arr, int start, int end) {
        if (start < 0 || end > arr.length) {
            throw new ArrayIndexOutOfBoundsException(
                    String.format(
                            "start %d end %d: out of bounds for array of length %d",
                            start, end, arr.length));
        }

        int numElements = (end - start) + 1;
        if (numElements < 1) {
            throw new IllegalArgumentException(
                    String.format(
                            "start %d end %d: end must be greater than start",
                            start, end));
        } else if (numElements == 1 || arr[end].compareTo(arr[end - 1]) > 0) {
            // already sorted
        } else if (numElements == 2) { // just swap if necessary
            if (arr[end].compareTo(arr[start]) < 0) {
                E tmp = arr[start];
                arr[start] = arr[end];
                arr[end] = tmp;
            }
        } else {
            // three or more elements
            binaryInsertMachinery(arr, start, end);
        }
    }

    /**
     * takes a list which is sorted except for the last element and inserts
     * the item list.get(list.size() - 1) into the rest of the list. Undefined
     * behavior if the
     * list is not sorted.
     * <p>
     * Unlike the array version, this method does not use a start and end index.
     * Use {@link List#subList(int, int)} to get a sublist to sort.
     */
    static <E extends Comparable<E>> void binaryInsert(List<E> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("empty list, no item to insert");
        } else if (list.size() == 1 || list.get(list.size() - 1).compareTo(list.get(list.size() - 2)) > 0) {
            // already sorted
        } else if (list.size() == 2) {
            if (list.get(1).compareTo(list.get(0)) < 0) {
                E tmp = list.get(0);
                list.set(0, list.get(1));
                list.set(1, tmp);
            }
        } else {
            binaryInsertMachinery(list);
        }

    }

    private static <E extends Comparable<E>> void binaryInsertMachinery(E[] arr, int start, int end) {
        E item = arr[end];
        int middleIndex = start + (end - start) / 2;
        E middle = arr[middleIndex];
        int cmp = item.compareTo(middle);
        if (cmp < 0) {
            for (int i = end; i > middleIndex; i--) {
                arr[i] = arr[i - 1];
            }
            arr[middleIndex] = item;
            binaryInsert(arr, start, middleIndex);
        } else if (cmp > 0) {
            binaryInsert(arr, middleIndex + 1, end);
        } else /* cmp == 0 */ {
            int idx = middleIndex;
            /*
             * seek idx to the last occurence of this element. This makes
             * this insertion stable in that it preserves the order of equal
             * elements:
             */
            while (idx < end - 1 && arr[idx].compareTo(arr[idx + 1]) == 0) {
                idx++;
            }
            for (int i = end; i > idx + 1; i--) {
                arr[i] = arr[i - 1];
            }
            arr[idx + 1] = item;
        }
    }

    private static <E extends Comparable<E>> void binaryInsertMachinery(List<E> list) {
        E item = list.get(list.size() - 1);
        int middleIndex = list.size() / 2;
        E middle = list.get(middleIndex);
        int cmp = item.compareTo(middle);
        if (cmp < 0) {
            for (int i = list.size() - 1; i > middleIndex; i--) {
                list.set(i, list.get(i - 1));
            }
            list.set(middleIndex, item);
            binaryInsert(list.subList(0, middleIndex + 1));
        } else if (cmp > 0) {
            binaryInsert(list.subList(middleIndex + 1, list.size()));
        } else /* cmp == 0 */ {
            int idx = middleIndex;
            /*
             * seek idx to the last occurence of this element. This makes
             * this insertion stable in that it preserves the order of equal
             * elements:
             */
            while (idx < list.size() - 2 && list.get(idx).compareTo(list.get(idx + 1)) == 0) {
                idx++;
            }
            for (int i = list.size() - 1; i > idx + 1; i--) {
                list.set(i, list.get(i - 1));
            }
            list.set(idx + 1, item);
        }
    }

    /**
     * @param arr   array to be sorted
     * @param start inclusive
     * @param end   exclusive
     */
    private static <E extends Comparable<E>> void binaryInsertionSort(E[] arr, int start, int end) {
        if (start == end || start == end - 1) {
            return;
        }
        int bottom = start;
        for (int top = start + 1; top < end; top++) {
            binaryInsert(arr, bottom, top);
        }
    }

    /**
     * Sorts this array using binary insertion sort. This is a stable sort.
     * 
     * @param <E> the type of elements in this array
     * @param arr array to be sorted
     */
    public static <E extends Comparable<E>> void binaryInsertionSort(E[] arr) {
        binaryInsertionSort(arr, 0, arr.length);
    }

    /**
     * Sorts this list using binary insertion sort. This is a stable sort.
     * 
     * @param <E>  the type of elements in this list
     * @param list list to be sorted
     */
    public static <E extends Comparable<E>> void binaryInsertionSort(List<E> list) {
        if (list.size() <= 1) {
            return;
        }
        for (int i = 1; i < list.size(); i++) {
            binaryInsert(list.subList(0, i + 1));
        }
    }

    /**
     * Sorts this array using merge sort. This is a stable sort.
     * 
     * @param <E> the type of elements in this array
     * @param arr array to be sorted
     */
    public static <E extends Comparable<E>> void mergeSort(E[] arr) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Merge two sorted arrays into one sorted array.
     */
    static <E extends Comparable<E>> E[] merge(E[] arr1, E[] arr2) {
        int i1 = 0;
        int i2 = 0;

        E[] merged = (E[]) Array.newInstance(arr1.getClass().getComponentType(), arr1.length + arr2.length);
        for (int i = 0; i < merged.length; i++) {
            if (i1 == arr1.length) {
                merged[i] = arr2[i2++];
            } else if (i2 == arr2.length) {
                merged[i] = arr1[i1++];
            } else if (arr1[i1].compareTo(arr2[i2]) < 0) {
                merged[i] = arr1[i1++];
            } else /* arr1[i1].compareTo(arr2[i2]) >= 0 */ {
                merged[i] = arr2[i2++];
            }
        }

        return merged;
    }

    /**
     * Merge two sorted lists into one sorted list.
     */
    static <E extends Comparable<E>> List<E> merge(List<E> list1, List<E> list2) {
        int i1 = 0;
        int i2 = 0;

        List<E> merged = new ArrayList<>(list1.size() + list2.size());
        for (int i = 0; i < list1.size() + list2.size(); i++) {
            if (i1 == list1.size()) {
                merged.add(list2.get(i2++));
            } else if (i2 == list2.size()) {
                merged.add(list1.get(i1++));
            } else if (list1.get(i1).compareTo(list2.get(i2)) < 0) {
                merged.add(list1.get(i1++));
            } else /* list1.get(i1).compareTo(list2.get(i2)) >= 0 */ {
                merged.add(list2.get(i2++));
            }
        }

        return merged;
    }
}
