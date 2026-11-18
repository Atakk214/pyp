package com.sistemapyp;
import com.sistemapyp.model.Usuario;
import com.sistemapyp.web.UsuarioWebDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UsuarioWebDAOTest {
 @Autowired UsuarioWebDAO dao;
 @Test void crudCompleto(){
  String id="PRUEBA-EV02";
  if(dao.buscar(id)!=null)dao.eliminar(id);
  try{
   dao.crear(new Usuario(id,"Prueba PyP","Otro",22,"3000000000","Colombia","PACIENTE"));
   assertNotNull(dao.buscar(id));
   Usuario u=dao.buscar(id);u.setNombre("Prueba actualizada");dao.actualizar(u);
   assertEquals("Prueba actualizada",dao.buscar(id).getNombre());
   assertTrue(dao.listar().stream().anyMatch(x->id.equals(x.getIdUsuario())));
  }finally{dao.eliminar(id);}
  assertNull(dao.buscar(id));
 }
}
