package com.sistemapyp.controller;

import com.sistemapyp.model.Usuario;
import com.sistemapyp.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
 * NOVEDAD Y CAMBIO IMPLEMENTADO EN ESTA EVIDENCIA (AA3-EV01):
 * - En la evidencia previa (AA2-EV02), la interacción web se realizaba mediante
 *   un Servlet tradicional (UsuarioServlet.java) sobreescribiendo doGet() y doPost(),
 *   leyendo parámetros con req.getParameter(), parseando cadenas manualmente y
 *   despachando peticiones con req.getRequestDispatcher().
 * - En esta evidencia (AA3-EV01), se aplica el patrón MVC moderno con SPRING MVC:
 *   1. La anotación @Controller gestiona el flujo de peticiones web del navegador.
 *   2. Anotaciones semánticas @GetMapping y @PostMapping para enrutamiento limpio y RESTful.
 *   3. Enlace automático de datos (Model Binding): Los campos de los formularios HTML
 *      se mapean directamente al objeto Usuario sin código manual de conversión.
 *   4. Validación integrada con @Valid y BindingResult: Si el usuario ingresa datos
 *      erróneos (ej. edad mayor a 120), los mensajes de error se muestran en el formulario
 *      sin que la página se rompa ni pierda los datos digitados.
 *   5. Mensajes Flash con RedirectAttributes: Permiten mostrar alertas amigables tras
 *      redirecciones (Post/Redirect/Get pattern).
 * ============================================================================
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioWebController {

    private final UsuarioService usuarioService;

    /**
     * Inyección de dependencias por constructor del servicio de usuarios.
     * 
     * @param usuarioService Servicio de lógica de negocio
     */
    public UsuarioWebController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * [LISTADO GENERAL Y BÚSQUEDA WEB]
     * Atiende peticiones GET a: /usuarios o /usuarios?filtro=PACIENTE o /usuarios?q=carlos
     * 
     * Carga el listado de usuarios, los totales para indicadores de salud
     * y los envía a la vista HTML mediante el objeto Model de Spring.
     * 
     * @param filtro Rol opcional ('PACIENTE' o 'PERSONAL_SALUD')
     * @param q Texto de búsqueda opcional
     * @param model Contenedor de datos que viajará a la plantilla de Thymeleaf
     * @return Nombre lógico de la vista HTML ("usuarios/lista")
     */
    @GetMapping
    public String listarUsuarios(
            @RequestParam(name = "filtro", required = false) String filtro,
            @RequestParam(name = "q", required = false) String q,
            Model model) {

        List<Usuario> usuarios;

        if (q != null && !q.trim().isEmpty()) {
            usuarios = usuarioService.buscarPorNombre(q);
            model.addAttribute("criterioBusqueda", q.trim());
        } else if (filtro != null && !filtro.trim().isEmpty()) {
            usuarios = usuarioService.listarPorTipo(filtro.trim().toUpperCase());
            model.addAttribute("filtroActivo", filtro.trim().toUpperCase());
        } else {
            usuarios = usuarioService.listarTodos();
        }

        // Métricas para el panel del Sistema PyP
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalPacientes", usuarioService.contarPorTipo("PACIENTE"));
        model.addAttribute("totalSalud", usuarioService.contarPorTipo("PERSONAL_SALUD"));

        return "usuarios/lista";
    }

    /**
     * [FORMULARIO PARA NUEVO REGISTRO]
     * Atiende peticiones GET a: /usuarios/nuevo
     * Prepara un objeto Usuario vacío y lo envía al formulario de registro.
     * 
     * @param model Objeto para pasar datos a la vista
     * @return Nombre lógico de la vista del formulario ("usuarios/formulario")
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("modoEdicion", false);
        model.addAttribute("titulo", "Registrar Nuevo Usuario / Paciente");
        return "usuarios/formulario";
    }

    /**
     * [GUARDAR NUEVO REGISTRO]
     * Atiende peticiones POST a: /usuarios/guardar
     * Valida los datos con @Valid y el validador de Jakarta.
     * 
     * @param usuario Objeto vinculado automáticamente desde el formulario
     * @param result Resultado de la validación (contiene errores si los hay)
     * @param redirectAttributes Permite enviar mensajes de éxito que sobreviven a la redirección
     * @param model Objeto para recargar datos en la vista si hay errores
     * @return Redirección a /usuarios si es exitoso o retorno al formulario si hay errores
     */
    @PostMapping("/guardar")
    public String guardarNuevoUsuario(
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Si hay errores de validación (@NotBlank, @Min, @Max, etc.) volvemos al formulario
        if (result.hasErrors()) {
            model.addAttribute("modoEdicion", false);
            model.addAttribute("titulo", "Registrar Nuevo Usuario / Paciente");
            return "usuarios/formulario";
        }

        try {
            usuarioService.registrar(usuario);
            redirectAttributes.addFlashAttribute("mensajeExito", 
                    "Usuario con documento " + usuario.getIdUsuario() + " registrado exitosamente en el Sistema PyP.");
            return "redirect:/usuarios";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("modoEdicion", false);
            model.addAttribute("titulo", "Registrar Nuevo Usuario / Paciente");
            model.addAttribute("mensajeError", ex.getMessage());
            return "usuarios/formulario";
        }
    }

    /**
     * [FORMULARIO PARA EDICIÓN]
     * Atiende peticiones GET a: /usuarios/editar/{id}
     * Carga los datos actuales del usuario para que el personal de salud pueda modificarlos.
     * 
     * @param id Documento del usuario a editar
     * @param model Modelo para enviar datos a la plantilla
     * @param redirectAttributes Mensaje si el usuario no existe
     * @return Vista del formulario o redirección al listado
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(
            @PathVariable("id") String id,
            Model model,
            RedirectAttributes redirectAttributes) {

        return usuarioService.buscarPorId(id)
                .map(usuario -> {
                    model.addAttribute("usuario", usuario);
                    model.addAttribute("modoEdicion", true);
                    model.addAttribute("titulo", "Modificar Datos de Usuario PyP");
                    return "usuarios/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("mensajeError", 
                            "El usuario con documento " + id + " no fue encontrado.");
                    return "redirect:/usuarios";
                });
    }

    /**
     * [ACTUALIZAR REGISTRO EXISTENTE]
     * Atiende peticiones POST a: /usuarios/actualizar
     * 
     * @param usuario Objeto con los datos actualizados
     * @param result Validación de formulario
     * @param redirectAttributes Mensajes flash
     * @param model Objeto del modelo
     * @return Redirección a la lista o recarga con mensajes de validación
     */
    @PostMapping("/actualizar")
    public String actualizarUsuario(
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("modoEdicion", true);
            model.addAttribute("titulo", "Modificar Datos de Usuario PyP");
            return "usuarios/formulario";
        }

        try {
            usuarioService.actualizar(usuario);
            redirectAttributes.addFlashAttribute("mensajeExito", 
                    "Datos del usuario " + usuario.getNombre() + " actualizados con éxito.");
            return "redirect:/usuarios";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("modoEdicion", true);
            model.addAttribute("titulo", "Modificar Datos de Usuario PyP");
            model.addAttribute("mensajeError", ex.getMessage());
            return "usuarios/formulario";
        }
    }

    /**
     * [ELIMINAR REGISTRO]
     * Atiende peticiones GET a: /usuarios/eliminar/{id}
     * 
     * @param id Documento del usuario a remover
     * @param redirectAttributes Mensaje flash informativo
     * @return Redirección a la lista de usuarios
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", 
                    "Usuario con documento " + id + " eliminado correctamente del Sistema PyP.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/usuarios";
    }
}
