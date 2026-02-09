<template>
  <div class="container-custom">
    <div class="header-section">
      <div>
        <h2 class="page-title">Customers</h2>
        <p class="page-subtitle">Manage your customer database</p>
      </div>
      <button class="btn-create" @click="openCreate">

      <span class="btn-icon">+</span>
        Add Customer
      </button>
    </div>

    <div v-if="loading" class="status-message loading">
      <div class="spinner"></div>
      Loading data...
    </div>
    <div v-if="error" class="status-message error">{{ error }}</div>

    <div class="customers-grid">
      <div class="customer-card" v-for="c in customers" :key="c.idCustomer">
        <div class="card-header-custom">
          <div class="avatar">
            <img
                v-if="c.imagePath && !brokenImages[c.idCustomer]"
                :src="avatarUrl(c.imagePath)"
                class="avatar-img"
                @error="onImgError(c.idCustomer)"
            />

            <span v-else>
  {{ getInitials(c.firstName, c.lastName) }}
</span>
          </div>
          <div class="customer-name">
            <h4>{{ c.firstName }} {{ c.lastName }}</h4>
          </div>
        </div>

        <div class="card-content">
          <div class="info-row">
            <span class="info-label-inline">Phone:</span>
            <span class="info-text">{{ c.phone }}</span>
          </div>
          <div class="info-row">
            <span class="info-label-inline">Email:</span>
            <span class="info-text">{{ c.email }}</span>
          </div>

          <div class="info-row">
            <span class="info-label-inline">Role:</span>
            <span class="info-text">{{ c.role }}</span>
          </div>

          <div class="info-row id-row">
            <span class="info-label">ID:</span>
            <span class="info-value">{{ c.idCustomer }}</span>
          </div>
        </div>

        <div class="card-actions">
          <button class="btn-edit" @click="openEdit(c)">
            Edit
          </button>
          <button class="btn-delete" @click="openDeleteModal(c.idCustomer)">
            Delete
          </button>
        </div>
      </div>
    </div>

    <transition name="modal-fade">
      <div v-if="modalOpen" class="modal-backdrop" @click="closeModal">
        <div class="modal-content" @click.stop>
          <div class="modal-header">
            <h3>{{ isEdit ? "Edit Customer" : "New Customer" }}</h3>
            <button class="btn-close" @click="closeModal">×</button>
          </div>

          <div class="modal-body">
            <div class="form-group">
              <label class="form-label">First Name</label>
              <input
                  v-model="form.firstName"
                  class="form-input"
                  :class="{ 'input-error': fieldErrors.firstName }"
                  placeholder="Enter first name"
              />
              <div v-if="fieldErrors.firstName" class="field-error">
                {{ fieldErrors.firstName }}
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">Last Name</label>
              <input
                  v-model="form.lastName"
                  class="form-input"
                  :class="{ 'input-error': fieldErrors.lastName }"
                  placeholder="Enter last name"
              />
              <div v-if="fieldErrors.lastName" class="field-error">
                {{ fieldErrors.lastName }}
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">Phone</label>
              <input
                  v-model="form.phone"
                  class="form-input"
                  :class="{ 'input-error': fieldErrors.phone }"
                  placeholder="+996 500 000 000"
              />
              <div v-if="fieldErrors.phone" class="field-error">
                {{ fieldErrors.phone }}
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">Email</label>
              <input
                  v-model="form.email"
                  class="form-input"
                  :class="{ 'input-error': fieldErrors.email }"
                  type="email"
                  placeholder="email@example.com"
              />
              <div v-if="fieldErrors.email" class="field-error">
                {{ fieldErrors.email }}
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">Password</label>
              <input
                  v-model="form.password"
                  class="form-input"
                  :class="{ 'input-error': fieldErrors.password }"
                  type="password"
                  placeholder="Enter password"
              />
              <div v-if="fieldErrors.password" class="field-error">
                {{ fieldErrors.password }}
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">Role</label>
              <div class="role-selector">
                <label class="role-option" :class="{ 'role-selected': form.role === 'USER' }">
                  <input
                      type="radio"
                      v-model="form.role"
                      value="USER"
                      class="role-radio"
                  />
                  <div class="role-card">
                    <div class="role-icon user-icon">
                      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                        <circle cx="12" cy="7" r="4"></circle>
                      </svg>
                    </div>
                    <div class="role-info">
                      <div class="role-title">User</div>
                      <div class="role-description">Standard access</div>
                    </div>
                  </div>
                </label>

                <label class="role-option" :class="{ 'role-selected': form.role === 'ADMIN' }">
                  <input
                      type="radio"
                      v-model="form.role"
                      value="ADMIN"
                      class="role-radio"
                  />
                  <div class="role-card">
                    <div class="role-icon admin-icon">
                      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M12 2L2 7l10 5 10-5-10-5z"></path>
                        <path d="M2 17l10 5 10-5M2 12l10 5 10-5"></path>
                      </svg>
                    </div>
                    <div class="role-info">
                      <div class="role-title">Admin</div>
                      <div class="role-description">Full access</div>
                    </div>
                  </div>
                </label>
              </div>
              <div v-if="fieldErrors.role" class="field-error">
                {{ fieldErrors.role }}
              </div>
            </div>

            <div v-if="modalError" class="modal-error">
              {{ modalError }}
            </div>
          </div>

          <div class="modal-footer">
            <button class="btn-cancel" @click="closeModal">Cancel</button>
            <button class="btn-save" @click="submit" :disabled="saving">
              {{ saving ? "Saving..." : "Save" }}
            </button>
          </div>
        </div>
      </div>
    </transition>

    <!-- Delete Confirmation Modal -->
    <transition name="modal-fade">
      <div v-if="deleteModalOpen" class="modal-backdrop" @click="closeDeleteModal">
        <div class="modal-content" @click.stop>
          <div class="modal-header">
            <h3>Confirm Deletion</h3>
            <button class="btn-close" @click="closeDeleteModal">×</button>
          </div>

          <div class="modal-body">
            <p class="delete-message">Are you sure you want to delete this customer? This action cannot be undone.</p>

            <div v-if="deleteError" class="modal-error">
              {{ deleteError }}
            </div>
          </div>

          <div class="modal-footer">
            <button class="btn-cancel" @click="closeDeleteModal">Cancel</button>
            <button class="btn-delete-btn" @click="confirmDelete" :disabled="deleting">
              {{ deleting ? "Deleting..." : "Delete" }}
            </button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import CustomerService from "../services/customer.service";

