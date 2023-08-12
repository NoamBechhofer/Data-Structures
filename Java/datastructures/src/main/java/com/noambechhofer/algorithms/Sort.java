package com.noambechhofer.algorithms;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Sort {
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

    public static <E extends Comparable<E>> void insertionSort(E[] arr) {
        insertionSort(arr, 0, arr.length);
    }

    public static <E extends Comparable<E>> void insertionSort(List<E> list) {
        insertionSort(list, 0, list.size());
    }

    /**
     * takes an array which is sorted except for the last element and inserts
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
        if (start > end) {
            throw new IllegalArgumentException(
                    String.format(
                            "start %d end %d: end must be greater than start",
                            start, end));
        }
        if (arr.length == 1 || arr[end].compareTo(arr[end - 1]) > 0) { // already sorted
            return;
        } else if (end == start + 1) {
            if (arr[end].compareTo(arr[start]) < 0) {
                E tmp = arr[start];
                arr[start] = arr[end];
                arr[end] = tmp;
            }
        } else {
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
            } else {
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
    }

    static <E extends Comparable<E>> void binaryInsert(List<E> list) {
        if (list.size() == 0) {
            throw new IllegalArgumentException("empty list, no item to insert");
        }
        if (list.size() == 1 || list.get(list.size() - 1).compareTo(list.get(list.size() - 2)) > 0) { // already sorted
            return;
        } else if (list.size() == 2) {
            if (list.get(1).compareTo(list.get(0)) < 0) {
                E tmp = list.get(0);
                list.set(0, list.get(1));
                list.set(1, tmp);
            }
        } else {
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
            } else {
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

    public static <E extends Comparable<E>> void binaryInsertionSort(E[] arr) {
        binaryInsertionSort(arr, 0, arr.length);
    }

    public static <E extends Comparable<E>> void binaryInsertionSort(List<E> list) {
        if (list.size() <= 1) {
            return;
        }
        for (int i = 1; i < list.size(); i++) {
            binaryInsert(list.subList(0, i + 1));
        }
    }

    public static <E extends Comparable<E>> void mergeSort(E[] arr) {
        throw new UnsupportedOperationException("not implemented");
    }

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
            } else {
                merged[i] = arr2[i2++];
            }
        }

        return merged;
    }

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
            } else {
                merged.add(list2.get(i2++));
            }
        }

        return merged;
    }
}
