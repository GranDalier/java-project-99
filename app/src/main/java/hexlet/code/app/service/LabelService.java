package hexlet.code.app.service;

import hexlet.code.app.dto.label.LabelCreateDTO;
import hexlet.code.app.dto.label.LabelDTO;
import hexlet.code.app.dto.label.LabelUpdateDTO;
import hexlet.code.app.exception.ResourceAlreadyExistsException;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class LabelService {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper mapper;

    public List<LabelDTO> findAll() {
        return labelRepository.findAll().stream()
                .map(mapper::map)
                .toList();
    }

    public LabelDTO findById(long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label id " + id + " not found"));
        return mapper.map(label);
    }

    public LabelDTO create(LabelCreateDTO labelData) {
        var label = mapper.map(labelData);
        checkIfUnique(label);
        labelRepository.save(label);
        return mapper.map(label);
    }

    public LabelDTO update(long id, LabelUpdateDTO labelData) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label id " + id + " not found"));
        mapper.update(labelData, label);
        checkIfUnique(label);
        labelRepository.save(label);
        return mapper.map(label);
    }

    public void delete(long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label id " + id + " not found"));
        labelRepository.delete(label);
    }

    private void checkIfUnique(Label label) {
        var name = label.getName();
        var optionalLabel = labelRepository.findByName(name);
        if (optionalLabel.isPresent()) {
            throw new ResourceAlreadyExistsException("Label with such name already exists");
        }
    }
}
