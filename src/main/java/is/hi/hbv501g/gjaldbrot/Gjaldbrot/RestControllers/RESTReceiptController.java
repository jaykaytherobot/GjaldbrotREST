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
    public List<Receipt> receiptsGet(HttpSession session) {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);
        return user.getReceipts();
    }

    @PostMapping(value = "/api/user/receipt", produces = "application/json")
    public String receiptPost(@RequestBody ReceiptHost receiptHost) {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);
        try {
            Receipt receipt = receiptHost.createReceipt();
            user.addReceipt(receipt);
            userService.save(user);
            return "Receipt is created";
        }
        catch (Exception e) {
            return "Could not create receipt";
        }
    }

    @GetMapping(value = "/api/user/receipt/:id",produces = "application/json")
    private String getReceiptGet(@PathVariable("id") long id){
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);
        Receipt receipt = user.getReceipt(id);
        if(receipt != null) {
            return receipt.toString();
        }
        return "User does not own a receipt with this id";
    }

    @PatchMapping(value = "/api/user/receipt/:id",produces = "application/json")
    private String changeReceiptPOST(@PathVariable("id") long id, @Valid ReceiptHost newReceipt){
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);
        Receipt receipt = user.getReceipt(id);
        if(receipt != null) {
            return newReceipt.toString();
        }
        return "User does not own a receipt with this id";
    }

    @DeleteMapping(value = "/api/user/receipt/:id", produces = "application/json")
    public String deleteReceipt(@PathVariable("id") long id) {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);
        Receipt receipt = user.getReceipt(id);
        if(receipt != null) {
            receiptService.delete(receipt);
            return "Receipt has been deleted";
        }
        return "User does not own a receipt with this id";
    }

    // @DeleteMapping(value = "/api/user/overview",produces = "application/json")

    // @DeleteMapping(value = "/api/user/comparison",produces = "application/json")

}
