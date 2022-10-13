package gcloudteach.com.libreria;

import java.nio.file.Path;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import author.Author;
import connection.DataStaxAstraProperties;



@SpringBootApplication
@Configuration
@EnableCassandraRepositories(basePackages = "gcloudteach.com")

@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class LibreriaApplication {
    
    // private AuthorRepo authorRepo;
    // @Autowired
    //     public void setRepo(AuthorRepo authorRepo) {
    //         this.authorRepo = authorRepo;
    //     }
    

    @Autowired 
    private AuthorRep authorRepo;
    
	public static void main(String[] args) {
		SpringApplication.run(LibreriaApplication.class, args);
	}


    @PostConstruct
    public void start(){

        System.out.println("Hasta aqui aplicacion exitosa");
        Author author = new Author();
         author.setId("id");        
         author.setName("Name");
         author.setPersonalName("personal");
        authorRepo.save(author);

    }


	@Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }

}
