package gcloudteach.com.libreria;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import author.Author;

@Repository
public interface AuthorRep extends CassandraRepository <Author,String>{
    
}
