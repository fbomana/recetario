package es.ait.recetario.config;

import es.ait.recetario.config.bbdd.BBDDManager;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

/**
 * This class configures the JPA values necesary for spring-jpa and spring-data-jpa.
 * 
 * @author aitkiar
 */
@Configuration
@EnableJpaRepositories(basePackages = {"es.ait.recetario"})
public class PersistenceContext
{
    @Bean
    DataSource datasource( Environment env ) throws NamingException
    {
        return BBDDManager.getInstance().getDatasource();
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,  Environment env) 
    {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("es.ait.recetario");
        entityManagerFactoryBean.setPersistenceUnitName("recetarioPU");
        Properties jpaProperties = new Properties(); 
        entityManagerFactoryBean.setJpaProperties(jpaProperties);
        return entityManagerFactoryBean;
    }

    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) 
    {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}

