# ✨ 프로젝트 소개 ✨

![image](https://user-images.githubusercontent.com/83330576/193493626-9350d558-50c1-4f3c-adc3-a3d7b7919b0a.png)

<br>
<h2><a href="https://billyproject.shop" dir="auto">🤩 빌리 서비스 바로가기</a></h2>
<h2><a href="https://gossamer-bath-a39.notion.site/Billy-1c64df5f1eb64281aad11b693835e3bc" dir="auto">😎 빌리 기획 바로가기</a></h2>
<br>
😆 프로젝트 Git address<br>
Back-end Github https://github.com/Billy-Project-Team1/Backend<br>
Front-end Github https://github.com/Billy-Project-Team1/Frontend<br>

## <h2>🏗 서비스 아키텍쳐</h2>
![image](https://user-images.githubusercontent.com/83330576/193495609-2b7f78ad-6191-4014-9b8c-9c2cc5ba37db.png)

## <h2>🍀 주요 기술</h2>
<details>
  <summary> GitHub Actions / CodeDeploy / Amazon S3를 사용한 자동 배포 </summary>
  <ul dir="auto">
    <li>
      유저의 측면에서 서버가 중단되어 서비스를 이용하지 못하는 상황을 막고자
GitHub Action으로 빌드를 진행하고 빌드한 파일을 S3에 업로드한 후
Codedeploy에서 사전에 작성된 script를 기반으로 자동 배포를 할 수 있도록 구현
    </li>
  </ul>
</details>

<details>
  <summary> JWT </summary>
  <ul dir="auto">
    <li>
      권한 부여 :  사용자가 로그인하면 이후의 각 요청에는 JWT가 포함되어 사용자가 해당 토큰으로 허용된 경로, 서비스 및 리소스에 액세스할 수 있음
    </li>
    <li>
      정보 교환 :  JSON 웹 토큰은 당사자 간에 정보를 안전하게 전송하는 좋은 방법
JWT는 보낸 사람이 자신이 누구인지 확인할 수 있음
또한 헤더와 페이로드를 사용하여 서명을 계산하므로 내용이 조작되지 않았는지 확인할 수도 있음
    </li>
  </ul>
</details>

<details>
  <summary> Route53 </summary>
  <ul dir="auto">
    <li>
      지연시간 기반 연결(Latency Based Routing) : 도메인 하나에 각 지역별로 가장 빠른 곳으로 연결해주는 서비시로 접속자하는 사용자가 전세계 어디서든 가장 반응이 빠른 네임서버로로 연결해주는 아주 훌륭한 서비스
    </li>
    <li>
      속도가 아주 빠른 유료 DNS 기반 : 일반적으로 한국의 도메인 주소 서비스 업체에서 무료로 제공하는 네임서버는 대부분 4개 이하로써 고정되어있는데 이로 인하여 해당 호스팅사에서 트래픽이 증가하면 네임서버 연결 단에서 먼저 시간 지체(Latency)가 일어나기 시작함. Route 53 에서는 해당 지역 리전의 가용 영역(Avaiable Area)에서 작동되는 수 천대의 네임서버에서 서버로드가 가장 작은 무작위 순서를 정해 할당하므로 네임서버의 동작 속도가 무척 빠름
    </li>
    <li>
      헬스 체크와 Fail Over : Route53은 자체적으로 Health check 기능을 가지고 있음. 즉, 하나의 DNS 명에 대해서 여러개의 IP 주소를 반환할 수 있는데, 해당 ip의 서버의 상태를 체크해서 장애 상태인 경우에는 네임서버의 리스에서 제외하고 있다가 장애가 복구 되면 다시 리스트에 추가하는 형태이므로 웹서버의 셧 다운타임을 최소로 하고 웹 사이트 반응 속도를 최대로 할 수 있는 기반 기술을 제공
    </li>
  </ul>
</details>

<details>
  <summary> Stomp </summary>
  <ul dir="auto">
    <li>
      Spring Security와 JWT를 연동해 메세지 보호 가능
    </li>
    <li>
      WebSocket만 사용해서 구현하면 요청,메시지 통신 과정을 처리하는 부분을 전부 구현해야 하지만 STOMP를 사용하면 기본적으로 pub/sub 구조로 되어있어 메시지를 발송하고, 메시지를 받아 처리하는 부분이 확실히 정해져 있기 때문에 개발하는 입장에서 명확하게 인지하고 개발할 수 있는 이점이 있습니다.
    </li>
  </ul>
</details>


<details>
  <summary> Elasticsearch </summary>
  <ul dir="auto">
    <li>JPA의 like절이나 contains절을 사용해서 검색기능을 구현할 때의 한계점
      <ul>
          <li>검색에 사용할 필드가 title과 detailLocation 두 가지였기 때문에 JPA로만 검색기능을 구현한다면 ex) 강남 자전거, 속초 헤어드라이기 이런식으로 지역과 제목 순으로 띄어쓰기를 포함해서만 검색이 가능한 한계점이 있었음
          </li>
      </ul>
    </li>
    <li>선택지는 Hibernate Search, Elasticsearch 등이 있었음</li>
    <li>두 가지 모두 구현해보았지만 Hibernate Search의 적용에 어려움이 있었고, Elasticsearch는 JPA의 Repository처럼 사용할 수 있었기 때문에 훨씬 간편하게 사용 가능, Elasticsearch 선택</li>
  </ul>
</details>
<details>
  <summary> QueryDSL </summary>
  <ul dir="auto">
    <li>다양한 조회 기능을 구현하고 싶고, 여러 테이블에서 데이터를 뽑아 사용하기 위함</li>
    <li>선택지는 QueryDSL, Native SQL, JOOQ 등이 있었음</li>
    <li>Native SQL은 데이터베이스 의존적, query문 작성이 QueryDSL보다 어려움</li>
    <li>QueryDSL은 Entity 클래스를 기반으로 QueryDSL 쿼리 전용 클래스를 만들어야 하는 단점이 있으나 자동으로 생성가능하며 사용이 매우 쉽고 직관적</li>
    <li>JOOQ은 QueryDSL 과 비교대상이 되는 프레임워크로 QueryDSL과 유사하며 Return 해주는 Class가 Entity가 아닌 별도의 Class 인 것이 단점.그리고 JOOQ은 유료데이터베이스에 대해서는 유료로 사용해야함</li>
    <li>현재 데이터베이스를 mysql을 사용하여 JOOQ 또한 무료로 사용 가능하지만 앞으로 계속 사용해볼 수 있는 확장성까지 고려해봤을 때 QueryDSL 사용해보는 것을 선택</li>
  </ul>
</details>

## <h2>🛠 트러블슈팅</h2>
<details>
  <summary> 검색 기능 관련 </summary>
  <ul dir="auto">
    <li><strong>문제 상황</strong>
        <ul dir="auto">
          <li>이전의 JPA의 like절이나 contains절을 사용할 때의 검색 기능은 ex) 강남 자전거, 속초 헤어드라이기 이런식으로 지역과 제목 순으로 띄어쓰기를 포함해서만 검색이 가능한 한계가 있었고, jmeter로 측정한 속도도 향상 시키고자함</li>
        </ul>
    </li>
    <li><strong>해결방안 1안</strong>
      <ul dir="auto">
        <li>Hibernate Search</li>
      </ul>
    </li>
    <li><strong>해결방안 2안</strong>
      <ul dir="auto">
        <li>Elasticsearch<br>
      </ul>
    </li>
    <li><strong>의견 조율</strong>
    <ul dir="auto">
      <li>Hibernate Search는 한글 검색 위주인 현재 프로젝트를 위해서는 Lucene Korean Analyzer 아리랑을 도입해야했고 적용 과정이 어려움, Elasticsearch는 한글 형태소 분석기인 nori를 쉽게 설치할 수 있고, Elasticsearch에서 색인된 데이터를 검색하고 시각화하는 기능을 제공해주는 Kibana에서 처음 index를 생성할 때 설정사항으로 글자를 쪼개서 검색할 수 있도록 설정이 가능</li>
      </ul>
    </li>
    <li><strong>의견 결정</strong>
      <ul dir="auto">
        <li>Elasticsearch으로 결정</li>
      </ul>
    </li>
    <li><strong>결과</strong>
      <ul dir="auto">
        <li>
          JMeter를 통해 1000명의 User가 동시에 이용했을 때의 평균 속도 10% 가량 감소
            <ul dir="auto">
              <li>
                JPA를 통한 검색 기능 JMeter Summary Report<br>
                <img width="496" alt="Screen Shot 2022-10-03 at 3 09 44 PM" src="https://user-images.githubusercontent.com/98302518/193512103-a49e8a31-db5d-4adf-9f9a-c1709765f584.png">
              </li>
              <li>
                Elasticsearch를 통한 검색 기능 JMeter Summary Report<br>
                <img width="499" alt="Screen Shot 2022-10-03 at 3 09 55 PM" src="https://user-images.githubusercontent.com/98302518/193512115-c86ae3d9-88a5-4987-8e9f-1213b149af38.png">
              </li>
            </ul>
        </li>
      </ul>
    </li>
  </ul>
