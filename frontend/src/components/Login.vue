<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <div class="avatar-login">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
            <circle cx="12" cy="7" r="4"></circle>
          </svg>
        </div>
        <h2 class="login-title">Welcome Back</h2>
        <p class="login-subtitle">Sign in to your account</p>
      </div>

      <Form @submit="handleLogin" :validation-schema="schema" class="login-form">
        <div class="form-group">
          <label for="username" class="form-label">Username</label>
          <Field
              name="username"
              type="text"
              class="form-input"
              placeholder="Enter your username"
          />
          <ErrorMessage name="username" class="error-message" />
        </div>

        <div class="form-group">
          <label for="password" class="form-label">Password</label>
          <Field
              name="password"
              type="password"
              class="form-input"
              placeholder="Enter your password"
          />
          <ErrorMessage name="password" class="error-message" />
        </div>

        <div class="form-group">
          <button type="submit" class="btn-login" :disabled="loading">
            <span v-show="loading" class="spinner"></span>
            <span>{{ loading ? 'Signing in...' : 'Sign In' }}</span>
          </button>
        </div>

        <div class="form-group" v-if="message">
          <div class="alert-error">
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
  name: "Login",
  components: {
    Form,
    Field,
    ErrorMessage,
  },
  data() {
    const schema = yup.object().shape({
      username: yup.string().required("Username is required!"),
      password: yup.string().required("Password is required!"),
    });

    return {
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
  created() {
    if (this.loggedIn) {
      this.$router.push("/profile");
    }
  },
  methods: {
    handleLogin(user) {
      this.loading = true;

      this.$store.dispatch("auth/login", user).then(
          () => {
            this.$router.push("/profile");
          },
          (error) => {
            this.loading = false;
            this.message =
                (error.response &&
                    error.response.data &&
                    error.response.data.message) ||
                error.message ||
                error.toString();
          }
      );
    },
  },
};
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #fef5f7 0%, #f0f4fd 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
}

.login-card {
  width: 100%;
  max-width: 440px;
  background: white;
  border-radius: 24px;
  padding: 48px 40px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
}

.login-header {
  text-align: center;
  margin-bottom: 36px;
}

.avatar-login {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #d4a5d4 0%, #c9b8d4 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  color: white;
  box-shadow: 0 4px 16px rgba(212, 165, 212, 0.3);
}

.login-title {
  margin: 0 0 8px 0;
  font-size: 28px;
  font-weight: 600;
  color: #4a5568;
}

.login-subtitle {
  margin: 0;
  color: #a0aec0;
  font-size: 15px;
}

.login-form {
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

.btn-login {
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

.btn-login:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(168, 213, 226, 0.4);
}

.btn-login:active:not(:disabled) {
  transform: translateY(0);
}

.btn-login:disabled {
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

.alert-error {
  background: linear-gradient(135deg, #ffd4d4 0%, #ffe8e8 100%);
  color: #c53030;
  padding: 14px 18px;
  border-radius: 12px;
  font-size: 14px;
  border: 1px solid #ffc4c4;
  margin-top: 4px;
}

@media (max-width: 480px) {
  .login-card {
    padding: 36px 28px;
  }

  .login-title {
    font-size: 24px;
  }

  .avatar-login {
    width: 64px;
    height: 64px;
  }

  .avatar-login svg {
    width: 36px;
    height: 36px;
  }
}
</style>