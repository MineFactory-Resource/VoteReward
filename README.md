VoteReward
=======
----
플러그인 소개
-----------
추천 보상 플러그인입니다.

----
플러그인 적용 방법
-----------
1. [release](https://github.com/MineFactory-Resource/VoteReward/releases) 항목에서 최신 버전의 플러그인 파일을 다운로드
2. 서버 속 plugins 폴더에 다운로드 받은 *.jar 파일을 넣기

----
config.yml 사용 방법
-----------
해당 플러그인의 config.yml에는 두가지 설정이 존재합니다.
1. 기본 설정
    ```yaml
    config:
      recommend:
        count: 2
        message: '&f추천 2회를 달성하여 추가 보상이 지급됩니다.'
        broadcast: '%player%님께서 추천을 2회 모두 하였습니다.'
        commands:
          - 'give %player% minecraft:stone 3'
      messages:
        not_rec: '오늘 서버 추천을 모두하지 않았습니다.'
        rec: '사이트를 전부 추천하셨습니다.'
        rec_list: '현재 추천하지 않은 사이트: '
    ```
   추천을 n회 하였을 경우 추가 보상을 지급합니다.
   플레이어가 n회 하였을 경우 서버에 뜨는 메세지(broadcast)와 플레이어에게 전송되는 메세지(message)를 입력할 수 있습니다.  
   
   또한 플러그인에서 사용되는 메세지 일부를 원하는 메세지로 수정하여 전송할 수 있습니다.
    

2. database 설정
    ```yaml
    mysql:
      enabled: false
      host: '127.0.0.1'
      port: '3306'
      username: 'root'
      password: '1234'
      database: 'minecraft'
      table: 'votereward'
    ```
    비활성화(false)할 경우 players.yml, 활성화(true)할 경우 mysql에 데이터를 저장합니다.
    mysql의 주소, 포트 등의 정보를 입력하여 mysql에 플러그인의 데이터를 저장할 수 있습니다.
   
3. vote 설정
    ```yaml
   vote:
      마인리스트:
        site: 'minelist.kr'
        url: 'minelist.kr'
        message: '&e마인리스트에서 추천을 하여 아이템을 지급받았습니다.'
        broadcast: '%player%님께서 마인리스트에서 추천을 하였습니다.'
        commands:
          - 'give %player% minecraft:stone 1'
      마인페이지:
        site: 'minepage'
        url: 'mine.page'
        message: '&e마인페이지에서 추천을 하여 아이템을 지급받았습니다.'
        broadcast: '%player%님께서 마인페이지에서 추천을 하였습니다.'
        commands:
          - 'give %player% minecraft:stone 2'
    ```
   추천정보를 받을 사이트 정보와 추천시 뜨는 메세지와 보상 정보를 입력하여, 해당사이트에서 추천을 받을 경우 플레이어가 보상을 받도록 설정합니다.  

    기본 양식)
    ```yaml
    vote:
      <이름>:
        site: '<서비스 이름>'
        url: '<사이트 주소>'
        message: '<개인 메세지>'
        broadcast: '<서버 메세지>'
        commands:
          - '<명령어1>'
          - '<명령어2>'...
   ```
   <이름>의 경우 추천하지 않은 사이트 목록에서 쓰일 이름이며(플레이어가 접속 또는 /추천 명령어 입력시 확인 가능), <사이트 주소>의 경우 그 사이트로 링크해주는 역할을 합니다.  
   <서비스 이름>을 통해 해당 사이트가 추천을 했다는 정보를 서버로 전송하였을 때 추천 사이트를 구별하는데 사용됩니다.  
   플레이어가 추천을 하였을 경우 뜨는 메세지(broadcast, message)와 보상 명령어를 입력해 줍니다. ('/'없이 입력)

----
Votifier 지원 정보
-----------
해당 플러그인을 사용하려면 Votifier(NuVotifier) 플러그인이 반드시 필요합니다.
