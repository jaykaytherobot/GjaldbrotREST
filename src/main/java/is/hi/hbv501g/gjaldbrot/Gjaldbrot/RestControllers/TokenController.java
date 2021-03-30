package is.hi.hbv501g.gjaldbrot.Gjaldbrot.RestControllers;

import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.User;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class TokenController {

    @Autowired
    private UserService userService;

    @ExceptionHandler({ Exception.class })
    @ResponseStatus(code=HttpStatus.BAD_REQUEST)
    public String handleException(Exception e) {
        return e.getMessage();
    }

    @PostMapping("/api/signup")
    public String createUser(@RequestParam("username") final String username,
                             @RequestParam("password") final String password) throws Exception {
        User newUser = new User(username, password);
        System.out.println(newUser);
        try {
            userService.signupUser(newUser);
            return "user created";
        }
        catch (Exception e) {
            System.out.println(e);
            throw new Exception("Username already exists");
        }
    }

    @PostMapping("/api/login")
    public String getToken(@RequestParam("username") final String username,
                           @RequestParam("password") final String password,
                           HttpServletResponse response) throws Exception{
        String token= userService.login(username,password);

        if(StringUtils.isEmpty(token)){
            throw new Exception("Could not login, check username and password");
        }
        return "{"+
                "\"token_type\":\"Bearer\","+
                "\"access_token\":\""+token+"\","+
                "\"expires_in\":3600,"+
                "\"expires_on\":1479937454,"+
                "\"refresh_token\":\"0/LTo....\""+
                "}";
    }
}
