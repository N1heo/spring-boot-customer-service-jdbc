import axios from "axios";
import authHeader from "./auth-header";

const API_URL = "/admin/customers";

class CustomerService {

  create(data) {
    return axios.post(API_URL, data, {headers: authHeader()});
  }

  update(idCustomer, data) {
    return axios.put(`${API_URL}/${idCustomer}`, data, {headers: authHeader()});
  }

  deleteById(idCustomer) {
    return axios.delete(`${API_URL}/${idCustomer}`, {headers: authHeader()});
  }

  getProfile() {
    return axios.get("/customer/profile", { headers: authHeader() });
  }

  getAll() {
    return axios.get("/admin/customers", { headers: authHeader() });
  }
}

export default new CustomerService();
