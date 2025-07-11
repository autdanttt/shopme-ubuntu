package com.shopme.setting;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface SettingRepository extends CrudRepository<Setting, Integer> {
    public List<Setting> findByCategory(SettingCategory category);
    @Query("SELECT s FROM Setting s WHERE s.category = ?1 OR s.category = ?2")
    public List<Setting> findByTwoCategories(SettingCategory catOne, SettingCategory catTwo);

    public Setting findByKey(String key);
}
