<template>
  <div class="user-board-container">
    <div class="board-wrapper">
      <div class="board-header">
        <div class="header-icon">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
            <circle cx="12" cy="7" r="4"></circle>
          </svg>
        </div>
        <h1 class="board-title">User Dashboard</h1>
        <p class="board-subtitle">Welcome to your personal space</p>
      </div>

      <div class="content-section">
        <div v-if="loading" class="loading-state">
          <div class="spinner-large"></div>
          <p>Loading your dashboard...</p>
        </div>
        <div v-else class="content-card">
          <div class="card-icon">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline>
            </svg>
          </div>
          <div class="content-text">
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
  name: "User",
  data() {
    return {
      content: "",
      loading: true,
    };
  },
  mounted() {
    UserService.getUserBoard().then(
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
.user-board-container {
  min-height: calc(100vh - 72px);
  padding: 48px 24px;
}

.board-wrapper {
  max-width: 1200px;
  margin: 0 auto;
}

.board-header {
  text-align: center;
  margin-bottom: 48px;
}

.header-icon {
  width: 96px;
  height: 96px;
  border-radius: 50%;
  background: linear-gradient(135deg, #d4a5d4 0%, #c9b8d4 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24px;
  color: white;
  box-shadow: 0 8px 24px rgba(212, 165, 212, 0.3);
}

.board-title {
  font-size: 42px;
  font-weight: 700;
  color: #4a5568;
  margin: 0 0 12px 0;
}

.board-subtitle {
  font-size: 16px;
  color: #a0aec0;
  margin: 0;
}

.content-section {
  margin-bottom: 48px;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  padding: 60px 20px;
  color: #a0aec0;
}

.spinner-large {
  width: 48px;
  height: 48px;
  border: 4px solid #e8eef5;
  border-top-color: #d4a5d4;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.content-card {
  background: white;
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  gap: 24px;
}

.card-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  background: linear-gradient(135deg, #fce4ec 0%, #f3e5f5 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #8b5a99;
  flex-shrink: 0;
}

.content-text {
  flex: 1;
}

.content-text p {
  font-size: 18px;
  color: #4a5568;
  line-height: 1.6;
  margin: 0;
}

@media (max-width: 768px) {
  .board-title {
    font-size: 32px;
  }

  .header-icon {
    width: 72px;
    height: 72px;
  }

  .header-icon svg {
    width: 36px;
    height: 36px;
  }

  .content-card {
    flex-direction: column;
    text-align: center;
    padding: 32px;
  }
}

@media (max-width: 480px) {
  .user-board-container {
    padding: 32px 16px;
  }

  .board-title {
    font-size: 28px;
  }

  .content-card {
    padding: 24px;
  }

  .content-text p {
    font-size: 16px;
  }
}
</style>