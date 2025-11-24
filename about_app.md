# StudyLogApp - 앱 구조 및 기능 상세 분석 보고서

## 목차
1. [프로젝트 구조](#1-프로젝트-구조)
2. [주요 파일 역할](#2-주요-파일-역할)
3. [화면별 기능 상세 분석](#3-화면별-기능-상세-분석)
4. [데이터 흐름 및 아키텍처](#4-데이터-흐름-및-아키텍처)
5. [코드 흐름 다이어그램](#5-코드-흐름-다이어그램)

---

## 1. 프로젝트 구조

### 1.1 폴더/파일 트리 구조

```
mobile-programming-study_log/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── AndroidManifest.xml          # 앱 매니페스트 (권한, Activity 등록)
│   │       ├── java/com/example/studylogapp/
│   │       │   ├── MainActivity.java        # 메인 화면 (진입점)
│   │       │   │
│   │       │   ├── ui/                      # UI 관련 클래스
│   │       │   │   ├── calendar/            # 캘린더 화면 관련
│   │       │   │   │   ├── CalendarActivity.java
│   │       │   │   │   ├── CalendarAdapter.java
│   │       │   │   │   ├── CalendarDay.java
│   │       │   │   │   └── CalendarViewHolder.java
│   │       │   │   │
│   │       │   │   ├── posting/            # 게시물 작성 화면 관련
│   │       │   │   │   ├── PostingActivity.java
│   │       │   │   │   ├── PhotoPickerAdapter.java
│   │       │   │   │   └── PhotoItem.java
│   │       │   │   │
│   │       │   │   ├── viewer/            # 슬라이드 뷰어 화면 관련
│   │       │   │   │   ├── SlideViewerActivity.java
│   │       │   │   │   ├── SlidePostFragment.java
│   │       │   │   │   ├── SlideViewPagerAdapter.java
│   │       │   │   │   └── PhotoSlideAdapter.java
│   │       │   │   │
│   │       │   │   └── settings/          # 설정 화면 관련
│   │       │   │       └── SettingsActivity.java
│   │       │   │
│   │       │   ├── model/                 # 데이터 모델
│   │       │   │   ├── StudyLog.java      # 학습 로그 엔티티
│   │       │   │   ├── StudyPost.java     # 학습 게시물 엔티티
│   │       │   │   └── Quiz.java          # 퀴즈 엔티티
│   │       │   │
│   │       │   ├── database/              # 데이터베이스 관련
│   │       │   │   ├── DatabaseHelper.java # SQLite 헬퍼
│   │       │   │   └── AppDatabase.java    # 데이터베이스 래퍼
│   │       │   │
│   │       │   ├── storage/               # 이미지 저장소 관리
│   │       │   │   └── ImageStorageManager.java
│   │       │   │
│   │       │   ├── alarm/                 # 알람 관련
│   │       │   │   ├── AlarmManagerHelper.java
│   │       │   │   └── AlarmReceiver.java
│   │       │   │
│   │       │   ├── ai/                    # AI 관련
│   │       │   │   └── GeminiQuizGenerator.java # Gemini API를 이용한 퀴즈 생성
│   │       │   │
│   │       │   └── utils/                 # 유틸리티 클래스
│   │       │       ├── DateUtils.java
│   │       │       └── PermissionUtils.java
│   │       │
│   │       └── res/                       # 리소스 파일
│   │           ├── layout/                # 레이아웃 XML
│   │           │   ├── activity_main.xml
│   │           │   ├── activity_calendar.xml
│   │           │   ├── activity_posting.xml
│   │           │   ├── activity_slide_viewer.xml
│   │           │   ├── activity_settings.xml
│   │           │   ├── item_calendar_day.xml
│   │           │   ├── item_photo_picker.xml
│   │           │   ├── item_photo_slide.xml
│   │           │   └── item_slide_post.xml
│   │           │
│   │           ├── values/                # 리소스 값
│   │           │   ├── strings.xml
│   │           │   ├── colors.xml
│   │           │   └── dimens.xml
│   │           │
│   │           └── drawable/              # 아이콘 및 이미지
│   │               ├── ic_launcher.xml
│   │               └── ic_launcher_round.xml
│   │
│   └── build.gradle                       # 앱 빌드 설정
│
├── build.gradle                           # 프로젝트 빌드 설정
├── settings.gradle                        # 프로젝트 설정
└── gradle.properties                     # Gradle 속성
```

---

## 2. 주요 파일 역할

### 2.1 Activity 클래스

| 파일명 | 역할 |
|--------|------|
| `MainActivity.java` | 앱의 진입점으로, 캘린더와 설정 화면으로 이동하는 메인 메뉴를 제공 |
| `CalendarActivity.java` | 월별 캘린더를 표시하고, 날짜 클릭 시 게시물 작성/조회 화면으로 전환. 오늘의 퀴즈 표시 및 풀이 기능 제공 |
| `PostingActivity.java` | 사진 추가, 요약/키워드 입력을 통해 학습 로그를 작성하는 화면. 게시물 저장 시 자동으로 AI 퀴즈 생성 |
| `SlideViewerActivity.java` | 저장된 게시물을 슬라이드 형태로 조회하고, 수정/삭제 기능 제공 |
| `SettingsActivity.java` | 알람 시간 설정 및 데이터 초기화 기능 제공 |

### 2.2 Fragment 클래스

| 파일명 | 역할 |
|--------|------|
| `SlidePostFragment.java` | ViewPager2 내부에서 개별 게시물을 표시하는 Fragment |

### 2.3 Adapter 클래스

| 파일명 | 역할 |
|--------|------|
| `CalendarAdapter.java` | RecyclerView에서 캘린더 그리드를 표시하고 날짜 클릭 이벤트 처리 |
| `PhotoPickerAdapter.java` | 게시물 작성 화면에서 선택한 사진 목록을 표시하고 편집 기능 제공 |
| `SlideViewPagerAdapter.java` | ViewPager2에서 여러 게시물을 Fragment로 전환하는 어댑터 |
| `PhotoSlideAdapter.java` | Fragment 내부에서 사진을 가로 스크롤로 표시하는 RecyclerView 어댑터 |

### 2.4 Model 클래스

| 파일명 | 역할 |
|--------|------|
| `StudyLog.java` | 날짜별 학습 로그의 메타데이터를 저장하는 엔티티 (날짜, 생성/수정 시간) |
| `StudyPost.java` | 개별 게시물 정보를 저장하는 엔티티 (사진 URI, 요약, 키워드, 순서) |
| `Quiz.java` | AI 생성 퀴즈 정보를 저장하는 엔티티 (문제, 선택지 4개, 정답, 설명) |
| `CalendarDay.java` | 캘린더 셀의 표시 정보를 담는 데이터 클래스 |
| `PhotoItem.java` | 게시물 작성 중 임시로 관리하는 사진 아이템 데이터 클래스 |

### 2.5 Database 클래스

| 파일명 | 역할 |
|--------|------|
| `DatabaseHelper.java` | SQLite 데이터베이스 생성 및 스키마 관리 (StudyLog, StudyPost, Quiz 테이블) |
| `AppDatabase.java` | 데이터베이스 CRUD 작업을 캡슐화한 래퍼 클래스 (StudyLog, StudyPost, Quiz) |

### 2.6 Utility 클래스

| 파일명 | 역할 |
|--------|------|
| `DateUtils.java` | 날짜 포맷팅, 파싱, 캘린더 계산 등의 날짜 관련 유틸리티 함수 제공 |
| `PermissionUtils.java` | 카메라, 저장소, 알림 권한 요청 및 확인 기능 제공 |
| `ImageStorageManager.java` | 이미지 파일 저장, 로드, 삭제를 관리하는 스토리지 매니저 |
| `AlarmManagerHelper.java` | 일일 알람 스케줄링 및 설정 관리 (SharedPreferences 사용) |
| `AlarmReceiver.java` | 알람 수신 시 게시물 작성 화면을 자동으로 열어주는 BroadcastReceiver |
| `GeminiQuizGenerator.java` | Google Gemini API를 활용하여 게시물 기반 퀴즈를 자동 생성하는 클래스 |

---

## 3. 화면별 기능 상세 분석

### 3.1 MainActivity (메인 화면)

#### 3.1.1 레이아웃 파일
- **파일**: `activity_main.xml`
- **구조**: 
  - `TextView`: 앱 이름 표시
  - `Button (btn_calendar)`: 캘린더 화면으로 이동
  - `Button (btn_settings)`: 설정 화면으로 이동

#### 3.1.2 주요 기능 및 코드 흐름

**동작 과정:**
1. `onCreate()` 호출 → `setContentView(R.layout.activity_main)`로 레이아웃 로드
2. `AlarmManagerHelper.scheduleDailyAlarm(this)` 호출하여 일일 알람 자동 설정
3. `findViewById()`로 버튼 참조 획득
4. `btnCalendar.setOnClickListener()`: 람다식으로 클릭 이벤트 핸들러 등록
5. 클릭 시 `Intent` 생성 → `CalendarActivity.class` 지정
6. `startActivity(intent)` 호출 → 화면 전환

**Intent 전달 과정:**
```
사용자 클릭 (btn_calendar)
    ↓
onClick 이벤트 발생
    ↓
Intent 생성: new Intent(MainActivity.this, CalendarActivity.class)
    ↓
startActivity(intent)
    ↓
Android 시스템이 CalendarActivity 인스턴스 생성 및 시작
    ↓
CalendarActivity.onCreate() 호출
```

---

### 3.2 CalendarActivity (캘린더 화면)

#### 3.2.1 레이아웃 파일
- **파일**: `activity_calendar.xml`
- **구조**:
  - `LinearLayout (horizontal)`: 이전/다음 월 버튼과 월/년도 텍스트
  - `RecyclerView (recycler_calendar)`: 7열 그리드로 캘린더 날짜 표시
  - `LinearLayout (quiz_container)`: 오늘의 퀴즈 영역 (문제, 선택지 4개, 결과, 설명)

#### 3.2.2 주요 기능 및 코드 흐름

**1. 초기화 과정**

**동작 과정:**
1. 데이터베이스 초기화: `AppDatabase` 인스턴스 생성 및 `open()` 호출
2. 현재 월 설정: `Calendar.getInstance()`로 현재 날짜 가져오고 1일로 설정
3. RecyclerView 설정: `GridLayoutManager`로 7열 그리드 구성
4. Adapter 설정: `CalendarAdapter` 생성 및 데이터베이스 전달
5. 날짜 클릭 리스너 등록: `setOnDateClickListener()`로 날짜 클릭 시 동작 정의
6. 퀴즈 UI 초기화: `initQuizUI()`로 퀴즈 관련 UI 요소 초기화
7. 오늘의 퀴즈 로드: `loadTodayQuiz()`로 오늘 날짜의 퀴즈 조회 및 표시

**2. 날짜 클릭 이벤트 처리 흐름**

```
사용자가 캘린더 날짜 클릭
    ↓
CalendarAdapter.onBindViewHolder()에서 itemView.setOnClickListener() 등록
    ↓
CalendarDay의 date 정보를 DateUtils.formatDate()로 문자열 변환
    ↓
listener.onDateClick(dateStr) 호출
    ↓
CalendarActivity의 람다식 실행
    ↓
database.getPostsByDate(date)로 해당 날짜 게시물 조회
    ↓
조건 분기:
    ├─ 게시물 있음 → SlideViewerActivity로 Intent 전달
    └─ 게시물 없음 → PostingActivity로 Intent 전달
    ↓
startActivity(intent)로 화면 전환
```

**3. CalendarAdapter의 날짜 생성 로직**

`generateDays()` 메서드에서 캘린더 날짜 데이터를 생성합니다.

**데이터 생성 흐름:**
1. 요일 헤더 7개 추가 (일~토)
2. 첫 날의 요일 계산 → 빈 셀 추가로 정렬
3. 데이터베이스에서 해당 월의 게시물이 있는 날짜 목록 조회
4. 1일부터 말일까지 반복:
   - 날짜 문자열 생성 (`DateUtils.formatDate()`)
   - 게시물 존재 여부 확인 (`datesWithPosts.contains()`)
   - 오늘 날짜 여부 확인 (`DateUtils.isToday()`)
   - 썸네일 URI 조회 (`database.getThumbnailUri()`)
   - `CalendarDay` 객체 생성 및 리스트에 추가

**4. 월 변경 버튼 처리**

`btnPrevMonth`와 `btnNextMonth` 클릭 시 `currentMonth.add(Calendar.MONTH, ±1)`로 월을 변경하고 `updateCalendar()`를 호출합니다.

`updateCalendar()` 메서드는 `DateUtils.formatMonthYear()`로 월/년도 텍스트를 업데이트하고 `adapter.updateMonth()`를 호출합니다.

**월 변경 흐름:**
```
버튼 클릭
    ↓
currentMonth.add(Calendar.MONTH, ±1)로 월 변경
    ↓
updateCalendar() 호출
    ↓
tvMonthYear.setText()로 월/년도 텍스트 업데이트
    ↓
adapter.updateMonth() 호출
    ↓
CalendarAdapter.generateDays() 재실행
    ↓
adapter.notifyDataSetChanged()로 RecyclerView 갱신
```

**5. 퀴즈 기능**

**퀴즈 로드 및 표시:**
```
CalendarActivity.onCreate()에서 loadTodayQuiz() 호출
    ↓
오늘 날짜 문자열 생성 (DateUtils.formatDate())
    ↓
database.getQuizByDate(today)로 퀴즈 조회
    ↓
조건 분기:
    ├─ 퀴즈 있음:
    │   └─ displayQuiz(quiz) 호출
    │       ├─ 문제, 선택지 4개 표시
    │       ├─ 선택지 버튼 클릭 리스너 등록
    │       └─ isQuizAnswered = false 설정
    │
    └─ 퀴즈 없음:
        └─ 안내 메시지 표시 ("아직 퀴즈가 없습니다...")
```

**퀴즈 풀이 흐름:**
```
사용자가 선택지 버튼 클릭
    ↓
onQuizOptionSelected(selectedOption) 호출
    ↓
isQuizAnswered 체크 → 이미 답변했으면 무시
    ↓
isQuizAnswered = true 설정
    ↓
정답 여부 확인:
    ├─ 정답인 경우:
    │   ├─ 선택한 버튼 배경색 변경 (초록색)
    │   ├─ tvQuizResult에 "정답입니다!" 표시
    │   └─ tvQuizExplanation에 설명 표시
    │
    └─ 오답인 경우:
        ├─ 선택한 버튼 배경색 변경 (빨간색)
        ├─ 정답 버튼 배경색 변경 (초록색)
        ├─ tvQuizResult에 "오답입니다" 표시
        └─ tvQuizExplanation에 설명 표시
```

---

### 3.3 PostingActivity (게시물 작성 화면)

#### 3.3.1 레이아웃 파일
- **파일**: `activity_posting.xml`
- **구조**:
  - `TextView (tv_date)`: 선택된 날짜 표시
  - `Button (btn_add_photo)`: 사진 추가 버튼
  - `RecyclerView (recycler_photos)`: 선택한 사진 목록 (가로 스크롤)
  - `Button (btn_save)`: 저장 버튼
  - `Button (btn_cancel)`: 취소 버튼

#### 3.3.2 주요 기능 및 코드 흐름

**1. 초기화 및 기존 데이터 로드**

`onCreate()` 메서드에서 초기화를 수행합니다.

**초기화 과정:**
1. `setContentView()`로 레이아웃 로드
2. `AppDatabase` 인스턴스 생성 및 `open()` 호출
3. `ImageStorageManager` 인스턴스 생성
4. `getIntent().getStringExtra("date")`로 날짜 추출 (없으면 `DateUtils.formatDate()`로 현재 날짜 사용)
5. `findViewById()`로 UI 요소 참조 획득
6. `DateUtils.parseDate()`로 날짜 파싱 후 `tvDate.setText()`로 표시
7. `PhotoPickerAdapter` 생성 및 `RecyclerView` 설정
8. `loadExistingData()`로 기존 게시물 로드
9. 버튼 클릭 리스너 등록:
   - `btnAddPhoto`: `showPhotoSourceDialog()` 호출
   - `btnSave`: `savePost()` 호출
   - `btnCancel`: `finish()` 호출
10. `adapter.setOnDeleteListener()`로 삭제 리스너 등록 (삭제 시 `imageStorage.deleteImage()` 호출)

**2. 사진 추가 프로세스**

`showPhotoSourceDialog()` 메서드에서 사진 소스 선택 다이얼로그를 표시합니다.

**사진 추가 흐름:**
1. `photoItems.size() >= MAX_PHOTOS` 체크 → 초과 시 Toast 표시 후 종료
2. `AlertDialog.Builder`로 카메라/갤러리 선택 다이얼로그 생성
3. 선택에 따라:
   - 카메라: `openCamera()` 호출
     - `PermissionUtils.hasCameraPermission()` 확인
     - 권한 없음 → `PermissionUtils.requestCameraPermission()` 호출
     - 권한 있음 → `Intent(MediaStore.ACTION_IMAGE_CAPTURE)` 생성 후 `startActivityForResult()` 호출
   - 갤러리: `openGallery()` 호출
     - `PermissionUtils.hasStoragePermission()` 확인
     - 권한 없음 → `PermissionUtils.requestStoragePermission()` 호출
     - 권한 있음 → `Intent(Intent.ACTION_PICK)` 생성 후 `startActivityForResult()` 호출

**사진 추가 흐름:**
```
사용자가 "사진 추가" 버튼 클릭
    ↓
showPhotoSourceDialog() 호출
    ↓
MAX_PHOTOS(3개) 체크 → 초과 시 Toast 표시 후 종료
    ↓
AlertDialog로 카메라/갤러리 선택
    ↓
선택에 따라:
    ├─ 카메라: openCamera()
    │   ├─ 권한 확인 (PermissionUtils.hasCameraPermission())
    │   ├─ 권한 없음 → PermissionUtils.requestCameraPermission()
    │   └─ 권한 있음 → Intent(MediaStore.ACTION_IMAGE_CAPTURE) 생성
    │       └─ startActivityForResult(intent, REQUEST_CAMERA)
    │
    └─ 갤러리: openGallery()
        ├─ 권한 확인 (PermissionUtils.hasStoragePermission())
        ├─ 권한 없음 → PermissionUtils.requestStoragePermission()
        └─ 권한 있음 → Intent(Intent.ACTION_PICK) 생성
            └─ startActivityForResult(intent, REQUEST_GALLERY)
```

**3. 사진 선택 결과 처리**

`onActivityResult()` 메서드에서 카메라/갤러리 결과를 처리합니다.

**결과 처리 흐름:**
```
카메라/갤러리 앱에서 사진 선택 완료
    ↓
onActivityResult() 호출
    ↓
requestCode로 구분:
    ├─ REQUEST_CAMERA:
    │   ├─ data.getExtras().get("data")로 Bitmap 추출
    │   └─ imageStorage.saveImage(bitmap)로 파일 저장
    │       └─ 저장 경로 반환
    │
    └─ REQUEST_GALLERY:
        ├─ data.getData()로 Uri 추출
        └─ imageStorage.saveImageFromUri(uri)로 파일 저장
            └─ 저장 경로 반환
    ↓
PhotoItem 객체 생성 (imagePath, "", "")
    ↓
photoItems 리스트에 추가
    ↓
adapter.notifyDataSetChanged()로 RecyclerView 갱신
```

**4. 이미지 저장 과정 (ImageStorageManager)**

`ImageStorageManager.saveImage()` 메서드에서 이미지를 저장합니다.

**저장 과정:**
1. `getImageDirectory()`로 저장 디렉토리 경로 획득
2. 디렉토리가 없으면 `mkdirs()`로 생성
3. 타임스탬프 기반 파일명 생성 (`IMG_yyyyMMdd_HHmmss.jpg`)
4. `FileOutputStream`으로 파일 생성
5. `bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)`로 JPEG 압축 저장
6. 파일 절대 경로 반환

**저장 경로:**
- `context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)/StudyLogImages/`
- 파일명: `IMG_yyyyMMdd_HHmmss.jpg`

**5. 게시물 저장 프로세스**

`savePost()` 메서드에서 게시물을 저장합니다.

**저장 흐름:**
```
사용자가 "저장" 버튼 클릭
    ↓
savePost() 호출
    ↓
photoItems.isEmpty() 체크 → 비어있으면 Toast 후 종료
    ↓
기존 게시물이 있으면 (existingLogId != -1):
    ├─ database.deleteAllPostsByLogId()로 기존 게시물 삭제
    └─ database.deleteQuizByLogId()로 기존 퀴즈 삭제
    ↓
database.getOrCreateStudyLogId(selectedDate)로 StudyLog ID 획득
    ├─ 기존 로그 있음 → 기존 ID 반환
    └─ 기존 로그 없음 → 새 StudyLog 생성 후 ID 반환
    ↓
photoItems 반복:
    ├─ PhotoItem에서 imagePath, summary, keyword 추출
    ├─ StudyPost 객체 생성 (logId, imagePath, summary, keyword, order)
    └─ database.insertStudyPost(post)로 DB 저장
    ↓
퀴즈 생성 (첫 번째 게시물 기반):
    ├─ 첫 번째 PhotoItem의 imagePath, summary, keyword 추출
    ├─ generateQuizAsync() 호출 (별도 스레드에서 실행)
    │   ├─ GeminiQuizGenerator 생성
    │   ├─ generator.generateQuiz() 호출
    │   │   ├─ 이미지 Base64 인코딩 (있는 경우)
    │   │   ├─ 프롬프트 생성
    │   │   ├─ Gemini API 호출 (이미지 포함 또는 텍스트만)
    │   │   └─ 응답 파싱하여 Quiz 객체 생성
    │   ├─ quiz.setStudyLogId(logId) 설정
    │   └─ database.insertQuiz(quiz)로 DB 저장
    └─ UI 스레드에서 Toast 표시
    ↓
Toast 표시
    ↓
setResult(RESULT_OK)로 결과 코드 설정
    ↓
finish()로 Activity 종료 및 이전 화면으로 복귀
```

**6. PhotoPickerAdapter의 데이터 바인딩**

`PhotoPickerAdapter.bind()` 메서드에서 각 아이템을 바인딩합니다.

**바인딩 흐름:**
1. 이미지 로드: `Glide`로 파일 경로에서 이미지 로드 및 표시
2. 텍스트 필드 설정: `etSummary`, `etKeyword`에 기존 값 설정
3. 포커스 이벤트: `setOnFocusChangeListener()`로 포커스 해제 시 `PhotoItem` 업데이트
4. 삭제 버튼: 클릭 시 `deleteListener.onDelete(position)` 호출

---

### 3.4 SlideViewerActivity (슬라이드 뷰어 화면)

#### 3.4.1 레이아웃 파일
- **파일**: `activity_slide_viewer.xml`
- **구조**:
  - `ViewPager2 (viewpager_slides)`: 게시물 슬라이드 표시
  - `LinearLayout (horizontal)`: 편집/삭제 버튼

#### 3.4.2 주요 기능 및 코드 흐름

**1. 초기화 및 게시물 로드**

`onCreate()` 메서드에서 초기화를 수행합니다.

**초기화 과정:**
1. `setContentView()`로 레이아웃 로드
2. `getIntent().getStringExtra("date")`로 날짜 추출 (없으면 `finish()` 호출)
3. `AppDatabase` 인스턴스 생성 및 `open()` 호출
4. `ImageStorageManager` 인스턴스 생성
5. `findViewById()`로 UI 요소 참조 획득
6. `loadPosts()`로 게시물 로드
7. 버튼 클릭 리스너 등록:
   - `btnEdit`: `Intent` 생성 후 `PostingActivity`로 전환 (`startActivityForResult()`)
   - `btnDelete`: `showDeleteConfirmDialog()` 호출

**2. 게시물 로드 및 ViewPager 설정**

`loadPosts()` 메서드에서 게시물을 로드하고 ViewPager를 설정합니다.

**로드 흐름:**
```
loadPosts() 호출
    ↓
database.getPostsByDate(selectedDate)로 게시물 리스트 조회
    ↓
게시물 없음 → Toast 표시 후 finish()
    ↓
게시물 있음:
    ├─ SlideViewPagerAdapter 생성 (posts 전달)
    └─ viewPager.setAdapter(adapter)로 어댑터 설정
```

**3. SlideViewPagerAdapter의 Fragment 생성**

`createFragment()` 메서드에서 `SlidePostFragment.newInstance()`를 호출하여 Fragment를 생성합니다.

**Fragment 생성 흐름:**
```
ViewPager2가 특정 position의 Fragment 요청
    ↓
createFragment(position) 호출
    ↓
posts.get(position)로 StudyPost 객체 획득
    ↓
SlidePostFragment.newInstance(post) 호출
    ↓
Bundle에 post 객체 저장 (Serializable)
    ↓
Fragment 인스턴스 반환
```

**4. SlidePostFragment의 데이터 표시**

`onCreateView()` 메서드에서 Fragment의 뷰를 생성하고 데이터를 바인딩합니다.

**초기화 과정:**
1. `inflater.inflate()`로 레이아웃 인플레이트
2. `findViewById()`로 UI 요소 참조 획득
3. `database.getPostsByLogId()`로 같은 날짜의 모든 게시물 조회
4. `photoUris` 리스트 생성 (각 게시물의 `getPhotoUri()` 호출)
5. `PhotoSlideAdapter` 생성 및 `RecyclerView` 설정
6. 현재 게시물의 인덱스 찾기 후 `scrollToPosition()` 호출
7. `tvSummary.setText()`, `tvKeyword.setText()`로 현재 게시물 정보 표시
8. `addOnScrollListener()`로 스크롤 리스너 등록:
   - 스크롤 완료 시 `findFirstVisibleItemPosition()`으로 현재 위치 확인
   - 해당 위치의 게시물 정보로 `tvSummary`, `tvKeyword` 업데이트

**Fragment 초기화 흐름:**
```
onCreateView() 호출
    ↓
item_slide_post.xml 레이아웃 인플레이트
    ↓
post 객체에서 studyLogId 추출
    ↓
database.getPostsByLogId()로 같은 날짜의 모든 게시물 조회
    ↓
photoUris 리스트 생성
    ↓
PhotoSlideAdapter 생성 및 RecyclerView 설정
    ↓
현재 게시물의 인덱스 찾기
    ↓
recyclerView.scrollToPosition()로 해당 위치로 스크롤
    ↓
tvSummary, tvKeyword에 현재 게시물 정보 표시
    ↓
RecyclerView 스크롤 리스너 등록:
    └─ 스크롤 완료 시 첫 번째 보이는 아이템의 summary/keyword 업데이트
```

**5. 삭제 기능**

`showDeleteConfirmDialog()` 메서드에서 삭제 확인 다이얼로그를 표시하고, 확인 시 `deleteAllPosts()`를 호출합니다.

**삭제 과정:**
1. `AlertDialog.Builder`로 확인 다이얼로그 생성
2. "예" 선택 시 `deleteAllPosts()` 호출:
   - `posts` 리스트 반복:
     - `imageStorage.deleteImage()`로 이미지 파일 삭제
     - `database.deleteStudyPost()`로 DB에서 게시물 삭제
   - `database.deleteStudyLog()`로 StudyLog 삭제
   - Toast 표시 후 `setResult(RESULT_OK)`, `finish()` 호출

**삭제 흐름:**
```
"삭제" 버튼 클릭
    ↓
showDeleteConfirmDialog() 호출
    ↓
AlertDialog 표시
    ↓
사용자가 "예" 선택
    ↓
deleteAllPosts() 호출
    ↓
posts 리스트 반복:
    ├─ imageStorage.deleteImage()로 이미지 파일 삭제
    └─ database.deleteStudyPost()로 DB에서 게시물 삭제
    ↓
database.deleteStudyLog()로 StudyLog 삭제
    ↓
Toast 표시
    ↓
setResult(RESULT_OK) 및 finish()
```

---

### 3.5 SettingsActivity (설정 화면)

#### 3.5.1 레이아웃 파일
- **파일**: `activity_settings.xml`
- **구조**: 알람 시간 표시, 알람 ON/OFF 스위치, 알람 시간 설정 버튼, 데이터 초기화 버튼

#### 3.5.2 주요 기능 및 코드 흐름

**1. 알람 시간 설정**

`showTimePicker()` 메서드에서 알람 시간 설정 다이얼로그를 표시합니다.

**설정 과정:**
1. `AlarmManagerHelper.getAlarmHour()`, `getAlarmMinute()`로 현재 설정값 조회
2. `TimePickerDialog` 생성 및 표시
3. 사용자가 시간 선택 후 확인:
   - `AlarmManagerHelper.setAlarmTime()` 호출 (SharedPreferences 저장 및 알람 재설정)
   - `updateAlarmTimeDisplay()`로 화면 업데이트
   - Toast 표시

**알람 시간 설정 흐름:**
```
"알람 시간 설정" 버튼 클릭
    ↓
showTimePicker() 호출
    ↓
AlarmManagerHelper.getAlarmHour/Minute()로 현재 설정값 조회
    ↓
TimePickerDialog 생성 및 표시
    ↓
사용자가 시간 선택 후 확인
    ↓
AlarmManagerHelper.setAlarmTime() 호출
    ├─ SharedPreferences에 시간 저장
    └─ scheduleDailyAlarm() 호출로 알람 재설정
    ↓
updateAlarmTimeDisplay()로 화면 업데이트
```

**2. 알람 ON/OFF 스위치**

`switchAlarm.setOnCheckedChangeListener()`로 스위치 상태 변경 리스너를 등록합니다.

**스위치 동작:**
- `AlarmManagerHelper.setAlarmEnabled()` 호출
  - `isChecked == true`: `scheduleDailyAlarm()` 호출
  - `isChecked == false`: `cancelAlarm()` 호출
- Toast로 상태 메시지 표시

**스위치 동작:**
- `isChecked == true`: `scheduleDailyAlarm()` 호출
- `isChecked == false`: `cancelAlarm()` 호출

**3. 데이터 초기화**

`showClearDataDialog()` 메서드에서 데이터 초기화 확인 다이얼로그를 표시합니다.

**초기화 과정:**
1. `AlertDialog.Builder`로 확인 다이얼로그 생성
2. "예" 선택 시 `database.clearAllData()` 호출:
   - StudyPost 테이블 전체 삭제
   - Quiz 테이블 전체 삭제
   - StudyLog 테이블 전체 삭제
3. Toast 표시

**초기화 흐름:**
```
"데이터 초기화" 버튼 클릭
    ↓
showClearDataDialog() 호출
    ↓
AlertDialog 표시
    ↓
사용자가 "예" 선택
    ↓
database.clearAllData() 호출
    ├─ StudyPost 테이블 전체 삭제
    ├─ Quiz 테이블 전체 삭제
    └─ StudyLog 테이블 전체 삭제
    ↓
Toast 표시
```

---

## 4. 데이터 흐름 및 아키텍처

### 4.1 데이터베이스 스키마

**StudyLog 테이블:**
```sql
CREATE TABLE StudyLog (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    date TEXT UNIQUE NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

**StudyPost 테이블:**
```sql
CREATE TABLE StudyPost (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    studyLogId INTEGER NOT NULL,
    photoUri TEXT NOT NULL,
    summary TEXT,
    keyword TEXT,
    displayOrder INTEGER NOT NULL,
    FOREIGN KEY(studyLogId) REFERENCES StudyLog(id) ON DELETE CASCADE
)
```

**Quiz 테이블:**
```sql
CREATE TABLE Quiz (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    studyLogId INTEGER NOT NULL,
    question TEXT NOT NULL,
    option1 TEXT NOT NULL,
    option2 TEXT NOT NULL,
    option3 TEXT NOT NULL,
    option4 TEXT NOT NULL,
    correctAnswer INTEGER NOT NULL,
    explanation TEXT,
    FOREIGN KEY(studyLogId) REFERENCES StudyLog(id) ON DELETE CASCADE
)
```

### 4.2 데이터 저장 흐름

```
사용자가 게시물 작성 완료
    ↓
PostingActivity.savePost()
    ↓
1. StudyLog 생성/조회
    └─ AppDatabase.getOrCreateStudyLogId()
        ├─ 기존 로그 있음 → ID 반환
        └─ 기존 로그 없음 → insertStudyLog() → 새 ID 반환
    ↓
2. 각 PhotoItem을 StudyPost로 변환
    └─ AppDatabase.insertStudyPost()
        └─ ContentValues 생성 → database.insert()
    ↓
3. 이미지 파일은 ImageStorageManager로 별도 저장
    └─ 파일 경로만 DB에 저장
    ↓
4. 퀴즈 생성 (비동기)
    └─ generateQuizAsync() 호출
        ├─ ExecutorService로 별도 스레드 실행
        ├─ GeminiQuizGenerator.generateQuiz() 호출
        │   ├─ 프롬프트 생성 (요약, 키워드 포함)
        │   ├─ Retrofit을 통한 Gemini API 호출 (gemini-1.5-flash)
        │   └─ JSON 응답 파싱하여 Quiz 객체 생성
        ├─ quiz.setStudyLogId(logId) 설정
        └─ AppDatabase.insertQuiz()로 DB 저장
```

### 4.3 데이터 조회 흐름

```
캘린더 화면에서 날짜 클릭
    ↓
CalendarActivity에서 database.getPostsByDate(date) 호출
    ↓
AppDatabase.getPostsByDate()
    ├─ getStudyLogByDate(date)로 StudyLog 조회
    └─ getPostsByLogId(logId)로 StudyPost 리스트 조회
        └─ Cursor로 쿼리 실행 → cursorToStudyPost()로 객체 변환
    ↓
게시물 있음 → SlideViewerActivity로 전환
게시물 없음 → PostingActivity로 전환
```

**퀴즈 조회 흐름:**
```
CalendarActivity.onCreate()에서 loadTodayQuiz() 호출
    ↓
오늘 날짜 문자열 생성 (DateUtils.formatDate())
    ↓
AppDatabase.getQuizByDate(today) 호출
    ├─ getStudyLogByDate(today)로 StudyLog 조회
    └─ getQuizByLogId(logId)로 Quiz 조회
        └─ Cursor로 쿼리 실행 → cursorToQuiz()로 객체 변환
    ↓
퀴즈 있음 → displayQuiz()로 UI에 표시
퀴즈 없음 → 안내 메시지 표시
```

### 4.4 이미지 저장소 구조

**저장 위치:**
- `context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)/StudyLogImages/`
- 파일명: `IMG_yyyyMMdd_HHmmss.jpg`

**저장 프로세스:**
1. 카메라: `Bitmap` → `FileOutputStream` → JPEG 압축 (90% 품질)
2. 갤러리: `Uri` → `InputStream` → `Bitmap` → 위와 동일한 저장 과정

---

## 5. 코드 흐름 다이어그램

### 5.1 앱 시작부터 게시물 작성까지의 전체 흐름

```
[앱 시작]
    ↓
MainActivity.onCreate()
    ├─ AlarmManagerHelper.scheduleDailyAlarm() [알람 자동 설정]
    └─ 버튼 클릭 리스너 등록
    ↓
[캘린더 버튼 클릭]
    ↓
Intent 생성 → CalendarActivity 시작
    ↓
CalendarActivity.onCreate()
    ├─ AppDatabase 초기화
    ├─ CalendarAdapter 생성
    └─ 날짜 클릭 리스너 등록
    ↓
[날짜 클릭]
    ↓
database.getPostsByDate(date) 조회
    ↓
조건 분기:
    ├─ 게시물 있음
    │   └─ SlideViewerActivity 시작 (Intent에 date 전달)
    │
    └─ 게시물 없음
        └─ PostingActivity 시작 (Intent에 date 전달)
            ↓
            PostingActivity.onCreate()
            ├─ Intent에서 date 추출
            ├─ 기존 데이터 로드 (loadExistingData())
            └─ 사진 추가 버튼 리스너 등록
            ↓
            [사진 추가 버튼 클릭]
            ↓
            showPhotoSourceDialog()
            ↓
            [카메라 선택]
            ↓
            openCamera()
            ├─ 권한 확인
            └─ Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            ↓
            [사진 촬영 완료]
            ↓
            onActivityResult(REQUEST_CAMERA)
            ├─ Bitmap 추출
            ├─ ImageStorageManager.saveImage()
            └─ PhotoItem 생성 및 리스트 추가
            ↓
            [저장 버튼 클릭]
            ↓
            savePost()
            ├─ 기존 게시물 삭제 (있을 경우)
            ├─ StudyLog ID 획득/생성
            ├─ 각 PhotoItem을 StudyPost로 변환
            └─ database.insertStudyPost() 반복
            ↓
            finish() → CalendarActivity로 복귀
```

### 5.2 게시물 조회 및 수정 흐름

```
[캘린더에서 게시물 있는 날짜 클릭]
    ↓
SlideViewerActivity.onCreate()
    ├─ Intent에서 date 추출
    ├─ loadPosts() 호출
    │   └─ database.getPostsByDate(date)
    └─ SlideViewPagerAdapter 생성
    ↓
ViewPager2가 Fragment 요청
    ↓
SlideViewPagerAdapter.createFragment(position)
    └─ SlidePostFragment.newInstance(post)
    ↓
SlidePostFragment.onCreateView()
    ├─ database.getPostsByLogId()로 모든 게시물 조회
    ├─ PhotoSlideAdapter 생성
    └─ RecyclerView 스크롤 리스너 등록
    ↓
[편집 버튼 클릭]
    ↓
Intent 생성 → PostingActivity 시작 (date 전달)
    ↓
PostingActivity.onCreate()
    └─ loadExistingData()로 기존 게시물 로드
    ↓
[수정 후 저장]
    ↓
savePost()에서 기존 게시물 삭제 후 재저장
    ↓
finish() → SlideViewerActivity로 복귀
    ↓
onActivityResult()에서 loadPosts() 재호출
    └─ ViewPager2 갱신
```

### 5.3 알람 시스템 흐름

```
[앱 시작 시]
    ↓
MainActivity.onCreate()
    └─ AlarmManagerHelper.scheduleDailyAlarm()
        ├─ SharedPreferences에서 설정 조회
        ├─ AlarmManager 인스턴스 획득
        ├─ PendingIntent 생성 (AlarmReceiver)
        └─ alarmManager.setExactAndAllowWhileIdle()
    ↓
[설정된 시간 도달]
    ↓
AlarmReceiver.onReceive()
    ├─ Intent Action 확인
    └─ PostingActivity 시작 (오늘 날짜 전달)
    ↓
[부팅 완료 시]
    ↓
AlarmReceiver.onReceive() (BOOT_COMPLETED)
    └─ AlarmManagerHelper.scheduleDailyAlarm() 재호출
```

### 5.4 권한 요청 흐름

```
[사진 추가 버튼 클릭]
    ↓
showPhotoSourceDialog()
    ↓
[카메라 선택]
    ↓
openCamera()
    ├─ PermissionUtils.hasCameraPermission() 확인
    │   └─ false → PermissionUtils.requestCameraPermission()
    │       └─ ActivityCompat.requestPermissions()
    │           ↓
    │           [사용자 권한 허용/거부]
    │           ↓
    │           onRequestPermissionsResult() 호출
    │           └─ 권한 허용 시 openCamera() 재호출
    │
    └─ true → Intent 생성 및 startActivityForResult()
```

---

## 6. 주요 기술 스택 및 라이브러리

### 6.1 Android 기본 컴포넌트
- **Activity**: 화면 관리
- **Fragment**: ViewPager2 내부 화면 단위
- **RecyclerView**: 리스트/그리드 표시
- **ViewPager2**: 슬라이드 뷰어
- **SQLiteDatabase**: 로컬 데이터 저장
- **SharedPreferences**: 알람 설정 저장
- **AlarmManager**: 일일 알람 스케줄링
- **BroadcastReceiver**: 알람 수신

### 6.2 외부 라이브러리
- **Glide 4.16.0**: 이미지 로딩 및 캐싱
- **Retrofit 2.9.0**: RESTful API 통신을 위한 HTTP 클라이언트
- **Gson 2.10.1**: JSON 직렬화/역직렬화
- **OkHttp 4.12.0**: HTTP 클라이언트 (Retrofit의 기본 클라이언트)
- **OkHttp Logging Interceptor 4.12.0**: HTTP 요청/응답 로깅
- **Material Design Components 1.11.0**: Material Design UI 컴포넌트

### 6.3 권한 관리
- **CAMERA**: 카메라 촬영
- **READ_EXTERNAL_STORAGE / READ_MEDIA_IMAGES**: 갤러리 접근
- **SCHEDULE_EXACT_ALARM**: 정확한 알람 스케줄링
- **POST_NOTIFICATIONS**: 알림 표시
- **인터넷**: Gemini API 호출 (퀴즈 생성 기능 사용 시)

---

## 7. Gemini API 통합 아키텍처 (기술 보고서)

### 7.1 Retrofit 기반 API 클라이언트 구조

앱은 **Retrofit**을 사용하여 Gemini API와 통신하는 모듈화된 아키텍처를 채택했습니다.

#### 7.1.1 ApiClient 싱글톤 패턴

`ApiClient` 클래스는 싱글톤 패턴으로 구현되어 앱 전체에서 단일 Retrofit 인스턴스를 공유합니다.

**초기화 과정:**
1. `StudyLogApplication.onCreate()`에서 `ApiClient.init()` 호출
2. `ApiClient.init()` 내부에서:
   - `HttpLoggingInterceptor` 생성 및 BODY 레벨 로깅 설정
   - `OkHttpClient.Builder`로 클라이언트 구성:
     - 연결 타임아웃: 30초
     - 읽기 타임아웃: 60초
     - 쓰기 타임아웃: 60초
     - 로깅 인터셉터 추가
   - `Retrofit.Builder`로 Retrofit 인스턴스 생성:
     - Base URL: `https://generativelanguage.googleapis.com/`
     - Gson 컨버터 팩토리 추가
     - 구성된 OkHttpClient 설정

**인스턴스 획득:**
- `ApiClient.getClient()`: Retrofit 인스턴스 반환 (초기화 확인 포함)
- `ApiClient.getApiKey()`: `BuildConfig.GEMINI_API_KEY`에서 API 키 반환

#### 7.1.2 GeminiApi 인터페이스

`GeminiApi` 인터페이스는 Retrofit의 어노테이션을 사용하여 API 엔드포인트를 정의합니다.

**메서드:**
- `generateContent()`: `@POST` 어노테이션으로 `v1beta/models/gemini-1.5-flash:generateContent` 엔드포인트 호출
  - `@Query("key")`: API 키를 쿼리 파라미터로 전달
  - `@Body`: `GeminiRequest` 객체를 JSON으로 직렬화하여 요청 본문에 포함

#### 7.1.3 DTO (Data Transfer Object) 구조

**GeminiRequest:**
- `contents`: 요청 콘텐츠 리스트
  - `parts`: 텍스트 파트 리스트
    - `text`: 프롬프트 문자열
- `generationConfig`: 생성 설정
  - `temperature`: 0.7
  - `topK`: 40
  - `topP`: 0.95
  - `maxOutputTokens`: 1024

**GeminiResponse:**
- `candidates`: 응답 후보 리스트
  - `content`: 콘텐츠 객체
    - `parts`: 파트 리스트
      - `text`: 생성된 텍스트
- `error`: 에러 정보 (있는 경우)
  - `code`: HTTP 상태 코드
  - `message`: 에러 메시지
  - `status`: 에러 상태

### 7.2 퀴즈 생성 프로세스

#### 7.2.1 GeminiQuizGenerator 클래스

`GeminiQuizGenerator`는 Retrofit을 통해 Gemini API를 호출하여 퀴즈를 생성합니다.

**주요 메서드:**

1. **`generateQuiz()`**: 
   - `ApiClient.getApiKey()`로 API 키 획득
   - `createPrompt()`로 프롬프트 생성
   - `createRequest()`로 `GeminiRequest` 객체 생성
   - `geminiApi.generateContent()` 호출 (동기 실행, 백그라운드 스레드에서 실행되어야 함)
   - `extractTextFromResponse()`로 응답에서 텍스트 추출
   - `parseQuizResponse()`로 JSON 파싱하여 `Quiz` 객체 생성

2. **`createPrompt()`**:
   - 요약과 키워드를 포함한 프롬프트 문자열 생성
   - JSON 형식 응답 요구사항 명시

3. **`createRequest()`**:
   - `GeminiRequest.Part` 생성 (프롬프트 포함)
   - `GeminiRequest.Content` 생성 (파트 리스트 포함)
   - `GeminiRequest.GenerationConfig` 생성 (온도, topK, topP, maxOutputTokens 설정)
   - `GeminiRequest` 객체 반환

4. **`extractTextFromResponse()`**:
   - `GeminiResponse` 객체에서 `candidates[0].content.parts[0].text` 경로로 텍스트 추출
   - null 체크 및 빈 문자열 검증

5. **`parseQuizResponse()`**:
   - 응답 텍스트에서 JSON 블록 추출 (`{` ~ `}`)
   - `JSONObject`로 파싱
   - `question`, `option1~4`, `correctAnswer`, `explanation` 필드 추출
   - `Quiz` 객체 생성 및 반환

#### 7.2.2 비동기 실행

`PostingActivity.savePost()`에서 `generateQuizAsync()` 메서드를 통해 별도 스레드에서 퀴즈 생성:

1. `ExecutorService`로 백그라운드 스레드 생성
2. `generator.generateQuiz()` 호출 (동기 메서드이지만 별도 스레드에서 실행)
3. 성공 시:
   - `quiz.setStudyLogId()`로 로그 ID 설정
   - `database.insertQuiz()`로 DB 저장
   - `QUIZ_CREATED` 브로드캐스트 전송
4. 실패 시: 에러 로그만 출력

### 7.3 API 키 관리

#### 7.3.1 local.properties 설정

`local.properties` 파일에 `GEMINI_API_KEY` 설정:
```
GEMINI_API_KEY=YOUR_API_KEY_HERE
```

#### 7.3.2 BuildConfig 주입

`app/build.gradle`에서:
1. `local.properties` 파일 읽기
2. `buildConfigField`로 `GEMINI_API_KEY`를 `BuildConfig`에 주입
3. `buildConfig true` 설정 활성화

#### 7.3.3 런타임 사용

- `ApiClient.getApiKey()`: `BuildConfig.GEMINI_API_KEY` 반환
- API 호출 시 `@Query("key")`로 쿼리 파라미터에 포함

### 7.4 에러 처리 및 로깅

#### 7.4.1 HTTP 에러 처리

`generateQuiz()` 내부에서:
- `response.isSuccessful()` 체크
- 실패 시 `response.code()`로 HTTP 상태 코드 확인
- `response.errorBody().string()`로 에러 본문 읽기
- 에러 메시지에서 `"message"` 필드 추출하여 간결하게 로깅

#### 7.4.2 응답 에러 처리

`GeminiResponse.getError()` 체크:
- 에러 객체가 있으면 `error.getCode()`와 `error.getMessage()` 로깅
- null 반환하여 실패 처리

#### 7.4.3 예외 처리

`try-catch` 블록으로:
- 네트워크 예외 (`IOException`)
- JSON 파싱 예외 (`JSONException`)
- 기타 예외 처리
- 간결한 에러 메시지 로깅 (전체 스택 트레이스 제외)

#### 7.4.4 로깅 최적화

- `HttpLoggingInterceptor`: HTTP 요청/응답 본문 전체 로깅 (개발용)
- 에러 메시지: 처음 200자만 출력하여 로그 길이 제한
- API 키: 마스킹 처리 (처음 5자 + "..." + 마지막 5자)

### 7.5 Application 클래스 초기화

`StudyLogApplication` 클래스는 `Application`을 상속받아 앱 시작 시 `ApiClient`를 초기화합니다.

**AndroidManifest.xml 설정:**
- `<application android:name=".StudyLogApplication">`로 등록

**초기화 흐름:**
1. 앱 시작 시 `StudyLogApplication.onCreate()` 호출
2. `ApiClient.init(this)` 호출
3. Retrofit 인스턴스 생성 및 저장
4. 이후 앱 전체에서 `ApiClient.getClient()`로 동일한 인스턴스 사용

### 7.6 성능 최적화

#### 7.6.1 이미지 처리 제거

초기 구현에서는 이미지를 Base64로 인코딩하여 API에 전송했으나, 성능 문제로 제거:
- 메인 스레드 블로킹 방지
- 네트워크 전송량 감소
- API 응답 시간 단축

현재는 텍스트(요약, 키워드)만 사용하여 퀴즈 생성.

#### 7.6.2 타임아웃 설정

- 연결 타임아웃: 30초 (네트워크 지연 대응)
- 읽기/쓰기 타임아웃: 60초 (대용량 응답 처리)

#### 7.6.3 동기 호출

`generateQuiz()`는 동기 메서드로 구현되어 `ExecutorService`에서 실행:
- Retrofit의 `Call.execute()` 사용
- 비동기 콜백 대신 동기 호출로 에러 처리 단순화
- 별도 스레드에서 실행되므로 UI 블로킹 없음

---

## 8. 데이터 전환 과정 상세

### 7.1 PhotoItem → StudyPost 변환

```
PhotoItem (임시 데이터)
    ├─ imagePath: String
    ├─ summary: String
    └─ keyword: String
    ↓
savePost()에서 변환
    ↓
StudyPost (영구 데이터)
    ├─ id: long (DB 자동 생성)
    ├─ studyLogId: long
    ├─ photoUri: String (imagePath와 동일)
    ├─ summary: String
    ├─ keyword: String
    └─ order: int (순서)
```

### 7.2 StudyPost → 화면 표시 데이터 변환

```
StudyPost (DB)
    ↓
SlideViewPagerAdapter.createFragment()
    └─ SlidePostFragment.newInstance(post)
    ↓
SlidePostFragment.onCreateView()
    ├─ database.getPostsByLogId()로 모든 게시물 조회
    └─ photoUris 리스트 생성
    ↓
PhotoSlideAdapter
    └─ RecyclerView에 이미지 표시
```

### 7.3 날짜 데이터 변환

```
Calendar 객체
    ↓
DateUtils.formatDate(calendar)
    └─ "yyyy-MM-dd" 형식 문자열
    ↓
Intent.putExtra("date", dateString)
    ↓
다른 Activity에서 getIntent().getStringExtra("date")
    ↓
DateUtils.parseDate(dateString)
    └─ Calendar 객체로 복원
```

---

## 9. 이벤트 핸들러 체인

### 8.1 버튼 클릭 이벤트 체인

```
View (XML 레이아웃)
    ↓
findViewById(R.id.btn_xxx)
    ↓
setOnClickListener(람다식 또는 리스너)
    ↓
onClick() 메서드 실행
    ↓
비즈니스 로직 처리
    ├─ Intent 생성 및 화면 전환
    ├─ 데이터베이스 작업
    ├─ 다이얼로그 표시
    └─ 기타 작업
```

### 8.2 RecyclerView 아이템 클릭 체인

```
RecyclerView 아이템 클릭
    ↓
ViewHolder.itemView.setOnClickListener()
    ↓
Adapter의 리스너 인터페이스 호출
    ↓
Activity/Fragment의 콜백 메서드 실행
    ↓
비즈니스 로직 처리
```

### 8.3 ViewPager2 페이지 전환 체인

```
사용자가 스와이프
    ↓
ViewPager2 내부 스크롤 처리
    ↓
createFragment(position) 호출
    ↓
Fragment 생성 및 표시
    ↓
Fragment의 onCreateView() 실행
    ↓
레이아웃 인플레이트 및 데이터 바인딩
```

---

## 10. 메모리 및 성능 고려사항

### 9.1 이미지 로딩 최적화
- **Glide 사용**: 자동 메모리 캐싱 및 리사이징
- **centerCrop() / fitCenter()**: 이미지 크기 조정

### 9.2 데이터베이스 관리
- **AppDatabase.open()**: Activity 생명주기와 연동
- **AppDatabase.close()**: onDestroy()에서 명시적 종료
- **Cursor 관리**: 자동 close() 처리

### 9.3 Fragment 생명주기
- **ViewPager2 + FragmentStateAdapter**: 메모리 효율적인 Fragment 관리
- **onDestroyView()**: 데이터베이스 연결 해제

---

## 11. 결론

이 앱은 **학습 로그를 날짜별로 관리하는 캘린더 기반 앱**으로, 다음과 같은 특징을 가집니다:

1. **계층적 데이터 구조**: StudyLog (날짜) → StudyPost (게시물) → Quiz (퀴즈)
2. **이미지 관리**: 외부 저장소에 파일로 저장, DB에는 경로만 저장
3. **AI 기반 퀴즈 생성**: Google Gemini API를 활용하여 게시물 기반 복습 퀴즈 자동 생성
4. **사용자 경험**: 캘린더에서 날짜 클릭 시 자동으로 작성/조회 화면 전환
5. **알람 시스템**: 일일 알람으로 학습 습관 형성 지원
6. **권한 관리**: 런타임 권한 요청으로 사용자 친화적 구현

각 화면은 **Intent를 통한 명시적 화면 전환**을 사용하며, **데이터베이스와 이미지 저장소를 분리**하여 관리합니다. **RecyclerView와 ViewPager2를 활용**한 효율적인 UI 구성으로 사용자 경험을 최적화했습니다. **Gemini AI를 통한 퀴즈 생성 기능**으로 학습 내용을 더 효과적으로 복습할 수 있도록 지원합니다.

### 주요 개선 사항
- **AI 퀴즈 생성**: 게시물 저장 시 자동으로 복습용 퀴즈 생성
- **퀴즈 풀이 기능**: CalendarActivity에서 오늘의 퀴즈를 풀고 정답 확인 가능
- **비동기 처리**: 퀴즈 생성은 별도 스레드에서 실행되어 UI 블로킹 방지

