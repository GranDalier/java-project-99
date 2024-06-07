package hexlet.code.app.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.instancio.Select;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import net.datafaker.Faker;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
public final class UserControllerTest {

    private static final String URL_PATH = "/api/users";
    private static final int PASS_MIN = 3;
    private static final int PASS_MAX = 100;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper om;

    private User testUser;

    @BeforeEach
    public void setUp() {
        this.testUser = createUser();
    }

    @Test
    public void testIndex() throws Exception {
        var request = get(URL_PATH).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        userRepository.save(testUser);

        var request = get(URL_PATH + "/" + testUser.getId()).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("email").isEqualTo(testUser.getEmail()),
                v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                v -> v.node("lastName").isEqualTo(testUser.getLastName()),
                v -> v.node("password").isAbsent()
        );
    }

    @Test
    public void testCreate() throws Exception {
        var request = post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testUser));
        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("email").isEqualTo(testUser.getEmail()),
                v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                v -> v.node("lastName").isEqualTo(testUser.getLastName()),
                v -> v.node("password").isAbsent()
        );
    }

    @Test
    public void testInvalidCreate() throws Exception {
        var testUser1 = createUser();
        testUser1.setEmail("badexample@com");
        var testUser2 = createUser();
        testUser2.setPasswordDigest(null);

        var request1 = post(URL_PATH).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testUser1));
        var request2 = post(URL_PATH).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testUser2));

        mockMvc.perform(request1)
                .andExpect(status().isBadRequest());
        mockMvc.perform(request2)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate() throws Exception {
        userRepository.save(testUser);

        var dto = new HashMap<String, String>();
        dto.put("email", "example@gmail.com");
        dto.put("firstName", "newFirstName");
        dto.put("lastName", "newLastName");
        dto.put("password", "secret");

        var request = put(URL_PATH + "/" + testUser.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("email").isEqualTo(dto.get("email")),
                v -> v.node("firstName").isEqualTo(dto.get("firstName")),
                v -> v.node("lastName").isEqualTo(dto.get("lastName")),
                v -> v.node("password").isAbsent()
        );
    }

    @Test
    public void testPartialUpdate() throws Exception {
        userRepository.save(testUser);

        var dto = new HashMap<String, String>();
        dto.put("firstName", "newFirstName");
        dto.put("lastName", "newLastName");

        var request = put(URL_PATH + "/" + testUser.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        var updatedUser = userRepository.findById(testUser.getId())
                .orElseThrow();

        assertThatJson(body).and(
                v -> v.node("email").isEqualTo(testUser.getEmail()),
                v -> v.node("firstName").isEqualTo(dto.get("firstName")),
                v -> v.node("lastName").isEqualTo(dto.get("lastName")),
                v -> v.node("password").isAbsent()
        );
        assertThat(updatedUser.getPassword()).isEqualTo(testUser.getPassword());
    }

    @Test
    public void testDelete() throws Exception {
        userRepository.save(testUser);

        var request = delete(URL_PATH + "/" + testUser.getId()).with(jwt());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(userRepository.existsById(testUser.getId())).isEqualTo(false);
    }

    @Test
    public void testSecurity() throws Exception {
        userRepository.save(testUser);

        var requestIndex = get(URL_PATH);
        var requestShow = get(URL_PATH + "/" + testUser.getId());
        var requestShowNoUser = get(URL_PATH + "/100");
        var requestUpdate = put(URL_PATH + "/" + testUser.getId());
        var requestDelete = delete(URL_PATH + "/" + testUser.getId());
        var unauthorizedRequests = List.of(requestIndex, requestShow, requestShowNoUser, requestUpdate, requestDelete);
        for (var req : unauthorizedRequests) {
            mockMvc.perform(req)
                    .andExpect(status().isUnauthorized());
        }
    }

    private User createUser() {
        return Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getPasswordDigest), () -> faker.internet().password(PASS_MIN, PASS_MAX))
                .create();
    }
}
