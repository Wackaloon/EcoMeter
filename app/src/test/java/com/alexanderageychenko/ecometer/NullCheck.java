package com.alexanderageychenko.ecometer;

import com.alexanderageychenko.ecometer.Tools.annotation.AllowedNull;
import com.alexanderageychenko.ecometer.Tools.annotation.IgnoreNullCheck;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;


/**
 * Created by Alexander on 01.05.2017.
 */

public class NullCheck {

    private static boolean checkNullable(Field f) {
        Annotation[] annotations = f.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(AllowedNull.class)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkIfMustBeIgnored(Field f) {
        Annotation[] annotations = f.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(IgnoreNullCheck.class)) {
                return true;
            }
        }
        return false;
    }

    static public boolean check(Object object, String name) {
        boolean fail = false;
        for (Field f : object.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (!f.getName().contains("this"))
                    if (f.get(object) == null) {
                        if (!checkNullable(f)) {
                            System.out.println(AnsiColor.ANSI_RED + name + "." + f.getName() + "==null" + AnsiColor.ANSI_RESET);
                            fail = true;
                        } else {
                            System.out.println(name + "." + f.getName() + "==null");
                        }
                    } else {
                        System.out.println(name + "." + f.getName() + "==" + f.getGenericType().toString());
                        if (f.getGenericType().toString().contains("com.alexanderageychenko.ecometer.Model.Entity")) {
                            boolean lFail = false;
                            if (!checkIfMustBeIgnored(f)) {
                                lFail = check(f.get(object), name + "." + f.getName());
                            }
                            if (lFail)
                                fail = true;
                        } else if (f.getGenericType() instanceof Collection) {
                            int i = 0;
                            for (Object o : ((Collection) f.get(object))) {
                                if (f.getGenericType().toString().contains("com.alexanderageychenko.ecometer.Model.Entity")) {
                                    boolean lFail = false;
                                    if (!checkIfMustBeIgnored(f)) {
                                        lFail = check(f.get(object), name + "." + f.getName());
                                    }
                                    if (lFail)
                                        fail = true;
                                }
                            }
                        }
                    }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                fail = true;
            }
        }

        return fail;
    }

}
