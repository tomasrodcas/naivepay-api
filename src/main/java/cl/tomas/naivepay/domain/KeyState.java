package cl.tomas.naivepay.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class KeyState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kstId;
    private String kstState;
}
