package org.jj;

import java.time.LocalDateTime;

public class TimestampProvider {
    public LocalDateTime getTimestamp() {
        return LocalDateTime.now();
    }
}
