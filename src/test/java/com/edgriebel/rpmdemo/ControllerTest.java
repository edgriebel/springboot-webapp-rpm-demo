package com.edgriebel.rpmdemo;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {
    protected static final String restPrefix = "/rest/";
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired 
    private Controller controller;
    
    // somehow this is magically instantiated by JacksonTester.initFields()
    private JacksonTester<Collection<DemoDO>> collDemoDOJson;
    private JacksonTester<DemoDO> DemoDOJson;

    @Test
//    @WithMockUser(username = "userid", password = "password", roles = "USER") 
    public void getStatus() throws Exception {
        ResultActions response = mvc.perform(MockMvcRequestBuilders.get(restPrefix+"status")
                .accept(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk())
                ;
        ObjectContent<DemoDO> ret = DemoDOJson.parse(response.andReturn().getResponse().getContentAsString());
        ret.assertThat().hasFieldOrProperty("enabled").as("Is enabled not null?").isNotNull();
        System.out.println("Status output: " + response.andReturn().getResponse().getContentAsString());
    }
        
    @Test
    public void getStuff_Empty() throws Exception {
        ResultActions response = mvc.perform(MockMvcRequestBuilders.get(restPrefix+"stuff")
                .accept(MediaType.APPLICATION_JSON));
        response.andExpect(status().is4xxClientError())
                ;
    }
        
    @Test
    public void getStuff() throws Exception {
        ResultActions response = mvc.perform(MockMvcRequestBuilders.get(restPrefix+"stuff/Thruston/Howell/III")
                .accept(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk());
        
        ObjectContent<Collection<DemoDO>> ret = collDemoDOJson.parse(response.andReturn().getResponse().getContentAsString());
        ret.assertThat().isNotNull()
            .asList()
            .isNotEmpty()
            .doesNotHaveDuplicates()
            .filteredOnNull("name")
            .isEmpty()
            ;
    }
    
    @Test
    public void getTest() throws Exception {
        String ret = controller.doTest("testValue");
        System.out.println(ret);
    }
    

    @Before
    public void initJson() {
        JacksonTester.initFields(this, new ObjectMapper());
    }
}
