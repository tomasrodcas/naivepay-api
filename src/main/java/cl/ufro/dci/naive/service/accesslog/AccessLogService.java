package cl.ufro.dci.naive.service.accesslog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.ufro.dci.naive.domain.AccessLog;
import cl.ufro.dci.naive.exceptions.ApiRequestException;
import cl.ufro.dci.naive.repository.AccessLogRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccessLogService {

    @Autowired
    AccessLogRepository repository;

    public AccessLog create(AccessLog accessLog) {
        log.info("Creating Access Log");
        try {
            return repository.save(accessLog);
        } catch (Exception e) {
            log.error("Error Creating AccessLog - ", e.getCause().getMessage());
            throw new ApiRequestException("Error Creating AccessLog");
        }
    }

}
