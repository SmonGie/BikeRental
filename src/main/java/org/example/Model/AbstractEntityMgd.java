package org.example.Model;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.Misc.UniqueIdMgd;

import java.io.Serializable;

public class AbstractEntityMgd implements Serializable {
    @BsonProperty("_id")
    private final UniqueIdMgd entityId;

    public UniqueIdMgd getEntityId() {
        return entityId;
    }

    public AbstractEntityMgd(UniqueIdMgd entityId) {
        this.entityId = entityId;
    }

    public AbstractEntityMgd() {
        this.entityId = null;
    }
}
