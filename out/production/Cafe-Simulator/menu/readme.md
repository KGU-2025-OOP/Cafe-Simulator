메뉴 시스템
├── Ingredient (재료)
│   ├── Name (이름)
│   ├── Making Time (제작 시간)
│   ├── Max Wait Time (최대 대기 시간)
│   └── Price (가격)
├── Options (재료)
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

### `Option`
메뉴 아이템에 사용되는 단일 옵션을 나타냅니다.

**속성:**
- `String name` - 옵션 이름
- `int makingTime` - 준비에 필요한 시간
- `int maxWaitTime` - 옵션가 상하기 전까지의 최대 시간
- `int price` - 옵션 가격

옵션과 재료는 MenuItem을 상속하여 사용됩니다.

### `Menu`
여러 재료로 구성된 메뉴 아이템을 나타냅니다.

**속성:**
- `String name` - 메뉴 이름
- `int ingredientCount` - 필요한 재료 개수
- `ArrayList<items> items` - 재료, 옵션 목록
- `int price` - 메뉴 가격

**메소드:**
- `addOptions` - 메뉴에 옵션을 추가합니다. 추가된 옵션은 `items`에 재료와 함께 추가됩니다.
- `getTotalCost` - 옵션값을 포함한 메뉴의 가격을 반환합니다.
- `getIngredients` - 메뉴에 들어있는 재료와 옵션 리스트를 반환합니다.

### `MenuItemList`
재료, 옵션을 위한 추상클래스 입니다.

**메소드:**
- MenuList 또한 동일한 메소드를 가집니다.

- `loadFromFile` - txt에서 데이터를 가져옵니다.
- `getItemList` - getter를 통해 리스트를 가져옵니다.
- `findByName` - 옵션추가 등에서 사용할 메소드로 이름을 비교하여 객체를 가져옵니다.
- `size` - 리스트의 전체 크기를 반환합니다.
- `get` - 리스트에서 인덱스 번호를 기반으로 객체를 가져옵니다.

각 리스트는 `get○○○○`의 형식으로 가져올 수 있습니다. `ex)getOptionList()`

## 데이터 파일 형식

### `ingredients.txt`, `options.txt`
```
<이름> <제작시간> <최대대기시간> <가격>
```

**예시:**
```
커피 10 25 0
디카페인커피 12 25 0
물 2 20 0
우유 5 25 0
```

### `menu.txt`
```
<메뉴이름> <재료개수> <재료1이름> <재료2이름> ... <메뉴가격>
```
 
**예시:**
```
아메리카노 2 커피 물 4000
카페라떼 2 커피 우유 4500
바닐라라떼 3 커피 우유 바닐라시럽 5000
```
---

코드 줄이기를 위해 추상화 클래스가 사용되었습니다.
요청에 따라 각 요소에 getter를 추가하였습니다.

print메서드는 테스트를 위해 작성되어 있습니다. 향후 개발 방향에 따라 변경될 수 있습니다.
Test.java에서 해당 데이터를 가져오는 법을 확인하시면 됩니다.