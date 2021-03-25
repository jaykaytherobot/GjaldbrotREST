package is.hi.hbv501g.gjaldbrot.Gjaldbrot.RestControllers;

import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.JSONWriter;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.Receipt;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.ReceiptHost;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.User;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services.ReceiptService;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
public class RESTReceiptController {
    @Autowired
    private UserService userService;

    private ReceiptService receiptService;

    @GetMapping(value = "/api/user/receipt",produces = "application/json")
    public String receiptsGet(HttpSession session) {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);
        return user.getReceipts().toString();
    }

    // @GetMapping(value = "/api/user/receipt/:id",produces = "application/json")

    @PatchMapping(value = "/api/user/receipt/:id",produces = "application/json")
    private String changeReceiptPOST(@PathVariable("id") long id, @Valid ReceiptHost newReceipt){
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);
        Receipt oldReceipt = (Receipt) session.getAttribute("changedReceipt");
        receiptService.change(oldReceipt, newReceipt);
        return "Receipt have been changed";
    }

    @DeleteMapping(value = "/api/user/receipt/:id", produces = "application/json")
    public String deleteReceipt(@PathVariable("id") long id) {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);
        Receipt receipt = receiptService.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid Receipt ID"));
        if(user.id = receipt.user_id) {
            receiptService.delete(receipt);
            return "Receipt has been deleted";
        }
        return "You are not the right user!";
    }

    // @GetMapping(value = "/api/user/types",produces = "application/json")

    // @PostMapping(value = "/api/user/types",produces = "application/json")

    // @PatchMapping(value = "/api/user/types/:id",produces = "application/json")

    // @DeleteMapping(value = "/api/user/types/:id",produces = "application/json")

    // @DeleteMapping(value = "/api/user/overview",produces = "application/json")

    // @DeleteMapping(value = "/api/user/comparison",produces = "application/json")

}
