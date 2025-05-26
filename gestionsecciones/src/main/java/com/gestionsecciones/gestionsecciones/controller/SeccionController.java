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

import com.gestionsecciones.gestionsecciones.model.Seccion;
import com.gestionsecciones.gestionsecciones.service.SeccionService;

@RestController
@RequestMapping("/api/seccion")
public class SeccionController {

    @Autowired
    private SeccionService seccionService;

    @PostMapping
    public ResponseEntity<Seccion> postSeccion(@RequestBody Seccion seccion) {
        Seccion buscado = seccionService.findxIdSeccion(seccion.getIdSeccion());
        if (buscado == null) {
            return new ResponseEntity<>(seccionService.crearSeccion(seccion), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping
    public ResponseEntity<List<Seccion>> getSeccion() {
        List<Seccion> lista = seccionService.findAllSecciones();
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(lista, HttpStatus.ACCEPTED);
        }
    }

    @PutMapping("/{idSeccion}")
    public ResponseEntity<Seccion> putSeccion(@PathVariable Integer idSeccion, @RequestBody Seccion seccion) {
        Seccion actualizado = seccionService.editSeccion(idSeccion, seccion);
        if (actualizado != null) {
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{idSeccion}")
    public ResponseEntity<Seccion> deleteSeccion(@PathVariable Integer idSeccion) {
        Seccion eliminado = seccionService.eliminarSeccion(idSeccion);
        if (eliminado != null) {
            return new ResponseEntity<>(eliminado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{idSeccion}/asignarProfesor/{profesorId}")
    public ResponseEntity<Seccion> asignarProfesorSeccion(@PathVariable Integer idSeccion, @PathVariable String profesorId) {
        Seccion seccionActualizada = seccionService.asignarProfesorSeccion(idSeccion, profesorId);
        if (seccionActualizada != null) {
            return new ResponseEntity<>(seccionActualizada, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/{idSeccion}/alumnos")
    public ResponseEntity<Seccion> asignarAlumnos(
            @PathVariable int idSeccion,
            @RequestBody List<String> alumnos) {  
        Seccion sec = seccionService.asignarAlumnosSeccion(idSeccion, alumnos);
        if (sec != null) {
            return ResponseEntity.ok(sec); 
        }
        return ResponseEntity.notFound().build();
    }


    

}
