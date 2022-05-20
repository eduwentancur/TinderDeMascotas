/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.example.tinder.repositorios;

import edu.example.tinder.entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Edu
 */
public interface FotoRepositorio extends JpaRepository<Foto, String>{
    
}
