package org.example.worker.configs;

import org.example.store.EnableMailStore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Import({
        EnableMailStore.class,
})
@EnableScheduling
@Configuration
public class ImportConfig {
}
