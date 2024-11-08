package org.example.Repositories;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;


public class UniqueIdCodecProvider implements CodecProvider {
    public UniqueIdCodecProvider() {}
    @Override
    public <T> Codec<T> get (Class<T> clazz, CodecRegistry registry) {
        if(clazz == UniqueIdMgd.class) {
            return (Codec<T>) new UniqueIdCodec(registry);
        }
        return null;
    }
}
