package com.domain.auth.role.controller;

import com.domain.auth.role.dto.RoleDto;
import com.domain.auth.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleController {
    private final RoleService service;

    @GetMapping("/all")
    public ResponseEntity<List<RoleDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<RoleDto> findByName(@PathVariable String name) {
        return ResponseEntity.ok(service.findByName(name));
    }

    @PostMapping
    public ResponseEntity<RoleDto> create(@RequestBody RoleDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @DeleteMapping
    public ResponseEntity<RoleDto> delete(@RequestBody RoleDto dto) {
        return ResponseEntity.ok(service.delete(dto));
    }
}
