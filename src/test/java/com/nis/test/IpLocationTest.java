package com.nis.test;/**
 * Created by dell on 2018-10-18.
 */

import com.alibaba.druid.sql.parser.SymbolTable;
import com.zdjizhi.utils.IpLookup;
import org.junit.Assert;
import org.junit.Test;

/**
 * @ClassName IpLocationTest
 * @Description TODO
 * @Author dell
 * @Date 2018-10-18 11:56
 * @Version 1.0
 **/
public class IpLocationTest {

    @Test
    public void testCountryLookup() {
        IpLookup ipLookup = new IpLookup.Builder(true).build();


        System.out.println( ipLookup.countryLookup("8.8.8.8"));

    }
}
