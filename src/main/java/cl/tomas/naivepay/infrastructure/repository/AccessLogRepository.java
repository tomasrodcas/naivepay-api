package cl.tomas.naivepay.infrastructure.repository;

import cl.tomas.naivepay.infrastructure.models.Access;
import cl.tomas.naivepay.infrastructure.models.AccessLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessLogRepository extends CrudRepository<AccessLog,Long>{
    List<AccessLog> findAccessLogsByAloAccess(Access aloAccess);
}
