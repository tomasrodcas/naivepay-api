package cl.tomas.naivepay.service.accesslog;

import cl.tomas.naivepay.exceptions.ApiRequestException;
import cl.tomas.naivepay.repository.AccessLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.tomas.naivepay.domain.AccessLog;
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
