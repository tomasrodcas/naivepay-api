package cl.tomas.naivepay.infrastructure.models;

import cl.tomas.naivepay.domain.entities.CustomerEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "customer")
public class Customer{

    @Id
    @Column(name = "cus_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cusId;
    @OneToOne()
    private Access cusAccess;
    @OneToMany(mappedBy = "shaCustomer")
    private List<SharedKey> cusSharedKeys = new ArrayList<>();
    @OneToMany(mappedBy = "accCustomer")
    private List<Account> accounts = new ArrayList<>();
    private String cusName;
    private String cusAddress;
    private String cusMail;
    private Date cusBirthDate;
    private char cusDv;
    private String cusRut;
    private String cusDevice;

    public CustomerEntity toEntity(){
        return new CustomerEntity(
                this.cusId,
                this.cusName,
                this.cusAddress,
                this.cusMail,
                this.cusBirthDate,
                this.cusDv,
                this.cusRut,
                this.cusDevice,
                this.cusAccess.toEntity()

                );
    }

    public CustomerEntity toEntityPublic(){
        return new CustomerEntity(
                this.cusId,
                this.cusName,
                null,
                this.cusMail,
                null,
                this.cusDv,
                this.cusRut,
                null,
                null

        );
    }
}
