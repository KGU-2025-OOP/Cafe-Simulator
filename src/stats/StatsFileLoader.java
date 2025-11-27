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

                String[] tokens = line.split(",");
                if (tokens.length < 2) {
                    System.err.println("일일 매출 포맷 오류: " + line);
                    continue;
                }

                try {
                    int revenue = Integer.parseInt(tokens[0].trim());
                    int round = Integer.parseInt(tokens[1].trim());
                    DailyRevenueRecord record = new DailyRevenueRecord(revenue, round);
                    result.add(record);
                } catch (NumberFormatException ex) {
                    System.err.println("일일 매출 숫자 파싱 오류: " + line);
                }
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


            int idx = line.lastIndexOf(',');
            if (idx == -1) {
                System.err.println("게임 세이브 포맷 오류: " + line);
                return null;
            }

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
    }
}


