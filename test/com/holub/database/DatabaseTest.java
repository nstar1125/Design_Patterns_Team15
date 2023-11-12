package com.holub.database;

import com.holub.text.ParseFailure;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    @Test
    @DisplayName("ReadOnlyCursor 테스트")
    public void testReadOnlyCursor() throws IOException, ParseFailure, IndexOutOfBoundsException {
        Database database = new Database("Dbase");
        database.begin();

        Table lastNames = database.execute("select * from name");
        ReadOnlyCursor readOnlyCursor = lastNames.readOnlyCursor();

        // rows test
        Object[][] rows = readOnlyCursor.rows();
        Object[][] expectedRows = {
                {"Fred", "Flintstone", "1"},
                {"Wilma", "Flintstone", "1"},
                {"Allen", "Holub", "0"},
        };
        assertArrayEquals(rows, expectedRows);

        // row test
        Object[] firstRow = readOnlyCursor.row(0);
        Object[] expectedFirstRow = {"Fred", "Flintstone", "1"};
        assertArrayEquals(firstRow, expectedFirstRow);

        // last column test
        Object[] lastCol = readOnlyCursor.column("last");
        Object[] expectedLastCol = {"Flintstone", "Flintstone", "Holub"};
        assertArrayEquals(lastCol, expectedLastCol);
    }

    @Test
    @DisplayName("order by parsing 테스트")
    public void testOrderByParse() throws IOException, ParseFailure {
        Database database = new Database("Dbase");
        database.begin();

        // parse fail
        database.execute("select * from name order by ASC");
        database.execute("select * from name order by addrId, first");
        database.execute("select * from name order by addrId, DESC");

        // parse success
        database.execute("select * from name order by addrId");
        database.execute("select * from name order by addrId ASC");
        database.execute("select * from name order by addrId DESC");
        database.execute("select * from name order by first ASC addrId DESC");
        database.execute("select * from name order by first addrId");
        database.execute("select * from name order by first addrId ASC");
    }
}