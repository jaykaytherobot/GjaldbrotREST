package is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services;

import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.User;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);
    User signupUser(User user);
    void delete(User user);
    List<User> findAll();
    User findByUsername(String username);
    User login(User user);
    String login(String username, String password);
    Optional<org.springframework.security.core.userdetails.User> findByToken(String token);
}