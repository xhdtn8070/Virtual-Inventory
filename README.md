# Virtual-Inventory 가상 인벤토리 플러그인
Virtual-Inventory Plugin은 마인크래프트에서 사용자에게 가상의 인벤토리를 제공해주는 Plugin이다.<br/>
Spigot-API 기반으로 작성되어 Spigot, Paper 버킷에서 사용가능하다.<br/>
Plugin에서 사용되는 메시지를 사용자가 지정할 수 있도록 개발되었다.<br/>
Logging 등 관리 측면의 기능 개발은 추후 진행될 예정이다.<br/>

## 사용법 Usage
```
	 1. /plugins 아래에 virtual-inventory-{version}.jar 위치 
	 2. /서버 동작
	 3. /plugins 아래에 다음과 같은 항목 자동 생성 됨
		- VirturalInventoryPlugin/inventory
		- VirturalInventoryPlugin/config.yml
		- VirturalInventoryPlugin/message.yml
	 4. config.yml, message.yml을 변경하고 커맨드에서 /reload confirm 입력하면 설정 적용됨.
```	
## 권한 Permission
```
    - vc.admin
	* luckPerm Plugin을 이용하여 '/lp user KOO_MA permission set vi.admin true' 커맨드 적용해주면 권한 등록완료
	* luckPerm Plugin을 사용하지 않고 permission 파일을 수정하여도 상관없음
```
## 설정 Config
```
	- 아래 파일 내부에 설명 첨부.
	- /plugins/VirturalInventoryPlugin/config.yml
	- /plugins/VirturalInventoryPlugin/message.yml
```	
<hr>
#### 버전 : 1.1
#### 사용된 API : Spigot-1.16.1
#### 테스트 완료 버전 : 1.12.2 , 1.16.1 

## 커맨드 Command
```bash
/vi
	- version 설명

/vi {info or i}
	- 커맨드 자체의 설명
	
/vi {help or h}
	- 명령어 소개
	
/vi opt
	- 현재 적용되어 있는 옵션 보여줌
	- permission
		- vc.admin	
		
/vi {name n} {#displayName}
	- displayName에 해당하는 실제 유저의 Name을 알 수 있음
	- 온라인 유저만 가능
	
/vi {list or l}
	- 자신의 창고 리스트를 확인할 수 있음

/vi {list or l} {#userName}
	- 다른 사람의 가상 인벤토리 리스트를 확인할 수 있음
	- permission
		- vc.admin

/vi {create or c} {#InventoryName}
	- 자신의 가상 인벤토리를 생성한다.

/vi {open or o} {#InventoryName}
	- 자신의 {#InventoryName}이라는 가상 인벤토리를 오픈한다.

/vi {openother or oo} {#userName} {#InventoryName}
	- 다른 사람의 가상 인벤토리를 오픈한다 확인할 수 있음
	- permission
		- vc.admin

/vi {remove or rm} {#InventoryName}
	
/vi {removeother or rmo} {#userName} {#InventoryName}
	- 다른 사람의 인벤토리를 삭제한다.
	- permission
		- vc.admin

/vi saveAll
	- 모든 사람의 인벤토리 저장
	- permission
		- vc.admin
```
## 변경점
```bash
    1.0.1 : other 명령어 변경, Tab Complete
```
   
## 삭제 Delete

```bash
/vi save
	- 자신의 인벤토리 저장
	- 서버 렉을 발생시키는 등의 비효율적인 현상이 예상되서 개발 단계에서 제거.
```

## 개발 계획 Development plan

```bash
tabComplete 개발 예정

/backup 
	- backup 생성
	- permission
		- vc.admin
		
backup 생성 방식
	ex) backup/2020/07/25 - userName.InventoryName-timestamp.txt
	
history 생성 방식
	ex) history/2020/07/25 - userName.InventoryName.txt
	한줄추가 한줄추가

    * db 안쓰고는 페이지네이션 처리 등에 문제가 생길 수 있다.
    * 파일 버전에서는 제공되지 않고 디비 버전에서는 사용될 수 있음.
    * 파일 버전에서는 직접 텍스트 문서만 읽을 수 있도록 제작할 예정
    * /vi {history or h}
```

## 기여 Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

gmail : xhddlf8070@gmail.com
## 라이선스 License
[MIT](https://choosealicense.com/licenses/mit/)
