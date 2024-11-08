package org.example.Repositories;

import java.util.UUID;

public class UniqueIdMgd {
    private UUID uuid;

    // Constructor
    public UniqueIdMgd(UUID uuid) {
        this.uuid = uuid;
    }

    // Getter
    public UUID getUuid() {
        return uuid;
    }

    // Setter
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
