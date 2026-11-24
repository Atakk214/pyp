package com.sistemapyp.controller;

import com.sistemapyp.model.Usuario;
import com.sistemapyp.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * SISTEMA DE PROMOCIÓN Y PREVENCIÓN EN SALUD (SISTEMA PyP)
 * SENA - CENTRO DE FORMACIÓN / REGIONAL
 * TECNÓLOGO EN ANÁLISIS Y DESARROLLO DE SOFTWARE (ADSO) - FICHA: 3336045
 * 
 * EVIDENCIA: GA7-220501096-AA3-EV01
 * ACTIVIDAD: Codificación de módulos del software stand-alone, web y móvil
 * COMPONENTE FORMATIVO: CF20 - Tecnologías y frameworks en el ecosistema Java
 * INTEGRANTES: Angie Catalina Garzón Duarte & Linyermar Miryet Altuzarra Balza
 * INSTRUCTORA: Jenny Guio
 * 
 * ============================================================================
 * NOVEDAD Y CUMPLIMIENTO DEL REQUISITO MÓVIL (AA3-EV01):
 * - El título oficial de esta evidencia exige codificar módulos para ambientes:
 *   "STAND-ALONE, WEB Y MÓVIL".
 * - ¿Cómo se cumple el requisito MÓVIL en esta arquitectura con Framework?
 *   1. Implementación de una API RESTful con @RestController que serializa datos
 *      en formato universal JSON (JavaScript Object Notation).
 *   2. Permite que la aplicación móvil Android del Sistema PyP (desarrollada con
 *      vistas XML registradas en este repositorio) consuma los servicios backend
 *      mediante clientes HTTP (como Retrofit o Volley).
 *   3. Desacoplamiento total: La app móvil puede registrar signos vitales, consultar
 *      citas y crear pacientes consumiendo los mismos servicios que la web.
 *   4. @CrossOrigin: Permite peticiones asíncronas desde emuladores móviles,
 *      dispositivos físicos y navegadores sin bloqueos de política CORS.
 * ============================================================================
 */
@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    public UsuarioRestController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * [ENDPOINT MÓVIL / WEB: LISTAR TODOS LOS USUARIOS]
     * GET /api/v1/usuarios
     * 
     * @return 200 OK con el arreglo JSON de todos los usuarios
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    /**
     * [ENDPOINT MÓVIL / WEB: CONSULTAR USUARIO POR DOCUMENTO]
     * GET /api/v1/usuarios/{id}
     * 
     * @param id Documento del usuario
     * @return 200 OK con los datos del usuario en JSON, o 404 NOT FOUND si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable("id") String id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * [ENDPOINT MÓVIL: FILTRAR POR ROL: PACIENTE O PERSONAL_SALUD]
     * GET /api/v1/usuarios/tipo/{tipo}
     * 
     * @param tipo 'PACIENTE' o 'PERSONAL_SALUD'
     * @return 200 OK con el listado JSON correspondiente
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Usuario>> listarPorTipo(@PathVariable("tipo") String tipo) {
        return ResponseEntity.ok(usuarioService.listarPorTipo(tipo.toUpperCase()));
    }

    /**
     * [ENDPOINT MÓVIL: REGISTRAR NUEVO USUARIO DESDE LA APP ANDROID]
     * POST /api/v1/usuarios
     * 
     * Recibe el cuerpo JSON de la petición (@RequestBody), lo valida con @Valid
     * y responde con código HTTP 201 CREATED si la inserción fue exitosa.
     * 
     * @param usuario Datos del nuevo usuario
     * @return 201 CREATED con el usuario registrado o 400 BAD REQUEST si hay error
     */
    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.registrar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (IllegalArgumentException ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("timestamp", LocalDateTime.now());
            error.put("status", HttpStatus.CONFLICT.value());
            error.put("error", "Conflicto de Integridad");
            error.put("mensaje", ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    /**
     * [ENDPOINT MÓVIL: ACTUALIZAR DATOS DE PACIENTE O PERSONAL]
     * PUT /api/v1/usuarios/{id}
     * 
     * @param id Documento a actualizar
     * @param usuario Datos modificados
     * @return 200 OK con el usuario actualizado o 404 NOT FOUND
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") String id, @Valid @RequestBody Usuario usuario) {
        usuario.setIdUsuario(id);
        try {
            Usuario usuarioActualizado = usuarioService.actualizar(usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (IllegalArgumentException ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("timestamp", LocalDateTime.now());
            error.put("status", HttpStatus.NOT_FOUND.value());
            error.put("error", "Recurso no encontrado");
            error.put("mensaje", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * [ENDPOINT MÓVIL: ELIMINAR USUARIO]
     * DELETE /api/v1/usuarios/{id}
     * 
     * @param id Documento a eliminar
     * @return 204 NO CONTENT si fue removido con éxito
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") String id) {
        try {
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("timestamp", LocalDateTime.now());
            error.put("status", HttpStatus.NOT_FOUND.value());
            error.put("error", "Recurso no encontrado");
            error.put("mensaje", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * [MANEJADOR DE EXCEPCIONES DE VALIDACIÓN - HTTP 400 BAD REQUEST]
     * Si la app móvil envía un JSON con datos inválidos (ej. edad < 0 o > 120),
     * este método captura el error y responde un JSON estructurado con la lista
     * de campos erróneos y sus mensajes en español.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> manejarErroresValidacion(MethodArgumentNotValidException ex) {
        Map<String, Object> respuesta = new HashMap<>();
        Map<String, String> errores = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }

        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        respuesta.put("error", "Validación fallida en la petición");
        respuesta.put("errores", errores);

        return respuesta;
    }
}
