package com.noash.poke.dao;

import com.noash.poke.domain.EvolutionChain;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EvolutionChainDaoTest {

    @Autowired
    private EvolutionChainDao evolutionChainDao;

    @Test
    @Transactional
    public void insert() throws Exception {
        EvolutionChain evolutionChain = new EvolutionChain();
        evolutionChain.setBaseId(9997);
        evolutionChain.setSecondId(9998);
        evolutionChain.setThirdId(9999);
        evolutionChain.setSecondDescr("777");
        evolutionChain.setThirdDescr("666");

        evolutionChainDao.insert(evolutionChain);
    }
}
