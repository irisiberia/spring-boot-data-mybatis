package com.example.springbootdatamybatis.mozart.common.mosaic;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;


import java.util.List;

/**
 * @Author he.zhou
 * @Date 2019-10-04 15:30
 **/
public class MosaicUtil { /**
 * 手机号隐藏中间4位
 *
 * @param mobilePhone
 * @return
 */
public static String hideMobile(String mobilePhone) {
    if (Strings.isNullOrEmpty(mobilePhone) || mobilePhone.length() <= 4) {
        return "********";
    }
    return mobilePhone.replaceAll("(\\d{3}).{4}(.*)", "$1****$2");
}

    /**
     * 身份证号隐藏中间11位，显示前4位，后3位
     *
     * @param idCardNo
     * @return
     */
    public static String hideCardNo(String idCardNo) {
        if (Strings.isNullOrEmpty(idCardNo) || idCardNo.length() <= 8) {
            return "********";
        }
        return idCardNo.replaceAll("(\\d{4})\\d{4}(.*)", "$1****$2");
    }

    /**
     * 隐藏从第四位至@前的内容
     *
     * @param emailAddress
     * @return
     */
    public static String hideEmail(String emailAddress) {
        if (emailAddress == null) {
            return null;
        }
        int atIdx = emailAddress.lastIndexOf('@');
        if (atIdx == -1 || atIdx <= 3)
            return emailAddress;
        StringBuilder sb = new StringBuilder(emailAddress.substring(0, 3));
        for (int i = 3; i < atIdx; i++) {
            sb.append('*');
        }
        return sb.append(emailAddress.substring(atIdx)).toString();
    }

    /***
     * 隐藏姓名，隐藏掉姓
     *
     * @param name
     * @return
     */
    public static String hideName(String name) {
        if (StringUtils.isEmpty(name) || name.length() < 2) {
            return name;
        }
        if (name.length() == 2) {
            return StringUtils.leftPad(name.substring(name.length() - 1), name.length(), '*');
        }
        return StringUtils.leftPad(name.substring(name.length() - 2), name.length(), '*');
    }

    /***
     * 批量隐藏姓名
     *
     * @param names
     * @return
     */
    public static List<String> hideName(List<String> names) {
        List<String> newGuestNames = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(names)) {
            for (String guestName : names) {
                newGuestNames.add(hideName(guestName));
            }
        }
        return newGuestNames;
    }

    public static void main(String[] args) {
        System.out.println(hideCardNo("000006671009"));
        System.out.println(hideMobile("17710231366"));
        System.out.println(hideName("勇"));
        System.out.println(hideName("陈勇勇"));
        System.out.println(hideName("陈哈哈哈勇"));
    }

}
