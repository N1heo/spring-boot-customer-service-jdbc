package kg.nurtelecom.internlabs.customerservice.payload.request.customer;

import jakarta.validation.constraints.*;

public class AdminCustomerUpdateRequest {

    private String name;

    @Email
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{9,15}$")
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
