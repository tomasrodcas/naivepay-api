package cl.tomas.naivepay.infrastructure.models;

import cl.tomas.naivepay.domain.entities.AccessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "access")
public class Access implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accId;
    @OneToMany(mappedBy = "aloAccess")
    private List<AccessLog> accAccessLog;
    private String accName;
    private String accPassword;
    private int accRole;

    private boolean blocked;
    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(Integer.toString(this.accRole)));
    }
    @Override
    public String getPassword() {
        return this.accPassword;
    }
    @Override
    public String getUsername() {
        return this.accName;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return !this.blocked;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public AccessEntity toEntity(){
        return new AccessEntity(
                this.accId,
                this.accName,
                null,
                this.accRole,
                this.blocked,
                this.enabled
        );
    }

}