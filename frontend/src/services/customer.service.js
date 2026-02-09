import axios from "axios";
import authHeader from "./auth-header";

const API_URL = "/admin/customers";

class CustomerService {

  create(data) {
    return axios.post("/admin/customers", data);
  }

  update(idCustomer, data) {
    return axios.put(`${API_URL}/${idCustomer}`, data);
  }

  deleteById(idCustomer) {
    return axios.delete(`${API_URL}/${idCustomer}`);
  }

  getProfile() {
    return axios.get("/customer/profile",);
  }

  getAll() {
    return axios.get("/admin/customers");
  }
  updateJson(idCustomer, data) {
    return axios.put(`${API_URL}/${idCustomer}`, data, {
      headers: {
        ...authHeader(),
        'Content-Type': 'application/json'
      }
    });
  }
}

export default new CustomerService();
