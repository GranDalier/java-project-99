package hexlet.code.app.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
public final class UserControllerTest {

    private static final String URL_PATH = "/api/users";

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
        var request = get(URL_PATH);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        userRepository.save(testUser);

        var request = get(URL_PATH + "/" + testUser.getId());
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
        testUser2.setPassword(null);

        var request1 = post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testUser1));
        var request2 = post(URL_PATH)
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

        var request = put(URL_PATH + "/" + testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        var updatedUser = userRepository.findById(testUser.getId())
                .orElseThrow();

        assertThatJson(body).and(
                v -> v.node("email").isEqualTo(dto.get("email")),
                v -> v.node("firstName").isEqualTo(dto.get("firstName")),
                v -> v.node("lastName").isEqualTo(dto.get("lastName")),
                v -> v.node("password").isAbsent()
        );
        assertThat(updatedUser.getPassword()).isEqualTo(dto.get("password"));
    }

    @Test
    public void testPartialUpdate() throws Exception {
        userRepository.save(testUser);

        var dto = new HashMap<String, String>();
        dto.put("firstName", "newFirstName");
        dto.put("lastName", "newLastName");

        var request = put(URL_PATH + "/" + testUser.getId())
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

        var request = delete(URL_PATH + "/" + testUser.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(userRepository.existsById(testUser.getId())).isEqualTo(false);
    }

    private User createUser() {
        return Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getPassword), () -> faker.internet().password())
                .create();
    }
}
