
package edu.example.tinder.entidades;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "votos")
public class Voto {
    @Id
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Temporal(TemporalType.TIMESTAMP)
    private Date respuesta;
    @ManyToOne
    private Mascota mascota1;
    @ManyToOne
    private Mascota mascota2;
    
}
