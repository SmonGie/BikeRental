package org.example.Repositories;

import java.util.UUID;

public class UniqueIdMgd {
    private UUID uuid;

    public UniqueIdMgd(UUID uuid) {
        this.uuid = uuid;
    }
    
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
