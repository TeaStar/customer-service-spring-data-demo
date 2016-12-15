package uk.co.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GettingStartedDocumentation {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                                      .apply(documentationConfiguration(this.restDocumentation))
                                      .alwaysDo(document("{method-name}/{step}/"))
                                      .build();
    }

    @Test
    public void index() throws Exception {
        this.mockMvc.perform(get("/").accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("_links.customer", is(notNullValue())));
    }

    @Test
    public void creatingACustomer() throws Exception {
        String customerLocation = createCustomer();
        getCustomer(customerLocation);
    }

    String createCustomer() throws Exception {
        Map<String, String> customer = new HashMap<>();
        customer.put("title", "Mr");
        customer.put("forename", "Milky");
        customer.put("surname", "Joe");
        customer.put("nationalInsuranceNumber", "JT998877V");

        String customerLocation =
            this.mockMvc.perform(post("/customer").contentType(MediaTypes.HAL_JSON)
                                                  .content(objectMapper.writeValueAsString(customer)))
                        .andExpect(status().isCreated())
                        .andExpect(header().string("Location", notNullValue()))
                        .andReturn()
                        .getResponse()
                        .getHeader("Location");
        return customerLocation;
    }

    MvcResult getCustomer(String customerLocation) throws Exception {
        return this.mockMvc.perform(get(customerLocation))
                           .andExpect(status().isOk())
                           .andExpect(jsonPath("title", is(notNullValue())))
                           .andExpect(jsonPath("forename", is(notNullValue())))
                           .andExpect(jsonPath("surname", is(notNullValue())))
                           .andExpect(jsonPath("nationalInsuranceNumber", is(notNullValue())))
                           .andExpect(jsonPath("_links.self", is(notNullValue())))
                           .andExpect(jsonPath("_links.customer", is(notNullValue())))
                           .andReturn();
    }

}
