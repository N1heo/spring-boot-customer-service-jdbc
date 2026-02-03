<template>
  <div class="moderator-board-container">
    <div class="board-wrapper">
      <div class="board-header">
        <div class="header-icon">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
          </svg>
        </div>
        <h1 class="board-title">Moderator Dashboard</h1>
        <p class="board-subtitle">Content moderation and management tools</p>
      </div>

      <div class="content-section">
        <div v-if="loading" class="loading-state">
          <div class="spinner-large"></div>
          <p>Loading moderator dashboard...</p>
        </div>
        <div v-else class="content-card">
          <div class="card-icon">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
              <path d="M9 12l2 2 4-4"></path>
            </svg>
          </div>
          <div class="content-text">
            <p>{{ content }}</p>
          </div>
        </div>
      </div>

      <div class="info-grid">
        <div class="info-card">
          <div class="info-card-icon">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
              <polyline points="22 4 12 14.01 9 11.01"></polyline>
            </svg>
          </div>
          <div class="info-card-content">
            <h3 class="info-card-title">Review Queue</h3>
            <p class="info-card-text">Manage pending content reviews</p>
          </div>
        </div>

        <div class="info-card">
          <div class="info-card-icon">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path>
              <line x1="12" y1="9" x2="12" y2="13"></line>
              <line x1="12" y1="17" x2="12.01" y2="17"></line>
            </svg>
          </div>
          <div class="info-card-content">
            <h3 class="info-card-title">Reports</h3>
            <p class="info-card-text">Handle user reports and flags</p>
          </div>
        </div>

        <div class="info-card">
          <div class="info-card-icon">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
              <circle cx="9" cy="7" r="4"></circle>
              <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
              <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
            </svg>
          </div>
          <div class="info-card-content">
            <h3 class="info-card-title">User Management</h3>
            <p class="info-card-text">Monitor and manage user accounts</p>
          </div>
        </div>

        <div class="info-card">
          <div class="info-card-icon">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="20" x2="18" y2="10"></line>
              <line x1="12" y1="20" x2="12" y2="4"></line>
              <line x1="6" y1="20" x2="6" y2="14"></line>
            </svg>
          </div>
          <div class="info-card-content">
            <h3 class="info-card-title">Analytics</h3>
            <p class="info-card-text">View moderation statistics</p>
          </div>
        </div>

        <div class="info-card">
          <div class="info-card-icon">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
            </svg>
          </div>
          <div class="info-card-content">
            <h3 class="info-card-title">Messages</h3>
            <p class="info-card-text">Communicate with users</p>
          </div>
        </div>

        <div class="info-card">
          <div class="info-card-icon">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z"></path>
            </svg>
          </div>
          <div class="info-card-content">
            <h3 class="info-card-title">Tools</h3>
            <p class="info-card-text">Access moderation utilities</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UserService from "../services/user.service";

export default {
  name: "Moderator",
  data() {
    return {
      content: "",
      loading: true,
    };
  },
  mounted() {
    UserService.getModeratorBoard().then(
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
.moderator-board-container {
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
  background: linear-gradient(135deg, #a8d5e2 0%, #b9e4f5 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24px;
  color: white;
  box-shadow: 0 8px 24px rgba(168, 213, 226, 0.3);
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
  border-top-color: #a8d5e2;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
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
  background: linear-gradient(135deg, #d4e9f7 0%, #cfe2f3 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #2c5282;
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

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
}

.info-card {
  background: white;
  border-radius: 16px;
  padding: 28px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  display: flex;
  gap: 20px;
  align-items: flex-start;
  transition: all 0.3s ease;
}

.info-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.info-card:nth-child(1) .info-card-icon {
  background: linear-gradient(135deg, #d4f4dd 0%, #e8f5e9 100%);
  color: #2d7738;
}

.info-card:nth-child(2) .info-card-icon {
  background: linear-gradient(135deg, #ffe8c4 0%, #fff3e0 100%);
  color: #d97706;
}

.info-card:nth-child(3) .info-card-icon {
  background: linear-gradient(135deg, #d4e9f7 0%, #cfe2f3 100%);
  color: #2c5282;
}

.info-card:nth-child(4) .info-card-icon {
  background: linear-gradient(135deg, #fce4ec 0%, #f3e5f5 100%);
  color: #8b5a99;
}

.info-card:nth-child(5) .info-card-icon {
  background: linear-gradient(135deg, #e0d4f7 0%, #e8e0f5 100%);
  color: #6b46c1;
}

.info-card:nth-child(6) .info-card-icon {
  background: linear-gradient(135grad, #ffd4d4 0%, #ffe8e8 100%);
  color: #c53030;
}

.info-card-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.info-card-content {
  flex: 1;
}

.info-card-title {
  font-size: 18px;
  font-weight: 600;
  color: #4a5568;
  margin: 0 0 8px 0;
}

.info-card-text {
  font-size: 14px;
  color: #a0aec0;
  margin: 0;
  line-height: 1.5;
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

  .info-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .moderator-board-container {
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