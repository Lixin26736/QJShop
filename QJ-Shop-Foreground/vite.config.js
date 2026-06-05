import {fileURLToPath, URL} from 'node:url'
import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
                              plugins: [
                                vue(),
                                vueDevTools(),
                              ],
                              resolve: {
                                alias: {
                                  '@': fileURLToPath(new URL('./src', import.meta.url))
                                },
                              },
                              server: {
                                host: '0.0.0.0', // 允许外部访问
                                port: 3000,
                                proxy: {
                                  '/api': {
                                    target: 'http://localhost:8080',
                                    changeOrigin: true,
                                    rewrite: (path) => path.replace(/^\/api/, '')
                                  }
                                }
                              },
                              build: {
                                minify: 'terser',
                                terserOptions: {
                                  compress: {
                                    drop_console: true,
                                    drop_debugger: true
                                  }
                                },
                                rollupOptions: {
                                  output: {
                                    // 将 manualChunks 改为函数形式
                                    manualChunks(id) {
                                      // element-plus 相关
                                      if (id.includes('element-plus')) {
                                        return 'element-plus'
                                      }
                                      // vant 相关
                                      if (id.includes('vant')) {
                                        return 'vant'
                                      }
                                      // vue 核心库
                                      if (id.includes('vue') || id.includes('vue-router') || id.includes('pinia')) {
                                        return 'vue-vendor'
                                      }
                                      // 其他 node_modules 依赖
                                      if (id.includes('node_modules')) {
                                        return 'vendor'
                                      }
                                    }
                                  }
                                },
                                chunkSizeWarningLimit: 1000
                              },
                              optimizeDeps: {
                                include: ['vue', 'vue-router', 'pinia', 'axios', 'element-plus', 'vant']
                              }
                            })