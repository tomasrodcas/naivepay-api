package cl.ufro.dci.naive.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long traId;
    @JsonManagedReference
    @ManyToOne
    private Account traAccount;
    @ManyToOne
    private TransactionState traTransactionState;
    private String traOriginAccount;
    private String traDestinationAccount;
    private Date traDate;
    private int traAmount;
    private String traToken;
    private String traDescription;
}
