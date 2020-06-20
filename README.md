# Voting System

|Version | Date       | Author    | Description         |
|------- | ---------- | --------- | ------------------- |
|1.0     | 11-06-2020 | Tran Thi Lang | Init document       |
|1.1     | 16-06-2020 | Tran Thi Lang | Login API       |
|1.2     | 20-06-2020 | Tran Thi Lang | Election API       |

# II. API Document
# Index

1. [Create New Wallet](#1-create-new-wallet)
2. [Log In](#2-log-in)
3. [Voting](#3-voting)
4. [Create Content Vote](#4-create-content-vote)
5. [Get Content Vote](#5-get-content-vote)
6. [Get Elector](#6-get-elector)


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
  "sex": 0
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
|data.walletAddress|String|1024|x|L2|Địa chỉ ví, dùng để nhận phiếu|
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
|data.type|String|15|x|L2|Loại ví, 0: voter, 1: elector|

# 3. Voting

## Raw Data
**Request:**

```json
{
  "requestId": "0e28ddd4-4017-decf-8ade-972e8c4d0cc6",
  "requestTime": 1555472829549,
  "sender": "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLncAlrgurh7TGTb8EdiB94GXjFzNWfm9UMzsfiYHRSuivKhnrpnOihnsh3jpYaf7VS1S0oLUiH1hcI7Ud8zcx5Pzq/7P+m1DHI+X5+naseTUqACWgfS7WZ4pGZtgWtrNecOe5VII4hpxKXienRzHhQCVpMSH/8gOiz3jaQ03/0wIDAQAB",
  "reciepient": "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkkHiLrducTVdJSSSsM3mXzBzCOmPbTqsvA01/7j2E9crYQ/ILPaGF77RjWwXBWRBLyVfnVLlI9FPaCcHxJN7pQF38FRHGvFo04Ki5KITsQeeZAz4Hlp6IgD58GP2QWvj23/af0Oz36bR0La+XZzFI/scyrZ4Bq9hpTLqE0ocsvwIDAQAB",
  "value": 5,
  "currency": "vote",
  "contentId": "CONTENT_1592324551319",
  "description": "Send to my boyfriend"
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
      "transId": "TRANS_1555472829580",
      "signature": "b6e7302c7a2df244bc76e3592b2e3f7ff39abc2a3b6ea161830acea57a427b5f"
     }
}
```

**Request:**

|Name|Type|Length|Required|Level|Description|
|----|----|:----:|:------:|:---:|-----------|
|requestId|String|50|x|L1|Định danh mỗi yêu cầu|
|requestTime|long||x|L1|Thời gian gọi request (tính theo millisecond) Múi giờ: GMT +7|
|sender|String|1024|x|L2|Địa chỉ người gửi|
|reciepient|String|1024|x|L2|Địa chỉ người nhận|
|value|Number||x|L2|Số lượng phiếu bầu (mặc định 1 phiếu)|
|curency|String||x|L2|Định lượng giá trị (mặc định vote)|
|description|String|100|x|L2|Mô tả giao dịch|
|contentId|String|100|x|L2|Định danh cho 1 cuộc bỏ phiếu|


**Response:**

|Name|Type|Length|Required|Level|Description|
|----|----|:----:|:------:|:---:|-----------|
|requestId|String|50|x|L1|Giống với yêu cầu ban đầu|
|resultCode|number|2|x|L1|Kết quả của request|
|message|String|24|x|L1|Mô tả chi tiết kết quả request|
|responseTime|long||x|L2|Thời gian trả kết quả cho request (tính theo millisecond) Múi giờ: GMT +7|
|data.transId|Number||x|L2|Mã giao dịch|ông|
|data.signature|String|512|x|L2|Chữ ký điện tử (privateKey, [sender + reciepient + value])|

# 4. Create Content Vote

## Raw Data
**Request:**

```json
{
  "requestId": "0e28ddd4-4017-decf-8ade-972e8c4d0cc6",
  "requestTime": 1555472829549,
  "content": "Bầu lớp trưởng",
  "startDate": 1655472829549,
  "endDate": 1855472829549,
  "description": "Bầu cho vui"
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
    "contentId": "CONTENT_1592323441036"
     }
}
```

**Request:**

|Name|Type|Length|Required|Level|Description|
|----|----|:----:|:------:|:---:|-----------|
|requestId|String|50|x|L1|Định danh mỗi yêu cầu|
|requestTime|long||x|L1|Thời gian gọi request (tính theo millisecond) Múi giờ: GMT +7|
|content|String|1024|x|L2|Nội dung cuộc bầu cử|
|startDate|Number|1024|x|L2|Thời gian bắt đầu mở bầu cử (tính theo millisecond) Múi giờ: GMT +7|
|endDate|Number||x|L2|Thời gian kết thúc bầu cử (tính theo millisecond) Múi giờ: GMT +7)|
|description|String||x|L2|Mô tả cuộc bầu cử|


**Response:**

|Name|Type|Length|Required|Level|Description|
|----|----|:----:|:------:|:---:|-----------|
|requestId|String|50|x|L1|Giống với yêu cầu ban đầu|
|resultCode|number|2|x|L1|Kết quả của request|
|message|String|24|x|L1|Mô tả chi tiết kết quả request|
|responseTime|long||x|L1|Thời gian trả kết quả cho request (tính theo millisecond) Múi giờ: GMT +7|
|data.contentId|String|45|x|L2|Mã cuộc bầu cử


# 5. Get Content Vote

## Raw Data
**Request:**

```json
{
  "requestId": "0e28ddd4-4017-decf-8ade-972e8c4d0cc6",
  "requestTime": 1555472829549,
  "startDate": 1655472829549,
  "endDate": 1855472829549
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
    "contents": [
        {   
            "contentId": "CONTENT_1592323441036",
            "content": "Bầu lớp trưởng",
            "description": "Bầu cho vui"
        }
    ]
  }
}
```

**Request:**

|Name|Type|Length|Required|Level|Description|
|----|----|:----:|:------:|:---:|-----------|
|requestId|String|50|x|L1|Định danh mỗi yêu cầu|
|requestTime|long||x|L1|Thời gian gọi request (tính theo millisecond) Múi giờ: GMT +7|
|startDate|String|1024||L1|Thời gian bắt đầu mở bâ ̀u cử - Nếu không truyền lấy thì lấy hết content còn hạn (tính theo millisecond) dd/MM/yyyy HH:mm:ss (định dạng 24h) Múi giờ: GMT +7|
|endDate|String|||L1|Thời gian kết thúc bầu cử - Nếu không truyền lấy thì lấy hết content còn hạn |


**Response:**

|Name|Type|Length|Required|Level|Description|
|----|----|:----:|:------:|:---:|-----------|
|requestId|String|50|x|L1|Giống với yêu cầu ban đầu|
|resultCode|number|2|x|L1|Kết quả của request|
|message|String|24|x|L1|Mô tả chi tiết kết quả request|
|responseTime|long||x|L1|Thời gian trả kết quả cho request (tính theo millisecond) Múi giờ: GMT +7|
|data.contents|JsonArray||x|L2|Danh sách các VOTE_CONTENT|
|data.contents.contentId|String||x|L3|Mã cuộc bầu cư
|data.contents.content|String||x|L3|Nội dung cuộc bầu cử|
|data.contents.description|String||x|L3|Mô tả cuộc bầu cử

# 6. Get Elector

## Raw Data
**Request:**

```json
{
  "requestId": "0e28ddd4-4017-decf-8ade-972e8c4d0cc6",
  "requestTime": 1555472829549,
  "contentId": "CONTENT_1592324551319"
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
    "electors": [
        {   
            "walletId": "b900c888-a17b-11ea-bb37-0242ac130002",
            "walletAddress": "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMYRJt85L2l9WRTjmVw0oMc3I0dX5jEttJLc/vc4WNGqQF1mpfbPeIyJ4/SokUsrLqu2Zb+6PuXs0HKID8vwL846mX+JqWgPdv//BbPJlC+bxKDjgn0AKi3XLGVEz2j27AVZjBnqxdKoqmK46teZrASoS5tqy1aEJSkfXyhBErVwIDAQAB",
            "firstName": "Anh",
            "lastName": "ABC",
            "email": "anh.abc@gmail.com",
            "type": 1,
            "sex": 1,
            "active": 1 
        }
    ]
  }
}
```

**Request:**

|Name|Type|Length|Required|Level|Description|
|----|----|:----:|:------:|:---:|-----------|
|requestId|String|50|x|L1|Định danh mỗi yêu cầu|
|requestTime|long||x|L1|Thời gian gọi request (tính theo millisecond) Múi giờ: GMT +7|
|contentId|String|100|x|L1|Mã content |


**Response:**

|Name|Type|Length|Required|Level|Description|
|----|----|:----:|:------:|:---:|-----------|
|requestId|String|50|x|L1|Giống với yêu cầu ban đầu|
|resultCode|number|2|x|L1|Kết quả của request|
|message|String|24|x|L1|Mô tả chi tiết kết quả request|
|responseTime|long||x|L1|Thời gian trả kết quả cho request (tính theo millisecond) Múi giờ: GMT +7|
|data.electors|JsonArray||x|L2|Danh sách các ứng cử viên|
|data.electors.walletId|String|1024|x|L3|Định danh 1 ví, dùng để đăng nhập|
|data.electors.walletAddress|String|1024|x|L3|Địa chỉ ví, dùng để nhận phiếu|
|data.electors.firstName|String|50|x|L2|Họ|
|data.electors.lastName|String|20|x|L2|Tên|
|data.electors.sex|Number|1024|x|L2|Giới tính, 0: nữ, 1: nam|
|data.electors.email|String|50|x|L2|Địa chỉ email|
|data.electors.type|Number||x|L2|Loại ví, 0: voter, 1: elector|
|data.electors.active|Number||x|L2|Ví còn tồn tại không: 0: bị khóa, 1: còn hoạt động|

