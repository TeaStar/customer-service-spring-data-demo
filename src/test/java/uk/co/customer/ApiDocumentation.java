package uk.co.customer;

import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.co.customer.domain.Customer;
import uk.co.customer.repository.CustomerRepository;

import javax.servlet.RequestDispatcher;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiDocumentation {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private CustomerRepository customerRepository;

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
    public void errorExample() throws Exception {
        this.mockMvc
            .perform(get("/error")
                         .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
                         .requestAttr(RequestDispatcher.ERROR_REQUEST_URI,
                                      "/customer")
                         .requestAttr(RequestDispatcher.ERROR_MESSAGE,
                                      "The customer 'http://localhost:8080/customer/123' does not exist"))
            .andDo(print()).andExpect(status().isBadRequest())
            .andExpect(jsonPath("error", is("Bad Request")))
            .andExpect(jsonPath("timestamp", is(notNullValue())))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("path", is(notNullValue())))
            .andDo(document("error-example",
                            responseFields(
                                fieldWithPath("error").description("The HTTP error that occurred, e.g. `Bad Request`"),
                                fieldWithPath("message").description("A description of the cause of the error"),
                                fieldWithPath("path").description("The path to which the request was made"),
                                fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
                                fieldWithPath("timestamp").description("The time, in milliseconds, at which the error occurred"))));
    }

    @Test
    public void indexExample() throws Exception {
        this.mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andDo(document("index-example",
                                    links(
                                        linkWithRel("customer").description("The <<resources-customer,Customer resource>>"),
                                        linkWithRel("address").description("The <<resources-address,Address resource>>"),
                                        linkWithRel("profile").description("The ALPS profile for the service")),
                                    responseFields(
                                        fieldWithPath("_links").description("<<resources-index-links,Links>> to other resources"))));

    }

}
