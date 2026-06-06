package cn.mikuyun.qjshopbackend.controller.user;

import cn.mikuyun.qjshopbackend.common.ApiResponse;
import cn.mikuyun.qjshopbackend.entity.CustomerService;
import cn.mikuyun.qjshopbackend.entity.Product;
import cn.mikuyun.qjshopbackend.mapper.ProductMapper;
import cn.mikuyun.qjshopbackend.service.CustomerServiceService;
import cn.mikuyun.qjshopbackend.service.DeepSeekService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/user/cs")
@RequiredArgsConstructor
public class CustomerServiceClientController {

    private final CustomerServiceService customerServiceService;
    private final DeepSeekService deepSeekService;
    private final ProductMapper productMapper;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/messages")
    public ApiResponse<List<CustomerService>> listMessages(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success(customerServiceService.listByUserId(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/send")
    public ApiResponse<Map<String, Object>> sendMessage(@RequestBody Map<String, Object> body,
                                                         Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        String content = body.get("content") != null ? body.get("content").toString() : "";
        String image = body.get("image") != null ? body.get("image").toString() : null;

        // 1. 保存用户消息
        CustomerService userMsg = new CustomerService();
        userMsg.setUserId(userId);
        userMsg.setContent(content);
        userMsg.setImage(image);
        userMsg.setSenderType(0);
        customerServiceService.sendMessage(userMsg);

        // 2. 搜索匹配的商品（已上架+有库存）
        List<Product> matchedProducts = searchProducts(content);

        // 3. 构建对话历史
        List<CustomerService> history = customerServiceService.listByUserId(userId);
        List<Map<String, Object>> chatHistory = new ArrayList<>();
        int recentCount = Math.min(history.size(), 10);
        for (int i = history.size() - recentCount; i < history.size(); i++) {
            CustomerService h = history.get(i);
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("role", h.getSenderType() == 0 ? "user" : "assistant");
            entry.put("content", h.getContent());
            chatHistory.add(entry);
        }

        // 4. 调用 DeepSeek AI
        Map<String, Object> aiResult = deepSeekService.chat(content, matchedProducts, chatHistory);

        // 5. 保存AI回复
        String reply = aiResult.get("reply") != null ? aiResult.get("reply").toString() : "";
        CustomerService aiMsg = new CustomerService();
        aiMsg.setUserId(userId);
        aiMsg.setContent(reply);
        aiMsg.setSenderType(1); // AI回复
        aiMsg.setIsRead(1);
        customerServiceService.sendMessage(aiMsg);

        // 6. 返回结果
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("userMessage", userMsg);
        result.put("reply", reply);
        result.put("products", aiResult.getOrDefault("products", Collections.emptyList()));
        return ApiResponse.success(result);
    }

    /**
     * 根据用户消息关键词搜索商品
     */
    // 关联词映射: 常见品类词 → DB中可能匹配的关键词
    private static final Map<String, String[]> RELATED_WORDS = new LinkedHashMap<>();
    static {
        RELATED_WORDS.put("饮料", new String[]{"气泡水","可乐","雪碧","橙汁","果汁","水","茶","咖啡","奶","饮"});
        RELATED_WORDS.put("手机", new String[]{"iPhone","华为","小米","OPPO","vivo","三星","手机"});
        RELATED_WORDS.put("电脑", new String[]{"MacBook","ThinkPad","MateBook","笔记本","电脑"});
        RELATED_WORDS.put("耳机", new String[]{"AirPods","Bose","索尼","耳机","降噪"});
        RELATED_WORDS.put("零食", new String[]{"坚果","车厘子","零食","食品"});
        RELATED_WORDS.put("衣服", new String[]{"羽绒","卫衣","大衣","连衣裙","西装","冲锋衣","衣服","CK","优衣库","Nike","ZARA"});
        RELATED_WORDS.put("鞋", new String[]{"Air","Ultraboost","跑鞋","乔丹","鞋"});
        RELATED_WORDS.put("护肤", new String[]{"SK-II","雅诗兰黛","海蓝之谜","神仙水","小棕瓶","护肤","精华"});
        RELATED_WORDS.put("家电", new String[]{"冰箱","洗衣机","空调","电视","戴森","吸尘器"});
        RELATED_WORDS.put("酒", new String[]{"茅台","酒"});
        RELATED_WORDS.put("手表", new String[]{"Apple Watch","华为 Watch","手表"});
    }

    private List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return Collections.emptyList();

        // 提取关键词 + 关联词扩展
        Set<String> searchWords = new LinkedHashSet<>();
        String[] words = keyword.replaceAll("[，,。.!！？?\\s]+", " ").split(" ");
        for (String w : words) {
            if (w.length() >= 1) searchWords.add(w);
        }
        // 扩展关联词
        Set<String> expandedWords = new LinkedHashSet<>(searchWords);
        for (String w : searchWords) {
            for (Map.Entry<String, String[]> entry : RELATED_WORDS.entrySet()) {
                if (w.contains(entry.getKey()) || entry.getKey().contains(w)) {
                    expandedWords.addAll(Arrays.asList(entry.getValue()));
                }
            }
        }

        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1); // 上架
        wrapper.gt(Product::getStock, 0);  // 有库存
        wrapper.and(w -> {
            boolean first = true;
            for (String word : expandedWords) {
                if (word.length() >= 1 && !first) {
                    w.or();
                }
                if (word.length() >= 1) {
                    w.like(Product::getName, word)
                     .or().like(Product::getSubtitle, word)
                     .or().like(Product::getDescription, word);
                    first = false;
                }
            }
        });
        wrapper.orderByDesc(Product::getSales);
        wrapper.last("limit 10");
        return productMapper.selectList(wrapper);
    }
}
