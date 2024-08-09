package org.jj.Providers;

public class SystemTimestampProvider implements TimestampProvider {

    @Override
    public long getTimestamp() {
        return System.currentTimeMillis();
    }
}
