public class EditDistance {

    public static int calculate(String first, String second) {

        int[] a = first.codePoints().toArray();
        int[] b = second.codePoints().toArray();

        int m = a.length;
        int n = b.length;

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {

            for (int j = 1; j <= n; j++) {

                int cost = (a[i - 1] == b[j - 1]) ? 0 : 1;

                dp[i][j] = Math.min(
                        Math.min(
                                dp[i - 1][j] + 1,
                                dp[i][j - 1] + 1
                        ),
                        dp[i - 1][j - 1] + cost
                );
            }
        }

        return dp[m][n];
    }
}