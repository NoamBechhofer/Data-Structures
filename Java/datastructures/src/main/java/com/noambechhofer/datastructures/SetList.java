package com.noambechhofer.datastructures;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;

import com.noambechhofer.datastructures.utils.DuplicateElementException;

/**
 * A union of the {@link Set} and {@link List} interfaces. Backed by a
 * {@link HashMap<Integer, E>}.
 * The aim is to compromise by having all significant methods run in O(n)
 */
@SuppressWarnings("java:S2160")
/*
 * SuppressWarnings justification:
 * Note that this class extends AbstractList.
 * Using AbstractList provides a sublist and spliterator implementation, which
 * saves me a lot of work.
 * SonarLint complains that I have not implemented equals() and hashCode(), but
 * these are already implemented by AbstractList.
 */
public class SetList<E> extends AbstractList<E> implements Set<E> {
    private class SetListIterator implements ListIterator<E> {
        /**
         * On a theoretical level, this points between the elements which would be
         * returned by {@link #previous()} and {@link #next()} (assuming those would not
         * throw exceptions). On a practical level, it points to the element that would
         * be returned by {@link #next()}. So it is initialized to 0, and may hold a
         * value anywhere between 0 (inclusive) and size() (inclusive, but in this
         * position {@link #next()} would throw an exception). In other words, there are
         * size() + 1 valid positions.
         */
        private int cursor;
        /** The element last returned by {@link #next()} or {@link #previous()} */
        private E curr;
        /**
         * Determines whether {@link #remove()} or {@link #set(Object)} can be called.
         * Only set to {@code true} after a call to {@link #next()} or
         * {@link #previous()}. Falsified upon construction, by {@link #remove()}, and
         * by {@link #add(Object).}
         */
        private boolean canMutate;
        /**
         * If true, last call was {@link #next()}. If false, last call was
         * {@link #previous()}. Used to determine {@link #set(Object)}'s behavior.
         */
        private boolean lastCallWasNext;

        public SetListIterator() {
            cursor = 0;
            canMutate = false;
        }

        public SetListIterator(final int index) {
            validateIndex(index);

            cursor = index;
            canMutate = false;
        }

        @Override
        public boolean hasNext() {
            return cursor < size();
        }

        @Override
        public E next() {
            try {
                curr = get(cursor++);
            } catch (final IndexOutOfBoundsException e) {
                throw new NoSuchElementException(e);
            }

            canMutate = true;
            lastCallWasNext = true;

            return curr;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            try {
                curr = get(--cursor);
            } catch (final IndexOutOfBoundsException e) {
                throw new NoSuchElementException(e);
            }

            canMutate = true;
            lastCallWasNext = false;

            return curr;
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (!canMutate) {
                throw new IllegalStateException();
            }

            SetList.this.remove(curr);
            canMutate = false;
            modCount++;
        }

        /**
         * ! USE CAUTION ! This method breaks the contract of this class by allowing
         * duplicates. Use {@link SetList#contains(Object)} to ensure you're not
         * inserting a duplicate.
         */
        @Override
        public void set(final E e) {
            if (!canMutate) {
                throw new IllegalStateException();
            }

            if (lastCallWasNext) {
                set(cursor - 1, e, true);
            } else {
                set(cursor, e, true);
            }

            modCount++;
        }

        @Override
        public void add(final E e) {
            SetList.this.add(cursor++, e);
            canMutate = false;

            modCount++;
        }

        /**
         * ! This is an internal extension of the {@link #set(int, Object)} function.
         * ! It's dangerous because it breaks the contract of this class - it will allow
         * ! duplicates to be inserted. The utility is to allow
         * ! {@link Collections#sort(List)} to work on this class. Use with extreme
         * ! caution.
         * 
         * @param index   index at which the specified element is to be inserted
         * @param element element to be inserted
         * @param force   if true, will insert the element even if it's a duplicate
         * @return the element previously at the specified position
         * @throws IndexOutOfBoundsException if the index is out of range
         * @throws DuplicateElementException if the element is already in the list and
         *                                   force is false
         */
        private E set(final int index, final E element, final boolean force) {
            if (!force) {
                SetList.this.set(index, element);
            }

            validateIndex(index);

            modCount++;

            elementMap.put(element, index);
            return indexMap.put(index, element);
        }
    }

