<template>
  <div class="register-container">
    <div class="register-card">
      <div class="register-header">
        <div class="avatar-register" :class="{ 'success-avatar': successful }">
          <svg v-if="!successful" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
            <circle cx="8.5" cy="7" r="4"></circle>
            <line x1="20" y1="8" x2="20" y2="14"></line>
            <line x1="23" y1="11" x2="17" y2="11"></line>
          </svg>
          <svg v-else width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
            <polyline points="22 4 12 14.01 9 11.01"></polyline>
          </svg>
        </div>
        <h2 class="register-title">{{ successful ? 'Success!' : 'Create Account' }}</h2>
        <p class="register-subtitle">{{ successful ? 'Your account has been created' : 'Sign up to get started' }}</p>
      </div>

      <Form @submit="handleRegister" :validation-schema="schema" class="register-form" v-if="!successful">

        <!-- Кастомный загрузчик фото -->
        <div class="form-group">
          <label class="form-label">Profile Photo (Optional)</label>
          <div class="photo-upload-wrapper">
            <input
                type="file"
                @change="onFile"
                accept="image/*"
                ref="fileInput"
                class="file-input-hidden"
                id="photoInput"
            />
            <label for="photoInput" class="photo-upload-label">
              <div class="photo-preview" v-if="photoPreview">
                <img :src="photoPreview" alt="Preview" />
                <div class="photo-overlay">
                  <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path>
                    <circle cx="12" cy="13" r="4"></circle>
                  </svg>
                  <span>Change</span>
                </div>
              </div>
              <div class="photo-placeholder" v-else>
                <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path>
                  <circle cx="12" cy="13" r="4"></circle>
                </svg>
                <span class="upload-text">Upload Photo</span>
                <span class="upload-hint">Click to browse</span>
              </div>
            </label>
          </div>
        </div>

        <div class="form-group">
          <label for="firstname" class="form-label">Firstname</label>
          <Field
              name="firstname"
              type="text"
              class="form-input"
              placeholder="Choose a firstname"
          />
          <ErrorMessage name="firstname" class="error-message" />
        </div>

        <div class="form-group">
          <label for="lastname" class="form-label">Lastname</label>
          <Field
              name="lastname"
              type="text"
              class="form-input"
              placeholder="Choose a lastname"
          />
          <ErrorMessage name="lastname" class="error-message" />
        </div>

        <div class="form-group">
          <label for="email" class="form-label">Email</label>
          <Field
              name="email"
              type="email"
              class="form-input"
              placeholder="Enter your email"
          />
          <ErrorMessage name="email" class="error-message" />
        </div>

        <div class="form-group">
          <label for="phone" class="form-label">Phone</label>
          <Field
              name="phone"
              type="tel"
              class="form-input"
              placeholder="Enter your phone"
          />
          <ErrorMessage name="phone" class="error-message" />
        </div>

        <div class="form-group">
          <label for="password" class="form-label">Password</label>
          <Field
              name="password"
              type="password"
              class="form-input"
              placeholder="Create a password"
          />
          <ErrorMessage name="password" class="error-message" />
        </div>

        <div class="form-group">
          <button type="submit" class="btn-register" :disabled="loading">
            <span v-show="loading" class="spinner"></span>
            <span>{{ loading ? 'Creating Account...' : 'Sign Up' }}</span>
          </button>
        </div>
      </Form>

      <div v-if="successful" class="success-content">
        <div class="success-message">
          <p class="success-text">Welcome aboard! Your account is ready.</p>
        </div>
        <button @click="goToProfile" class="btn-profile">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
            <circle cx="12" cy="7" r="4"></circle>
          </svg>
          <span>Go to Profile</span>
        </button>
      </div>

      <!-- Error Message -->
      <div v-if="message && !successful" class="form-group">
        <div class="alert alert-error">
          {{ message }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {Form, Field, ErrorMessage} from "vee-validate";
import * as yup from "yup";

export default {
  name: "Register",
  components: {
    Form,
    Field,
    ErrorMessage,
  },
  data() {
    const schema = yup.object().shape({
      firstname: yup
          .string()
          .required("Firstname is required!")
          .min(3, "Must be at least 3 characters!")
          .max(20, "Must be maximum 20 characters!"),
      lastname: yup
          .string()
          .required("Lastname is required!")
          .min(3, "Must be at least 3 characters!")
          .max(20, "Must be maximum 20 characters!"),
      email: yup
          .string()
          .required("Email is required!")
          .email("Email is invalid!")
          .max(50, "Must be maximum 50 characters!"),
      phone: yup
          .string()
          .required("Phone required")
          .min(9),
      password: yup
          .string()
          .required("Password is required!")
          .min(6, "Must be at least 6 characters!")
          .max(40, "Must be maximum 40 characters!"),
    });

    return {
      successful: false,
      loading: false,
      message: "",
      schema,
      photoFile: null,
      photoPreview: null
    };
  },

  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
  },
  mounted() {
    if (this.loggedIn) {
      this.$router.push("/profile");
    }
  },
  methods: {
    onFile(e) {
      const file = e.target.files[0];
      if (file) {
        this.photoFile = file;

        const reader = new FileReader();
        reader.onload = (e) => {
          this.photoPreview = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },
    async handleRegister(user) {
      this.message = "";
      this.successful = false;
      this.loading = true;

      try {
        await this.$store.dispatch("auth/register", {
          user: user,
          photo: this.photoFile
        });

        this.successful = true;
        this.loading = false;

      } catch (error) {
        this.message =
            error.response?.data ||
            error.message ||
            error.toString();

        this.successful = false;
        this.loading = false;
      }
    },
    goToProfile() {
      this.$router.push("/profile");
    }
  },
};
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #fef5f7 0%, #f0f4fd 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
}

.register-card {
  width: 100%;
  max-width: 440px;
  background: white;
  border-radius: 24px;
  padding: 48px 40px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
}

.register-header {
  text-align: center;
  margin-bottom: 36px;
}

.avatar-register {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #a8d5e2 0%, #b9e4f5 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  color: white;
  box-shadow: 0 4px 16px rgba(168, 213, 226, 0.3);
  transition: all 0.3s ease;
}

.avatar-register.success-avatar {
  background: linear-gradient(135deg, #81c784 0%, #66bb6a 100%);
  box-shadow: 0 4px 16px rgba(129, 199, 132, 0.4);
}

.register-title {
  margin: 0 0 8px 0;
  font-size: 28px;
  font-weight: 600;
  color: #4a5568;
}

.register-subtitle {
  margin: 0;
  color: #a0aec0;
  font-size: 15px;
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-label {
  margin-bottom: 8px;
  color: #4a5568;
  font-weight: 500;
  font-size: 14px;
}

.photo-upload-wrapper {
  width: 100%;
}

.file-input-hidden {
  display: none;
}

.photo-upload-label {
  display: block;
  cursor: pointer;
}

.photo-placeholder {
  width: 100%;
  height: 160px;
  border: 2px dashed #e8eef5;
  border-radius: 12px;
  background: #fafcfe;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.3s ease;
}

.photo-placeholder:hover {
  border-color: #a8d5e2;
  background: white;
  box-shadow: 0 0 0 3px rgba(168, 213, 226, 0.1);
}

.photo-placeholder svg {
  color: #a8d5e2;
}

.upload-text {
  font-size: 15px;
  font-weight: 500;
  color: #4a5568;
}

.upload-hint {
  font-size: 13px;
  color: #a0aec0;
}

.photo-preview {
  width: 100%;
  height: 160px;
  border-radius: 12px;
  overflow: hidden;
  position: relative;
}

.photo-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.photo-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.3s ease;
  color: white;
}

.photo-preview:hover .photo-overlay {
  opacity: 1;
}

.photo-overlay span {
  font-size: 14px;
  font-weight: 500;
}

.form-input {
  width: 100%;
  padding: 14px 16px;
  border: 2px solid #e8eef5;
  border-radius: 12px;
  font-size: 15px;
  transition: all 0.2s;
  box-sizing: border-box;
  background: #fafcfe;
}

.form-input:focus {
  outline: none;
  border-color: #a8d5e2;
  background: white;
  box-shadow: 0 0 0 3px rgba(168, 213, 226, 0.1);
}

.form-input::placeholder {
  color: #cbd5e0;
}

.error-message {
  margin-top: 6px;
  color: #e53e3e;
  font-size: 13px;
  font-weight: 500;
}

.btn-register {
  width: 100%;
  padding: 14px 24px;
  background: linear-gradient(135deg, #a8d5e2 0%, #b9e4f5 100%);
  color: #2d3748;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(168, 213, 226, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-top: 8px;
}

.btn-register:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(168, 213, 226, 0.4);
}

.btn-register:active:not(:disabled) {
  transform: translateY(0);
}

.btn-register:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(45, 55, 72, 0.2);
  border-top-color: #2d3748;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.success-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
  animation: fadeIn 0.5s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.success-message {
  text-align: center;
  padding: 24px;
  background: linear-gradient(135deg, #d4f4dd 0%, #e8f5e9 100%);
  border-radius: 12px;
  border: 1px solid #b8e6c4;
}

.success-text {
  margin: 0;
  color: #2d7738;
  font-size: 15px;
  font-weight: 500;
}

.btn-profile {
  width: 100%;
  padding: 14px 24px;
  background: linear-gradient(135deg, #81c784 0%, #66bb6a 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(129, 199, 132, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.btn-profile:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(129, 199, 132, 0.4);
}

.btn-profile:active {
  transform: translateY(0);
}

.alert {
  padding: 14px 18px;
  border-radius: 12px;
  font-size: 14px;
  margin-top: 4px;
}

.alert-error {
  background: linear-gradient(135deg, #ffd4d4 0%, #ffe8e8 100%);
  color: #c53030;
  border: 1px solid #ffc4c4;
}

@media (max-width: 480px) {
  .register-card {
    padding: 36px 28px;
  }

  .register-title {
    font-size: 24px;
  }

  .avatar-register {
    width: 64px;
    height: 64px;
  }

  .avatar-register svg {
    width: 36px;
    height: 36px;
  }

  .photo-placeholder,
  .photo-preview {
    height: 140px;
  }
}
</style>