</details>

<details>
  <summary> 예약 상태 별 조회 API 설계 </summary>
  <ul dir="auto">
    <li><strong>문제 상황</strong>
        <ul dir="auto">
          <li>현재 프로젝트의 주요 기능 중 예약 상태 별 조회에서 빌려주는 사람 입장과 빌리는 사람 입장 모두에서 각 5가지의 상태 별 조회 기능 구현이 필요</li>
        </ul>
    </li>
    <li><strong>해결방안 1안</strong>
      <ul dir="auto">
        <li>빌리는 사람, 빌려주는 사람의 입장에서 각 5가지의 상태 별 API 구현 (총 10개의 API)</li>
      </ul>
    </li>
    <li><strong>해결방안 2안</strong>
      <ul dir="auto">
        <li>빌리는 사람, 빌려주는 사람의 입장에서 상태도 매개변수로 넣어주는 API 구현 (총 2개의 API)</li>
      </ul>
    </li>
    <li><strong>의견 조율</strong>
    <ul dir="auto">
      <li>1안은 프론트 측에서 만들어진 API를 호출하기만 하면 된다는 장점이 있지만 같은 로직의 API가 너무 많이 생김,  2안은 프론트 측에서 정해놓은 상태값을 전달해주기만 하면 2개의 API로 구현 가능</li>
      </ul>
    </li>
    <li><strong>의견 결정</strong>
      <ul dir="auto">
        <li>1안은 같은 로직을 반복적으로 10번이나 작성하기 때문에 매우 비효율적이라고 판단, 2안이 훨씬 효율적이라고 판단하여 결정</li>
      </ul>
    </li>
  </ul>
