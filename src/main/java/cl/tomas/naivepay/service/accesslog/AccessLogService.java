package cl.tomas.naivepay.service.accesslog;

import cl.tomas.naivepay.infrastructure.models.Access;
import cl.tomas.naivepay.domain.exceptions.ApiRequestException;
import cl.tomas.naivepay.infrastructure.repository.AccessLogRepository;
import cl.tomas.naivepay.infrastructure.repository.AccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.tomas.naivepay.infrastructure.models.AccessLog;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class AccessLogService {

    @Autowired
    AccessLogRepository repository;

    @Autowired
    AccessRepository accessRepository;

    public AccessLog create(AccessLog accessLog) {
        log.info("Creating Access Log");
        try {
            return repository.save(accessLog);
        } catch (Exception e) {
            log.error("Error Creating AccessLog - ", e.getMessage());
            throw new ApiRequestException("Error Creating AccessLog");
        }
    }

    public List<AccessLog> getAll(){
        log.info("Fetching access logs");
        try{
            return (List<AccessLog>) repository.findAll();
        }catch(Exception e){
            log.error("Error Fetching Access Logs | {}", e.getMessage());
            throw new ApiRequestException("Error Fetching Access Logs");
        }
    }

    public List<AccessLog> getByAccess(long accId){
        log.info("Fetching access logs");
        try{
            Access acc = accessRepository.findById(accId).orElseThrow();
            return (List<AccessLog>) repository.findAccessLogsByAloAccess(acc);
        }catch(Exception e){
            log.error("Error Fetching Access Logs | {}", e.getMessage());
            throw new ApiRequestException("Error Fetching Access Logs");
        }
    }

}
