package kg.nurtelecom.internlabs.customerservice.service;

import kg.nurtelecom.internlabs.customerservice.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerProfileService implements UserDetailsService {


    private static final Logger log = LoggerFactory.getLogger(CustomerProfileService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // пока тестовый, реальная реализация после подключение jdbc
        return new User("bolo", "$2a$12$FCtID4P6GY2uEL86P12KQuYtlstORhynA6wjCWGkdVFXqXKKpgaUC");
    }
}