</details>

<details>
  <summary> 채팅 관련 </summary>
  <ul dir="auto">
    <li><strong>문제 상황</strong>
        <ul dir="auto">
          <li>채팅룸을 개설하는 권한은 빌리는 사람이 가지고 있음, 해당 물건을 빌려주는 사람의 경우 채팅방이 생성이 되었는지 알 수가 없음 빌리는 사람이 채팅방을 개설하고 들어갔을 때,  ENTER TYPE의 메세지를 서버로 전달 하는 과정에서 게시글의 주인도 입력해준다. 위 과정에서 useEffect로 post의 정보를 불러 왔지만, useEffect의 특성상 렌더링 이후에 실행이 되는 함수로 ENTER TYPE메세지를 보낼 때 게시글의주인 데이터가 undefined 으로 정의 되어 제대로 실행이 되지 않음</li>
        </ul>
    </li>
    <li><strong>해결방안 1안</strong>
      <ul dir="auto">
        <li>useEffect 내에 async await를 적용하여 dispatch로 불러온뒤 ENTER TYPE메세지를 보내게 끔 설정</li>
      </ul>
    </li>
    <li><strong>해결방안 2안</strong>
      <ul dir="auto">
        <li>Back-End 서버에서 별도로 invite 하는 로직을 만들어서 초대 알림 등을 보내 입장하였을 때 게시글의 주인이 ENTER가 되도록 설정</li>
      </ul>
    </li>
    <li><strong>의견 조율</strong>
    <ul dir="auto">
      <li>useEffect 내에 async await 를 적용하는 것이 일반적으로 사용되지 않았지만 callback 함수를 이용하여 콜백함수에 async await을 적용하여 해결 가능 해 보였고, back-end 서버에서 별도의 로직을 만드는데 시간적 소요가 많이 발생할 것으로 보임</li>
      </ul>
    </li>
    <li><strong>의견 결정</strong>
      <ul dir="auto">
        <li>해결 방안 1안으로 결정 </li>
      </ul>
    </li>
  </ul>
</details>

