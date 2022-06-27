package cl.tomas.naivepay.infrastructure.models;

import cl.tomas.naivepay.domain.entities.TransactionEntity;
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
    @ManyToOne
    private Account traAccount;
    @ManyToOne
    private TransactionState traTransactionState;
    @ManyToOne
    private Account traDestinationAccount;
    private Date traDate;
    private int traAmount;
    private String traDescription;

    public TransactionEntity toEntity(){
        return new TransactionEntity(
                this.traId,
                this.traAccount.getAccNum(),
                this.traDestinationAccount.getAccNum(),
                this.traDate,
                this.traAmount,
                this.traDescription,
                this.traTransactionState.getTrsName(),
                this.traAccount.getAccCustomer().getCusName(),
                this.traDestinationAccount.getAccCustomer().getCusName()
        );
    }
}
