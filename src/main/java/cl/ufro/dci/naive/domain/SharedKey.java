package cl.ufro.dci.naive.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

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
