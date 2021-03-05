package is.hi.hbv501g.gjaldbrot.Gjaldbrot.Controllers;

import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.User;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UserController {


    private final UserService userService;

    /**
     * UserController
     * @param userService connection to the userService class and implementation
     */
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * loginGET(User user)
     * @param user (not used)
     * @return view of loginPage
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String loginGET(User user){
        return "login";
    }

    /**
     * signUpGET(User user)
     * @param user (not used)
     * @return view of signupPage
     */
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signUpGET(User user){
        return "signup";
    }

    /**
     * signUpPOST(@Valid User user, BindingResult result, Model model).
     * @param user gets the user entity.
     * @param result gets the BindingResult to find out if there are error with the signup.
     * @param model get the model for the signup (not used).
     * @return if there is error view signupPage or if not view loginPage
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signUpPOST(@Valid User user, BindingResult result, Model model){
        if(result.hasErrors()){
            return "signup";
        }
        User exists = userService.findByUsername(user.getUsername());
        if(exists == null){
            userService.signupUser(user);
        }
        return "login";
    }

    /**
     * usersGET(Model model)
     * @param model gets the model for the user to addAttributes.
     * @return view of usersPage
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String usersGET(Model model){
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    /**
     * loginPOST(@Valid User user, BindingResult result, Model model, HttpSession session).
     * @param user gets the user entity for the login.
     * @param result gets the BindingResult to find out if there are error with the login.
     * @param model (not used).
     * @param session binds the current user to the session.
     * @return mainPage if the user exits if not redirect to the frontPage.
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginPOST(@Valid User user, BindingResult result, Model model, HttpSession session){
        if(result.hasErrors()){
            return "login";
        }
        User exists = userService.login(user);
        if(exists != null){
            session.setAttribute("LoggedInUser", user);
            userService.findByUsername(user.getUsername());
            System.out.println(userService.findByUsername(user.getUsername()));
            return "redirect:/mainPage";
        }
        return "redirect:/";
    }

    /**
     * mainPageGET(HttpSession session, Model model)
     * @param session current session for binding values.
     * @param model current model.
     * @return is there is no user redirect to frontPage view or if user sessionUser then view for mainPage
     */
    @RequestMapping(value = "/mainPage", method = RequestMethod.GET)
    public String mainPageGET(HttpSession session, Model model){
        User sessionUser = (User) session.getAttribute("LoggedInUser");
        if(sessionUser != null){
            model.addAttribute("LoggedInUser", sessionUser);
            return "mainPage";
        }
        return "redirect:/";
    }

    /**
     * logoutGET(HttpSession session, Model model)
     * @param session current session.
     * @param model current model.
     * @return gets view of loginPage if user is not null or if not redirects to frontPage view.
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutGET(HttpSession session, Model model){
        User sessionUser = (User) session.getAttribute("LoggedInUser");
        if(sessionUser != null){
            session.removeAttribute("LoggedInUser");
            return "redirect:/";
        }
        return "redirect:/";
    }
}
