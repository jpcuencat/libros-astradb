package gcloudteach.com.libreria;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    // public void setRepo(AuthorRepo authorRepo) {
    // this.authorRepo = authorRepo;
    // }

    @Autowired
    private AuthorRep authorRepo;

    @Value("${datadump.location.author}")
    private String authorDumpLocation;

    @Value("${datadump.location.works}")
    private String worksDumpLocation;

    public static void main(String[] args) {
        SpringApplication.run(LibreriaApplication.class, args);
    }

    private void initAuthors() {
        Path path = Paths.get(authorDumpLocation); 
     //   System.out.println("Hasta aqui aplicacion exitosa" + authorDumpLocation);
        try (Stream<String> lines = Files.lines(path)) { 
            lines.forEach(line -> {
                // Read and parse the line 
                String jsonString = line.substring(line.indexOf("{")); 
                try {
                JSONObject jsonObject = new JSONObject(jsonString); 
                // Construct Author object 
                
                Author author = new Author(); 
                author.setName(jsonObject.optString("name")); 
                author.setPersonalName(jsonObject.optString("personal_name")); 
                author.setId(jsonObject.optString("key").replace("/authors/",""));
                // Persist using Repository 
                System.out.println("Escribiendo author: " + author.getName() + "...");
                authorRepo.save(author);
                } catch(JSONException e){
                    e.printStackTrace();
                }
            });
         }catch (IOException e) {
                 e.printStackTrace();
         }
    }

    private void initWorks() {

    }

    @PostConstruct
    public void start() {

        // System.out.println("Hasta aqui aplicacion exitosa");
        // Author author = new Author();
        // author.setId("id");
        // author.setName("Name");
        // author.setPersonalName("personal");
        // authorRepo.save(author);
        initAuthors();
        initWorks();
     //   System.out.println(worksDumpLocation);

    }

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }

}
