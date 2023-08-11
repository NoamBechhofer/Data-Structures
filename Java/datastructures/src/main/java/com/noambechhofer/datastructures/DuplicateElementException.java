package com.noambechhofer.datastructures;

public class DuplicateElementException extends IllegalArgumentException {
    private static final long serialVersionUID = 0xDeadBeefCafeD00DL;

    public DuplicateElementException() {
    }

    public DuplicateElementException(String message) {
        super(message);
    }

    public DuplicateElementException(Throwable cause) {
        super(cause);
    }

    public DuplicateElementException(String message, Throwable cause) {
        super(message, cause);
    }
}
