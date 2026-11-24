package com.sistemapyp.service;

import com.sistemapyp.model.Usuario;
import com.sistemapyp.repository.IUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
 * NOVEDAD Y CAMBIO IMPLEMENTADO EN ESTAS PRUEBAS (AA3-EV01):
 * - En las evidencias previas (EV01 y EV02), las pruebas requerían scripts manuales
 *   o archivos de texto de prueba sin aserciones formales del framework.
 * - En esta evidencia (AA3-EV01), se implementan PRUEBAS AUTOMATIZADAS CON JUNIT 5
 *   y SPRING BOOT TEST:
 *   1. @SpringBootTest: Carga el contexto completo de dependencias de Spring.
 *   2. @ActiveProfiles("test"): Utiliza la base de datos en memoria H2 para ejecutar
 *      los tests en menos de 2 segundos de forma aislada e independiente.
 *   3. @Transactional: Deshace automáticamente los cambios en la base de datos tras
 *      cada prueba, garantizando que un test no afecte al siguiente.
 *   4. Validación de reglas de negocio: Comprueba inserción, unicidad de documento,
 *      actualización, filtrado por rol y eliminación.
 * ============================================================================
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("CP01 - Debe registrar un nuevo paciente exitosamente en el Sistema PyP")
    void debeRegistrarUsuarioExitosamente() {
        // 1. Preparar datos de prueba
        Usuario paciente = new Usuario(
                "1001", "Angie Garzon", "FEMENINO", 22, 
                "3001234567", "Colombia", "PACIENTE"
        );

        // 2. Ejecutar acción en el servicio
        Usuario guardado = usuarioService.registrar(paciente);

        // 3. Validar resultados con aserciones JUnit 5
        assertNotNull(guardado, "El usuario guardado no debe ser nulo");
        assertEquals("1001", guardado.getIdUsuario());
        assertEquals("Angie Garzon", guardado.getNombre());
        assertEquals("PACIENTE", guardado.getTipoUsuario());
        assertTrue(usuarioRepository.existsById("1001"), "El usuario debe existir en la base de datos");
    }

    @Test
    @DisplayName("CP02 - Debe rechazar el registro si el documento de identidad ya existe")
    void debeLanzarExcepcionCuandoDocumentoYaExiste() {
        // 1. Registrar paciente inicial
        Usuario paciente1 = new Usuario(
                "1002", "Linyermar Altuzarra", "FEMENINO", 21, 
                "3109876543", "Colombia", "PACIENTE"
        );
        usuarioService.registrar(paciente1);

        // 2. Intentar registrar otro paciente con el MISMO documento (1002)
        Usuario duplicado = new Usuario(
                "1002", "Otra Persona", "MASCULINO", 30, 
                "3000000000", "Colombia", "PACIENTE"
        );

        // 3. Verificar que la regla de negocio lance IllegalArgumentException
        IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class, 
                () -> usuarioService.registrar(duplicado),
                "Debe lanzar excepción por documento duplicado"
        );

        assertTrue(excepcion.getMessage().contains("Ya existe un usuario registrado"),
                "El mensaje de error debe indicar la duplicidad del documento");
    }

    @Test
    @DisplayName("CP03 - Debe actualizar correctamente los datos de un usuario existente")
    void debeActualizarUsuarioExitosamente() {
        // 1. Registrar usuario base
        Usuario usuario = new Usuario(
                "2001", "Dr. Roberto Gomez", "MASCULINO", 45, 
                "3205557788", "Colombia", "PERSONAL_SALUD"
        );
        usuarioService.registrar(usuario);

        // 2. Modificar datos (nuevo teléfono y edad)
        usuario.setTelefono("3209999999");
        usuario.setEdad(46);
        Usuario actualizado = usuarioService.actualizar(usuario);

        // 3. Validar actualización
        assertEquals("3209999999", actualizado.getTelefono());
        assertEquals(46, actualizado.getEdad());
    }

    @Test
    @DisplayName("CP04 - Debe listar usuarios filtrados por rol (PACIENTE o PERSONAL_SALUD)")
    void debeListarUsuariosPorTipo() {
        // 1. Registrar dos pacientes y un médico con documentos válidos (mínimo 3 caracteres)
        usuarioService.registrar(new Usuario("101", "Ana Gomez", "FEMENINO", 20, "111222333", "Colombia", "PACIENTE"));
        usuarioService.registrar(new Usuario("102", "Beatriz Ruiz", "FEMENINO", 25, "222333444", "Colombia", "PACIENTE"));
        usuarioService.registrar(new Usuario("201", "Dr. Carlos Silva", "MASCULINO", 40, "333444555", "Colombia", "PERSONAL_SALUD"));

        // 2. Filtrar por PACIENTE
        List<Usuario> pacientes = usuarioService.listarPorTipo("PACIENTE");
        List<Usuario> personal = usuarioService.listarPorTipo("PERSONAL_SALUD");

        // 3. Validar cantidades
        assertEquals(2, pacientes.size(), "Deben existir exactamente 2 pacientes");
        assertEquals(1, personal.size(), "Debe existir exactamente 1 personal de salud");
    }

    @Test
    @DisplayName("CP05 - Debe eliminar un usuario del sistema correctamente")
    void debeEliminarUsuarioExitosamente() {
        // 1. Registrar usuario
        Usuario u = new Usuario("9999", "Usuario Temporal", "OTRO", 30, "000", "CO", "PACIENTE");
        usuarioService.registrar(u);
        assertTrue(usuarioRepository.existsById("9999"));

        // 2. Eliminar
        usuarioService.eliminar("9999");

        // 3. Validar que ya no exista
        assertFalse(usuarioRepository.existsById("9999"), "El usuario debe haber sido removido");
        Optional<Usuario> consulta = usuarioService.buscarPorId("9999");
        assertTrue(consulta.isEmpty(), "La consulta por ID debe retornar Optional vacío");
    }
}
