package cl.tomas.naivepay.infrastructure.models;

import cl.tomas.naivepay.infrastructure.models.Access;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aloId;
    @JsonBackReference
    @ManyToOne()
    private Access aloAccess;
    private LocalDateTime aloDate;
    private String aloDescription;

}