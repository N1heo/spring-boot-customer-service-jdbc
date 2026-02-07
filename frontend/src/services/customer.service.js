import axios from "axios";
import authHeader from "./auth-header";

const API_URL = "/customers";

class CustomerService {
  getAllWithFormat(format) {
    const headers = {
      ...authHeader(),
      Accept: format === "xml" ? "application/xml" : "application/json",
    };

    const config = {headers};

    if (format === "xml") {
      config.responseType = "text";
    }

    return axios(API_URL, config);
  }

  create(data) {
    return axios.post(API_URL, data, {headers: authHeader()});
  }

  update(idCustomer, data) {
    return axios.put(`${API_URL}/${idCustomer}`, data, {headers: authHeader()});
  }

  deleteById(idCustomer) {
    return axios.delete(`${API_URL}/${idCustomer}`, {headers: authHeader()});
  }


  getCookie(name) {
    const parts = document.cookie.split(";").map((c) => c.trim());
    for (const part of parts) {
      if (part.startsWith(name + "=")) {
        return decodeURIComponent(part.substring(name.length + 1));
      }
    }
    return "";
  }
}

export default new CustomerService();
