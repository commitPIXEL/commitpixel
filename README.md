![commitFox](https://github.com/commitPIXEL/commitpixel/assets/63138047/3cbec7b3-d9f2-4f8e-a9cb-c094e75e2ec6)

### commit 기반 URL 홍보 사이트

> 서비스명: commit Pixel<br/>
> 팀명: 진짜_진짜_최종_팀<br/>
> 개발 기간: 2023.10.10 ~ 2023.11.17 (약 6주)

<br/>

## [🚩 목차]

1. [서비스 소개](#commit-기반-url-홍보-사이트)
2. [목차](#-목차)
2. [STACKS](#-stacks)
3. [UCC영상](#-ucc영상)
4. [문서](#-문서)
5. [팀원 역할](#-팀원-역할)
7. [주요 기능](#-주요-기능)
8. [Git 컨벤션](#-git-컨벤션)
9. [Code 컨벤션](#-code-컨벤션)
10. [프로젝트 구조](#-프로젝트-구조)
11. [주요 기능 미리보기](#-주요-기능-미리보기)
12. [산출물](#-산출물)
13. [Awards](#-awards)

---


<br/>


## [📚 STACKS]

<div align=center> 
  <img src="https://img.shields.io/badge/Next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white"> 
  <img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=white">
  <img src="https://img.shields.io/badge/node.js-339933?style=for-the-badge&logo=Node.js&logoColor=white">
  <img src="https://img.shields.io/badge/React Query-FF4154?style=for-the-badge&logo=reactquery&logoColor=white">
  <img src="https://img.shields.io/badge/Redux-764ABC?style=for-the-badge&logo=redux&logoColor=white">
  <br/>

  <img src="https://img.shields.io/badge/Tailwind Css-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white"> 
  <img src="https://img.shields.io/badge/MUI-007FFF?style=for-the-badge&logo=mui&logoColor=white"> 
  <img src="https://img.shields.io/badge/Font Awesome-528DD7?style=for-the-badge&logo=fontawesome&logoColor=black">
  <br/>

  <img src="https://img.shields.io/badge/PWA-5A0FC8?style=for-the-badge&logo=pwa&logoColor=white"> 
  <img src="https://img.shields.io/badge/ESLint-4B32C3?style=for-the-badge&logo=eslint&logoColor=white"> 
  <img src="https://img.shields.io/badge/Prettier-F7B93E?style=for-the-badge&logo=prettier&logoColor=black">
  <img src="https://img.shields.io/badge/.ENV-ECD53F?style=for-the-badge&logo=dotenv&logoColor=black">
  <br/>
  <br/>

  
  <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/MySql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
  <img src="https://img.shields.io/badge/mongoDB-47A248?style=for-the-badge&logo=MongoDB&logoColor=white">
  <br/>

  <img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"> 
  <img src="https://img.shields.io/badge/apache tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=white">
  <img src="https://img.shields.io/badge/apache Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white">
  <br/>
  
  <img src="https://img.shields.io/badge/GitLab-FC6D26?style=for-the-badge&logo=gitlab&logoColor=white">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
  <img src="https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white">
  <br/>
</div>


<br/><br/>


### [🎬 UCC영상]

[ UCC 바로가기](https://youtu.be/sAV3ZN6DmWI)


<br/>


### [📃 문서]

> [1️⃣ 주제 아이디어](https://www.notion.so/realrealfinal/7e3c170d14ae43c380244b55083f0ecf?pvs=4) <br/>
> [2️⃣ 학습 공유](https://www.notion.so/realrealfinal/07e610411009431ba548f7b2c383515a?pvs=4) <br/>
> [3️⃣ 기능 명세서](https://www.notion.so/realrealfinal/477dde150f444b49b61818a020e4959e?pvs=4) <br/>
> [4️⃣ API 명세서](https://www.notion.so/realrealfinal/API-130fa77afd73449d8b208f7f59777d4a?pvs=4)


<br/>


## [👨 팀원 역할]
|팀원|역할|비고|
|------|------|---------------|
|이용현|팀장|인프라, CI/CD, Rank 서비스|
|김희조|팀원|인증/인가(back), Github Oauth, solved.ac 연동, 타임랩스, S3|
|권도현|팀원|인프라, CI/CD, Pixel WebSocket, Image 트레이싱 툴|
|한다솜|팀원|Github commit 연동 크레딧, Pixel WebSocket 이미지 픽셀화|
|김인범|팀원|인증/인가(front), 건의사항, URL 입력 및 승인, 유저 정보 연동|
|이예린|팀원|캔버스 구현, 그림 툴 구현, Image 서비스 연동, Pixel 서비스 연동|


<br/>


## [📑 주요 기능]()

-   **회원가입/로그인 기능**
    
    > a. SNS로그인(GitHub)으로 별도 인증 없이 빠르게 로그인할 수 있다.
    > 
    > c. 가입 후 최초 로그인 시 튜토리얼 페이지로 이동한다.
    
-   **튜토리얼 기능**
    
    > a. 간단하게 사이트 소개를 한다.
    > 
    > b. 패치노트를 제공하여 변경사항 정보를 제공한다.
    > 
    > c. 사용방법과 단축키에 대한 설명을 GIF와 짧은 텍스트로 이해하기 쉽게 제공한다.
    
-   **공유 칸바스 기능**
    
    > a. 브러시를 사용하여 픽셀을 색칠하고 URL을 홍보할 수 있다.
    > 
    > b. 스포이드를 사용하여 색을 복사할 수 있다. 
    > 
    > c. 도구 해제 상태에서 픽셀을 클릭하여 홍보URL를 확인할 수 있다.
    > 
    > d. '우클릭'으로 칸바스 이동할 수 있다.
    
-   **사용자 정보 기능**
    
    > a. 프로필 이미지, 닉네임, 총 픽셀 수, 사용가능 픽셀 수, 픽셀 수 새로고침 버튼, 홍보URL, solvedAC 연동 버튼을 확인할 수 있다.
    > 
    > b. 픽셀을 색칠하면 실시간으로 사용가능 픽셀 수가 줄어드는 것을 확인할 수 있다.
    > 
    > c. 픽셀 수 새로고침 버튼을 사용해서 픽셀 수를 최신화할 수 있다. 연동API 요청 제한으로 15분 대기 시간이 있다.
    > 
    > d. 인가된 URL에 한해서 홍보 URL을 변경할 수 있다. 변경 요청한 URL이 비인가 URL이면 whitelist 요청 입력창이 뜬다.
    > 
    > e. solvedAC 연동 버튼을 클릭하면 solvedAC ID 입력창이 나온다. solvedAC 상태 메시지에 'commitpixel.com' 입력 후 입력창에 ID를 입력하면 계정이 연동되고 버튼은 사라진다. 
    
-   **이미지 픽셀화 기능**
    
    > a. 사용자 로컬에 있는 이미지를 픽셀화 해준다.
    > 
    > b. 이를 참고하여 그림 그리기에 도움을 준다.
    
-   **타입랩스 보기 기능**
    
    > a. 하루 동안 칸바스의 변화를 타입랩스로 보여준다.  
    > 
    > b. 구체적으로 08시-01시 칸바스의 변화를 10분 간격으로 보여준다.
    
-   **랭킹 기능**
    
    > a. URL랭킹: 칸바스에 있는 홍보 URL의 수에 대한 랭킹을 제공한다.
    > 
    > b. Pixel랭킹: 사용자가 픽셀 색칠한 수에 대한 랭킹을 제공한다. 
    > 
    > c. 이를 통해서 추가적인 홍보 효과를 제공하고 사용자 참여를 유도한다.
    >
    > d. nav에 있는 Flourish 아이콘을 클릭하여 변화를 확인할 수 있다.
  
-   **앱 기능**
    
    > a. PWA를 적용하여 네이티브 환경을 제공한다.
    > 
    > b. 데탑, 모바일에서 앱을 다운받아 사용할 수 있다. 


    <br/>


## ✔ Git 컨벤션
```bash
fe/feature/기능명  
```
```bash
be/userms/feature/기능명  
```
[ Git 컨벤션 바로가기](https://www.notion.so/realrealfinal/Git-Convention-d9d8def33b8949bda41eb3ad35b2e512?pvs=4)


<br/>


## ✔ Code 컨벤션
[ Front-end 컨벤션 바로가기](https://www.notion.so/realrealfinal/FE-Convention-f02a40ecac62456d9c21af06a29aaea7?pvs=4)
[ Back-end 컨벤션 바로가기](https://www.notion.so/realrealfinal/BE-Convention-6ed36c69f77847f68cac762d62cdc551?pvs=4)


<br/>


## ✔ 프로젝트 구조  
![아키텍쳐](https://github.com/commitPIXEL/commitpixel/assets/63138047/bfb42f37-0a9c-4fc5-9995-789cb0f8fb8a)


<br/>


## 💻 주요 기능 미리보기    
![서비스소개](https://github.com/commitPIXEL/commitpixel/assets/63138047/1bd2ecae-0917-4660-8071-109bca9d8a50)

### 1. 튜토리얼 화면           

![튜토리얼](https://github.com/commitPIXEL/commitpixel/assets/63138047/2f268590-036c-419e-b305-0a1a1c2f4947)
- 튜토리얼 화면에서는 사이트에 대한 간단한 설명, 패치노트, 사용 방법을 소개합니다.
- 'commitPixel 하러 가기' 버튼을 눌러 메인 화면으로 이동할 수 있습니다.


### 2. 비로그인 메인 화면           

![비로그인](https://github.com/commitPIXEL/commitpixel/assets/63138047/13534540-4824-4899-9fa9-3e1e9441fa64)
- 비로그인 메인 화면에서는 칸바스, 소셜 로그인, 이미지 픽셀화, 타입랩스, 랭킹, 카카오 공유가 사이드바에 나옵니다.


### 3. 로그인 메인 화면           

![로그인](https://github.com/commitPIXEL/commitpixel/assets/63138047/93e129e9-b023-415d-b29c-3175cc2a7c41)
- 로그인 메인 화면에서는 사이드바에서 로그인 버튼이 사라지고 사용자 정보와, 건의하기가 나타납니다.


### 4. 캔버스           

![픽셀](https://github.com/commitPIXEL/commitpixel/assets/63138047/dda4e197-972e-4d1f-90fa-72ea980c7ad6)
- 브러시를 사용하여 캔버스에 픽셀을 찍을 수 있습니다. 사용자 정보에서 픽셀을 사용했음을 확인할 수 있습니다.
- 스포이드를 사용하여 색을 복사할 수 있습니다.
- 도구 해제 상태에서 픽셀을 클릭하여 홍보URL 확인 및 이동할 수 있습니다.


### 5. 사용자 정보           

![사용자정보](https://github.com/commitPIXEL/commitpixel/assets/63138047/25d8af87-3dcb-460e-be58-397bcfeee133)
- 새로고침 버튼으로 픽셀 수를 새로고침할 수 있습니다. (재사용 대기 시간 15분)
- 펜 아이콘을 클릭하여 홍보 URL을 변경할 수 있습니다.
- solvedAc 연동을 할 수 있다.


### 6. 이미지 픽셀화           

![이미지 픽셀화](https://github.com/commitPIXEL/commitpixel/assets/63138047/448f381e-a0a8-46cd-bd07-56d98756a7ef)
- 로컬에 있는 이미지를 픽셀화할 수 있습니다.

	
### 7. 타입랩스           

![타임랩스](https://github.com/commitPIXEL/commitpixel/assets/63138047/9ef73e93-b467-4e02-98e9-3409e8798784)
- 칸바스의 타입랩스를 확인할 수 있습니다.


### 8. 랭킹           

![랭킹](https://github.com/commitPIXEL/commitpixel/assets/63138047/25ecfb5d-47af-4564-a16e-d271a14f883b)
- URL 랭킹과 Pixel 랭킹을 확인할 수 있고 URL을 클릭하면 해당 링크로 이동합니다.


### 9. 카카오 공유           

![카카오공유](https://github.com/commitPIXEL/commitpixel/assets/63138047/424ec54a-487c-4f22-b213-2fec06f6d62a)
- 카카오 공유로 카카오톡 친구에게 웹사이트를 공유할 수 있습니다.
- Open Graph를 적용하여 링크를 복붙하면 웹사이트를 미리보기 할 수 있습니다.


### 10. 건의하기           

![건의하기](https://github.com/commitPIXEL/commitpixel/assets/63138047/201615ce-fd2a-47f7-ae06-6b0eae3d0b28)
- 일반 건의사항을 요청할 수 있습니다.
- whitelist 추가 요청을 할 수 있습니다. (이미 인가된 url이면 등록되지 않습니다.)


### 11. Flourish           

![Flourish](https://github.com/commitPIXEL/commitpixel/assets/63138047/d907d7b5-fca6-48c6-a134-8ad532233b00)
- 랭킹의 변화를 시각적으로 확인할 수 있다.

### 12. 앱 다운로드           

![pwa](https://github.com/commitPIXEL/commitpixel/assets/63138047/b08c4448-3615-4a94-ab30-31e258bf7a27)
- PWA를 적용하여 앱으로 다운로드 할 수 있습니다.
- 모바일 뷰를 적용하여 모바일에서도 이용할 수 있습니다.


<br/>


# 📑 산출물  
> 1. ER 다이어그램     
> 2. 목업   


##  ER 다이어그램   
![erd](https://github.com/commitPIXEL/commitpixel/assets/63138047/44706b51-ed5d-40a0-8039-0547023e7da0)


<br/>


### ✔ 목업
<img src="https://github.com/commitPIXEL/commitpixel/assets/63138047/e97572ec-9310-48a7-966e-57f2e72b875f" width="300" height="300">
<img src="https://github.com/commitPIXEL/commitpixel/assets/63138047/576c0f39-4350-4b18-9eb2-491692782ee7" width="300" height="300">
<img src="https://github.com/commitPIXEL/commitpixel/assets/63138047/d8ccfe2c-e063-4e22-8e58-3f0214ce8aec" width="300" height="300">
<img src="https://github.com/commitPIXEL/commitpixel/assets/63138047/80fde1df-c311-4ee3-a828-3f9bbfea7f61" width="300" height="300">
<img src="https://github.com/commitPIXEL/commitpixel/assets/63138047/4cb9114d-57bc-4cce-a6fd-143a2d69b377" width="300" height="300">


<br/>


# 🏆 Awards
- 🥉삼성청년SW아카데미 2학기 자율PJT 3등 수상
