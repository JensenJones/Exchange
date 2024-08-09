package org.jj.Providers;

public class IntIdProvider implements IdProvider {
    private int lastId = 0;

    public IntIdProvider() {
    }

    @Override
    public int generateId() {
        lastId ++;
        return lastId;
    }
}
