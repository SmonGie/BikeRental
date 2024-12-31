package org.example.Mappers;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import org.example.Dao.ClientDao;

@Mapper
public interface ClientMapper {
    @DaoFactory
    ClientDao ClientDao(@DaoKeyspace String keyspace, @DaoTable String table);

    @DaoFactory
    ClientDao ClientDao();
}
