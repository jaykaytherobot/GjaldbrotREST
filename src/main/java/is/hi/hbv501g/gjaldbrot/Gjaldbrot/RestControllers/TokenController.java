package is.hi.hbv501g.gjaldbrot.Gjaldbrot.RestControllers;

import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class TokenController {

    @Autowired
    private UserService userService;

    @PostMapping("/token")
    public String getToken(@RequestParam("username") final String username, @RequestParam("password") final String password, HttpServletResponse response){
        String token= userService.login(username,password);

        if(StringUtils.isEmpty(token)){
            return "no token found";
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
