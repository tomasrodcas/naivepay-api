package cl.tomas.naivepay.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    @OneToMany(mappedBy = "accCustomer")
    private List<Account> accounts = new ArrayList<>();
    private String cusName;
    private String cusAddress;
    private String cusMail;
    private Date cusBirthDate;
    private char cusDv;
    private String cusRut;
    private String cusDevice;
}
