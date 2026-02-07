import axios from "axios";
import authHeader from "./auth-header";

const API_URL = "/admin/customers";

class CustomerService {
  getAllWithFormat(format) {
    const headers = {
      ...authHeader(),
      Accept: format === "json"
          ? "application/json"
          : "application/xml"
    };

    const config = {headers};

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

  getProfile() {
    return axios.get("/customer/profile", { headers: authHeader() });
  }
}

export default new CustomerService();
