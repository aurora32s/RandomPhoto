### 당근마켓 안드로이드 엔지니어 과제

### 📌 1. 내용
|내용|날짜|
|-------|---------------|
| 과제 시작일 | 2023.02.20 월 |
| 과제 마감일 | 2023.02.27 월 23:59 |
| 실제 제출일 | 2023.03.02 목 09:00 |

### 🍿 2. 요구사항
#### 2.1 갤러리 앱 구현
```
https://picsum.photos/
```
### 🛠 3. 제한사항
1. 네트워크 관련 라이브러리를 제외한 라이브러리는 사용 불가
2. 이미지 로드 시 DiskCache까지 가능하도록 설계 필요
2. 멀티모듈 구성 권장
3. 테스트코드 작성 권장

### 🔥 4. 문서 링크 정리
(* 열리지 않는 문서가 있다면 반드시 DM으로 말씀 부탁드립니다. 🙏)
|번호|문서명|링크|
|-----|----------|---------------|
|1|API 정리 문서|[API 정리 노션 링크](https://seom-seom.notion.site/Picsum-API-233deb20e25544469142c8376ba4ead2)|
|2|화면기획서|[화면 기획서 링크](https://drive.google.com/file/d/1D72OHRV1FrJeZcKwtMeYneTLgmQMG5Kz/view?usp=sharing)|
|3|Figma UI 기획서|[Figma 링크](https://www.figma.com/file/imArI7vqRy53wX6Q2ogj2r/%EB%8B%B9%EA%B7%BC%ED%8F%AC%ED%86%A0?node-id=0%3A1&t=XnVfBAjTElQ67qOb-1)|
|4|WBS 문서|[WBS 스프레드시트 링크](https://docs.google.com/spreadsheets/d/1eKU6OHMDxXxYqm6o-FdIEMV8gKxX7bSD43O3JEM-2nE/edit?usp=sharing)|
|5|스크럼|[스크럼 노션 링크](https://seom-seom.notion.site/ede478dd358a4b37a016d58e6210f9e6)|
|6|테스트버전 설치 링크|[Firebase 설치링크](https://appdistribution.firebase.dev/i/d4bfb118c5bc92aa)|

### 🪴 5.프로젝트 구조
<img width="1022" alt="스크린샷 2023-03-05 오후 3 57 55" src="https://user-images.githubusercontent.com/22411296/222946433-7a10949d-67a3-41d6-b8b4-f979b25aefe1.png">

### 🍎 사용기술
|분야|기술|
|-----|----------|
|UI|Jetpack Compose(100%)|
|비동기|Kotlin Coroutine(+Flow)|
|DI|Hilt|
|네트워크|Retrofit2,OkHttp|
|테스트|JUnit, Hamcrest, OkHttp mockserver|
|gradle|toml, build-logic|
|추가|Paging3|

<br/>

### 🏠 6. Branch 전략
```
1. 이슈 생성 > PR 생성 > 코드리뷰 > Allow > Merge
2. 자신의 Branch는 자신이 Merge 합니다.
3. 반드시 코드리뷰를 진행한 이후에 Merge 하셔야 합니다.
4. 코드리뷰에 나온 사항 중 차후에 변경할 사항이라면 반드시 이슈생성이 선행적으로 이루어진 후 Merge할 수 있습니다.
```
#### 6.1 Branch 명
```
[이슈카테고리]/[이슈번호]-[작업이름]

ex. Base/#4-create-project
```

#### 6.2 Commit
```
[작업 타입] [#이슈번호] - [작업설명]

ex. [CREATE] #4 - create basic compose project
```
|번호|작업타입|설명|
|-----|----------|---------------|
|1|CREATE|파일 및 프로젝트 생성|
|2|EDIT|파일 수정|
|3|FEAT|새로운 기능 추가|
|4|REFACTOR|기존 기능 수정|
|5|DELETE|기존 코드 제거|
|6|TEST|테스트 코드 추가|

<br/>

#### 6.3 Branch Flow
<img width="840" alt="스크린샷 2023-02-22 오전 12 19 27" src="https://user-images.githubusercontent.com/22411296/220386178-478f7056-de6e-45e0-80cf-fd947f3c3e5c.png">
