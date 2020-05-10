package com.hq.ecmp.ms.api.conf;

/**
 * @author ghb
 * @description 支付宝支付配置类
 * @date 2020/5/5
 */
public class AlipayConfig {

        // 1.商户appid,使用商户自己的appid即可
        public static String APPID = "2021001160623612";

        //2.私钥 pkcs8格式的，与在支付宝存储的公钥对应
        public static String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCUDSC1DnE0s8Rjhd5LJWsv8jVre7/ZG1YOqSFrmuia9qTmi9+rJOML6DaygdeZ8DJTKnUz5MSmLfl2r6WaMSB3kXYaHk4/XDt8w5MJWtg1BXgxGA2MChHaUzYvzmuQ2Ru7cGTNCU5QGgoGpbBF77RBp8zZeLdwNsCre8QEsmgi57d2i6d6A5ZUd7jZufhclxdhNjlcSJUXXnmgTF/raXl4X7EHUAB3XPY4SQIs1i5DmYmYK62q+WFt+POz2DKVEemAVnb0Je1VsVRoOkKeJ9Av0M+k35ql8ZNDXWEP057/eX+OrkeKJ+53nCHa03HxGK24sV/MiF+zdPIXLGslUfYbAgMBAAECggEAJYniliXQTGWcUQO7ZmBZejG2m8cn0LueqV7261q+ybPqAc7LapCSxQ0e+Un0ycJecDnet8a0mdIkY5SLr7nvGoIWaO7wNh6DstG/8WpIUcFORVxs6uoOtsXMgnibS4HRB0irNlW6tpKzohXWyRTLJq6OQUHbeSdY2OIrxGkcAGVPXZGUfmgDSPQphXGTcEUAG8eYvyBVXGlMq3ehOCqgIlV2QKd+eHe/atdDf/7zTwnLLD/4BIONC2nmN3hvcSTiTytTtCHThsYoTm7JJtfEB6+HC+n4608ZtEWSFMcoHBW9HDN5LdFU5krQTg7uozdnK/vZnUlI40+9g8FUAop6kQKBgQDR1jzC/iLF0SnRLjuI13RSFdUxQdNm9kTTRL0M+Lk0dxGgjpxY9xL2RJ4U5YFUsaNoDS06YqE+MMJjhF9wE2vk/atx+cuZPANKiqpeYlifbFDMOKV50/F8ETIMKAdja6BjkWjEu/ul3pGjHDgdKsMgXvE7kW8+JZZfAYr6M7x2CQKBgQC0nzMlQCRELlV2QfY/y4DVVdwOHa0kcWK4BDQoG3hrIqZI8qDHtylbQOr2x7OzCkevyPhAxktTpSB6En+h1kSlMXgW50G7BJgRh02PYjO/LzG1keTrskd/Lhz/rLJx6RNu38Kd0qXyyu4ECdh3a5uwdGcGR5gK0j8gRaNLI7z0AwKBgGCR2RrgPH6PmVAkl8+NYIIafCgUJEeBVQFp5BthKFMF7T3YBApJgxeFUr9JNDEXI53SWptBIvXtGLkWBZodxgz31Xw5usKFxPn8QRX46tJL1g92jmk36i3v9/lyPpw5vvGzzOSlotWeTaYkaYq60FxIdXq1nt2aDM8idaRaRQSJAoGBAK14UIiwJ6SL02x2QWhXrG43vdaYzdc4RpVxBB6Y83gBmS5qR9xHnyKTIJbOiVmkv+Cl5Tdguquk5aIX7hiiTt9QuYNVqx1QdjQvfF/+8q/lRZqaef+cA2q0AQnU9LTOIunTYXht/Q9HsnyScCGRab94rBGt5OpPrmzyNLhPpIO9AoGAWz1zT29lmjadHei346qJJ0OkQcva8XJm/NtQ05XZ1pQFq03e+mI/FuIpG39n2exMAWt4zmW1f3b5X31h53LtA2a1/RsWZuowz3lTXbZ1XZxNTzCzqCnZthwc7keavhx1gwvowduLJ0AEEPXyEmR5g+4tNTDbNO1awzCbhXg61iQ=";

        // 3.支付宝公钥，支付宝生成的公钥，切勿与商户公钥混淆
        public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnYXyZokXkeyj1NuZvCSP0jTKQ007wSvkqQPsZWtl4Z1xPof6t6Hva3Ua9KBiJplSXhUQHj4Dkknwr572tiGnbsZTEMHiMqnrVGL0SWakT3zrzU9u5E6feE0OVohtCw6jMDkFUuyXWX/eBNiFTVuraGhnl8iiB+KyTy1j5hMh1Dqd08ncpWQ9NTXJRXrcmlpUTggub3SOh9680SDtQfxILzzgWOGdRkO60tGZvSuFwhVipK5DEzW2qgxxY9b95kpc7cEqOi5s2pNu/0nppsFrXGukiuS1Ky9gt+1xfP/4E5d6DbIYiUjw5yjyLeIVXZM9yMvdt9GUbAIKRboP9BHbUwIDAQAB";

        // 4.服务器异步通知页面路径 需http://或者https://格式的完整路径，必须外网可以正常访问，可以使用natapp进行外网映射
        public static String notify_url = "https://app-api.hqzhixing.com/pay/ali/v1/callback";

        //5.页面跳转同步通知页面路径 需http://或者https://格式的完整路径，必须外网可以正常访问 商户可以自定义同步跳转地址
        public static String return_url = "";

        // 6.请求支付宝的网关地址,此处为沙箱测试地址，正式环境替换即可
        public static String URL = "https://openapi.alipay.com/gateway.do";

        // 7.编码
        public static String CHARSET = "UTF-8";

        // 8.返回格式
        public static String FORMAT = "json";

        // 9.加密类型
        public static String SIGNTYPE = "RSA2";



}
