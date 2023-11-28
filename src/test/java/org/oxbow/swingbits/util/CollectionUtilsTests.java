package org.oxbow.swingbits.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectionUtilsTests {

    @Test
    void nullCollectionIsEmpty() {
        Collection<?> c = null;
        assertTrue(CollectionUtils.isEmpty(c));
    }

    @Test
    void emptyCollectionIsEmpty() {
        Collection<?> c = Collections.emptyList();
        assertTrue(CollectionUtils.isEmpty(c));
    }

    @Test
    void nonEmptyCollectionIsNotEmpty() {
        Collection<?> c = Arrays.asList("123");
        assertFalse(CollectionUtils.isEmpty(c));
    }
    
}
