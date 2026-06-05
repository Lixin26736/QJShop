/**
 * 环境配置工具类
 * 用于管理不同环境下的API地址配置
 */

// 环境类型
const ENV_TYPE = {
  DEVELOPMENT: 'development',  // 本地开发
  EMULATOR: 'emulator',        // 模拟器
  REAL_DEVICE: 'real_device'   // 真机
}

// 当前环境 - 根据需要修改这个值
const CURRENT_ENV = ENV_TYPE.REAL_DEVICE

// 不同环境的配置
const ENV_CONFIG = {
  [ENV_TYPE.DEVELOPMENT]: {
    // 本地开发环境 - 使用localhost
    API_BASE_URL: 'http://localhost:3000',
    BACKEND_URL: 'http://localhost:8080'
  },
  [ENV_TYPE.EMULATOR]: {
    // 模拟器环境 - 使用VirtualBox Host-Only网络IP
    API_BASE_URL: 'http://192.168.56.1:3000',
    BACKEND_URL: 'http://192.168.56.1:8080'
  },
  [ENV_TYPE.REAL_DEVICE]: {
    // 真机环境 - 使用真实局域网IP
    // 请根据你的实际IP地址修改这里
    API_BASE_URL: 'http://192.168.3.191:3000',
    BACKEND_URL: 'http://192.168.3.191:8080'
  }
}

/**
 * 获取当前环境的API基础URL
 * @returns {string} API基础URL
 */
export function getApiBaseUrl() {
  return ENV_CONFIG[CURRENT_ENV].API_BASE_URL
}

/**
 * 获取当前环境的后端URL
 * @returns {string} 后端URL
 */
export function getBackendUrl() {
  return ENV_CONFIG[CURRENT_ENV].BACKEND_URL
}

/**
 * 获取当前环境类型
 * @returns {string} 环境类型
 */
export function getCurrentEnv() {
  return CURRENT_ENV
}

/**
 * 判断是否为开发环境
 * @returns {boolean}
 */
export function isDevelopment() {
  return CURRENT_ENV === ENV_TYPE.DEVELOPMENT
}

/**
 * 判断是否为模拟器环境
 * @returns {boolean}
 */
export function isEmulator() {
  return CURRENT_ENV === ENV_TYPE.EMULATOR
}

/**
 * 判断是否为真机环境
 * @returns {boolean}
 */
export function isRealDevice() {
  return CURRENT_ENV === ENV_TYPE.REAL_DEVICE
}

// 导出所有配置
export default {
  ENV_TYPE,
  CURRENT_ENV,
  ENV_CONFIG,
  getApiBaseUrl,
  getBackendUrl,
  getCurrentEnv,
  isDevelopment,
  isEmulator,
  isRealDevice
}
