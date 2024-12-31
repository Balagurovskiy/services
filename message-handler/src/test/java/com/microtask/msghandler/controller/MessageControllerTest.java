package com.microtask.msghandler.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.microtask.msghandler.MessageHandlerTest;
import com.microtask.msghandler.dto.MessageRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MessageHandlerTest.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MessageControllerTest {
    @Autowired
    private MessagesController controller;

    private MockMvc mockMvc;

    @Value("${test.expected.header.name}")
    private String expectedHeader;
    @Value("${test.expected.header.value}")
    private String expectedHeaderVal;
    @Value("${test.expected.body}")
    private String expectedBody;
    @Value("${test.message}")
    private String testMessage;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testHandler_ExpectedValidInsert_Select() throws Exception{
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/message/")
                        .header(expectedHeader, expectedHeaderVal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                mapper.writeValueAsBytes(
                                        new MessageRequest(testMessage, ""))
                        ))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=ISO-8859-1"))
                .andReturn();

        String testBodyId = result.getResponse().getContentAsString();

        Assertions.assertEquals(expectedBody, testBodyId);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/message/id/" + testBodyId)
                        .header(expectedHeader, expectedHeaderVal))
//                        .param("id", content))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(testMessage));
    }
}

