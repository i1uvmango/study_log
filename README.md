# Study Log App

Instagram 스토리 보관함 캘린더에서 영감을 받아 만든 Android 공부 기록 캘린더 앱

![Instagram 스토리 캘린더 레퍼런스](docs/res/story_calendar.jpg)

## 주요 기능

1. **일일 알람**: 매일 정해진 시간에 알람 발생
2. **게시물 작성**: 사진(최대 3장), 요약, 키워드 입력
3. **캘린더 UI**: 월별 캘린더로 게시물 확인
4. **슬라이드 뷰어**: 게시물을 슬라이드로 확인
5. **데이터베이스**: SQLite를 사용한 로컬 저장

## 기술 스택

- Java
- Android SDK (Min: API 24, Target: API 34)
- SQLite
- RecyclerView
- ViewPager2
- Glide (이미지 로딩)

## 빌드 방법

1. Android Studio에서 프로젝트 열기
2. Gradle 동기화
3. 앱 실행

## 권한

- 카메라
- 저장소 읽기/쓰기
- 알람 설정
- 부팅 완료 수신

