[![](https://lh3.google.com/u/0/d/11kJc0UgNctIIdEQV2V07X-GW_v7B7Fd1=w1008-h892-iv1)](https://user-images.githubusercontent.com/72757829/114306905-615df500-9b18-11eb-80dc-e1bfc0a8e33c.png)  
commit 기반 URL 홍보 사이트

> 서비스명: commit Pixel
> 팀명: 진짜_진짜_최종_팀
> 개발 기간: 2023.10.10 ~ 2023.11.17 (약 6주)

### [🎬UCC영상]

[ UCC 바로가기](https://www.youtube.com/shorts/_tgeXkGApKQ)

### [📃문서]

> [기획 문서](https://www.notion.so/PJT-2a4c024fa30c4c919aee328b7e09e64c)  
> [1️⃣SubPJT_기획문서](https://naver.com),  [2️⃣SubPJT2_학습 및 개발기본 문서](https://naver.com),  [3️⃣SubPJT3_심화 및 마무리 문서](https://naver.com)

## [👨 팀원 역할]
|팀원|역할|비고|
|------|------|---------------|
|이용현|팀장|인프라, CI/CD, Rank 서비스|
|김희조|팀원|인증/인가(back), Github Oauth, solved.ac 연동, 타임랩스, S3|
|권도현|팀원|인프라, CI/CD, Pixel WebSocket, Image 트레이싱 툴|
|한다솜|팀원|Github commit 연동 크레딧, Pixel WebSocket 이미지 픽셀화|
|김인범|팀원|인증/인가(front), 건의사항, URL 입력 및 승인, 유저 정보 연동|
|이예린|팀원|캔버스 구현, 그림 툴 구현, Image 서비스 연동, Pixel 서비스 연동|

  

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

## ✔Git 컨벤션
```bash
fe/feature/기능명  
```
```bash
be/userms/feature/기능명  
```
[ Git 컨벤션 바로가기](https://www.notion.so/realrealfinal/Git-Convention-d9d8def33b8949bda41eb3ad35b2e512?pvs=4)

<br/>

## ✔Code 컨벤션
[ Front-end 컨벤션 바로가기](https://www.notion.so/realrealfinal/FE-Convention-f02a40ecac62456d9c21af06a29aaea7?pvs=4)
[ Back-end 컨벤션 바로가기](https://www.notion.so/realrealfinal/BE-Convention-6ed36c69f77847f68cac762d62cdc551?pvs=4)

<br/>

## ✔프로젝트 구조  
![프로젝트구조](https://lh3.google.com/u/0/d/1zUlW2vGsm-OXGSwkifzxHn8HQoqQYEP5=w1920-h922-iv1)

<br/>

## 💻 주요 기능 미리보기    
![서비스소개](https://lh3.google.com/u/0/d/1gQ5r_W1hjhwzUgKT9Gr17ETcKkprgfEQ=w1910-h870-iv1)

### 1. 튜토리얼 화면           

![튜토리얼 페이지](https://lh3.google.com/u/0/d/1Mus2F_ko1A-4KsKOeOEL-h82eB73rOgt=w960-h910-iv1)
- 튜토리얼 화면에서는 사이트에 대한 간단한 설명, 패치노트, 사용 방법을 소개합니다.
- 'commitPixel 하러 가기' 버튼을 눌러 메인 화면으로 이동할 수 있습니다.


### 2. 비로그인 메인 화면           

![비로그인 메인 페이지](https://lh3.google.com/u/0/d/1esPzPrCPm9MB15idDwzT1EviQCc9s7It=w960-h930-iv1)
- 비로그인 메인 화면에서는 칸바스, 소셜 로그인, 이미지 픽셀화, 타입랩스, 랭킹, 카카오 공유가 사이드바에 나옵니다.


### 3. 로그인 메인 화면           

![로그인 메인 페이지](https://lh3.google.com/u/0/d/1FtE2roQqaMVazYNnWQ2dyOVYESbM7PRL=w960-h930-iv1)
- 로그인 메인 화면에서는 사이드바에서 로그인 버튼이 사라지고 사용자 정보와, 건의하기가 나타납니다.


### 4. 칸바스           

![칸바스](https://lh3.google.com/u/0/d/1hcVq-2IH_-LsUm5h0oRmRr2G1B8azvLz=w960-h930-iv1)
- 브러시를 사용하여 칸바스에 픽셀을 찍을 수 있습니다. 사용자 정보에서 픽셀을 사용했음을 확인할 수 있습니다.
- 스포이드를 사용하여 색을 복사할 수 있습니다.
- 도구 해제 상태에서 픽셀을 클릭하여 홍보URL 확인 및 이동할 수 있습니다.


### 5. 사용자 정보           

![사용자 정보](https://lh3.google.com/u/0/d/1MWyn13sT-VCYM6ko0_1ooh40dRfEp2EH=w960-h930-iv1)
- 새로고침 버튼으로 픽셀 수를 새로고침할 수 있습니다. (재사용 대기 시간 15분)
- 펜 아이콘을 클릭하여 홍보 URL을 변경할 수 있습니다.
- solvedAc 연동을 할 수 있다.


### 6. 이미지 픽셀화           

![이미지 픽셀화](https://lh3.google.com/u/0/d/1XuNp6nQX2ejP4ZB2Yr9wWcNLTfMRsNxG=w960-h930-iv1)
- 로컬에 있는 이미지를 픽셀화할 수 있습니다.

	
### 7. 타입랩스           

![타입랩스](https://lh3.google.com/u/0/d/1PFQ4704w1AifXU_tNv_2PYAjfBZTVOsc=w960-h930-iv1)
- 칸바스의 타입랩스를 확인할 수 있습니다.


### 8. 랭킹           

![랭킹](https://lh3.google.com/u/0/d/14wirlABj_-JTOOrmPDVgZIHK_WL6kdcj=w960-h930-iv1)
- URL 랭킹과 Pixel 랭킹을 확인할 수 있고 URL을 클릭하면 해당 링크로 이동합니다.


### 9. 카카오 공유           

![카카오 공유](https://lh3.google.com/u/0/d/1kIDPYU8U36pdg5tTqyPspcL1XdzTCrIL=w960-h930-iv1)
- 카카오 공유로 카카오톡 친구에게 웹사이트를 공유할 수 있습니다.
- Open Graph를 적용하여 링크를 복붙하면 웹사이트를 미리보기 할 수 있습니다.


### 10. 건의하기           

![건의하기](https://lh3.google.com/u/0/d/1_RA2a_uTogUs_C8hfLrdys-Ry5rqIK-p=w960-h930-iv1)
- 일반 건의사항을 요청할 수 있습니다.
- whitelist 추가 요청을 할 수 있습니다. (이미 인가된 url이면 등록되지 않습니다.)


### 11. Flourish           

![Flourish](https://lh3.google.com/u/0/d/1nC9OeBZmi0SYQ-APfBU4akdRgDl9URzG=w1920-h922-iv1)
- 랭킹의 변화를 시각적으로 확인할 수 있다.

### 12. 앱 다운로드           

![앱 다운로드](https://lh3.google.com/u/0/d/1GVhSBEaH4aUFgC81x4kxXLcxHveA8E8G=w960-h930-iv1)
- PWA를 적용하여 앱으로 다운로드 할 수 있습니다.
- 모바일 뷰를 적용하여 모바일에서도 이용할 수 있습니다.

<br/>


# 📑산출물  
> 1. ER 다이어그램     
> 2. 목업   


##  ER 다이어그램   
![erd](https://lh3.google.com/u/0/d/13Is2YSI9s_SEFKFVtKivWxtR95hbKo73=w2000-h7200-iv1)


---


<br/>



### 목업
![슬라이드1](https://lh3.google.com/u/0/d/10_2iK1SMvIwvQ-ihwImxnkPy96CCsd5S=w2560-h1346-iv1)
![슬라이드2](https://lh3.google.com/u/0/d/1EeL_Bjq22PEjsdg2A2AS5q3YoHvBir7f=w2560-h1346-iv1)
![슬라이드3](https://lh3.google.com/u/0/d/1g2oPyzzfx5zPwmQqeo2IilYSo4nMrGQh=w2560-h1346-iv1)
![슬라이드4](https://lh3.google.com/u/0/d/1dB2XpTUZPOD6IzkfDO28V0WrR_RqfuPg=w2560-h1346-iv1)
![슬라이드5](https://lh3.google.com/u/0/d/1oxWXtnjtTtDfXNMUgFahxrYuxXB8GSna=w1250-h1250-iv1)