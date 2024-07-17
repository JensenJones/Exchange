package org.jj;

public class SystemTimestampProvider implements TimestampProvider {

    @Override
    public long getTimestamp() {
        return System.currentTimeMillis();
    }
}
