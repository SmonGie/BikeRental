package Benchmark;

import com.mongodb.client.ClientSession;
import org.example.Model.RedisManager;
import org.example.Model.bikes.*;
import org.example.Repositories.BikeRedisRepository;
import org.example.Repositories.BikeRepository;
import org.example.Repositories.IBikeRepository;
import org.example.Repositories.MongoRepository;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import redis.clients.jedis.JedisPooled;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class BenchmarkTest {

    private  MongoRepository repo;
    private  IBikeRepository bikeRepository, RedisRepository;
    private  BikeRedisRepository bikeRedisRepository;
    private ClientSession session;
    private  JedisPooled pooled;
    private  BikeMgd bikeMgd1, bikeMgd2;
    String bikeMgd2Id;
    String bikeMgd1Id;


    @Setup(Level.Trial)

    public void setupBenchmark() {

        repo = new MongoRepository();
        bikeRepository = new BikeRepository(repo.getDatabase(), repo.getMongoClient());

        RedisManager redisManager = new RedisManager();
        redisManager.initConnection();
        pooled = redisManager.getPooledConnection();

        RedisRepository = new BikeRedisRepository(bikeRepository, pooled);
        bikeRedisRepository = new BikeRedisRepository(bikeRepository, pooled);
        session = repo.getMongoClient().startSession();

        pooled.flushDB();

        MountainBike mtb2 = new MountainBike(true, "EXtreme X-Cal", 1200);
        ElectricBike ebike = new ElectricBike(true, "Giant E+", 500);
        bikeMgd1 = new MountainBikeMgd(mtb2);
        bikeMgd2 = new ElectricBikeMgd(ebike);
        RedisRepository.save(bikeMgd1);
        RedisRepository.save(bikeMgd2);
         bikeMgd2Id = bikeMgd2.getBikeId();
         bikeMgd1Id = bikeMgd1.getBikeId();

    }


    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void CacheHit(Blackhole blackhole) {

        blackhole.consume( RedisRepository.findById(bikeMgd1Id));

    }



    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void CacheMiss(Blackhole blackhole) {

        bikeRedisRepository.deleteCacheOnly(bikeMgd2);

        blackhole.consume(RedisRepository.findById(bikeMgd2Id));

    }


    @TearDown
    public void cleanUp() {

        pooled.flushDB();
        pooled.close();
        session.close();

    }


}
