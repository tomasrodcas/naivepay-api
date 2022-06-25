package cl.ufro.dci.naive.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction_state")
public class TransactionState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trsId;
    private String trsName;

}
