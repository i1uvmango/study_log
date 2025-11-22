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
│   │       │   │   └── StudyPost.java     # 학습 게시물 엔티티
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
| `CalendarActivity.java` | 월별 캘린더를 표시하고, 날짜 클릭 시 게시물 작성/조회 화면으로 전환 |
| `PostingActivity.java` | 사진 추가, 요약/키워드 입력을 통해 학습 로그를 작성하는 화면 |
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
| `CalendarDay.java` | 캘린더 셀의 표시 정보를 담는 데이터 클래스 |
| `PhotoItem.java` | 게시물 작성 중 임시로 관리하는 사진 아이템 데이터 클래스 |

### 2.5 Database 클래스

| 파일명 | 역할 |
|--------|------|
| `DatabaseHelper.java` | SQLite 데이터베이스 생성 및 스키마 관리 (StudyLog, StudyPost 테이블) |
| `AppDatabase.java` | 데이터베이스 CRUD 작업을 캡슐화한 래퍼 클래스 |

### 2.6 Utility 클래스

| 파일명 | 역할 |
|--------|------|
| `DateUtils.java` | 날짜 포맷팅, 파싱, 캘린더 계산 등의 날짜 관련 유틸리티 함수 제공 |
| `PermissionUtils.java` | 카메라, 저장소, 알림 권한 요청 및 확인 기능 제공 |
| `ImageStorageManager.java` | 이미지 파일 저장, 로드, 삭제를 관리하는 스토리지 매니저 |
| `AlarmManagerHelper.java` | 일일 알람 스케줄링 및 설정 관리 (SharedPreferences 사용) |
| `AlarmReceiver.java` | 알람 수신 시 게시물 작성 화면을 자동으로 열어주는 BroadcastReceiver |

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

```16:36:app/src/main/java/com/example/studylogapp/MainActivity.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 알람 설정
        AlarmManagerHelper.scheduleDailyAlarm(this);

        Button btnCalendar = findViewById(R.id.btn_calendar);
        Button btnSettings = findViewById(R.id.btn_settings);

        btnCalendar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }
```

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

#### 3.2.2 주요 기능 및 코드 흐름

**1. 초기화 과정**

```31:76:app/src/main/java/com/example/studylogapp/ui/calendar/CalendarActivity.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        database = new AppDatabase(this);
        database.open();

        currentMonth = Calendar.getInstance();
        currentMonth.set(Calendar.DAY_OF_MONTH, 1);

        tvMonthYear = findViewById(R.id.tv_month_year);
        btnPrevMonth = findViewById(R.id.btn_prev_month);
        btnNextMonth = findViewById(R.id.btn_next_month);
        recyclerView = findViewById(R.id.recycler_calendar);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 7));
        adapter = new CalendarAdapter(this, currentMonth, database);
        recyclerView.setAdapter(adapter);

        adapter.setOnDateClickListener(date -> {
            List<StudyPost> posts = database.getPostsByDate(date);
            if (posts != null && !posts.isEmpty()) {
                // 게시물이 있으면 슬라이드 뷰어로 이동
                Intent intent = new Intent(CalendarActivity.this, SlideViewerActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            } else {
                // 게시물이 없으면 작성 화면으로 이동
                Intent intent = new Intent(CalendarActivity.this, PostingActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        btnPrevMonth.setOnClickListener(v -> {
            currentMonth.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        btnNextMonth.setOnClickListener(v -> {
            currentMonth.add(Calendar.MONTH, 1);
            updateCalendar();
        });

        updateCalendar();
    }
```

**동작 과정:**
1. 데이터베이스 초기화: `AppDatabase` 인스턴스 생성 및 `open()` 호출
2. 현재 월 설정: `Calendar.getInstance()`로 현재 날짜 가져오고 1일로 설정
3. RecyclerView 설정: `GridLayoutManager`로 7열 그리드 구성
4. Adapter 설정: `CalendarAdapter` 생성 및 데이터베이스 전달
5. 날짜 클릭 리스너 등록: `setOnDateClickListener()`로 날짜 클릭 시 동작 정의

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

