package com.sistemapyp.web;
import com.sistemapyp.model.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public class UsuarioWebDAO {
 private final JdbcTemplate jdbc;
 public UsuarioWebDAO(JdbcTemplate jdbc){this.jdbc=jdbc;}
 private final org.springframework.jdbc.core.RowMapper<Usuario> mapper=(rs,n)->new Usuario(rs.getString("id_usuario"),rs.getString("nombre"),rs.getString("sexo"),rs.getInt("edad"),rs.getString("telefono"),rs.getString("pais"),rs.getString("tipo_usuario"));
 public List<Usuario> listar(){return jdbc.query("SELECT * FROM usuarios ORDER BY nombre",mapper);}
 public Usuario buscar(String id){List<Usuario> lista=jdbc.query("SELECT * FROM usuarios WHERE id_usuario=?",mapper,id);return lista.isEmpty()?null:lista.get(0);}
 public void crear(Usuario u){jdbc.update("INSERT INTO usuarios(id_usuario,nombre,sexo,edad,telefono,pais,tipo_usuario) VALUES(?,?,?,?,?,?,?)",u.getIdUsuario(),u.getNombre(),u.getSexo(),u.getEdad(),u.getTelefono(),u.getPais(),u.getTipoUsuario());}
 public void actualizar(Usuario u){jdbc.update("UPDATE usuarios SET nombre=?,sexo=?,edad=?,telefono=?,pais=?,tipo_usuario=? WHERE id_usuario=?",u.getNombre(),u.getSexo(),u.getEdad(),u.getTelefono(),u.getPais(),u.getTipoUsuario(),u.getIdUsuario());}
 public void eliminar(String id){jdbc.update("DELETE FROM usuarios WHERE id_usuario=?",id);}
}
