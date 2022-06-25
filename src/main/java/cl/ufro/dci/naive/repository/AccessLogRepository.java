package cl.ufro.dci.naive.repository;

import cl.ufro.dci.naive.domain.Access;
import cl.ufro.dci.naive.domain.AccessLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessLogRepository extends CrudRepository<AccessLog,Long>{
    List<AccessLog> findAccessLogsByAloAccess(Access aloAccess);
}
