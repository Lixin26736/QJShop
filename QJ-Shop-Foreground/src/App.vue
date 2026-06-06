<template>
  <router-view />
</template>

<script setup>
</script>

<style>
:root {
  --primary: #2563eb;
  --primary-light: #3b82f6;
  --primary-dark: #1d4ed8;
  --secondary: #f59e0b;
  --danger: #ef4444;
  --success: #10b981;
  --bg: #f1f5f9;
  --bg-card: #ffffff;
  --text: #1e293b;
  --text-secondary: #64748b;
  --border: #e2e8f0;
  --shadow-sm: 0 1px 2px rgba(0,0,0,0.05);
  --shadow: 0 1px 3px rgba(0,0,0,0.1), 0 1px 2px rgba(0,0,0,0.06);
  --shadow-md: 0 4px 6px rgba(0,0,0,0.07), 0 2px 4px rgba(0,0,0,0.06);
  --shadow-lg: 0 10px 15px rgba(0,0,0,0.1), 0 4px 6px rgba(0,0,0,0.05);
  --radius: 8px;
  --radius-lg: 12px;
}

* { margin: 0; padding: 0; box-sizing: border-box; }

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'PingFang SC', 'Microsoft YaHei', sans-serif;
  background: var(--bg);
  color: var(--text);
  -webkit-font-smoothing: antialiased;
}

#app { width: 100%; min-height: 100vh; }

/* 全局滚动条美化 */
::-webkit-scrollbar { width: 6px; }
::-webkit-scrollbar-track { background: transparent; }
::-webkit-scrollbar-thumb { background: #cbd5e1; border-radius: 3px; }
::-webkit-scrollbar-thumb:hover { background: #94a3b8; }

/* 移动端管理后台表格优化 */
@media screen and (max-width: 768px) {
  .el-table { font-size: 12px !important; }
  .el-table .cell { padding: 6px 4px !important; }
  .el-table .el-button--small { padding: 4px 8px !important; font-size: 11px !important; }
  .el-form--inline .el-form-item { margin-right: 8px !important; margin-bottom: 4px !important; }
  .el-pagination { justify-content: center !important; font-size: 12px !important; }
  .el-pagination button, .el-pagination .el-pager li { min-width: 24px !important; height: 24px !important; }
  .el-dialog { width: 92% !important; }
  .el-drawer { width: 220px !important; }
}
</style>
