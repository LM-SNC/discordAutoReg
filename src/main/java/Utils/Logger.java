package Utils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Logger {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    private boolean useColors = true;


    private static Logger instance;
    private HashMap<String, String> classNamesCache;
    private ArrayList<String> excludeClassNames;
    private SimpleDateFormat simpleDateFormat;

    private static String convert(String ... strings) {
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string + ", ");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length()-2, sb.length());
        }
        return sb.toString();
    }


    public static void logFuncStart() {
        instance().log("[START]", ANSI_BLUE);
    }

    public static void logFuncStart(String ... strings) {
        instance().log("[START] -- " + convert(strings), ANSI_BLUE);
    }

    public static void logFuncEnd(String ... strings) {
        instance().log("[END] -- " + convert(strings), ANSI_CYAN);
    }

    public static void logFuncEnd() {
        instance().log("[END]", ANSI_CYAN);
    }

    public static void logWithTime(String ... strings) {
        instance().log("[" + instance().simpleDateFormat.format(System.currentTimeMillis()) + "] -- " + convert(strings), ANSI_BLACK);
    }

    public static void logError(String ... strings) {
        instance().log("[ERROR] -- " + convert(strings), ANSI_RED);
    }

    public static void logWarn(String ... strings) {
        instance().log(convert(strings), ANSI_YELLOW);
    }

    public static void logInfo(String ... strings) {
        instance().log(convert(strings), ANSI_WHITE);
    }

    public static void logDebug(String ... strings) {
        instance().log(convert(strings), ANSI_GREEN);
    }


    private static Logger instance() {
        if (instance == null) instance = new Logger();
        return instance;
    }

    public Logger() {
        classNamesCache = new HashMap<>();
        excludeClassNames = new ArrayList<>();
        excludeClassNames.add(Thread.class.getName());
        excludeClassNames.add(Logger.class.getName());
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss.SSS");//yyyy-MM-dd 'at' HH:mm:ss z");
    }



    private void log(String message, String color) {
        String classAndMethodName = getClassAndMethodName();
        String outStr = ((useColors) ? color : "") + classAndMethodName + ((useColors) ? ANSI_RESET : "") + " -- " + message;
        System.out.println(outStr);
    }

    private StackTraceElement getCallerElement(StackTraceElement[] elements) {
        for (StackTraceElement element : elements) {
            String elementClassName = element.getClassName();
            if (!excludeClassNames.contains(elementClassName)) {
                return element;
            }
        }
        return null;
    }

    private String getClassName(StackTraceElement element) {
        String elementClassName = element.getClassName();
        if (!classNamesCache.containsKey(elementClassName)) {
            classNamesCache.put(elementClassName, elementClassName.substring(elementClassName.lastIndexOf(".") + 1));
        }
        return classNamesCache.get(elementClassName);
    }

    private String getClassAndMethodName() {
        StackTraceElement callerElement = getCallerElement(Thread.currentThread().getStackTrace());
        if (callerElement != null) {
            return getClassName(callerElement) + "::" + callerElement.getMethodName() + "();";
        }
        return "NULL::null();";
//        return getClassName(Objects.requireNonNull(getCallerElement(Thread.currentThread().getStackTrace())));
    }
}