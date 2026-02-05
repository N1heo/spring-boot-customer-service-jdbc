package kg.nurtelecom.internlabs.customerservice.enums;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_CUSTOMER;


    @Override
    public @Nullable String getAuthority() {
        return name();
    }
}
