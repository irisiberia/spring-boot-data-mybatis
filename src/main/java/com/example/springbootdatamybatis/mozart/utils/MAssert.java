package com.example.springbootdatamybatis.mozart.utils;

/**
 * @Author he.zhou
 * @Date 2019-10-04 18:06
 **/

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by fan.tang on 2017/6/28.
 */
public class MAssert {
    public static void checkArgument(boolean expression, String errMsg) {
        if (!expression) {
            throw new StatusCodeException(Status.error(errMsg));
        }
    }

    public static void isTrue(boolean expression) {
        if (!expression) {
            throw new StatusCodeException(Status.error("参数校验失败"));
        }
    }

    public static void isTrue(boolean expression, ErrorMessage errorMsg) {
        if (!expression) {
            throw new StatusCodeException(Status.error(errorMsg.getCode(), errorMsg.getErrMsg()));
        }
    }

    public static void isTrue(boolean expression, String errorMsg) {
        if (!expression) {
            throw new StatusCodeException(Status.error(errorMsg));
        }
    }

    public static <T> T notNull(T object) {
        if (object == null) {
            throw new StatusCodeException(Status.error("参数不能为null"));
        } else {
            return object;
        }
    }

    public static <T> T notNull(T object, ErrorMessage errorMsg) {
        if (object == null) {
            throw new StatusCodeException(Status.error(errorMsg.getCode(), errorMsg.getErrMsg()));
        } else {
            return object;
        }
    }

    public static <T> T notNull(T object, String errorMsg) {
        if (object == null) {
            throw new StatusCodeException(Status.error(errorMsg));
        } else {
            return object;
        }
    }

    public static void hasLength(String text) {
        if (!StringUtils.hasLength(text)) {
            throw new StatusCodeException(Status.error("参数不能为空串"));
        }
    }

    public static void hasLength(String text, ErrorMessage errorMsg) {
        if (!StringUtils.hasLength(text)) {
            throw new StatusCodeException(Status.error(errorMsg.getCode(), errorMsg.getErrMsg()));
        }
    }

    public static void hasLength(String text, String errorMsg) {
        if (!StringUtils.hasLength(text)) {
            throw new StatusCodeException(Status.error(errorMsg));
        }
    }

    public static void notEmpty(Object[] array) {
        if (ObjectUtils.isEmpty(array)) {
            throw new StatusCodeException(Status.error("数组不能为空"));
        }
    }

    public static void notEmpty(Object[] array, ErrorMessage errorMsg) {
        if (ObjectUtils.isEmpty(array)) {
            throw new StatusCodeException(Status.error(errorMsg.getCode(), errorMsg.getErrMsg()));
        }
    }

    public static void notEmpty(Object[] array, String errorMsg) {
        if (ObjectUtils.isEmpty(array)) {
            throw new StatusCodeException(Status.error(errorMsg));
        }
    }

    public static void notEmpty(Collection collection) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new StatusCodeException(Status.error("集合不能为空"));
        }
    }

    public static void notEmpty(Collection collection, ErrorMessage errorMsg) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new StatusCodeException(Status.error(errorMsg.getCode(), errorMsg.getErrMsg()));
        }
    }

    public static void notEmpty(Collection collection, String errorMsg) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new StatusCodeException(Status.error(errorMsg));
        }
    }

    public static void notEmpty(Map map) {
        if (CollectionUtils.isEmpty(map)) {
            throw new StatusCodeException(Status.error("Map不能为空"));
        }
    }

    public static void notEmpty(Map map, ErrorMessage errorMsg) {
        if (CollectionUtils.isEmpty(map)) {
            throw new StatusCodeException(Status.error(errorMsg.getCode(), errorMsg.getErrMsg()));
        }
    }

    public static void notEmpty(Map map, String errorMsg) {
        if (CollectionUtils.isEmpty(map)) {
            throw new StatusCodeException(errorMsg, Status.error(errorMsg));
        }
    }
}
