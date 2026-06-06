package cn.mikuyun.qjshopbackend.service;

import cn.mikuyun.qjshopbackend.entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DeepSeekService {

    @Value("${deepseek.api-key}")
    private String apiKey;

    @Value("${deepseek.api-url}")
    private String apiUrl;

    @Value("${deepseek.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 调用DeepSeek AI进行客服对话+商品推荐
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> chat(String userMessage, List<Product> recommendProducts, List<Map<String, Object>> history) {
        try {
            // 构建系统提示词
            StringBuilder systemPrompt = new StringBuilder();
            systemPrompt.append("你是QJ商城的AI客服助手，名字叫\"小Q\"。你需要用中文回复用户。\n");
            systemPrompt.append("你的职责：解答购物问题、推荐商品、提供订单帮助。\n");

            if (recommendProducts != null && !recommendProducts.isEmpty()) {
                systemPrompt.append("\n以下是可以推荐给用户的商品（来自数据库，已上架且有库存）：\n");
                for (int i = 0; i < Math.min(recommendProducts.size(), 10); i++) {
                    Product p = recommendProducts.get(i);
                    systemPrompt.append(String.format("- ID:%d, 名称:%s, 价格:¥%s",
                            p.getId(), p.getName(), p.getPrice()));
                    if (p.getSubtitle() != null && !p.getSubtitle().isEmpty()) {
                        systemPrompt.append(", 描述:").append(p.getSubtitle());
                    }
                    systemPrompt.append("\n");
                }
                systemPrompt.append("\n如果用户询问商品相关问题，请推荐上述匹配的商品。回复格式要求：\n");
                systemPrompt.append("1. 先给出自然友好的文字回复\n");
                systemPrompt.append("2. 如果需要推荐商品，在回复末尾添加 [RECOMMEND]商品ID列表[/RECOMMEND] 标记\n");
                systemPrompt.append("3. 例如：[RECOMMEND]101,102,103[/RECOMMEND]\n");
            }

            // 构建消息列表
            List<Map<String, Object>> messages = new ArrayList<>();

            Map<String, Object> systemMsg = new LinkedHashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", systemPrompt.toString());
            messages.add(systemMsg);

            if (history != null) {
                for (Map<String, Object> h : history) {
                    Map<String, Object> histMsg = new LinkedHashMap<>();
                    histMsg.put("role", h.get("role"));
                    histMsg.put("content", h.get("content"));
                    messages.add(histMsg);
                }
            }

            Map<String, Object> userMsg = new LinkedHashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);
            messages.add(userMsg);

            // 构建请求body
            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 800);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, entity, String.class);

            if (response.getBody() != null) {
                Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);
                List<Map<String, Object>> choices = (List<Map<String, Object>>) result.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String content = (String) message.get("content");

                    // 解析推荐商品ID
                    List<Long> recommendedIds = new ArrayList<>();
                    String replyText = content;
                    if (content.contains("[RECOMMEND]") && content.contains("[/RECOMMEND]")) {
                        int start = content.indexOf("[RECOMMEND]") + 11;
                        int end = content.indexOf("[/RECOMMEND]");
                        String idsStr = content.substring(start, end).trim();
                        replyText = content.substring(0, content.indexOf("[RECOMMEND]")).trim();

                        for (String idStr : idsStr.split(",")) {
                            try {
                                recommendedIds.add(Long.parseLong(idStr.trim()));
                            } catch (NumberFormatException ignored) {}
                        }
                    }

                    // 组装推荐商品详情
                    List<Map<String, Object>> productCards = new ArrayList<>();
                    for (Long id : recommendedIds) {
                        for (Product p : recommendProducts) {
                            if (p.getId().equals(id)) {
                                Map<String, Object> card = new LinkedHashMap<>();
                                card.put("id", p.getId());
                                card.put("name", p.getName());
                                card.put("price", p.getPrice());
                                card.put("image", p.getMainImage());
                                card.put("subtitle", p.getSubtitle());
                                card.put("link", "/client/product/" + p.getId());
                                productCards.add(card);
                                break;
                            }
                        }
                    }

                    Map<String, Object> output = new LinkedHashMap<>();
                    output.put("reply", replyText);
                    output.put("products", productCards);
                    return output;
                }
            }
        } catch (Exception e) {
            // AI 调用失败时返回预设回复
            Map<String, Object> fallback = new LinkedHashMap<>();
            fallback.put("reply", getFallbackReply(userMessage));
            fallback.put("products", Collections.emptyList());
            return fallback;
        }
        Map<String, Object> fallback = new LinkedHashMap<>();
        fallback.put("reply", "抱歉，我暂时无法处理您的请求，请稍后再试。");
        fallback.put("products", Collections.emptyList());
        return fallback;
    }

    private String getFallbackReply(String message) {
        if (message.contains("订单") || message.contains("发货")) {
            return "一般下单后1-3个工作日内发货。您可以在\"我的订单\"中查看订单状态。";
        }
        if (message.contains("退货") || message.contains("退款")) {
            return "在订单详情页面可以申请退款，审核通过后寄回商品即可。退款将在1-3个工作日退回。";
        }
        if (message.contains("运费") || message.contains("包邮")) {
            return "全场满99元包邮，不满99元运费10元。";
        }
        return "您好，我是小Q客服。请问有什么可以帮您的？您可以问我关于商品、订单、发货等问题。";
    }
}
