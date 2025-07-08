package com.project.backend.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpecificationUtilTest {

    @Test
    public void testIsValid_String() {
        assertTrue(SpecificationUtil.isValid("abc"));
        assertFalse(SpecificationUtil.isValid(""));
        assertFalse(SpecificationUtil.isValid(" "));
        assertFalse(SpecificationUtil.isValid((String) null));
        assertFalse(SpecificationUtil.isValid("null"));
    }

    @Test
    public void testIsValid_Boolean() {
        assertTrue(SpecificationUtil.isValid(Boolean.TRUE));
        assertTrue(SpecificationUtil.isValid(Boolean.FALSE));
        assertFalse(SpecificationUtil.isValid((Boolean) null));
    }

    @Test
    public void testConstructor() {
        SpecificationUtil instance = new SpecificationUtil();
        assertEquals(SpecificationUtil.class, instance.getClass());
    }
}
