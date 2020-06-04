import annotation.LazyBenchmark;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class Benchmark implements Runnable{

    private long totalTime = 0;

    public Benchmark(Class... clazz) {
        run();
    }

    @Override
    public void run() {
        invoke();
    }

    @SuppressWarnings("unchecked")
    private void invoke(){
        int formatLength = 0;
        try {
            Field field = ClassLoader.class.getDeclaredField("classes");
            field.setAccessible(true);
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Vector<Class> classes = (Vector<Class>) field.get(classLoader);
            ArrayList<String> classesList = new ArrayList<>();
            for (Class c : classes) {
                classesList.add(c.getName());
            }
            for (String className : classesList) {
                Class clazz = Class.forName(className);
                String formatLineClass = "[BENCHMARK] " + clazz.getName() + ".";
                Method[] methods = clazz.getDeclaredMethods();
                Map<Integer, Method> orderedList = new TreeMap<>();
                List<Method> notOrderedList = new ArrayList<>();
                for (Method m : methods) {
                    if (m.isAnnotationPresent(LazyBenchmark.class)) {
                        if (!m.isAccessible())
                            m.setAccessible(true);

                        LazyBenchmark benchmark = m.getAnnotation(LazyBenchmark.class);
                        int call = benchmark.priority();

                        if (call != 0)
                            orderedList.put(call, m);
                        else
                            notOrderedList.add(m);
                    }
                }

                List<Method> orderedShowList = new LinkedList<>();
                orderedShowList.addAll(orderedList.values());
                orderedShowList.addAll(notOrderedList);

                if (orderedShowList.size() > 0) {
                    Map<Integer, String> formatData = new TreeMap<>();
                    for (Method method : orderedShowList) {
                        String formatLineMethod = formatLineClass + method.getName() + "()";
                        formatData.put(formatLineMethod.length(), formatLineMethod);
                    }
                    List<Integer> nums = new LinkedList<>(formatData.keySet());

                    int maxLength = nums.get(nums.size() - 1);
                    if (maxLength > formatLength)
                        formatLength  = maxLength;

                    for (Method method : orderedShowList) {
                        String formatLineMethod = formatLineClass + method.getName() + "()";
                        int currLength = formatLineMethod.length();
                        if (currLength < formatLength) {
                            StringBuilder sb = new StringBuilder();
                            int diff = formatLength - currLength;
                            for (int i = 0; i < diff; i++) {
                                sb.append(" ");
                            }
                            formatLineMethod += sb.toString();
                        }
                        methodCall(clazz, method, formatLineMethod);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if (totalTime > 0)
            System.out.println("[TOTAL]     "+totalTime / 1000 + "s [" + totalTime + " ms]");
    }

    private void methodCall(Class clazz, Method method, String formatLine) {
        try {
            Parameter[] p = method.getParameters();
            if (p.length > 0) {
                System.out.println(formatLine+" -->  sorry, methods with arguments is not supported");
                return;
            }
            String exception = "";
            long start = System.currentTimeMillis();
            try {
                method.invoke(clazz.newInstance(), null);
            } catch (Exception e){
                int lineNum = e.getCause().getStackTrace()[0].getLineNumber();
                exception = " was thrown "+ e.getCause().getClass().getSimpleName()+" at line "+lineNum;
            }
            long stop = System.currentTimeMillis();

            long resMSec = (stop - start);
            double resSec = (double) (resMSec / 1000);

            String resultLine = formatLine + " -->  " + resSec + "s [" + resMSec + " ms]"+exception;
            totalTime += resMSec;
            System.out.println(resultLine);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
