package is.hi.hbv501g.gjaldbrot.Gjaldbrot.RestControllers;

import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.JSONWriter;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.Receipt;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.ReceiptHost;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.User;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTReceiptController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/api/user/getReceipts",produces = "application/json")
    public User getUserDetail(){
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);
        return user;
    }

    @PostMapping(value = "/api/user/newReceipt", produces = "application/json")
    public Receipt addReceipt(@RequestBody ReceiptHost newReceipt) {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);
        try {
            Receipt receipt = newReceipt.createReceipt();
            user.addReceipt(receipt);
            userService.save(user);
            return receipt;
        }
        catch (Exception e) {
            return null;
        }
    }

}