<details>
  <summary> 예약 상태 Axios 통신 관련 </summary>
  <ul dir="auto">
    <li><strong>문제 상황</strong>
        <ul dir="auto">
          <li>예약 상태 변경 버튼을 클릭하면 변경된 예약상태를 출력하지 못한다. 예약 상태변경 후 새로고침을 위해 ‘window.location.replace()’을 추가하여 새로고침이 되지만 첫 화면으로 관심목록 페이지가 보인다. 그리고 spa 프레임워크,라이브러리에서 권장하지 않는 로직이고 예약 상태 변경 후 이후의 작업을 진행하지 않아 새로고침이 필요했다.</li>
        </ul>
    </li>
    <li><strong>해결방안 1안</strong>
      <ul dir="auto">
        <li>response값에 데이터를 다시 받아와서 extrareducers에 넣어준다. (ex.filter 메소드사용)</li>
      </ul>
    </li>
    <li><strong>해결방안 2안</strong>
      <ul dir="auto">
        <li>extrareducers로 변경하지 않고, 현재 예약상태 리스트를 다시 axios로 get하여 반영</li>
      </ul>
    </li>
    <li><strong>의견 조율</strong>
      <ul dir="auto">
        <li>asxios 통신후 response 값에 success: true / false만 출력이 되어 별도 extrareducer를 변경 할 수 없는 상황. 해결방안 1안의 경우 소요되는 리소스가 크다</li>
      </ul>
    </li>
    <li><strong>의견 결정</strong>
      <ul dir="auto">
        <li>변경된 상태에 대해서 전체 리스트를 get해오는 것으로 결정</li>
      </ul>
    </li>
  </ul>
</details>

<details>
  <summary> 이미지 Resizing 관련 </summary>
  <ul dir="auto">
    <li><strong>문제 상황</strong>
        <ul dir="auto">
          <li>Main 페이지 및 포스트 상세 조회 시 업로드 이미지 크기별로 다른 1)렌더링 속도가 나타남 2)Lighthouse 성능 점수도 37점으로 낮은 점수로 나왔고 그 이유 대부분은 이미지 크기와 관련되어 있었다.</li>
        </ul>
    </li>
    <li><strong>해결방안 1안</strong>
      <ul dir="auto">
        <li>FrontEnd 측에서 라이브러리를 사용하여 최대 이미지 설정한 뒤 BackEnd 서버로 데이터를 보내주어 이미지 크기를 줄이는 법</li>
      </ul>
    </li>
    <li><strong>해결방안 2안</strong>
      <ul dir="auto">
        <li>FrontEnd 에서 현재와 같이 업로드 이미지 사이즈 제한 없이 보낸 뒤 BackEnd에서 S3로 업로드시 이미지 사이즈를 정하여 저장 하는 방법
            두 방안 모두 이미지가 쓰이는 가장 큰 크기(420px)를 최대로 하여 저장하는 방법을 고려 하였다.
        </li>
      </ul>
    </li>
    <li><strong>의견 조율</strong>
      <ul dir="auto">
        <li>해결 방안 2안의 경우 클라이언트에서 가공되지 않는 큰 이미지를 보내는 경우 서버에 부담이 생길 수 있고, 해결 방안 1안으로 진행 하였을 경우 업로드 전 이미지 미리보기 부터 최적화된 이미지 사이즈를 선택 할 수 있다. </li>
      </ul>
    </li>
    <li><strong>의견 결정</strong>
      <ul dir="auto">
        <li>해결 방안 1안으로 결정을 하여 아래와 같이 성능이 향상 되었다.</li>
      </ul>
    </li>
  </ul>
</details>


## <h4>👩‍👦‍👦 팀원</h4>
<table>
  <thead>
    <tr>
      <th align="center">이름</th>
      <th align="center">깃허브 주소</th>
      <th align="center">역할</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center">김보슬</td>
      <td align="center"><a href="https://github.com/keembogeul">https://github.com/keembogeul</a></td>
      <td align="center">[리더]</td>
    </tr>
    <tr>
      <td align="center">류지우</td>
      <td align="center"><a href="https://github.com/ryujiwoo184">https://github.com/ryujiwoo184</a></td>
      <td align="center">[팀원]</td>
    </tr>
    <tr>
      <td align="center">민영기</td>
      <td align="center"><a href="https://github.com/minyoungki">https://github.com/minyoungki</a></td>
      <td align="center">[팀원]</td>
    </tr>
  </tbody>
</table>


