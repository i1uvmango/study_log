# Gemini API 키 권한 설정 가이드

## 🔑 API 키 권한 설정 방법

### 1. Google Cloud Console 접속
1. [Google Cloud Console](https://console.cloud.google.com/) 접속
2. 프로젝트 선택 (또는 새 프로젝트 생성)

### 2. Generative Language API 활성화
1. **API 및 서비스** > **라이브러리** 메뉴로 이동
2. 검색창에 "Generative Language API" 입력
3. **Generative Language API** 선택
4. **사용 설정** 버튼 클릭

### 3. API 키 생성 및 권한 설정
1. **API 및 서비스** > **사용자 인증 정보** 메뉴로 이동
2. **+ 사용자 인증 정보 만들기** > **API 키** 선택
3. 생성된 API 키를 복사
4. API 키를 클릭하여 편집
5. **API 제한사항** 섹션에서:
   - **API 키 제한** 선택
   - **Generative Language API** 체크박스 선택
   - 또는 개발 단계에서는 **제한사항 없음** 선택 가능 (보안상 권장하지 않음)
6. **저장** 버튼 클릭

### 4. API 키를 프로젝트에 설정
1. 프로젝트 루트의 `local.properties` 파일 열기
2. 다음 형식으로 추가:
   ```
   GEMINI_API_KEY=YOUR_API_KEY_HERE
   ```
3. Android Studio에서 **File > Sync Project with Gradle Files** 실행
4. 프로젝트 **Clean & Rebuild**

## 🔍 사용 가능한 모델 확인 방법

앱에서 사용 가능한 모델을 확인하려면:

1. `GeminiQuizGenerator.java` 파일 열기
2. `generateQuiz()` 메서드에서 다음 주석 해제:
   ```java
   // checkAvailableModels();
   ```
   → 
   ```java
   checkAvailableModels();
   ```
3. 앱 실행 후 로그캣에서 모델 목록 확인

## ⚠️ 일반적인 오류 및 해결 방법

### 오류: "API key not valid" 또는 HTTP 403
**원인**: API 키에 Generative Language API 권한이 없음
**해결**: 위의 "3. API 키 생성 및 권한 설정" 단계 확인

### 오류: "models/gemini-1.5-flash is not found" 또는 HTTP 404
**원인**: 
1. 모델 이름이 잘못되었거나
2. API 버전이 맞지 않거나
3. 해당 모델이 사용 불가능한 지역

**해결**:
1. `checkAvailableModels()` 메서드로 실제 사용 가능한 모델 확인
2. 사용 가능한 모델 이름으로 `GeminiApi.java`의 엔드포인트 수정
3. 예: `gemini-1.5-flash` → `gemini-pro` 또는 `gemini-1.5-pro`

### 오류: "Quota exceeded" 또는 HTTP 429
**원인**: API 사용량 한도 초과
**해결**: 
1. Google Cloud Console에서 할당량 확인
2. 필요시 결제 계정 연결 또는 할당량 증가 요청

## 📝 현재 설정 확인

현재 프로젝트 설정:
- **API 엔드포인트**: `v1beta/models/gemini-1.5-flash:generateContent`
- **API 키 위치**: `local.properties` → `BuildConfig.GEMINI_API_KEY`
- **API 클라이언트**: Retrofit 기반 (`ApiClient`)

## 🔐 보안 권장사항

1. **절대 API 키를 Git에 커밋하지 마세요**
   - `local.properties`는 `.gitignore`에 포함되어 있어야 함
   
2. **프로덕션 환경에서는**:
   - API 키 제한사항 설정 필수
   - 특정 앱 패키지명으로 제한
   - IP 주소 제한 (서버 사용 시)

3. **API 키 노출 방지**:
   - 로그에 전체 API 키 출력 금지 (현재 마스킹 처리됨)
   - ProGuard/R8 난독화 사용

