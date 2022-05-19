/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.example.tinder.repositorios;

import edu.example.tinder.entidades.Voto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepositorio extends JpaRepository<Voto, String> {
    
    @Query("SELECT c FROM Voto c WHERE c.mascota1.id = :id ORDER BY c.fecha DESC")
    public List<Voto> buscarVotosPropios(String id);
    
    @Query("SELECT c FROM Voto c WHERE c.mascota2.id = :id ORDER BY c.fecha DESC")
    public List<Voto> buscarVotosRecibidos(String id);

}
