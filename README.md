## 支付宝授权登录插件

### 目前只支持 Android

### 代码示例

```javascript
const hyyAlipay = uni.requireNativePlugin("Hyy-Alipay");

hyyAlipay.openAuthScheme(
  {
    appId: "", // 支付宝分配给开发者的应用 ID
    scheme: "", // manifest.json -> "App常用其他设置" -> "UrlSchemes"里设置
  },
  (res) => {
    // 参考支付宝官方文档 https://opendocs.alipay.com/open/218/wy75xo?pathHash=03eeb9c7
    console.log(res);
  }
);
```

回调数据示例：

```javascript
{"scope":"auth_user","result_code":"SUCCESS","state":"init","app_id":"xx","auth_code":"xx"}

```
