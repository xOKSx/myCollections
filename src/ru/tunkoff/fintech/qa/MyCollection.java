package ru.tunkoff.fintech.qa;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyCollection<E> implements Collection<E> {

    private static final int BASE_SIZE = 10;
    private static final float SCALE_FACTOR = 1.5f;

    private int size;
    private Object[] elementData = new Object[BASE_SIZE];

    private class MyIterator<T> implements Iterator<T> {

        private int cursor = 0;
        private boolean removeIsUsed = true;

        @Override
        public boolean hasNext() {
            return this.cursor < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            if (this.cursor >= size) {
                throw new NoSuchElementException();
            }
            this.removeIsUsed = false;
            return (T) elementData[this.cursor++];
        }

        @Override
        public void remove() {
            if (this.cursor == 0 || this.cursor > size || this.removeIsUsed) {
                throw new IllegalStateException();
            }
            this.removeIsUsed = true;
            removeByIndex(--this.cursor);
        }
    }

    @Override
    public final int size() {
        return this.size;
    }

    @Override
    public final boolean isEmpty() {
        return !(this.size > 0);
    }

    @Override
    public final Iterator<E> iterator() {
        return new MyIterator<>();
    }

    @Override
    public final Object[] toArray() {
        return Arrays.copyOf(elementData, this.size);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> T[] toArray(final T[] a) {
        if (a.length < this.size) {
            return (T[]) Arrays.copyOf(elementData, this.size);
        } else {
            for (int i = 0; i < this.size; i++) {
                a[i] = (T) elementData[i];
            }
            return a;
        }

    }

    @Override
    public final boolean add(final E e) {
        if (this.size == elementData.length) {
            elementData = Arrays.copyOf(elementData, (int) (size * SCALE_FACTOR));
        }
        elementData[size++] = e;
        return true;
    }

    @Override
    public final boolean addAll(final Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        } else {
            for (E o : c) {
                add(o);
            }
            return true;
        }
    }

    @Override
    public final boolean contains(final Object o) {
        try {
            findElement(o);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public final boolean containsAll(final Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final boolean remove(final Object o) {
        try {
            int index = findElement(o);
            removeByIndex(index);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public final boolean removeAll(final Collection<?> c) {
        int oldSize = this.size;
        boolean isRemoved;
        for (Object o : c) {
            do {
                isRemoved = remove(o);
            } while (isRemoved);
        }
        return !(oldSize == this.size);
    }

    @Override
    public final boolean retainAll(final Collection<?> c) {
        int oldSize = this.size;
        boolean toRetain;

        for (int i = 0; i < this.size; i++) {
            toRetain = false;
            for (Object o : c) {
                if (o != null) {
                    if (elementData[i] != null && elementData[i].equals(o)) {
                        toRetain = true;
                        break;
                    }
                } else {
                    if (elementData[i] == null) {
                        toRetain = true;
                        break;
                    }
                }
            }
            if (!toRetain) {
                removeByIndex(i);
                --i;
            }
        }
        return !(oldSize == this.size);
    }

    @Override
    public final void clear() {
        for (int i = 0; i < this.size; i++) {
            elementData[i] = null;
        }
        this.size = 0;
    }

    @Override
    public final String toString() {
        MyIterator<E> it = new MyIterator<>();
        StringBuilder out = new StringBuilder("[");
        while (it.hasNext()) {
            out.append(it.next());
            if (it.hasNext()) {
                out.append(", ");
            }
        }
        out.append("]");
        return out.toString();
    }

    private void removeByIndex(final int index) {
        if (index < this.size - 1) {
            System.arraycopy(elementData, index + 1, elementData, index, this.size - 1 - index);
        }
        elementData[--this.size] = null;
    }

    private int findElement(final Object o) throws NoSuchElementException {
        if (o != null) {
            for (int i = 0; i < this.size; i++) {
                if (elementData[i] != null && elementData[i].equals(o)) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < this.size; i++) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        }
        throw new NoSuchElementException();
    }
}
