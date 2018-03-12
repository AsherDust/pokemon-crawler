package com.noash.poke.dao;

import com.noash.poke.domain.Ability;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AbilityDaoTest {

    @Autowired
    private AbilityDao abilityDao;

    @Test
    @Transactional
    public void insert() throws Exception {
        Ability ability = new Ability();
        ability.setId(9999);
        ability.setNameZh("加速");
        ability.setNameEn("Speed Boost");
        ability.setNameJp("かそく");
        ability.setGeneration(3);
        ability.setBriefDescr("每一回合速度会变快");
        ability.setEffectDescr("每回合结束时自身速度等级+1");

        abilityDao.insert(ability);
    }
}
