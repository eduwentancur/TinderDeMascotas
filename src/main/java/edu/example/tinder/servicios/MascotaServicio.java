package edu.example.tinder.servicios;

import edu.example.tinder.entidades.Foto;
import edu.example.tinder.enumeraciones.Sexo;
import edu.example.tinder.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import edu.example.tinder.entidades.Usuario;
import edu.example.tinder.errores.ErrorServicio;
import edu.example.tinder.entidades.Mascota;
import edu.example.tinder.repositorios.MascotaRepositorio;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MascotaServicio {
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private MascotaRepositorio mascotaRepositorio;
    
    @Autowired
    private FotoServicio fotoServicio;
    
    
    @Transactional
    public void agregarMascota(MultipartFile archivo,String idUsuario, String nombre, Sexo sexo) throws ErrorServicio{
        
        Usuario usuario = usuarioRepositorio.findById(idUsuario).get();
        
        validar(nombre,sexo);
        
        Mascota mascota = new Mascota();
        
        mascota.setNombre(nombre);
        mascota.setSexo(sexo);
        mascota.setBaja(new Date());
        
        Foto foto = fotoServicio.guardar(archivo);
        mascota.setFoto(foto);
        mascotaRepositorio.save(mascota);
    }
    
    @Transactional
    public void modificar(MultipartFile archivo,String idUsuario, String idMascota, String nombre, Sexo sexo)throws ErrorServicio{
        validar(nombre,sexo);
        Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota);
        if (respuesta.isPresent()) {
            Mascota mascota = respuesta.get();
            if (mascota.getUsuario().getId().equals(idUsuario)) {
                mascota.setNombre(nombre);
                mascota.setSexo(sexo);
                String idFoto = null;
                if(mascota.getFoto() != null){
                    idFoto = mascota.getFoto().getId();
                }
                
                Foto foto = fotoServicio.actualizar(idFoto, archivo);
                mascota.setFoto(foto);
                
                mascotaRepositorio.save(mascota);
            }else{
                throw new ErrorServicio("No tiene permiso suficiente para realizar la operacion");
            }
            
        }else{
            throw new ErrorServicio("No existe una mascota con el identificador solicitado");
        }
        
        
    }
    
    @Transactional
    public void eliminar(String idUsuario, String idMascota)throws ErrorServicio{
        Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota);
        if (respuesta.isPresent()){
            Mascota mascota = respuesta.get();
            if (mascota.getUsuario().getId().equals(idUsuario)) {
                mascota.setBaja(new Date());
                mascotaRepositorio.save(mascota);
            }else{
                throw new ErrorServicio("No tiene permiso suficiente para realizar la operacion");
            }
        }
    }
    
    
    public void validar(String nombre,Sexo sexo)throws ErrorServicio{
        if(nombre == null || nombre.isEmpty()){throw new ErrorServicio("El nombre no puede estar vacio");}
        if (sexo == null) {throw new ErrorServicio("El sexo de la mascota no puede ser nulo");}
    }
    
}
