package stats;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatsFileLoader {

    private static final String OPTIONS_FILE_PATH = "resources/options.txt";

    private static final Set<String> OPTION_KEYWORDS = new HashSet<>();

    static {
        Path path = Paths.get(OPTIONS_FILE_PATH);
        if (Files.exists(path)) {
            try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty()) {
                        continue;
                    }
                    String[] parts = trimmed.split("\\s+");
                    if (parts.length == 0) {
                        continue;
                    }
                    String optionName = parts[0];
                    OPTION_KEYWORDS.add(optionName);
                }
            } catch (IOException e) {
                initFallbackOptionKeywords();
            }
        } else {
            initFallbackOptionKeywords();
        }
    }

    private static void initFallbackOptionKeywords() {
        OPTION_KEYWORDS.clear();
        OPTION_KEYWORDS.addAll(Arrays.asList(
                "샷추가",
                "휘핑추가",
                "시럽추가",
                "얼음조절",
                "디카페인변경",
                "라지사이즈",
                "오트밀크변경",
                "두유변경",
                "무얼음",
                "진하게",
                "연하게",
                "차갑게",
                "따뜻하게",
                "토핑추가",
                "설탕추가",
                "시나몬추가",
                "생크림추가",
                "컵홀더추가",
                "포장하기",
                "테이크아웃"
        ));
    }

    public static List<SalesRecord> loadSalesRecords(String filePath) throws IOException {
        List<SalesRecord> result = new ArrayList<>();
        Path path = Paths.get(filePath);

        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }

                String[] parts = trimmed.split("\\s+");
                if (parts.length < 5) {
                    continue;
                }

                int round = Integer.parseInt(parts[0]);
                String timestamp = parts[1];
                String orderId = parts[2];
                int price = Integer.parseInt(parts[3]);

                List<String> menuTokens = new ArrayList<>();
                List<String> optionTokens = new ArrayList<>();

                boolean inOption = false;
                for (int i = 4; i < parts.length; i++) {
                    String token = parts[i];
                    if (OPTION_KEYWORDS.contains(token)) {
                        inOption = true;
                        optionTokens.add(token);
                    } else {
                        if (inOption) {
                            optionTokens.add(token);
                        } else {
                            menuTokens.add(token);
                        }
                    }
                }

                String menuName = String.join(" ", menuTokens);
                SalesRecord record = new SalesRecord(round, timestamp, orderId, price, menuName, optionTokens);
                result.add(record);
            }
        }

        return result;
    }

    public static List<DailyRevenueRecord> loadDailyRevenues(String filePath) throws IOException {
        List<DailyRevenueRecord> result = new ArrayList<>();
        Path path = Paths.get(filePath);

        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                String[] parts = trimmed.split("\\s+");
                if (parts.length < 2) {
                    continue;
                }
                int revenue = Integer.parseInt(parts[0]);
                int round = Integer.parseInt(parts[1]);
                result.add(new DailyRevenueRecord(round, revenue));
            }
        }

        return result;
    }

    public static GameSaveInfo loadGameSaveInfo(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                String[] parts = trimmed.split("\\s+");
                if (parts.length < 2) {
                    continue;
                }
                int day = Integer.parseInt(parts[parts.length - 1]);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < parts.length - 1; i++) {
                    if (i > 0) {
                        sb.append(" ");
                    }
                    sb.append(parts[i]);
                }
                String cafeName = sb.toString();
                return new GameSaveInfo(cafeName, day);
            }
        }

        return new GameSaveInfo("", 0);
    }
}