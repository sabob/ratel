package com.google.ratel.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import com.google.ratel.deps.lang3.*;

/**
 *
 */
public class ParamUtils {

    private final static Set<Class> supportedParamTypes = new HashSet<Class>();

    static {
        supportedParamTypes.add(String.class);
        supportedParamTypes.add(int.class);
        supportedParamTypes.add(Integer.class);
        supportedParamTypes.add(float.class);
        supportedParamTypes.add(Float.class);
        supportedParamTypes.add(long.class);
        supportedParamTypes.add(Long.class);
        supportedParamTypes.add(double.class);
        supportedParamTypes.add(Double.class);
        supportedParamTypes.add(short.class);
        supportedParamTypes.add(Short.class);
        supportedParamTypes.add(char.class);
        supportedParamTypes.add(Character.class);
        supportedParamTypes.add(byte.class);
        supportedParamTypes.add(Byte.class);
        supportedParamTypes.add(boolean.class);
        supportedParamTypes.add(Boolean.class);
    }

    public static BigDecimal toBigDecimal(String str) {
        BigDecimal bd = toBigDecimal(str, null);
        return bd;
    }

    public static BigDecimal toBigDecimal(String str, BigDecimal defaultValue) {
        if (StringUtils.isBlank(str)) {
            return defaultValue;
        }
        str = str.trim();

        try {
            BigDecimal bd = new BigDecimal(str);
            return bd;
        } catch (NumberFormatException ignore) {
            // TODO perhaps throw exceptions instead of swallow
            return defaultValue;
        }
    }

    public static BigInteger toBigInteger(String str) {
        BigInteger bi = toBigInteger(str, null);
        return bi;
    }

    public static BigInteger toBigInteger(String str, BigInteger defaultValue) {
        if (StringUtils.isBlank(str)) {
            return defaultValue;
        }

        try {
            str = str.trim();

            BigInteger bi = new BigInteger(str);
            return bi;
        } catch (NumberFormatException ignore) {
            return defaultValue;
        }
    }

    public static byte toByte(String str) {
        if (StringUtils.isBlank(str)) {
            return 0;
        }

        try {
            str = str.trim();
            byte b = Byte.parseByte(str);
            return b;
        } catch (NumberFormatException ignore) {
            return 0;
        }
    }

    public static Byte toByteObj(String str, Byte defaultValue) {
        if (StringUtils.isBlank(str)) {
            return defaultValue;
        }

        try {
            str = str.trim();
            Byte b = Byte.valueOf(str);
            return b;
        } catch (NumberFormatException ignore) {
            return defaultValue;
        }
    }

    public static Byte toByteObj(String str) {
        Byte b = toByteObj(str, null);
        return b;
    }

    public static char toChar(String str) {
        if (StringUtils.isBlank(str)) {
            return 0;
        }

        str = str.trim();
        char c = str.charAt(0);
        return c;
    }

    public static Character toCharacterObj(String str, Character defaultValue) {
        if (StringUtils.isBlank(str)) {
            return defaultValue;
        }

        str = str.trim();
        char c = str.charAt(0);
        Character cObj = Character.valueOf(c);
        return cObj;
    }

    public static Character toCharacterObj(String str) {
        Character c = toCharacterObj(str, null);
        return c;
    }

    public static double toDouble(String str) {
        if (StringUtils.isBlank(str)) {
            return 0D;
        }

        try {
            str = str.trim();
            double d = Double.parseDouble(str);
            return d;
        } catch (NumberFormatException ignore) {
            return 0D;
        }
    }

    public static Double toDoubleObj(String str, Double defaultValue) {
        try {
            if (StringUtils.isBlank(str)) {
                return defaultValue;
            }

            str = str.trim();
            Double d = Double.valueOf(str);
            return d;
        } catch (NumberFormatException ignore) {
            return defaultValue;
        }
    }

    public static Double toDoubleObj(String str) {
        Double d = toDoubleObj(str, null);
        return d;
    }

    public static float toFloat(String str) {
        if (StringUtils.isBlank(str)) {
            return 0;
        }

        try {
            str = str.trim();
            float f = Float.parseFloat(str);
            return f;
        } catch (NumberFormatException ignore) {
            return 0;
        }
    }

    public static Float toFloatObj(String str, Float defaultValue) {
        if (StringUtils.isBlank(str)) {
            return defaultValue;
        }

        try {
            str = str.trim();
            Float s = Float.valueOf(str);
            return s;
        } catch (NumberFormatException ignore) {
            return defaultValue;
        }
    }

    public static Float toFloatObj(String str) {
        Float f = toFloatObj(str, null);
        return f;
    }

    public static int toInt(String str) {
        if (StringUtils.isBlank(str)) {
            return 0;
        }

        try {
            str = str.trim();
            int i = Integer.parseInt(str);
            return i;
        } catch (NumberFormatException ignore) {
            return 0;
        }
    }

    public static Integer toIntegerObj(String str, Integer defaultValue) {
        if (StringUtils.isBlank(str)) {
            return defaultValue;
        }

        try {
            str = str.trim();
            Integer i = Integer.valueOf(str);
            return i;
        } catch (NumberFormatException ignore) {
            return defaultValue;
        }
    }

    public static Integer toIntegerObj(String str) {
        Integer i = toIntegerObj(str, null);
        return i;
    }

    public static long toLong(String str) {
        if (StringUtils.isBlank(str)) {
            return 0L;
        }

        try {
            str = str.trim();
            long l = Long.parseLong(str);
            return l;
        } catch (NumberFormatException ignore) {
            return 0L;
        }
    }

