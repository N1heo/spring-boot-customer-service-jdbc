package kg.nurtelecom.internlabs.service;

import kg.nurtelecom.internlabs.model.UserPrinciple;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return new UserPrinciple("bolo", "$2a$12$FCtID4P6GY2uEL86P12KQuYtlstORhynA6wjCWGkdVFXqXKKpgaUC");
    }
}
