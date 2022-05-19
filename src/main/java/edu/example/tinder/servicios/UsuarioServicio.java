/*
 Los servicios van a ejecutar las funcionalidades para que 
 la aplicacion cumpla las peticiones del usuario
 Acciones
 1-Permitir que un usuario se registre
 2-Permitir que un usuario registrado ingrese y edite su cuenta
 3-Permitir que un usuario registrado ingrese y desabilite su cuenta
 4- Permitir que un usuario pueda habilitar la cuenta

 Dentro de esta clase escribimos los metodos que nos permitar llevar adelante estas reglas de negocio
 */
package edu.example.tinder.servicios;

import edu.example.tinder.entidades.Usuario;
import edu.example.tinder.errores.ErrorServicio;
import edu.example.tinder.repositorios.UsuarioRepositorio;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {
    
    @Autowired  //esta variable la inicializa
    private UsuarioRepositorio usuarioRepositorio;
    
    
    public void regitrar(String nombre, String apellido, String mail, String clave) throws ErrorServicio{
        
        validar(nombre,apellido,mail,clave);
        
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        usuario.setClave(clave);
        usuario.setAlta(new Date());
        usuarioRepositorio.save(usuario);
    }
    
    public void modificar(String id,String nombre, String apellido, String mail, String clave) throws ErrorServicio{
        
        validar(nombre,apellido,mail,clave);
        
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = usuarioRepositorio.findById(id).get();
            usuario.setApellido(apellido);
            usuario.setNombre(nombre);
            usuario.setMail(mail);
            usuarioRepositorio.save(usuario);
        }else{
            throw new ErrorServicio("No se encontro el usuario solicitado");
        }
    }
    
    
    public void deshabilitar(String id) throws ErrorServicio{
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = usuarioRepositorio.findById(id).get();
            usuario.setBaja(new Date());
            usuarioRepositorio.save(usuario);
        }else{
            throw new ErrorServicio("No se encontro el usuario no puede ser nulo");
        }
    
    }
    
    public void habilitar(String id) throws ErrorServicio{
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = usuarioRepositorio.findById(id).get();
            usuario.setBaja(null);
            usuarioRepositorio.save(usuario);
        }else{
            throw new ErrorServicio("No se encontro el usuario no puede ser nulo");
        }
    
    }
    
    
    
    public void validar(String nombre, String apellido, String mail, String clave) throws ErrorServicio{
    
        if(nombre == null || nombre.isEmpty()){throw new ErrorServicio("El nombre del usuario no debe estar vacio o nulo");}
        
        if(apellido == null || apellido.isEmpty()){throw new ErrorServicio("El apelldio del usuario no debe estar vacio o nulo");}
        
        if(mail == null || mail.isEmpty()){throw new ErrorServicio("El mail del usuario no debe estar vacio o nulo");}
        
        if(clave == null || clave.isEmpty() || clave.length() < 6){throw new ErrorServicio("La clave no puede ser nulo y debe tener mas de 6 digitos");}
        
    }
    
    
    
}
