## ✨ Billy 빌리
![Billy_main](https://user-images.githubusercontent.com/74149915/193491387-b955be63-3c38-4c81-9928-0f53ea7455fe.png)

## 👉🏻 프로젝트 소개 <br>
- 팔기에는 아깝고 정작 집에서 자리만 차지하고 있는 물건들이 있을 때 🚲
- 구매하기는 아깝지만 정작 필요할 때 없는 물건들을 사용을 해야할 때 🔌
- 여행지에서 사용할 카메라, 보드게임 같이 잠시 필요한 물건을 구하고 싶을 때 👾
- 나에게 잘 맞는 물건인지 미리 사용해보고 싶을 때 🧑‍💻

✨ 일상의 편리함을 도와주는 개인 대여 플랫폼, Billy 입니다! 🥳


👉🏻[빌리 이용해보기 Click!](https://billyproject.shop/) <br>
👉🏻[빌리 팀 노션 Click!](https://gossamer-bath-a39.notion.site/Billy-1c64df5f1eb64281aad11b693835e3bc)


<br>


## 🛠 프로젝트 아키텍쳐
<img alt="Billy_아키텍쳐" src="https://user-images.githubusercontent.com/74149915/193500281-ffecbf24-629e-4094-9323-cdb012053d81.png">

<br>

## ⚙ 기술 스택

### ✔ Frond-end
<div>
<img src="https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=Axios&logoColor=white"/>
<img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=React&logoColor=black"/>
<img src="https://img.shields.io/badge/Redux Toolkit-764ABC?style=for-the-badge&logo=Redux&logoColor=white"/>
<img src="https://img.shields.io/badge/React Router-CA4245?style=for-the-badge&logo=React Router&logoColor=white"/>
<img src="https://img.shields.io/badge/Javascript-F7DF1E?style=for-the-badge&logo=Javascript&logoColor=black"/>
<img src="https://img.shields.io/badge/Cross ENV-ECD53F?style=for-the-badge&logo=.ENV&logoColor=black">
<img src="https://img.shields.io/badge/Sass-CC6699?style=for-the-badge&logo=Sass&logoColor=white"/>
<img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">
<img src="https://img.shields.io/badge/socket.io-010101?style=for-the-badge&logo=socket.io&logoColor=white">
<img src="https://img.shields.io/badge/Vercel-000000?style=for-the-badge&logo=Vercel&logoColor=white">
<img src="https://img.shields.io/badge/Stromp-353535?style=for-the-badge&logoColor=white">
</div>

### ✔ Back-end
<div>
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white"/>
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"/>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>
<img src="https://img.shields.io/badge/Elasticsearch-005571?style=for-the-badge&logo=Elasticsearch&logoColor=white"/>
<img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=AmazonEC2&logoColor=white"/>
<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white"/>
<img src="https://img.shields.io/badge/Socket.io-010101?style=for-the-badge&logo=Socket.io&logoColor=white"/>
<img src="https://img.shields.io/badge/Stromp-353535?style=for-the-badge&logoColor=white">
<img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white"/>
<img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=for-the-badge&logo=Amazon RDS&logoColor=white"/>
</div>

### ✔ Dev tools
<div>
<img src="https://img.shields.io/badge/Visual Studio Code-007ACC?style=for-the-badge&logo=Visual Studio Code&logoColor=white">
<img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=for-the-badge&logo=IntelliJ IDEA&logoColor=white"/>
<img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white"/>
<img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white"/>
<img src="https://img.shields.io/badge/KakaoTalk-FFCD00?style=for-the-badge&logo=KakaoTalk&logoColor=black"/>
<img src="https://img.shields.io/badge/PWA-5A0FC8?style=for-the-badge&logo=PWA&logoColor=white">
</div>

<br><br>

## 📝 기술 스택 & 라이브러리 사용 이유
| **기술 스택** | **사용이유** |
| :--- | :--- |
| Redux ToolKit |가장 유명한 전역 상태 관리 라이브러리 인 Redux에서 추가적으로 immer와 thunk가 내장 되어 업그레이드 된 라이브러리, 게시물 업로드 및 예약 상태 관리에 있어서 각 컴포넌트 별 State 값이 공통적으로 적용되어야 하기에 사용 하였음 |
| STOMPJS/SOCKJS | 프로젝트 협업 BackEnd서버가 node.js가 아닌 spring.java로 가장 호환성이 좋은 라이브러리로 Billy프로젝트의 가장 핵심 기능인 채팅을 구현하기 위하여 사용 |
| PWA | 모바일 최적화를 위하여 실제 IOS 혹은 google 앱 배포가 아니지만 최대한 비슷한 느낌을 주어 웹과 앱의 이질감을 최소화 |
| 카카오 간편 로그인 | 모바일 최적화 프로젝트를 고려하여 한국인이 가장 많이 이용한다는 카카오 간편 로그인으로 별도의 회원 가입 없이 유저의 접근성 극대화 |
| 카카오 지도 API | - 대여하는 장소를 단순한 주소 검색으로 하여 Text로 표시 해주는게 아닌 Map을 활용한 친절한 UI/UX를 제공 하기 위하여 카카오 지도 API를 사용 <br> - 비슷한 기능을 하는 API인 구글/네이버 지도가 존재하였지만 카카오 간편 로그인을 통하여 이미 발급 받은 엑세스key로 카카오 지도 API도 사용 가능하였기에 코드의 부담을 한줄이라도 덜 수 있어서 카카오 지도 API를 선택 |
| SWIPER | React 개발자들이 가장 많이 사용하는 이미지 슬라이더 라이브러리로 사용 방법 및 최적화 관련한 자료가 많아서 사용  |
| Browser-Image-Compression | FrontEnd에서 이미지 업로드 크기를 최적화 하여 BackEnd 서버로 전달 해야 하기 때문에 사용된 라이브러리로 자바스크립트 기반 이미지 압축이라 API 사용법이 매우 간편하여 선택 |
| React-Multi-Date-Picker | 예약 서비스를 제공 해야 하는 프로젝트 특성상 사용하게 된 달력 라이브러리로 단일 일자 선택 혹은 기간 선택도 가능하여 사용하게 되었지만 CSS적인 부분을 커스텀하기 어려운 단점이 있었음 |
| React-Router-Dom | SPA방식의 React에 주소창을 변경하였을 때 다른 컴포넌트가 보여지도록 하기 위하여 사용한 라이브러리 |
| Node-Sass(SCSS) | css를 대체하는 scss 사용으로 설치한 라이브러리, css컴포넌트 끼리의 호환이 가능한 장점이 있으며 장점은 비슷하지만 전혀 다른 라이브러리인 styled-component의 경우 기능적인 컴포넌트인지 스타일만을 위한 컴포넌트인지 구분하기 힘든 단점이 있기에 node-sass(scss)를 사용 |
| VERCEL | FrontEnd 호스팅 사이트로서 복잡한 절차없이 GitHub 레포지토리를 이용하여 빠른 배포가 가능 하다는 장점이 있고, PWA로 구현된 프로젝트로서 모바일 환경에 맞게 설계되어 수시로 스마트폰에서 적용된 내역을 디버깅 할수 있어야하는데, vercel을 통해 GitHub에 연결하면 완성된 main branch 뿐 아니라 개발중인 dev/개인개발branch 에 push 사항도 휘발성있는 https:// 주소를 알려주어 선택하게된 호스팅 사이트 |

<br>

## 💡 주요 기능
1. 무한 스크롤 ♾
2. 대여 물품 업로드 🆙
3. 실시간 채팅 💬
4. 물품 검색 기능 🔍
5. 캘린더 날짜 설정 후 물품 예약 🗓
6. 예약상품의 5가지 예약 상태 변화 컨트롤 🚦

<br>

| **무한 스크롤** | **대여 물품 업로드** | **실시간 채팅** |
| :---: | :---: | :---: |
| <img src ="https://user-images.githubusercontent.com/74149915/193535101-eca3aebc-200c-4376-af01-837472077983.gif" width="230" height="380" /> | <img src ="https://user-images.githubusercontent.com/74149915/193519145-741bedc8-3d2e-4f0e-bd6a-014aaefc6aec.gif" width="230" height="380"/> | <img src ="https://user-images.githubusercontent.com/74149915/193522250-4178de4d-0b9d-4e76-ace0-23d1fe587860.gif" width="230" height="380" />
| **물품 검색** | **물품 예약** | **5가지 상태 변화** |
| <img src ="https://user-images.githubusercontent.com/74149915/193519161-26ffc1bd-ee1b-4dd0-a7ba-8c6eb82ab685.gif" width="230" height="380" />  | <img src ="https://user-images.githubusercontent.com/74149915/193519175-e1008602-f346-4f36-955f-b4c3392ead9d.gif" width="230" height="380" />  | <img src ="https://user-images.githubusercontent.com/74149915/193522939-ecae888a-2249-4a04-8495-df9a5e2f3db8.gif" width="230" height="380" /> |

<br>

## 🔆 트러블슈팅
  
<details>
<summary> 1. 검색 기능 관련 </summary>
<div markdown="1">

  <br>

‼️ **문제 상황**  : 이전의 JPA의 like절이나 contains절을 사용할 때의 검색 기능은 ex) 강남 자전거, 속초 헤어드라이기 이런식으로 지역과 제목 순으로 띄어쓰기를 포함해서만 검색이 가능한 한계가 있었고, jmeter로 측정한 속도도 향상 시키고자함

  <br>
  
1️⃣ **해결방안 1안** : Hibernate Search

2️⃣ **해결방안 2안** : Elasticsearch

<br>
  
⚖️ **의견 조율** : Hibernate Search는 한글 검색 위주인 현재 프로젝트를 위해서는 Lucene Korean Analyzer 아리랑을 도입해야했고 적용 과정이 어려움, Elasticsearch는 한글 형태소 분석기인 nori를 쉽게 설치할 수 있고, Elasticsearch에서 색인된 데이터를 검색하고 시각화하는 기능을 제공해주는 Kibana에서 처음 index를 생성할 때 설정사항으로 글자를 쪼개서 검색할 수 있도록 설정이 가능

  <br>
  
✅ **의견 결정** : Elasticsearch으로 결정

✅ **결과** :  JMeter를 통해 1000명의 User가 동시에 이용했을 때의 평균 속도 17.4% 가량 감소

- JPA를 통한 검색 기능 JMeter Summary Report

<img width="499" alt="1" src="https://user-images.githubusercontent.com/74149915/193532729-46800014-cdb8-427c-b6ea-ce2cc91ae42f.png">

  <br>
  
- Elasticsearch를 통한 검색 기능 JMeter Summary Report

<img width="496" alt="2" src="https://user-images.githubusercontent.com/74149915/193532711-03b1cfba-be68-4aa4-afd4-0cd7df7717bc.png">
  
</div>
</details>


  
<details>
<summary>2. 예약 상태 별 조회 API 설계</summary>
<div markdown="1">       

  <br>
  
‼️ **문제 상황**  : 현재 프로젝트의 주요 기능 중 예약 상태 별 조회에서 빌려주는 사람 입장과 빌리는 사람 입장 모두에서 각 5가지의 상태 별 조회 기능 구현이 필요
  
  <br>

1️⃣ **해결방안 1안** : 빌리는 사람, 빌려주는 사람의 입장에서 각 5가지의 상태 별 API 구현 (총 10개의 API)

2️⃣ **해결방안 2안** : 빌리는 사람, 빌려주는 사람의 입장에서 상태도 매개변수로 넣어주는 API 구현 (총 2개의 API)

  <br>
  
⚖️ **의견 조율** : 1안은 프론트 측에서 만들어진 API를 호출하기만 하면 된다는 장점이 있지만 같은 로직의 API가 너무 많이 생김,  2안은 프론트 측에서 정해놓은 상태값을 전달해주기만 하면 2개의 API로 구현 가능

  <br>
  
✅ **의견 결정** : 1안은 같은 로직을 반복적으로 10번이나 작성하기 때문에 매우 비효율적이라고 판단, 2안이 훨씬 효율적이라고 판단하여 결정

</div>
</details>

<details>
<summary>3. 채팅 관련</summary>
<div markdown="1">       

  <br>
  
‼️ **문제 상황** : 채팅룸을 개설하는 권한은 빌리는 사람이 가지고 있음, 해당 물건을 빌려주는 사람의 경우 채팅방이 생성이 되었는지 알 수가 없음 빌리는 사람이 채팅방을 개설하고 들어갔을 때,  ENTER TYPE의 메세지를 서버로 전달 하는 과정에서 게시글의 주인도 입력해준다. 위 과정에서 useEffect로 post의 정보를 불러 왔지만, useEffect의 특성상 렌더링 이후에 실행이 되는 함수로 ENTER TYPE메세지를 보낼 때 게시글의주인 데이터가 undefined 으로 정의 되어 제대로 실행이 되지 않음

  <br>
  
1️⃣ **해결방안 1안** : useEffect 내에 async await를 적용하여 dispatch로 불러온뒤 ENTER TYPE메세지를 보내게 끔 설정

2️⃣ **해결방안 2안** : Back-End 서버에서 별도로 invite 하는 로직을 만들어서 초대 알림 등을 보내 입장하였을 때 게시글의 주인이 ENTER가 되도록 설정

  <br>
  
⚖️ **의견 조율** : useEffect 내에 async await 를 적용하는 것이 일반적으로 사용되지 않았지만 callback 함수를 이용하여 콜백함수에 async await을 적용하여 해결 가능 해 보였고, back-end 서버에서 별도의 로직을 만드는데 시간적 소요가 많이 발생할 것으로 보임

  <br>
  
✅ **의견 결정** : 해결 방안 1안으로 결정

</div>
</details>
  
<details>
<summary>4. 예약 상태 Axios 통신 관련</summary>
<div markdown="1">       

  <br>
  
‼️ **문제 상황**  : 예약 상태 변경 버튼을 클릭하면 변경된 예약상태를 출력하지 못한다. 예약 상태변경 후 새로고침을 위해 ‘window.location.replace()’을 추가하여 새로고침이 되지만 첫 화면으로 관심목록 페이지가 보인다. 그리고 spa 프레임워크,라이브러리에서 권장하지 않는 로직이고 예약 상태 변경 후 이후의 작업을 진행하지 않아 새로고침이 필요했다.

  <br>
  
1️⃣ **해결방안 1안** : response값에 데이터를 다시 받아와서 extrareducers에 넣어준다. (ex.filter 메소드사용)

2️⃣ **해결방안 2안** : extrareducers로 변경하지 않고, 현재 예약상태 리스트를 다시 axios로 get하여 반영

  <br>
  
⚖️ **의견 조율** : asxios 통신후 response 값에 success: true / false만 출력이 되어 별도 extrareducer를 변경 할 수 없는 상황. 해결방안 1안의 경우 소요되는 리소스가 크다

  <br>
  
✅ **의견 결정** : 변경된 상태에 대해서 전체 리스트를 get해오는 것으로 결정

</div>
</details>
  
<details>
 <summary> 5. 이미지 Resizing 관련 </summary>
<div markdown="1">       

  <br>
  
‼️ **문제 상황** : Main 페이지 및 포스트 상세 조회 시 업로드 이미지 크기별로 다른 1)렌더링 속도가 나타남 2)Lighthouse 성능 점수도 37점으로 낮은 점수로 나왔고 그 이유 대부분은 이미지 크기와 관련되어 있었다.

