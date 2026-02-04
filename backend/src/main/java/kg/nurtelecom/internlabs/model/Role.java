package kg.nurtelecom.internlabs.model;

import kg.nurtelecom.internlabs.enums.ERole;

public class Role {
    private Long id;
    private ERole roleName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ERole getRoleName() {
        return roleName;
    }

    public void setRoleName(ERole roleName) {
        this.roleName = roleName;
    }
}
