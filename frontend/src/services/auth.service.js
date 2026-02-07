import axios from 'axios';
axios.defaults.baseURL = "http://localhost:4445";


class AuthService {

  login(user) {
    return axios.post('/login', {
      email: user.email,
      password: user.password
    })
        .then(response => {
          const token = response.data.token;

          if (token) {
            const userData = {
              accessToken: token,
              username: user.email,
              roles: [this.parseRole(token)]
            };

            localStorage.setItem("user", JSON.stringify(userData));
            return userData;
          }
        });
  }


  logout() {
    localStorage.removeItem('user');
  }

  register(user, photoFile) {
    const formData = new FormData();

    const data = {
      email: user.email,
      password: user.password,
      firstname: user.firstname,
      lastname: user.lastname,
      phone: user.phone
    };

    formData.append(
        "data",
        new Blob([JSON.stringify(data)], { type: "application/json" })
    );

    if (photoFile) {
      formData.append("photo", photoFile);
    }

    return axios.post("/register", formData);

  }

  parseRole(token) {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return "ROLE_" + payload.role;
  }
}

export default new AuthService();