export default {
  name: "Customer",
  data() {
    return {
      customers: [],
      brokenImages: {},
      loading: false,
      error: "",
      modalOpen: false,
      deleteModalOpen: false,
      isEdit: false,
      editId: null,
      deleteId: null,
      saving: false,
      deleting: false,
      modalError: "",
      deleteError: "",
      form: {
        firstName: "",
        lastName: "",
        phone: "",
        email: "",
        photoFile: null,
        role: 'USER'
      },
      fieldErrors: {
        firstName: "",
        lastName: "",
        phone: "",
        email: "",
      },
    };
  },

  mounted() {
    this.load();
  },

  methods: {
    avatarUrl(path) {
      if (!path) return null;

      return "http://localhost:4445" + path;
    },
    onImgError(id) {
      this.brokenImages[id] = true;
    },
    getInitials(firstName, lastName) {
      const first = firstName ? firstName.charAt(0).toUpperCase() : "";
      const last = lastName ? lastName.charAt(0).toUpperCase() : "";
      return first + last || "??";
    },

    async load() {
      this.loading = true;
      this.error = "";

      try {
        const res = await CustomerService.getAll();
        this.customers = res.data || [];
      } catch (e) {
        this.error =
            e.response?.data?.message ||
            e.message ||
            String(e);
      } finally {
        this.loading = false;
      }
    },

    clearFieldErrors() {
      this.fieldErrors = {
        firstName: "",
        lastName: "",
        phone: "",
        email: "",
      };
    },

    openCreate() {
      this.isEdit = false;
      this.editId = null;
      this.modalError = "";
      this.clearFieldErrors();
      this.form = {firstName: "", lastName: "", phone: "", email: "", role: 'USER'};
      this.modalOpen = true;
    },

    openEdit(c) {
      this.isEdit = true;
      this.editId = c.idCustomer;
      this.modalError = "";
      this.clearFieldErrors();
      this.form = {
        firstName: c.firstName || "",
        lastName: c.lastName || "",
        phone: c.phone || "",
        email: c.email || "",
        password: "",
        photoFile: null,
        role: c.role || 'USER'
      };
      this.modalOpen = true;
    },

    closeModal() {
      this.modalOpen = false;
      this.clearFieldErrors();
    },

    async submit() {
      this.saving = true
      this.modalError = ""
      this.clearFieldErrors()

      try {
        const fd = new FormData()

        const payload = {
          firstName: this.form.firstName,
          lastName: this.form.lastName,
          phone: this.form.phone,
          email: this.form.email,
          role: this.form.role,
          password: this.form.password
        }

        fd.append(
            "data",
            new Blob([JSON.stringify(payload)], { type: "application/json" })
        )

        if (this.form.photoFile) {
          fd.append("photo", this.form.photoFile)
        }

        if (this.isEdit) {
          // await CustomerService.update(this.editId, fd)
          await CustomerService.updateJson(this.editId, payload);
        } else {
          // await CustomerService.create(fd)
          await CustomerService.create(fd);
        }
        this.modalOpen = false
        await this.load()
      } catch (e) {
        this.modalError = e.response?.data?.message || e.message
      } finally {
        this.saving = false
      }
    },

    openDeleteModal(idCustomer) {
      this.deleteId = idCustomer;
      this.deleteError = "";
      this.deleteModalOpen = true;
    },

    closeDeleteModal() {
      this.deleteModalOpen = false;
      this.deleteId = null;
    },

    async confirmDelete() {
      if (!this.deleteId) return;

      this.deleting = true;
      this.deleteError = "";
      try {
        await CustomerService.deleteById(this.deleteId);
        this.deleteModalOpen = false;
        await this.load();
      } catch (e) {
        this.deleteError =
            (e.response && e.response.data && e.response.data.message) ||
            e.message ||
            String(e);
      } finally {
        this.deleting = false;
      }
    },
  },
};
</script>