    /** Maps indices to their corresponding elements */
    private final HashMap<Integer, E> indexMap;
    /** 
     * Maps elements to their corresponding indices.
     * <p>
     * Using a reverse HashMap allows us to accomplish indexOf() in O(1) time.
     * The tradeoff is space, however since Java uses Object references, the
     * space cost is not too bad.
     * Also we have to maintain two HashMaps, which is a bit
     * more work.
     */
    private final HashMap<E, Integer> elementMap;

    /**
     * Standard constructor.
     */
    public SetList() {
        this.indexMap = new HashMap<>();
        this.elementMap = new HashMap<>();
    }

    /**
     * Returns the number of elements
     * 
     * @return number of elements
     */
    @Override
    public int size() {
        return indexMap.size();
    }

    /**
     * Returns {@code true} if there are no elements
     * 
     * @return {@code true} if there are no elements
     */
    @Override
    public boolean isEmpty() {
        return this.indexMap.isEmpty();
    }

    /**
     * Returns {@code true} if this SetList contains the specified element.
     * 
     * @param o element whose presence in this SetList is to be tested
     * @return {@code true} if this SetList contains the specified element.
     */
    @Override
    public boolean contains(final Object o) {
        return this.elementMap.containsKey(o);
    }

    /**
     * Returns an iterator over this SetList in proper sequence.
     * 
     * @return an iterator over this SetList in proper sequence.
     */
    @Override
    public Iterator<E> iterator() {
        return new SetListIterator();
    }

    /**
     * Returns a list iterator over the elements in this SetList (in proper
     * sequence).
     * <p>
     * ! N.B. the iterator's {@link SetListIterator#set(Object)} method will not
     * enforce duplicates.
     * 
     * @return a list iterator over the elements in this SetList (in proper
     *         sequence).
     */
    @Override
    public ListIterator<E> listIterator() {
        return new SetListIterator();
    }

    /**
     * Returns a list iterator over the elements in this SetList (in proper
     * sequence), starting at the specified position in the SetList. The specified
     * index indicates the first element that would be returned by an initial call
     * to next. An initial call to previous would return the element with the
     * specified index minus one.
     * <p>
     * ! N.B. the iterator's {@link SetListIterator#set(Object)} method will not
     * enforce duplicates.
     * 
     * @param index index of the first element to be returned from the list iterator
     *              (by a call to next)
     * 
     * @return a list iterator over the elements in this list (in proper sequence),
     *         starting at the specified position in the list
     * 
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0
     *                                   || index > size())
     */
    @Override
    public ListIterator<E> listIterator(final int index) {
        return new SetListIterator(index);
    }

    /**
     * Returns an array containing all of the elements in this SetList in proper
     * sequence (from first to last element).
     * <p>
     * The returned array will be "safe" in that no references to it are maintained
     * by this SetList. The caller is thus free to modify the returned array.
     * 
     * @return an array containing all of the elements in this list in proper
     *         sequence (from first to last element).
     */
    @Override
    public Object[] toArray() {
        return toArray(new Object[0]);
    }

    /**
     * Returns an array containing all of the elements in this SetList in proper
     * sequence (from first to last element); the runtime type of the returned array
     * is that of the specified array. If the SetList fits in the specified array,
     * it is returned therein. Otherwise, a new array is allocated with the runtime
     * type of the specified array and the size of this SetList.
     * <p>
     * If the SetList fits in the specified array with room to spare (i.e., the
     * array has more elements than the SetList), the element in the array
     * immediately following the end of the SetList is set to {@code null}. (This is
     * useful in determining the length of the SetList <i>only</i> if the caller
     * knows that the list does not contain any null elements.)
     * <p>
     * The returned array will be "safe" in that no references to it are maintained
     * by this SetList. The caller is thus free to modify the returned array.
     * <p>
     * Like the {@link #toArray()} method, this method acts as bridge between
     * array-based and collection-based APIs. Further, this method allows precise
     * control over the runtime type of the output array, and may, under certain
     * circumstances, be used to save allocation costs.
     * <p>
     * Suppose {@code x} is a list known to contain only strings. The following code
     * can be used to dump the list into a newly allocated array of {@code String}:
     * <p>
     * {@code String[] y = x.toArray(new String[0]);}
     * <p>
     * Note that {@code toArray(new Object[0])} is identical in function to
     * {@code toArray()}.
     * 
     * @param <T> the runtime type of the array to contain the collection
     * @param a   the array into which the elements of this SetList are to be
     *            stored,
     *            if it is big enough; otherwise, a new array of the same runtime
     *            type
     *            is allocated for this purpose.
     * 
     * @return an array containing the elements of this SetList.
     * 
     * @throws ArrayStoreException  if the runtime type of the specified array is
     *                              not a supertype of the runtime type of every
     *                              element in this SetList
     * @throws NullPointerException if the specified array is null
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(final T[] a) {
        T[] arr = a;

        if (a.length < size()) {
            arr = (T[]) Array.newInstance(a.getClass().getComponentType(), size());
        }

        for (int i = 0; i < size(); i++) {
            final E curr = get(i);

            if (!a.getClass().getComponentType().isAssignableFrom(curr.getClass())) {
                throw new ArrayStoreException();
            }

            arr[i] = (T) curr;
        }

        for (int i = size(); i < a.length; i++) {
            arr[i] = null;
        }

        return arr;
    }

    /**
     * Appends the specified element to the end of this SetList if it is not already
     * present. The presence of the item is based on {@code ==} equality and
     * {@code .equals()} equality. More formally, adds the specified element
     * {@code e} to this SetList if the SetList contains no element {@code e2} such
     * that {@code e2 == e || (e != null && e.equals(e2))}. If this set already
     * contains the element, the call leaves the set unchanged and returns
     * {@code false}. This ensures that SetLists never contain duplicate elements.
     * <p>
     * Unlike {@link #add(int, Object)}, insertion of a duplicate does not throw a
     * {@link DuplicateElementException}, and instead returns false.
     * 
     * @param e element to be appended to this list
     * 
     * @return true if the element is not already present in the list.
     */
    @Override
    public boolean add(final E e) {
        if (this.contains(e)) {
            return false;
        }

        addInternal(size(), e);
        return true;
    }

