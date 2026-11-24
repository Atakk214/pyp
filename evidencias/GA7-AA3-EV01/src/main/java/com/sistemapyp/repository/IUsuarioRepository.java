package com.sistemapyp.repository;

import com.sistemapyp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
 * - En las evidencias anteriores (EV01 y EV02), para hacer persistencia se requería:
 *   1. Escribir sentencias SQL manuales en Strings: "SELECT ... WHERE id_usuario = ?".
 *   2. Crear clases DAO (IUsuarioDAO, UsuarioDAOImpl, UsuarioWebDAO) con decenas de líneas
 *      de código repetitivo (abrir conexiones, crear PreparedStatement, setear parámetros,
 *      ejecutar y capturar SQLExceptions).
 * - En esta evidencia (AA3-EV01), se aplica el patrón REPOSITORY con SPRING DATA JPA:
 *   1. Al heredar de JpaRepository<Usuario, String>, Spring genera automáticamente en
 *      tiempo de ejecución la implementación de todas las operaciones CRUD fundamentales:
 *      - save(entidad): Registrar o actualizar usuario.
 *      - findById(id): Buscar por documento de identidad.
 *      - findAll(): Listar todos los usuarios.
 *      - deleteById(id): Eliminar usuario por ID.
 *      - existsById(id): Comprobar si existe un documento registrado.
 *   2. Consultas derivadas por nombre de método (Query Methods): Spring Data analiza la firma
 *      del método en Java y construye internamente la consulta SQL optimizada para MySQL.
 * ============================================================================
 */
@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, String> {

    /**
     * [MÉTODO DE CONSULTA DERIVADA POR CONVENCIÓN DE NOMBRES - QUERY METHOD]
     * Spring Data traduce automáticamente este método a la consulta SQL:
     * `SELECT * FROM usuario WHERE tipo_usuario = ? ORDER BY nombre ASC`
     * 
     * Útil para filtrar en los paneles del Sistema PyP únicamente los registros
     * de 'PACIENTE' o del 'PERSONAL_SALUD'.
     * 
     * @param tipoUsuario Tipo de usuario ('PACIENTE' o 'PERSONAL_SALUD')
     * @return Lista de usuarios que coinciden con el rol solicitado
     */
    List<Usuario> findByTipoUsuarioOrderByNombreAsc(String tipoUsuario);

    /**
     * [BÚSQUEDA DINÁMICA POR COINCIDENCIA DE NOMBRE]
     * Traduce a: `SELECT * FROM usuario WHERE UPPER(nombre) LIKE UPPER('%' || ? || '%')`
     * 
     * Permite que el personal de salud busque rápidamente pacientes en el sistema
     * ingresando parte de su nombre o apellido.
     * 
     * @param criterio Cadena de texto a buscar
     * @return Lista de usuarios cuyos nombres coinciden parcialmente
     */
    List<Usuario> findByNombreContainingIgnoreCase(String criterio);

    /**
     * [CONTEO POR TIPO DE USUARIO]
     * Traduce a: `SELECT COUNT(*) FROM usuario WHERE tipo_usuario = ?`
     * 
     * Proporciona estadísticas en tiempo real para los indicadores de salud pública
     * del Sistema de Promoción y Prevención (PyP).
     * 
     * @param tipoUsuario Rol a contabilizar
     * @return Total de registros bajo dicho rol
     */
    long countByTipoUsuario(String tipoUsuario);
}
