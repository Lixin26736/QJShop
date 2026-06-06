-- ==========================================
-- 新增商品 (8件)
-- ==========================================
INSERT INTO product (category_id, name, subtitle, main_image, detail_images, price, original_price, stock, sales, description, detail_content, status, is_hot, is_new, sort_order) VALUES
(8, '三星 Galaxy S24 Ultra', 'Galaxy AI 智能助手 钛金属框架', 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600', '["https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=1200"]', 9699.00, 10999.00, 60, 720, '三星旗舰AI手机,2亿像素摄像头', '<p>骁龙8Gen3处理器,5000mAh电池</p>', 1, 0, 1, 10),
(12, 'Bose QC45 降噪耳机', '主动降噪 24小时续航', 'https://images.unsplash.com/photo-1583394838336-acd977736f90?w=600', '["https://images.unsplash.com/photo-1583394838336-acd977736f90?w=1200"]', 1799.00, 2299.00, 85, 560, 'Bose QuietComfort 45头戴式无线降噪耳机', '<p>TriPort声学结构,通透模式切换</p>', 1, 1, 0, 11),
(24, '海蓝之谜 修护精萃液', '海洋焕活 深层修护', 'https://images.unsplash.com/photo-1571781926291-c477ebfd024b?w=600', '["https://images.unsplash.com/photo-1571781926291-c477ebfd024b?w=1200","https://images.unsplash.com/photo-1598440947619-2c35fc9aa908?w=1200"]', 1160.00, 1290.00, 40, 380, 'LA MER海蓝之谜修护精萃液150ml', '<p>Miracle Broth™浓缩精华</p>', 1, 1, 0, 12),
(25, '戴森 V15 Detect 无线吸尘器', '激光探测 智能灰尘感应', 'https://images.unsplash.com/photo-1558317374-067fb5f30001?w=600', '["https://images.unsplash.com/photo-1558317374-067fb5f30001?w=1200"]', 4990.00, 5490.00, 35, 420, '激光显尘 长达60分钟续航', '<p>Dyson Hyperdymium马达</p>', 1, 1, 1, 13),
(20, 'Calvin Klein 休闲西装', '修身版型 弹力面料', 'https://images.unsplash.com/photo-1593030761757-71fae45fa0e7?w=600', '["https://images.unsplash.com/photo-1593030761757-71fae45fa0e7?w=1200"]', 1599.00, 1999.00, 55, 280, 'CK修身休闲西装外套', '<p>88%聚酯纤维+12%氨纶</p>', 1, 0, 0, 14),
(13, '骆驼 户外冲锋衣', '防风防水透气 三合一', 'https://images.unsplash.com/photo-1604645537608-e0a4630956ce?w=600', '["https://images.unsplash.com/photo-1604645537608-e0a4630956ce?w=1200"]', 599.00, 899.00, 150, 890, '加绒内胆 可拆卸单穿', '<p>3层复合面料,暴雨级防水</p>', 1, 1, 1, 15),
(28, '茅台 飞天53度 500ml', '酱香突出 幽雅细腻', 'https://images.unsplash.com/photo-1584225064785-c62a8b43d148?w=600', '["https://images.unsplash.com/photo-1584225064785-c62a8b43d148?w=1200"]', 1499.00, 1499.00, 20, 1560, '贵州茅台酒 飞天53%vol 500ml', '<p>传统大曲酱香工艺,5年窖藏</p>', 1, 1, 0, 16),
(9, 'iPad Air M2', 'M2芯片 Liquid Retina屏', 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=600', '["https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=1200","https://images.unsplash.com/photo-1561154464-82e9adf32764?w=1200"]', 4399.00, 4799.00, 70, 650, '10.9英寸全面屏 支持Apple Pencil', '<p>M2芯片8核CPU+10核GPU</p>', 1, 0, 1, 17);

-- ==========================================
-- 新增商品规格
-- ==========================================
INSERT INTO product_spec (product_id, spec_name, spec_value, price, stock) VALUES
(1, '颜色', '原色钛金属', 9999.00, 30),
(1, '颜色', '蓝色钛金属', 9999.00, 35),
(1, '颜色', '白色钛金属', 9999.00, 35),
(1, '容量', '256GB', 9999.00, 40),
(1, '容量', '512GB', 11999.00, 30),
(1, '容量', '1TB', 13999.00, 30),
(11, '颜色', '白色', 1899.00, 100),
(11, '颜色', '黑色', 1899.00, 100),
(17, '尺码', '40', 999.00, 10),
(17, '尺码', '41', 999.00, 15),
(17, '尺码', '42', 999.00, 15),
(17, '尺码', '43', 999.00, 10),
(13, '尺码', 'M', 499.00, 80),
(13, '尺码', 'L', 499.00, 100),
(13, '尺码', 'XL', 499.00, 80),
(13, '尺码', 'XXL', 499.00, 40);

-- ==========================================
-- 新增订单
-- ==========================================
INSERT INTO order_info (order_no, user_id, address_id, total_amount, pay_amount, freight, pay_type, pay_time, delivery_time, receive_time, status, remark) VALUES
('ORD20240601001', 2, 1, 10298.00, 10298.00, 0.00, 1, '2024-06-01 10:30:00', '2024-06-01 15:00:00', '2024-06-03 14:20:00', 3, '送礼请包装好'),
('ORD20240601002', 3, 3, 349.00, 349.00, 10.00, 2, NULL, NULL, NULL, 0, NULL),
('ORD20240602001', 4, 5, 3999.00, 3989.00, 0.00, 1, '2024-06-02 09:15:00', NULL, NULL, 1, '尽快发货'),
('ORD20240602002', 2, 1, 1799.00, 1799.00, 0.00, 1, '2024-06-02 14:00:00', '2024-06-02 17:30:00', NULL, 2, NULL),
('ORD20240603001', 5, 5, 5198.00, 5198.00, 0.00, 2, '2024-06-03 11:00:00', '2024-06-03 16:00:00', '2024-06-05 10:00:00', 3, '已收到很好用'),
('ORD20240603002', 3, 3, 2299.00, 2299.00, 0.00, 1, '2024-06-03 20:30:00', NULL, NULL, 1, NULL);

INSERT INTO order_item (order_id, product_id, product_name, product_image, price, quantity, total_price) VALUES
(21, 1, 'iPhone 15 Pro Max', 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600', 9999.00, 1, 9999.00),
(21, 12, '索尼 WH-1000XM5', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600', 299.00, 1, 299.00),
(22, 14, 'Nike 运动卫衣', 'https://images.unsplash.com/photo-1572495641004-28421ae29fbc?w=600', 349.00, 1, 349.00),
(23, 101, '海尔 520L 十字对开门冰箱', 'https://images.unsplash.com/photo-1571175443880-49e1d25b2bc5?w=600', 3999.00, 1, 3999.00),
(24, 105, '新秀丽 20英寸 登机箱', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600', 699.00, 1, 699.00),
(24, 110, '安踏 氮科技专业跑鞋', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600', 499.00, 1, 499.00),
(24, 11, 'AirPods Pro 2', 'https://images.unsplash.com/photo-1606841837239-c5a1a4a07af7?w=600', 1899.00, 1, 1899.00),
(25, 3, '小米14 Pro', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600', 4999.00, 1, 4999.00),
(25, 13, '优衣库 高级轻型羽绒服', 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=600', 199.00, 1, 199.00),
(26, 12, '索尼 WH-1000XM5', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600', 2299.00, 1, 2299.00);

-- ==========================================
-- 新增评价
-- ==========================================
INSERT INTO review (product_id, user_id, order_id, rating, content, images, reply, reply_time, status) VALUES
(1, 2, 21, 5, '手感很好,钛金属边框质感一流,拍照效果惊艳!', '["https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=700"]', '感谢您的认可!', '2024-06-04 10:00:00', 1),
(3, 5, 25, 5, '小米性价比很高,徕卡拍照确实不错,系统流畅', NULL, NULL, NULL, 1),
(11, 2, 24, 4, '降噪效果很棒,佩戴舒适,就是价格略贵', NULL, 'AirPods Pro确实物有所值哦', '2024-06-05 14:00:00', 1),
(13, 5, 25, 5, '轻薄保暖,冬天穿非常合适,收纳也方便', '["https://images.unsplash.com/photo-1551028719-00167b16eac5?w=700"]', NULL, NULL, 1),
(14, 3, 22, 3, '质量还可以,但颜色和图片有点色差', NULL, '非常抱歉,可联系客服处理', '2024-06-03 09:00:00', 1),
(101, 4, 23, 5, '冰箱空间大,制冷快,一级能效很省电', '["https://images.unsplash.com/photo-1571175443880-49e1d25b2bc5?w=700"]', NULL, NULL, 1),
(15, 2, NULL, 4, '款式还不错,面料柔软亲肤', NULL, NULL, NULL, 1),
(17, 3, NULL, 5, '经典AJ1,上脚很好看,配什么裤子都行', '["https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=700"]', NULL, NULL, 1);

-- ==========================================
-- 新增收货地址
-- ==========================================
INSERT INTO address (user_id, receiver_name, receiver_phone, province, city, district, detail_address, is_default) VALUES
(2, '张三', '13800138001', '广东省', '深圳市', '南山区', '科技园路1号创新大厦A座1201', 1),
(2, '张三', '13800138001', '广东省', '广州市', '天河区', '体育西路100号', 0),
(3, '李四', '13800138002', '浙江省', '杭州市', '西湖区', '文三路138号', 1),
(4, '王五', '13800138003', '北京市', '北京市', '朝阳区', '建国路88号SOHO现代城B座2003', 1),
(5, '赵六', '13800138004', '上海市', '上海市', '浦东新区', '张江高科技园区碧波路690号', 1);
