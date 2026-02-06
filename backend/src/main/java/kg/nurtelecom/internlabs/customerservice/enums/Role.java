package kg.nurtelecom.internlabs.customerservice.enums;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    USER;


    @Override
    public @Nullable String getAuthority() {
        return name();
    }
}
