package hexlet.code.app.controller.api;

import hexlet.code.app.dto.label.LabelDTO;
import hexlet.code.app.dto.label.LabelCreateDTO;
import hexlet.code.app.dto.label.LabelUpdateDTO;
import hexlet.code.app.service.LabelService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    @Autowired
    private LabelService labelService;

    /**
     * Authentication needed.
     * @return {@code List<LabelDTO>}
     */
    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<LabelDTO>> index() {
        var taskStatuses = labelService.findAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(taskStatuses.size()))
                .body(taskStatuses);
    }

    /**
     * Authentication needed.
     * @param id long
     * @return {@code LabelDTO}
     */
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO show(@PathVariable long id) {
        return labelService.findById(id);
    }

    /**
     * Authentication needed.
     * @param labelData LabelCreateDTO
     * @return {@code LabelDTO}
     */
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@RequestBody @Valid LabelCreateDTO labelData) {
        return labelService.create(labelData);
    }

    /**
     * Authentication needed.
     * @param id long
     * @param labelData LabelUpdateDTO
     * @return {@code LabelDTO}
     */
    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO update(@PathVariable long id, @RequestBody @Valid LabelUpdateDTO labelData) {
        return labelService.update(id, labelData);
    }

    /**
     * Authentication needed.
     * @param id long
     */
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        labelService.delete(id);
    }
}
