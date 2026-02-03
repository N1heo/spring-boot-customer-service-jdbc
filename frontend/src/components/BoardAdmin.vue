<template>
  <div class="admin-board-container">
    <div class="board-wrapper">
      <div class="board-header">
        <div class="header-icon">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
            <line x1="9" y1="3" x2="9" y2="21"></line>
          </svg>
        </div>
        <h1 class="board-title">Admin Dashboard</h1>
        <p class="board-subtitle">Complete system control and management</p>
      </div>

      <div class="content-section">
        <div v-if="loading" class="loading-state">
          <div class="spinner-large"></div>
          <p>Loading admin dashboard...</p>
        </div>
        <div v-else class="content-card">
          <div class="card-icon">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 2L2 7l10 5 10-5-10-5z"></path>
              <path d="M2 17l10 5 10-5"></path>
              <path d="M2 12l10 5 10-5"></path>
            </svg>
          </div>
          <div class="content-text">
            <p>{{ content }}</p>
          </div>
        </div>
      </div>

      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
              <circle cx="9" cy="7" r="4"></circle>
              <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
              <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
            </svg>
          </div>
          <div class="stat-content">
            <h3 class="stat-value">1,234</h3>
            <p class="stat-label">Total Users</p>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline>
            </svg>
          </div>
          <div class="stat-content">
            <h3 class="stat-value">856</h3>
            <p class="stat-label">Active Sessions</p>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
              <polyline points="7 10 12 15 17 10"></polyline>
              <line x1="12" y1="15" x2="12" y2="3"></line>
            </svg>
          </div>
          <div class="stat-content">
            <h3 class="stat-value">4.8 GB</h3>
            <p class="stat-label">Storage Used</p>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"></circle>
              <polyline points="12 6 12 12 16 14"></polyline>
            </svg>
          </div>
          <div class="stat-content">
            <h3 class="stat-value">99.8%</h3>
            <p class="stat-label">Uptime</p>
          </div>
        </div>
      </div>

      <div class="info-grid">
        <div class="info-card">
          <div class="info-card-icon">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
              <circle cx="8.5" cy="7" r="4"></circle>
              <polyline points="17 11 19 13 23 9"></polyline>
            </svg>
          </div>
          <div class="info-card-content">
            <h3 class="info-card-title">User Management</h3>
            <p class="info-card-text">Create, edit, and manage user accounts</p>
          </div>
        </div>

        <div class="info-card">
          <div class="info-card-icon">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
            </svg>
          </div>
          <div class="info-card-content">
            <h3 class="info-card-title">Security</h3>
            <p class="info-card-text">Configure security settings and permissions</p>
          </div>
        </div>

        <div class="info-card">
          <div class="info-card-icon">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
              <polyline points="14 2 14 8 20 8"></polyline>
            </svg>
          </div>
          <div class="info-card-content">
            <h3 class="info-card-title">System Logs</h3>
            <p class="info-card-text">View and analyze system activity logs</p>
          </div>
        </div>

        <div class="info-card">
          <div class="info-card-icon">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path>
              <polyline points="3.27 6.96 12 12.01 20.73 6.96"></polyline>
              <line x1="12" y1="22.08" x2="12" y2="12"></line>
            </svg>
          </div>
          <div class="info-card-content">
            <h3 class="info-card-title">Database</h3>
            <p class="info-card-text">Manage database and backups</p>
          </div>
        </div>

        <div class="info-card">
          <div class="info-card-icon">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="3"></circle>
              <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z"></path>
            </svg>
          </div>
          <div class="info-card-content">
            <h3 class="info-card-title">System Settings</h3>
            <p class="info-card-text">Configure global system preferences</p>
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
            <h3 class="info-card-title">Reports & Analytics</h3>
            <p class="info-card-text">Generate detailed system reports</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UserService from "../services/user.service";

export default {
  name: "Admin",
  data() {
    return {
      content: "",
      loading: true,
    };
  },
  mounted() {
    UserService.getAdminBoard().then(
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
.admin-board-container {
  min-height: calc(100vh - 72px);
  padding: 48px 24px;
}

.board-wrapper {
  max-width: 1400px;
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
  background: linear-gradient(135deg, #ffd4d4 0%, #ffe8e8 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24px;
  color: #c53030;
  box-shadow: 0 8px 24px rgba(255, 212, 212, 0.4);
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
  border-top-color: #ffa8a8;
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
  background: linear-gradient(135deg, #ffe8c4 0%, #fff3e0 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #d97706;
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

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
  margin-bottom: 48px;
}

.stat-card {
  background: white;
  border-radius: 16px;
  padding: 28px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  display: flex;
  gap: 20px;
  align-items: center;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.stat-card:nth-child(1) .stat-icon {
  background: linear-gradient(135deg, #d4e9f7 0%, #cfe2f3 100%);
  color: #2c5282;
}

.stat-card:nth-child(2) .stat-icon {
  background: linear-gradient(135deg, #d4f4dd 0%, #e8f5e9 100%);
  color: #2d7738;
}

.stat-card:nth-child(3) .stat-icon {
  background: linear-gradient(135deg, #fce4ec 0%, #f3e5f5 100%);
  color: #8b5a99;
}

.stat-card:nth-child(4) .stat-icon {
  background: linear-gradient(135deg, #ffe8c4 0%, #fff3e0 100%);
  color: #d97706;
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #4a5568;
  margin: 0 0 4px 0;
}

.stat-label {
  font-size: 14px;
  color: #a0aec0;
  margin: 0;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
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
  background: linear-gradient(135deg, #d4e9f7 0%, #cfe2f3 100%);
  color: #2c5282;
}

.info-card:nth-child(2) .info-card-icon {
  background: linear-gradient(135deg, #ffd4d4 0%, #ffe8e8 100%);
  color: #c53030;
}

.info-card:nth-child(3) .info-card-icon {
  background: linear-gradient(135deg, #ffe8c4 0%, #fff3e0 100%);
  color: #d97706;
}

.info-card:nth-child(4) .info-card-icon {
  background: linear-gradient(135deg, #d4f4dd 0%, #e8f5e9 100%);
  color: #2d7738;
}

.info-card:nth-child(5) .info-card-icon {
  background: linear-gradient(135deg, #e0d4f7 0%, #e8e0f5 100%);
  color: #6b46c1;
}

.info-card:nth-child(6) .info-card-icon {
  background: linear-gradient(135deg, #fce4ec 0%, #f3e5f5 100%);
  color: #8b5a99;
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

  .stats-grid {
    grid-template-columns: 1fr;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .admin-board-container {
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

  .stat-value {
    font-size: 24px;
  }
}
</style>