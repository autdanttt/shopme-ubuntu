package com.shopme.setting;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingBag;

import java.util.List;

public class PaymentSettingBag extends SettingBag {
    public PaymentSettingBag(List<Setting> listSettings) {
        super(listSettings);
    }

    public String getURL(){
        return super.getValue("PAYPAL_API_BASE_URL");
    }
    public String getClientID(){
        return super.getValue("PAYPAL_API_CLIENT_ID");
    }
    public String getClientSecret(){
        return super.getValue("PAYPAL_API_CLIENT_SECRET");
    }
}
