package gogame.common;

public final class SimpleLogger {
    public static void log(Object ...o) {
        StringBuilder sb = new StringBuilder();
        sb.append("[thread-");
        sb.append(Thread.currentThread().getId());
        sb.append("]: ");

        for (Object obj : o) {
            sb.append((obj == null) ? "null" : obj.toString());
        }

        System.out.println(sb.toString());
    }
}
