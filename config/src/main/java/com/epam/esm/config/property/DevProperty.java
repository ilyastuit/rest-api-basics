package com.epam.esm.config.property;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Profile("dev")
@Configuration
@PropertySource(value= {"classpath:application-dev.properties"})
public class DevProperty {
}
