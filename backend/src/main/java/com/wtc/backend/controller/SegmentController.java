package com.wtc.backend.controller;

import com.wtc.backend.dto.SegmentDTO;
import com.wtc.backend.dto.SegmentRequest;
import com.wtc.backend.service.SegmentService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/segments")
public class SegmentController {

    private final SegmentService segmentService;

    public SegmentController(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    @GetMapping
    public ResponseEntity<List<SegmentDTO>> getByOperator(@RequestParam String operatorId) {
        return ResponseEntity.ok(segmentService.getByOperator(operatorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SegmentDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(segmentService.getById(id));
    }

    @PostMapping
    public ResponseEntity<SegmentDTO> create(@Valid @RequestBody SegmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(segmentService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SegmentDTO> update(@PathVariable String id, @Valid @RequestBody SegmentRequest request) {
        return ResponseEntity.ok(segmentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String id) {
        segmentService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Segmento removido com sucesso"));
    }
}
