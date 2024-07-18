package org.jj;

public class IntIdProvider implements IdProvider {
    private static int lastId = 0;

    public IntIdProvider() {
    }

    @Override
    public int getNewId() {
        return lastId++;
    }
}