```45:80:app/src/main/java/com/example/studylogapp/ui/calendar/CalendarAdapter.java
    private void generateDays() {
        days.clear();
        
        // 요일 헤더 추가
        String[] weekDays = {"일", "월", "화", "수", "목", "금", "토"};
        for (String day : weekDays) {
            days.add(new CalendarDay(day, true, false, false, null));
        }

        Calendar firstDay = DateUtils.getFirstDayOfMonth(currentMonth);
        int firstDayOfWeek = DateUtils.getFirstDayOfWeek(currentMonth);
        int daysInMonth = DateUtils.getDaysInMonth(currentMonth);

        // 빈 셀 추가
        for (int i = 0; i < firstDayOfWeek; i++) {
            days.add(new CalendarDay("", false, false, false, null));
        }

        // 날짜 셀 추가
        List<String> datesWithPosts = database.getDatesWithPosts(
            currentMonth.get(Calendar.YEAR),
            currentMonth.get(Calendar.MONTH) + 1
        );

        Calendar today = Calendar.getInstance();
        for (int i = 1; i <= daysInMonth; i++) {
            Calendar day = (Calendar) firstDay.clone();
            day.set(Calendar.DAY_OF_MONTH, i);
            String dateStr = DateUtils.formatDate(day);
            boolean hasPost = datesWithPosts.contains(dateStr);
            boolean isToday = DateUtils.isToday(day);
            String thumbnailUri = hasPost ? database.getThumbnailUri(dateStr) : null;
            
            days.add(new CalendarDay(String.valueOf(i), false, isToday, hasPost, thumbnailUri));
        }
    }
```

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

```66:74:app/src/main/java/com/example/studylogapp/ui/calendar/CalendarActivity.java
        btnPrevMonth.setOnClickListener(v -> {
            currentMonth.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        btnNextMonth.setOnClickListener(v -> {
            currentMonth.add(Calendar.MONTH, 1);
            updateCalendar();
        });
```

```79:82:app/src/main/java/com/example/studylogapp/ui/calendar/CalendarActivity.java
    private void updateCalendar() {
        tvMonthYear.setText(DateUtils.formatMonthYear(currentMonth));
        adapter.updateMonth(currentMonth);
    }
```

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

```49:95:app/src/main/java/com/example/studylogapp/ui/posting/PostingActivity.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        database = new AppDatabase(this);
        database.open();
        imageStorage = new ImageStorageManager(this);

        selectedDate = getIntent().getStringExtra("date");
        if (selectedDate == null) {
            selectedDate = DateUtils.formatDate(Calendar.getInstance());
        }

        tvDate = findViewById(R.id.tv_date);
        btnAddPhoto = findViewById(R.id.btn_add_photo);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        recyclerView = findViewById(R.id.recycler_photos);

        Calendar date = DateUtils.parseDate(selectedDate);
        tvDate.setText(String.format(getString(R.string.date_format),
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH) + 1,
            date.get(Calendar.DAY_OF_MONTH)));

        photoItems = new ArrayList<>();
        adapter = new PhotoPickerAdapter(photoItems, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        // 기존 데이터 로드
        loadExistingData();

        btnAddPhoto.setOnClickListener(v -> showPhotoSourceDialog());
        btnSave.setOnClickListener(v -> savePost());
        btnCancel.setOnClickListener(v -> finish());

        adapter.setOnDeleteListener(position -> {
            PhotoItem item = photoItems.get(position);
            if (item.getImagePath() != null) {
                imageStorage.deleteImage(item.getImagePath());
            }
            photoItems.remove(position);
            adapter.notifyDataSetChanged();
        });
    }
```

**Intent 데이터 수신:**
- `getIntent().getStringExtra("date")`로 전달받은 날짜 문자열 추출
- 날짜가 없으면 현재 날짜 사용

**2. 사진 추가 프로세스**

```108:147:app/src/main/java/com/example/studylogapp/ui/posting/PostingActivity.java
    private void showPhotoSourceDialog() {
        if (photoItems.size() >= MAX_PHOTOS) {
            Toast.makeText(this, R.string.max_photos, Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_photo_source);
        builder.setItems(new String[]{getString(R.string.camera), getString(R.string.gallery)},
            (dialog, which) -> {
                if (which == 0) {
                    openCamera();
                } else {
                    openGallery();
                }
            });
        builder.show();
    }

    private void openCamera() {
        if (!PermissionUtils.hasCameraPermission(this)) {
            PermissionUtils.requestCameraPermission(this);
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    private void openGallery() {
        if (!PermissionUtils.hasStoragePermission(this)) {
            PermissionUtils.requestStoragePermission(this);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }
```

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

```149:169:app/src/main/java/com/example/studylogapp/ui/posting/PostingActivity.java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            String imagePath = imageStorage.saveImage(bitmap);
            if (imagePath != null) {
                photoItems.add(new PhotoItem(imagePath, "", ""));
                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == REQUEST_GALLERY && data != null) {
            Uri uri = data.getData();
            String imagePath = imageStorage.saveImageFromUri(uri);
            if (imagePath != null) {
                photoItems.add(new PhotoItem(imagePath, "", ""));
                adapter.notifyDataSetChanged();
            }
        }
    }
```

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

