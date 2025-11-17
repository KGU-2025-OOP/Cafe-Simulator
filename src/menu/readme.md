메뉴 시스템
├── Ingredient (재료)
│   ├── Type (종류)
│   ├── Name (이름)
│   ├── Making Time (제작 시간)
│   ├── Max Wait Time (최대 대기 시간)
│   └── Price (가격)
└── Menu (메뉴)
    ├── Name (이름)
    ├── Ingredient Count (재료 개수)
    ├── Ingredients List (재료 목록)
    └── Price (가격)

## 클래스 설명

### `Ingredient`
메뉴 아이템에 사용되는 단일 재료를 나타냅니다.

**속성:**
- `String type` - 재료 카테고리 (예: "재료", "양념", "소스")
- `String name` - 재료 이름
- `int makingTime` - 준비에 필요한 시간
- `int maxWaitTime` - 재료가 상하기 전까지의 최대 시간
- `int price` - 재료 가격

### `Menu`
여러 재료로 구성된 메뉴 아이템을 나타냅니다.

**속성:**
- `String name` - 메뉴 이름
- `int ingredientCount` - 필요한 재료 개수
- `ArrayList<Ingredient> ingredients` - 재료 목록
- `int price` - 최종 메뉴 가격

## 데이터 파일 형식

### `ingredients.txt`
```
<타입> <이름> <제작시간> <최대대기시간> <가격>
```

**예시:**
```
재료 토마토 5 30 100
재료 양파 3 25 80
재료 치즈 10 15 200
양념 소금 1 999 50
소스 토마토소스 8 20 150
```

### `menu.txt`
```
<메뉴이름> <재료개수> <재료1이름> <재료2이름> ... <메뉴가격>
```

**예시:**
```
피자 3 토마토 양파 치즈 5000
샐러드 2 토마토 양파 3000
파스타 3 토마토 토마토소스 소금 4500

---

print메서드는 테스트를 위해 작성되어 있습니다. 향후 개발 방향에 따라 변경될 수 있습니다.
Test.java에서 해당 데이터를 가져오는 법을 확인하시면 됩니다.