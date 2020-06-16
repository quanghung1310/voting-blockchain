# Voting System

|Version | Date       | Author    | Description         |
|------- | ---------- | --------- | ------------------- |
|1.0     | 11-06-2020 | Tran Thi Lang | Init document       |
|1.1     | 16-06-2020 | Tran Thi Lang | Login API       |


# II. API Document
# Index

1. [Create New Wallet](#1-create-new-wallet)
2. [Log In](#1-log-in)



# 1. Create New Wallet

## Raw Data
**Request:**

```json
{
  "requestId": "0e28ddd4-4017-decf-8ade-972e8c4d0cc6",
  "requestTime": 1555472829549,
  "email": "tranlang.dtnt@gmail.com",
  "password": "123456789",
  "lastName": "Lang",
  "firstName": "Tran",
  "type": 0,
  "sex": 0,
}
```

**Response:**
```json
{
  "requestId": "0e28ddd4-4017-decf-8ade-972e8c4d0cc6",
  "resultCode": 0,
  "message": "success",
  "responseTime": 1555472829580,
  "data": {
      "walletId": "b900c888-a17b-11ea-bb37-0242ac130002",
      "walletAddress": "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMYRJt85L2l9WRTjmVw0oMc3I0dX5jEttJLc/vc4WNGqQF1mpfbPeIyJ4/SokUsrLqu2Zb+6PuXs0HKID8vwL846mX+JqWgPdv//BbPJlC+bxKDjgn0AKi3XLGVEz2j27AVZjBnqxdKoqmK46teZrASoS5tqy1aEJSkfXyhBErVwIDAQAB",
      "walletPrimary": "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIxhEm3zkvaX1ZFOOZXDSgxzcjR1fmMS20ktz+9zhY0apAXWal9s94jInj9KiRSysuq7Zlv7o+5ezQcogPy/AvzjqZf4mpaA92//8Fs8mUL5vEoOOCfQAqLdcsZUTPaPbsBVmMGerF0qiqYrjq15msBKhLm2rLVoQlKR9fKEEStXAgMBAAECgYAxCmx0yuXpjcuHUqudXTcOOHqKRw2bMOg4nW+J+HKMg+UPQQOySJAFUgiulaxj9bW5eTReJ//x815vBMBjC5vQE4q2jnOSjaPcmFCBZVtCQWzKWK2vOeb5E5fUkfxZkl3i0Mad328bMkWj6SYY5DxAfiRsvgFsbVyc95nbUjMpgQJBAOpfJwOYthVDcgszuSCIzT6QBLHRz6lc8DDMg5sreAmmKK+Cz6548WyQud3IGitWr0vkEBFhzc/zw53lCE4tjxcCQQCZVWucR+UgMJ5Acg5WM19wVbNtGsUQLnKcAFNc/w9qVs6+Y7GfMPcPIh5SKKL0wavGhRRKMfnvWq/HogGzwO3BAkEAixo4fKD5iPtDx7RGLzIipvwxXRzK5JldkYkAn2sYTpnI0gqQmtv7ZlUri59FMO29EwzkIHzs+3yRYLhbypKttwJADjCiic75fFYjfxFPFFkivvGfbjxo+ktiHd/F1zhfg9bOwT3WpUXRx1u/9JiAJCh8Lh4It6kSWT1KQS5T+/+hwQJAcYHpcj5uiZukdak9IchXE22A3wiLPNNttkt0756ap/LOXeDUoRn1Tre5/wsnFDdOy9XeMHDAbZUlaWESCD1xOA==",
      "createDate": "16/06/2020 21:20:15"
  }
}
```

**Request:**

|Name|Type|Length|Required|Level|Description|
|----|----|:----:|:------:|:---:|-----------|
|requestId|String|50|x|L1|Định danh mỗi yêu cầu|
|requestTime|long||x|L1|Thời gian gọi request (tính theo millisecond) Múi giờ: GMT +7|
|data.email|String|50|x|L2|Địa chỉ email|
|data.password|String|15|x|L2|Mật khẩu đăng nhập|
|data.lastName|String|45|x|L2|Họ|
|data.firstName|String|45|x|L2|Tên|
|data.type|String|15|x|L2|Loại ví, 0: ví voter, 1: ví ứng cử viên|
|data.sex|String|4|x|L2|Giới tính user, 0: nữ, 1: nam|

**Response:**

|Name|Type|Length|Required|Level|Description|
|----|----|:----:|:------:|:---:|-----------|
|requestId|String|50|x|L1|Giống với yêu cầu ban đầu|
|resultCode|number|2|x|L1|Kết quả của request|
|message|String|24|x|L1|Mô tả chi tiết kết quả request|
|responseTime|long||x|L1|Thời gian trả kết quả cho request (tính theo millisecond) Múi giờ: GMT +7|
|data.walletId|String|32|x|L2|Định danh 1 ví, dùng để đăng nhập|
|data.walletAddress|String|1024|x|L2|Địa chỉ ví, dùng để nhận coin|
|data.walletPrimary|String|1024|x|L2|Định danh private|
|data.createDate|String||x|L2|Thời gian tạo ví chính là requestTime - dd/MM/yyyy HH:mm:ss (định dạng 24h) Múi giờ: GMT +7|

# 2. Log In

## Raw Data
**Request:**

```json
{
  "requestId": "0e28ddd4-4017-decf-8ade-972e8c4d0cc6",
  "requestTime": 1555472829549,
  "walletId": "1592239898676_131dd58bae5d42f2a16dcc82db0ea638",
  "password": "123456789"
}
```

**Response:**
```json
{
  "requestId": "0e28ddd4-4017-decf-8ade-972e8c4d0cc6",
  "resultCode": 0,
  "message": "success",
  "responseTime": 1555472829580,
  "data": {
      "firstName": "Tran Thi",
      "lastName": "Lang",
      "sex": 0,
      "email": "tranlang@gmail.com",
      "type": 0
  }
}
```

**Request:**

|Name|Type|Length|Required|Level|Description|
|----|----|:----:|:------:|:---:|-----------|
|requestId|String|50|x|L1|Định danh mỗi yêu cầu|
|requestTime|long||x|L1|Thời gian gọi request (tính theo millisecond) Múi giờ: GMT +7|
|data.walletId|String|100|x|L2|Địa chỉ hệ thống trả về sau khi đăng ký thành công|
|data.password|String|15|x|L2|Mật khẩu đăng nhập|


**Response:**

|Name|Type|Length|Required|Level|Description|
|----|----|:----:|:------:|:---:|-----------|
|requestId|String|50|x|L1|Giống với yêu cầu ban đầu|
|resultCode|number|2|x|L1|Kết quả của request|
|message|String|24|x|L1|Mô tả chi tiết kết quả request|
|responseTime|long||x|L1|Thời gian trả kết quả cho request (tính theo millisecond) Múi giờ: GMT +7|
|data.firstName|String|50|x|L2|Họ|
|data.lastName|String|20|x|L2|Tên|
|data.sex|Number|1024|x|L2|Giới tính, 0: nữ, 1: nam|
|data.email|String|50|x|L2|Địa chỉ email|
|data.type|String|15|x|L2|Loại ví, 0: ví voter, 1: ví ứng cử viên|
