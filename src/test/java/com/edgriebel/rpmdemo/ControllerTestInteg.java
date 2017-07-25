package com.edgriebel.rpmdemo;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTestInteg {

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
    }

    @Test
    public void getHomepage() throws Exception {
        ResponseEntity<String> response = template.getForEntity(base.toString(),
                String.class);
        assertThat(response.getBody(), startsWith("<!DOCTYPE"));
    }
    
    @Test
    public void getStatus() throws Exception {
        ResponseEntity<DemoDO> response = template.getForEntity(base.toString() + "rest/status", DemoDO.class);
        assertThat(response, notNullValue());
        assertThat(response.getBody(), notNullValue());
        System.out.println("Status: " + response.getBody());
    }
    
    @Test
    public void getStuff() throws Exception {
        @SuppressWarnings("rawtypes")
        ResponseEntity<Collection> response = template.getForEntity(base.toString() + "rest/stuff/Anakin/Luke/Leia", Collection.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response, notNullValue());
        assertThat(response.getBody(), notNullValue());
        System.out.println("Stuff DemoDO collection:: " + response.getBody());
    }
    
    @Test
    public void getHello() throws Exception {
        ResponseEntity<DemoDO> response = template.getForEntity(base.toString() + "rest/hello?name=myname", DemoDO.class);
        assertThat(response, notNullValue());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getName(), is("Hello myname!"));
        System.out.println("Hello DemoDO: " + response.getBody());
    }
    
}
