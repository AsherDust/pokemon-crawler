package com.noash.poke.dao;

import com.noash.poke.domain.PokedexId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PokedexIdDaoTest {
    @Autowired
    private PokedexIdDao pokedexIdDao;

    @Test
    @Transactional
    public void insert() throws Exception {
        PokedexId pokedexId = new PokedexId(999,999,999,999,999,999,999,999,999,999,999);
        pokedexIdDao.insert(pokedexId);
    }
}
