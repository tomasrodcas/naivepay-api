package cl.tomas.naivepay.controller.access;

import cl.tomas.naivepay.domain.entities.AccessEntity;
import cl.tomas.naivepay.infrastructure.models.Access;
import cl.tomas.naivepay.service.access.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/access")
public class AccessController {

    @Autowired
    AccessService service;

    @PutMapping("/update-password")
    public ResponseEntity<AccessEntity> update(@RequestBody long accId, @RequestBody String oldPassword, @RequestBody String newPassword){
        return ResponseEntity.status(200).body(service.updatePassword(accId, oldPassword, newPassword));
    }

    @PutMapping("/block/{accId}")
    public ResponseEntity<AccessEntity> blockAccess(@PathVariable long accId){
        return ResponseEntity.status(200).body(service.block(accId));
    }

    @PutMapping("/unblock/{accId}")
    public ResponseEntity<AccessEntity> unblockAccess(@PathVariable long accId){
        return ResponseEntity.status(200).body(service.unblock(accId));
    }

    @GetMapping("/get/{accId}")
    public ResponseEntity<AccessEntity> getAccess(@PathVariable long accId){
        return ResponseEntity.status(200).body(service.getWithoutPassword(accId));
    }
}