    /**
     * Inserts the specified element at the specified position in this SetList.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right (adds one to their indices).
     * <p>
     * Unlike {@link #add(Object)}, insertion of a duplicate throws a
     * {@link DuplicateElementException}.
     * 
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     * 
     * @throws DuplicateElementException if the element is already present
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0
     *                                   ||
     *                                   index > size())
     */
    @Override
    public void add(final int index, final E element) {
        if (elementMap.containsKey(element)) {
            throw new DuplicateElementException();
        }

        addInternal(index, element);
    }

    /**
     * Unsupported. Would be quite inefficient. If you really need to do this, use
     * multiple calls to {@link #add(Object)}
     */
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported. Would be quite inefficient. If you really need to do this, use
     * multiple calls to {@link #add(int, Object)}
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the first occurrence of the specified element from this SetList, if
     * it is present. If this SetList does not contain the element, it is unchanged.
     * More formally, removes an element {@code e} such that
     * {@code (o==null ? e==null : o.equals(e))}, if this SetList contains such an
     * element. Returns {@code true} if this SetList contained the element (or
     * equivalently, if this SetList changed as a result of the call). (This SetList
     * will not contain the element once the call returns.)
     * 
     * @param o element to be removed from this SetList, if present
     * 
     * @return {@code true} if this list contained the specified element
     */
    @Override
    public boolean remove(final Object o) {
        for (int i = 0; i < size(); i++) {
            if (Objects.equals(o, get(i))) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the element at the specified position in this SetList. Shifts any
     * subsequent elements to the left (subtracts one from their indices). Returns
     * the element that was removed from the SetList.
     * 
     * @param index the index of the element to be removed
     * 
     * @return the element previously at the specified position
     * 
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   {@code (index &lt; 0 || index >= size())}
     */
    @Override
    public E remove(final int index) {
        validateIndex(index);

        final E ret = indexMap.remove(index);
        elementMap.remove(ret);

        int i = index;
        for (; i < size(); i++) {
            final E val = get(i + 1);
            elementMap.put(val, i);
            indexMap.put(i, val);
        }

        indexMap.remove(i);
        modCount++;
        return ret;
    }

    /**
     * Removes all of the elements from this SetList. The SetList will be empty
     * after this call returns.
     */
    @Override
    public void clear() {
        elementMap.clear();
        indexMap.clear();
        modCount++;
    }

    /**
     * Returns the element at the specified position in this SetList.
     * 
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   {@code (index < 0 || index >= size())}
     */
    @Override
    public E get(final int index) {
        validateIndex(index);

        return indexMap.get(index);
    }

    /**
     * Replaces the element at the specified position in this SetList with the
     * specified element
     * 
     * @param index   index of the element to replace
     * @param element element to be stored at the specified position
     * 
     * @return the element previously at the specified position
     * 
     * @throws DuplicateElementException if the element is already present
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   {@code (index < 0 || index >= size())}
     */
    @Override
    public E set(final int index, final E element) {
        validateIndex(index);
        if (elementMap.containsKey(element)) {
            throw new DuplicateElementException();
        }

        modCount++;
        elementMap.put(element, index);
        return indexMap.put(index, element);
    }

    /**
     * Returns the index of the specified element in this SetList, or -1 if this
     * SetList does not contain the element. More formally, returns the index i such
     * that (o==null ? get(i)==null : o.equals(get(i))), or -1 if there is no such
     * index.
     * 
     * @param o element to search for
     * 
     * @return the index of the specified element in this SetList, or -1 if this
     *         SetList does not contain the element
     */
    @Override
    /*
     * Suppressing warnings justification:
     * sonarlint is complaining about the use of ==, saying that this operation
     * places dependence on operator precedence, which is a bad practice.
     * Sonarlint is wrong. The code is not ambiguous. I guess they just didn't
     * program the rule to work correctly with ternary operators. I should
     * probably open an issue on their github. Oh well.
     */
    @SuppressWarnings("java:S864")
    public int indexOf(final Object o) {
        for (int i = 0; i < size(); i++) {
            final E curr = get(i);
            if (o == null ? curr == null : o.equals(curr)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns the index of the specified element in this SetList, or -1 if this
     * SetList does not contain the element. More formally, returns the index i such
     * that (o==null ? get(i)==null : o.equals(get(i))), or -1 if there is no such
     * index.
     * <p>
     * Because a {@code SetList} does not allow duplicates, calling this method is
     * exactly equivalent to calling {@code indexIf(o)}.
     * 
     * @param o element to search for
     * 
     * @return the index of the specified element in this SetList, or -1 if this
     *         SetList does not contain the element
     *
     */
    @Override
    public int lastIndexOf(final Object o) {
        return indexOf(o);
    }

    /**
     * Creates a Spliterator over the elements in this SetList.
     * <p>
     * The Spliterator reports Spliterator.SIZED and Spliterator.ORDERED.
     * 
     * @return a Spliterator over the elements in this SetList
     */
    @Override
    /*
     * Suppressing warnings justification:
     * SonarLint thinks if i remove this method I can simply inherit it. That
     * should work, but java is being weird about implementing spliterator from
     * both List and Set.
     */
    @SuppressWarnings("java:S125")
    public Spliterator<E> spliterator() {
        return super.spliterator();
    }

    /**
     * Unsupported. Would be quite inefficient. If you really need to do this, use
     * multiple calls to {@link #remove(Object)}
     */
    @Override
    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException("Unimplemented method 'removeAll'");
    }

    /**
     * Unsupported. Would be quite inefficient. If you really need to do this, use
     * multiple calls to {@link #contains(Object)}
     */
    @Override
    public boolean containsAll(final Collection<?> c) {
        throw new UnsupportedOperationException("Unimplemented method 'containsAll'");
    }

    /**
     * Unsupported. Would be quite inefficient. If you really need to do this, use
     * multiple calls to {@link #remove(Object)}
     */
    @Override
    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException("Unimplemented method 'retainAll'");
    }

    /**
     * Internal method. CALLLER MUST ENSURE THAT THE ELEMENT IS NOT ALREADY PRESENT.
     * 
     * Runs in O(n) because elements may need to be shunted over to make room
     * depending on the index.
     */
    private void addInternal(final int index, final E ele) {
        // cannot use validateIndex() because we consider size() valid here
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size()));
        }

        if (index < size()) {
            for (int i = size(); i > index; i--) {
                final E val = get(i - 1);
                elementMap.put(val, i);
                indexMap.put(i, val);
            }
        }

        elementMap.put(ele, index);
        indexMap.put(index, ele);
        modCount++;
    }

    /**
     * @param index index to validate
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 ||
     *                                   index >= size())
     */
    private void validateIndex(final int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size()));
        }
    }

    // void dataStructureInvariants() {
    // assert elementMap.size() == indexMap.size();
    // assert elementMap.size() == size();
    // assert indexMap.size() == size();
    // assert indexMap.keySet().stream().allMatch((Integer idx) -> {
    // E ele = indexMap.get(idx);
    // return elementMap.containsKey(indexMap.get(idx)) &&
    // elementMap.get(ele).equals(idx);
    // });
    // assert elementMap.keySet().stream().allMatch((E ele) -> {
    // int idx = elementMap.get(ele);
    // return indexMap.containsKey(idx) && elementMap.get(ele).equals(idx) && idx ==
    // indexOf(ele);
    // });
    // }

}
