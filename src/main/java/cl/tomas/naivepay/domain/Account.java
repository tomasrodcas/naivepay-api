package cl.tomas.naivepay.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accId;
    @JsonBackReference
    @ManyToOne
    private Customer accCustomer;
    @OneToMany(mappedBy = "traAccount")
    @JsonBackReference
    private List<Transaction> accTransactions = new ArrayList<>();
    private int accNum;
    private int accCvv;
    @Column(columnDefinition = "Integer default 0")
    private int accAmount;
}
