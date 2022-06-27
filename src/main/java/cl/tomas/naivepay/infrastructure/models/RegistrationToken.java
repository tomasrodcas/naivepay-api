package cl.tomas.naivepay.infrastructure.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RegistrationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tkId;
    private String tkToken;
    private LocalDateTime tkIssuedDate;
    private LocalDateTime tkExpDate;
    private LocalDateTime tkConfirmedDate;
    @ManyToOne
    private Access tkAccess;
}
