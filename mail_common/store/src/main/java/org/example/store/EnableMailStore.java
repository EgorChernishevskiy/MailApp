package org.example.store;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan("org.example.store.dao")
@EnableJpaRepositories("org.example.store.repositories")
@EntityScan("org.example.store.entities")
public class EnableMailStore {


}
