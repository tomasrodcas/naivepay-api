package cl.tomas.naivepay.domain.entities;

import cl.tomas.naivepay.infrastructure.models.Access;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessLogEntity implements Serializable {

    private Long aloId;
    private Access aloAccess;
    private LocalDateTime aloDate;
    private String aloDescription;
}