![무한스크롤_error](https://user-images.githubusercontent.com/74149915/193534389-8e4fed64-09d0-4daa-b262-407b2d81bf7a.gif)

  <br>
  
<1. 메인 화면 무한 스크롤 이미지>

<img width="600" alt="1" src="https://user-images.githubusercontent.com/74149915/193534351-c2c6deb9-1034-45da-ba4a-2fa4d1c245e9.png">
 
<br>
  
<2. 이미지 리사이징 전 Lighthouse 성능 측정 이미지>

  <br>
  
1️⃣ **해결 방안 1안** : FrontEnd 측에서 라이브러리를 사용하여 최대 이미지 설정한 뒤 BackEnd 서버로 데이터를 보내주어 이미지 크기를 줄이는 법

2️⃣ **해결 방안 2안** : FrontEnd 에서 현재와 같이 업로드 이미지 사이즈 제한 없이 보낸 뒤 BackEnd에서 S3로 업로드시 이미지 사이즈를 정하여 저장 하는 방법

두 방안 모두 이미지가 쓰이는 가장 큰 크기(420px)를 최대로 하여 저장하는 방법을 고려 하였다.

  <br>
  
⚖️ **의견 조율** : 해결 방안 2안의 경우 클라이언트에서 가공되지 않는 큰 이미지를 보내는 경우 서버에 부담이 생길 수 있고, 해결 방안 1안으로 진행 하였을 경우 업로드 전 이미지 미리보기 부터 최적화된 이미지 사이즈를 선택 할 수 있다. 

  <br>
  
✅ **의견 결정** : 해결 방안 1안으로 결정을 하여 아래와 같이 성능이 향상 되었다.

</div>
</details>
  
  <br>
  
## 👻 Billy 팀원들!

|Role|Name|Github|
|---|---|---|
|팀장*BE|김보슬|https://github.com/keembogeul|
|BE|류지우|https://github.com/ryujiwoo184|
|BE|민영기|https://github.com/minyoungki|
|부팀장*FE|나소나|https://github.com/4775614|
|FE|서윤원|https://github.com/Yoonwonsuh|
|FE|김은경|https://github.com/GinaEunK|
|UX/UI|권채은||

<br>