```28:48:app/src/main/java/com/example/studylogapp/storage/ImageStorageManager.java
    public String saveImage(Bitmap bitmap) {
        File imageDir = getImageDirectory();
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";
        File imageFile = new File(imageDir, imageFileName);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            Log.e(TAG, "Error saving image", e);
            return null;
        }
    }
```

**저장 경로:**
- `context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)/StudyLogImages/`
- 파일명: `IMG_yyyyMMdd_HHmmss.jpg`

**5. 게시물 저장 프로세스**

```183:205:app/src/main/java/com/example/studylogapp/ui/posting/PostingActivity.java
    private void savePost() {
        if (photoItems.isEmpty()) {
            Toast.makeText(this, R.string.photo_required, Toast.LENGTH_SHORT).show();
            return;
        }

        // 기존 게시물 삭제
        if (existingLogId != -1) {
            database.deleteAllPostsByLogId(existingLogId);
        }

        // 새 게시물 저장
        long logId = database.getOrCreateStudyLogId(selectedDate);
        for (int i = 0; i < photoItems.size(); i++) {
            PhotoItem item = photoItems.get(i);
            StudyPost post = new StudyPost(logId, item.getImagePath(), item.getSummary(), item.getKeyword(), i);
            database.insertStudyPost(post);
        }

        Toast.makeText(this, R.string.save, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
```

**저장 흐름:**
```
사용자가 "저장" 버튼 클릭
    ↓
savePost() 호출
    ↓
photoItems.isEmpty() 체크 → 비어있으면 Toast 후 종료
    ↓
기존 게시물이 있으면 (existingLogId != -1):
    └─ database.deleteAllPostsByLogId()로 기존 게시물 삭제
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
Toast 표시
    ↓
setResult(RESULT_OK)로 결과 코드 설정
    ↓
finish()로 Activity 종료 및 이전 화면으로 복귀
```

**6. PhotoPickerAdapter의 데이터 바인딩**

```69:99:app/src/main/java/com/example/studylogapp/ui/posting/PhotoPickerAdapter.java
        public void bind(PhotoItem item, int position) {
            File imageFile = new File(item.getImagePath());
            if (imageFile.exists()) {
                Glide.with(itemView.getContext())
                    .load(imageFile)
                    .centerCrop()
                    .into(ivPhoto);
            }

            etSummary.setText(item.getSummary());
            etSummary.setHint(activity.getString(R.string.summary_hint));
            etSummary.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    item.setSummary(etSummary.getText().toString());
                }
            });

            etKeyword.setText(item.getKeyword());
            etKeyword.setHint(activity.getString(R.string.keyword_hint));
            etKeyword.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    item.setKeyword(etKeyword.getText().toString());
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDelete(position);
                }
            });
        }
```

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

```30:58:app/src/main/java/com/example/studylogapp/ui/viewer/SlideViewerActivity.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_viewer);

        selectedDate = getIntent().getStringExtra("date");
        if (selectedDate == null) {
            finish();
            return;
        }

        database = new AppDatabase(this);
        database.open();
        imageStorage = new ImageStorageManager(this);

        viewPager = findViewById(R.id.viewpager_slides);
        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);

        loadPosts();

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(SlideViewerActivity.this, PostingActivity.class);
            intent.putExtra("date", selectedDate);
            startActivityForResult(intent, 100);
        });

        btnDelete.setOnClickListener(v -> showDeleteConfirmDialog());
    }
```

**2. 게시물 로드 및 ViewPager 설정**

```60:70:app/src/main/java/com/example/studylogapp/ui/viewer/SlideViewerActivity.java
    private void loadPosts() {
        posts = database.getPostsByDate(selectedDate);
        if (posts == null || posts.isEmpty()) {
            Toast.makeText(this, R.string.no_photo, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        adapter = new SlideViewPagerAdapter(this, posts);
        viewPager.setAdapter(adapter);
    }
```

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

```22:24:app/src/main/java/com/example/studylogapp/ui/viewer/SlideViewPagerAdapter.java
    public androidx.fragment.app.Fragment createFragment(int position) {
        return SlidePostFragment.newInstance(posts.get(position));
    }
```

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

