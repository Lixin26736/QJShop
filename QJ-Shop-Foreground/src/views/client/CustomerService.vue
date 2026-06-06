<template>
  <div class="cs-page">
    <van-nav-bar title="AI客服小Q" left-arrow @click-left="$router.back()" fixed placeholder />
    <div class="chat-area" ref="chatArea">
      <div v-if="messages.length === 0" class="welcome-area">
        <div class="welcome-avatar">🤖</div>
        <h3>你好，我是小Q</h3>
        <p>你的AI购物助手，可以帮你找商品、查订单、解答问题</p>
        <div class="quick-questions">
          <div v-for="q in quickQuestions" :key="q" class="quick-chip" @click="sendQuick(q)">{{ q }}</div>
        </div>
      </div>
      <div v-for="(msg, idx) in messages" :key="idx">
        <div class="msg-row" :class="msg.senderType === 0 ? 'user' : 'ai'">
          <div class="msg-avatar" v-if="msg.senderType !== 0">🤖</div>
          <div class="msg-body">
            <div class="msg-bubble" :class="msg.senderType === 0 ? 'user-bubble' : 'ai-bubble'">
              <div class="msg-text">{{ msg.content }}</div>
              <div class="msg-time">{{ formatTime(msg.createTime) }}</div>
            </div>
            <!-- 商品推荐卡片 -->
            <div v-if="msg.products && msg.products.length" class="product-recos">
              <div class="reco-label">📦 为您推荐以下商品：</div>
              <div class="reco-list">
                <div v-for="p in msg.products" :key="p.id" class="reco-card" @click="goProduct(p.id)">
                  <img :src="getImageUrl(p.image) || getPlaceholder(p.name, p.id, 120, 100)" class="reco-img"
                       @error="e => e.target.src = getPlaceholder(p.name, p.id, 120, 100)" />
                  <div class="reco-info">
                    <div class="reco-name">{{ p.name }}</div>
                    <div class="reco-price">¥{{ p.price }}</div>
                  </div>
                  <van-icon name="arrow" class="reco-arrow" />
                </div>
              </div>
            </div>
          </div>
          <div class="msg-avatar" v-if="msg.senderType === 0">👤</div>
        </div>
      </div>
      <div v-if="thinking" class="msg-row ai">
        <div class="msg-avatar">🤖</div>
        <div class="msg-body">
          <div class="msg-bubble ai-bubble thinking-bubble">
            <span class="dot">●</span><span class="dot">●</span><span class="dot">●</span>
          </div>
        </div>
      </div>
    </div>
    <div class="chat-input-bar">
      <van-field v-model="inputText" placeholder="输入问题，小Q帮你找好物..." @keypress.enter="sendMessage" :disabled="thinking" autocomplete="off">
        <template #button>
          <van-button size="small" type="primary" @click="sendMessage" :loading="thinking" round>发送</van-button>
        </template>
      </van-field>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { csApi } from '@/api/customerService'
import { getImageUrl, getPlaceholder } from '@/utils/image'
import { showToast } from 'vant'

const router = useRouter()
const messages = ref([])
const inputText = ref('')
const thinking = ref(false)
const chatArea = ref(null)

const quickQuestions = ['推荐一款手机', '有什么好用的护肤品', '最近有什么新品', '包邮吗', '如何退货']

const loadMessages = async () => {
  try { messages.value = (await csApi.getMessages()) || [] }
  catch (e) { /* 未登录时不加载 */ }
}

const sendQuick = (q) => { inputText.value = q; sendMessage() }

const sendMessage = async () => {
  const text = inputText.value.trim()
  if (!text || thinking.value) return
  inputText.value = ''
  thinking.value = true

  // 添加用户消息
  const userMsg = { id: Date.now(), content: text, senderType: 0, createTime: new Date().toISOString() }
  messages.value.push(userMsg)
  scrollToBottom()

  try {
    const res = await csApi.sendMessage(text)
    // 添加AI回复
    const aiMsg = {
      id: Date.now() + 1,
      content: res.reply || '已收到您的消息',
      senderType: 1,
      createTime: new Date().toISOString(),
      products: res.products || []
    }
    messages.value.push(aiMsg)
  } catch (e) {
    messages.value.push({
      id: Date.now() + 1,
      content: '抱歉，暂时无法连接客服。请稍后再试。',
      senderType: 1,
      createTime: new Date().toISOString(),
      products: []
    })
    if (e.message) showToast(e.message)
  } finally {
    thinking.value = false
    scrollToBottom()
  }
}

