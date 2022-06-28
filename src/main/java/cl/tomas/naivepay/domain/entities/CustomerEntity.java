package cl.tomas.naivepay.domain.entities;

import cl.tomas.naivepay.infrastructure.models.Access;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerEntity implements Serializable {
    private Long cusId;
    private String cusName;
    private String cusAddress;
    private String cusEmail;
    private Date cusBirthdate;
    private String cusRut;
    private AccessEntity cusAccess;
}
