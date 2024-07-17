package org.jj;

import java.util.UUID;

public class IdProviderImplUuid implements IdProvider {

    @Override
    public UUID getUUID() {
        return UUID.randomUUID();
    }
}
