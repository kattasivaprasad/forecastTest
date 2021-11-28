package api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.City;
import model.Forecast;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ForecastServiceImplTest {

    @InjectMocks
    ForecastServiceImpl ForecastServiceImpl;

    @Mock
    Retrofit retrofit;

    @Mock
    ForecastService service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getForecastResponseValidationTest() throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String cityName = "Dubai";
            Mockito.when(retrofit.create(any())).thenReturn(service);
            Call<List<Forecast>> mockedListForeCast = Mockito.mock(Call.class);
            Call<List<City>> mockedListCity = Mockito.mock(Call.class);
            Mockito.when(service.findCityByName(anyString())).thenReturn(mockedListCity);
            Mockito.when(service.getForecast(anyLong(), any())).thenReturn(mockedListForeCast);
            Mockito.when(mockedListCity.execute()).thenReturn(Response.success(returnCityDetails(cityName)));
            Mockito.when(mockedListForeCast.execute()).thenReturn(Response.success(returnCityForecast()));
            String forecastCall = ForecastServiceImpl.getForecast(cityName, LocalDate.now().plusDays(1));
            Assert.assertEquals("Response Parameters are not matching", forecastCall, String.format("Weather on (%s) in %s:\n%s", LocalDate.now().plusDays(1).toString().replaceAll("-", "/"), cityName, returnCityForecast().get(0)));
    }

    @Test
    public void getForecastExceptionScenarioTest() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String cityName = "Dubai";
        Mockito.when(retrofit.create(any())).thenReturn(service);
        Call<List<Forecast>> mockedListForeCast = Mockito.mock(Call.class);
        Call<List<City>> mockedListCity = Mockito.mock(Call.class);
        Mockito.when(service.findCityByName(anyString())).thenReturn(mockedListCity);
        Mockito.when(service.getForecast(anyLong(), any())).thenReturn(mockedListForeCast);
        Mockito.when(mockedListCity.execute()).thenReturn(Response.success(returnCityDetails(cityName)));
        List<Forecast> forecastList1 = new ArrayList<>();
        Forecast forecast = new Forecast();
        forecastList1.add(forecast);
        Mockito.when(mockedListForeCast.execute()).thenReturn(Response.success(forecastList1));
        String forecastCall2 = ForecastServiceImpl.getForecast(cityName, LocalDate.now().plusDays(1));
        Assert.assertEquals("Response Parameters are not matching", forecastCall2, String.format("Weather on (%s) in %s:\n%s", LocalDate.now().plusDays(1).toString().replaceAll("-", "/"), cityName, forecastList1.get(0)));
    }

    private List<Forecast> returnCityForecast() {
        List<Forecast> forecastList = new ArrayList<>();
        Forecast forecast = new Forecast();
        forecast.setHumidity(73);
        forecast.setId(44418L);
        forecast.setTemperature(3.9);
        forecast.setWeatherState("Heavy Cloud");
        forecast.setWindSpeed(4.8);
        forecastList.add(forecast);

        return forecastList;
    }

    private List<City> returnCityDetails(String cityName) {
        List<City> cityList = new ArrayList<>();
        City city = new City();
        city.setTitle(cityName);
        city.setWoeid(44418L);
        cityList.add(city);
        return cityList;
    }

    @Getter
    @AllArgsConstructor
    public enum CityNames {
        LONDON("London"),
        ABUDHABI("Abu Dhabi"),
        DUBAI("Dubai");

        private String value;
    }

}
