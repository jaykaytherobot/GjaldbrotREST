package is.hi.hbv501g.gjaldbrot.Gjaldbrot.RestControllers;

import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.*;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services.ReceiptService;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class RESTReceiptController {
    @Autowired
    private UserService userService;

    private ReceiptService receiptService;

    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String onError(Exception e) {
        return e.getMessage();
    }

    @GetMapping(value = "/api/user/receipt", produces = "application/json")
    public List<ReceiptHost> receiptsGet(HttpSession session) {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);
        List<Receipt> receiptList = user.getReceipts();
        List<ReceiptType> receiptTypeList = user.getReceiptTypes();
        return ReceiptHost.receiptListToHostList(receiptList, receiptTypeList);
    }

    @PostMapping(value = "/api/user/receipt", produces = "application/json")
    public String receiptPost(@RequestBody ReceiptHost receiptHost)
        throws Exception{
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
            throw new Exception("Could not create receipt");
        }
    }

    @GetMapping(value = "/api/user/receipt/:id",produces = "application/json")
    private String getReceiptGet(@PathVariable("id") long id)
        throws Exception{
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);
        Receipt receipt = user.getReceipt(id);
        if(receipt != null) {
            return receipt.toString();
        }
        else {
            throw new Exception("User does not own a receipt with this id");
        }
    }

    @PatchMapping(value = "/api/user/receipt/{id}",produces = "application/json")
    private String changeReceiptPOST(@PathVariable("id") long id, @RequestBody ReceiptHost newReceipt)
        throws Exception{
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);
        Receipt receipt = user.getReceipt(id);
        if(receipt != null) {
            receipt.modify(newReceipt);
            userService.save(user);
            return newReceipt.toString();
        }
        else {
            throw new Exception("User does not own a receipt with this id");
        }
    }

    @DeleteMapping(value = "/api/user/receipt/:id", produces = "application/json")
    public String deleteReceipt(@PathVariable("id") long id)
        throws Exception{
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);

        Receipt receipt = user.getReceipt(id);
        if (receipt != null) {
            user.deleteReceipt(receipt);
            userService.save(user);
            return "Receipt deleted";
        }
        else {
            throw new Exception("User does not own a receipt with this id");
        }
    }

    @GetMapping(value = "/api/user/overview", produces = "application/json")
    public String overview(){
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);

        if (user != null) {
            return JSONWriter.writeReceipts(user, user.getReceipts());
        }
        return "User does not own a receipt with this id";
    }

    @GetMapping(value = "/api/user/comparison", produces = "application/json")
    public String comparison() {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userService.findByUsername(username);

        if (user != null) {
            return JSONWriter.writeComparisonData(user, user.getReceipts());
        }
        return "User does not own a receipt with this id";
    }
}
