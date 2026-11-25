
package com.aydsii.demo;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.aydsii.demo.config.ConfiguracionTransferencia;

@SpringBootTest
class ConfiguracionTransferenciaTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void debeMantenerUnaUnicaInstancia() {

        ConfiguracionTransferencia primera =
            context.getBean(ConfiguracionTransferencia.class);

        ConfiguracionTransferencia segunda =
            context.getBean(ConfiguracionTransferencia.class);

        assertSame(primera, segunda);
    }
}
