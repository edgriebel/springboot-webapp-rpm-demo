package com.edgriebel.rpmdemo;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {
        "hello.values=SingleName"
        })

public class StatusCheckerTest {
    Logger logger = Logger.getLogger(ControllerTest.class.getName());
    
    @Value("${hello.values}")
    private String [] helloValues;

    @Test
    public void testGetStatus() {
        DemoDO out = statusChecker.getStatus();
        assertThat(out, notNullValue());
        assertThat(out.enabled, notNullValue());
        System.out.println("DemoDO: " + out);
    }
    
    @Test
    public void testDoStuff() throws Exception {
        Collection<DemoDO> out = statusChecker.doStuff("first", "second", "third");
        assertThat(out, allOf(notNullValue(), not(empty())));
        System.out.println(out);
    }

    @Test
    public void testHello() throws Exception {
        DemoDO out = statusChecker.getHello("people");
        assertThat(out, notNullValue());
        assertThat(out.name, notNullValue());
        System.out.println(out);
        
        out = statusChecker.getHello(null);
        assertThat(out, notNullValue());
        assertThat("test.properties file contains a single entry, it must be pulling in the wrong one",
                helloValues.length, is(1));
        assertThat(out.name, containsString(helloValues[0]));
    }

    @Autowired
    private StatusChecker statusChecker;
}
