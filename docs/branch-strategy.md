## 브랜치 전략

<details>
<summary>Git-Flow 사용</summary>

![image](https://github.com/user-attachments/assets/16fe6e24-98ba-4aa8-b5c8-ecf7874ef638)

</details>

<br>

### 브랜치 네이밍
* Issue 에서 브랜치 생성
* main : 프로젝트가 최종적으로 배포되는 중심 브랜치입니다.
* develop : 개발이 진행되는 브랜치입니다. dev 브랜치가 배포할 수준의 기능을 갖추면 release 브랜치로 머지됩니다.
* feature : 기능을 개발하는 브랜치입니다. dev 브랜치에서 파생되는 브랜치이며, dev 브랜치로 머지합니다.
* release : 배포 전 버전 기록을 남깁니다. 브랜치에서 모든 테스트가 통과한 후 main 브랜치로 병합
* hotfix : 브랜치는 배포 된 후 생긴 에러의 긴급 상황에서 사용, 즉시 테스트 및 배포되어야 함
* 작업 별로 구분하기 위해서 SCRUM-NUM 사용  
  `feature/{도메인}/{#스크럼번호}`
| 분류 | 내용 | 명명규칙                        |
| --- | --- |-----------------------------|
| main | 최종적으로 배포되는 브랜치 | main                        |
| develop | 개발 중인 코드가 모이는 브랜치 | develop                     |
| feature | 새로운 기능을 개발하는 브랜치 | feature/{도메인}/{#스크럼번호}      |
| refactor | 개발된 기능을 리팩터링하는 브랜치 | refactor/{도메인}/{#스크럼번호} |
| release | 배포를 준비하는 브랜치 | release/{버전} |
| hotfix | 배포 된 후 발생한 버그를 수정하는 브랜치 | hotfix/(#이슈번호)              |

<br>

### 활용 예시
```markdown
main

develop

feature/register/#12

refactor/login

release/V1.0.0

hotfix/#22
```
