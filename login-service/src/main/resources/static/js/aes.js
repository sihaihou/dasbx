/**
 * AES对称加密
 * 
 * @param {Object} source 明文
 * @param {Object} key Base64格式的密钥
 * @returns {string} Base64格式密文
 */
const encryptAes = (content, aesKey) => {
	// 1. 解码Base64得到32字节的密钥
	const realKey = CryptoJS.enc.Base64.parse(aesKey);
	// ECB模式加密
	const encrypted = CryptoJS.AES.encrypt(content, realKey, {
		mode : CryptoJS.mode.ECB, // AES加密模式
		padding : CryptoJS.pad.Pkcs7
	// 填充方式
	})
	return encrypted.toString(); // 返回base64格式密文
}
/**
 * AES对称解密
 * @param {String} ciphertext Base64格式的密文
 * @param {String} key Base64格式的密钥
 * @returns {string} 明文字符串
 */
const decryptAes = (ciphertext, aesKey) => {
	// 1. 解码Base64得到32字节的密钥
	const realKey = CryptoJS.enc.Base64.parse(aesKey);
	// base64格式密文转换
	const base64 = CryptoJS.enc.Base64.parse(ciphertext)
	// 解密
	const decryptedData = CryptoJS.AES.decrypt(base64, realKey, {
		mode : CryptoJS.mode.ECB, // AES加密模式
		padding : CryptoJS.pad.Pkcs7
	// 填充方式
	})
	return CryptoJS.enc.Utf8.stringify(decryptedData).toString() // 返回明文
}

/**
 * AES对称加密 （CBC模式，需要偏移量）
 * 
 * @param {String} source 明文参数
 * @param {String} aesKey Base64格式的密匙
 * @returns {string} Base64格式密文
 */
const encryptAesCBC = (content, aesKey) => {
	// 1. 解码Base64得到32字节的密钥
	const realKey = CryptoJS.enc.Base64.parse(aesKey);
	
	// 2. 生成随机16字节IV
    const iv = CryptoJS.lib.WordArray.random(16);
    // 3. UTF-8编码明文
    const plainText = CryptoJS.enc.Utf8.parse(content)
    
	const realIv = CryptoJS.enc.Base64.parse(iv);
	// 4. AES-CBC加密
	const encrypted = CryptoJS.AES.encrypt(plainText, realKey, {
		iv : iv,
		mode : CryptoJS.mode.CBC, // AES加密模式
		padding : CryptoJS.pad.Pkcs7
	})
	// 5. 获取密文字节（WordArray格式）
    const ciphertext = encrypted.ciphertext;
    
    // 6. 拼接IV + 密文（与后端逻辑一致）
    const combined = iv.concat(ciphertext);
    
    // 7. 返回Base64格式
    return combined.toString(CryptoJS.enc.Base64);
}
/**
 * AES对称解密 （CBC模式，需要偏移量）
 * @param {String} ciphertext Base64格式的密文
 * @param {String} aesKey Base64格式的密钥
 * @returns {string} 明文字符串
 */
const decryptAesCBC = (combinedBase64, aesKey) => {
	// 1. 解码Base64密钥
	const realKey = CryptoJS.enc.Base64.parse(aesKey);
	// 2. 解码Base64，获取combined字节数组
    const combined = CryptoJS.enc.Base64.parse(combinedBase64);
    
    // 3. 提取前16字节作为IV
    const iv = CryptoJS.lib.WordArray.create(combined.words.slice(0, 4), 16);
    // 4. 提取剩余部分作为密文
    const ciphertext = CryptoJS.lib.WordArray.create(
        combined.words.slice(4), 
        combined.sigBytes - 16
    );
	// 5.解密
	const decrypted = CryptoJS.AES.decrypt(ciphertext, realKey, {
		iv : iv,
		mode : CryptoJS.mode.CBC, // AES加密模式
		padding : CryptoJS.pad.Pkcs7
	// 填充方式
	})
	// 6. 转换为UTF-8字符串
	return decrypted.toString(CryptoJS.enc.Utf8); // 返回明文
}
