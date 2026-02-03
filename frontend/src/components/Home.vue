<template>
  <div class="home-container">
    <div class="home-wrapper">
      <div class="hero-section">
        <div class="hero-icon">
          <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
            <polyline points="9 22 9 12 15 12 15 22"></polyline>
          </svg>
        </div>
        <h1 class="hero-title">Welcome Home</h1>
        <div class="content-card">
          <div v-if="loading" class="loading-state">
            <div class="spinner-large"></div>
            <p>Loading content...</p>
          </div>
          <div v-else-if="content" class="content-display">
            <p>{{ content }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UserService from "../services/user.service";

export default {
  name: "Home",
  data() {
    return {
      content: "",
      loading: true,
    };
  },
  mounted() {
    UserService.getPublicContent().then(
        (response) => {
          this.content = response.data;
          this.loading = false;
        },
        (error) => {
          this.content =
              (error.response &&
                  error.response.data &&
                  error.response.data.message) ||
              error.message ||
              error.toString();
          this.loading = false;
        }
    );
  },
};
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #fef5f7 0%, #f0f4fd 100%);
  padding: 48px 24px;
}

.home-wrapper {
  max-width: 1200px;
  margin: 0 auto;
}

.hero-section {
  text-align: center;
  margin-bottom: 64px;
}

.hero-icon {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: linear-gradient(135deg, #d4a5d4 0%, #c9b8d4 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 32px;
  color: white;
  box-shadow: 0 8px 24px rgba(212, 165, 212, 0.3);
}

.hero-title {
  font-size: 48px;
  font-weight: 700;
  color: #4a5568;
  margin: 0 0 32px 0;
  background: linear-gradient(135deg, #4a5568 0%, #6b7688 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.content-card {
  max-width: 700px;
  margin: 0 auto;
  background: white;
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  min-height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  color: #a0aec0;
}

.spinner-large {
  width: 40px;
  height: 40px;
  border: 4px solid #e8eef5;
  border-top-color: #a8d5e2;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.content-display p {
  font-size: 18px;
  color: #4a5568;
  line-height: 1.6;
  margin: 0;
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 36px;
  }

  .hero-icon {
    width: 96px;
    height: 96px;
  }

  .hero-icon svg {
    width: 48px;
    height: 48px;
  }

  .content-card {
    padding: 28px;
  }

  .content-display p {
    font-size: 16px;
  }
}

@media (max-width: 480px) {
  .hero-title {
    font-size: 28px;
  }

  .home-container {
    padding: 32px 16px;
  }
}
</style>