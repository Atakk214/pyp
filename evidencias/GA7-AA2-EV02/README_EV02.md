# Sistema PyP — GA7-220501096-AA2-EV02

Módulo web de gestión de usuarios para Promoción y Prevención en Salud. Basado en las clases Java de la EV01 aportadas por el equipo. Implementa HTML, servlet Java (`UsuarioServlet`), `doGet`, `doPost`, JSP y CRUD con H2 local, sin necesidad de instalar MySQL.

## Requisitos
Java JDK 21. Windows: abrir terminal en esta carpeta y ejecutar `./mvnw.cmd spring-boot:run` (en PowerShell: `.\mvnw.cmd spring-boot:run`). Abrir http://localhost:8080/usuarios. Para detener: Ctrl+C.

## Pruebas
`.\mvnw.cmd test` (Windows) o `./mvnw test` (Linux/Mac). El test CRUD `UsuarioWebDAOTest` verifica crear, leer, actualizar, listar y eliminar con H2. Se conservó el test MySQL de EV01 como archivo de referencia `.txt` para que no dependa de MySQL.

## Estructura
- `src/main/java/com/sistemapyp/model/Usuario.java`: modelo reutilizado EV01.
- `src/main/java/com/sistemapyp/web/UsuarioWebDAO.java`: persistencia H2 y CRUD.
- `src/main/java/com/sistemapyp/web/UsuarioServlet.java`: servlet con GET y POST.
- `src/main/webapp/WEB-INF/jsp/`: páginas JSP de listado y formulario.
- `src/main/resources/static/`: CSS e inicio HTML.
- `src/main/resources/schema.sql`: tabla usuarios.

## Nota
El módulo EV01 de consola y JDBC/MySQL permanece en `src/main/java/com/sistemapyp/{app,config,dao}` como referencia. Esta EV02 usa su propia persistencia H2. Los datos se guardan en la carpeta local `datos/`, ignorada por Git. Para probar, usar datos ficticios: no ingresar información real de pacientes.

## Versionamiento
Se entrega el código fuente para subir a un repositorio Git del equipo. El archivo `ENLACE_REPOSITORIO.txt` indica el repositorio de referencia; **esta copia no ha sido subida automáticamente**.
