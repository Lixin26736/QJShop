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
            systemPrompt.append("你是QJ商城的AI客服助手，名字叫\"小Q,工号26793\"。你需要用中文回复用户。\n");
            systemPrompt.append("你的职责：解答购物问题、推荐商品、提供订单帮助。\n");
            systemPrompt.append("【风格要求】回复必须自然口语化，像真人客服聊天。\n");
            systemPrompt.append("禁止使用以下符号：* - # ` ``` ~~ ** __ 以及任何Markdown格式。\n");
            systemPrompt.append("不要使用列表序号1. 2. 3.，用换行分段即可。商品信息用【】包裹。\n");

            if (recommendProducts != null && !recommendProducts.isEmpty()) {
                systemPrompt.append("\n以下是可以推荐给用户的商品（已上架有库存）：\n");
                for (int i = 0; i < Math.min(recommendProducts.size(), 10); i++) {
                    Product p = recommendProducts.get(i);
                    systemPrompt.append(String.format("- ID:%d, 名称:%s, 价格:¥%s, 描述:%s\n",
                            p.getId(), p.getName(), p.getPrice(),
                            p.getSubtitle() != null ? p.getSubtitle() : ""));
                }
                systemPrompt.append("\n【重要规则】你必须根据用户的需求从上述列表中挑选1-3个最相关的商品进行推荐。");
                systemPrompt.append("用户说的可能是泛指(如\"饮料\"\"手机\")，你要智能匹配相关商品。");
                systemPrompt.append("你的回复末尾必须包含 [RECOMMEND]商品ID[/RECOMMEND] 标记，多个ID逗号分隔，最多3个。");
                systemPrompt.append("示例回复：\"为您推荐以下饮料~\\n【元气森林气泡水】清爽解腻 ¥59.90\\n【智利进口车厘子】JJ级 ¥199.00 [RECOMMEND]108,106[/RECOMMEND]\"\n");
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

                    // 组装推荐商品详情(最多3个)
                    List<Map<String, Object>> productCards = new ArrayList<>();
                    int recCount = 0;
                    for (Long id : recommendedIds) {
                        if (recCount >= 3) break;
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
                                recCount++;
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
