package repositorios;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import author.Author;
@Repository
public interface AuthorRepo extends CassandraRepository <Author,String>{
    
}

