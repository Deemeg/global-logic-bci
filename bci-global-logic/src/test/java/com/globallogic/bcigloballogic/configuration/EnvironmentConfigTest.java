package com.globallogic.bcigloballogic.configuration;

import com.globallogic.bcigloballogic.util.JwtAuthEntryPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnvironmentConfigTest {

    private  EnvironmentConfig environmentConfig ;
    @BeforeEach
    public void setUp() {
        environmentConfig = new EnvironmentConfig();
    }
    @Test
    public void testGetSecret() {
        ReflectionTestUtils.setField(environmentConfig, "secret", "testSecret");
        String result = environmentConfig.getSecret();

        assertEquals("testSecret", result);
    }

    @Test
    public void testGetExpiration() {
        ReflectionTestUtils.setField(environmentConfig, "expiration", 3600000L); // 1 hour in milliseconds
        Long result = environmentConfig.getExpiration();

        assertEquals(3600000L, result);
    }
}
