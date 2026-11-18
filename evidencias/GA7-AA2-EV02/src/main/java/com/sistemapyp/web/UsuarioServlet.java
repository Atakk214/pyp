package com.sistemapyp.web;
import com.sistemapyp.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
@Configuration
public class UsuarioServlet {
 @Bean public ServletRegistrationBean<HttpServlet> usuariosServlet(UsuarioWebDAO dao){
  HttpServlet servlet=new HttpServlet(){
   @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
    String accion=req.getParameter("accion");
    if("nuevo".equals(accion)||"editar".equals(accion)){
     if("editar".equals(accion)){req.setAttribute("usuario",dao.buscar(req.getParameter("id")));}
     req.getRequestDispatcher("/WEB-INF/jsp/formulario.jsp").forward(req,resp);return;
    }
    req.setAttribute("usuarios",dao.listar());req.getRequestDispatcher("/WEB-INF/jsp/lista.jsp").forward(req,resp);
   }
   @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
    req.setCharacterEncoding("UTF-8");String accion=req.getParameter("accion");
    try{
     if("eliminar".equals(accion)){dao.eliminar(req.getParameter("id"));}
     else{
      String id=req.getParameter("id");String nombre=req.getParameter("nombre");
      if(id==null||id.isBlank()||nombre==null||nombre.isBlank())throw new IllegalArgumentException("Documento y nombre son obligatorios");
      int edad=Integer.parseInt(req.getParameter("edad"));if(edad<0||edad>120)throw new IllegalArgumentException("Edad fuera de rango");
      Usuario u=new Usuario(id.trim(),nombre.trim(),req.getParameter("sexo"),edad,req.getParameter("telefono"),req.getParameter("pais"),req.getParameter("tipoUsuario"));
      if("actualizar".equals(accion))dao.actualizar(u);else dao.crear(u);
     }
     resp.sendRedirect(req.getContextPath()+"/usuarios");
    }catch(Exception ex){req.setAttribute("error","No fue posible guardar: "+ex.getMessage());req.setAttribute("usuarios",dao.listar());req.getRequestDispatcher("/WEB-INF/jsp/lista.jsp").forward(req,resp);}
   }
  };
  return new ServletRegistrationBean<>(servlet,"/usuarios");
 }
}
