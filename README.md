# Parking-Management (Prototype) / (수정중)
주차 관리 시스템 (프로토타입) 프로젝트 (2017)

### 프로젝트 계획서
----------------------------------------------

https://github.com/64byte/Parking-Management/blob/master/document/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%20%EA%B3%84%ED%9A%8D%EC%84%9C.pdf

### 요구 명세서
----------------------------------------------

https://github.com/64byte/Parking-Management/blob/master/document/%EC%9A%94%EA%B5%AC%EB%AA%85%EC%84%B8%EC%84%9C.pdf

### 소프트웨어 설계서
----------------------------------------------

https://github.com/64byte/Parking-Management/blob/master/document/%EC%86%8C%ED%94%84%ED%8A%B8%EC%9B%A8%EC%96%B4%20%EC%84%A4%EA%B3%84%EC%84%9C.pdf

### 소스 코드
----------------------------------------------

  * Android
    - Activity
      - MainActivity: https://github.com/64byte/Parking-Management/tree/master/Parking/src/main/java/com/story/parking/activity
      - LoginActivity: https://github.com/64byte/Parking-Management/blob/master/Parking/src/main/java/com/story/parking/activity/LoginActivity.java
  
    - AppConfig
      https://github.com/64byte/Parking-Management/blob/master/Parking/src/main/java/com/story/parking/AppConfig.java
    
    - RequestManager
      https://github.com/64byte/Parking-Management/blob/master/Parking/src/main/java/com/story/parking/RequestManager.java
      
    - Util
      https://github.com/64byte/Parking-Management/blob/master/Parking/src/main/java/com/story/parking/Util.java
  
  * nodejs (API 서버)
    - Stack: expressjs, mongodb
  
    * models
      * car: https://github.com/64byte/Parking-Management/blob/master/server/models/car.js
      * user: https://github.com/64byte/Parking-Management/blob/master/server/models/user.js
      * usinginfo: https://github.com/64byte/Parking-Management/blob/master/server/models/usinginfo.js
      
    * routers
      https://github.com/64byte/Parking-Management/blob/master/server/routes/index.js
      
    * main (entry point)
      https://github.com/64byte/Parking-Management/blob/master/server/app.js

### 구현 화면
----------------------------------------------
