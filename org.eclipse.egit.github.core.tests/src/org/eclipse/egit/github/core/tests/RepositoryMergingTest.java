package org.eclipse.egit.github.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.eclipse.egit.github.core.RepositoryMerging;
import org.junit.Test;

/**
 * Unit test of {@link RepositoryMerging}
 */
public class RepositoryMergingTest {

    /**
     * Test default state of merging
     */
    @Test
    public void defaultState() {
        RepositoryMerging merging = new RepositoryMerging();
        assertNull(merging.getBase());
        assertNull(merging.getCommitMessage());
        assertNull(merging.getHead());
    }

    /**
     * Test updating merging fields
     */
    @Test
    public void updateFields() {
        RepositoryMerging merging = new RepositoryMerging();
        assertEquals("baseMerging", merging.setBase("baseMerging").getBase());
        assertEquals("headMerging", merging.setHead("headMerging").getHead());
        assertEquals("messageMerging", merging.setCommitMessage("messageMerging").getCommitMessage());
    }
}
