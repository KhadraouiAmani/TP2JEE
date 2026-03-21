package com.bibliotech.bibliotechproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@Configuration
@EnableReactiveMongoAuditing
public class MongoConfig {
    /*
     * This file tells Spring Boot to automatically handle
     * @CreatedDate fields in your models.
     * Every time a book or borrowing is saved,
     * it will automatically record the time.
     */
}