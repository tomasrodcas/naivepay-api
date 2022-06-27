package cl.tomas.naivepay.domain.entities;

import cl.tomas.naivepay.infrastructure.models.AccessLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessEntity implements Serializable {
    private Long accId;
    private String accName;
    private String accPassword;
    private int accRole;
    private boolean blocked;
    private boolean enabled;
}
