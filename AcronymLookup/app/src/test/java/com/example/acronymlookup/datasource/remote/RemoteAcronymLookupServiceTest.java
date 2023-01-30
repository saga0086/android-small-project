/*
 * Copyright 2023
 * Author: Wei
 */

package com.example.acronymlookup.datasource.remote;

import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import com.example.acronymlookup.datamodel.LookupData;
import com.example.acronymlookup.datasource.LookupListener;

public class RemoteAcronymLookupServiceTest {
    private final Retrofit client = Mockito.mock(Retrofit.class);

    @Test
    public void testLookup() {
        final LookupApiService apiService = Mockito.mock(LookupApiService.class);
        final String acronym = "testacronym";
        final RemoteAcronymLookupService service = new RemoteAcronymLookupService();
        service.setRetrofit(client);
        final Call<List<LookupData>> call = Mockito.mock(Call.class);
        Mockito.when(client.create(LookupApiService.class)).thenReturn(apiService);
        Mockito.when(apiService.getFullForm(ArgumentMatchers.eq(acronym))).thenReturn(call);

        service.lookup(acronym, Mockito.mock(LookupListener.class));
        Mockito.verify(client).create(LookupApiService.class);
        Mockito.verify(apiService).getFullForm(ArgumentMatchers.eq(acronym));
        Mockito.verify(call).enqueue(ArgumentMatchers.isA(Callback.class));
    }
}
