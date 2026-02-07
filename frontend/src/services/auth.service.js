import axios from 'axios';
axios.defaults.baseURL = "http://localhost:4445";


class AuthService {
  login(user) {
    return axios
      .post('/login', {
        email: user.email,
        password: user.password
      })
      .then(response => {
        if (response.data.accessToken) {
          localStorage.setItem('user', JSON.stringify(response.data));
        }

        return response.data;
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
}

export default new AuthService();
