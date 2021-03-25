package is.hi.hbv501g.gjaldbrot.Gjaldbrot.RestControllers;

import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.ReceiptType;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.User;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services.ReceiptTypeService;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RESTReceiptTypesController {
    @Autowired
    ReceiptTypeService receiptTypeService;
    @Autowired
    UserService userService;

    @GetMapping(value = "/api/user/types",produces = "application/json")
    public List<ReceiptType> getReceiptTypes() {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);
        return user.getReceiptTypes();
    }

    @PostMapping(value = "api/user/types", produces = "application/json")
    public ReceiptType postReceiptType(@RequestBody ReceiptType receiptType) {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);

        user.addReceiptType(receiptType);
        userService.save(user);
        return receiptType;
    }
}
