package com.holidaysoffer.holidayofferapp.activity_search;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SearchActivityFlagConversionTest {

    @Test
    public void selectionShouldBeRetrievedFromFlag() {

        SearchActivity searchActivity = new SearchActivity();
        boolean[] selection = new boolean[] {true, true, false, false, true};

        int flag = searchActivity.boolArrayToInt(selection);
        assertEquals(1 + 2 + 16, flag);

        boolean[] result = searchActivity.getClimateSelection(flag, selection.length);
        assertArrayEquals(selection, result);
    }

    @Test
    public void flagShouldBeRetrievedFromSelection() {

        SearchActivity searchActivity = new SearchActivity();
        boolean[] selection = new boolean[5];
        int flag = 3;

        selection = searchActivity.getClimateSelection(flag, selection.length);
        assertArrayEquals(new boolean[]{true, true, false, false, false}, selection);

        int result = searchActivity.boolArrayToInt(selection);
        assertEquals(flag, result);
    }

}