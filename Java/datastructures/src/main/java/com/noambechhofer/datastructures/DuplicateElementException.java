package com.noambechhofer.datastructures;

/**
 * Thrown to indicate that a data structure which does not allow duplicates has
 * been passed an element which is already present
 */
class DuplicateElementException extends IllegalArgumentException {
    public DuplicateElementException() {
    }

    public DuplicateElementException(String s) {
        super(s);
    }

    public DuplicateElementException(Throwable cause) {
        super(cause);
    }

    public DuplicateElementException(String message, Throwable cause) {
        super(message, cause);
    }
}