<style scoped>
.container-custom {
  max-width: 1200px;
  margin: 0 auto;
  padding: 32px 24px;
  background: linear-gradient(135deg, #fef5f7 0%, #f0f4fd 100%);
  min-height: 100vh;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
  flex-wrap: wrap;
  gap: 16px;
}

.page-title {
  font-size: 32px;
  font-weight: 600;
  color: #4a5568;
  margin: 0 0 4px 0;
}

.page-subtitle {
  color: #a0aec0;
  margin: 0;
  font-size: 15px;
}

.btn-create {
  background: linear-gradient(135deg, #a8d5e2 0%, #b9e4f5 100%);
  color: #2d3748;
  border: none;
  padding: 12px 24px;
  border-radius: 12px;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(168, 213, 226, 0.3);
}

.btn-create:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(168, 213, 226, 0.4);
}

.btn-icon {
  font-size: 20px;
  font-weight: 600;
}

.status-message {
  padding: 16px 20px;
  border-radius: 12px;
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.status-message.loading {
  background: #e8f4f8;
  color: #4a90a4;
}

.status-message.error {
  background: #fde8e8;
  color: #c53030;
}

.spinner {
  width: 16px;
  height: 16px;
  border: 2px solid #a8d5e2;
  border-top-color: transparent;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.customers-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 24px;
}

.customer-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
}

.customer-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.card-header-custom {
  background: linear-gradient(135deg, #fce4ec 0%, #f3e5f5 100%);
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.avatar {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  overflow: hidden;
  background: linear-gradient(135deg, #d4a5d4 0%, #c9b8d4 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 18px;
  color: white;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}


.customer-name h4 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #4a5568;
}

.card-content {
  padding: 20px 24px;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-label-inline {
  color: #a0aec0;
  font-size: 14px;
  font-weight: 500;
  min-width: 60px;
}

.info-text {
  color: #4a5568;
  font-size: 14px;
}

.id-row {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.info-label {
  color: #a0aec0;
  font-size: 13px;
  font-weight: 500;
}

.info-value {
  color: #718096;
  font-size: 13px;
  font-family: monospace;
}

.card-actions {
  padding: 16px 24px;
  background: #fafafa;
  display: flex;
  gap: 12px;
}

.btn-edit, .btn-delete {
  flex: 1;
  padding: 10px 16px;
  border: none;
  border-radius: 10px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
}

.btn-edit {
  background: linear-gradient(135deg, #d4e9f7 0%, #cfe2f3 100%);
  color: #2c5282;
}

.btn-edit:hover {
  background: linear-gradient(135deg, #c0dff0 0%, #b8d4eb 100%);
}

.btn-delete {
  background: linear-gradient(135deg, #ffd4d4 0%, #ffcece 100%);
  color: #c53030;
}

.btn-delete:hover {
  background: linear-gradient(135deg, #ffc4c4 0%, #ffb8b8 100%);
}

/* Modal */
.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  padding: 20px;
}

.modal-content {
  background: white;
  border-radius: 20px;
  width: 100%;
  max-width: 520px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  overflow: hidden;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  background: linear-gradient(135deg, #fce4ec 0%, #f3e5f5 100%);
  padding: 24px 28px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  font-size: 22px;
  font-weight: 600;
  color: #4a5568;
}

.id-cell {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
  font-size: 12px;
  color: rgba(0,0,0,0.70);
  white-space: nowrap;
}




.btn-close {
  background: none;
  border: none;
  font-size: 32px;
  color: #a0aec0;
  cursor: pointer;
  line-height: 1;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  transition: all 0.2s;
}

.xml-mode-switch {
  display: flex;
  gap: 8px;
}

.btn-close:hover {
  background: rgba(0, 0, 0, 0.05);
  color: #4a5568;
}

.modal-body {
  padding: 28px;
}

.delete-message {
  color: #4a5568;
  font-size: 16px;
  margin: 0;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  margin-bottom: 8px;
  color: #4a5568;
  font-weight: 500;
  font-size: 14px;
}

.form-input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e8eef5;
  border-radius: 10px;
  font-size: 15px;
  transition: all 0.2s;
  box-sizing: border-box;
}

.form-input:focus {
  outline: none;
  border-color: #a8d5e2;
  background: #fafcfe;
}

.form-input.input-error {
  border-color: #fc8181;
  background: #fff5f5;
}

.form-input.input-error:focus {
  border-color: #f56565;
  background: #fff5f5;
}

.field-error {
  color: #c53030;
  font-size: 13px;
  margin-top: 6px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.field-error::before {
  content: "⚠";
  font-size: 14px;
}

.modal-error {
  background: #fde8e8;
  color: #c53030;
  padding: 12px 16px;
  border-radius: 10px;
  margin-top: 16px;
  font-size: 14px;
  text-align: center;
  font-weight: 500;
}

.modal-footer {
  padding: 20px 28px;
  background: #fafafa;
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.btn-cancel, .btn-save, .btn-delete-btn {
  padding: 12px 24px;
  border: none;
  border-radius: 10px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 15px;
}

.btn-cancel {
  background: #e2e8f0;
  color: #4a5568;
}

.btn-cancel:hover {
  background: #cbd5e0;
}

.btn-save {
  background: linear-gradient(135deg, #a8d5e2 0%, #b9e4f5 100%);
  color: #2d3748;
  min-width: 120px;
}

.btn-save:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(168, 213, 226, 0.4);
}

.btn-save:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-delete-btn {
  background: linear-gradient(135deg, #ffd4d4 0%, #ffcece 100%);
  color: #c53030;
  min-width: 120px;
}

.btn-delete-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #ffc4c4 0%, #ffb8b8 100%);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(255, 212, 212, 0.4);
}

.btn-delete-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.modal-fade-enter-active, .modal-fade-leave-active {
  transition: opacity 0.3s;
}

.modal-fade-enter-active .modal-content,
.modal-fade-leave-active .modal-content {
  transition: transform 0.3s;
}

.modal-fade-enter-from, .modal-fade-leave-to {
  opacity: 0;
}

.modal-fade-enter-from .modal-content {
  transform: scale(0.9);
}

.modal-fade-leave-to .modal-content {
  transform: scale(0.9);
}

.role-selector {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin-top: 8px;
}

.role-option {
  cursor: pointer;
  position: relative;
}

.role-radio {
  position: absolute;
  opacity: 0;
  pointer-events: none;
}

.role-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  border: 2px solid #e8eef5;
  border-radius: 12px;
  background: #fafcfe;
  transition: all 0.2s ease;
}

.role-option:hover .role-card {
  border-color: #a8d5e2;
  background: white;
}

.role-selected .role-card {
  border-color: #a8d5e2;
  background: linear-gradient(135deg, #f0f9fc 0%, #fafcfe 100%);
  box-shadow: 0 0 0 3px rgba(168, 213, 226, 0.1);
}

.role-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.2s ease;
}

.user-icon {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  color: #1976d2;
}

.admin-icon {
  background: linear-gradient(135deg, #fff3e0 0%, #ffe0b2 100%);
  color: #f57c00;
}

.role-selected .user-icon {
  background: linear-gradient(135deg, #64b5f6 0%, #42a5f5 100%);
  color: white;
}

.role-selected .admin-icon {
  background: linear-gradient(135deg, #ffb74d 0%, #ffa726 100%);
  color: white;
}

.role-info {
  flex: 1;
}

.role-title {
  font-size: 15px;
  font-weight: 600;
  color: #4a5568;
  margin-bottom: 2px;
}

.role-description {
  font-size: 13px;
  color: #a0aec0;
}

@media (max-width: 480px) {
  .role-selector {
    grid-template-columns: 1fr;
  }
}
</style>