```49:103:app/src/main/java/com/example/studylogapp/ui/viewer/SlidePostFragment.java
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_slide_post, container, false);
        
        recyclerView = view.findViewById(R.id.viewpager_photos);
        tvSummary = view.findViewById(R.id.tv_summary);
        tvKeyword = view.findViewById(R.id.tv_keyword);

        if (post != null) {
            // 같은 날짜의 모든 게시물 가져오기
            allPosts = database.getPostsByLogId(post.getStudyLogId());
            List<String> photoUris = new ArrayList<>();
            for (StudyPost p : allPosts) {
                photoUris.add(p.getPhotoUri());
            }

            adapter = new PhotoSlideAdapter(requireContext(), photoUris);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);

            // 현재 게시물의 인덱스 찾기
            int currentIndex = 0;
            for (int i = 0; i < allPosts.size(); i++) {
                if (allPosts.get(i).getId() == post.getId()) {
                    currentIndex = i;
                    break;
                }
            }
            recyclerView.scrollToPosition(currentIndex);

            tvSummary.setText(post.getSummary());
            tvKeyword.setText(post.getKeyword());

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        if (layoutManager != null) {
                            int position = layoutManager.findFirstVisibleItemPosition();
                            if (position >= 0 && position < allPosts.size()) {
                                StudyPost currentPost = allPosts.get(position);
                                tvSummary.setText(currentPost.getSummary());
                                tvKeyword.setText(currentPost.getKeyword());
                            }
                        }
                    }
                }
            });
        }

        return view;
    }
```

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

```72:93:app/src/main/java/com/example/studylogapp/ui/viewer/SlideViewerActivity.java
    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.delete)
            .setMessage(R.string.delete_confirm)
            .setPositiveButton(R.string.yes, (dialog, which) -> {
                deleteAllPosts();
            })
            .setNegativeButton(R.string.no, null)
            .show();
    }

    private void deleteAllPosts() {
        for (StudyPost post : posts) {
            imageStorage.deleteImage(post.getPhotoUri());
            database.deleteStudyPost(post.getId());
        }
        database.deleteStudyLog(selectedDate);
        
        Toast.makeText(this, R.string.delete, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
```

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

```59:71:app/src/main/java/com/example/studylogapp/ui/settings/SettingsActivity.java
    private void showTimePicker() {
        int currentHour = AlarmManagerHelper.getAlarmHour(this);
        int currentMinute = AlarmManagerHelper.getAlarmMinute(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
            (view, hourOfDay, minute) -> {
                AlarmManagerHelper.setAlarmTime(SettingsActivity.this, hourOfDay, minute);
                updateAlarmTimeDisplay();
                Toast.makeText(SettingsActivity.this, "알람 시간이 설정되었습니다", Toast.LENGTH_SHORT).show();
            },
            currentHour, currentMinute, true);
        timePickerDialog.show();
    }
```

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

```42:47:app/src/main/java/com/example/studylogapp/ui/settings/SettingsActivity.java
        switchAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AlarmManagerHelper.setAlarmEnabled(SettingsActivity.this, isChecked);
            Toast.makeText(SettingsActivity.this, 
                isChecked ? "알람이 켜졌습니다" : "알람이 꺼졌습니다", 
                Toast.LENGTH_SHORT).show();
        });
```

**스위치 동작:**
- `isChecked == true`: `scheduleDailyAlarm()` 호출
- `isChecked == false`: `cancelAlarm()` 호출

**3. 데이터 초기화**

```73:83:app/src/main/java/com/example/studylogapp/ui/settings/SettingsActivity.java
    private void showClearDataDialog() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.clear_data)
            .setMessage(R.string.clear_data_confirm)
            .setPositiveButton(R.string.yes, (dialog, which) -> {
                database.clearAllData();
                Toast.makeText(SettingsActivity.this, "모든 데이터가 삭제되었습니다", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton(R.string.no, null)
            .show();
    }
```

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
- **Glide**: 이미지 로딩 및 캐싱

### 6.3 권한 관리
- **CAMERA**: 카메라 촬영
- **READ_EXTERNAL_STORAGE / READ_MEDIA_IMAGES**: 갤러리 접근
- **SCHEDULE_EXACT_ALARM**: 정확한 알람 스케줄링
- **POST_NOTIFICATIONS**: 알림 표시

---

## 7. 데이터 전환 과정 상세

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

## 8. 이벤트 핸들러 체인

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

## 9. 메모리 및 성능 고려사항

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

## 10. 결론

이 앱은 **학습 로그를 날짜별로 관리하는 캘린더 기반 앱**으로, 다음과 같은 특징을 가집니다:

1. **계층적 데이터 구조**: StudyLog (날짜) → StudyPost (게시물)
2. **이미지 관리**: 외부 저장소에 파일로 저장, DB에는 경로만 저장
3. **사용자 경험**: 캘린더에서 날짜 클릭 시 자동으로 작성/조회 화면 전환
4. **알람 시스템**: 일일 알람으로 학습 습관 형성 지원
5. **권한 관리**: 런타임 권한 요청으로 사용자 친화적 구현

각 화면은 **Intent를 통한 명시적 화면 전환**을 사용하며, **데이터베이스와 이미지 저장소를 분리**하여 관리합니다. **RecyclerView와 ViewPager2를 활용**한 효율적인 UI 구성으로 사용자 경험을 최적화했습니다.

