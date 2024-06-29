package hexlet.code.app.component;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.repository.LabelRepository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public final class DataInitializerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    @Autowired
    private LabelMapper labelMapper;

    @Test
    public void testInitializeAdmin() throws Exception {
        var request = get("/api/users/1").with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        var admin = userRepository.findById(1L).orElseThrow();

        assertThatJson(body).isPresent();
        assertThatJson(userMapper.map(admin)).isEqualTo(body);
    }

    @Test
    public void testInitializeStatuses() throws Exception {
        var request = get("/api/task_statuses").with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        var statuses = taskStatusRepository.findAll().stream()
                .map(taskStatusMapper::map)
                .toList();

        assertThatJson(body).isArray().isNotEmpty();
        assertThatJson(statuses).isIn(body);
    }

    @Test
    public void testInitializeLabels() throws Exception {
        var request = get("/api/labels").with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        var labels = labelRepository.findAll().stream()
                .map(labelMapper::map)
                .toList();

        assertThatJson(body).isArray().isNotEmpty();
        assertThatJson(labels).isIn(body);
    }
}
