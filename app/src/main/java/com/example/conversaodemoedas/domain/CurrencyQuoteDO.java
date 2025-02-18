package com.example.conversaodemoedas.domain;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyQuoteDO {

    @SerializedName("USDBRL")
    private COINS usdbrl;

    @SerializedName("EURBRL")
    private COINS eurbrl;

    @Getter
    @Setter
    public class COINS {
        private String code;
        @SerializedName("codeIn")
        private String codein;
        private String name;
        private String high;
        private String low;
        private String varBid;
        private String pctChange;
        private String bid;
        private String ask;
        private String timestamp;
        @SerializedName("create_date")
        private String createDate;
    }

}
