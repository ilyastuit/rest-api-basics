package com.epam.esm.config.property;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("prod")
@PropertySource(value= {"classpath:application-prod.properties"})
public class ProdProperty {
}
