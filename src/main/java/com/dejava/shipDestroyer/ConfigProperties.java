package com.dejava.shipDestroyer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:kek.properties")
public class ConfigProperties {

    @Value(value = "${rest.username}")
    private String authUser;

    @Value(value = "${rest.password}")
    private String authPass;
}