const goProduct = (id) => router.push(`/client/product/${id}`)

const scrollToBottom = async () => {
  await nextTick()
  if (chatArea.value) chatArea.value.scrollTop = chatArea.value.scrollHeight
}

const formatTime = (t) => {
  if (!t) return ''
  const d = new Date(t)
  return `${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

onMounted(() => loadMessages())
</script>

<style scoped>
.cs-page { display: flex; flex-direction: column; height: 100vh; background: var(--bg); }
.chat-area { flex: 1; overflow-y: auto; padding: 16px; }
.welcome-area { text-align: center; padding: 40px 20px; }
.welcome-avatar { font-size: 56px; margin-bottom: 12px; }
.welcome-area h3 { font-size: 20px; color: var(--text); margin-bottom: 8px; }
.welcome-area p { color: var(--text-secondary); font-size: 14px; margin-bottom: 24px; }
.quick-questions { display: flex; flex-wrap: wrap; gap: 10px; justify-content: center; }
.quick-chip {
  padding: 8px 18px; background: var(--bg-card); border: 1px solid var(--border);
  border-radius: 20px; cursor: pointer; font-size: 13px; color: var(--primary);
  transition: all 0.2s;
}
.quick-chip:hover { background: var(--primary); color: #fff; border-color: var(--primary); }
.msg-row { display: flex; align-items: flex-start; gap: 8px; margin-bottom: 20px; }
.msg-row.user { flex-direction: row-reverse; }
.msg-avatar { width: 34px; height: 34px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 18px; flex-shrink: 0; }
.msg-body { max-width: 78%; }
.msg-bubble { padding: 10px 14px; border-radius: 14px; font-size: 14px; line-height: 1.55; }
.user-bubble { background: var(--primary); color: #fff; border-bottom-right-radius: 4px; }
.ai-bubble { background: var(--bg-card); color: var(--text); border-bottom-left-radius: 4px; box-shadow: var(--shadow-sm); }
.msg-text { white-space: pre-wrap; word-break: break-word; }
.msg-time { font-size: 10px; opacity: 0.55; margin-top: 4px; text-align: right; }
.thinking-bubble { padding: 12px 20px; }
.thinking-bubble .dot { animation: blink 1.4s infinite; font-size: 8px; margin: 0 2px; color: var(--text-secondary); }
.thinking-bubble .dot:nth-child(2) { animation-delay: 0.2s; }
.thinking-bubble .dot:nth-child(3) { animation-delay: 0.4s; }
@keyframes blink { 0%,100% { opacity: 0.2; } 50% { opacity: 1; } }
.product-recos { margin-top: 8px; }
.reco-label { font-size: 12px; color: var(--text-secondary); margin-bottom: 8px; }
.reco-list { display: flex; flex-direction: column; gap: 6px; }
.reco-card {
  display: flex; align-items: center; gap: 10px; padding: 8px;
  background: var(--bg-card); border-radius: var(--radius);
  box-shadow: var(--shadow-sm); cursor: pointer;
  transition: transform 0.2s;
}
.reco-card:hover { transform: translateX(4px); }
.reco-card:active { transform: scale(0.98); }
.reco-img { width: 56px; height: 44px; border-radius: 6px; object-fit: cover; flex-shrink: 0; }
.reco-info { flex: 1; min-width: 0; }
.reco-name { font-size: 13px; color: var(--text); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.reco-price { font-size: 14px; font-weight: 700; color: var(--danger); margin-top: 2px; }
.reco-arrow { color: var(--text-secondary); flex-shrink: 0; }
.chat-input-bar { padding: 10px 12px; background: var(--bg-card); border-top: 1px solid var(--border); }
:deep(.chat-input-bar .van-field) { border-radius: 24px; background: var(--bg); padding: 0 12px; }
</style>
