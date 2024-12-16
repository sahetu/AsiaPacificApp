package asiapacific.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryStateCityActivity extends AppCompatActivity {

    Spinner country,state,city;

    ArrayList<String> countryIdArray;
    ArrayList<String> countryNameArray;

    ArrayList<String> stateIdArray;
    ArrayList<String> stateNameArray;

    ArrayList<String> cityIdArray;
    ArrayList<String> cityNameArray;

    ApiInterface apiInterface;
    ProgressDialog pd;

    String sCountryId,sCountryName,sStateId,sStateName,sCityId,sCityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_state_city);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        country = findViewById(R.id.counrty_spinner);
        state = findViewById(R.id.state_spinner);
        city = findViewById(R.id.city_spinner);

        if(new ConnectionDetector(CountryStateCityActivity.this).networkConnected()){
            pd = new ProgressDialog(CountryStateCityActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            getCountryData();
        }
        else{
            new ConnectionDetector(CountryStateCityActivity.this).networkDisconnected();
        }

    }

    private void getCountryData() {
        Call<GetCountryData> call = apiInterface.getCountryData();
        call.enqueue(new Callback<GetCountryData>() {
            @Override
            public void onResponse(Call<GetCountryData> call, Response<GetCountryData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status){
                        countryIdArray = new ArrayList<>();
                        countryNameArray = new ArrayList<>();
                        for(int i=0;i<response.body().countryData.size();i++){
                            countryIdArray.add(response.body().countryData.get(i).id);
                            countryNameArray.add(response.body().countryData.get(i).name);
                        }
                        ArrayAdapter adapter = new ArrayAdapter(CountryStateCityActivity.this, android.R.layout.simple_list_item_1,countryNameArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
                        country.setAdapter(adapter);

                        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                sCountryId = countryIdArray.get(i);
                                sCountryName = countryNameArray.get(i);
                                if(new ConnectionDetector(CountryStateCityActivity.this).networkConnected()){
                                    getStateData();
                                }
                                else{
                                    new ConnectionDetector(CountryStateCityActivity.this).networkDisconnected();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }
                    else{
                        new ToastCommonMethod(CountryStateCityActivity.this,response.body().message);
                    }
                }
                else{
                    new ToastCommonMethod(CountryStateCityActivity.this,"Server Error Code : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetCountryData> call, Throwable t) {
                pd.dismiss();
                new ToastCommonMethod(CountryStateCityActivity.this,t.getMessage());
            }
        });
    }

    private void getStateData() {
        Call<GetStateData> call = apiInterface.getStateData(sCountryId);
        call.enqueue(new Callback<GetStateData>() {
            @Override
            public void onResponse(Call<GetStateData> call, Response<GetStateData> response) {
                if(response.code()==200){
                    if(response.body().status){
                        stateIdArray = new ArrayList<>();
                        stateNameArray = new ArrayList<>();
                        for(int i=0;i<response.body().stateData.size();i++){
                            stateIdArray.add(response.body().stateData.get(i).id);
                            stateNameArray.add(response.body().stateData.get(i).name);
                        }
                        ArrayAdapter adapter = new ArrayAdapter(CountryStateCityActivity.this, android.R.layout.simple_list_item_1,stateNameArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
                        state.setAdapter(adapter);

                        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                sStateId = stateIdArray.get(i);
                                sStateName = stateNameArray.get(i);
                                if(new ConnectionDetector(CountryStateCityActivity.this).networkConnected()){
                                    getCityData();
                                }
                                else{
                                    new ConnectionDetector(CountryStateCityActivity.this).networkDisconnected();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }
                    else{
                        new ToastCommonMethod(CountryStateCityActivity.this,response.body().message);
                    }
                }
                else{
                    new ToastCommonMethod(CountryStateCityActivity.this,"Server Error Code : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetStateData> call, Throwable t) {
                new ToastCommonMethod(CountryStateCityActivity.this,t.getMessage());
            }
        });
    }

    private void getCityData() {
        Call<GetCityData> call = apiInterface.getCityData(sStateId);
        call.enqueue(new Callback<GetCityData>() {
            @Override
            public void onResponse(Call<GetCityData> call, Response<GetCityData> response) {
                if(response.code()==200){
                    if(response.body().status){
                        cityIdArray = new ArrayList<>();
                        cityNameArray = new ArrayList<>();
                        for(int i=0;i<response.body().cityData.size();i++){
                            cityIdArray.add(response.body().cityData.get(i).id);
                            cityNameArray.add(response.body().cityData.get(i).name);
                        }
                        ArrayAdapter adapter = new ArrayAdapter(CountryStateCityActivity.this, android.R.layout.simple_list_item_1,cityNameArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
                        city.setAdapter(adapter);

                        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                sCityId = cityIdArray.get(i);
                                sCityName = cityNameArray.get(i);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }
                    else{
                        new ToastCommonMethod(CountryStateCityActivity.this,response.body().message);
                    }
                }
                else{
                    new ToastCommonMethod(CountryStateCityActivity.this,"Server Error Code : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetCityData> call, Throwable t) {
                new ToastCommonMethod(CountryStateCityActivity.this,t.getMessage());
            }
        });
    }

}