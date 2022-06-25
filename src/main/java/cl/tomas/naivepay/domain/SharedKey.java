package cl.tomas.naivepay.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SharedKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shaId;
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer shaCustomer;
    @ManyToOne(fetch = FetchType.EAGER)
    private KeyState shaKeyState;
    private byte[] shaKey;
}