    public static Long toLongObj(String str, Long defaultValue) {
        if (StringUtils.isBlank(str)) {
            return defaultValue;
        }

        try {
            str = str.trim();
            Long l = Long.valueOf(str);
            return l;
        } catch (NumberFormatException ignore) {
            return defaultValue;
        }
    }

    public static Long toLongObj(String str) {
        Long l = toLongObj(str, null);
        return l;
    }

    public static short toShort(String str) {
        if (StringUtils.isBlank(str)) {
            return 0;
        }

        try {
            str = str.trim();
            short s = Short.parseShort(str);
            return s;
        } catch (NumberFormatException ignore) {
            return 0;
        }
    }

    public static Short toShortObj(String str, Short defaultValue) {
        if (StringUtils.isBlank(str)) {
            return defaultValue;
        }

        try {
            str = str.trim();
            Short s = Short.valueOf(str);
            return s;
        } catch (NumberFormatException ignore) {
            return defaultValue;
        }
    }

    public static Short toShortObj(String str) {
        Short s = toShortObj(str, null);
        return s;
    }

    public static boolean toBoolean(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }

        str = str.trim();
        boolean b = Boolean.parseBoolean(str);
        return b;
    }

    public static Boolean toBooleanObj(String str, Boolean defaultValue) {
        if (StringUtils.isBlank(str)) {
            return defaultValue;
        }

        str = str.trim();
        Boolean b = Boolean.valueOf(str);
        return b;
    }

    public static Boolean toBooleanObj(String str) {
        Boolean b = toBooleanObj(str, null);
        return b;
    }

    public static boolean isSupportedParameter(Class type) {
        if (supportedParamTypes.contains(type)) {
            return true;
        }
        return false;
    }

    public static Object toType(Class type, String... values) {
        if (type == String.class) {
            return values;
        }

        if (type == int.class) {
            int[] array = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = toInt(values[i]);
            }
            return array;
        }

        if (type == Integer.class) {
            Integer[] array = new Integer[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = toIntegerObj(values[i]);
            }
            return array;
        }

        if (type == boolean.class) {
            boolean[] array = new boolean[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = toBoolean(values[i]);
            }
            return array;
        }

        if (type == Boolean.class) {
            Boolean[] array = new Boolean[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = toBooleanObj(values[i]);
            }
            return array;
        }

        if (type == long.class) {
            long[] array = new long[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = toLong(values[i]);
            }
            return array;
        }

        if (type == Long.class) {
            Long[] array = new Long[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = toLongObj(values[i]);
            }
            return array;
        }

        if (type == double.class) {
            double[] array = new double[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = toDouble(values[i]);
            }
            return array;
        }

        if (type == Double.class) {
            Double[] array = new Double[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = toDoubleObj(values[i]);
            }
            return array;
        }

        if (type == float.class) {
            float[] array = new float[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = toFloat(values[i]);
            }
            return array;
        }

        if (type == Float.class) {
            Float[] array = new Float[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = toFloatObj(values[i]);
            }
            return array;
        }

        if (type == short.class) {
            short[] array = new short[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = toShort(values[i]);
            }
            return array;
        }

        if (type == Short.class) {
            Short[] array = new Short[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = toShortObj(values[i]);
            }
            return array;
        }

        if (type == char.class) {
            char[] array = new char[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = toChar(values[i]);
            }
            return array;
        }

        if (type == Character.class) {
            Character[] array = new Character[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = toCharacterObj(values[i]);
            }
            return array;
        }

        if (type == byte.class) {
            byte[] array = new byte[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = toByte(values[i]);
            }
            return array;
        }

        if (type == Byte.class) {
            Byte[] array = new Byte[values.length];
            for (int i = 0; i < values.length; i++) {
                array[i] = toByteObj(values[i]);
            }
            return array;
        }

        return null;
    }

    public static Object toType(Class type, String value) {
        if (type == String.class) {
            return value;

        }



        if (type == int.class) {
            return toInt(value);
        }



        if (type == Integer.class) {
            return toIntegerObj(value);
        }



        if (type == boolean.class) {
            return toBoolean(value);
        }



        if (type == Boolean.class) {
            return toBooleanObj(value);
        }



        if (type == long.class) {
            return toLong(value);
        }



        if (type == Long.class) {
            return toLongObj(value);
        }



        if (type == double.class) {
            return toDouble(value);
        }



        if (type == Double.class) {
            return toDoubleObj(value);
        }



        if (type == float.class) {
            return toFloat(value);
        }



        if (type == Float.class) {
            return toFloatObj(value);
        }



        if (type == short.class) {
            return toShort(value);
        }



        if (type == Short.class) {
            return toShortObj(value);
        }



        if (type == char.class) {
            return toChar(value);
        }



        if (type == Character.class) {
            return toCharacterObj(value);
        }



        if (type == byte.class) {
            return toByte(value);
        }



        if (type == Byte.class) {
            return toByteObj(value);
        }

        return null;
    }

    public static void main(String args[]) {
        System.out.println("toBigInteger:" + toBigInteger("89 89 89 "));
        System.out.println("toBigDecimal:" + toBigDecimal(" 90.88"));
        System.out.println("toInt:" + toInt(""));
        System.out.println("toIntegerObj:" + toIntegerObj("9"));
        System.out.println("toIntegerObj:" + toIntegerObj("", 0));

        System.out.println("toByte:" + toByte("9"));
        System.out.println("toByteObj:" + toByteObj("9"));
        byte b = 127;
        System.out.println("toByteObj:" + toByteObj("128", b));

        System.out.println("toBooleanObj(false):" + toBooleanObj("false"));
        System.out.println("toBooleanObj(true):" + toBooleanObj("true"));
        System.out.println("toBooleanObj():" + toBooleanObj(""));


    }
}
