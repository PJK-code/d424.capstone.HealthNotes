package com.example.d424capstone;

import com.example.d424capstone.utils.SecurityUtils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SecurityUtilsTest {

    @Test
    public void testNoteContentLengthLimiter_5000Characters() {
        // test content with exactly 5000 characters (should be valid)
        String validContent = new String(new char[5000]).replace('\0', 'a');
        assertTrue("Note content of 5000 characters should be valid", SecurityUtils.isValidText(validContent, 5000));

        // test content with 5001 characters (should exceed limiter and return false)
        String invalidContent = validContent + "a";
        assertFalse("Note content exceeding 5000 characters (5001) should be invalid", SecurityUtils.isValidText(invalidContent, 5000));
        // assert to test an expect true for 5001 characters
        //assertTrue("Note content exceeding 5000 characters (5001) should be invalid", SecurityUtils.isValidText(invalidContent, 5000));
    }
}
