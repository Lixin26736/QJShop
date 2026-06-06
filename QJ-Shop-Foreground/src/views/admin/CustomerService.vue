<template>
  <div class="admin-cs">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>客服消息</span>
          <el-badge :value="unreadCount" :hidden="!unreadCount">
            <el-button type="primary" @click="loadUnread">刷新未读</el-button>
          </el-badge>
        </div>
      </template>
      <div v-if="selectedUserId" class="chat-box">
        <div class="chat-header">
          <el-button size="small" @click="selectedUserId = null">← 返回列表</el-button>
          <span>用户ID: {{ selectedUserId }}</span>
        </div>
        <div class="chat-messages">
          <div v-for="msg in chatMessages" :key="msg.id" class="msg-item" :class="msg.senderType === 0 ? 'user' : 'admin'">
            <div class="msg-bubble">{{ msg.content }}</div>
            <div class="msg-time">{{ msg.createTime }}</div>
          </div>
        </div>
        <div class="chat-input-area">
          <el-input v-model="replyInput" placeholder="输入回复..." @keypress.enter="sendReply">
            <template #append><el-button @click="sendReply">发送</el-button></template>
          </el-input>
        </div>
      </div>
      <div v-else>
        <el-table :data="unreadMessages" style="width:100%">
          <el-table-column prop="userId" label="用户ID" width="80" />
          <el-table-column prop="content" label="最新消息" min-width="300" />
          <el-table-column prop="createTime" label="时间" width="160" />
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button size="small" @click="openChat(row.userId)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { csApi } from '@/api/customerService'

const unreadMessages = ref([])
const unreadCount = ref(0)
const selectedUserId = ref(null)
const chatMessages = ref([])
const replyInput = ref('')

const loadUnread = async () => {
  try {
    unreadMessages.value = await csApi.getUnread()
    unreadCount.value = unreadMessages.value.length
  } catch (e) { /* ignore */ }
}

const openChat = async (userId) => {
  selectedUserId.value = userId
  try {
    chatMessages.value = await csApi.getMessagesByUser(userId)
  } catch (e) { /* ignore */ }
}

const sendReply = async () => {
  if (!replyInput.value.trim()) return
  try {
    await csApi.reply(selectedUserId.value, replyInput.value)
    // 添加本地显示
    chatMessages.value.push({
      id: Date.now(), userId: selectedUserId.value, content: replyInput.value, senderType: 1, createTime: new Date().toISOString()
    })
    replyInput.value = ''
    ElMessage.success('已发送')
  } catch (e) { ElMessage.error('发送失败') }
}

loadUnread()
</script>

<style scoped>
.admin-cs { padding: 24px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.chat-box { display: flex; flex-direction: column; height: 62vh; }
.chat-header { padding-bottom: 12px; border-bottom: 1px solid var(--border); margin-bottom: 8px; }
.chat-messages { flex: 1; overflow-y: auto; padding: 12px; }
.msg-item { margin-bottom: 16px; overflow: hidden; }
.msg-item.user { text-align: left; }
.msg-item.admin { text-align: right; }
.msg-bubble { max-width: 72%; padding: 10px 16px; border-radius: 12px; display: inline-block; text-align: left; font-size: 14px; line-height: 1.5; }
.msg-item.user .msg-bubble { background: #eff6ff; border: 1px solid #bfdbfe; }
.msg-item.admin .msg-bubble { background: var(--primary); color: #fff; }
.msg-time { font-size: 11px; color: var(--text-secondary); margin-top: 4px; }
.chat-input-area { margin-top: 12px; padding-top: 12px; border-top: 1px solid var(--border); }
</style>
