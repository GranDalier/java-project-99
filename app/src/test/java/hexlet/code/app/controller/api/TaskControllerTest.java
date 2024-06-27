package hexlet.code.app.controller.api;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.util.ModelGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
        .JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public final class TaskControllerTest {

    private static final String URL_PATH = "/api/tasks";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper om;

    private User testUser;
    private JwtRequestPostProcessor userToken;

    private TaskStatus testTaskStatus;

    private Task testTask;

    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(testUser);
        userToken = jwt().jwt(builder -> builder.subject(testUser.getEmail()));

        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(testTaskStatus);

        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTask.setAssignee(testUser);
        testTask.setTaskStatus(testTaskStatus);
        taskRepository.save(testTask);
    }

    @Test
    public void testIndex() throws Exception {
        var request = get(URL_PATH).with(userToken);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        var request = get(URL_PATH + "/" + testTask.getId()).with(userToken);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("id").isNotNull(),
                v -> v.node("index").isEqualTo(testTask.getIndex()),
                v -> v.node("createdAt").isNotNull(),
                v -> v.node("assignee_id").isPresent(),
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("content").isEqualTo(testTask.getDescription()),
                v -> v.node("status").isNotNull()
        );
    }

    @Test
    public void testCreate() throws Exception {
        var taskCreateDTO = Instancio.of(modelGenerator.getTaskCreateDTOModel()).create();
        taskCreateDTO.setAssigneeId(testUser.getId());
        taskCreateDTO.setStatus(testTaskStatus.getSlug());
        var request = post(URL_PATH)
                .contentType(MediaType.APPLICATION_JSON).with(userToken)
                .content(om.writeValueAsString(taskCreateDTO));
        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("id").isNotNull(),
                v -> v.node("index").isEqualTo(taskCreateDTO.getIndex()),
                v -> v.node("createdAt").isNotNull(),
                v -> v.node("assignee_id").isPresent(),
                v -> v.node("title").isEqualTo(taskCreateDTO.getName()),
                v -> v.node("content").isEqualTo(taskCreateDTO.getDescription()),
                v -> v.node("status").isNotNull()
        );
    }

    @Test
    public void testPartialUpdate() throws Exception {
        var dto = new HashMap<String, String>();
        dto.put("title", "newTitle");
        dto.put("content", "newContent");

        var request = put(URL_PATH + "/" + testTask.getId()).with(userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        var updatedUser = userRepository.findById(testUser.getId())
                .orElseThrow();

        assertThatJson(body).and(
                v -> v.node("id").isNotNull(),
                v -> v.node("title").isEqualTo(dto.get("title")),
                v -> v.node("content").isEqualTo(dto.get("content"))
        );
        assertThat(updatedUser.getPassword()).isEqualTo(testUser.getPassword());
    }

    @Test
    public void testDelete() throws Exception {
        var testTask1 = Instancio.of(modelGenerator.getTaskModel()).create();
        testTask1.setTaskStatus(testTaskStatus);
        taskRepository.save(testTask1);

        var request = delete(URL_PATH + "/" + testTask1.getId()).with(userToken);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertFalse(taskRepository.existsById(testTask1.getId()));
    }

    @Test
    public void testSecurity() throws Exception {
        var requestIndex = get(URL_PATH);
        var requestShow = get(URL_PATH + "/" + testTask.getId());
        var requestShowNoContent = get(URL_PATH + "/100");
        var requestUpdate = put(URL_PATH + "/" + testTask.getId());
        var requestDelete = delete(URL_PATH + "/" + testTask.getId());
        var unauthorizedRequests = List.of(
                requestIndex, requestShow, requestShowNoContent, requestUpdate, requestDelete
        );
        for (var req : unauthorizedRequests) {
            mockMvc.perform(req)
                    .andExpect(status().isUnauthorized());
        }
    }
}
