package org.example.Mappers;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import org.example.Dao.BikeDao;

@Mapper
public interface BikeMapper {
    @DaoFactory
    BikeDao BikeDao(@DaoKeyspace String keyspace, @DaoTable String table);

    @DaoFactory
    BikeDao BikeDao();
}
