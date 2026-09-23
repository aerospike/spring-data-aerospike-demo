package com.demo.springbootautoconfigure;

import com.aerospike.client.IAerospikeClient;
import com.demo.springbootautoconfigure.repository.AutoconfiguredMovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.aerospike.config.AerospikeSettings;
import org.springframework.data.aerospike.core.AerospikeTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpringBootAutoconfigureAerospikeDemoApplicationTest {

    @Autowired
    IAerospikeClient aerospikeClient;

    @Autowired
    AerospikeTemplate aerospikeTemplate;

    @Autowired
    AutoconfiguredMovieRepository repository;

    @Autowired
    AerospikeSettings aerospikeSettings;

    @Test
    void contextLoadsWithSpringBootAutoConfiguration() {
        assertThat(aerospikeClient).isNotNull();
        assertThat(aerospikeTemplate).isNotNull();
        assertThat(repository).isNotNull();
        assertThat(aerospikeSettings.getConnectionSettings().getHosts()).isNotBlank();
        assertThat(aerospikeSettings.getDataSettings().getNamespace()).isNotBlank();
    }
}
