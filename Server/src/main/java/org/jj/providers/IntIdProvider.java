package org.jj.providers;

public class IntIdProvider implements IdProvider {
    private int lastId = 0;

    public IntIdProvider() {
    }

    @Override
    public int generateId() {
        return ++lastId;
    }
}
