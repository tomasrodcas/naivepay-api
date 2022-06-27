package cl.tomas.naivepay.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NonNull
public class Deposit implements Serializable {
    private long accId;
    private int amount;
}
