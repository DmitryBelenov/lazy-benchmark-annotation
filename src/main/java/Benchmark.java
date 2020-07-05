import annotation.LazyBenchmark;

import java.lang.reflect.*;
import java.util.*;

public class Benchmark implements Runnable{

    private long totalTime = 0;
    private Object[] customRefTypes;

    public Benchmark(Object[] customRefTypes, Class... clazz) {
        if (customRefTypes != null) this.customRefTypes = customRefTypes;
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
            Constructor[] constructors = clazz.getConstructors();
            for (Constructor c : constructors){
                Parameter[] p = c.getParameters();
                if (p.length > 0){
                    System.out.println(formatLine+" -->  sorry, class with arguments in constructor is not supported");
                    return;
                }
            }
            Parameter[] p = method.getParameters();
            Object[] args = null;
            if (p.length > 0) {
                args = new Object[p.length];
                for (int i = 0; i < p.length; i++) {
                    Type type = p[i].getParameterizedType();
                    String typeName = type.getTypeName();
                    boolean isArray = typeName.endsWith("[]");
                    if (typeName.contains(".")){
                        String[] typePackArray = typeName.split("\\.");
                        String refType = typePackArray[typePackArray.length-1];
                        if (refType.contains("String")){
                            args[i] = isArray ? new String[]{"Hello", "world"} : "Hello world";
                        } else if (refType.contains("Integer")){
                            args[i] = isArray ? new Integer[]{100, 200} : 100;
                        } else if (refType.contains("Byte")){
                            args[i] = isArray ? new Byte[]{100,100} : new Byte("100");
                        } else if (refType.contains("Short")){
                            args[i] = isArray ? new Short[]{1000,2000} : new Short((short) 1000);
                        } else if (refType.contains("Long")){
                            args[i] = isArray ? new Long[]{100L, 200L} : 100L;
                        } else if (refType.contains("Float")){
                            args[i] = isArray ? new Float[]{100F, 200F} : 100F;
                        } else if (refType.contains("Double")){
                            args[i] = isArray ? new Double[]{100.10, 200.20} : 100.10;
                        } else if (refType.contains("Boolean")){
                            args[i] = isArray ? new Boolean[]{true, false} : true;
                        } else if (refType.contains("Character")){
                            args[i] = isArray ? new Character[]{'H', 'w'} : 'H';
                        }
                    } else if (typeName.contains("int")){
                        args[i] = isArray ? new int[]{100, 200} : 100;
                    } else if (typeName.contains("byte")){
                        if (isArray) args[i] = new byte[]{100, 100};
                        else args[i] = new Byte("100");
                    } else if (typeName.contains("short")){
                        args[i] = isArray ? new short[]{100,200} : new Short("100");
                    } else if (typeName.contains("long")){
                        args[i] = isArray ? new long[]{100L, 200L} : 100L;
                    } else if (typeName.contains("char")){
                        args[i] = isArray ? new char[]{'H', 'w'} : 'H';
                    } else if (typeName.contains("float")){
                        args[i] = isArray ? new float[]{100F, 200F} : 100F;
                    } else if (typeName.contains("double")){
                        args[i] = isArray ? new double[]{100.10, 200.20} : 100.10;
                    } else if (typeName.contains("boolean")){
                        args[i] = isArray ? new boolean[]{true, false} : true;
                    } else {
                        boolean detect = false;
                        if (customRefTypes != null){
                            for (Object o : customRefTypes){
                                if (typeName.contains(o.getClass().getSimpleName())){
                                    args[i] = o;
                                    detect = true;
                                    break;
                                }
                            }
                        }
                        if (!detect)
                            System.out.println(formatLine+" --> no custom argument instance detected");
                    }
                }
            }
            String exception = "";
            long start = System.currentTimeMillis();
            try {
                method.invoke(clazz.newInstance(), args);
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
