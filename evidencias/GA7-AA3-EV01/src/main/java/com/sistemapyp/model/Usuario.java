package com.sistemapyp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

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
 * - En la evidencia previa (AA2-EV02), esta clase era un POJO plano sin anotaciones
 *   cuyas columnas debían mapearse manualmente fila por fila mediante un RowMapper
 *   de JdbcTemplate: `(rs, n) -> new Usuario(rs.getString("id_usuario"), ...)`.
 * - En esta evidencia (AA3-EV01), se transforma en una ENTIDAD JPA (Java Persistence API):
 *   1. @Entity y @Table: Hibernate mapea automáticamente esta clase con la tabla
 *      física 'usuario' de la base de datos MySQL (sistema_pyp).
 *   2. Validaciones declarativas de Jakarta Validation (@NotBlank, @Min, @Max, @Pattern)
 *      que hacen cumplir en tiempo de ejecución las restricciones CHECK del modelo físico:
 *      `chk_edad: edad BETWEEN 0 AND 120` y `chk_tipo_usuario: PACIENTE | PERSONAL_SALUD`.
 *   3. Desacoplamiento total de sentencias SQL directas.
 * ============================================================================
 */
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    // ========================================================================
    // ATRIBUTOS MAPEADOS CON ANOTACIONES JPA Y VALIDACIONES DECLARATIVAS
    // ========================================================================

    /**
     * [CLAVE PRIMARIA]
     * @Id: Define este campo como el identificador único (Primary Key) de la entidad.
     * @Column: Vincula con la columna 'id_usuario' (Documento de Identidad del paciente/personal).
     * @NotBlank: Valida que el documento no sea nulo ni contenga únicamente espacios en blanco.
     */
    @Id
    @Column(name = "id_usuario", length = 30, nullable = false)
    @NotBlank(message = "El número de documento / ID de usuario es obligatorio.")
    @Size(min = 3, max = 30, message = "El documento debe contener entre 3 y 30 caracteres.")
    private String idUsuario;

    /**
     * [NOMBRE COMPLETO]
     * @Column: Vincula con la columna 'nombre' de tipo VARCHAR(120), obligatorio (nullable = false).
     */
    @Column(name = "nombre", length = 120, nullable = false)
    @NotBlank(message = "El nombre completo del usuario es obligatorio.")
    @Size(min = 2, max = 120, message = "El nombre debe contener entre 2 y 120 caracteres.")
    private String nombre;

    /**
     * [GÉNERO / SEXO BIOLÓGICO]
     * Relevante para las métricas e indicadores de Promoción y Prevención en Salud (PyP).
     */
    @Column(name = "sexo", length = 30)
    private String sexo;

    /**
     * [EDAD DEL PACIENTE - RESTRICCIÓN CHECK: chk_edad]
     * @Min(0) y @Max(120): Garantizan que la edad registrada esté dentro del rango
     * de vida biológicamente válido para las curvas de crecimiento y prevención.
     */
    @Column(name = "edad")
    @Min(value = 0, message = "La edad mínima permitida es 0 años.")
    @Max(value = 120, message = "La edad máxima permitida es 120 años.")
    private Integer edad;

    /**
     * [TELÉFONO DE CONTACTO]
     * Permite coordinar llamadas de seguimiento médico y recordatorios de citas PyP.
     */
    @Column(name = "telefono", length = 30)
    private String telefono;

    /**
     * [PAÍS DE RESIDENCIA O PROCEDENCIA]
     */
    @Column(name = "pais", length = 80)
    private String pais;

    /**
     * [ROL EN EL SISTEMA - RESTRICCIÓN CHECK: chk_tipo_usuario]
     * @Pattern: Restringe los valores estrictamente a los dos roles del Sistema PyP:
     * - 'PACIENTE'
     * - 'PERSONAL_SALUD'
     */
    @Column(name = "tipo_usuario", length = 30, nullable = false)
    @NotBlank(message = "El tipo de usuario es obligatorio.")
    @Pattern(regexp = "PACIENTE|PERSONAL_SALUD", 
             message = "El tipo de usuario debe ser exactamente 'PACIENTE' o 'PERSONAL_SALUD'.")
    private String tipoUsuario;

    // ========================================================================
    // CONSTRUCTORES
    // ========================================================================

    /**
     * Constructor vacío requerido obligatoriamente por el estándar JPA/Hibernate
     * para la instanciación por reflexión al recuperar registros de la base de datos.
     */
    public Usuario() {
    }

    /**
     * Constructor sobrecargado con todos los atributos para facilitar la creación
     * rápida de instancias en servicios, pruebas unitarias y controladores.
     */
    public Usuario(String idUsuario, String nombre, String sexo, Integer edad, 
                   String telefono, String pais, String tipoUsuario) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.sexo = sexo;
        this.edad = edad;
        this.telefono = telefono;
        this.pais = pais;
        this.tipoUsuario = tipoUsuario;
    }

    // ========================================================================
    // MÉTODOS GETTERS Y SETTERS (Encapsulamiento Java Beans)
    // ========================================================================

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    // ========================================================================
    // MÉTODOS EQUALS, HASHCODE Y TOSTRING (Buenas Prácticas en JPA)
    // ========================================================================

    /**
     * Dos entidades Usuario se consideran idénticas si comparten la misma clave primaria (idUsuario).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(idUsuario, usuario.idUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario='" + idUsuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", sexo='" + sexo + '\'' +
                ", edad=" + edad +
                ", telefono='" + telefono + '\'' +
                ", pais='" + pais + '\'' +
                ", tipoUsuario='" + tipoUsuario + '\'' +
                '}';
    }
}
