package com.reyco.dasbx.gateway.core.sign;

public class DefaultSignature extends AbstractSignature {
    @Override
    protected String doSign(SigningContent content) throws SignatureException {
        try {
            StringBuilder source = new StringBuilder();
            // 按照“规范化请求”顺序拼接
            // 格式: Method|url|Timestamp|Nonce|Body
            // 1. 请求方法 (全大写)
            source.append(content.getMethod().toUpperCase()).append("|")

            // 2. 请求路径 (不含域名，如 /api/order/create)
            .append(content.getUrl()).append("|")
            
            // 3. 时间戳与随机数
            .append(content.getTimestamp()).append("|")
                  .append(content.getNonce());

            // 4. 请求体 (如果有)
            if (content.getBody() != null && !content.getBody().isEmpty()) {
                source.append("|").append(content.getBody());
            }
            // 5.执行哈希
            return new SimpleHash(content.getAlgorithm(), source.toString(), content.getSecret(), 1).toHex();
        } catch (Exception e) {
            throw new SignatureException("生成签名失败");
        }
    }
}