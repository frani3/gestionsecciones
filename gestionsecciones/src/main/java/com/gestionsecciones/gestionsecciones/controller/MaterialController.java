package com.gestionsecciones.gestionsecciones.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.gestionsecciones.gestionsecciones.model.Material;
import com.gestionsecciones.gestionsecciones.service.MaterialService;

@RestController
@RequestMapping("/api/material")
public class MaterialController {
    
    @Autowired
    private MaterialService materialService;

    @PostMapping
    public ResponseEntity<Material> postMaterial(@RequestBody Material material) {
        Material buscado = materialService.findxIdMaterial(material.getIdMaterial());
        if (buscado == null) {
            return new ResponseEntity<>(materialService.crearMaterial(material), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping
    public ResponseEntity<List<Material>> getMaterial() {
        List<Material> lista = materialService.findAllMateriales();
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(lista, HttpStatus.ACCEPTED);
        }
    }

    @PutMapping("/{idMaterial}")
    public ResponseEntity<Material> putMaterial(@PathVariable Integer idMaterial, @RequestBody Material material) {
        Material actualizado = materialService.editMaterial(idMaterial, material);
        if (actualizado != null) {
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{idMaterial}")
    public ResponseEntity<Material> deleteMaterial(@PathVariable Integer idMaterial) {
        Material eliminado = materialService.eliminarMaterial(idMaterial);
        if (eliminado != null) {
            return new ResponseEntity<>(eliminado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    
}
