// TODO: Remove all inheritDoc tags and write out full documentation
// TODO: testing

package com.noambechhofer.datastructures;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;

/**
 * The aim is to compromise by having all significant methods run in O(n)
 */
public class SetList<E> implements List<E>, Set<E> {
    /** Maps indices to their corresponding elements */
    private HashMap<Integer, E> map;

    public SetList() {
        this.map = new HashMap<>();
    }

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

        public SetListIterator(int index) {
            if (index < 0 || index > size())
                throw new IndexOutOfBoundsException();

            cursor = index;
            canMutate = false;
        }

        @Override
        public boolean hasNext() {
            return cursor < size();
        }

        @Override
        public E next() {
            curr = get(cursor++);

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
            curr = get(--cursor);

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
            if (!canMutate)
                throw new IllegalStateException();

            SetList.this.remove(curr);
            canMutate = false;
        }

        @Override
        public void set(E e) {
            if (!canMutate)
                throw new IllegalStateException();

            if (lastCallWasNext)
                SetList.this.set(cursor - 1, e, true);
            else
                SetList.this.set(cursor, e, true);
        }

        @Override
        public void add(E e) {
            SetList.this.add(cursor++, e);
            canMutate = false;
        }
    }

    /**
     * Returns the number of elements
     * 
     * @return number of elements
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * Returns {@code true} if there are no elements
     * 
     * @return {@code true} if there are no elements
     */
    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    /**
     * Returns {@code true} if this SetList contains the specified element.
     * 
     * @param o element whose presence in this SetList is to be tested
     * @return {@code true} if this SetList contains the specified element.
     */
    @Override
    public boolean contains(Object o) {
        return this.map.containsValue(o);
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
     * ! DO NOT INVOKE THIS METHOD !
     * This method is supported to allow {@link Collections#sort(List)} to function
     * on this class (and as specified by {@link List}). The {@link ListIterator} it
     * returns has a set() method which is allowed to break the contract of this
     * class by inserting duplicate elements.
     * <p>
     * Use {@link #iterator()} instead.
     */
    @Deprecated
    @Override
    public ListIterator<E> listIterator() {
        return new SetListIterator();
    }

    /**
     * ! DO NOT INVOKE THIS METHOD !
     * This method is specified by {@link List} but should not be necessary to use.
     * Its use is dangerous since it is allowed to break the contract of this class
     * by inserting duplicates. See {@link #listIterator()}.
     * 
     * @see SetList#listIterator()
     */
    @Deprecated
    @Override
    public ListIterator<E> listIterator(int index) {
        return new SetListIterator(index);
    }

    /**
     * TODO: javadoc
     */
    @Override
    public Object[] toArray() {
        Object[] ret = new Object[size()];

        for (int i = 0; i < size(); i++) {
            ret[i] = get(i);
        }

        return ret;
    }

    /**
     * TODO: javadoc
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        Object[] ret = a;

        if (a.length < size()) {
            ret = new Object[size()];
        }

        for (int i = 0; i < size(); i++) {
            ret[i] = get(i);
        }
        for (int i = size(); i < a.length; i++)
            ret[i] = null;

        return (T[]) ret;
    }

    /**
     * TODO: javadoc
     * <p>
     * Runs in {@code O(n)} time.
     * 
     * @throws DuplicateElementException if the elemetn is already present
     */
    @Override
    public boolean add(E e) {
        if (this.contains(e))
            return false;

        addInternal(size(), e);

        return true;
    }

    /**
     * TODO: javadoc
     * 
     * @throws DuplicateElementException if the element is already present
     */
    @Override
    public void add(int index, E element) {
        if (map.containsValue(element))
            throw new DuplicateElementException();

        addInternal(index, element);
    }

    /**
     * Internal method. CALLLER MUST ENSURE THAT THE ELEMENT IS NOT ALREADY PRESENT.
     * 
     * Runs in O(n) because elements may need to be shunted over to make room
     * depending on the index.
     */
    private void addInternal(int index, E ele) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size()));

        if (index < size())
            for (int i = size(); i > index; i--)
                map.put(i, get(i - 1));

        map.put(index, ele);
    }

    /**
     * Unsupported. Would be quite inefficient. If you really need to do this, use
     * multiple calls to {@link #add(Object)}
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported. Would be quite inefficient. If you really need to do this, use
     * multiple calls to {@link #add(int, Object)}
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * TODO: javadoc
     * <p>
     * Runs in {@code O(n)} time.
     */
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size(); i++)
            if (Objects.equals(o, get(i))) {
                remove(i);
                return true;
            }
        return false;
    }

    /**
     * TODO: javadoc
     */
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size()));

        E ret = map.remove(index);

        for (int i = index; i < size(); i++)
            map.put(i, get(i + 1));

        return ret;
    }

    /**
     * TODO: javadoc
     */
    @Override
    public void clear() {
        map.clear();
    }

    /**
     * TODO: javadoc
     */
    @Override
    public E get(int index) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size()));

        return map.get(index);
    }

    // TODO: make sure all throws are in the javadoc
    /**
     * TODO: javadoc
     * 
     * @throws DuplicateElementException if the element is already present
     */
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size()));
        if (map.containsValue(element))
            throw new DuplicateElementException();

        return map.put(index, element);
    }

    /**
     * ! This is an internal extension of the {@link #set(int, Object)} function.
     * ! It's dangerous because it breaks the contract of this class - it will allow
     * ! duplicates to be inserted. The utility is to allow
     * ! {@link Collections#sort(List)} to work on this class. Use with extreme
     * ! caution.
     */
    private E set(int index, E element, boolean force) {
        if (!force)
            set(index, element);

        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size()));

        return map.put(index, element);
    }

    /**
     * TODO: javadoc
     */
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size(); i++) {
            E curr = get(i);
            if (o == null ? curr == null : o.equals(curr))
                return i;
        }

        return -1;
    }

    /**
     * TODO: javadoc
     * <p>
     * Because a {@code SetList} does not allow duplicates, calling this method is
     * exactly equivalent to calling {@code indexIf(o)}.
     */
    @Override
    public int lastIndexOf(Object o) {
        return indexOf(o);
    }

    public Spliterator<E> spliterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'subList'");
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'subList'");
    }

    /**
     * Unsupported. Would be quite inefficient. If you really need to do this, use
     * multiple calls to {@link #remove(Object)}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Unimplemented method 'removeAll'");
    }

    /**
     * Unsupported. Would be quite inefficient. If you really need to do this, use
     * multiple calls to {@link #contains(Object)}
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Unimplemented method 'containsAll'");
    }

    /**
     * Unsupported. Would be quite inefficient. If you really need to do this, use
     * multiple calls to {@link #remove(Object)}
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Unimplemented method 'retainAll'");
    }
}

class DuplicateElementException extends IllegalArgumentException {
    public DuplicateElementException() {
        super("this class does not allow the insertion of duplicate elements");
    }
}
