package com.shopme.admin.setting;

import com.shopme.common.entity.GeneralSettingBag;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {

    @Autowired
    private SettingRepository repo;

    public List<Setting> listAllSettings(){
        return (List<Setting>) repo.findAll();
    }


    public GeneralSettingBag getGeneralSettings(){
        List<Setting> settings = new ArrayList<>();
        List<Setting> generalSettings = repo.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings = repo.findByCategory(SettingCategory.CURRENCY);

        settings.addAll(generalSettings);
        settings.addAll(currencySettings);
        return new GeneralSettingBag(settings);
    }

    public void saveAll(Iterable<Setting> settings){
        repo.saveAll(settings);
    }


    public List<Setting> getMailServerSettings(){
        return repo.findByCategory(SettingCategory.MAIL_SERVER);
    }

    public List<Setting> getMailTemplateSettings(){
        return repo.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }

    public List<Setting> getCurrencySetting() {
        return repo.findByCategory(SettingCategory.CURRENCY);
    }

    public List<Setting> getPaymentSetting(){
        return repo.findByCategory(SettingCategory.PAYMENT);
    }
}
