package by.giorgi.jobportalback.service;


import by.giorgi.jobportalback.model.entity.User;
import by.giorgi.jobportalback.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service("userService")
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;


    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

//    public User getUserByUsername(String username) throws UsernameNotFoundException{
//        return userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException(username));
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
