package cl.tomas.naivepay.domain.entities;


import cl.tomas.naivepay.infrastructure.models.Account;
import cl.tomas.naivepay.infrastructure.models.TransactionState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEntity implements Serializable {
    long traId;
    private long traOriginAccountNum;
    private long traDestinationAccountNum;
    private Date traDate;
    private int traAmount;
    private String traDescription;
    private String traState;
    private String traOriginCustomer;
    private String traDestinationCustomer;

}
