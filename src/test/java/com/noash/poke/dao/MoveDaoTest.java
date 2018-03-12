package com.noash.poke.dao;

import com.noash.poke.domain.Move;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MoveDaoTest {

    @Autowired
    private MoveDao moveDao;

    @Test
    @Transactional
    public void insert() throws Exception {
        Move move = new Move();
        move.setId(9999);
        move.setNameZh("大晴天");
        move.setNameEn("Sunny Day");
        move.setNameJp("にほんばれ");
        move.setTypeId(2);
        move.setClazz(0);
        move.setPower(0);
        move.setAccuracy(101);
        move.setPp(5);
        move.setPriority(0);
        move.setZPower(0);
        move.setZEffect(9);
        move.setTarget(7);
        move.setTags("000000000000000000000");
        move.setDescr("在５回合内阳光变得强烈，从而提高火属性的招式威力。水属性的招式威力则降低。");

        moveDao.insert(move);
    }
}
