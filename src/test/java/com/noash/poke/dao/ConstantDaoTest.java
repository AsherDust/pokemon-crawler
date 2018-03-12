package com.noash.poke.dao;

import com.noash.poke.domain.Constant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConstantDaoTest {

    @Autowired
    private ConstantDao constantDao;

    @Test
    @Transactional
    public void insert() throws Exception {
        Constant constant = new Constant();
        constant.setTypeName("test");
        constant.setCode(0);
        constant.setValue("nksnd耐烦");

        constantDao.insert(constant);
    }
}
