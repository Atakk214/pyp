package com.sistemapyp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
 * - En la evidencia previa (AA2-EV02), la aplicación requería configuración manual
 *   de servlets en un contenedor web WAR.
 * - En esta evidencia (AA3-EV01), se implementa la clase principal de arranque con
 *   la anotación @SpringBootApplication, que:
 *   1. Habilita la autoconfiguración de Spring Boot (@EnableAutoConfiguration).
 *   2. Realiza el escaneo automático de componentes (@ComponentScan) en el paquete
 *      com.sistemapyp (entidades, repositorios, servicios y controladores).
 *   3. Inicia el servidor web embebido Tomcat en el puerto 8080 (Stand-alone y Web).
 * ============================================================================
 */
@SpringBootApplication
public class SistemaPypFrameworkApplication {

    /**
     * Método principal (punto de entrada de la aplicación Java).
     * Arranca el contexto completo de Spring Boot.
     * 
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(SistemaPypFrameworkApplication.class, args);
        System.out.println("====================================================================");
        System.out.println(">> [SISTEMA PyP] Módulo GA7-AA3-EV01 (Spring Boot) iniciado con éxito");
        System.out.println(">> Acceso Web Local: http://localhost:8080/usuarios");
        System.out.println(">> API REST Móvil:   http://localhost:8080/api/v1/usuarios");
        System.out.println("====================================================================");
    }
}
