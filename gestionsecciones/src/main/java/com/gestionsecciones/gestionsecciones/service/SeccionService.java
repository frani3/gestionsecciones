package com.gestionsecciones.gestionsecciones.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gestionsecciones.gestionsecciones.model.CursoDTO;
import com.gestionsecciones.gestionsecciones.model.Seccion;
import com.gestionsecciones.gestionsecciones.model.UsuarioDTO;
import com.gestionsecciones.gestionsecciones.repository.SeccionRepository;

@Service
public class SeccionService {
    
    /*
     * +crearSeccion()
     * +editarSeccion(nombre: String)
     * +eliminarSeccion()
     * +Listar Seccion(): Seccion
     * +asignarProfesorSeccion(profesorId: String)
     * +asignarAlumnosSeccion(listaAlumnos: List<String>)
     * 
     */

    @Autowired
    private SeccionRepository seccionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Seccion crearSeccion(Seccion seccion) {
        String url = "http://localhost:8082/api/curso/" + seccion.getIdCurso();
        CursoDTO curso = restTemplate.getForObject(url, CursoDTO.class);
        if (curso != null) {
            //seccion.setIdCurso(curso.getIdCurso());
            seccion.setNombreCurso(curso.getNombre());
        }
        

        return seccionRepository.save(seccion);
    }



    public Seccion findxIdSeccion(int idSeccion) {
        return seccionRepository.findById(idSeccion);
    }
    

    public List<Seccion> findAllSecciones() {
        List<Seccion> secciones = seccionRepository.findAll();

        for (Seccion seccion : secciones) {
            List<String> alumnos = seccion.getAlumnos();
            
            List<String> nombresAlumnos = new ArrayList<>();
            for (String alumnoId : alumnos) {
                String url = "http://localhost:8081/api/usuario/" + alumnoId;  
                UsuarioDTO alumno = restTemplate.getForObject(url, UsuarioDTO.class);
                
                if (alumno != null) {
                    nombresAlumnos.add(alumno.getNombre() + " " + alumno.getApellido());
                }
            }
            
            seccion.setAlumnos(nombresAlumnos);
        }
        
        return secciones;
    }


    public Seccion editSeccion(Integer idSeccion, Seccion seccion) {
        Optional<Seccion> seccionExistente = seccionRepository.findById(idSeccion);
        if (seccionExistente.isPresent()) {
            Seccion seccionActualizado = seccionExistente.get();
            seccionActualizado.setNombreSeccion(seccion.getNombreSeccion());


            return seccionRepository.save(seccionActualizado);
        }
        return null;
    }

    public Seccion eliminarSeccion(int idSeccion) {
        Seccion seccion = seccionRepository.findById(idSeccion);
        if (seccion != null) {
            seccionRepository.deleteById(idSeccion);
            return seccion;
        }
        return null;
    }

    public Seccion asignarProfesorSeccion(Integer idSeccion, String profesorId) {
        

        String url = "http://localhost:8081/api/usuario/" + profesorId;
        UsuarioDTO profesor = restTemplate.getForObject(url, UsuarioDTO.class);

        if (profesor != null) {
            Seccion seccion = findxIdSeccion(idSeccion);
            seccion.setProfesorAsignado(profesor.getNombre() + " " + profesor.getApellido());
            seccion.setCorreoProfesorAsignado(profesor.getEmail());
            
            
            if (seccion != null) {

                seccion.setIdProfesor(profesor.getIdUsuario()); 
                return seccionRepository.save(seccion);
            }
        }
        return null;
    }


    public Seccion asignarAlumnosSeccion(Integer idSeccion, List<String> nuevosAlumnos) {


        Seccion sec = findxIdSeccion(idSeccion);

        if (sec != null) {
            List<String> actuales = sec.getAlumnos();


            if (actuales == null) {
                actuales = new ArrayList<>();
                sec.setAlumnos(actuales);
            }

            for (String alumnoId : nuevosAlumnos) {
                String url = "http://localhost:8081/api/usuario/" + alumnoId;
                UsuarioDTO alumno = restTemplate.getForObject(url, UsuarioDTO.class);

                if (alumno != null && !actuales.contains(alumnoId)) {
                    actuales.add(alumnoId); 
                } else if (alumno == null) {
                    throw new IllegalArgumentException("Alumno con ID " + alumnoId + " no existe.");
                }
            }


            return seccionRepository.save(sec);
        }


        return null;
    }





}
