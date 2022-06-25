package cl.tomas.naivepay.repository;

import cl.tomas.naivepay.domain.Access;
import cl.tomas.naivepay.domain.AccessLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessLogRepository extends CrudRepository<AccessLog,Long>{
    List<AccessLog> findAccessLogsByAloAccess(Access aloAccess);
}
