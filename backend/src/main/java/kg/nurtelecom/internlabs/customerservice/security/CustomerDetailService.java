package kg.nurtelecom.internlabs.customerservice.security;

import kg.nurtelecom.internlabs.customerservice.enums.Role;
import kg.nurtelecom.internlabs.customerservice.model.Customer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailService implements UserDetailsService {




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = new Customer();
        if (customer == null) {
            throw new UsernameNotFoundException("User with username " + username + " not found");
        }
        // пока тестовый, реальная реализация после подключение jdbc
        return new UserPrinciple(customer.getUsername(), customer.getPassword(), customer.getRole());
    }
}
