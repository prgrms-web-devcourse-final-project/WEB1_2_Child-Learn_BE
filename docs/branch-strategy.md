## 브랜치 전략

<details>
<summary>Git-Flow 사용</summary>

![image](https://github.com/user-attachments/assets/16fe6e24-98ba-4aa8-b5c8-ecf7874ef638)

</details>

<br>

### 브랜치 네이밍
* Issue 에서 브랜치 생성
* main : 프로젝트가 최종적으로 배포되는 중심 브랜치입니다.
* dev : 개발이 진행되는 브랜치입니다. dev 브랜치가 배포할 수준의 기능을 갖추면 release 브랜치로 머지됩니다.
* feature : 기능을 개발하는 브랜치입니다. dev 브랜치에서 파생되는 브랜치이며, dev 브랜치로 머지합니다.
* release 브랜치에서 모든 테스트가 통과한 후 main 브랜치로 병합
  * CI/CD가 되어 있는 상태에서 바로 main 브랜치로 병합 시 배포 실패가 뜨는 것을 방지하고,
    추가 에러가 있는지 확인하는 과정이라고 생각해주세요.
* hotfix 브랜치는 배포 된 후 생긴 에러의 긴급 상황에서 사용, 즉시 테스트 및 배포되어야 함
* 필요 시 작업 별로 구분하기 위해서 Issue Tracker ID 사용  
  `dev/feat/{기능명}/{#이슈번호}`

| 분류 | 내용 | 명명규칙 |
| --- | --- | --- |
| main | 최종적으로 배포되는 브랜치 | main |
| develop | 개발 중인 코드가 모이는 브랜치 | dev |
| feature | 새로운 기능을 개발하는 브랜치 | dev/feature/{기능명} /{#이슈번호} |
| refactor | 개발된 기능을 리팩터링하는 브랜치 | dev/refactor/{기능명}/{#이슈번호} |
| release | 배포를 준비하는 브랜치 | release/{버전} |
| hotfix | 배포 된 후 발생한 버그를 수정하는 브랜치 | hotfix/(#이슈번호) |

※ 배포를 준비하는 브랜치는 dev 또는 release 에서 현재와 다르게 추후 변경될 수 있습니다.

<br>

### 활용 예시
```markdown
main

dev/feature/login/#1

dev/feature/register/#12

dev/refactor/login

release/1.0.0

hotfix/#22
```
