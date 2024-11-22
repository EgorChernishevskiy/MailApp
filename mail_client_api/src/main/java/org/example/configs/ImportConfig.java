package org.example.configs;

import org.example.store.EnableMailStore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
        EnableMailStore.class,
})
@Configuration
public class ImportConfig {
}
