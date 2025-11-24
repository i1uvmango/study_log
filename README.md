# Study Log App

Instagram 스토리 보관함 캘린더에서 영감을 받아 만든 Android 공부 기록 캘린더 앱

![Instagram 스토리 캘린더 레퍼런스](docs/res/story_calendar.jpg)

## 주요 기능

1. **일일 알람**: 매일 정해진 시간에 알람 발생
2. **게시물 작성**: 사진(최대 3장), 요약, 키워드 입력
3. **AI 퀴즈 생성**: Gemini AI를 활용한 자동 복습 퀴즈 생성
4. **캘린더 UI**: 월별 캘린더로 게시물 확인
5. **슬라이드 뷰어**: 게시물을 슬라이드로 확인
6. **데이터베이스**: SQLite를 사용한 로컬 저장

## 기술 스택

- **언어**: Java
- **플랫폼**: Android SDK (Min: API 24, Target: API 34)
- **데이터베이스**: SQLite
- **UI 컴포넌트**: 
- RecyclerView
- ViewPager2
  - Fragment
- **이미지 로딩**: Glide 4.16.0
- **AI 서비스**: Google Gemini API (gemini-pro, gemini-pro-vision)
- **HTTP 클라이언트**: OkHttp 4.12.0
- **Material Design**: Material Components 1.11.0

## 빌드 방법

1. Android Studio에서 프로젝트 열기
2. Gradle 동기화
3. Gemini API 키 설정 (선택사항, 퀴즈 기능 사용 시 필요)
   - `app/src/main/java/com/example/studylogapp/ai/GeminiQuizGenerator.java` 파일에서 API_KEY 수정
4. 앱 실행

## 권한

- **카메라**: 사진 촬영
- **저장소 읽기/쓰기**: 갤러리 접근 및 이미지 저장
- **알람 설정**: 일일 알람 스케줄링
- **부팅 완료 수신**: 재부팅 후 알람 재설정
- **알림**: 알람 알림 표시

## 주요 화면

- **MainActivity**: 메인 메뉴 (캘린더, 설정)
- **CalendarActivity**: 월별 캘린더 및 오늘의 퀴즈
- **PostingActivity**: 게시물 작성 (사진, 요약, 키워드)
- **SlideViewerActivity**: 게시물 슬라이드 뷰어
- **SettingsActivity**: 알람 설정 및 데이터 관리

## 데이터베이스 구조

- **StudyLog**: 날짜별 학습 로그 메타데이터
- **StudyPost**: 개별 게시물 (사진, 요약, 키워드)
- **Quiz**: AI 생성 퀴즈 (문제, 선택지, 정답, 설명)

