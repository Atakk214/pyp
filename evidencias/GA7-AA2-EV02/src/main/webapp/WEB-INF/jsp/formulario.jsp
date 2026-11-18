
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.sistemapyp.model.Usuario" %>

<%
    Usuario u = (Usuario) request.getAttribute("usuario");
    boolean editar = u != null;
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sistema PyP | Formulario</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos.css">
</head>

<body>
    <main class="contenedor estrecho">

        <a href="usuarios">← Volver a usuarios</a>

        <h1>
            <%= editar ? "Editar usuario" : "Registrar usuario" %>
        </h1>

        <p>Completa los datos para el seguimiento de salud.</p>

        <form class="tarjeta formulario" method="post" action="usuarios">

            <input type="hidden"
                   name="accion"
                   value="<%= editar ? "actualizar" : "crear" %>">

            <label>
                Documento
                <input type="text"
                       required
                       maxlength="30"
                       name="id"
                       value="<%= editar ? esc(u.getIdUsuario()) : "" %>"
                       <%= editar ? "readonly" : "" %>>
            </label>

            <label>
                Nombre completo
                <input type="text"
                       required
                       maxlength="120"
                       name="nombre"
                       value="<%= editar ? esc(u.getNombre()) : "" %>">
            </label>

            <label>
                Sexo
                <select name="sexo">
                    <%
                        for (String s : new String[]{
                            "Femenino", "Masculino", "Otro", "Prefiero no decir"
                        }) {
                    %>
                        <option <%= editar && s.equals(u.getSexo()) ? "selected" : "" %>>
                            <%= s %>
                        </option>
                    <%
                        }
                    %>
                </select>
            </label>

            <label>
                Edad
                <input type="number"
                       required
                       min="0"
                       max="120"
                       name="edad"
                       value="<%= editar ? u.getEdad() : "" %>">
            </label>

            <label>
                Teléfono
                <input type="text"
                       maxlength="30"
                       name="telefono"
                       value="<%= editar ? esc(u.getTelefono()) : "" %>">
            </label>

            <label>
                País
                <input type="text"
                       maxlength="80"
                       name="pais"
                       value="<%= editar ? esc(u.getPais()) : "Colombia" %>">
            </label>

            <label>
                Tipo de usuario
                <select name="tipoUsuario">

                    <option value="PACIENTE"
                        <%= editar && "PACIENTE".equals(u.getTipoUsuario())
                            ? "selected" : "" %>>
                        Paciente
                    </option>

                    <option value="PERSONAL_SALUD"
                        <%= editar && "PERSONAL_SALUD".equals(u.getTipoUsuario())
                            ? "selected" : "" %>>
                        Personal de salud
                    </option>

                </select>
            </label>

            <button class="boton" type="submit">
                <%= editar ? "Guardar cambios" : "Registrar usuario" %>
            </button>

        </form>
    </main>
</body>
</html>

<%!
    private String esc(String s) {
        return s == null ? "" : s
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
%>
