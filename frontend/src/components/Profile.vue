<template>
  <div class="profile-container">
    <div class="profile-wrapper">
      <div class="profile-header">
        <div class="avatar-large">
          {{ getInitials(currentUser.username) }}
        </div>
        <div class="header-text">
          <h2 class="username">{{ currentUser.username }}</h2>
          <p class="subtitle">User Profile</p>
        </div>
      </div>

      <div class="profile-content">
        <div class="info-section">
          <h3 class="section-title">Account Information</h3>

          <div class="info-card">
            <div class="info-item">
              <span class="info-label">User idCustomer</span>
              <span class="info-value">{{ profile?.idCustomer }}</span>
            </div>

            <div class="info-item">
              <span class="info-label">Email Address</span>
              <span class="info-value">{{ profile?.email }}</span>
            </div>

            <div class="info-item">
              <span class="info-label">Username</span>
              <span class="info-value">{{ profile?.firstName }} {{ profile?.lastName }}</span>
            </div>

            <div class="info-item">
              <span class="info-label">Phone</span>
              <span class="info-value">{{ profile?.phone }}</span>
            </div>

          </div>
        </div>

        <div class="info-section">
          <h3 class="section-title">Roles & Permissions</h3>

          <div class="roles-container">
            <div
                class="role-badge"
                v-for="role in currentUser.roles"
                :key="role"
            >
              {{ role }}
            </div>
          </div>
        </div>

        <div class="info-section">
          <h3 class="section-title">Access Token</h3>

          <div class="token-card">
            <code class="token-display">
              {{ currentUser.accessToken.substring(0, 20) }} ...
              {{ currentUser.accessToken.substr(currentUser.accessToken.length - 20) }}
            </code>
            <button class="btn-copy" @click="copyToken" title="Copy full token">
              {{ copied ? 'Copied!' : 'Copy' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import CustomerService from "../services/customer.service";

export default {
  name: 'Profile',
  data() {
    return {
      copied: false,
      profile: null
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push("/login");
      return;
    }

    CustomerService.getProfile().then(res => {
      this.profile = res.data;
    });
  },
  methods: {
    getInitials(username) {
      if (!username) return '?';
      return username.substring(0, 2).toUpperCase();
    },
    copyToken() {
      navigator.clipboard.writeText(this.currentUser.accessToken);
      this.copied = true;
      setTimeout(() => {
        this.copied = false;
      }, 2000);
    }
  }
};
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #fef5f7 0%, #f0f4fd 100%);
  padding: 32px 24px;
}

.profile-wrapper {
  max-width: 800px;
  margin: 0 auto;
}

.profile-header {
  background: white;
  border-radius: 20px;
  padding: 40px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  display: flex;
  align-items: center;
  gap: 24px;
}

.avatar-large {
  width: 96px;
  height: 96px;
  border-radius: 50%;
  background: linear-gradient(135deg, #d4a5d4 0%, #c9b8d4 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 32px;
  color: white;
  flex-shrink: 0;
  box-shadow: 0 4px 16px rgba(212, 165, 212, 0.3);
}

.header-text {
  flex: 1;
}

.username {
  margin: 0 0 4px 0;
  font-size: 32px;
  font-weight: 600;
  color: #4a5568;
}

.subtitle {
  margin: 0;
  color: #a0aec0;
  font-size: 16px;
}

.profile-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.info-section {
  background: white;
  border-radius: 16px;
  padding: 28px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.section-title {
  margin: 0 0 20px 0;
  font-size: 18px;
  font-weight: 600;
  color: #4a5568;
  padding-bottom: 12px;
  border-bottom: 2px solid #f0f0f0;
}

.info-card {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: linear-gradient(135deg, #fef9fa 0%, #f8f9fe 100%);
  border-radius: 12px;
  border: 1px solid #f0e8f0;
}

.info-label {
  font-weight: 500;
  color: #718096;
  font-size: 14px;
}

.info-value {
  font-weight: 500;
  color: #4a5568;
  font-size: 15px;
}

.roles-container {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.role-badge {
  background: linear-gradient(135deg, #d4e9f7 0%, #cfe2f3 100%);
  color: #2c5282;
  padding: 10px 20px;
  border-radius: 20px;
  font-weight: 500;
  font-size: 14px;
  box-shadow: 0 2px 6px rgba(168, 213, 226, 0.2);
}

.token-card {
  background: #f7fafc;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.token-display {
  flex: 1;
  font-family: 'Monaco', 'Menlo', 'Courier New', monospace;
  font-size: 13px;
  color: #4a5568;
  word-break: break-all;
  background: white;
  padding: 12px 16px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  min-width: 200px;
}

.btn-copy {
  background: linear-gradient(135deg, #a8d5e2 0%, #b9e4f5 100%);
  color: #2d3748;
  border: none;
  padding: 10px 20px;
  border-radius: 10px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
  box-shadow: 0 2px 6px rgba(168, 213, 226, 0.2);
  white-space: nowrap;
}

.btn-copy:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(168, 213, 226, 0.3);
}

.btn-copy:active {
  transform: translateY(0);
}

@media (max-width: 640px) {
  .profile-header {
    flex-direction: column;
    text-align: center;
    padding: 32px 24px;
  }

  .username {
    font-size: 24px;
  }

  .info-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .token-card {
    flex-direction: column;
  }

  .btn-copy {
    width: 100%;
  }
}
</style>