package stats;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StatsFileLoader {

    public static List<SalesRecord> loadSalesRecords(String filePath) throws IOException {
        List<SalesRecord> result = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(
                Paths.get(filePath), StandardCharsets.UTF_8)) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 공백 기준으로 나누기
                String[] tokens = line.split("\\s+");
                if (tokens.length < 5) {
                    System.err.println("판매 기록 포맷 오류 (토큰 부족): " + line);
                    continue;
                }

                try {
                    int round = Integer.parseInt(tokens[0]);
                    String timestamp = tokens[1];
                    String orderId = tokens[2];
                    int price = Integer.parseInt(tokens[3]);
                    String menuName = tokens[4];

                    List<String> options = new ArrayList<>();
                    for (int i = 5; i < tokens.length; i++) {
                        options.add(tokens[i]);
                    }

                    SalesRecord record = new SalesRecord(
                            round, timestamp, orderId, price, menuName, options
                    );
                    result.add(record);

                } catch (NumberFormatException ex) {
                    System.err.println("판매 기록 숫자 파싱 오류: " + line);
                }
            }
        }

        return result;
    }

    public static List<DailyRevenueRecord> loadDailyRevenues(String filePath) throws IOException {
        List<DailyRevenueRecord> result = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(
                Paths.get(filePath), StandardCharsets.UTF_8)) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                int revenue;
                int round;

                if (line.contains(",")) {
                    // 새 포맷: "매출,라운드"
                    String[] tokens = line.split(",");
                    if (tokens.length < 2) {
                        System.err.println("일일 매출 포맷 오류: " + line);
                        continue;
                    }
                    try {
                        revenue = Integer.parseInt(tokens[0].trim());
                        round   = Integer.parseInt(tokens[1].trim());
                    } catch (NumberFormatException ex) {
                        System.err.println("일일 매출 숫자 파싱 오류: " + line);
                        continue;
                    }
                } else {
                    // 옛 포맷: "라운드 매출" (예: "1 32900")
                    String[] tokens = line.split("\\s+");
                    if (tokens.length < 2) {
                        System.err.println("일일 매출 포맷 오류: " + line);
                        continue;
                    }
                    try {
                        // ★ 여기서 어떤 게 day/매출인지 헷갈릴 수 있는데,
                        //   로그 "1 32900"을 보면 1=일차, 32900=매출로 보는 게 자연스러워서 이렇게 둠
                        round   = Integer.parseInt(tokens[0].trim());
                        revenue = Integer.parseInt(tokens[1].trim());
                    } catch (NumberFormatException ex) {
                        System.err.println("일일 매출 숫자 파싱 오류: " + line);
                        continue;
                    }
                }

                DailyRevenueRecord record = new DailyRevenueRecord(revenue, round);
                result.add(record);
            }
        }

        return result;
    }

    public static GameSaveInfo loadGameSaveInfo(String filePath) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(
                Paths.get(filePath), StandardCharsets.UTF_8)) {

            String line = br.readLine();
            if (line == null) {
                return null;
            }

            line = line.trim();
            if (line.isEmpty()) {
                return null;
            }

            // 1) 우선 새 포맷: "카페이름, 2"
            int idx = line.lastIndexOf(',');
            if (idx != -1) {
                String cafeName = line.substring(0, idx).trim();
                String levelStr = line.substring(idx + 1).trim();
                try {
                    int level = Integer.parseInt(levelStr);
                    return new GameSaveInfo(cafeName, level);
                } catch (NumberFormatException ex) {
                    System.err.println("게임 세이브 숫자 파싱 오류: " + line);
                    return null;
                }
            }

            // 2) 옛 포맷: "카페이름 2"
            String[] tokens = line.split("\\s+");
            if (tokens.length < 2) {
                System.err.println("게임 세이브 포맷 오류: " + line);
                return null;
            }

            String levelStr = tokens[tokens.length - 1];
            StringBuilder nameBuilder = new StringBuilder();
            for (int i = 0; i < tokens.length - 1; i++) {
                if (i > 0) nameBuilder.append(' ');
                nameBuilder.append(tokens[i]);
            }
            String cafeName = nameBuilder.toString();

            try {
                int level = Integer.parseInt(levelStr);
                return new GameSaveInfo(cafeName, level);
            } catch (NumberFormatException ex) {
                System.err.println("게임 세이브 숫자 파싱 오류: " + line);
                return null;
            }
        }
    }
}


