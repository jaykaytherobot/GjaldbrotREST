package is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services.Implementations;

import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.ReceiptType;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.User;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Repositories.ReceiptTypeRepository;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Repositories.UserRepository;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImplementation implements UserService {
    UserRepository userRepository;
    ReceiptTypeRepository typeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, ReceiptTypeRepository typeRepository) {
        this.userRepository = userRepository;
        this.typeRepository = typeRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User signupUser(User user){
        ReceiptType[] defaultReceiptTypes = new ReceiptType[3];
        defaultReceiptTypes[0] = new ReceiptType(user, "Matur", -256);
        defaultReceiptTypes[1] = new ReceiptType(user, "Leiga", -16711936);
        defaultReceiptTypes[2] = new ReceiptType(user, "Annað", -16776961);
        List<ReceiptType> receiptTypes = user.getReceiptTypes();
        for (ReceiptType defaultReceiptType: defaultReceiptTypes) {
            user.addReceiptType(defaultReceiptType);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user);
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User login(User user) {
        User exists = findByUsername(user.getUsername());
        if(exists != null){
            if(passwordEncoder.matches(user.getPassword(), exists.getPassword())){
                System.out.println(user);
                return user;
            }
        }
        return null;
    }

    @Override
    public String login(String username, String password) {
        Optional<User> optUser = userRepository.findByUsername(username);
        if(optUser.isPresent()) {
            User user = optUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                System.out.println(user.getPassword());
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                userRepository.save(user);
                return token;
            }
        }
        return StringUtils.EMPTY;
    }


    @Override
    public Optional<org.springframework.security.core.userdetails.User> findByToken(String token) {
        Optional<User> optUser = userRepository.findByToken(token);
        if(optUser.isPresent()) {
            User user = optUser.get();
            org.springframework.security.core.userdetails.User secUser = new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(), true, true, true, true, AuthorityUtils.createAuthorityList("USER")
            );
            return Optional.of(secUser);
        }
        return Optional.empty();
    }
}