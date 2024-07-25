package org.jj;

public class IntIdProvider implements IdProvider {
    private int lastId = 0;

    public IntIdProvider() {
    }

    @Override
    public int getNewId() {
        lastId ++;
        return lastId;
    }
}
