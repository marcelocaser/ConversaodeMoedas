package com.example.conversaodemoedas.business.interfaces;

import com.example.conversaodemoedas.domain.CurrencyQuoteDO;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("json/last/USD-BRL")
    Call<CurrencyQuoteDO> getCotacaoDolarParaReal();
}
