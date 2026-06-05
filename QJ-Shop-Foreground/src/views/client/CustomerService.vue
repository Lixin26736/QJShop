<template>
  <div class="cs-page">
    <van-nav-bar title="AI客服" left-arrow @click-left="$router.back()" />

    <div class="chat-area" ref="chatArea">
      <div v-for="(msg, idx) in messages" :key="idx" class="chat-item" :class="msg.senderType === 0 ? 'user' : 'admin'">
        <div class="chat-bubble">
          <div class="chat-content">{{ msg.content }}</div>
          <img v-if="msg.image" :src="getImage(msg.image)" class="chat-image" />
          <div class="chat-time">{{ formatTime(msg.createTime) }}</div>
        </div>
      </div>
      <div v-if="messages.length === 0" class="welcome">
        <p>你好！我是AI客服助手</p>
        <p>有什么可以帮你的吗？</p>
        <div class="quick-questions">
          <van-button size="small" plain v-for="q in quickQuestions" :key="q" @click="sendQuick(q)">{{ q }}</van-button>
        </div>
      </div>
    </div>

    <div class="chat-input">
      <van-field v-model="inputText" placeholder="输入问题..." @keypress.enter="sendMessage">
        <template #button>
          <van-button size="small" type="primary" @click="sendMessage">发送</van-button>
        </template>
      </van-field>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { csApi } from '@/api/customerService'
import { uploadApi } from '@/api/upload'
import { showToast } from 'vant'

const messages = ref([])
const inputText = ref('')
const chatArea = ref(null)

const quickQuestions = ['订单什么时候发货？', '如何退货？', '运费是多少？', '怎么联系人工客服？']

// AI 预设回复
const aiReplies = {
  '订单什么时候发货': '一般下单后1-3个工作日内发货，节假日顺延。',
  '如何退货': '在订单详情页面点击"申请退款"，审核通过后寄回商品即可退款。',
  '运费是多少': '全场满99元包邮，不满99元运费10元。偏远地区另计。',
  '怎么联系人工客服': '请在工作时间(9:00-18:00)拨打客服热线: 400-xxx-xxxx',
  '退货': '请在订单详情页面点击"申请退款"，审核通过后寄回商品即可退款。',
  '发货': '一般下单后1-3个工作日内发货，节假日顺延。',
  '退款': '退款将在收到退回商品后1-3个工作日内退回原支付方式。'
}

const findAIReply = (text) => {
  for (const [keyword, reply] of Object.entries(aiReplies)) {
    if (text.includes(keyword)) return reply
  }
  return '您好，我已收到您的问题，会尽快为您处理。如需人工服务，请在工作时间拨打客服热线。感谢您的理解！'
}

const loadMessages = async () => {
  try {
    messages.value = await csApi.getMessages()
  } catch (e) { /* ignore */ }
}

const sendQuick = (q) => {
  inputText.value = q
  sendMessage()
}

const sendMessage = async () => {
  const text = inputText.value.trim()
  if (!text) return
  inputText.value = ''

  // 添加用户消息
  try {
    const userMsg = await csApi.sendMessage(text)
    messages.value.push(userMsg)
    scrollToBottom()

    // AI自动回复
    setTimeout(() => {
      const aiReply = findAIReply(text)
      const aiMsg = {
        id: Date.now(),
        content: aiReply,
        senderType: 1,
        createTime: new Date().toISOString()
      }
      messages.value.push(aiMsg)
      scrollToBottom()
    }, 800)
  } catch (e) {
    showToast('发送失败')
  }
}

const scrollToBottom = async () => {
  await nextTick()
  if (chatArea.value) {
    chatArea.value.scrollTop = chatArea.value.scrollHeight
  }
}

const getImage = (img) => uploadApi.getImageUrl(img)

const formatTime = (t) => {
  if (!t) return ''
  const d = new Date(t)
  return `${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

onMounted(() => loadMessages())
</script>

<style scoped>
.cs-page { display: flex; flex-direction: column; height: 100vh; background: #f7f8fa; }
.chat-area { flex: 1; overflow-y: auto; padding: 15px; }
.chat-item { margin-bottom: 15px; display: flex; }
.chat-item.user { justify-content: flex-end; }
.chat-item.admin { justify-content: flex-start; }
.chat-bubble { max-width: 75%; padding: 10px 14px; border-radius: 8px; font-size: 14px; line-height: 1.5; }
.chat-item.user .chat-bubble { background: #1989fa; color: #fff; }
.chat-item.admin .chat-bubble { background: #fff; color: #333; box-shadow: 0 1px 2px rgba(0,0,0,0.1); }
.chat-image { max-width: 200px; margin-top: 5px; border-radius: 4px; }
.chat-time { font-size: 10px; opacity: 0.6; margin-top: 4px; }
.welcome { text-align: center; padding: 40px 20px; color: #666; }
.quick-questions { margin-top: 20px; display: flex; flex-wrap: wrap; gap: 8px; justify-content: center; }
.chat-input { border-top: 1px solid #eee; background: #fff; padding: 8px; }
</style>
