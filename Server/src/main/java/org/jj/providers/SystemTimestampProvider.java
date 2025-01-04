package org.jj.providers;

public class SystemTimestampProvider implements TimestampProvider {

    @Override
    public long getTimestamp() {
        return System.currentTimeMillis();
    }
}
