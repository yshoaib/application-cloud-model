package ca.appsimulations.models.configuration;


import ca.appsimulations.models.model.lqnmodel.LqnModelFactory;
import com.rits.cloning.Cloner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Cloner cloner() {
        return new Cloner();
    }

    @Bean
    public LqnModelFactory lqnModelFactory() {
        return new LqnModelFactory();
    }
}