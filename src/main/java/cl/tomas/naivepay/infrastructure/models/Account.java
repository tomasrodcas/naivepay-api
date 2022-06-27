package cl.tomas.naivepay.infrastructure.models;

import cl.tomas.naivepay.domain.entities.AccountEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accId;
    @ManyToOne
    private Customer accCustomer;
    @OneToMany(mappedBy = "traAccount")
    private List<Transaction> accTransactions = new ArrayList<>();
    private long accNum;
    private int accCvv;
    @Column(columnDefinition = "Integer default 0")
    private int accAmount;

    public AccountEntity toEntity(){
        Customer customer = this.accCustomer;
        customer.setCusAccess(null);
        return new AccountEntity(
                this.accId,
                customer.toEntityPublic(),
                this.accTransactions.stream().map(Transaction::toEntity).collect(Collectors.toList()),
                this.accNum,
                this.accCvv,
                this.accAmount
        );
    }

}
