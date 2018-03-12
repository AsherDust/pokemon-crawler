package com.noash.poke.dao;

import com.noash.poke.domain.ItemAccess;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemAccessDaoTest {

    @Autowired
    private ItemAccessDao itemAccessDao;

    @Test
    @Transactional
    public void insert() throws Exception {
        ItemAccess itemAccess = new ItemAccess();
        itemAccess.setItemId(645);
        itemAccess.setType("购买");
        itemAccess.setAccess("皇家巨蛋内第1个服务员处100BP兑换");

        itemAccessDao.insert(itemAccess);
    }
}
