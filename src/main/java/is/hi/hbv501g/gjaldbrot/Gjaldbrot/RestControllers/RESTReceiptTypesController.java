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

    @PatchMapping(value = "api/user/types/{id}", produces = "application/json")
    public ReceiptType patchReceiptType(@PathVariable Long id, @RequestBody ReceiptType receiptType) {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);

        ReceiptType oldReceiptType = user.getReceiptType(id);
        if (oldReceiptType != null) {
            oldReceiptType.setName(receiptType.getName());
            userService.save(user);
            return oldReceiptType;
        }
        return null;
    }

    @DeleteMapping(value = "/api/user/types/{id}", produces = "application/json")
    public String deleteReceiptType(@PathVariable Long id) {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);

        ReceiptType receiptType = user.getReceiptType(id);
        if (receiptType != null) {
            user.deleteReceiptsOfType(id);
            user.deleteReceiptType(receiptType);
            userService.save(user);
            return "Receipt type deleted";
        }
        return "No receipt type to delete with this id";
    }
}
