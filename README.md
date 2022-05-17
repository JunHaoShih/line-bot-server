# line-bot-server

此為Demo用LINE聊天機器人，有以下功能
 - 讀取並顯示當期統一發票中獎號碼
 - 讀取並顯示近五周的中油油價歷史
 - 讀取並顯示未來三天的天氣預報
 - 擲骰子

## 功能

### 統一發票中獎號碼

向BOT發送指令 `!發票` 便會回傳當期統一發票中獎號碼

<img src="https://i.imgur.com/crVHenc.jpg" alt="line-demo-invoice" width="500"/>

### 中油油價歷史查詢

向BOT發送指令 `!油價` 油種 便會回傳該油種近五周的油價

<img src="https://i.imgur.com/iI58RCq.jpg" alt="line-bot-oil-price" width="500"/>

### 未來三天的天氣預報
向BOT發送指令 `!天氣` 縣市 區域 便會回傳剛區域未來三天的天氣預報

<img src="https://imgur.com/xhFMZyP.jpg" alt="line-bot-weather" width="500"/>

### 指令查詢
向BOT發送指令 `!指令` 即可取得目前可使用的指令

### BOT
掃描下方QR Code即可將Demo用LINE Bot加入好友

(PS1: 本專案使用免費方案部屬在render，所以server若有閒置一段時間，下次server收到指令要先等render重啟(約1~2分鐘)，啟動後即可正常發送)

(PS2: 由於render和line bot都是用免費方案，所以render時限(每月750hr)和line免費訊息則數(每月500則)超過，line bot就會失效，只能等下個月)

<img src="https://qr-official.line.me/sid/L/110agtlf.png" alt="line-demo-bot-qrcode" width="200"/>

