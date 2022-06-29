package cl.tomas.naivepay.domain.entities;

import cl.tomas.naivepay.infrastructure.models.Customer;
import cl.tomas.naivepay.infrastructure.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity implements Serializable {
    private Long accId;
    private CustomerEntity accCustomer;
    private List<TransactionEntity> accTransactions;
    private long accNum;
    private Integer accCvv;
    private int accAmount;
}
