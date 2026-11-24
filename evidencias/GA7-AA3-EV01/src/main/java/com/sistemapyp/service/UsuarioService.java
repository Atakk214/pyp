package com.sistemapyp.service;

import com.sistemapyp.model.Usuario;
import com.sistemapyp.repository.IUsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
 * - En las evidencias previas (EV01 y EV02), la lógica de negocio estaba dispersa
 *   dentro de los servlets (UsuarioServlet.java) o en métodos sueltos de consola.
 * - En esta evidencia (AA3-EV01), se introduce formalmente la CAPA DE SERVICIO (@Service):
 *   1. Separación de responsabilidades: El controlador solo recibe y responde peticiones;
 *      el servicio es quien aplica las reglas de negocio médicas y de validación.
 *   2. Gestión transaccional declarativa (@Transactional): Garantiza las propiedades ACID
 *      de la base de datos (atomicidad, consistencia, aislamiento y durabilidad). Si ocurre
 *      un fallo inesperado, se realiza un Rollback automático protegiendo la base de datos MySQL.
 *   3. Inyección de dependencias por constructor: Estándar moderno de Spring que favorece
 *      el desacoplamiento, la inmutabilidad y facilita las pruebas unitarias con mocks.
 * ============================================================================
 */
@Service
@Transactional(readOnly = true)
public class UsuarioService {

    // [INYECCIÓN DE DEPENDENCIAS]
    // Repositorio Spring Data JPA inyectado mediante inmutabilidad (final)
    private final IUsuarioRepository usuarioRepository;

    /**
     * Constructor para inyección de dependencias gestionada por el contenedor de Spring.
     * 
     * @param usuarioRepository Instancia del repositorio de usuarios
     */
    public UsuarioService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // ========================================================================
    // MÉTODOS DE CONSULTA (Solo Lectura - readOnly = true)
    // ========================================================================

    /**
     * Obtiene el listado completo de usuarios registrados en el Sistema PyP.
     * 
     * @return Lista con todos los usuarios
     */
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Obtiene usuarios filtrados por su rol en el sistema (PACIENTE o PERSONAL_SALUD),
     * ordenados alfabéticamente por nombre.
     * 
     * @param tipoUsuario Rol del usuario
     * @return Lista filtrada y ordenada
     */
    public List<Usuario> listarPorTipo(String tipoUsuario) {
        return usuarioRepository.findByTipoUsuarioOrderByNombreAsc(tipoUsuario);
    }

    /**
     * Busca pacientes por coincidencia parcial de su nombre (búsqueda insensible a mayúsculas).
     * 
     * @param criterio Texto o nombre a buscar
     * @return Lista de coincidencias
     */
    public List<Usuario> buscarPorNombre(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return usuarioRepository.findAll();
        }
        return usuarioRepository.findByNombreContainingIgnoreCase(criterio.trim());
    }

    /**
     * Consulta un usuario por su número de documento / ID único.
     * 
     * @param idUsuario Documento a buscar
     * @return Optional con el usuario si existe, o vacío si no se encuentra
     */
    public Optional<Usuario> buscarPorId(String idUsuario) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            return Optional.empty();
        }
        return usuarioRepository.findById(idUsuario.trim());
    }

    /**
     * Obtiene la cantidad total de pacientes o personal registrados para estadísticas PyP.
     * 
     * @param tipoUsuario Rol a consultar
     * @return Total de registros
     */
    public long contarPorTipo(String tipoUsuario) {
        return usuarioRepository.countByTipoUsuario(tipoUsuario);
    }

    // ========================================================================
    // MÉTODOS DE MODIFICACIÓN / ESCRITURA (@Transactional)
    // ========================================================================

    /**
     * Registra un nuevo usuario en el Sistema PyP aplicando las reglas de negocio:
     * 1. Verifica que el documento no exista previamente para evitar registros duplicados.
     * 2. Sanitiza y formatea los datos (trim y mayúsculas en tipoUsuario).
     * 3. Persiste la entidad mediante Spring Data JPA.
     * 
     * @param usuario Objeto con la información a registrar
     * @return Usuario registrado con éxito
     * @throws IllegalArgumentException Si el documento ya se encuentra registrado
     */
    @Transactional
    public Usuario registrar(Usuario usuario) {
        // [REGLA DE NEGOCIO 1]: Validación de unicidad de documento de identidad
        if (usuarioRepository.existsById(usuario.getIdUsuario().trim())) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con el documento: " + usuario.getIdUsuario());
        }

        // [NORMALIZACIÓN DE DATOS]
        usuario.setIdUsuario(usuario.getIdUsuario().trim());
        usuario.setNombre(usuario.getNombre().trim());
        if (usuario.getTipoUsuario() != null) {
            usuario.setTipoUsuario(usuario.getTipoUsuario().trim().toUpperCase());
        }

        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza la información de un usuario existente en el Sistema PyP.
     * 
     * @param usuario Objeto con los datos modificados
     * @return Usuario actualizado
     * @throws IllegalArgumentException Si el usuario no existe en la base de datos
     */
    @Transactional
    public Usuario actualizar(Usuario usuario) {
        // [REGLA DE NEGOCIO 2]: Comprobar que el registro exista antes de modificarlo
        if (!usuarioRepository.existsById(usuario.getIdUsuario().trim())) {
            throw new IllegalArgumentException("No se encontró ningún usuario con el documento: " + usuario.getIdUsuario() + " para actualizar.");
        }

        usuario.setIdUsuario(usuario.getIdUsuario().trim());
        usuario.setNombre(usuario.getNombre().trim());
        if (usuario.getTipoUsuario() != null) {
            usuario.setTipoUsuario(usuario.getTipoUsuario().trim().toUpperCase());
        }

        return usuarioRepository.save(usuario);
    }

    /**
     * Elimina un usuario del sistema a partir de su documento de identidad.
     * 
     * @param idUsuario Documento del usuario a eliminar
     * @throws IllegalArgumentException Si el documento no existe
     */
    @Transactional
    public void eliminar(String idUsuario) {
        if (!usuarioRepository.existsById(idUsuario.trim())) {
            throw new IllegalArgumentException("No es posible eliminar: el documento " + idUsuario + " no existe en el sistema.");
        }
        usuarioRepository.deleteById(idUsuario.trim());
    }
}
