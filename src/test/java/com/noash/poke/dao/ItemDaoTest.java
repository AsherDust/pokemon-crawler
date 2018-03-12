package com.noash.poke.dao;

import com.noash.poke.domain.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemDaoTest {

    @Autowired
    private ItemDao itemDao;

    @Test
    @Transactional
    public void insert() throws Exception {
        Item item = new Item();
        item.setId(9999);
        item.setNameZh("招式学习器０１");
        item.setNameEn("TM01");
        item.setNameJp("わざマシン０１");
        item.setType(6);
        item.setPrice(10000);
        item.setIsUsable(false);
        item.setIsDisposable(false);
        item.setTmMoveId(526);
        item.setDescr("激励自己，从而提高攻击和特攻。");

        itemDao.insert(item);
    }
}
