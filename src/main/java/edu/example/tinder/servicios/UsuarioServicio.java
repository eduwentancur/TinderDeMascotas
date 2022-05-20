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

import edu.example.tinder.entidades.Foto;
import edu.example.tinder.entidades.Usuario;
import edu.example.tinder.errores.ErrorServicio;
import edu.example.tinder.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService{
    
    @Autowired  //esta variable la inicializa
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private FotoServicio fotoServicio;
    
    
    
    public void registrar(MultipartFile archivo, String nombre, String apellido, String mail, String clave) throws ErrorServicio{
        
        validar(nombre,apellido,mail,clave);
        
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);
        usuario.setAlta(new Date());
        
        Foto foto = fotoServicio.guardar(archivo);
        usuario.setFoto(foto);
        
        usuarioRepositorio.save(usuario);
    }
    
    public void modificar(MultipartFile archivo,String id,String nombre, String apellido, String mail, String clave) throws ErrorServicio{
        
        validar(nombre,apellido,mail,clave);
        
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = usuarioRepositorio.findById(id).get();
            usuario.setApellido(apellido);
            usuario.setNombre(nombre);
            usuario.setMail(mail);
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);
            String idFoto = null;
            
            if (usuario.getFoto()!=null) {
                idFoto=usuario.getFoto().getId();
            }
            
            Foto foto = fotoServicio.actualizar(idFoto, archivo);
            usuario.setFoto(foto);
            
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

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorMail(mail);
        if(usuario != null){
            
            List<GrantedAuthority> permisos = new ArrayList<>();
            
            GrantedAuthority p1 = new SimpleGrantedAuthority("MODULO_FOTOS");
            permisos.add(p1);
            GrantedAuthority p2 = new SimpleGrantedAuthority("MODULO_MASCOTAS");
            permisos.add(p2);
            GrantedAuthority p3 = new SimpleGrantedAuthority("MODULO_VOTOS");
            permisos.add(p3);
            
            
            User user = new User(usuario.getMail(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }
    
}
