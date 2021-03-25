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

    private ReceiptService receiptService;

    @GetMapping(value = "/api/user/receipt",produces = "application/json")
    public String receiptsGet(HttpSession session) {
        User user = userService.findByUsername(sessionUser.getUsername());
        List<Receipt> receipts = receiptService.getReceiptByUser(user);
        return receipts;
    }

    //@GetMapping(value = "/api/user/receipt/:id",produces = "application/json")

    @PatchMapping(value = "/api/user/receipt/:id",produces = "application/json")
    private String changeReceiptPOST(@Valid ReceiptHost newReceipt, BindingResult result, Model model, HttpSession session){
        Receipt oldReceipt = (Receipt) session.getAttribute("changedReceipt");
        Receipt changedReceipt = receiptService.change(oldReceipt, newReceipt);
        return changedReceipt;
    }

    @DeleteMapping(value = "/api/user/receipt/:id", produces = "application/json")
    public String deleteReceipt(@PathVariable("id") long id) {
        Receipt receipt = receiptService.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid Receipt ID"));
        receiptService.delete(receipt);
        return "Receipt has been deleted";
    }


    @GetMapping(value = "/api/user/types",produces = "application/json")

    @PostMapping(value = "/api/user/types",produces = "application/json")

    @PatchMapping(value = "/api/user/types/:id",produces = "application/json")

    @DeleteMapping(value = "/api/user/types/:id",produces = "application/json")

    @DeleteMapping(value = "/api/user/overview",produces = "application/json")

    @DeleteMapping(value = "/api/user/comparison",produces = "application/json")

}
