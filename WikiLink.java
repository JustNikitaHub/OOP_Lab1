import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

    public static void main(String[] args) throws UnsupportedEncodingException {
        
        Scanner scanner = new Scanner(System.in);
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        System.out.print("Введите запрос: ");
        String query = scanner.nextLine();
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String apiUrl = "https://ru.wikipedia.org/w/api.php?action=query&format=json&titles=" + encodedQuery;
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
            String pageId = findPageId(response.toString());
            if (!pageId.isEmpty()) {
                String pageUrl = "https://ru.wikipedia.org/?curid=" + pageId;
                System.out.println("Ссылка на сайт: " + pageUrl);
                java.awt.Desktop.getDesktop().browse(new URL(pageUrl).toURI());
            } else {
                System.out.println("Такой страницы нет!");
            }

        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
    }
    private static String findPageId(String jsonResponse) {
        Pattern pattern = Pattern.compile("\"pageid\":(\\d+)");
        Matcher matcher = pattern.matcher(jsonResponse);
        return matcher.find() ? matcher.group(1) : "";
    }