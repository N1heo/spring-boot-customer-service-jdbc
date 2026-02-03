<template>
  <div class="register-container">
    <div class="register-card">
      <div class="register-header">
        <div class="avatar-register">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
            <circle cx="8.5" cy="7" r="4"></circle>
            <line x1="20" y1="8" x2="20" y2="14"></line>
            <line x1="23" y1="11" x2="17" y2="11"></line>
          </svg>
        </div>
        <h2 class="register-title">Create Account</h2>
        <p class="register-subtitle">Sign up to get started</p>
      </div>

      <Form @submit="handleRegister" :validation-schema="schema" class="register-form">
        <div v-if="!successful">
          <div class="form-group">
            <label for="username" class="form-label">Username</label>
            <Field
                name="username"
                type="text"
                class="form-input"
                placeholder="Choose a username"
            />
            <ErrorMessage name="username" class="error-message" />
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
        </div>

        <div v-if="message" class="form-group">
          <div class="alert" :class="successful ? 'alert-success' : 'alert-error'">
            {{ message }}
          </div>
        </div>
      </Form>
    </div>
  </div>
</template>

<script>
import { Form, Field, ErrorMessage } from "vee-validate";
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
      username: yup
          .string()
          .required("Username is required!")
          .min(3, "Must be at least 3 characters!")
          .max(20, "Must be maximum 20 characters!"),
      email: yup
          .string()
          .required("Email is required!")
          .email("Email is invalid!")
          .max(50, "Must be maximum 50 characters!"),
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
    handleRegister(user) {
      this.message = "";
      this.successful = false;
      this.loading = true;

      this.$store.dispatch("auth/register", user).then(
          (data) => {
            this.message = data.message;
            this.successful = true;
            this.loading = false;
          },
          (error) => {
            this.message =
                (error.response &&
                    error.response.data &&
                    error.response.data.message) ||
                error.message ||
                error.toString();
            this.successful = false;
            this.loading = false;
          }
      );
    },
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
  to { transform: rotate(360deg); }
}

.alert {
  padding: 14px 18px;
  border-radius: 12px;
  font-size: 14px;
  margin-top: 4px;
}

.alert-success {
  background: linear-gradient(135deg, #d4f4dd 0%, #e8f5e9 100%);
  color: #2d7738;
  border: 1px solid #b8e6c4;
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
}
</style>