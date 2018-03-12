package com.noash.poke.dao;

import com.noash.poke.domain.PokedexId;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PokedexIdDao {

    String TABLE = "pokedex_id";
    String COLUMNS = "national_id, sm, sm_melemele, sm_akala, sm_ulaula, sm_poni, usum, usum_melemele, usum_akala, usum_ulaula, usum_poni";

    @Insert({
        "INSERT INTO", TABLE,
        "(", COLUMNS, ")",
        "VALUES",
        "(#{nationalId}, #{sm}, #{smMelemele}, #{smAkala}, #{smUlaula}, #{smPoni},",
        "#{usum}, #{usumMelemele}, #{usumAkala}, #{usumUlaula}, #{usumPoni})"
    })
    int insert(PokedexId pokedexId);

}
